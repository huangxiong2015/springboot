/*
 * Created: 2016年12月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.sitemap.manager;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.yikuyi.product.category.dao.ProductCatetoryClient;
import com.yikuyi.product.common.utils.MapReduceUtil;
import com.yikuyi.product.goods.dao.ProductClient;

/**
 * 用于生成sitemap的处理类
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SiteMapManager {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SiteMapManager.class);

	@Autowired
	private MongoOperations mo;
	
	@Autowired
	private ProductCatetoryClient pcc;
	
	@Autowired
	private ProductClient pc;

	/**
	 * 上传文件的本地存放路径
	 */
	@Value("${leadMaterialFilePath}")
	private String localFilePath;
	
	/**
	 * 当前已经生成的文件url集合
	 */
	private Map<String,List<String>> siteMap = new HashMap<>();
	
	/**
	 * 开始执行时间
	 */
	private long startTime = 0;
	
	/**
	 * 生成网站地图
	 * 1、清空以前的数据
	 * 2、查询全部次小类
	 * 3、mapreduce统计 原厂+分销商+产品分类
	 * 4、合并得出 原厂+产品分类
	 * 5、合并得出 分销商+产品分类
	 * 6、统计全部型号
	 * 7、生成汇总文件
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public Map<String,List<String>> generateSiteMap(List<Integer> conditionCateIds){
		//1、清空以前的数据
		clear();
		startTime = System.currentTimeMillis();
		//2、查询全部次小类
		List<Integer> cateIds = getAllCateIds(conditionCateIds);
		//3、mapreduce统计 原厂+分销商+产品分类
		Map<Integer,List<Document>> vendorMfrCateList = statisticsVendorMfrCate(cateIds);
		cateIds.clear();
		//4、合并得出 原厂+产品分类
		statisticsMfrCate(vendorMfrCateList);
		//5、合并得出 分销商+产品分类
		statisticsVendorCate(vendorMfrCateList);
		vendorMfrCateList.clear();
		//6、统计全部型号
		statisticsAllMpn(conditionCateIds);
		//7、生成汇总文件
		writeSiteMapXml();
		return siteMap;
	}
	
	/**
	 * 清理以前生成过的文件
	 * @since 2018年2月7日
	 * @author tongkun@yikuyi.com
	 */
	public void clear(){
		siteMap.clear();
		File file = new File(localFilePath+"/sitemap");
		if(!file.exists()){
			file.mkdirs();
		}
		for(File ch:file.listFiles()){
			ch.delete();
		}
	}
	
	/**
	 * 获取全部次小类的id
	 * @return
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public List<Integer> getAllCateIds(List<Integer> conditionCateIds){
		List<Integer> cateIds = new ArrayList<>();
		Consumer<Document> con = cate->{
			Object idObj = cate.get("_id");
			if(idObj instanceof Integer){
				cateIds.add((Integer)idObj);
			}else{
				cateIds.add(Integer.parseInt(idObj.toString().replace(".0","")));
			}
		};
		Document condition = new Document("cateLevel",3);
		if(conditionCateIds!=null){
			condition.append("_id", new Document("$in",conditionCateIds));
		}
		pcc.getCollection().find(condition).sort(new Document("_id",-1)).forEach(con);
		return cateIds;
	}
	
	/**
	 * 使用mapreduce统计所有供应商+原厂+分类的组合
	 * key:次小类id   value:原厂、供应商
	 * @param cateIds 所有的次小类id
	 * @return
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public Map<Integer,List<Document>> statisticsVendorMfrCate(List<Integer> cateIds){
		int resultCount = 0;
		Map<Integer,List<Document>> result = new HashMap<>();//统计结果
		List<String> urls = new ArrayList<>();//组合url集合
		//每个次小类进行循环
		for(Integer cateId:cateIds){
			//初始化，分组条件为原厂id，供应商id
			GroupBy groupBy = new GroupBy("spu.manufacturerId","vendorId");
			groupBy.initialDocument(new BasicDBObject());
			//mapreduce脚本
			groupBy.reduceFunction(
					new StringBuilder("function(obj,prev) {}").toString());
			//执行mapreduce，将所有有数据的原厂id，供应商id组合放在statResult中
			List<Document> statResult = new ArrayList<>();
			MapReduceUtil.doMapReduce(mo, where("spu.categories.2._id").is(cateId).and("spu.manufacturerId").nin(new Object[]{null}), "product", groupBy, res->{
				for(Document oneResult:res){
					oneResult.put("mfrId", oneResult.get("spu.manufacturerId"));
					oneResult.remove("spu.manufacturerId");
					statResult.add(oneResult);
				}
			});
			//如果统计结果不为0则放入结果集合中
			if(!statResult.isEmpty()){
				result.put(cateId, statResult);
				resultCount+=statResult.size();
				//拼装为url加入url集合中
				for(Document res:statResult){
					urls.add("https://www.yikuyi.com/search/inventory/vendor/"+res.getString("vendorId")+".htm?manufacturer="+res.get("mfrId").toString().replace(".0","")+"&cat="+cateId);
				}
				logger.info("sitemap 生成-步骤1：分类:"+cateId+"  不同的原厂、供应商数量："+statResult.size()+" 当前已有组合数："+resultCount+" 已耗费时间："+(System.currentTimeMillis()-startTime));
			}
		}
		//url结果写入文件
		writeUrlXml(urls, "vendor-mfr-cate");
		
		return result;
	}
	
	/**
	 * 统计所有的 原厂+产品分类 组合
	 * @param vendorMfrCateList 之前统计出的 供应商+原厂+分类的组合
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public void statisticsMfrCate(Map<Integer,List<Document>> vendorMfrCateMap){
		int resultCount = 0;
		//url集合
		List<String> urls = new ArrayList<>();
		//循环之前的统计结果
		for(Entry<Integer,List<Document>> entry:vendorMfrCateMap.entrySet()){
			Integer cateId = entry.getKey();
			List<Document> vendorMfrList = entry.getValue();
			Set<Integer> mfrList = new HashSet<>();
			//循环统计所有的原厂
			for(Document vendorMfr:vendorMfrList){
				Object mfrIdObj = vendorMfr.get("mfrId");
				if(mfrIdObj instanceof Integer){
					mfrList.add((Integer)mfrIdObj);
				}else{
					mfrList.add(Integer.parseInt(mfrIdObj.toString().replace(".0", "")));
				}
			}
			resultCount+=mfrList.size();
			//拼装为url加入url集合中
			for(Integer mfrId:mfrList){
				urls.add("https://www.yikuyi.com/search/inventory/cate/"+cateId+"-"+mfrId+".htm");
			}
			logger.info("sitemap 生成-步骤2：分类:"+cateId+"  不同的原厂数量："+mfrList.size()+" 当前已有组合数："+resultCount+" 已耗费时间："+(System.currentTimeMillis()-startTime));
		}
		//url结果写入文件
		writeUrlXml(urls, "mfr-cate");
	}

	/**
	 * 统计所有的 分销商+产品分类 组合
	 * @param vendorMfrCateList 之前统计出的 供应商+原厂+分类的组合
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public void statisticsVendorCate(Map<Integer,List<Document>> vendorMfrCateList){
		int resultCount = 0;
		//url集合
		List<String> urls = new ArrayList<>();
		//循环之前的统计结果
		for(Entry<Integer,List<Document>> entry:vendorMfrCateList.entrySet()){
			Integer cateId = entry.getKey();
			List<Document> vendorMfrList = entry.getValue();
			Set<String> vendorIdList = new HashSet<>();
			//循环统计所有的原厂
			for(Document vendorMfr:vendorMfrList){
				String vendorId = vendorMfr.getString("vendorId");
				vendorIdList.add(vendorId);
			}
			resultCount+=vendorIdList.size();
			//拼装为url加入url集合中
			for(String vendorId:vendorIdList){
				urls.add("https://www.yikuyi.com/search/inventory/vendor/"+vendorId+".htm?cat="+cateId);
			}
			logger.info("sitemap 生成-步骤3：分类:"+cateId+"  不同的供应商数量："+vendorIdList.size()+" 当前已有组合数："+resultCount+" 已耗费时间："+(System.currentTimeMillis()-startTime));
		}
		//url结果写入文件
		writeUrlXml(urls, "vendor-cate");
	}

	/**
	 * 统计有库存有价格的型号
	 * @param conditionCateIds 测试分类id，如果提供则只统计指定id的数据，并且只统计100个，用于快速测试文件生成
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	public void statisticsAllMpn(List<Integer> conditionCateIds){
		List<String> mpnUrls = new ArrayList<>();
		List<String> productUrls = new ArrayList<>();
		
		Document params = new Document();
		params.put("count", 0);
		params.put("no", 0);
		Consumer<Document> con = sku->{
			params.put("no", params.getInteger("no")+1);
			if(checkHaveQtyPrice(sku)){
				Document spu = (Document)sku.get("spu");
				//以下为详情页url
				productUrls.add("https://www.yikuyi.com/product/"+sku.getString("_id")+".htm");
				if(productUrls.size()==50000){
					List<String> newProductUrls = new ArrayList<>();
					newProductUrls.addAll(productUrls);
					productUrls.clear();
					writeUrlXml(newProductUrls, "product");
				}
				//以下为搜索页url
				//判断是否是新的型号
				if(params.getString("lastMpn")!=null&&!spu.getString("manufacturerPartNumber").equals(params.getString("lastMpn"))){
					params.put("count", params.getInteger("count")+1);
					//拼装url
					mpnUrls.add("https://www.yikuyi.com/search/inventory/kw/"+params.getString("lastMpn").replaceAll("#", "%23")+".htm");
					//每50000个url写一个文件
					if(mpnUrls.size()==50000){
						List<String> newUrls = new ArrayList<>();
						newUrls.addAll(mpnUrls);
						mpnUrls.clear();
						writeUrlXml(newUrls, "list");
					}
				}
				params.put("lastMpn", spu.getString("manufacturerPartNumber"));
			}

			if(params.getInteger("no")%1000==0){
				logger.info("sitemap 生成-步骤4：统计有价格有库存的型号  当前已经统计数量："+params.getInteger("no")+" 符合条件数量："+params.getInteger("count")+" 已耗费时间："+(System.currentTimeMillis()-startTime));
			}
			
		};
		Document condition = new Document();
		if(conditionCateIds!=null){
			condition.append("spu.categories.2._id", new Document("$in",conditionCateIds));
		}
		FindIterable<Document> fi = pc.getCollection().find(condition).sort(new Document("spu.manufacturerPartNumber",1));
		//用于单元测试
		if(conditionCateIds!=null){
			fi = fi.limit(100);
		}
		fi.forEach(con);
		//清理剩余数据
		if(mpnUrls.size()>0){
			writeUrlXml(mpnUrls, "product");
		}
	}
	
	/**
	 * 检查是否是标准、有效sku且有价格有库存
	 * 如果符合条件返回true，不符合条件返回false
	 * @param sku
	 * @return
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	@SuppressWarnings("unchecked")
	public boolean checkHaveQtyPrice(Document sku){
		//状态无效跳过
		if(sku.get("status")==null||!"1".equals(sku.get("status").toString())){
			return false;
		}
		//spu无效跳过
		Document spu = (Document)sku.get("spu");
		if(spu==null||spu.get("_id")==null||spu.get("spuId")==null||spu.get("manufacturerPartNumber")==null){
			return false;
		}
		//无币种跳过
		List<Document> prices = (List<Document>)sku.get("prices");
		if(CollectionUtils.isEmpty(prices)){
			return false;
		}
		//无价格无库存跳过
		List<Document> levels = (List<Document>)prices.get(0).get("priceLevels");
		if(CollectionUtils.isEmpty(levels)||sku.get("qty")==null||"0".equals(sku.get("qty").toString())){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 生成网站地图
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	private void writeSiteMapXml(){
		//生成xml对象
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element index = doc.addElement("sitemapindex");
		//循环所有文件url
		for(Entry<String,List<String>> entry:siteMap.entrySet()){
			for(String fileUrl:entry.getValue()){
				Element sitemap = index.addElement("sitemap");
				Element loc = sitemap.addElement("loc");
				loc.addText(fileUrl);
			}
		}
		//写入文件
		writeXML(doc, "index.xml");
	}
	
	/**
	 * 将url输出为xml文件
	 * @param urls 指定的url
	 * @param type 当前指定的url类型
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	private void writeUrlXml(List<String> urls,String type){
		//获取类型指定的url集合
		List<String> typeUrls = siteMap.get(type);
		if(typeUrls==null){
			typeUrls = new ArrayList<>();
			siteMap.put(type, typeUrls);
		}
		//生成xml对象
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element urlsetE = doc.addElement("urlset");
		for(String url:urls){
			Element urlE = urlsetE.addElement("url");
			Element locE = urlE.addElement("loc");
			locE.add(DocumentHelper.createText(url));
		}
		//执行保存
		String url = writeXML(doc, type+"-"+(typeUrls.size()+1)+".xml");
		typeUrls.add(url);
	}
	
	 /**
	  * 将doc对象转为输出流
	 * @param doc xml文档对象
	 * @param filename 文件名
	 * @since 2018年2月6日
	 * @author tongkun@yikuyi.com
	 */
	private String writeXML(org.dom4j.Document doc,String filename){
		String url = "https://www.yikuyi.com/sitemap/"+filename;
		//生成到文件
		File fileUrl = new File(localFilePath+"/sitemap/"+filename);
        OutputFormat format = OutputFormat.createPrettyPrint(); 
        XMLWriter writer = null;
        try (FileOutputStream fos = new FileOutputStream(fileUrl);){ 
        	writer = new XMLWriter(fos, format);
			writer.write(doc);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally{
			try {
				writer.close();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
        //清理文件
//        fileUrl.delete();
        return url;
	 }
}