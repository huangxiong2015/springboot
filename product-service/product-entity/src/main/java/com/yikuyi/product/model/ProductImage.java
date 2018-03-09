package com.yikuyi.product.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;
/**
 * 商品图片
 * @author zr.wujiajun
 */
public class ProductImage extends AbstractEntity{
	
	private static final long serialVersionUID = 8033691033635585112L;
	@ApiModelProperty(value="图片类型")
	private String type;
	@ApiModelProperty(value="图片地址")
	private String url;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
}
