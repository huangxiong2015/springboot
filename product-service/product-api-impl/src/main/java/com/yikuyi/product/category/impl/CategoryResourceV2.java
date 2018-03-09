package com.yikuyi.product.category.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.product.category.ICategoryResourceV2;
import com.yikuyi.product.category.manager.CategoryManager;

@RestController
@RequestMapping("v2/products/categories")
public class CategoryResourceV2 implements ICategoryResourceV2 {

	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return 子分类列表
	 */
	@Override
	@RequestMapping(value = "/children", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductCategory> getChildrenById(@RequestParam(value="parentCateId",required=false)  Integer parentCateId) {
		return categoryManager.getChildrenbyCondition(parentCateId);
	}
	
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return 子分类列表
	 */
	@Override
	@RequestMapping(value = "/getParentsByNames", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public List<ProductCategoryParent> getParentsByNames(@RequestBody Set<String> cateNames , @RequestParam(value="cateLevel",required=true) ProductCategoryLevel cateLevel) {
		return categoryManager.findCategoryByNameAndLevle(cateNames,cateLevel);
	}

}
