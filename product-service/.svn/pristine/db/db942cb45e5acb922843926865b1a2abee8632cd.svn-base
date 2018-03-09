package com.yikuyi.product.activity;

import java.util.List;

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProducOrderVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 活动商品服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
public interface IActivityProductResource {
	
	/**
	 * 由订单操作触发，根据订单数量和参加的活动，增减活动库存<br>
	 * 必须指定要参加的活动，根据指定的活动扣减redis中的库存，然后发送mq通知修改数据库中的库存。如果redis中不存在这个活动则抛出异常
	 * @author tongkun@yikuyi.com
	 * @param productOrders 下订单的活动商品列表
	 * @throws BusinessException 
	 */
	@ApiOperation(value = "由订单操作触发，根据订单数量和参加的活动，增减活动库存", notes = "作者：佟昆<br>根据商品当前参加的活动，对库存进行扣减或增加。此接口是强制同步的，这意味着执行完成时，操作必定为成功或失败。如果为库存扣减，如果剩余库存不够或者不在活动期间扣减则会抛出异常。", response = Void.class)
	public List<ActivityProducOrderVo> updateActivityProductQty(List<ActivityProducOrderVo> productOrders) throws BusinessException;


	/**
	 * 根据活动商品id查询活动商品信息
	 * @author zr.aoxianbing@yikuyi.com
	 * @param productId 活动商品id
	 */
	@ApiOperation(value = "根据活动商品id查询商品草稿信息", notes = "作者：敖县兵<br>根据活动商品id查询商品草稿信息", response = ActivityProduct.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityProductDraft getProductById(@ApiParam(value = "商品Id", required = true)  String activityProductId);
	/**
	 * 编辑商品信息
	 * @author zr.aoxianbing@yikuyi.com
	 * @param ActivityProduct 活动商品
	 */
	@ApiOperation(value = "编辑保存草稿商品信息", notes = "作者：敖县兵<br>编辑保存草稿商品信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void editProductInfo(@ApiParam("商品信息") ActivityProductDraft activityProduct);

	/**
	 * 保存商品历史记录
	 * @since 2017年6月19日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "保存前一天生效的活动商品的历史记录", notes = "作者：佟昆<br>将前一天生效的活动商品的总数和剩余数保存在历史表中", response = Void.class)
	public String saveActivitiesProductsHistory();
}