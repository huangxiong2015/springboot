package com.yikuyi.document.model;

import org.springframework.data.annotation.Id;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品上传,具体文件内容记录
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("DocumentTitle")
public class DocumentTitle extends AbstractEntity{
	
	private static final long serialVersionUID = 5334524279611049422L;
	
	@Id
	@ApiModelProperty(value="文档ID")
	private String id;
	
	@ApiModelProperty(value="EXCEL,sheet下标")
	private int sheetIndex;
	
	@ApiModelProperty(value="EXCEL,sheet名称")
	private String sheetName;
	
	@ApiModelProperty(value="原始导入数据")
	private String originalTitle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}
}