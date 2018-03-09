package com.yikuyi.product.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.ProductBrand;
import com.ykyframework.exception.BusinessException;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author tangr
 * 制造商的接口调用定义
 */
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface BrandClient {

	@RequestLine("GET /v1/products/brands/{id}")
	public ProductBrand findById(@Param("id") Integer id);

	@RequestLine("GET /v1/products/brands")
	public List<ProductBrand> findAll();

	@RequestLine("GET /v1/products/fullInfoBrands")
	public List<ProductBrand> findAllByContidion();
	
	@RequestLine("GET /v1/products/manufacturers?page={page}&size={size}&brandName={brandName}&creator={creator}&startDate={startDate}&endDate={endDate}")
	@Headers({"Authorization: Basic {authToken}"})
	public PageInfo<ProductBrand> getBrandList(@Param("page") int page, 
				                               @Param("size") int size, 
				                               @Param("brandName") String brandName, 
				                               @Param("creator") String creator, 
				                               @Param("startDate") String startDate,
				                               @Param("endDate") String endDate,
				                               @Param("authToken") String authToken);

	@RequestLine("POST /v1/products/manufacturers")
	@Headers({"Authorization: Basic {authToken}"})
	public ProductBrand addBrand( ProductBrand info, @Param("authToken") String authToken) throws BusinessException;
	
	@RequestLine("PUT /v1/products/manufacturers/{id}")
	@Headers({"Authorization: Basic {authToken}"})
	public ProductBrand updateBrand(@Param("id") Integer id,ProductBrand info,@Param("authToken") String authToken) throws BusinessException;

	@RequestLine("GET /v1/products/brands/cache")
	public String cacheBrand();
	
	@RequestLine("GET /v1/products/brands/aliasMap")
	@Headers({"Authorization: Basic {authToken}"})
	public Map<String,ProductBrand> getAliasBrandMap(@Param("authToken") String authToken);
	
	@RequestLine("POST /v1/products/brands/aliasMap")
	@Headers({"Authorization: Basic {authToken}"})
	public Map<String,ProductBrand> getAliasBrandMapByNames(@RequestBody Set<String> names,@Param("authToken") String authToken);
	
	/**
	 * 批量验证品牌(包含别名)是否存在
	 * @param brandNameList
	 * @param authToken
	 * @return
	 * @since 2017年11月28日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/products/brands/existBrandName")
	@Headers({"Authorization: Basic {authToken}"})
	public Map<String,ProductBrand> existBrandNameList(@RequestBody List<String> brandNameList,@Param("authToken") String authToken);
}
