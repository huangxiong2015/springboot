package com.yikuyi.product;

import org.junit.Before;

public class BaseTest {

	public ProductClientBuilder productClient;
	
	@Before
	public void init(){
		productClient = new ProductClientBuilder("http://192.168.1.110:27083");
	}

}
