/*
 * Created: 2016年2月4日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.test.context;

import org.springframework.test.context.ActiveProfilesResolver;

/**
 * 用于spring单元测试的profile解析器，可以通过设置spring.profiles.active的环境变量来达到在不同环境进行测试的目的。
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @see http://benweizhu.github.io/blog/2015/11/07/spring-active-profile-resolver-and-active-profiles-and-integration-test/
 */
public class SpringActiveProfileResolver implements ActiveProfilesResolver {

	@Override
    public String[] resolve(final Class<?> aClass) {
        final String activeProfile = System.getProperty("spring.profiles.active");
        //如果没有配置默认取dev环境的
        return new String[] { activeProfile == null ? "dev" : activeProfile };
    }

}
