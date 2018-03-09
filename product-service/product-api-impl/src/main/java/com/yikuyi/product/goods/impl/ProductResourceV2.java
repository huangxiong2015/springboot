package com.yikuyi.product.goods.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.goods.IProductResourceV2;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.vo.ProductVo;

@RestController
@RequestMapping("v2/products")
public class ProductResourceV2 implements IProductResourceV2 {
	
	@Autowired
	private ProductManager productManager;

	/**
	 * 根据ids批量查询商品的全部信息,包含价格,交期等
	 * @param ids
	 * @return
	 * @author zr.wujiajun
	 */	
	@Override
	@RequestMapping(value = "/batch/full", method = RequestMethod.POST,produces = "application/json; charset=utf-8")	
	public List<ProductVo> getFullInfoByIds(@RequestBody List<String> ids) {
		return productManager.findFullInfoV2(ids);
	}
	
	/**
	 * 根据  品牌 和 型号  查商品ID，价格阶梯
	 * @params manufacturer
	 * @params manufacturerPartNumber
	 * @author tb.huangqingfeng
	 */
	@Override
	@RequestMapping(value = "/batch/findFacturerAndPartNumber", method = RequestMethod.GET,produces = "application/json; charset=utf-8")	
	public List<ProductVo> findFacturerAndPartNumber(
			@RequestParam(value="manufacturer",required=true) String manufacturer,
			@RequestParam(value="manufacturerPartNumber",required=true) String manufacturerPartNumber) {
		return productManager.findFacturerAndPartNumber(manufacturer,manufacturerPartNumber);
		
	}


}
