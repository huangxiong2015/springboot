package com.yikuyi.product.strategy;

/**
 * 包邮
 * @author zr.wanghong
 * @version 1.0.0
 */
public interface IStrategyCacheResource {
	
	/**
	 * 定时任务清理包邮缓存
	 */
	void refreshStrategyCacheTask();
}
