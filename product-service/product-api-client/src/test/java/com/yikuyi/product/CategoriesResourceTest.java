package com.yikuyi.product;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.model.ProductCategory.ProductCategoryLevel;
import com.yikuyi.category.vo.ProductCategoryParent;

public class CategoriesResourceTest extends BaseTest {

	
	@Test
	public void findAll(){
		ProductCategory cate = super.productClient.categoryResource().findById(103,"YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(cate != null);
	}
	
	@Test
	public void findAll2(){
		Set<String> cateNames = new HashSet<>(Arrays.asList("步进电机"));
		List<ProductCategoryParent> cates = super.productClient.categoryResource().getParentsByNames(cateNames, ProductCategoryLevel.CAT_ELEVEL_3);
		Assert.assertTrue(cates != null);
	}
}
