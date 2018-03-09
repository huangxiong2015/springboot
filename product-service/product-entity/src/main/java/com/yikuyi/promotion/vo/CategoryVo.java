package com.yikuyi.promotion.vo;

import java.io.Serializable;

/**
 * 商品条件前端json对应的Vo
 * 
 * @author DELL-USER10
 *
 */
public class CategoryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 大类ID/小类ID */
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}