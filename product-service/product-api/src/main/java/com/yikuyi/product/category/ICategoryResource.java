package com.yikuyi.product.category;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.category.vo.ProductCategoryVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 分类接口
 * @author zr.wanghong
 */
public interface ICategoryResource {		
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return 子分类列表
	 */
	@ApiOperation(value = "根据类别ID查询子分类列表", notes = "根据类别ID查询子分类列表", response = ProductCategory.class,responseContainer="List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的分类不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductCategory> getChildrenById(@ApiParam("父级类别ID") Integer parentCateId);
	
	@ApiOperation(value = "根据类别ID查询分类", notes = "根据类别ID查询分类", response = ProductCategory.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的分类不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public ProductCategory findById(@ApiParam("当前类别ID") Integer cateId);
	
	@ApiOperation(value = "批量查询类别信息",notes="批量查询类别信息", response = ProductCategoryParent.class, responseContainer = "List")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<ProductCategoryParent> getListByIds(@ApiParam(value = "类别Ids") List<Long> ids);
	
	@ApiOperation(value = "查询全部分类信息",notes="查询全部分类信息", response = ProductCategoryChild.class,responseContainer="List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "不存在分类信息", response = Void.class) })
	@RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public List<ProductCategoryChild> getAllCategory(@ApiParam(value = "状态的数组,用于查询是否显示的数据，1显示 0不显示")Integer[] status);
	
	
	@ApiOperation(value = "更新分类", notes = "更新分类", response = ProductCategoryVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的分类不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT,produces = "application/json; charset=utf-8")
	public ProductCategory update(ProductCategoryVo category)throws BusinessException;
	
	@ApiOperation(value = "新增分类", notes = "新增分类", response = ProductCategoryVo.class)
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的分类不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.PUT,produces = "application/json; charset=utf-8")
	public ProductCategoryVo add(ProductCategoryVo category)throws BusinessException;
	
	@ApiOperation(value = "刷新分类redis缓存",notes="刷新分类redis缓存", response = Void.class)
	@RequestMapping(method=RequestMethod.PUT)
	public void initAliasCategoryMap();
}
