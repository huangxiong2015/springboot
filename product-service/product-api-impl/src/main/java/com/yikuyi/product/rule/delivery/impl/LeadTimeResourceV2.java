package com.yikuyi.product.rule.delivery.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yikuyi.product.rule.delivery.ILeadTimeResourceV2;
import com.yikuyi.product.rule.delivery.manager.LeadTimeManager;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;

@RestController
@RequestMapping("v2/products")
public class LeadTimeResourceV2 implements ILeadTimeResourceV2{
	
	@Autowired
	private LeadTimeManager leadTimeManager;
	
	/**
	 * 根据商品信息查询交期策略
	 * @param ids
	 * @return
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/batch/leadtime",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<ProductLeadTimeVo> getProductLeadTime(@RequestBody List<ProductLeadTimeVo> info) {
		return leadTimeManager.getLeadTimeByProdcut(info);
	}

}
