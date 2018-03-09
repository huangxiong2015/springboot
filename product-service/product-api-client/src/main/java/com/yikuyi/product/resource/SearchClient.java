/*
 * Created: 2017年6月30日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.resource;

import java.util.Map;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface SearchClient {
	@Headers({"Accept-Encoding:gzip, deflate"})
	@RequestLine("GET /v1/inventory?keyword={keyword}&vendorId={vendorId}&manufacturer={manufacturer}&cat={cat}&sort={sort}&page={page}&pageSize={pageSize}&showQty={showQty}&minQty={minQty}")
	public Map<String, Object> search(
			@Param("keyword") String keyword,
			@Param("vendorId") String vendorId,
			@Param("manufacturer") String manufacturer,
			@Param("cat") String cat,
			@Param("sort") String sort,
			@Param("page") int page,
			@Param("pageSize") int pageSize,
			@Param("showQty") String showQty,
			@Param("minQty") String minQty);
}
