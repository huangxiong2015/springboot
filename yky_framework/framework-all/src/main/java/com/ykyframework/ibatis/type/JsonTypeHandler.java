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
package com.ykyframework.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.JSONObject;

/**
 * text，json数据的mybatis处理器
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
public class JsonTypeHandler extends BaseTypeHandler<JSONObject> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toJSONString());
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return JSONObject.parseObject(rs.getString(columnName));
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return JSONObject.parseObject(rs.getString(columnIndex));
	}

	@Override
	public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return JSONObject.parseObject(cs.getString(columnIndex));
	}
}
