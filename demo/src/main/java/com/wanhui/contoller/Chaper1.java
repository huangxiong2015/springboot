package com.wanhui.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wanhui.config.ConfigBean;

/**
 * 通过配置文件ConfigBean里面的属性赋值
 * @author hx
 *
 */
@RestController
public class Chaper1 {

	@Value("${name}")
	private String a;
	@Value("${com.wanhui.cc}")
	private String b;
	@Autowired
	private ConfigBean configBean;
	
	@RequestMapping("/") 
	public String index(){
//		return a;
		return configBean.getName()+configBean.getCode();
//		return b;
	}
}
