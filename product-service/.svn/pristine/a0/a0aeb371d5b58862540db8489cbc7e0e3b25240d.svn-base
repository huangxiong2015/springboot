package com.yikuyi.product.strategy;

import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.strategy.model.Strategy;
import com.yikuyi.strategy.model.Strategy.StrategyStatus;
import com.yikuyi.strategy.model.Strategy.StrategyType;
import com.yikuyi.strategy.vo.StrategyVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 包邮/限购
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface IStrategyResource {
	
	
	/**
	 * 包邮/限购规则列表查询
	 * @param title
	 * @param updateDateStart
	 * @param updateDateEnd
	 * @param creatorName
	 * @param strategyStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "包邮/限购规则列表信息", notes = "包邮/限购规则列表信息", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<StrategyVo> list(@ApiParam(value="名称", required=false) String title,
			@ApiParam(value = "开始更新时间", required = false) String updateDateStart,
			@ApiParam(value = "结束更新时间", required = false) String updateDateEnd,
			@ApiParam(value="创建人", required=false) String creatorName,
			@ApiParam(value="状态", required=false) StrategyStatus strategyStatus,
			@ApiParam(value="类型:FREE_DERIVERY 包邮,LIMITATIONS 限购", required=false) StrategyType strategyType,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);

	
	/**
	 * 查询包邮/限购规则详情
	 * @param id
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询包邮/限购规则详情", notes = "查询包邮/限购规则详情", response = Strategy.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public StrategyVo getStrategy(@ApiParam(value="id", required=true) String id);
	
	/**
	 * 删除包邮/限购规则信息
	 * @param ruleType
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "删除包邮/限购规则信息", notes = "删除包邮/限购规则信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void deleteStrategyById(@ApiParam(value="id", required=true) String id) throws BusinessException;
	
	/**
	 *  修改包邮/限购状态
	 * @param id
	 * @param strategyStatus
	 * @throws BusinessException
	 * @throws ParseException
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "修改包邮/限购状态", notes = "修改包邮/限购状态", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updateStrategyStatus(@ApiParam(value="id", required=true) String id,
			@ApiParam(value="状态", required=true) StrategyStatus strategyStatus) throws BusinessException,ParseException;
	
	/**
	 * 添加包邮/限购模块相关信息
	 * @param ruleId
	 * @param specialOfferRule
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "添加包邮/限购模块相关信息", notes = "添加包邮/限购模块相关信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void addStrategy(@ApiParam(value="strategyId", required=true) String strategyId,
			@RequestBody(required=true) Strategy strategy) throws BusinessException;
	
	
	/**
	 * 将正式商品表中的数据迁移到草稿表中
	 * @param strategyId
	 * @throws BusinessException
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "将正式商品表中的数据迁移到草稿表中", notes = "将正式商品表中的数据迁移到草稿表中", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void copyStrategyProductToDraft(@ApiParam(value="strategyId", required=true) String strategyId) throws BusinessException;
	
	
	/**
	 * 编辑包邮/限购模块信息
	 * @param id
	 * @param strategy
	 * @throws BusinessException
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "编辑包邮/限购模块信息", notes = "编辑包邮/限购模块信息", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void updateStrategy(@ApiParam(value="id", required=true) String id,@RequestBody(required=true) Strategy strategy) throws BusinessException;
	
	
	/**
	 * 定时刷缓存
	 * @since 2018年1月19日
	 * @author zr.wanghong@yikuyi.com
	 */
	@ApiOperation(value = "定时刷缓存", notes = "定时刷缓存", response = Void.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void refreshStrategyCacheTask();
}
