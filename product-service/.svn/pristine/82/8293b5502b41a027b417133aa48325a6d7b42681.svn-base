package com.yikuyi.document.model;

import org.springframework.data.annotation.Id;

import com.ykyframework.model.AbstractEntity;
import com.ykyframework.model.enums.IntEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品上传文档记录
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("Document")
public class Document extends AbstractEntity {
	
	private static final long serialVersionUID = 5334524279611049422L;
	
	@Id
	@ApiModelProperty(value="文档ID")
	private String id;
	
	@ApiModelProperty(value="会员ID")
	private DocumentType typeId;
	
	@ApiModelProperty(value="文档存储位置")
	private String documentLocation;
	
	@ApiModelProperty(value="文档名称")
	private String documentName;
	
	@ApiModelProperty(value="文档状态")
	private DocumentStatus statusId;
	
	@ApiModelProperty(value="备注")
	private String comments;
	
	@ApiModelProperty(value="是否有取消操作,0否,1是")
	private CancelStatus isCancel;
	
	@ApiModelProperty(value="上传文件的总行数")
	private Integer dataCount;
	
	@ApiModelProperty(value="错误文件下载路径")
	private String errorFileLocation;
	
	@ApiModelProperty(value="供应商Id")
	private String partyId;
	
	@ApiModelProperty(value="供应商Id")
	private String facilityId;
	
	@ApiModelProperty(value="日志文件存储位置")
	private String logLocation;
	
	public enum CancelStatus implements IntEnum {
		/**
		 * 是
		 */
		YES(1),
		/**
		 * 否
		 */
		NO(0);
		private int value = 0;
		private CancelStatus(int i){
			this.value = i;
		}
		public int getValue() {
			return value;
		}
	}
	
	public enum DocumentType{
		/**
		 * 客户需求数据文档
		 */
		VENDOR_REQ_DOCUMENT,
		/**
		 * 供应商SKU附件数据
		 */
		VENDOR_SKU_DOCUMENT,
		/**
		 * 供应商SPU附件数据
		 */
		VENDOR_SPU_DOCUMENT,
		/**
		 * 系统标准型号同步任务Type
		 */
		PRODUCT_STAND_NO,
		/**
		 * 活动商品秒杀数据
		 */
		ACTIVITY_DOCUMENT,
		/**
		 * 活动装修商品数据
		 */
		PROMOTION_DOCUMENT,
		/**
		 * 专属特价商品数据
		 */
		SPECIAL_OFFERP_RODUCT
		
	}
	public enum DocumentStatus{
		/**
		 * [文档状态] 已创建
		 */
		DOC_CREATED,
		/**
		 * [文档状态] 已删除
		 */
		DOC_DELETED,
		/**
		 * [文档状态] 正在处理
		 */
		DOC_IN_PROCESS,
		/**
		 * [文档状态] 已锁定
		 */
		DOC_LOCKED,
		/**
		 * [文档状态] 处理失败
		 */
		DOC_PRO_FAILED,
		/**
		 * [文档状态] 部分成功，部分失败
		 */
		DOC_PRO_PART_SUCCESS,
		/**
		 * 成功处理完成
		 */
		DOC_PRO_SUCCESS,
		/**
		 * [文档状态] 已取消
		 */
		DOC_CANCEL
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DocumentType getTypeId() {
		return typeId;
	}
	public void setTypeId(DocumentType typeId) {
		this.typeId = typeId;
	}
	public String getDocumentLocation() {
		return documentLocation;
	}
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public DocumentStatus getStatusId() {
		return statusId;
	}
	public void setStatusId(DocumentStatus statusId) {
		this.statusId = statusId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = DocumentLog.getSizeLengthMsg(comments);
	}
	public CancelStatus getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(CancelStatus isCancel) {
		this.isCancel = isCancel;
	}
	public String getErrorFileLocation() {
		return errorFileLocation;
	}
	public void setErrorFileLocation(String errorFileLocation) {
		this.errorFileLocation = errorFileLocation;
	}
	public Integer getDataCount() {
		return dataCount;
	}
	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getLogLocation() {
		return logLocation;
	}
	public void setLogLocation(String logLocation) {
		this.logLocation = logLocation;
	}
}