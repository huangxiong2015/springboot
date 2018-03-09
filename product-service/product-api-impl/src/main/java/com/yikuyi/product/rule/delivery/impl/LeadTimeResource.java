package com.yikuyi.product.rule.delivery.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yikuyi.product.rule.delivery.ILeadTimeResource;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.rule.delivery.vo.ProductInfo;

@RestController
@RequestMapping("v1/products")
public class LeadTimeResource implements ILeadTimeResource{
	
	@Autowired
	private LeadTimeManager leadTimeManager;

	/**
	 * 根据商品Id实时查询交期信息
	 * @param ids
	 * @return
	 * @since 2016年12月5日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/batch/leadtime",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<ProductInfo> getLeadTimeList(@RequestBody List<String> ids) {
		return leadTimeManager.getLeadTimeList(ids);
	}
}
