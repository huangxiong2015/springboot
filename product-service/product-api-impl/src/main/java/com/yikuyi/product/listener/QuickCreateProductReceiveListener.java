package com.yikuyi.product.listener;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.product.goods.manager.CreateProductManager;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

/**
 * 
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
@Service
public class QuickCreateProductReceiveListener implements MsgReceiveListener {

	@Autowired
	private CreateProductManager createProductManager;

	@Override
	public void onMessage(Serializable arg) {
		createProductManager.onMessage(arg);
	}

}
