package com.yikuyi.document.model;

import org.springframework.data.annotation.Id;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品上传记录
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("ProductDocument")
public class ProductDocument extends AbstractEntity {

	private static final long serialVersionUID = 5334524279611049422L;
	
	@Id
	@ApiModelProperty(value = "文档ID")
	private String id;

	@ApiModelProperty(value = "会员ID")
	private String partyId;

	@ApiModelProperty(value = "描述")
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = DocumentLog.getSizeLengthMsg(description);
	}
}