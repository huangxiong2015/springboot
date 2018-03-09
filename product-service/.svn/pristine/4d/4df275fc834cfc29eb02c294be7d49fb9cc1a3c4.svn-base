package com.yikuyi.product.rule.logistics.manager;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.rule.logistics.vo.LogisticsFee;

@Service
@Transactional
public class LogisticsFeeManager {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticsFeeManager.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 实时查询商品的运费
	 * @param fee
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public double getLogisticsFee(LogisticsFee fee){
		logger.info("LogisticsFeeManager---method:getLogisticsFee");
		double logisticsFee = 0;
		//从redis中取出运费模板信息
		Cache cache = cacheManager.getCache("logisticsRule");
		String key = "logisticsRule.user-defined";
		Map<String,String> logisticsMap = cache.get(key,Map.class);
		//如果缓存中没有找到运费信息，则直接返回0
		if(logisticsMap == null || logisticsMap.isEmpty()){
			return logisticsFee;
		}
		//币种为人民币
		if(("CNY").equals(fee.getCurrency())){
			String chFeeInfo = logisticsMap.get("CH");
			return handleLogisticsInfo(fee,chFeeInfo);
		}else if(("USD").equals(fee.getCurrency())){   //币种为美元
			String hkFeeInfo = logisticsMap.get("HK");
			return handleLogisticsInfo(fee,hkFeeInfo);
		}else{   //币种为其他运费直接返回0
			return logisticsFee;
		}
	}
	
	/**
	 * 处理从Redis中取出来的运费模板数据
	 * @param currency
	 * @param feeInfo
	 * @return
	 * @since 2016年12月7日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private double handleLogisticsInfo(LogisticsFee fee,String feeInfo){
		//如果缓存中没有找到运费信息，则直接返回0
		if(feeInfo == null || feeInfo.isEmpty()){
			return 0;
		}
		String[] feeValue = feeInfo.split("\\|");
		String[] pinkage = feeValue[0].split(":");
		double pinkageValue = Double.parseDouble(pinkage[1]);
		//如果订单总额要大于等于运费规则模板中的包邮金额时，则表示运费包邮，返回0
		if(fee.getAmount() >= pinkageValue){
			return 0;
		}
		//国内运费
		if(("CNY").equals(fee.getCurrency())){
			double insideValue;
			double outsideValue;
			String[] faltFee = feeValue[1].split(":");
			if(!("IN_SIDE").equals(faltFee[0])){
				outsideValue = Double.parseDouble(faltFee[1]);
				faltFee = feeValue[2].split(":");
				insideValue = Double.parseDouble(faltFee[1]);
			}else{
				insideValue = Double.parseDouble(faltFee[1]);
				faltFee = feeValue[2].split(":");
				outsideValue = Double.parseDouble(faltFee[1]);
			}
			if((ProvinceConstant.GEO_ID).equals(fee.getToGeoId())){
				return insideValue;
			}else{
				return outsideValue;
			}
		}else{   //香港运费
			String[] hkFaltFee = feeValue[1].split(":");
			return Double.parseDouble(hkFaltFee[1]);
		}
	}
}
