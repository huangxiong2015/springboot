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
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.ykyframework.model.enums.IntEnum;

/**
 * 枚举类型的mybatis处理器，用于处理EnumOrdinalTypeHandler不支持自定义的数值持久化的问题。
 * 
 * @see http://www.tuicool.com/articles/nm67Nvm
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 * @param <E>
 */
public class IntEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
	private Class<E> type;
	private Map<Integer, E> map = new HashMap<>();

	public IntEnumTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
		E[] enums = type.getEnumConstants();
		if (enums == null) {
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
		}
		for (E e : enums) {
			IntEnum valuedEnum = (IntEnum) e;
			map.put(valuedEnum.getValue(), e);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		IntEnum valuedEnum = (IntEnum) parameter;
		ps.setInt(i, valuedEnum.getValue());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int i = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return getIntEnum(i);
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int i = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return getIntEnum(i);
		}
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int i = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			return getIntEnum(i);
		}
	}

	private E getIntEnum(int value) {
		try {
			return map.get(value);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName() + " by value.",
					ex);
		}
	}
}
