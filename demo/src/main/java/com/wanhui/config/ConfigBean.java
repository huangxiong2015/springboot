package com.wanhui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//通过配置文件给属性赋值
@ConfigurationProperties(prefix="com.wanhui")
public class ConfigBean {
	private String name;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
