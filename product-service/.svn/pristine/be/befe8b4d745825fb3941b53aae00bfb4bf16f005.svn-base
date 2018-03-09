package com.yikuyi.product.rule.logistics;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.pagehelper.PageInfo;
import com.yikuyi.rule.logistics.vo.LogisticsInfo;
import com.yikuyi.rule.price.ProductPriceRule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 运费模板相关的服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
public interface ILogisticsResource {
	
	/**
	 * 新增运费模板
	 * @param info
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "新增运费模板", notes = "新增运费模板", response = LogisticsInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class)})
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public LogisticsInfo addLogistics(@ApiParam("运费模板信息") LogisticsInfo info);
	
	/**
	 * 查询运费模板列表
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "查询运费模板列表", notes = "查询运费模板列表", response = LogisticsInfo.class, responseContainer="List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "对应页面不存在", response = Void.class)})
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<LogisticsInfo> getLogisticsList();
	
	/**
	 * 根据运费模块Id查询详情
	 * @param id
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "根据运费模块Id查询详情", notes = "根据运费模块Id查询详情", response = LogisticsInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的运费模板，因此无法获取数据", response = Void.class)})
	@RequestMapping(method=RequestMethod.GET)
	public LogisticsInfo getLogisticsDetail(@ApiParam(value = "运费模板ID", required = true) @PathVariable("id") String id);
	
	/**
	 * 修改运费模板
	 * @param id
	 * @param info
	 * @return
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "修改运费模板", notes = "修改运费模板", response = LogisticsInfo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "不存在指定id的运费模板，因此无法获取数据", response = Void.class)})
	@RequestMapping(method=RequestMethod.PUT)
	public LogisticsInfo updateLogistics(@ApiParam(value = "运费模板ID", required = true) @PathVariable("id") String id, 
			@ApiParam("运费模板信息") LogisticsInfo info);
	
	/**
	 * 启用、停用运费模板
	 * @param id
	 * @param stauts
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "启用、停用运费模板", notes = "启用、停用运费模板", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的运费模板,操作失败", response = Void.class)})
	@RequestMapping(method=RequestMethod.PUT)
	public LogisticsInfo updateLogisticsStatus(@ApiParam(value = "运费模板ID",required=true) @PathVariable("id") String id,
			@ApiParam("模板状态") @RequestParam(value="status",required=true) ProductPriceRule.RuleStatus stauts);
	
	/**
	 * 删除运费模板
	 * @param id
	 * @since 2016年12月6日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@ApiOperation(value = "删除运费模板", notes = "删除运费模板", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的运费模板,删除失败", response = Void.class)})
	@RequestMapping(method=RequestMethod.DELETE)
	public void deleteLogistics(@ApiParam(value = "运费模板ID") @PathVariable("id") String id);
}
