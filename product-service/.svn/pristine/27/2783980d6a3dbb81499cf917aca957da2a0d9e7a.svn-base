package com.yikuyi.product.category;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.vo.ProductCategoryParent;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 分类接口
 * @author zr.wanghong
 */
public interface ICategoryResourceV2 {		
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return 子分类列表
	 */
	@ApiOperation(value = "根据类别ID查询子分类列表", notes = "根据类别ID查询子分类列表", response = ProductCategory.class,responseContainer="List")
	@ApiResponses(value ={@ApiResponse(code = 404, message = "访问的分类不存在", response = Void.class) })
	@RequestMapping(method=RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductCategory> getChildrenById(@ApiParam("父级类别ID") Integer parentCateId);
	
	
	/**
	 * 根据名称和级别批量查询分类
	 * 
	 * @param cateNames
	 * @param cateLevel
	 * @return
	 * @since 2018年2月3日
	 * @author jik.shu@yikuyi.com
	 */
	public List<ProductCategoryParent> getParentsByNames(@ApiParam Set<String> cateNames , @ApiParam(value="cateLevel",required=true) ProductCategoryLevel cateLevel);
}
