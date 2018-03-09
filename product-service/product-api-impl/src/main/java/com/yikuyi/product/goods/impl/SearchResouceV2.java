package com.yikuyi.product.goods.impl;

import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.goods.ISearchResouceV2;
import com.yikuyi.product.goods.manager.ProductSearchResultManager;
import com.yikuyi.product.goods.manager.SearchManagerV2;
import com.yikuyi.product.model.ProductSearchResult;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;

@RestController
@RequestMapping("v2/inventory")
public class SearchResouceV2 implements ISearchResouceV2{
	private static final Logger logger = LoggerFactory.getLogger(SearchResouceV2.class);
	
	@Autowired
	private ProductSearchResultManager productSearchResultManager;
	
	@Autowired
	private SearchManagerV2 searchManager;
	
	@RequestMapping( method=RequestMethod.GET)
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> search(
			@RequestParam(value="keyword",required=false) String keyword,
			@RequestParam(value="vendorId",required=false) String vendorId,
			@RequestParam(value="manufacturer",required=false) String manufacturer,
			@RequestParam(value="cat",required=false) String cat,
			@RequestParam(value="sort",required=false) String sort,
			@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=false, defaultValue="20") int pageSize,
			@RequestParam(value="showQty",required=false) String showQty,
			@RequestParam(value="isPreview", required=false, defaultValue="N")String isPreview) throws InterruptedException {
		try {
			return searchManager.getProductInfo(keyword, vendorId, manufacturer, cat, sort, page, pageSize, showQty,isPreview);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	@RequestMapping(value="/record/searchresult", method=RequestMethod.POST)
	@Produces(value=MediaType.APPLICATION_JSON)
	public String insert(@RequestBody(required=true) ProductSearchResult searchResult) {
		String searchResultId = String.valueOf(IdGen.getInstance().nextId());
		searchResult.setSearchResultId(searchResultId);
		return productSearchResultManager.insert(searchResult);
	}
	
}
