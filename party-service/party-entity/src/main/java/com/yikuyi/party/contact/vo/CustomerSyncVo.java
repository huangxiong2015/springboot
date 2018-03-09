package com.yikuyi.party.contact.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("CustomerSyncVo")
public class CustomerSyncVo {
	

	@ApiModelProperty(value = "区分是全量还是增量")
	private String flag;
	
	@ApiModelProperty(value = "同步开始时间")
	private String createStart;
	
	@ApiModelProperty(value = "同步结束时间")
	private String createEnd;
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCreateStart() {
		return createStart;
	}

	public void setCreateStart(String createStart) {
		this.createStart = createStart;
	}

	public String getCreateEnd() {
		return createEnd;
	}

	public void setCreateEnd(String createEnd) {
		this.createEnd = createEnd;
	}
	

	
	
}
