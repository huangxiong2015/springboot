package com.yikuyi.strategy.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * 包邮信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "strategy")
public class Strategy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8684996509296591142L;

	@Id
	@ApiModelProperty(value = "包邮id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "名称")
	private String title;
	
	@ApiModelProperty(value = "类型")
	private StrategyType strategyType;
	
	@ApiModelProperty(value = "冗余字段存储")
	private JSONObject content;
	
	@ApiModelProperty(value = "有效时间起始")
	private String startDate;
	
	@ApiModelProperty(value = "有效时间结束")
	private String endDate;
	
	@ApiModelProperty(value = "状态")
	private StrategyStatus strategyStatus;
	
	@ApiModelProperty(value = "创建者")
	private String creator;
	
	@ApiModelProperty(value = "创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value = "更新者")
	private String lastUpdateUser;
	
	@ApiModelProperty(value = "更新者名称")
	private String lastUpdateUserName;
	
	public enum StrategyType {
		/**
		 * 包邮
		 */
		FREE_DERIVERY,
		/**
		 * 限购
		 */
		LIMITATIONS
	}
	
	public enum StrategyStatus {
		/**
		 * 停用
		 */
		HOLD,
		/**
		 * 启用
		 */
		START,
		/**
		 * 进行中
		 */
		PUBLISHED,
		/**
		 * 已结束
		 */
		END,
		/**
		 * 删除
		 */
		DELETED
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public StrategyType getStrategyType() {
		return strategyType;
	}

	public void setStrategyType(StrategyType strategyType) {
		this.strategyType = strategyType;
	}
	
	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public StrategyStatus getStrategyStatus() {
		return strategyStatus;
	}

	public void setStrategyStatus(StrategyStatus strategyStatus) {
		this.strategyStatus = strategyStatus;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	@Override
	public boolean equals(Object obj) {
		if(StringUtils.isEmpty(this.id)){
			return false;
		}
		if(obj instanceof Strategy){
			Strategy strategy = (Strategy)obj;
			if(StringUtils.isEmpty(strategy.getId())){
				return false;
			}
			return strategy.getId().equals(this.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if(StringUtils.isNotEmpty(this.id))
			return this.id.hashCode();
		return super.hashCode();
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
	

}