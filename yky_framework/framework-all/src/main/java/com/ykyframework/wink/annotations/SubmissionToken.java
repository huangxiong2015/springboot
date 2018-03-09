/*
 * Created: 2016年4月26日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.wink.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubmissionToken {
	boolean generateToken() default false ;   //是否生成TOKEN
	boolean checkToken() default false;		//是否校验TOKEN
//	boolean checkAjaxRequest() default false;		//是否校验AJAX请求，默认为否。由于后台无法判断是否为ＡＪＡＸ请求，
}
