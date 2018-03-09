package com.yikuyi.product.model;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;

public class ProductSearchResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8506657047332415384L;

	@ApiModelProperty(value="搜索ID")
	private String searchResultId;
	
	@ApiModelProperty(value="搜索用户ID")
	private String partyId;
	
	@ApiModelProperty(value="排序字段")
	private String orderByName;
	
	@ApiModelProperty(value="搜索用户的IP")
	private String partyIp;
	
	@ApiModelProperty(value="关键词")
	private String keyWord;
	
	@ApiModelProperty(value="搜索用户的名字")
	private String partyName;
	
	@ApiModelProperty(value="搜索结果数据,Y 有结果，N: 无结果数据")
	private String hasResult;
	
	@ApiModelProperty(value="搜索耗时")
	private double secondsTotal;
	
	@ApiModelProperty(value="搜索时间")
	private Date searchDate;

	public String getSearchResultId() {
		return searchResultId;
	}

	public void setSearchResultId(String searchResultId) {
		this.searchResultId = searchResultId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getOrderByName() {
		return orderByName;
	}

	public void setOrderByName(String orderByName) {
		this.orderByName = orderByName;
	}

	public String getPartyIp() {
		return partyIp;
	}

	public void setPartyIp(String partyIp) {
		this.partyIp = partyIp;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getHasResult() {
		return hasResult;
	}

	public void setHasResult(String hasResult) {
		this.hasResult = hasResult;
	}

	public double getSecondsTotal() {
		return secondsTotal;
	}

	public void setSecondsTotal(double secondsTotal) {
		this.secondsTotal = secondsTotal;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}
	
}
