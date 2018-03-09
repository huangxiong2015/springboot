///*
// * Created: 2017年7月7日
// *
// * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
// * Copyright (c) 2015-2017 
// * License, Version 1.0. Published by Yikuyi IT department.
// *
// * For the convenience of communicating and reusing of codes,
// * any java names, variables as well as comments should be written according to the regulations strictly.
// */
//package com.yikuyi.product.material.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.yikuyi.product.material.IManufactureTransferResource;
//import com.yikuyi.product.material.bll.ManufactureTransferManager;
//
//@RestController
//@RequestMapping("v1/imports")
//public class ManufactureTransferResource implements IManufactureTransferResource {
//
//	@Autowired
//	private ManufactureTransferManager transferManager ;
//	
//	@Override
//	@RequestMapping(value = "/manufacture/transfer", method = RequestMethod.GET)
//	public String transferManufacture() {
//		return transferManager.transfer();
//	}
//}
