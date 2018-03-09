package com.yikuyi.party.statistics.api;

import com.yikuyi.party.statistics.vo.StatisticsVo.StatisticsType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 统计数据统一接口
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public interface IStatisticsResource {
	
	/**
	 * 根据类型获取不同统计结果数量
	 * @return
	 * @since 2017年3月23日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "获取统计结果", notes = "获取统计结果", response = Void.class)
	@ApiResponses(value ={ @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public Long getStatisticsNumByType(@ApiParam(value = "type", required = true) StatisticsType type);
}