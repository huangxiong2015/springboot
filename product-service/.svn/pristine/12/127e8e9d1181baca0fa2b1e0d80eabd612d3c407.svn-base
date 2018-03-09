package com.yikuyi.rule.delivery.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yikuyi.product.model.Product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("ProductLeadTimeVo")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class ProductLeadTimeVo extends Product implements Serializable{
	
	private static final long serialVersionUID = 5754405794213764239L;

	@ApiModelProperty(value="交期模板名称")
	private String templateName;
	
	@ApiModelProperty(value="实际交期")
	private List<LeadTimeVo> realityList;
	
	@ApiModelProperty(value="上传交期为空页面不显示(Y/N)")
	private String isShowLeadTime;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<LeadTimeVo> getRealityList() {
		return realityList;
	}
	
	public void setRealityList(List<LeadTimeVo> realityList) {
		this.realityList = realityList;
	}

	public String getIsShowLeadTime() {
		return isShowLeadTime;
	}

	public void setIsShowLeadTime(String isShowLeadTime) {
		this.isShowLeadTime = isShowLeadTime;
	}
}
