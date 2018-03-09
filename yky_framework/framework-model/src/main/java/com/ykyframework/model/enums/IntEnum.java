/*
 * Created: 2015年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2015 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * int型的枚举变量，用于定义POJO的状态字段。
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public interface IntEnum {
	@JsonValue
    int getValue();
}