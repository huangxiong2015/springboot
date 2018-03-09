/*
 * Created: 2016年1月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.utils;

import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring Context的工具类，可以直接使用此工具类来getBean
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public class SpringContextUtils implements ApplicationContextAware {
	
	private static final SpringContextUtils instance = new SpringContextUtils();
	
	/**
	 * 
	 *以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.         
	 * @author zhuh                                                                   
	 */
    private ApplicationContext applicationContext;
    
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtils.instance.applicationContext = applicationContext;		
	}
	
    @PreDestroy
    public void destoryInstance() {
    	instance.applicationContext = null;
    }
   
    //取得存储在静态变量中的ApplicationContext.
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return instance.applicationContext;
    }
    
    //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) instance.applicationContext.getBean(name);
    }

    //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
    //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
    //如果有多个Bean符合Class, 取出第一个.
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        @SuppressWarnings("rawtypes")
                Map beanMaps = instance.applicationContext.getBeansOfType(clazz);
        if (beanMaps!=null && !beanMaps.isEmpty()) {
            return (T) beanMaps.values().iterator().next();
        } else{
            return null;
        }
    }

    private static void checkApplicationContext() {
        if (instance.applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextUtils");
        }
    }
}
