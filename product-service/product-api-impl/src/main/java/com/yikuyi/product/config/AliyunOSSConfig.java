/*
 * Created: 2016年12月12日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ykyframework.oss.AliyunOSSAccount;
import com.ykyframework.oss.AliyunOSSBucket;
import com.ykyframework.oss.AliyunOSSDirectory;
import com.ykyframework.oss.AliyunOSSOperator;
import com.ykyframework.oss.AliyunOSSOperator.AliyunOSSAccessType;


@Configuration
@ConfigurationProperties
public class AliyunOSSConfig {

	@Value("${aliyun.OSS.accessKeyId}")
	private String accessKeyId;

	@Value("${aliyun.OSS.accessKeySecret}")
	private String accessKeySecret;

	@Value("${aliyun.OSS.role}")
	private String roleARN;

	@Value("${aliyun.OSS.endpoint}")
	private String aliyunOSSEndpoint;

	@Bean(name = "aliyun.oss.account")
	public AliyunOSSAccount getAliyunOSSAccount() {
		return new AliyunOSSAccount(this.accessKeyId, this.accessKeySecret, this.roleARN);
	}
	
	@Bean(name="materialDomainList")
	@Profile(value={"hz-uat"})
	public List<String> getDomainListUAT() {
		return Arrays.asList(new String[] {"image:img0-uat.ykystatic.com", "file-uat.ykystatic.com" });
	}
	
	//生产环境material(public-read)域名映射，image:开始的为图片域名，其它为通用域名
	@Bean(name="materialDomainList")
	@Profile(value={"prod"})
	public List<String> getDomainListProd() {
		return Arrays.asList(new String[] { "image:img0.ykystatic.com", "image:img1.ykystatic.com", "file.ykystatic.com" });
	}
	
	//sit/dev/mock没有域名映射
	@Bean(name="materialDomainList")
	@Profile({"mock","sit","sit-mq", "dev"})
	public List<String> getDomainListOther() {
		return Arrays.asList(new String[] {});
	}
	
	@Profile({"dev", "sit","sit-mq"})
	@Bean
	public AliyunOSSOperator aliyunOSSOperatorPUP(@Autowired AliyunOSSAccount account) {
		return new  AliyunOSSOperator(account, AliyunOSSAccessType.OSS_ACCESS_PUBLIC);
	}
	
	@Profile({"hz-uat", "prod","prod-mq","prod-fmq"})
	@Bean
	public AliyunOSSOperator aliyunOSSOperatorVPC(@Autowired AliyunOSSAccount account) {
		return new  AliyunOSSOperator(account, AliyunOSSAccessType.OSS_ACCESS_VPC);
	}
	

	/**
	 * biz-public的bucket
	 * @param account
	 * @param bucketPublicread
	 * @return
	 * @since 2017年1月4日
	 * @author liudian@yikuyi.com
	 */
	@Bean(name = "aliyun.OSS.bucket.publicread")
	public AliyunOSSBucket getOSSBucketPublicread(
			@Autowired(required = true) @Qualifier(value = "aliyun.oss.account") AliyunOSSAccount account,
			@Value("${aliyun.OSS.bucket.publicread}") String bucketPublicread) {
		return new AliyunOSSBucket(account, this.aliyunOSSEndpoint, bucketPublicread);
	}

	@Bean(name = "aliyun.OSS.bucket.publicread.root")
	public AliyunOSSDirectory getOSSBucketPublicreadRoot(
			@Autowired(required = true) @Qualifier(value = "aliyun.OSS.bucket.publicread") AliyunOSSBucket bucket,
			@Value("${aliyun.OSS.bucket.root}") String bucketRoot) {
		return new AliyunOSSDirectory(bucket, bucketRoot);
	}

	/**
	 * 私有读取权限的bucket
	 * @param account
	 * @param bucketPrivateread
	 * @return
	 * @since 2017年1月4日
	 * @author liudian@yikuyi.com
	 */
	@Bean(name = "aliyun.OSS.bucket.privateread")
	public AliyunOSSBucket getOSSBucketPrivateread(
			@Autowired(required = true) @Qualifier(value = "aliyun.oss.account") AliyunOSSAccount account,
			@Value("${aliyun.OSS.bucket.privateread}") String bucketPrivateread) {
		return new AliyunOSSBucket(account, this.aliyunOSSEndpoint, bucketPrivateread);
	}

	@Bean(name = "aliyun.OSS.bucket.privateread.root")
	public AliyunOSSDirectory getOSSBucketPrivatereadRoot(
			@Autowired(required = true) @Qualifier(value = "aliyun.OSS.bucket.privateread") AliyunOSSBucket bucket,
			@Value("${aliyun.OSS.bucket.root}") String bucketRoot) {
		return new AliyunOSSDirectory(bucket, bucketRoot);
	}
}
