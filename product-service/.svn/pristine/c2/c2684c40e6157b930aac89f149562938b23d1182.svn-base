package com.yikuyi.product;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import com.yikuyi.brand.model.ProductBrand;
import feign.FeignException;

public class BrandResourceTest extends BaseTest {

	
	@Test(expected=FeignException.class)
	public void findAll(){
		List<ProductBrand> list = super.productClient.brandResource().findAll();
		Assert.assertNotNull(list);
	}
}
