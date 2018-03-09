package com.yikuyi.category.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.ykyframework.model.AbstractEntity;
import com.ykyframework.model.enums.IntEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分类信息
 * @author zr.wenjiao@yikuyi.com
 */
@Document(collection="product_category")
public class ProductCategory extends AbstractEntity{
	
	private static final long serialVersionUID = 212354824480038854L;
	/**
	 *  主键id 
	 */
	@ApiModelProperty(value="类别ID")
	@Id
	@JsonProperty("_id")
	private Integer id;
	
	@ApiModelProperty(value="类别名称")
    @Field(value="cateName")
	@JsonProperty("cateName")
	private String name;
	
	@ApiModelProperty(value="类别级别")
    @Field(value="cateLevel")
	@JsonProperty("cateLevel")
	private Integer level;
	
	@ApiModelProperty(value="类别状态")
	private Integer status; 

	@ApiModelProperty(value="类别别名")
	private List<ProductCategoryAlias> cateAlias;
	
	@ApiModelProperty(value="操作人名称")
	private String operatedUserName;
	
	@ApiModelProperty(value="类别图标")
	private String icon;
	
	@ApiModelProperty(value="扩展样式")
	private ProductCategoryStyle style;
	
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	public enum ProductCategoryLevel implements IntEnum {
		CAT_ELEVEL_1(1), CAT_ELEVEL_2(2), CAT_ELEVEL_3(3);

		private int value = 1;

		private ProductCategoryLevel(int i) {
			this.value = i;
		}

		public int getValue() {
			return value;
		}
	}
	
	public ProductCategory (){}
	
	public ProductCategory(ProductCategoryParent categoryParent) {
		this.id = categoryParent.getId();
		this.name = categoryParent.getName();
		this.level = categoryParent.getLevel();
		this.status = categoryParent.getStatus();
	}
	
	public ProductCategory(ProductCategory category){
		this.id = category.getId();
		this.name = category.getName();
		this.level = category.getLevel();
		this.status = category.getStatus();
	}

	
	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<ProductCategoryAlias> getCateAlias() {
		return cateAlias;
	}

	public void setCateAlias(List<ProductCategoryAlias> cateAlias) {
		this.cateAlias = cateAlias;
	}

	public String getOperatedUserName() {
		return operatedUserName;
	}

	public void setOperatedUserName(String operatedUserName) {
		this.operatedUserName = operatedUserName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public ProductCategoryStyle getStyle() {
		return style;
	}

	public void setStyle(ProductCategoryStyle style) {
		this.style = style;
	}

	
}