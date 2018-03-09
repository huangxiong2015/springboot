package com.yikuyi.party.contact.vo;

import java.io.Serializable;

/**
 * 返回结果
 * @author 张伟
 *
 */
public class MsgResultVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5767959367697461149L;

	/**
	 * 错误码
	 */
	private String code;
	
	/**
	 * 当前错误码对应的结果
	 */
	private Object value;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MsgResultVo [code=" + code + ", value=" + value + "]";
	}

	public MsgResultVo(String code, Object value) {
		super();
		this.code = code;
		this.value = value;
	}

	public MsgResultVo() {
		super();
	}

	
	
}