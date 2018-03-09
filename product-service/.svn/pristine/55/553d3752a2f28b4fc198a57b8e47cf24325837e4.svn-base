package com.yikuyi.product.brand;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.yikuyi.brand.model.FuzzySearchType;
import com.yikuyi.brand.model.ProductBrand;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 品牌相关服务
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年2月23日
 */
public interface IBrandResource {
	
	@ApiOperation(value = "根据Id查询单个品牌详情信息", notes = "根据Id查询单个品牌详情信息", response = ProductBrand.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的品牌详情不存在", response = Void.class) })
	public ProductBrand findById(@ApiParam("品牌ID") Integer id);
	
	@ApiOperation(value = "查询品牌列表信息", notes = "查询品牌列表信息", response = ProductBrand.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的品牌列表信息不存在", response = Void.class) })
	public List<ProductBrand> findAll();
	
	@ApiOperation(value = "查询品牌列表信息,包含品牌logo和描述信息", notes = "查询品牌列表信息,包含品牌logo和描述信息", response = ProductBrand.class,responseContainer = "List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的品牌列表信息不存在", response = Void.class) })
	public List<ProductBrand> findAllByContidion();
	
	@ApiOperation(value = "根据查询条件查询品牌列表信息", notes = "查询品牌列表信息", response = ProductBrand.class,responseContainer = "PageInfo")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class),
			@ApiResponse(code = 404, message = "对应页面不存在", response = Void.class)})
	@RequestMapping(method=RequestMethod.GET)
	public PageInfo<ProductBrand> getBrandList(@ApiParam(value = "页数") @RequestParam(value="page",required=false,defaultValue="1") int page,
                                               @ApiParam(value = "每页条数") @RequestParam(value="size",required=false,defaultValue="20") int size,
                                               @ApiParam(value = "品牌名称") @RequestParam(value="brandName",required=false) String brandName,
                                               @ApiParam(value = "创建人") @RequestParam(value="creator",required=false) String creator,
                                               @ApiParam(value = "创建开始时间") @RequestParam(value="startDate",required=false) String startDate,
                                               @ApiParam(value = "创建结束时间") @RequestParam(value="endDate",required=false) String endDate);
	
	@ApiOperation(value = "新增品牌信息", notes = "新增品牌信息", response = ProductBrand.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "品牌不存在", response = Void.class)})
	@RequestMapping(method=RequestMethod.POST,produces = "application/json; charset=utf-8")
    public ProductBrand addBrand(@ApiParam("品牌信息") ProductBrand info) throws BusinessException;
	
	@ApiOperation(value = "修改品牌信息", notes = "修改品牌信息", response = ProductBrand.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "不存在指定id的品牌，因此无法获取数据", response = Void.class)})
	@RequestMapping(method=RequestMethod.PUT)
	public ProductBrand updateBrand(@ApiParam(value = "品牌ID", required = true) @PathVariable("id") Integer id,
			                        @ApiParam("品牌信息") ProductBrand info) throws BusinessException;
	
	@ApiOperation(value = "缓存所有的品牌", notes = "缓存所有的品牌", response = String.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的品牌列表不存在", response = Void.class) })
	public String cacheBrand();
	
	@ApiOperation(value = "查询所有品牌和别名", notes = "查询所有品牌和别名", response = ProductBrand.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的品牌信息不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET)
	public Map<String,ProductBrand> getAliasBrandMap();
	
	@ApiOperation(value = "刷新品牌别名缓存", notes = "刷新品牌别名缓存", response = Void.class)
	@RequestMapping(method=RequestMethod.PUT)
	public void initAliasBrandMap();
	
	/**
	 * 根据别名查询品牌
	 */
	@ApiOperation(value = "刷新品牌别名缓存", notes = "刷新品牌别名缓存", response = Void.class)
	public Map<String, ProductBrand> getAliasBrandMapByNames(@ApiParam(value = "names") Set<String> names);
	
	/**
	 * 批量验证品牌(包含别名)是否存在
	 * @param manufacturerPartNumberList
	 * @since 2017年11月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "批量验证品牌(包含别名)是否存在", notes = "批量验证品牌(包含别名)是否存在", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public Map<String,ProductBrand> existBrandNameList(@ApiParam(value = "型号集合")List<String> brandNameList);
	
	
	@ApiOperation(value = "根据品牌名称或别名的部分关键字模糊搜索品牌", notes = "作者：王洪<br>根据品牌名称或别名的部分关键字模糊搜索品牌", response = ProductBrand.class,responseContainer = "List")
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public List<ProductBrand> findbyAlias(@ApiParam(value="关键字(支持品牌名称或别名模糊搜索)")String keyword,
										  @ApiParam(value="是否支持别名搜索（Y/N），默认支持Y")String isSupportAlias, 
										  @ApiParam(value="模糊搜索的类型，默认全模糊FULL_FUZZY")FuzzySearchType fuzzySearchType);
}
