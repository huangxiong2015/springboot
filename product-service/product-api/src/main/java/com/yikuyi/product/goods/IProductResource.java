package com.yikuyi.product.goods;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductRequest;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 批量查询商品信息相关的服务
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
public interface IProductResource {
	/**
	 * 批量查询商品基本信息
	 * 
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "批量查询商品基本信息,不包含交期,实时价格等", notes = "批量查询商品基本信息,不包含交期,实时价格等" ,response = List.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<Product> getBasicInfoByIds(List<String> ids);

	/**
	 * 批量查询商品详细信息,包含价格,交期等
	 * 
	 * @params ids
	 * @author zr.wujiajun
	 */
	@ApiOperation(value = "批量查询商品信息,包含实时价格,交期等", notes = "实时查询商品信息,包含实时价格,交期等" ,response = ProductVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.POST)
	public List<ProductVo> getFullInfoByIds(List<String> ids);
	/**
	 * 更新产品
	 * @param list 更新的产品列表
	 * @param updateType
	 * @return
	 * @since 2017年3月20日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "更新产品", notes = "更新产品，根据不同的更新类型，可以更新不同的字段。")
	public String updateProduct(String id,Product product) throws BusinessException;
	/**
	 * 批量创建sku信息
	 * 
	 * @param rawDatas
	 */
	@ApiOperation(value = "批量创建sku信息", notes = "批量创建sku信息")
	public String createProduct(List<RawData> rawDatas);
	
	/**
	 * 根据商品,型号或者分类查询
	 * @param partNum
	 * @param cat
	 * @param cateLevel
	 * @return
	 * @since 2016年12月17日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@ApiOperation(value = "根据型号或者分类查询数据", notes = "根据型号或者分类查询数据" ,response = Product.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public Product matchingProduct(@ApiParam(value = "型号", required = false) String partNum);
	
	/**
	 * 销售中的商品查询
	 * @param partNum
	 * @param cat
	 * @param cateLevel
	 * @return
	 * @since 2017年1月6日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@ApiOperation(value = "查询销售中的商品", notes = "查询销售中的商品,参数id,manufacturerPartNumber,manufacturer,vendorId,keyword,类别等至少输入一个" ,response = ProductVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})	
	@RequestMapping(method=RequestMethod.GET)
	public Map<String, Object> getSaleProductList(@ApiParam(value = "商品编码", required = false) String id,
			@ApiParam(value = "制造商型号", required = false) String manufacturerPartNumber,
			@ApiParam(value = "制造商", required = false) String manufacturer,
			@ApiParam(value = "供应商", required = false) String vendorId,
			@ApiParam(value = "仓库", required = false) String sourceId,
			@ApiParam(value = "大类", required = false) Integer cate1Id,
			@ApiParam(value = "小类", required = false) Integer cate2Id,
			@ApiParam(value = "次小类", required = false) Integer cate3Id,
			@ApiParam(value = "制造商/制造商型号", required = false) String keyword,
			@ApiParam(value = "开始时间", required = false) String startDate,
			@ApiParam(value = "结束时间", required = false) String endDate,
			@ApiParam(value = "是否标准物料", required = false) Boolean standard,
			@ApiParam(value="页码", required=false, defaultValue="1") int page,
			@ApiParam(value="每页记录条数",required=false, defaultValue="20") int pageSize,
			@ApiParam(value="是否有库存（Y/N）",required=false) String hasQty,
			@ApiParam(value="是否有库存（Y/N）",required=false) String isInvalid
			);
	
	/**
	 * 销售中的商品 批量删除（按条件或者勾选ids）
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @return
	 * @throws SystemException
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "批量删除（按条件或者勾选ids）", notes = "查询销售中的商品,参数id,manufacturerPartNumber,manufacturer,vendorId,keyword,类别等至少输入一个" ,response = Boolean.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})	
	public Boolean deleteAll(@ApiParam(value = "商品编码", required = false) String id,
			@ApiParam(value = "制造商型号", required = false) String manufacturerPartNumber,
			@ApiParam(value = "制造商", required = false) String manufacturer,
			@ApiParam(value = "供应商", required = false) String vendorId,
			@ApiParam(value = "仓库", required = false) String sourceId,
			@ApiParam(value = "大类", required = false) Integer cate1Id,
			@ApiParam(value = "小类", required = false) Integer cate2Id,
			@ApiParam(value = "次小类", required = false) Integer cate3Id,
			@ApiParam(value = "制造商/制造商型号", required = false) String keyword,
			@ApiParam(value = "开始时间", required = false) String startDate,
			@ApiParam(value = "结束时间", required = false) String endDate,
			@ApiParam(value = "是否标准物料", required = false) Boolean standard,
			@ApiParam(value="ids，id以逗号分割", required=false) String ids,
			@ApiParam(value="是否有库存（Y/N）",required=false) String hasQty,
			@ApiParam(value="商品是否失效（Y/N）",required=false) String isInvalid
			);
	
	
	/**
	 * 销售中的商品 导出excel
	 * @param id
	 * @param manufacturerPartNumber
	 * @param manufacturer
	 * @param vendorId
	 * @param sourceId
	 * @param cate1Id
	 * @param cate2Id
	 * @param cate3Id
	 * @param keyword
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @return
	 * @since 2017年8月30日
	 * @author injor.huang@yikuyi.com
	 */
	@ApiOperation(value = "导出（按条件或者勾选ids）", notes = "查询销售中的商品,参数id,manufacturerPartNumber,manufacturer,vendorId,keyword,类别等至少输入一个" ,response = String.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})	
	public void exportExcel(@ApiParam(value = "商品编码", required = false) String id,
			@ApiParam(value = "制造商型号", required = false) String manufacturerPartNumber,
			@ApiParam(value = "制造商", required = false) String manufacturer,
			@ApiParam(value = "供应商", required = false) String vendorId,
			@ApiParam(value = "仓库", required = false) String sourceId,
			@ApiParam(value = "大类", required = false) Integer cate1Id,
			@ApiParam(value = "小类", required = false) Integer cate2Id,
			@ApiParam(value = "次小类", required = false) Integer cate3Id,
			@ApiParam(value = "制造商/制造商型号", required = false) String keyword,
			@ApiParam(value = "开始时间", required = false) String startDate,
			@ApiParam(value = "结束时间", required = false) String endDate,
			@ApiParam(value = "是否标准物料", required = false) Boolean standard,
			@ApiParam(value="ids，id以逗号分割", required=false) String ids,
			@ApiParam(value="是否有库存（Y/N）",required=false) String hasQty,
			@ApiParam(value="商品是否失效（Y/N）",required=false) String isInvalid
			)throws BusinessException;
	
	
	@ApiOperation(value = "删除单个销售中的商品", notes = "删除单个销售中的商品" ,response = ProductVo.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void deleteSaleProduct(String id) throws BusinessException;
	
	/**
	 * 查询该供应商是否存在商品
	 * @param vendorId
	 * @return boolean true在商品 false不存在
	 * @since 2017年2月10日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@ApiOperation(value = "查询该供应商是否存在商品,true存在false不存在", notes = "查询该供应商是否存在商品,true存在false不存在" ,response = Boolean.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public boolean isHasProduct(@ApiParam(value = "供应商id", required = true) String vendorId);
	
	
	/**
	 * 推荐相关商品
	 * @param  id 当前商品id	
	 * @since 2017年2月25日
	 * @author zr.wujiajun
	 */
	@ApiOperation(value = "推荐相关商品", notes = "推荐相关商品" ,response = Product.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<ProductVo> recommendOthers(@ApiParam(value = "当前商品id", required = true) String id,@ApiParam(value = "当前商品cat3Id", required = false) String catId);
	
	
	@ApiOperation(value = "同步产品的基础属性数据（品牌和分类）,并同步到搜索引擎", notes = "同步产品的基础属性数据（品牌和分类）,并同步到搜索引擎" ,response = Void.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void synProductProperties(@ApiParam(value = "数据同步的批次ID", required = true) String processId);
	
	@ApiOperation(value = "清理产品库中非标准或为失效的垃圾数据", notes = "清理产品库中非标准或为失效的垃圾数据" ,response = Void.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void cleanNonStandardData();
	
	@ApiOperation(value = "根据关键词搜索联想词", notes = "作者：王洪<br/> 根据输入关键字带出相关的联想词集合" ,response = String.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<String> associaWord(@ApiParam("输入关键字")String keyword);

	@ApiOperation(value = "对一个商品进行管制", notes = "作者：佟昆<br/> 管制的商品会被删除且不能更新" ,response = Void.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	void saleControl(String productId);
	
	/**
	 * 根据供应商ID或者品牌ID查询热销型号数据
	 * @param arg
	 * @param flag
	 * @return
	 * @since 2017年10月26日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "根据供应商ID或者品牌ID查询热销型号数据", notes = "作者：李京<br/> 根据供应商ID或者品牌ID查询热销型号数据" ,response = ProductVo.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<ProductVo> findProductInfobyCondition(@ApiParam(value = "供应商ID或者品牌ID", required = true)String arg,@ApiParam(value = "标识（v:表示根据供应商id查询热销型号数据，b:表示根据品牌id查询热销型号数据）", required = true)String flag);
	
	@ApiOperation(value = "根据供应商ID或者品牌ID查询最新商品数据", notes ="作者：李京<br/> 根据供应商ID或者品牌ID查询最新商品数据", response = ProductVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public List<ProductVo> findNewProducts(@ApiParam(value = "供应商ID或者品牌ID", required = true)String arg,
			@ApiParam(value = "标识（v:表示根据供应商id查询最新商品数据，b:表示根据品牌id查询最新商品数据）", required = true)String flag);
	
	@ApiOperation(value = "搜索页你是不是想找的接口，根据关键字联想相关的关键词（型号或品牌或分类）", notes ="作者：王洪<br/> 搜索也你是不是想找的接口，根据关键字联想相关的关键词（型号或品牌或分类", response = String.class,responseContainer="List")
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public List<String> searchRecommond(@ApiParam("搜索关键字")String keyword);
	
	
	/**
	 * 批量根据  品牌Id 和 型号  查商品信息
	 * @author injor.huang
	 * @date 2018年1月31日
	 * @param productRequests
	 * @return
	 */
	@ApiOperation(value = "批量根据  品牌Id 和 型号  查商品信息", notes = "根据  品牌Id 和 型号  查商品" ,response = ProductVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<ProductVo> findBatchMfrIdAndMpm(@ApiParam("制造商名称")List<ProductRequest> productRequests );
	
}
