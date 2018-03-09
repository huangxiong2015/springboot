package com.yikuyi.product.rule.logistics.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.rule.logistics.ILogisticsFeeResource;
import com.yikuyi.product.rule.logistics.manager.LogisticsFeeManager;
import com.yikuyi.rule.logistics.vo.LogisticsFee;

@RestController
@RequestMapping("v1/logistics/fee")
public class LogisticsFeeResource implements ILogisticsFeeResource{
	
	@Autowired
	private LogisticsFeeManager logisticsFeeManager;

	/**
	 * 实时查询商品的运费信息
	 * @param fee
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public double getLogisticsFee(@RequestBody LogisticsFee fee) {
		return logisticsFeeManager.getLogisticsFee(fee);
	}

}
