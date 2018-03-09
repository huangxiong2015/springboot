package com.yikuyi.product.rule.delivery;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.pagehelper.PageInfo;
import com.yikuyi.rule.delivery.vo.DeliveryInfo;
import com.yikuyi.rule.price.ProductPriceRule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IDeliveryResource {
	
	/**
	 * 新增交期模板
	 * @param info
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "新增交期模板", notes = "新增交期模板", response = DeliveryInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class)})
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public DeliveryInfo addDelivery(@ApiParam("交期模板信息") DeliveryInfo info);
	
	/**
	 * 查询交期模板列表
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "查询交期模板列表", notes = "查询交期模板列表", response = DeliveryInfo.class, responseContainer="PageInfo")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "对应页面不存在", response = Void.class)})
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<DeliveryInfo> getDeliveryList(@ApiParam(value = "页数") @RequestParam(value="page",required=false,defaultValue="1") int page,
			                                      @ApiParam(value = "每页条数") @RequestParam(value="size",required=false,defaultValue="20") int size,
			                                      @ApiParam(value = "创建时间-开始时间")String startDate,
				                          		  @ApiParam(value = "创建时间-结束时间")String endDate,
			                          			  @ApiParam(value = "规则状态，取值DISABLED、ENABLED或为空")String ruleStatus,
			                          			  @ApiParam(value = "供应商ID")String vendorId,
			                          			  @ApiParam(value = "规则名称")String ruleName);
	
	/**
	 * 根据交期模块Id查询详情
	 * @param id
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "根据交期模块Id查询详情", notes = "根据交期模块Id查询详情", response = DeliveryInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的交期模板，因此无法获取数据", response = Void.class)})
	@RequestMapping(method=RequestMethod.GET)
	public DeliveryInfo getDeliveryDetail(@ApiParam(value = "交期模板ID", required = true) @PathVariable("id") String id);
	
	/**
	 * 修改交期模板
	 * @param id
	 * @param info
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "修改交期模板", notes = "修改交期模板", response = DeliveryInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "不存在指定id的交期模板，因此无法获取数据", response = Void.class)})
	@RequestMapping(method=RequestMethod.PUT)
	public DeliveryInfo updateDelivery(@ApiParam(value = "交期模板ID", required = true) @PathVariable("id") String id, 
			@ApiParam("交期模板信息") DeliveryInfo info);
	
	/**
	 * 启用、停用交期模板
	 * @param id
	 * @param stauts
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "启用、停用交期模板", notes = "启用、停用交期模板", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的交期模板,操作失败", response = Void.class)})
	@RequestMapping(method=RequestMethod.PUT)
	public DeliveryInfo updateDeliveryStatus(@ApiParam(value = "交期模板ID",required=true) @PathVariable("id") String id,
			@ApiParam("模板状态") @RequestParam(value="status",required=true) ProductPriceRule.RuleStatus stauts);
	
	/**
	 * 删除交期模板
	 * @param id
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "删除交期模板", notes = "删除交期模板", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的交期模板,删除失败", response = Void.class)})
	@RequestMapping(method=RequestMethod.DELETE)
	public void deleteDelivery(@ApiParam(value = "运费模板ID") @PathVariable("id") String id);

}
