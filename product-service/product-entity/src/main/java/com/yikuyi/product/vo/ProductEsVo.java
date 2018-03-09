/*
 * Created: 2016年12月14日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.vo;

import io.swagger.annotations.ApiModel;

/**
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("productEsVo")
public class ProductEsVo extends ProductVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum SortLevel {
		ONE(1), TWO(2);
		private int value;

		SortLevel(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private int sortQtyLevel = SortLevel.TWO.getValue();
	private int sortPriLevel = SortLevel.TWO.getValue();
	private int sortImaLevel = SortLevel.TWO.getValue();

	public int getSortQtyLevel() {
		return sortQtyLevel;
	}

	public void setSortQtyLevel(int sortQtyLevel) {
		this.sortQtyLevel = sortQtyLevel;
	}

	public int getSortPriLevel() {
		return sortPriLevel;
	}

	public void setSortPriLevel(int sortPriLevel) {
		this.sortPriLevel = sortPriLevel;
	}

	public int getSortImaLevel() {
		return sortImaLevel;
	}

	public void setSortImaLevel(int sortImaLevel) {
		this.sortImaLevel = sortImaLevel;
	}

}