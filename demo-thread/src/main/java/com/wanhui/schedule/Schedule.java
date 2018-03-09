package com.wanhui.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wanhui.processor.DigikeyProcessor;
import com.wanhui.processor.pipeline.DigikeyDetailPipeline;

import us.codecraft.webmagic.Spider;

@Component
public class Schedule {
	@Value("${digikey.cate.url}")
	private String urls;
	
	@Autowired
	DigikeyDetailPipeline detailPipeline;
	
	@Scheduled(cron="0 53 17 * * ?")//每天22点0分执行
	public void executeCrawlerTask(){
			Spider.create(new DigikeyProcessor()).addPipeline(detailPipeline)
			//从"https://www.digikey.com/products/en/discrete-semiconductor-products/thyristors-diacs-sidacs/274"开始抓
//			.addUrl(urls.split(","))
			.addUrl("https://www.digikey.com/product-detail/en/nexperia-usa-inc/BAV99235/1727-4311-1-ND/2209921")
			.thread(5)
			.run();
	}
}
