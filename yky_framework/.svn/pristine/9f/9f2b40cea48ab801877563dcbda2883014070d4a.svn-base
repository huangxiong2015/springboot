/*
 * Created: 2015年10月25日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.wink.providers.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.ykyframework.exception.UnLoginException;

/**
 * wink中如果遇到用户未登录的异常的处理器，用此种方法处理起来更优雅，不破坏代码结构。
 * @author liaoke@yikuyi.com
 * @since 1.0.0
 */
public class UnLoginExceptionMapper implements ExceptionMapper<UnLoginException> {

	@Override
	public Response toResponse(UnLoginException ex) {
		//FIXME 后续要改一下错误码，以及返回的结构体
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("No Login info.").build();
	}



}
