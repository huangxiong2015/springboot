package com.yikuyi.product.listener;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.model.Product;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * @author tongkun
 * 过期商品进行失效处理监听
 */
@Service
public class InvalidProductListener implements MsgReceiveListener {
	
	private static final Logger logger = LoggerFactory.getLogger(InvalidProductListener.class);
	
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public void onMessage(Serializable arg) { 
		logger.debug("InvalidProductListener,arg为：{}", arg);
		
		//失效通知
		boolean isPartyVo = false;
		if(arg instanceof String){
			String supplierId = arg.toString();
			int pageSize = 1000;
			int page = 0;
			//lastId用于循环分批处理
			String lastId = StringUtils.EMPTY;
			isPartyVo = true;
			//一个供应商一天只发一次
			Cache cache = cacheManager.getCache("crawlerSyncProduct");
			ValueWrapper valueWrapper = cache.get(supplierId);
			if(valueWrapper == null){
				cache.put(supplierId, supplierId);
			}else{
				return;
			}
						
			int invalidCount = 0;
			int waitForAdviceCount = 0;
			productManager.processVendorProduct(supplierId,page,pageSize,invalidCount,waitForAdviceCount,lastId);
		}
		if(isPartyVo){
			return;
		}
		//获取数据
		@SuppressWarnings("unchecked")
		ArrayList<Product> datas = (ArrayList<Product>)arg;
		//执行更新
		productManager.updateInvalidProduct(datas);
	}

}
