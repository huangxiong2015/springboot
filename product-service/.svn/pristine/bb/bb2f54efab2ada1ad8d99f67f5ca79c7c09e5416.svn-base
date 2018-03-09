package com.yikuyi.document.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品上传,具体文件内容记录
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("DocumentLog")
public class DocumentLog extends AbstractEntity{
	
	private static final long serialVersionUID = 5334524279611049422L;
	
	private static final int COMMENTS_LENGTH = 250;
	
	@Id
	@ApiModelProperty(value="文档ID")
	private String id;
	
	@ApiModelProperty(value="EXCEL,sheet下标")
	private int sheetIndex = 0;
	
	@ApiModelProperty(value="EXCEL行号")
	private int lineNo;
	
	@ApiModelProperty(value="日志状态")
	private DocumentLogStatus status;
	
	@ApiModelProperty(value="备注")
	private String comments;
	
	@ApiModelProperty(value="原始导入数据")
	private String originalData;
	
	public enum DocumentLogStatus{
		/**
		 * 成功,数据库存储0
		 */
		SUCCESS(0),
		/**
		 * 失败,数据库存储1
		 */
		FAIL(1);
		private int value;
		DocumentLogStatus(int value){
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public DocumentLogStatus getStatus() {
		return status;
	}
	public void setStatus(DocumentLogStatus status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = getSizeLengthMsg(comments);
	}

	public String getOriginalData() {
		return originalData;
	}
	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}
	public int getSheetIndex() {
		return sheetIndex;
	}
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
	//现在string长度,防止插入数据库异常
	public static String getSizeLengthMsg(String msg){
		return null != msg && msg.length()>COMMENTS_LENGTH ? msg.substring(0,COMMENTS_LENGTH) : msg;
	}
	//现在string长度,防止插入数据库异常
	public static String getSizeLengthMsg(Exception e){
		if(null != e){
			StringBuilder builder = new StringBuilder();
			builder.append(e.toString()+",");
			for(StackTraceElement el : e.getStackTrace()){
				if((builder.length() + el.toString().length()) > 250){
					break;
				}
				builder.append(el.toString()+",");
			}
			return getSizeLengthMsg(builder.toString());
		}
		return StringUtils.EMPTY;
	}
}