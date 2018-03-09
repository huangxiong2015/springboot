package com.yikuyi.product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.product.vo.ProductVo;

public class ProductResourceTest extends BaseTest {

	@Test
	public void getFullInfoByIds(){
		List<String> listIds = null;
		String[] strs = new String[] {"1486457802360531", "1483689358323013", "1483689549420927", "1482150543441696", "1482150544613179"};
		listIds = Arrays.asList(strs);
		List<ProductVo> listData = productClient.productResource().getFullInfoByIds(listIds);
		Assert.assertNotNull(listData);
	}
	
	@Test
	public void activityTaskTest(){
		productClient.activityProductResource().activityTask();
	}
	
	@Test
	public void test(){
		Map<String, Object> saleProductList = productClient.productResource().getSaleProductList(null, "3352V-1-104", "Bourns", "mouser", "mouser-100", null, null, null, null, null, null, 1, 20, "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertNotNull(saleProductList);
	}
	
	
}
