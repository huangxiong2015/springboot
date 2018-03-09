package com.yikuyi.product.model;

import java.util.List;
import com.ykyframework.model.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品附件文档
 * 
 * @author zr.wujiajun
 */
public class ProductDocument extends AbstractEntity {

	private static final long serialVersionUID = 6364756152486072206L;

	@ApiModelProperty(value = "商品的附件")
	private List<ProductAttachment> attaches;

	@ApiModelProperty(value = "商品的附件描述")
	private String description;

	@ApiModelProperty(value = "商品的附件名")
	private String name;

	@ApiModelProperty(value = "商品的附件类型")
	private String type;

	@ApiModelProperty(value = "附件的url，冗余字段")
	private String url;

	public List<ProductAttachment> getAttaches() {
		return attaches;
	}

	public void setAttaches(List<ProductAttachment> attaches) {
		this.attaches = attaches;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
