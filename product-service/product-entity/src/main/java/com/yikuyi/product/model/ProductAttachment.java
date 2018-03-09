package com.yikuyi.product.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品附件文档
 * 
 * @author zr.wujiajun
 */
public class ProductAttachment extends AbstractEntity {

	private static final long serialVersionUID = 6364756152486072206L;
	@ApiModelProperty(value = "附件名称")
	private String name;
	@ApiModelProperty(value = "附件地址")
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
