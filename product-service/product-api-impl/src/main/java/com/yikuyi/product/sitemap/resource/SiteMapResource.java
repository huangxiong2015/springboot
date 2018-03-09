package com.yikuyi.product.sitemap.resource;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.sitemap.manager.SiteMapManager;

@RestController
@RequestMapping("v1/sitemap")
public class SiteMapResource{

	@Autowired
	private SiteMapManager siteMapManager;
	
	/**
	 * 生成sitmap文件
	 * @return
	 * @since 2018年2月7日
	 * @author tongkun@yikuyi.com
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public Map<String,List<String>> generateSiteMap() {
		return siteMapManager.generateSiteMap(null);
	}
}