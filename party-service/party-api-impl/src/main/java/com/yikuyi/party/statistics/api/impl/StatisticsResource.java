package com.yikuyi.party.statistics.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.statistics.api.IStatisticsResource;
import com.yikuyi.party.statistics.bll.StatisticsManager;
import com.yikuyi.party.statistics.vo.StatisticsVo.StatisticsType;

/**
 * @author tangr
 *
 */
@RestController
@RequestMapping("v1/statistics")
public class StatisticsResource implements IStatisticsResource{
	@Autowired
	private StatisticsManager statisticsManager;
	
	/**
	 * 根据类型获取不同统计结果数量
	 */
	@Override
	@RequestMapping(method=RequestMethod.GET)
	public Long getStatisticsNumByType(@RequestParam(value="type") StatisticsType type) {
		return statisticsManager.getStatisticsNumByType(type);
	}
}
