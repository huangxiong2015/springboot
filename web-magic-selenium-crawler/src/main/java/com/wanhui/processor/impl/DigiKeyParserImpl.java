package com.wanhui.processor.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selectable;

/**
 * DigiKey详情解析
 * 
 * @author hx
 * @version 1.0.0
 */
@Component
public class DigiKeyParserImpl{
	private static final String IMMEDIATELY_REGEX =".*immediately.*";
	private static final String ROHS_REGEX =".*RoHS Compliant.*";
	
	/**
	 * vendor信息
	 */
	public void getVendors(Page page) {
		//vendor自己的分类
	   	 String category = page.getHtml().xpath("//div[@id=sharebread]//div[@class=breadcrumbs]//a[2]/text()").toString().trim();
	   	 String family = page.getHtml().xpath("//div[@id=sharebread]//div[@class=breadcrumbs]//a[3]/text()").toString().trim();
	   	 JSONArray vendorCategories = new JSONArray();
	   	 JSONObject categoryJSON = new JSONObject();
	   	 JSONObject familyJSON = new JSONObject();
//	   	 categoryJSON.put("id", "");
	   	 categoryJSON.put("id", UUID.randomUUID().toString());
	   	 //categoryJSON.put("level", 1);
	   	 categoryJSON.put("cateLevel", 1);
	   	 //categoryJSON.put("name", category);
	   	 categoryJSON.put("cateName", category);
	   	 categoryJSON.put("status", 1);
	   	
//	   	 familyJSON.put("id", "");
	   	 familyJSON.put("id", UUID.randomUUID().toString());
	   	 familyJSON.put("cateLevel", 2);
	   	 familyJSON.put("cateName", family);
	   	 familyJSON.put("status", 1);
	   	vendorCategories.add(categoryJSON);
		vendorCategories.add(familyJSON);
	   	 
	   	 
	   	 page.putField("vendorCategories", vendorCategories);
	   	// page.putField("family", familyJSON);
	   	 //skuId取数据库旧的，不抓新的
	   	  page.putField("skuId", StringUtils.trimToEmpty(page.getHtml().xpath("//td[@id='reportPartNumber']/text()").toString()));
	}

	
	/**
	 * 英文价格信息
	 */
	public void getEnPrices(Page page) {
		List<String> prices = page.getHtml().xpath("//table[@id=product-dollars]//tr").all();       	
    	/*
    	 * 有存在梯度价格为空的情况
    	 * http://www.digikey.com/product-detail/en/general-cable-carol-brand/181CV600.40.01/181CV600.40.01-ND/2758904
    	 */   
    	JSONArray priceArray = new JSONArray();
    	JSONArray priceLevels = new JSONArray();
    	JSONObject priceJSON = new JSONObject();
    	if(0 == prices.size()){//如果不存在梯度价格
    		priceJSON.put("priceLevels", priceLevels);
        	priceJSON.put("currencyCode", "USD");
        	priceJSON.put("unitPrice", BigDecimal.valueOf(Double.valueOf("0")));
        	priceArray.add(priceJSON);
        	page.putField("priceArray", priceArray);
    		
    	}else{
    		
//    		for (Integer i : IntStream.range(1, prices.size()).toArray()) {
//			}
    		
        	//第0个是标题不需要   		
        	IntStream.range(1, prices.size()).forEach(i -> {
        		JSONObject priceJson = new JSONObject();
        		Html html = Html.create("<table>"+prices.get(i)+"</table>");
        		
        		String priceBreak = StringUtils.trimToEmpty(html.xpath("//tr//td[1]/text()").toString()).replaceAll("[^(0-9)]", "");
        		if(StringUtils.isNotEmpty(priceBreak)){
        			int price = Integer.valueOf(priceBreak);
            		//第一个梯度价格的Unit Price放在span里面 需要特殊处理
        			BigDecimal unitPrice;
            		if(i == 1){//第一个梯度价格在<span>里面取
            			unitPrice = BigDecimal.valueOf(Double.valueOf(html.xpath("//tr//td[2]//span/text()").toString().trim().replaceAll(",", "")));
            		}else{
            			unitPrice = BigDecimal.valueOf(Double.valueOf(html.xpath("//tr//td[2]/text()").toString().trim().replaceAll(",", "")));
            		}
            		
            		priceJson.put("packaging", "");
            		priceJson.put("price", unitPrice);
            		priceJson.put("breakQuantity", price);
            		priceLevels.add(priceJson);
            		
            		if(i ==1){//单价，和梯度价格中的第一个是冗余的
            			//page.putField("unitPrice", unitPrice);
            			priceJSON.put("unitPrice", unitPrice);
            		}
        		}else{
        			priceJson.put("packaging", "");
            		priceJson.put("price", BigDecimal.valueOf(Double.valueOf("0")));
            		priceJson.put("breakQuantity", 0);
            		priceLevels.add(priceJson);
            		//page.putField("unitPrice", Double.valueOf("0"));
            		priceJSON.put("unitPrice", BigDecimal.valueOf(Double.valueOf("0")));
        		}        		       		       		
        		
        	});
        	priceJSON.put("priceLevels", priceLevels);
        	priceJSON.put("currencyCode", "USD");
        	priceArray.add(priceJSON);
        	page.putField("prices", priceArray);
    	}
	}

	/**
	 * 库存信息
	 */
	public void getStocks(Page page) {
		//库存信息
    	JSONArray stockArray = new JSONArray();
    	JSONObject immediatelyStock = new JSONObject();
    	JSONObject OriginalStock = new JSONObject();       	        	
    	String source = page.getHtml().xpath("//td[@id='quantityAvailable']/text()").toString().trim(); 
    	if(source.matches(IMMEDIATELY_REGEX)){//现货
    		String quantity = page.getHtml().xpath("//td[@id='quantityAvailable']//span[@id=dkQty]/text()").toString().trim().replaceAll(",", "");
    		immediatelyStock.put("leadTime", 0);
    		immediatelyStock.put("quantity", Integer.valueOf(quantity));
    		immediatelyStock.put("source", 100);
    	}else{//不是现货
    		immediatelyStock.put("leadTime", 0);
    		immediatelyStock.put("quantity", 0);
    		immediatelyStock.put("source", 100);
    	}
    	stockArray.add(immediatelyStock);
    	
    	//计算原厂交期 (由于xsoup不支持last()函数，只能使用css选择器来做了)
    	List<Selectable> allProductDetailsRows = ((HtmlNode)page.getHtml().css("table[id=product-details]").css("tbody").css("tr")).nodes();
    	Selectable lastRow = allProductDetailsRows.get(allProductDetailsRows.size()-1);
    	String lastLabel = lastRow.xpath("//th//text()").toString();
    	
    	if ("Manufacturer Standard Lead Time".intern().equals(lastLabel)) {
	    	String leadTime = lastRow.xpath("//td//span/text()").toString();
			int leadTimeInt = 0;//有可能不存在
			if(StringUtils.isNotEmpty(leadTime)){
				leadTime = leadTime.trim().replaceAll("[^(0-9)]", "");
				leadTimeInt = Integer.valueOf(leadTime)*7;
			}
			OriginalStock.put("leadTime", leadTimeInt);
			OriginalStock.put("quantity", 0);
			OriginalStock.put("source", 400);
			stockArray.add(OriginalStock);
    	}
    	
    	page.putField("stocks", stockArray);
	}

	
	/**
	 * 将str改为驼峰命名，首字母小写，去除空行非字母数字的特殊字符
	 * @param str
	 * @return
	 */
	public static String lowercase(String str){
		//提取字母，去掉特殊字符
		char[] uniqueCode = str.replaceAll("[^(A-Za-z0-9)]", "").toCharArray();
		//如果第一个是字母，将首大写字母的ASCII后移32位变成小写
    	if(uniqueCode.length > 0 && Character.isLetter(uniqueCode[0])){
    		uniqueCode[0]+=32;
    	}
    	return String.valueOf(uniqueCode);
	}

	/**
	 * 基本信息
	 */
	public void getBasics(Page page) {
		String rohs = StringUtils.trim(page.getHtml().xpath("//table[@id=product-details]//tbody//tr[6]//td/text()").toString());
    	if(StringUtils.isNotEmpty(rohs) && rohs.matches(ROHS_REGEX)){
    		page.putField("rohs", true);
    	}else{
    		page.putField("rohs", false);
    	}
		String manufacturer =  StringUtils.trim(page.getHtml().xpath("//td//span[@itemprop=name]/text()").toString());
    	//String vendorDetailsLink = page.getHtml().xpath("//td//a[@itemprop=url]/@href").toString().trim();
    	String vendorDetailsLink = page.getRequest().getUrl();
    	String manufacturerPartNumber = page.getHtml().xpath("//td//h1[@itemprop=model]/text()").toString().trim();
    	String description =  StringUtils.trim(page.getHtml().xpath("//table[@id=product-details]//tbody//tr[5]//td[@itemprop=description]/text()").toString());
    	
    	String source = page.getHtml().xpath("//td[@id='quantityAvailable']/text()").toString().trim(); 
    	if(source.matches(IMMEDIATELY_REGEX)){//现货
    		String quantity = page.getHtml().xpath("//td[@id='quantityAvailable']//span[@id=dkQty]/text()").toString().trim().replaceAll(",", "");
    		page.putField("quantity", Integer.valueOf(quantity));
    	}else{//不是现货
    		page.putField("quantity", 0);//库存
    	}
    	page.putField("manufacturer", manufacturer);
    	page.putField("vendorDetailsLink", vendorDetailsLink);
    	page.putField("manufacturerPartNumber", manufacturerPartNumber);
    	page.putField("description", description);
	}


	public void doDeatailProcess(Page page) {
//		//基本信息
		this.getBasics(page);
		//价格阶梯
		this.getEnPrices(page);
		//vendor信息
		this.getVendors(page);
//		this.testDetailProcess(page);
	}
	
	/**
	 * 详情页面解析，test 例如：https://www.digikey.com/product-detail/en/powerex-inc/BS08D-T112/835-1126-1-ND/2499088
	 * @param page
	 */
	public void testDetailProcess(Page page){
		List<String> list = page.getHtml().xpath("//*[@id=product-dollars]//tr").all();
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		JSONObject result = new JSONObject();
		JSONArray jList = new JSONArray();
		JSONObject json = new JSONObject();
		list.remove(0);
		
		for (int i=0;i<list.size();i++) {
			Html html = Html.create("<table>".concat(list.get(i)).concat("</table>"));//创建一个把tr标签放在table里面来解析
			String breakQty = html.xpath("//tr//td[1]/text()").toString().trim();
			String unitPrice = "";
			if(i == 0){
				unitPrice = html.xpath("//tr//td[2]//span/text()").toString().trim();//相对路径
			}else{
				unitPrice = html.xpath("//tr//td[2]/text()").toString().trim();
			}
			json.put("breakQty", breakQty);
			json.put("unitPrice", unitPrice);
			jList.add(json);
		}
		result.put("prices", jList);
		String cate2 = page.getHtml().xpath("//*[@id=prod-att-table]/tbody/tr[2]/td[1]/a/text()").toString().trim();
		String cate3 = page.getHtml().xpath("//*[@id=prod-att-table]/tbody/tr[3]/td[1]/a/text()").toString().trim();
		
		String manufacturer = page.getHtml().xpath("//*[@id=product-details]/tbody/tr[3]/td/h2/span/a/span/text()").toString();
		
		String source = page.getHtml().xpath("//*[@id=quantityAvailable]/text()").toString().trim();
		String quantity = "0";
		if(source.contains("immediately")){
			quantity = page.getHtml().xpath("//*[@id=dkQty]/text()").toString().trim(); 
		}
		result.put("cate2", cate2);
		result.put("cate3", cate3);
		result.put("manufacturer", manufacturer);
		result.put("quantity", quantity);
		System.out.println(JSON.toJSONString(result));
	}
}
