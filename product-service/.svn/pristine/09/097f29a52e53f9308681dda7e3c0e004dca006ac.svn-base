package com.yikuyi.product.goods;

import java.util.Map;
import com.yikuyi.product.model.ProductSearchResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

public interface ISearchResouce {

	@ApiOperation(value = "库存搜索服务接口", notes = "库存搜索服务接口", response = Map.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	Map<String, Object> search(@ApiParam(value = "关键字") String keyword, 
			@ApiParam(value = "供应商ID") String vendorId, 
			@ApiParam(value = "制造商ID") String manufacturer, 
			@ApiParam(value = "分类Id") String cat,
			@ApiParam(value = "排序，暂只支持按库存排序") String sort, 
			@ApiParam(value = "当前页") int page, 
			@ApiParam(value = "每页条数") int pageSize, 
			@ApiParam(value = "只显示有库存的") String showQty,
			@ApiParam(value = "最小库存数") String minQty) throws InterruptedException;
	
	@ApiOperation(value = "商品搜索结果页日志记录", notes = "商品搜索结果页日志记录", response = ProductSearchResult.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	String insert(ProductSearchResult searchResult);

}