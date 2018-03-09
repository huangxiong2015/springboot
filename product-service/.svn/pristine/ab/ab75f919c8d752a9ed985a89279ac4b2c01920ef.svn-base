package com.yikuyi.product.category.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.product.base.ProductApplicationTestBase;

public class CategoryResourceTest extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 
	
	private String host;

	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 测试查询全部分类
	 * 
	 * @since 2017年2月24日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetAllCategory(){
		System.out.println("\n调用查询全部分类--(GET)"+host + "/v1/products/categories/list");
		this.mockPartyService();
		@SuppressWarnings("unchecked")
		List<ProductCategoryChild> result = restTemplate.getForObject(host + "/v1/products/categories/list?status=1", List.class);
		Assert.assertNotNull(result);
	}

}
