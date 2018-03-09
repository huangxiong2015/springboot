package com.yikuyi.promotion.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 促销标识前端json对应的Vo
 * @author DELL-USER10
 *
 */
public class PromotionFlagVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int PROMOTION = 1;
	public static final int PRETTY_CHEAP = 2;
	public static final int CUSTOMIZE = 0;

	private boolean show = false;
	
	private int type;
	
	private Map<String,String> list;
	
	private Map<String,String> detail;
	
	private Map<String,String> popwindow;
	
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<String, String> getList() {
		return list;
	}

	public void setList(Map<String, String> list) {
		this.list = list;
	}

	public Map<String, String> getDetail() {
		return detail;
	}

	public void setDetail(Map<String, String> detail) {
		this.detail = detail;
	}

	public Map<String, String> getPopwindow() {
		return popwindow;
	}

	public void setPopwindow(Map<String, String> popwindow) {
		this.popwindow = popwindow;
	}
}