/*
 * Created: 2016年5月31日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.oss;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.RequestMessage;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.aliyun.oss.common.utils.HttpUtil;
import com.aliyun.oss.internal.OSSConstants;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.internal.RequestParameters;
import com.aliyun.oss.internal.SignUtils;
import com.aliyun.oss.model.Grant;
import com.aliyun.oss.model.GroupGrantee;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.Permission;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.net.UrlEscapers;
import com.ykyframework.oss.AliyunOSSOperator.AliyunOSSAccessType;

import net.sf.json.JSONObject;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSHelper {

	private static final String OSS_SIGNATURE_FAILED = "Get OSS signature failed.";

	public static final String KEY_SCHEME = "SCHEME";
	
	public static final String KEY_HOST = "host";
	
	public static final String KEY_OBJECT = "key";

	public static final String KEY_BUCKET = "bucket";

	public static final String KEY_ENDPOINT = "endpoint";

	private static final Logger logger = LoggerFactory.getLogger(AliyunOSSHelper.class);

	protected static final long EXPIRE_SECONDS = 30L;

	public static final String STS_API_VERSION = "2015-04-01";

	/*
	 * 访问阿里云的ACS身份有效时间
	 */
	private static final long ACS_MAX_DURATION_SECONDS = 3600;

	private static Cache<String, Boolean> pubReadCache = CacheBuilder.newBuilder().maximumSize(16)
			.expireAfterWrite(3600, TimeUnit.SECONDS).build();

	/**
	 * @author liudian@yikuyi.com
	 * @version 1.0.0
	 */
	private static final class CachedOSSClientRemovalListener implements RemovalListener<String, OSSClient> {
		@Override
		public void onRemoval(RemovalNotification<String, OSSClient> notification) {
			notification.getValue().shutdown();
			logger.info("OSSClient in cache with key({}) is moved and shutdown.", notification.getKey());
		}
	}

	/*
	 * 缓存访问OSS的临时身份，半小时有效（默认的身份有效时间为3600秒）
	 */
	private static Cache<String, OSSClient> ossClientCache = CacheBuilder.newBuilder().maximumSize(32)
			.expireAfterWrite(30, TimeUnit.MINUTES).removalListener(new CachedOSSClientRemovalListener()).build();

	private AliyunOSSHelper() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Generate the policy & signature for uploading file to aliOSS bucket.
	 * 
	 * @param bucket
	 *            The bucket to upload
	 * @param expireSeconds
	 *            expire time in seconds for the post request
	 * @param charsetName
	 * @param isImage
	 *            whether a image or not
	 * @param dir
	 *            the directory that the file to upload
	 * @param name
	 *            the key to upload/access the file
	 * @return A map to return the necessary informations, contains { accessid:
	 *         the accesskeyid for the OSS access account, policy: the policy of
	 *         the post request, signature: signature of the post reqeust, host:
	 *         the host post to, expire: expire time in TimeMillis type, key:
	 *         the full access key to the object, expiration: the expire time in
	 *         Rfc822 type }
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	protected static Map<String, String> getPostSignature(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			String bucket, String key, long expireSeconds, boolean isImage) {
		Map<String, String> respMap = new LinkedHashMap<>();
		OSSClient client = null;
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			String hostNoProtocol = bucket + "." + client.getEndpoint().getHost();
			String host = "http://" + hostNoProtocol;
			String postHost = "https://" + hostNoProtocol;
			Credentials credentials = client.getCredentialsProvider().getCredentials();
			String accessId = credentials.getAccessKeyId();
			String securityToken = credentials.getSecurityToken();
			long expireEndTime = System.currentTimeMillis() + expireSeconds * 1000;
			Date expiration = new Date(expireEndTime);
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);

			String dir = trimDir("/");
			String name = trimKey(key);

			// 如果name为空，或者key不合法，生成一个uuid为key，否则以给定name为key
			if (StringUtils.isBlank(name) || !OSSUtils.validateObjectKey(name)) {
				String uuid = UUID.randomUUID().toString();
				String extension = FilenameUtils.getExtension(name);
				name = trimKey(DigestUtils.md5Hex(uuid)) + (StringUtils.isEmpty(extension) ? "" : "." + extension);
			}
			// 将key进行编码
			String rKey = dir + name;

			//只有操作当前文件的权限
			policyConds.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, rKey);

			String postPolicy = client.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("UTF-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = client.calculatePostSignature(postPolicy);

			respMap.put("hostNoProtocol", hostNoProtocol);
			respMap.put("accessKeyid", accessId);
			respMap.put("OSSAccessKeyId", accessId);
			respMap.put("policy", encodedPolicy);
			respMap.put("signature", postSignature);
			respMap.put("Signature", postSignature);
			respMap.put("host", host);
			respMap.put("posthost", postHost);
			respMap.put("expire", String.valueOf(expireEndTime / 1000));
			respMap.put(KEY_BUCKET, bucket);
			respMap.put(KEY_OBJECT, rKey);
			respMap.put("expiration", DateUtil.formatRfc822Date(expiration));
			respMap.put("securityToken", securityToken);
			respMap.put(OSSHeaders.OSS_SECURITY_TOKEN, securityToken);

			String authorization = "OSS " + accessId + ":" + postSignature;
			respMap.put("authorization", authorization);
			respMap.put(OSSHeaders.DATE, DateUtil.formatRfc822Date(new Date()));

			String url = getAccessUrl(client, "https", bucket, key, isImage, expireSeconds, AliyunOSSDomainMapper.getDomains(bucket));
			respMap.put("accessUrl", url);
			Map<String, String> hostMap = parseOSSObjectUrl(url);
			respMap.put("accessDomain", hostMap.get("host"));
			respMap.put("url", host + "/" + rKey);
			if (isImage) {
				//如果是图片，获取image的访问url和缩略图的url
				respMap.put("image", url);
				respMap.put("imageUrl", host + "/" + rKey);
				String thumbnailAccessUrl = getAccessUrl(client, "https", bucket, key + ThumbnailsSuffix.THUMBNAIL_SUFFIX_32.suffix, true, expireSeconds,  AliyunOSSDomainMapper.getDomains(bucket));
				respMap.put("thumbnail", thumbnailAccessUrl);
				respMap.put("thumbnailUrl", host + "/" + rKey + ThumbnailsSuffix.THUMBNAIL_SUFFIX_32.suffix);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Signature generated: {}", respMap);
			}

		} catch (ClientException | OSSException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
		} catch (UnsupportedEncodingException | ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return respMap;
	}

	/**
	 * 生成put文件的url，url带签名和认证数据
	 * 
	 * @param account
	 * @param endpoint
	 * @param bucket
	 * @param key
	 * @param expireSeconds
	 * @return
	 * @since 2016年6月15日
	 * @author liudian@yikuyi.com
	 */
	static String getPutUrl(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, String bucket, String key,
			long expireSeconds) {
		OSSClient client = null;
		String url = null;
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			long expireEndTime = System.currentTimeMillis() + expireSeconds * 1000;
			Date expiration = new Date(expireEndTime);

			String dir = trimDir("/");
			String name = trimKey(key);
			String rKey = dir + name;
			URL putUrl = client.generatePresignedUrl(bucket, rKey, expiration, HttpMethod.PUT);
			url = putUrl.toString();

			logger.debug("Puturl generated: {}", url);

		} catch (ClientException | OSSException e) {
			// 客户端异常，并将客户端从cache移除并关闭
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			logger.error(OSS_SIGNATURE_FAILED, e);
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return url;
	}

	/**
	 * 生成put的的相关签名和认证
	 * 
	 * @param account
	 * @param endpoint
	 * @param bucket
	 * @param key
	 * @param expireSeconds
	 * @return
	 * @since 2016年6月15日
	 * @author liudian@yikuyi.com
	 */
	static Map<String, String> getPutSignature(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			String bucket, String key, long expireSeconds) {
		OSSClient client = null;
		Map<String, String> respMap = new LinkedHashMap<>();
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			long expireEndTime = System.currentTimeMillis() + expireSeconds * 1000;
			Date expiration = new Date(expireEndTime);

			String dir = trimDir("/");
			String name = trimKey(key);

			URL putUrl = client.generatePresignedUrl(bucket, dir + name, expiration, HttpMethod.PUT);
			String url = putUrl.toString();
			respMap.put("puturl", url);

			logger.debug("Puturl generated: {}", url);

		} catch (ClientException | OSSException e) {
			// 客户端异常，并将客户端从cache移除并关闭
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			logger.error(OSS_SIGNATURE_FAILED, e);
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return respMap;
	}

	private static void removeClient(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, AliyunOSSAccessType clientType) {
		String url = getEndpointUrl(endpoint, clientType);
		String ossClientCacheKey = getClientCacheKey(account, url);
		logger.info("Cached ossclient with key({}) has be removed.", ossClientCacheKey);
		ossClientCache.invalidate(ossClientCacheKey);
	}
	
	public static ImageUrls getImageUrl(AliyunOSSOperator ossOperator, String getUrl, long expireSeconds) {
		return getImageUrl(ossOperator.getAccount(), getUrl, expireSeconds, ThumbnailsSuffix.THUMBNAIL_SUFFIX_32, ossOperator.getClientAccessType());
	}
	
	public static ImageUrls getImageUrl(AliyunOSSAccount account, String getUrl, long expireSeconds) {
		return getImageUrl(account, getUrl, expireSeconds, ThumbnailsSuffix.THUMBNAIL_SUFFIX_32, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}
	
	public static ImageUrls getImageUrl(AliyunOSSAccount account, String getUrl, long expireSeconds, AliyunOSSAccessType accessType) {
		return getImageUrl(account, getUrl, expireSeconds, ThumbnailsSuffix.THUMBNAIL_SUFFIX_32, accessType);
	}
	
	public static Map<String, ImageUrls> getImageUrls(AliyunOSSAccount account,List<String> urls, long expireSeconds) {
		return getImageUrls(account, urls, expireSeconds, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}
	
	public static Map<String, ImageUrls> getImageUrls(AliyunOSSAccount account,List<String> urls, long expireSeconds, AliyunOSSAccessType accessType) {
		return getImageUrls(account, urls, expireSeconds, ThumbnailsSuffix.THUMBNAIL_SUFFIX_32, accessType);
	}
	
	public static Map<String, ImageUrls> getImageUrls(AliyunOSSOperator ossOperator ,List<String> urls, long expireSeconds) {
		return getImageUrls(ossOperator.getAccount(), urls, expireSeconds, ThumbnailsSuffix.THUMBNAIL_SUFFIX_32, ossOperator.getClientAccessType());
	}
	
	public static ImageUrls getImageUrl(AliyunOSSAccount account, String getUrl, long expireSeconds, ThumbnailsSuffix thumbnail) {
		return getImageUrl(account, getUrl, expireSeconds, thumbnail, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}

	public static ImageUrls getImageUrl(AliyunOSSAccount account, String getUrl, long expireSeconds, ThumbnailsSuffix thumbnail, AliyunOSSAccessType accessType) {
		Map<String, String> hostMap = parseOSSObjectUrl(getUrl);
		String key = hostMap.get(KEY_OBJECT);
		String bucket = hostMap.get(KEY_BUCKET);
		String endp = hostMap.get(KEY_ENDPOINT);
		String schema = hostMap.get(KEY_SCHEME);
		if (StringUtils.isNotBlank(endp)) {
			AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(KEY_ENDPOINT));
			OSSClient client = null;
			try {
				List<String> domains = AliyunOSSDomainMapper.getDomains(bucket);
				client = getClient(account, endpoint, accessType);
				String image = getAccessUrl(client, schema, bucket, key, true, expireSeconds, domains);
				String thumbnailUrl = getAccessUrl(client, schema, bucket, key + thumbnail.suffix, true, expireSeconds,
						domains);

				return new ImageUrls(image, thumbnailUrl);
			} catch (ClientException | OSSException e) {
				// 客户端异常，并将客户端从cache移除并关闭
				removeClient(account, endpoint, accessType);
				logger.error(OSS_SIGNATURE_FAILED, e);
			} catch (ExecutionException e) {
				logger.error("getImageUrl error", e);
			}
		}
		return null;
	}

	public static class ImageUrls {
		private String image;

		private String thumbnail;

		public ImageUrls(String image, String thumbnail) {
			this.image = image;
			this.thumbnail = thumbnail;
		}

		/**
		 * @return the image
		 */
		public final String getImage() {
			return image;
		}

		/**
		 * @param image
		 *            the image to set
		 */
		public final void setImage(String image) {
			this.image = image;
		}

		/**
		 * @return the thumbnail
		 */
		public final String getThumbnail() {
			return thumbnail;
		}

		/**
		 * @param thumbnail
		 *            the thumbnail to set
		 */
		public final void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		@Override
		public String toString() {
			return JSONObject.fromObject(this).toString();
		}
	}

	/**
	 * 批量获取图片的访问路径
	 * 
	 * @param account
	 * @param urls
	 * @param expireSeconds
	 * @param charsetName
	 * @param isImage
	 * @return
	 * @since 2016年6月15日
	 * @author liudian@yikuyi.com
	 */
	public static Map<String, ImageUrls> getImageUrls(AliyunOSSAccount account, List<String> urls, long expireSeconds, ThumbnailsSuffix thumbnail, AliyunOSSAccessType accessType) {
		Map<String, ImageUrls> retMap = new LinkedHashMap<>();
		for (String getUrl : urls) {
			AliyunOSSEndpoint endpoint = null;
			try {
				Map<String, String> hostMap = parseOSSObjectUrl(getUrl);
				String key = hostMap.get(KEY_OBJECT);
				String bucket = hostMap.get(KEY_BUCKET);
				String endp = hostMap.get(KEY_ENDPOINT);
				String schema = hostMap.get(KEY_SCHEME);
				endpoint = AliyunOSSEndpoint.valueOf(endp);
				OSSClient client = getClient(account, endpoint, accessType);
				List<String> domains = AliyunOSSDomainMapper.getDomains(bucket);
				String image = getAccessUrl(client, schema, bucket, key, true, expireSeconds, domains);
				String thumbnailUrl = getAccessUrl(client, schema, bucket,
						key + (thumbnail == null ? ThumbnailsSuffix.THUMBNAIL_SUFFIX_32 : thumbnail.suffix), true,
						expireSeconds, domains);
				ImageUrls imageUrls = new ImageUrls(image, thumbnailUrl);
				retMap.put(getUrl, imageUrls);
			} catch (ClientException | OSSException e) {
				logger.error(OSS_SIGNATURE_FAILED, e);
				// 客户端异常，并将客户端从cache移除并关闭
				removeClient(account, endpoint, accessType);
			} catch (Exception e) {
				logger.error(OSS_SIGNATURE_FAILED, e);
			}
		}
		return retMap;
	}
	
	public static String getAccessUrl(AliyunOSSAccount account, String getUrl, boolean isImage, long expireSeconds) {
		return getAccessUrl(account, getUrl, isImage, expireSeconds, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}
	
	public static String getAccessUrl(AliyunOSSAccount account, String getUrl, boolean isImage, long expireSeconds, AliyunOSSAccessType accessType) {
		Map<String, String> hostMap = AliyunOSSHelper.parseOSSObjectUrl(getUrl);
		String key = hostMap.get(AliyunOSSHelper.KEY_OBJECT);
		String bucket = hostMap.get(AliyunOSSHelper.KEY_BUCKET);
		String schema = hostMap.get(AliyunOSSHelper.KEY_SCHEME);
		AliyunOSSEndpoint endpoint = AliyunOSSEndpoint.valueOf(hostMap.get(AliyunOSSHelper.KEY_ENDPOINT));
		return getAccessUrl(account, endpoint, schema, bucket, key, isImage, expireSeconds, accessType);
	}

	/**
	 * Generate the access url for a aliOSS Object
	 * 
	 * @param bucket
	 *            bucket to access
	 * @param dir
	 *            the directory that the file to access
	 * @param name
	 *            access key to the file
	 * @param expireSeconds
	 *            expire time in seconds
	 * 
	 * @return
	 * @since 2016年6月1日
	 * @author liudian@yikuyi.com
	 */
	protected static String getAccessUrl(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, String schema, final String bucket,
			String key, boolean isImage, long expireSeconds, AliyunOSSAccessType accessType) {
		String retUrl = null;
		OSSClient client = null;
		String tKey = key;
		try {
			client = getClient(account, endpoint, accessType);
			retUrl = getAccessUrl(client, schema, bucket, tKey, isImage, expireSeconds, AliyunOSSDomainMapper.getDomains(bucket));
		} catch (ClientException | OSSException e) {
			// 客户端异常，并将客户端从cache移除并关闭
			removeClient(account, endpoint, accessType);
			logger.error(OSS_SIGNATURE_FAILED, e);
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return retUrl;
	}

	private static String getAccessUrl(final OSSClient client, final String schema, final String bucket, String key, boolean isImage,
			long expireSeconds, List<String> domains) {
		String retUrl;
		String dir = trimDir("/");
		String name = trimKey(key);
		name = dir + name;
		boolean isReadable = false;

		// 判断bucket的读写权限
		try {
			isReadable = pubReadCache.get(bucket, () -> {
				boolean retVal = false;
				Set<Grant> gants = client.getBucketAcl(bucket).getGrants();
				Iterator<Grant> iter = gants.iterator();
				while (iter.hasNext()) {
					Grant grant = iter.next();
					if (grant.getGrantee().equals(GroupGrantee.AllUsers)
							&& (Permission.Read.equals(grant.getPermission())
									|| Permission.FullControl.equals(grant.getPermission()))) {
						retVal = true;
						break;
					}
				}
				return retVal;
			});
		} catch (ExecutionException e) {
			logger.error("getAccessUrl error", e);
		}

		if (isReadable) {
			String host = getDomain(client, bucket, isImage, domains);
			String baseName = OSSUtils.determineResourcePath(host, key, true);
			retUrl = baseName;
		} else {
			long expireEndTime = System.currentTimeMillis() + expireSeconds * 1000;
			Date expiration = new Date(expireEndTime);
			URL getUrl = client.generatePresignedUrl(bucket, name, expiration);
			retUrl = getUrl.toString();
		}
		
		try {
			URIBuilder ub = new URIBuilder(retUrl);
			ub.setScheme(schema);
			URI getUri = ub.build();
			retUrl = getUri.toString();
		} catch (URISyntaxException e) {
			logger.warn("getAccessUrl {}, error", retUrl, e);
		}
		return retUrl;
	}

	private static final String getDomain(OSSClient client, String bucket, boolean isImage, List<String> domains) {
		String host = client.getEndpoint().getScheme() + "://" + bucket + "." + client.getEndpoint().getHost();
		if (CollectionUtils.isNotEmpty(domains)) {
			List<String> domainsLocal = filterDomains(isImage, domains);
			if (CollectionUtils.isNotEmpty(domainsLocal)) {
				int idx = RandomUtils.nextInt() % domainsLocal.size();
				host = client.getEndpoint().getScheme() + "://" + domainsLocal.get(idx);
			}
		}
		return host;
	}

	private static final List<String> filterDomains(final boolean isImage, List<String> domains) {
		final String imageDomain = AliyunOSSDomainMapper.IMAGE_DOMAIN_PREFIX + ":";
		List<String> dms;

		if (isImage) {
			// 判断是否有针对图片的域名映射规则（对应规则为：image:{domain}）
			// 如果有针对域名映射的规则则取出使用
			dms = domains.parallelStream().filter(d -> d.startsWith(imageDomain)).collect(Collectors.toList());
			if (CollectionUtils.isEmpty(dms)) {
				dms = new ArrayList<>();
				// 如果没有针对图片的域名映射，则域名映射针对所有文件有效
				dms.addAll(domains);
			}
		} else {
			// 取出非针对图片映射的部分
			dms = domains.parallelStream().filter(d -> !d.startsWith(imageDomain)).collect(Collectors.toList());
		}

		// 去除域名映射定义中的前缀（前缀以':'为结束）
		dms = dms.parallelStream()
				.map(d -> (null != d && StringUtils.contains(d, ":")) ? StringUtils.substringAfter(d, ":") : d)
				.collect(Collectors.toList());

		return dms;
	}

	protected static String putObject(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, final String bucket,
			String key, InputStream input) {
		OSSClient client = null;
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);

			String tKey = trimKey(key);
			client.putObject(bucket, tKey, input);
			return "http://" + bucket + "." + endpoint.getUrl() + "/" + tKey;
		} catch (ClientException | OSSException e) {
			// 客户端异常，并将客户端从cache移除并关闭
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			logger.error(OSS_SIGNATURE_FAILED, e);
			return null;
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
			return null;
		}
	}

	/**
	 * Get a object as inputstream from aliyun oss
	 * 
	 * @param account
	 *            the account to access aliyun oss
	 * @param endpoint
	 *            the endpoint that bucket store on
	 * @param bucket
	 *            the bucket name of aliyun oss
	 * @param objectKey
	 *            the object key of object(includes the directory and object
	 *            name)
	 * @return the inputstream fetch from aliyun oss
	 * @since 2016年11月10日
	 * @author liudian@yikuyi.com
	 */
	protected static InputStream getObject(AliyunOSSAccount account, AliyunOSSAccessType clientType,
			AliyunOSSEndpoint endpoint, final String bucket, String objectKey) {
		OSSClient client = null;
		try {
			client = getClient(account, endpoint, clientType);
			String key = trimKey(objectKey);
			OSSObject object = client.getObject(bucket, key);
			return object.getObjectContent();
		} catch (OSSException | ClientException e) {
			// 出现任何异常，将缓存中的client移除并关闭，下次重新获取
			removeClient(account, endpoint, clientType);
			logger.error("Get OSS signature failed, error information: ", e);
			return null;
		} catch (ExecutionException e) {
			logger.error("Get OSS signature failed, error information: ", e);
			return null;
		}
	}

	/**
	 * 从aliyunOSS的bucket中删除指定key对应的对象
	 * @param account 访问aliyun的账户信息
	 * @param endpoint aliyunOSS的服务节点
	 * @param bucket 对象所在的bucket名
	 * @param objectKey 要删除的对象的key
	 * @return true表示成功删除，false表示出现异常情况
	 * @since 2016年9月22日
	 * @author liudian@yikuyi.com
	 */
	protected static boolean deleteObject(AliyunOSSAccount account, AliyunOSSAccessType clientType,
			AliyunOSSEndpoint endpoint, final String bucket, String objectKey) {
		OSSClient client = null;
		try {
			client = getClient(account, endpoint, clientType);

			String key = trimKey(objectKey);
			client.deleteObject(bucket, key);
			return true;
		} catch (ClientException | OSSException e) {
			// 出现任何异常，将缓存中的client移除并关闭，下次重新获取
			removeClient(account, endpoint, clientType);
			logger.error("Delete object from AliyunOSS failed. bucket:{}, key:{}, because:", bucket, objectKey, e);
			return false;
		} catch (ExecutionException e) {
			logger.error("Delete object from AliyunOSS failed. bucket:{}, key:{}, because:", bucket, objectKey, e);
			return false;
		}
	}

	static Map<String, String> getDeleteSignature(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			final String bucket, String objectKey) {
		Map<String, String> retMap = new LinkedHashMap<>();
		OSSClient client = null;
		Date date = new Date();
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);

			String key = trimKey(objectKey);

			String bucketName = bucket;
			if (bucket == null) {
				throw new IllegalArgumentException(OSSUtils.OSS_RESOURCE_MANAGER.getString("MustSetBucketName"));
			}
			OSSUtils.ensureBucketNameValid(bucketName);

			Credentials currentCreds = client.getCredentialsProvider().getCredentials();
			String accessId = currentCreds.getAccessKeyId();
			String accessKey = currentCreds.getSecretAccessKey();
			boolean useSecurityToken = currentCreds.useSecurityToken();
			HttpMethod method = HttpMethod.DELETE;

			ClientConfiguration config = client.getClientConfiguration();
			String resourcePath = OSSUtils.determineResourcePath(bucketName, key, config.isSLDEnabled());

			RequestMessage requestMessage = new RequestMessage();
			requestMessage.setEndpoint(OSSUtils.determineFinalEndpoint(client.getEndpoint(), bucketName, config));
			requestMessage.setMethod(method);
			requestMessage.setResourcePath(resourcePath);

			String dateStr = DateUtil.formatRfc822Date(date);
			requestMessage.addHeader(HttpHeaders.DATE, dateStr);

			if (useSecurityToken) {
				requestMessage.addHeader(OSSHeaders.OSS_SECURITY_TOKEN, currentCreds.getSecurityToken());
			}

			String canonicalResource = "/" + bucketName + (key != null ? "/" + key : "");
			String canonicalString = SignUtils.buildCanonicalString(method.toString(), canonicalResource,
					requestMessage, null);
			String signature = ServiceSignature.create().computeSignature(accessKey, canonicalString);

			String authorization = "OSS " + accessId + ":" + signature;
			retMap.put(OSSHeaders.AUTHORIZATION, authorization);
			retMap.putAll(requestMessage.getHeaders());
			retMap.put(OSSHeaders.HOST, bucket + "." + endpoint.getUrl());
		} catch (ClientException | OSSException e) {
			// 出现任何异常，将缓存中的client移除并关闭，下次重新获取
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
			logger.error(OSS_SIGNATURE_FAILED, e);
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return retMap;
	}

	static String getDeleteUrl(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, final String bucket,
			String objectKey, Date date, long expireSeconds) {
		String delUrl = null;
		OSSClient client = null;
		try {
			client = getClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);

			String key = trimKey(objectKey);

			String bucketName = bucket;
			if (bucket == null) {
				throw new IllegalArgumentException(OSSUtils.OSS_RESOURCE_MANAGER.getString("MustSetBucketName"));
			}
			OSSUtils.ensureBucketNameValid(bucketName);

			Date expiration = new Date(date.getTime() + expireSeconds * 1000L);
			String expires = String.valueOf(expiration.getTime() / 1000L);

			/*
			 * 可以使用DateUtil.formatRfc822Date格式化日期为RFC822
			 */

			Credentials currentCreds = client.getCredentialsProvider().getCredentials();
			String accessId = currentCreds.getAccessKeyId();
			String accessKey = currentCreds.getSecretAccessKey();
			boolean useSecurityToken = currentCreds.useSecurityToken();
			HttpMethod method = HttpMethod.DELETE;

			ClientConfiguration config = client.getClientConfiguration();
			String resourcePath = OSSUtils.determineResourcePath(bucketName, key, config.isSLDEnabled());

			RequestMessage requestMessage = new RequestMessage();
			requestMessage.setEndpoint(OSSUtils.determineFinalEndpoint(client.getEndpoint(), bucketName, config));
			requestMessage.setMethod(method);
			requestMessage.setResourcePath(resourcePath);

			requestMessage.addHeader(HttpHeaders.DATE, expires);

			if (useSecurityToken) {
				requestMessage.addParameter(RequestParameters.SECURITY_TOKEN, currentCreds.getSecurityToken());
			}

			String canonicalResource = "/" + bucketName + (key != null ? "/" + key : "");
			String canonicalString = SignUtils.buildCanonicalString(method.toString(), canonicalResource,
					requestMessage, null);
			String signature = ServiceSignature.create().computeSignature(accessKey, canonicalString);

			Map<String, String> params = new LinkedHashMap<>();
			params.put(HttpHeaders.EXPIRES, expires);
			params.put(RequestParameters.OSS_ACCESS_KEY_ID, accessId);
			params.put(RequestParameters.SIGNATURE, signature);
			params.putAll(requestMessage.getParameters());

			String queryString = HttpUtil.paramToQueryString(params, OSSConstants.DEFAULT_CHARSET_NAME);

			/* Compse HTTP request uri. */
			delUrl = requestMessage.getEndpoint().toString();
			if (!delUrl.endsWith("/")) {
				delUrl += "/";
			}
			delUrl += resourcePath + "?" + queryString;
		} catch (ClientException | OSSException e) {
			// 出现任何异常，将缓存中的client移除并关闭，下次重新获取
			logger.error(OSS_SIGNATURE_FAILED, e);
			removeClient(account, endpoint, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
		} catch (ExecutionException e) {
			logger.error(OSS_SIGNATURE_FAILED, e);
		}
		return delUrl;
	}

	/**
	 * 通过STS授权机制，想用户临时授予Role的权限，并以此生成OSS客户端
	 * 
	 * @param account
	 * @param endpoint
	 * @param isImage
	 * @return
	 * @throws ClientException
	 * @since 2016年6月14日
	 * @author liudian@yikuyi.com
	 * @throws ExecutionException
	 */
	private static OSSClient getClient(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, AliyunOSSAccessType type)
			throws ExecutionException {
		String url = getEndpointUrl(endpoint, type);
		String ossClientCacheKey = getClientCacheKey(account, url);
		ClientConfiguration clientConfig = new ClientConfiguration();
		if (AliyunOSSAccessType.OSS_ACCESS_VPC.equals(type) || AliyunOSSAccessType.OSS_ACCESS_INTERNAL.equals(type)) {
			clientConfig.setProtocol(Protocol.HTTP);
		}
		else {
			clientConfig.setProtocol(Protocol.HTTPS);
		}
		return getOSSClient(account, endpoint, ossClientCacheKey, url, clientConfig);
	}

	/**
	 * @param endpoint
	 * @param type
	 * @return
	 * @since 2017年4月20日
	 * @author liudian@yikuyi.com
	 */
	private static String getEndpointUrl(AliyunOSSEndpoint endpoint, AliyunOSSAccessType type) {
		String url = endpoint.getUrl();
		switch (type) {
		case OSS_ACCESS_INTERNAL:
			url = endpoint.getInternalUrl();
			break;
		case OSS_ACCESS_VPC:
			url = endpoint.getVpcUrl();
			break;
		case OSS_ACCESS_PUBLIC:
			url = endpoint.getUrl();
			break;
		default:
			break;
		}
		return url;
	}

	/**
	 * @param account
	 * @param url
	 * @return
	 * @since 2017年4月6日
	 * @author liudian@yikuyi.com
	 */
	private static String getClientCacheKey(AliyunOSSAccount account, String url) {
		String ossClientCacheKey = "";
		ossClientCacheKey += account.getAccessKeyId() + ".";
		ossClientCacheKey += account.getAccessKeySecret() + ".";
		ossClientCacheKey += account.getRole() + "@";
		ossClientCacheKey += url;
		return ossClientCacheKey;
	}

	/**
	 * @param account
	 * @param endpoint
	 * @param ossClientCacheKey
	 * @param url
	 * @return
	 * @throws ExecutionException
	 * @since 2017年4月6日
	 * @author liudian@yikuyi.com
	 */
	private static OSSClient getOSSClient(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			String ossClientCacheKey, String url, ClientConfiguration clientConfig) throws ExecutionException {
		return ossClientCache.get(ossClientCacheKey, () -> {
			AssumeRoleResponse arResponse = assumeRole(account, endpoint, ProtocolType.HTTPS);
			String accessId = arResponse.getCredentials().getAccessKeyId();
			String accessKey = arResponse.getCredentials().getAccessKeySecret();
			String securityToken = arResponse.getCredentials().getSecurityToken();
			return new OSSClient(url, accessId, accessKey, securityToken, clientConfig);
		});
	}

	/**
	 * 去除key前后的无效字符
	 * 
	 * @param key
	 * @return
	 * @since 2016年9月22日
	 * @author liudian@yikuyi.com
	 */
	public static String trimKey(String key) {
		String tKey = key;
		if (StringUtils.isBlank(tKey)) {
			return "";
		}
		if (tKey.startsWith("/") || tKey.endsWith("/")) {
			tKey = tKey.replaceFirst("^/*", "").replaceFirst("/*$", "");
		}
		return tKey;
	}

	/**
	 * 取出路径前后的无效字符
	 * 
	 * @param dir
	 * @return
	 * @since 2016年9月22日
	 * @author liudian@yikuyi.com
	 */
	public static String trimDir(String dir) {
		String directory = dir;
		if (StringUtils.isBlank(directory)) {
			return "";
		}
		if (directory.startsWith("/") || directory.endsWith("/")) {
			directory = directory.replaceFirst("^/*", "").replaceFirst("/+$", "/");
		}
		if (StringUtils.isNotEmpty(directory) && !directory.endsWith("/")) {
			directory += "/";
		}
		return directory;
	}

	/**
	 * Parse endpoint and bucket from a object url.
	 * 
	 * @param getUrl
	 *            OSS对象的地址，不带权限和参数
	 * @return
	 * @since 2016年6月14日
	 * @author liudian@yikuyi.com
	 */
	protected static Map<String, String> parseOSSObjectUrl(String getUrl) {
		String escapedUrl = UrlEscapers.urlFragmentEscaper().escape(getUrl);
		Map<String, String> retMap = new LinkedHashMap<>();
		URI uri = URI.create(escapedUrl);
		String host = uri.getHost();
		String path = trimKey(uri.getPath());
		retMap.put(KEY_SCHEME, uri.getScheme());
		retMap.put(KEY_HOST, host);
		retMap.put(KEY_OBJECT, path);
		AliyunOSSEndpoint[] endpoints = AliyunOSSEndpoint.values();
		String endpoint = null;
		String bucket = null;
		for (AliyunOSSEndpoint endpt : endpoints) {
			if (whetherThisEndpoint(host, endpt)) {
				int index = host.indexOf('.');
				endpoint = endpt.name();
				bucket = host.substring(0, index);
				break;
			}
		}
		if (StringUtils.isEmpty(endpoint)) {
			bucket = AliyunOSSDomainMapper.findBucketbyDomain(host);
			AliyunOSSEndpoint endpt = AliyunOSSDomainMapper.findEndpointbyBucket(bucket);
			endpoint = (endpt == null ? host:endpt.name());			
		}
		if (StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(bucket)){
			throw new IllegalArgumentException("无法将{" + getUrl + "}映射成有效的bucket或endpont。");
		}
		retMap.put(KEY_ENDPOINT, endpoint);
		retMap.put(KEY_BUCKET, bucket);
		return retMap;
	}

	private static final boolean whetherThisEndpoint(String host, AliyunOSSEndpoint endpt) {
		if (StringUtils.isEmpty(host)) {
			return false;
		}
		// 普通oss或img域名
		if (host.endsWith("." + endpt.getUrl()) || host.endsWith("." + endpt.getImgUrl())) {
			return true;
		}
		// oss或img内网域名
		if (host.endsWith("." + endpt.getInternalUrl()) || host.endsWith("." + endpt.getInternalImgUrl())) {
			return true;
		}
		// oss或imgvpc域名
		return (host.endsWith("." + endpt.getVpcUrl()) || host.endsWith("." + endpt.getVpcImgUrl()));
	}

	private static AssumeRoleResponse assumeRole(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			ProtocolType protocolType) throws com.aliyuncs.exceptions.ClientException {
		return assumeRole(account, endpoint, null, protocolType);
	}

	private static AssumeRoleResponse assumeRole(AliyunOSSAccount account, AliyunOSSEndpoint endpoint, String policy,
			ProtocolType protocolType) throws com.aliyuncs.exceptions.ClientException {
		String roleSessionName = "oss-access-session-" + System.currentTimeMillis();
		return assumeRole(account, endpoint, roleSessionName, policy, protocolType);
	}

	/**
	 * 向相应OSS用户授予指定角色权限的方法
	 * 
	 * @param account OSS访问账号
	 * @param endpoint 对应的OSS端点
	 * @param roleSessionName 给定一个会话名
	 * @param policy 授权策略，该策略与role的策略取交集
	 * @param protocolType 协议类型，http、https
	 * @return
	 * @throws ClientException
	 * @since 2016年6月15日
	 * @author liudian@yikuyi.com
	 */
	private static AssumeRoleResponse assumeRole(AliyunOSSAccount account, AliyunOSSEndpoint endpoint,
			String roleSessionName, String policy, ProtocolType protocolType)
			throws com.aliyuncs.exceptions.ClientException {
		// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
		String accessKeyId = account.getAccessKeyId();
		String accessKeySecret = account.getAccessKeySecret();
		String role = account.getRole();
		IClientProfile profile = DefaultProfile.getProfile(endpoint.getRegionId(), accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		// 创建一个 AssumeRoleRequest 并设置请求参数
		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion(STS_API_VERSION);
		request.setMethod(MethodType.POST);
		request.setProtocol(protocolType);
		request.setRoleArn(role);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(policy);
		request.setDurationSeconds(ACS_MAX_DURATION_SECONDS);
		// 发起请求，并得到response
		return client.getAcsResponse(request);
	}
}