package com.yikuyi.product.brand.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.brand.IBrandResource;
import com.yikuyi.product.brand.manager.BrandManager;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/products")
public class BrandResource implements IBrandResource{

	@Autowired
	private BrandManager brandManager;
	
	@Override
	@RequestMapping(value ="/brands/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ProductBrand findById(@PathVariable("id") Integer id) {
		return brandManager.findById(id);
	}

	@Override
	@RequestMapping(value="/brands", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductBrand> findAll() {
		return brandManager.findAll();
	}

	@Override
	@RequestMapping(value="/fullInfoBrands", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductBrand> findAllByContidion() {
		return brandManager.findAllByCondition();
	}
	
	/**
	 * 查询品牌列表信息
	 */
	@Override
	@RequestMapping(value="/manufacturers", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON)
	public PageInfo<ProductBrand> getBrandList(@RequestParam(value="page",required=false,defaultValue="1") int page, 
			                               @RequestParam(value="size",required=false,defaultValue="20") int size, 
			                               @RequestParam(value="brandName",required=false) String brandName, 
			                               @RequestParam(value="creator",required=false) String creator, 
			                               @RequestParam(value="startDate",required=false) String startDate,
			                               @RequestParam(value="endDate",required=false) String endDate) {
		return brandManager.getBrandList(brandName, creator, page, size, startDate, endDate);
	}

	/**
	 * 新增品牌
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/manufacturers",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public ProductBrand addBrand(@RequestBody ProductBrand info) throws BusinessException {
		//获取制造商Id的最大值
		Integer id = brandManager.getMaxBrandId();
		info.setStatus(1);
		info.setCreator(RequestHelper.getLoginUser().getUsername());
		info.setCreatedDate(new Date());
		info.setLastUpdateUser(RequestHelper.getLoginUser().getUsername());
		info.setLastUpdateDate(new Date());
		return brandManager.saveBrand(id,info);
	}

	/**
	 * 修改品牌
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/manufacturers/{id}",method=RequestMethod.PUT,produces = "application/json; charset=utf-8")
	public ProductBrand updateBrand(@PathVariable("id") Integer id, @RequestBody ProductBrand info) throws BusinessException {
		info.setLastUpdateUser(RequestHelper.getLoginUser().getUsername());
		info.setLastUpdateDate(new Date());
		return brandManager.updateBrand(id, info);
	}

	/**
	 * 缓存所有的品牌
	 */
	@Override
	@RequestMapping(value="/brands/cache", method=RequestMethod.GET, produces = "application/json; charset=utf-8")
	public String cacheBrand() {
		return brandManager.cacheBrands();
	}

	/**
	 * 查询所有的品牌和别名
	 */
	@Override
	@RequestMapping(value="/brands/aliasMap", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public Map<String, ProductBrand> getAliasBrandMap() {
		return brandManager.getAliasBrandMap();
	}
	
	/**
	 * 查询所有的品牌和别名
	 */
	@Override
	@RequestMapping(value="/brands/aliasMap", method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	public Map<String, ProductBrand> getAliasBrandMapByNames(@RequestBody Set<String> names) {
		return brandManager.getBrandByAliasName(names);
	}
	
	/**
	 * 用别名获取制造商对象
	 * @param vendorId 供应商id，为空则查询通用别名，不为空则查询指定供应商的专用别名
	 * @param alias 别名
	 * @return 获得的品牌对象
	 * @since 2017年11月1日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "用别名获取制造商对象", notes = "用别名获取数据库中已有的制造商对象", response = ProductBrand.class)
	@RequestMapping(value="/brands/getByAlias", method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ProductBrand getByAlias(@ApiParam(value = "供应商id，为空则查询通用别名，不为空则查询这个供应商的专用别名") @RequestParam(value="vendorId",required=false) String vendorId,
			@ApiParam(value = "制造商别名") @RequestParam("alias") String alias){
		ProductBrand pb = brandManager.getBrandByAliasName(vendorId, alias);
		return pb;
	}
	
	@Override
	@RequestMapping(value="/brands/cache", method=RequestMethod.PUT)
	public void initAliasBrandMap() {
		brandManager.initAliasBrand();
	}
	
	/**
	 * 批量验证品牌(包含别名)是否存在
	 */
	@Override
	@RequestMapping(value="/brands/existBrandName" ,method = RequestMethod.POST)
	public Map<String,ProductBrand> existBrandNameList(@RequestBody List<String> brandNameList) {
		return brandManager.existBrandNameList(brandNameList);
	}
	
	@Override
	@RequestMapping(value="/brands/alias" ,method = RequestMethod.GET)
	public List<ProductBrand> findbyAlias(@RequestParam("keyword") String keyword,
										  @RequestParam(value="isSupportAlias",defaultValue="Y",required = false)String isSupportAlias, 
									      @RequestParam(value="fuzzySearchType",defaultValue="FULL_FUZZY",required = false)FuzzySearchType fuzzySearchType){
		return brandManager.findByAilas(keyword,isSupportAlias,fuzzySearchType);
	}
}
