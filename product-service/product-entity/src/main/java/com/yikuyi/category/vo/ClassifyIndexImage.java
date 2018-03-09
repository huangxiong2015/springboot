package com.yikuyi.category.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * portal首页分类广告图片
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class ClassifyIndexImage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1543691007549258380L;
	
	@ApiModelProperty(value="首页广告图片路径")
	private String image;
	
	@ApiModelProperty(value="首页广告图片对应的链接")
	private String url;
	
	@ApiModelProperty(value="类型")
	private String type;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
