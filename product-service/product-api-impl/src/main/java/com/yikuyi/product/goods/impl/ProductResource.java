package com.yikuyi.product.goods.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.document.bll.SynProductPropertiesManager;
import com.yikuyi.product.goods.IProductResource;
import com.yikuyi.product.goods.manager.ProductAsyncManager;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/products")
public class ProductResource implements IProductResource {
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private ProductAsyncManager productAsyncManager;
	
	@Autowired  
    private SynProductPropertiesManager spuSkuManager;  
	
	/**
	 * 根据IDS批量查询商品的基本信息
	 * @param ids
	 * @return
	 * @throws JsonProcessingException
	 * @author zr.wujiajun
	 */
	@Override
	@RequestMapping(value = "/batch/basic", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<Product> getBasicInfoByIds(@RequestBody List<String> ids) {
		return productManager.findBasicInfo(ids);
	}
	
	/**
	 * 根据ids批量查询商品的全部信息,包含价格,交期等
	 * @param ids
	 * @return
	 * @author zr.wujiajun
	 */	
	@Override
	@RequestMapping(value = "/batch/full", method = RequestMethod.POST,produces = "application/json; charset=utf-8")	
	public List<ProductVo> getFullInfoByIds(@RequestBody List<String> ids) {
		return productManager.findFullInfo(ids);
	}


	@RequestMapping(value="/{id}",method = RequestMethod.PUT,produces = "application/json; charset=utf-8")	
	@Override
	public String updateProduct(@PathVariable(required=true) String id,@RequestBody Product product) throws BusinessException{
		if(StringUtils.isNotBlank(id)){
			product.setId(id);
		}
		return productManager.updateProductInfo(product);
	}

	@ApiOperation(value = "根据raw数据创建商品", response = String.class)
	@RequestMapping(method = RequestMethod.POST,produces = "application/json; charset=utf-8")	
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public String createProduct(@RequestBody List<RawData> rawDatas) {
		return productManager.createProduct(rawDatas , MaterialVoType.UPDATE_DATA);
	}

	@Override
	@RequestMapping(value = "/matchingProduct", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public Product matchingProduct(@RequestParam("partNum") String partNum) {
		return productManager.matchingProduct(partNum);
	}
	
	/**
	 * 查询销售中的商品
	 * @return Map集合，包含以下内容:
	 * @return page页码
	 * @return pageSize每页记录条数
	 * @return total记录总数
	 * @return productList 商品记录
	 * @author zr.wujiajun
	 */
	@Override
	@RequestMapping(value = "/getSaleProductList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public Map<String, Object> getSaleProductList(@RequestParam(value="id", required=false) String id,
			@RequestParam(value="manufacturerPartNumber", required=false) String manufacturerPartNumber,
			@RequestParam(value="manufacturer", required=false) String manufacturer,
			@RequestParam(value="vendorId", required=false) String vendorId,
			@RequestParam(value="sourceId", required=false) String sourceId,
			@RequestParam(value="cate1Id", required=false) Integer cate1Id,
			@RequestParam(value="cate2Id", required=false) Integer cate2Id, 
			@RequestParam(value="cate3Id", required=false) Integer cate3Id,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="startDate", required=false) String startDate,
			@RequestParam(value="endDate", required=false) String endDate,
			@RequestParam(value="standard", required=false) Boolean standard,
			@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=false, defaultValue="20") int pageSize,
			@RequestParam(value="hasQty",required=false) String hasQty,
			@RequestParam(value="isInvalid",required=false) String isInvalid
			){
		if(StringUtils.isBlank(id) && StringUtils.isBlank(manufacturerPartNumber) && StringUtils.isBlank(manufacturer) && StringUtils.isBlank(vendorId) ){
			if( StringUtils.isBlank(keyword) && cate1Id==null && cate2Id==null && cate3Id==null && standard == null){
				if(StringUtils.isBlank(sourceId) && StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(hasQty) && StringUtils.isBlank(isInvalid)){
					Map<String,Object> paramErrorMap = new HashMap<>();
					paramErrorMap.put("total", 0);
					paramErrorMap.put("productList", new ArrayList<ProductVo>());
					paramErrorMap.put("reason", "至少输入一个查询条件，请确认参数是否正确");
					return paramErrorMap; 
				}				
			}			
		}	
		try {
			return productManager.getSaleProductList(id, manufacturerPartNumber, manufacturer, vendorId, sourceId, cate1Id, cate2Id, cate3Id, keyword, startDate,endDate,standard,page, pageSize,hasQty,isInvalid);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	@Override
	@RequestMapping(value = "/isHasProduct", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public boolean isHasProduct(@RequestParam(value="vendorId", required=true) String vendorId) {		
		return productManager.isHasProduct(vendorId);
	}

	@Override
	@RequestMapping(value="/recommendOthers",method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductVo> recommendOthers(@RequestParam(value="id", required=true) String id,@RequestParam(value="catId", required=false) String catId) {
		return productManager.recommendOthers(id,catId);
	}

	
	/**
	 * 删除单个销售中的商品
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	public void deleteSaleProduct(@PathVariable(required=true) String id) throws BusinessException {
		Product prod = new Product();
		prod.setStatus(0);
		updateProduct(id, prod);
	}
	
	/**
	 * 删除所有数据
	 */
	@Override
	@RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
	public Boolean deleteAll(@RequestParam(value="id", required=false) String id,
			@RequestParam(value="manufacturerPartNumber", required=false) String manufacturerPartNumber,
			@RequestParam(value="manufacturer", required=false) String manufacturer,
			@RequestParam(value="vendorId", required=false) String vendorId,
			@RequestParam(value="sourceId", required=false) String sourceId,
			@RequestParam(value="cate1Id", required=false) Integer cate1Id,
			@RequestParam(value="cate2Id", required=false) Integer cate2Id, 
			@RequestParam(value="cate3Id", required=false) Integer cate3Id,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="startDate", required=false) String startDate,
			@RequestParam(value="endDate", required=false) String endDate,
			@RequestParam(value="standard", required=false) Boolean standard,
			@RequestParam(value="ids", required=false)String ids,
			@RequestParam(value="hasQty",required=false) String hasQty,
			@RequestParam(value="isInvalid",required=false) String isInvalid) {
		if(StringUtils.isBlank(id) && StringUtils.isBlank(manufacturerPartNumber) && StringUtils.isBlank(manufacturer) && StringUtils.isBlank(vendorId) ){
			if( StringUtils.isBlank(keyword) && cate1Id==null && cate2Id==null && cate3Id==null && standard == null){
				if(StringUtils.isBlank(sourceId) && StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(hasQty) && StringUtils.isBlank(isInvalid)){
					return false; 
				}				
			}			
		}	
		try {
			return productManager.deleteAll(id, manufacturerPartNumber, manufacturer, vendorId, sourceId, cate1Id, cate2Id, cate3Id, keyword, startDate,endDate,standard,ids,hasQty,isInvalid);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	
	/**
	 * 同步产品的基础属性数据（品牌和分类），并同步到搜索引擎
	 * @throws BusinessException
	 * @since 2017年5月9日
	 * @author zr.wanghong
	 */
	@Override
	@RequestMapping(value="/properties/sync/{processId}",method = RequestMethod.GET)
	public void synProductProperties(@PathVariable(required=true,value="processId") String processId){
		spuSkuManager.synProductProperties(processId);
	}
	
	/**
	 * 一个月以外的非标准库存数据从正式表物理清除，写入到备份表里面
	 */
	@Override
	@RequestMapping(value="/nonstandard/clean",method = RequestMethod.GET)
	public void cleanNonStandardData(){
		productAsyncManager.cleanNonStandardData();
	}
	
	/**
	 * 搜索引擎根据关键词带出联想词集合
	 */
	@Override
	@RequestMapping(value="/keyword/association",method = RequestMethod.GET)
	public List<String> associaWord(@RequestParam(required = true) String keyword){
		return productManager.associaWord(keyword);
	}
	
	@Override
	@RequestMapping(value="/saleControl/{productId}",method = RequestMethod.GET)
	public void saleControl(@PathVariable(required=true,value="productId") String productId){
		productManager.saleControl(productId);
	}

	/**
	 * 销售中的商品 导出excel
	 */
	@Override
	@RequestMapping(value="/export",method = RequestMethod.GET)
	public void exportExcel(@RequestParam(value="id", required=false) String id,
			@RequestParam(value="manufacturerPartNumber", required=false) String manufacturerPartNumber,
			@RequestParam(value="manufacturer", required=false) String manufacturer,
			@RequestParam(value="vendorId", required=false) String vendorId,
			@RequestParam(value="sourceId", required=false) String sourceId,
			@RequestParam(value="cate1Id", required=false) Integer cate1Id,
			@RequestParam(value="cate2Id", required=false) Integer cate2Id, 
			@RequestParam(value="cate3Id", required=false) Integer cate3Id,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="startDate", required=false) String startDate,
			@RequestParam(value="endDate", required=false) String endDate,
			@RequestParam(value="standard", required=false) Boolean standard,
			@RequestParam(value="ids", required=false)String ids,
			@RequestParam(value="hasQty", required=false)String hasQty,
			@RequestParam(value="isInvalid",required=false) String isInvalid
			)throws BusinessException {
		if(StringUtils.isBlank(id) && StringUtils.isBlank(manufacturerPartNumber) && StringUtils.isBlank(manufacturer) && StringUtils.isBlank(vendorId) ){
			if( StringUtils.isBlank(keyword) && cate1Id==null && cate2Id==null && cate3Id==null && standard == null){
				if(StringUtils.isBlank(sourceId) && StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) && StringUtils.isBlank(hasQty) && StringUtils.isBlank(isInvalid)){
					return; 
				}				
			}			
		}
		String userName = RequestHelper.getLoginUser().getUsername();
		productManager.export(new ProductRequest(id, manufacturerPartNumber, manufacturer, vendorId, sourceId,
				cate1Id, cate2Id, cate3Id, keyword,startDate, endDate,standard, null,hasQty,isInvalid),userName);
	}
	
	/**
	 * 根据供应商ID或者品牌ID查询热销型号数据
	 * @param arg
	 * @param flag
	 * @return
	 * @since 2017年10月26日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/findProductInfos",method = RequestMethod.GET)
	public List<ProductVo> findProductInfobyCondition(@RequestParam(value="arg", required=true)String arg, @RequestParam(value="flag", required=true)String flag) {
		return productManager.findProductInfobyCondition(arg,flag);
	}		
	
	/**
	 * 规定时间外库存下架批处理
	 * @param vendorId 供应商id，如果为空则表示所有供应商
	 * @param day 规定的时间，单位：天
	 * @return 执行结果描述
	 */
	@ApiOperation(value = "批量价格失效接口", notes = "按供应商和时间将价格批量失效掉" ,response = Product.class)
	@RequestMapping(value="/downShelfStockBatch",method = RequestMethod.GET)
	@ResponseBody
	public String downShelfStockBatch(@RequestParam(value="vendorId", required=false)String vendorIds,@RequestParam(value="day", required=true)Integer day) {
		//计算失效时间
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0-day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//依次执行所有指定的供应商
		long count = 0;
		String[] vendorIdArray = vendorIds.split(",");
		for(String vendorId:vendorIdArray){
			count += productManager.downShelfStock(vendorId, cal.getTimeInMillis());
		}
		return "本次下架库存数量："+count;
	}	
	
	@RequestMapping(value="/adviceInvalidProduct",method = RequestMethod.GET)
	public void adviceInvalidProduct(){
		productManager.adviceInvalidProduct();
	}

	@Override
	@RequestMapping(value="/findNewProducts",method = RequestMethod.GET)
	public List<ProductVo> findNewProducts(@RequestParam(value="arg", required=true)String arg, @RequestParam(value="flag", required=true)String flag) {
		return productManager.findNewProducts(arg,flag);
	}
	
	@Override
	@RequestMapping(value="/searchRecommond",method = RequestMethod.GET)
	public List<String> searchRecommond(@RequestParam(value="keyword", required=true)String keyword){
		return productManager.searchRecommond(keyword);
	}
	
	/**
	 * 批量根据  品牌Id 和 型号  查商品信息
	 * @author injor.huang
	 */
	@Override
	@RequestMapping(value = "/batch/findBatchMfrIdAndMpm", method = RequestMethod.POST,produces = "application/json; charset=utf-8")	
	public List<ProductVo> findBatchMfrIdAndMpm(@RequestParam(value="productRequests",required=true) List<ProductRequest> productRequests) {
		return productManager.findBatchMfrIdAndMpm(productRequests);
	}
}
