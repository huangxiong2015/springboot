/*
 * Created: 2016年6月17日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.oss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public class AliyunOSSDomainMapper {
	
	public static final String IMAGE_DOMAIN_PREFIX = "image";
	
	public static final String BUCKET_HZ_ICTRADE_BIZ_PUBLIC_UAT = "ictrade-biz-public-hz-uat";
	
	public static final String BUCKET_HZ_ICTRADE_PUBLIC_UAT = "ictrade-public-hz-uat";
	
	public static final String BUCKET_HZ_ICTRADE_PRIVATE_UAT = "ictrade-private-hz-uat";
	
	public static final String BUCKET_SZ_YKY_PUBLIC_ALL = "sz-yky-public-all";
	
	public static final String BUCKET_SZ_ICTRADE_PUBLIC_PROD = "sz-ictrade-public-prod";
	
	public static final String BUCKET_SZ_ICTRADE_PRIVATE_PROD = "sz-ictrade-private-prod";
		
	private static final Map<String, AliyunOSSEndpoint> bucketEndpointMap = new HashMap<>();
	
	private static final Map<String, String> domainBucketMap = new HashMap<>();
	
	private static final Map<String, Boolean> imageDomainMap = new HashMap<>();
	
	static {
		//bucket和endpoint映射		
		bucketEndpointMap.put(BUCKET_HZ_ICTRADE_BIZ_PUBLIC_UAT, AliyunOSSEndpoint.OSS_CN_HANGZHOU);
		bucketEndpointMap.put(BUCKET_HZ_ICTRADE_PUBLIC_UAT, AliyunOSSEndpoint.OSS_CN_HANGZHOU);
		bucketEndpointMap.put(BUCKET_HZ_ICTRADE_PRIVATE_UAT, AliyunOSSEndpoint.OSS_CN_HANGZHOU);
		bucketEndpointMap.put(BUCKET_SZ_YKY_PUBLIC_ALL, AliyunOSSEndpoint.OSS_CN_SHENZHEN);
		bucketEndpointMap.put(BUCKET_SZ_ICTRADE_PUBLIC_PROD, AliyunOSSEndpoint.OSS_CN_SHENZHEN);
		bucketEndpointMap.put(BUCKET_SZ_ICTRADE_PRIVATE_PROD, AliyunOSSEndpoint.OSS_CN_SHENZHEN);	
	
		//域名映射
		domainBucketMap.put("file-uat.ykystatic.com", BUCKET_HZ_ICTRADE_PUBLIC_UAT);
		domainBucketMap.put("img0-uat.ykystatic.com", BUCKET_HZ_ICTRADE_PUBLIC_UAT);
		domainBucketMap.put("img1-uat.ykystatic.com", BUCKET_HZ_ICTRADE_PUBLIC_UAT);
		domainBucketMap.put("file.ykystatic.com", BUCKET_SZ_ICTRADE_PUBLIC_PROD);
		domainBucketMap.put("img0.ykystatic.com", BUCKET_SZ_ICTRADE_PUBLIC_PROD);
		domainBucketMap.put("img1.ykystatic.com", BUCKET_SZ_ICTRADE_PUBLIC_PROD);
		domainBucketMap.put("img2.ykystatic.com", BUCKET_SZ_ICTRADE_PUBLIC_PROD);
		domainBucketMap.put("misc.ykystatic.com", BUCKET_SZ_YKY_PUBLIC_ALL);
		
		//是否Image域
		imageDomainMap.put("file-uat.ykystatic.com", Boolean.FALSE);
		imageDomainMap.put("img0-uat.ykystatic.com", Boolean.TRUE);
		imageDomainMap.put("img1-uat.ykystatic.com", Boolean.TRUE);
		imageDomainMap.put("file.ykystatic.com", Boolean.FALSE);
		imageDomainMap.put("img0.ykystatic.com", Boolean.TRUE);
		imageDomainMap.put("img1.ykystatic.com", Boolean.TRUE);
		imageDomainMap.put("img2.ykystatic.com", Boolean.TRUE);
		imageDomainMap.put("misc.ykystatic.com", Boolean.FALSE);
	}
	
	public static List<String> getDomains(String bucket) {
		List<String> domains = new ArrayList<>();
		if (!bucketEndpointMap.containsKey(bucket)){
			return domains; 
		}
		domains = domainBucketMap.entrySet().stream().filter(value -> value.getValue().equals(bucket))
				.map(entry -> (checkisImage(entry.getKey()) ? (IMAGE_DOMAIN_PREFIX + ":") : "") + entry.getKey())
				.collect(Collectors.toList());
		return domains;
	}
	
	public void setDomainMap(Map<String, List<String>> domainMap) {
		if (MapUtils.isNotEmpty(domainMap)) {
			domainMap.forEach(AliyunOSSDomainMapper::putBucketDomainMapping);
		}
	}
	
	public static void putBucketDomainMapping(String bucket, List<String> domains) {
		final String imageDomain = IMAGE_DOMAIN_PREFIX + ":";		
		for (String domain : domains) {
			String parsedDomain = StringUtils.removeStart(domain, imageDomain);
			if (StringUtils.startsWith(domain, imageDomain)){
				imageDomainMap.put(parsedDomain, Boolean.TRUE);
			}
			domainBucketMap.put(parsedDomain, bucket);
		}
	}
	
	public static void putBucketEndpointMapping(String bucket, AliyunOSSEndpoint endpoint) {
		AliyunOSSEndpoint configuredEndpoint = AliyunOSSDomainMapper.findEndpointbyBucket(bucket);
		//bucketname和endpoing的的配置必须要匹配
		if (configuredEndpoint == null){
			bucketEndpointMap.put(bucket, endpoint);			
		}
		else if (!endpoint.equals(AliyunOSSDomainMapper.findEndpointbyBucket(bucket))){
			throw new IllegalArgumentException("BucketName与endpoint不匹配，请联系管理员确认并修改配置");
		}
	}
	
	public static AliyunOSSEndpoint findEndpointbyBucket(String bucket) {
		return bucketEndpointMap.get(bucket);
	}
	
	public static String findBucketbyDomain(String domain) {
		return domainBucketMap.get(domain);
	}
	
	public static boolean checkisImage(String domain) {
		Boolean isImage = imageDomainMap.get(domain);
		return (isImage != null && isImage.booleanValue());
	}

}
