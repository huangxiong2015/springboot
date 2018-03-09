package com.wanhui.processor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wanhui.processor.impl.DigiKeyParserImpl;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
@Component
public class DigikeyProcessor implements PageProcessor {

	private final Logger logger = LoggerFactory.getLogger(DigikeyProcessor.class);
	@Autowired
	private DigiKeyParserImpl digiKeyParserImpl; 
	
	private Site site = Site.me()
			.setDomain("www.digikey.com")
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
			.setRetryTimes(3)
			.setSleepTime(2000)
			.setTimeOut(6000)
			// 需要处理的状态码集合
			.setAcceptStatCode(new HashSet<Integer>(Arrays.asList(200,403,404,503)));;
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		if (this.isDetailPage(page)) {
			// 详情页
			new DigiKeyParserImpl().doDeatailProcess(page);
//			digiKeyParserImpl.doDeatailProcess(page);
			
		} else if (this.isCateGoryRootPage(page)) {
			// 分类总表
			doCategoryRootProcess(page);
		} else if (this.isCategoryListPage(page)) {
			// 分类产品列表页
			doCategoryListProcess(page);
		}
		else {
			logger.error("Url:{},not matched any pattern!",page.getUrl());
			page.setSkip(true);
		}

	}
	
	private boolean isDetailPage(Page page) {
		return page.getUrl().regex(getRootPattern() + "/product-detail/en/.+").match();
	}
	
	private String getRootPattern() {
		return "(http|https)://" + this.getSite().getDomain();
	}
	
	private boolean isCateGoryRootPage(Page page) {
		return page.getUrl().regex(getRootPattern() + "/products/en(/|.{0})$").match();
	}
	
	private void doCategoryRootProcess(Page page) {
		switch (page.getStatusCode()) {
		case 200:
			// 分类列表中的所有分类页面
			List<String> categoryList = page.getHtml().xpath("//ul[@class=catfiltersub]//li//a").links().all();
			// 将分类列表页加入
			categoryList.stream().forEach(url -> page.addTargetRequest(url));
			break;
		case 403:
		case 500:
		case 503:
//			this.tagedTryAgain(page, RequestPriority.LOW);
			break;
		default:
			break;
		}
		page.setSkip(true);
	}
	
	
	private void doCategoryListProcess(Page page) {
		switch (page.getStatusCode()) {
		case 200:
			// 分类列表中的所有分类页面
			List<String> productList = page.getHtml().xpath("//table[@id=productTable]//tbody[@id=lnkPart]//tr//td[@class=tr-mfgPartNumber]//a").links().all();
			// 将分类列表页加入
			productList.stream().forEach(url -> page.addTargetRequest(url));
			List<String> nextPageList = page.getHtml().xpath("//div[@class=mid-wrapper]//div[@class=page-slector]//div[@class=paging]//div[@class=paging-inner]//a[@class=Next]").links().all();
			nextPageList.stream().findFirst().ifPresent(url -> page.addTargetRequest(url));
			break;
		case 403:
		case 500:
		case 503:
//			this.tagedTryAgain(page, RequestPriority.LOW);
			break;
		default:
			break;
		}
		page.setSkip(true);
	}

	private boolean isCategoryListPage(Page page) {		
		return page.getUrl().regex(
				getRootPattern() + "/products/en/.+?/.+?/\\d+(.{0}|(/page/\\d+))")
				.match();
	}
}
