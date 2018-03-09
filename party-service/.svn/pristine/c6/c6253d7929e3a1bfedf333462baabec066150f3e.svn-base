package com.yikuyi.party.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ykyframework.oss.AliyunOSSDirectory;
import com.ykyframework.oss.AliyunOSSFileUploadSetting;
import com.ykyframework.oss.AliyunOSSFileUploadType;
import com.ykyframework.oss.AliyunOSSHashType;

@Configuration
@ConfigurationProperties
public class AliyunOSSFileUploadConfig {
	// 共有
	@Autowired
	@Qualifier(value = "aliyun.OSS.bucket.publicread.root")
	private AliyunOSSDirectory publicDirectory;

	// 私有
	@Autowired
	@Qualifier(value = "aliyun.OSS.bucket.privateread.root")
	private AliyunOSSDirectory privateDirectory;

	/**
	 * 
	 * @param url
	 *            上传路径
	 * @param aliyunOSSDirectory
	 *            公有/私有
	 * @return
	 * @since 2017年2月8日
	 * @author tb.chenxuemin@yikuyi.com
	 */
	private AliyunOSSFileUploadType initFileUploadType(String dir, AliyunOSSDirectory aliyunOSSDirectory, AliyunOSSHashType hashType) {
		AliyunOSSFileUploadType voucherType = new AliyunOSSFileUploadType(aliyunOSSDirectory, hashType, dir);
		return voucherType;
	}

	// 兼容之前web工程的使用方式，初始化和注入AliyunOSSFileUploadSetting
	@Bean(name = "aliyun.OSSFileUploadSetting")
	public AliyunOSSFileUploadSetting getOSSFileUploadSetting() {
		AliyunOSSFileUploadSetting uploadSetting = new AliyunOSSFileUploadSetting();
		Map<String, AliyunOSSFileUploadType> uploadTypes = new HashMap<>();
		uploadTypes.put("productLine.export", this.initFileUploadType("productLine/export/", privateDirectory, AliyunOSSHashType.DATE_DISCRETE));
		uploadSetting.setUploadTypes(uploadTypes);
		return uploadSetting;
	}
}
