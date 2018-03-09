package com.ictrade.common.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

/**
 * 验证配置文件是否有效
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class IctradeConfigTest {
	
	/**
	 * 测试properties文件有没有缺少项目
	 * @since 2016年9月20日
	 * @author zr.tongkun@yikuyi.com
	 * @throws IOException 
	 */
	@Test
	public void testIctradeProperties() throws IOException{
		Set<Object> allKeySet = new HashSet<Object>();//全部key的合集
		Properties dev_prop = new Properties();
		dev_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system.properties"));
//		Properties sit_prop = new Properties();
//		sit_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system_sit.properties"));
		Properties sit1_prop = new Properties();
		sit1_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system_sit1.properties"));
//		Properties uat_prop = new Properties();
//		uat_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system_uat.properties"));
		Properties hz_uat_prop = new Properties();
		hz_uat_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system_hz-uat.properties"));
		Properties prod_prop = new Properties();
		prod_prop.load(IctradeConfigTest.class.getClassLoader().getResourceAsStream("common-config/properties/system_prod.properties"));
		//集合全部的key
		for(Object k:dev_prop.keySet()){
			allKeySet.add(k);
		}
		/*for(Object k:sit_prop.keySet()){
			allKeySet.add(k);
		}*/
		for(Object k:sit1_prop.keySet()){
			allKeySet.add(k);
		}
	/*	for(Object k:uat_prop.keySet()){
			allKeySet.add(k);
		}*/
		for(Object k:hz_uat_prop.keySet()){
			allKeySet.add(k);
		}
		for(Object k:prod_prop.keySet()){
			allKeySet.add(k);
		}
		//挨个检查各个配置文件，是否有缺少的项目
		for(Object k:allKeySet){
			 assertEquals(k+"_dev",dev_prop.keySet().contains(k)?(k+"_dev"):null);//dev环境
//			 assertEquals(k+"_sit",sit_prop.keySet().contains(k)?(k+"_sit"):null);//sit环境
			 assertEquals(k+"_sit1",sit1_prop.keySet().contains(k)?(k+"_sit1"):null);//sit1环境
//			 assertEquals(k+"_uat",uat_prop.keySet().contains(k)?(k+"_uat"):null);//uat环境
			 assertEquals(k+"_hz_uat",hz_uat_prop.keySet().contains(k)?(k+"_hz_uat"):null);//hz_uat环境
			 assertEquals(k+"_prod",prod_prop.keySet().contains(k)?(k+"_prod"):null);//prod环境
		}
	}
}
