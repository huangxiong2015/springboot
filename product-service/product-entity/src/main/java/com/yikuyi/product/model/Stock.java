package com.yikuyi.product.model;


import com.ykyframework.model.AbstractEntity;
/**
 * 库存信息项
 * 
 * @author zr.wujiajun
 * @since 2016年11月21
 */
public class Stock extends AbstractEntity {

	private static final long serialVersionUID = 6248212884057888918L;
	private String leadTime;
	private long quantity;
	private String source;
	private String sourceId;
	private String quickFindKey;

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getQuickFindKey() {
		return quickFindKey;
	}

	public void setQuickFindKey(String quickFindKey) {
		this.quickFindKey = quickFindKey;
	}
	
}
