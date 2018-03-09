package com.yikuyi.product.goods.manager;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yikuyi.product.goods.dao.ProductSearchResultDao;
import com.yikuyi.product.model.ProductSearchResult;

/**
 * 商品搜索结果页日志记录
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@Service
public class ProductSearchResultManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductSearchResultManager.class);
	
	@Autowired
	private ProductSearchResultDao productSearchResultDao;
	
	/**
	 * 新增
	 * @param searchResult
	 * @return
	 * @since 2017年5月11日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public String insert(ProductSearchResult searchResult){
		String result = "success";
		logger.info("商品搜索结果页新增日志记录");
		try{
			searchResult.setSearchDate(new Date());
			productSearchResultDao.insert(searchResult);
		}catch(Exception e){
			logger.error("商品搜索结果页新增日志记录出错！错误信息{}",e);
			result = "fail";
		}
		return result;
	}

}
