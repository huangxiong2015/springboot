package com.yikuyi.specialoffer.vo;

import java.io.Serializable;
import java.util.List;

import com.yikuyi.specialoffer.model.SpecialOfferRule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("SpecialOfferRuleVo")
public class SpecialOfferRuleVo extends SpecialOfferRule implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8044530488026080698L;

	@ApiModelProperty(value = "制造商名称集合")
	private String mfrName;
	
	@ApiModelProperty(value = "制造商名称数组")
	private String mfrNameArray;
	
	@ApiModelProperty(value = "仓库名称集合")
	private String sourceName;
	
	@ApiModelProperty(value = "次小类名称集合")
	private String catName;
	
	@ApiModelProperty(value = "次小类名称数组")
	private List<String> catNameArray;
	
	public String getMfrName() {
		return mfrName;
	}

	public void setMfrName(String mfrName) {
		this.mfrName = mfrName;
	}
	
	public String getMfrNameArray() {
		return mfrNameArray;
	}

	public void setMfrNameArray(String mfrNameArray) {
		this.mfrNameArray = mfrNameArray;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public List<String> getCatNameArray() {
		return catNameArray;
	}

	public void setCatNameArray(List<String> catNameArray) {
		this.catNameArray = catNameArray;
	}

}