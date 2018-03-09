package com.yikuyi.product.resource;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductVo;
import com.ykyframework.exception.SystemException;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author tangr
 * 商品的接口feign定义
 */
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface ProductClient{
	
	@RequestLine("POST /v1/products/batch/full")
	public List<ProductVo> getFullInfoByIds(@RequestBody List<String> ids);
	
	@RequestLine("GET /v1/products/isHasProduct?vendorId={vendorId}")
	@Headers({"Authorization: Basic {authToken}"})
	public boolean isHasProduct(@Param("vendorId") String vendorId,@Param("authToken") String authToken);
	
	@RequestLine("GET /v1/products/getSaleProductList?id={id}&manufacturerPartNumber={manufacturerPartNumber}&manufacturer={manufacturer}&vendorId={vendorId}&sourceId={sourceId}&cate1Id={cate1Id}&cate2Id={cate2Id}&cate3Id={cate3Id}&keyword={keyword}&startDate={startDate}&endDate={endDate}&page={page}&pageSize={pageSize}")
	@Headers({"Authorization: Basic {authToken}"})
	public Map<String, Object> getSaleProductList(@Param("id") String id,
			@Param("manufacturerPartNumber") String manufacturerPartNumber,
			@Param("manufacturer") String manufacturer,
			@Param("vendorId") String vendorId,
			@Param("sourceId") String sourceId,
			@Param("cate1Id") Integer cate1Id,
			@Param("cate2Id") Integer cate2Id, 
			@Param("cate3Id") Integer cate3Id,
			@Param("keyword") String keyword,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate,
			@Param("page") int page,
			@Param("pageSize") int pageSize,
			@Param("authToken") String authToken)throws SystemException;
	
	@RequestLine("GET /v1/products/recommendOthers?id={id}&catId={catId}")
	public List<ProductVo> recommendOthers(@Param("id") String id,@Param("catId")String catId);
	
	@RequestLine("POST /v1/products//batch/findBatchMfrIdAndMpm")
	public List<ProductVo> findBatchMfrIdAndMpm(@Param("productRequests") List<ProductRequest> productRequests);

}
