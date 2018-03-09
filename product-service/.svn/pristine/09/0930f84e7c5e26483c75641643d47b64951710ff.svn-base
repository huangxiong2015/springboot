///*
// * Created: 2017年7月6日
// *
// * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
// * Copyright (c) 2015-2017 
// * License, Version 1.0. Published by Yikuyi IT department.
// *
// * For the convenience of communicating and reusing of codes,
// * any java names, variables as well as comments should be written according to the regulations strictly.
// */
//package com.yikuyi.product.material.bll;
//
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//import static org.springframework.data.mongodb.core.query.Query.query;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ictrade.tools.leadin.LeadInFactory;
//import com.ictrade.tools.leadin.LeadInProcesser;
//import com.yikuyi.brand.model.ProductBrand;
//import com.yikuyi.product.brand.dao.BrandRepository;
//import com.yikuyi.product.brand.manager.BrandManager;
//import com.yikuyi.product.goods.dao.ProductRepository;
//import com.yikuyi.product.goods.dao.ProductStandRepository;
//import com.yikuyi.product.model.Product;
//import com.yikuyi.product.model.ProductStand;
//
///**
// * 物料品牌转移业务处理类
// * @author zr.wanghong
// * @version 1.0.0
// * @since 2017年7月10日
// */
//@Service
//@Transactional
//public class ManufactureTransferManager {
//	
//	private static final Logger logger = LoggerFactory.getLogger(ManufactureTransferManager.class);
//	
//	@Autowired
//	private ProductRepository productRepository;
//
//	@Autowired
//	private ProductStandRepository productStandRepository;
//	
//	@Autowired
//	private BrandRepository brandRepository;
//	
//	@Autowired
//	private BrandManager brandManager;
//	
//	@Autowired
//    private MongoTemplate mongoTemplate;
//	
//	private static final String FIELD_OLDID = "oldId";
//	private static final String FIELD_OLDSPUID = "oldSpuId";
//	private static final String FIELD_NEWID = "newId";
//	private static final String FIELD_NEWSPUID = "newSpuId";
//	private static final String FIELD_SPU_ID = "spu._id";
//	
//	
//	/**
//	 * 导入库存上传文件路径
//	 */
//	@Value("${leadManufactureTransferFilePath}")
//	private String leadManufactureTransferFilePath;
//	
//	/**
//	 * 是否需要转移
//	 * @param oldProductBrand
//	 * @param newProductBrand
//	 * @return
//	 * @since 2017年7月7日
//	 * @author zr.wanghong
//	 */
//	public boolean isNeedTransfer(ProductBrand oldProductBrand,ProductBrand newProductBrand){
//		if(oldProductBrand == null || newProductBrand == null)
//			return false;
//		
//		//品牌相同不需要转移
//		if(oldProductBrand.getId().equals(newProductBrand.getId()))
//			return false;
//		return true;
//	}
//	
//	/**
//	 * 品牌转移
//	 * @param fileUrl
//	 * @since 2017年7月7日
//	 * @author zr.wanghong
//	 */
//	public String transfer(){
//		File file = new File(leadManufactureTransferFilePath);
//		File[] files = file.listFiles();
//		
//		if(files.length ==0){
//			return "not found file!";
//		}
//		LeadInProcesser leadInProcesser = LeadInFactory.createProcess(files[0]);
//		//过滤第一行
//		leadInProcesser.getNext();
//	
//		String[] lineData;
//		List<String[]> processDatas = new ArrayList<>();
//		logger.info("开始解析数据！");
//		while ((lineData = leadInProcesser.getNext()) != null) {
//			processDatas.add(lineData);
//		}
//		logger.info("解析数据完成！");
//		
//		//获取所有品牌
//		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();
//		
//		//数据循环
//		for(int i = 0;i < processDatas.size();i++){
//			String[] processData = processDatas.get(i);
//			processV2(brandMap, processData);
//			if(i%1000==0){
//				logger.info("已转移品牌 {}/{}",i,processDatas.size());
//			}
//		}
//		
//		
//		logger.info("品牌转移完成！");
//		return "transfer success!";
//	}
//
//	/**
//	 * 转换处理
//	 * @param brandMap
//	 * @param processData
//	 * @since 2017年7月13日
//	 * @author tongkun@yikuyi.com
//	 */
//	private void processV2(Map<String, ProductBrand> brandMap, String[] processData) {
//		if ( processData.length < 3) {
//			logger.info("品牌转移，本行错误：1："+(processData.length>=1?processData[0]:"")+"   2:"+(processData.length>=2?processData[1]:""));
//			return;
//		}
//		String oldManufacture = processData[0].trim();//旧品牌
//		String partNumber = processData[1].trim();//型号
//		String newManufacture = processData[2].trim();//新品牌
//		ProductBrand oldBrand = brandMap.get(oldManufacture.trim().toUpperCase());
//		ProductBrand newBrand = brandMap.get(newManufacture.trim().toUpperCase());
//		if(oldBrand==null){
//			logger.info("品牌转移，没有找到旧品牌："+oldManufacture.trim().toUpperCase());
//			return;
//		}
//		if(newBrand==null){
//			logger.info("品牌转移，没有找到新品牌："+newManufacture.trim().toUpperCase());
//			return;
//		}
//		if(oldBrand.getId().equals(newBrand.getId())){
//			logger.info("品牌转移，品牌相同，不需要转移："+oldManufacture.trim().toUpperCase());
//			return;
//		}
//		//转移spu
//		transBrandProductStand(partNumber, oldBrand, newBrand);
//		
//		//转移sku
//		transBrandProduct(partNumber, oldBrand, newBrand);
//	}
//
//	/**
//	 * 转移sku数据
//	 * @param partNumber
//	 * @param oldBrand
//	 * @param newBrand
//	 * @since 2017年7月13日
//	 * @author tongkun@yikuyi.com
//	 */
//	private void transBrandProduct(String partNumber, ProductBrand oldBrand, ProductBrand newBrand) {
//		List<Product> saveList = new ArrayList<>();
//		
//		JSONObject con = new JSONObject();
//		con.put("spu.manufacturerPartNumber", partNumber);
//		con.put("status", 1);
//		List<Product> ps = productRepository.findListByCondition(con);
//		for(Product p:ps){
//			String oldQuickFindkey = p.getQuickFindKey();
//			String newQuickFindkey = this.getQuickFindKey(p, newBrand);
//			//旧spuId
//			String oldBrandName = oldBrand.getBrandShort() == null ? oldBrand.getBrandName() : oldBrand.getBrandShort().trim();
//			String oldSpuId = (partNumber.trim()+"-"+ oldBrandName ).toUpperCase();
//			//新spuId
//			String newBrandNameTemp = newBrand.getBrandShort() == null ? newBrand.getBrandName() : newBrand.getBrandShort();
//			String newSpuId = partNumber.trim().toUpperCase() +"-"+ newBrandNameTemp.trim().toUpperCase();
//			//如果是旧品牌
//			if((isEqualBrand(oldBrand, p, oldSpuId)||isEqualBrand(newBrand, p, newSpuId))&&!newQuickFindkey.equals(p.getQuickFindKey())){
//				//找下有没有同时存在新品牌的情况
//				List<String> conList = new ArrayList<>();
//				conList.add(newQuickFindkey);
//				List<Product> ps2 = productRepository.findProductByQuickFindKey(conList);
//				//如果存在新品牌的数据
//				if(!CollectionUtils.isEmpty(ps2)){
//					p.setStatus(0);
//					saveList.add(p);
//					logger.info("品牌转移，Product，删除旧数据 oldQF:"+oldQuickFindkey+" newQF:"+newQuickFindkey);
//				}
//				//如果不存在新品牌的数据
//				else{
//					List<ProductStand> ops = productStandRepository.findProductStandBySpuId(newSpuId);
//					//如果没有原本的spu
//					if(CollectionUtils.isEmpty(ops)){
//						p.getSpu().setId(null);
//					}
//					//如果有spuId
//					else{
//						p.getSpu().setId(ops.get(0).getId());
//					}
//					p.getSpu().setSpuId(newSpuId);
//					p.getSpu().setManufacturer(newBrand.getBrandName());
//					p.getSpu().setManufacturerAgg(newBrand.getBrandName());
//					p.getSpu().setManufacturerShort(newBrand.getBrandShort());
//					p.getSpu().setManufacturerId(newBrand.getId());
//					p.setQuickFindKey(newQuickFindkey);
//					saveList.add(p);
//					logger.info("品牌转移，Product，不能存在新品牌，直接转移数据 oldQF:"+oldQuickFindkey+" newQF:"+newQuickFindkey+" spuId:"+p.getSpu().getId());
//				}
//			}
//			//如果商品是新品牌
//			else if(newQuickFindkey.equals(p.getQuickFindKey())){
//				List<ProductStand> ops = productStandRepository.findProductStandBySpuId(newSpuId);
//				//如果没有原本的spu
//				if(CollectionUtils.isEmpty(ops)){
//					p.getSpu().setId(null);
//				}
//				//如果有spuId
//				else{
//					p.getSpu().setId(ops.get(0).getId());
//				}
//				p.getSpu().setSpuId(newSpuId);
//				p.getSpu().setManufacturer(newBrand.getBrandName());
//				p.getSpu().setManufacturerAgg(newBrand.getBrandName());
//				p.getSpu().setManufacturerShort(newBrand.getBrandShort());
//				p.getSpu().setManufacturerId(newBrand.getId());
//				p.setQuickFindKey(newQuickFindkey);
//				saveList.add(p);
//				logger.info("品牌转移，Product，商品是新品牌，更新数据 oldQF:"+oldQuickFindkey+" newQF:"+newQuickFindkey+" spuId:"+p.getSpu().getId());
//			}
//			//如果都不是
//			else{
//				logger.info("品牌转移，Product，不是旧品牌，也不是新品牌，不更新 oldQF:"+oldQuickFindkey+" newQF:"+newQuickFindkey+" spuId:"+p.getSpu().getId());
//			}
//		}
//		productRepository.save(saveList);
//	}
//	
//	/**
//	 * 转移spu数据
//	 * @param partNumber
//	 * @param oldBrand
//	 * @param newBrand
//	 * @since 2017年7月13日
//	 * @author tongkun@yikuyi.com
//	 */
//	public void transBrandProductStand(String partNumber,ProductBrand oldBrand,ProductBrand newBrand){
//		List<ProductStand> saveList = new ArrayList<>();
//		//根据型号查询物料
//		List<ProductStand> pss = productStandRepository.findProductStandByNo(partNumber);
//		for(ProductStand ps:pss){
//			//旧spuId
//			String oldBrandName = oldBrand.getBrandShort() == null ? oldBrand.getBrandName() : oldBrand.getBrandShort().trim();
//			String oldSpuId = (partNumber.trim()+"-"+ oldBrandName ).toUpperCase();
//			//新spuId
//			String newBrandNameTemp = newBrand.getBrandShort() == null ? newBrand.getBrandName() : newBrand.getBrandShort();
//			String newSpuId = partNumber.trim().toUpperCase() +"-"+ newBrandNameTemp.trim().toUpperCase();
//			
//			//如果确认是旧品牌则需要转移
//			if(isEqualBrand(oldBrand,ps,oldSpuId)){
//				//找下有没有同时存在新品牌的情况
//				List<ProductStand> ps2 = productStandRepository.findProductStandBySpuId(newSpuId);
//				//如果存在新品牌的情况，旧品牌删除
//				if(!CollectionUtils.isEmpty(ps2)){
//					productStandRepository.delete(ps.getId());
//					logger.info("品牌转移，ProductStand，删除旧数据 oldSpuId:"+oldSpuId+" newSpuId:"+newSpuId);
//				}
//				//如果不存在新品牌的情况，旧品牌转移
//				else{
//					ps.setSpuId(newSpuId);
//					ps.setManufacturer(newBrand.getBrandName());
//					ps.setManufacturerAgg(newBrand.getBrandName());
//					ps.setManufacturerShort(newBrand.getBrandShort());
//					ps.setManufacturerId(newBrand.getId());
//					saveList.add(ps);
//					logger.info("品牌转移，ProductStand，不存在新品牌 oldSpuId:"+oldSpuId+" newSpuId:"+newSpuId);
//				}
//			}
//			//新品牌不需要转移，但是需要看下有什么需要修改
//			else if(isEqualBrand(newBrand,ps,newSpuId)){
//				String spuIdTemp = ps.getSpuId();
//				boolean isUpdated = false;
//				if(!newSpuId.equals(ps.getSpuId())){
//					//找下有没有同时存在新品牌的情况
//					List<ProductStand> ps2 = productStandRepository.findProductStandBySpuId(newSpuId);
//					if(!CollectionUtils.isEmpty(ps2)){
//						productStandRepository.delete(ps.getId());
//						logger.info("品牌转移，ProductStand，删除旧数据 oldSpuId:"+ps.getSpuId()+" newSpuId:"+newSpuId);
//						continue;
//					}
//					//没有新的spu继续更新
//					ps.setSpuId(newSpuId);
//					isUpdated = true;
//				}
//				if(!newBrand.getBrandName().equals(ps.getManufacturer())){
//					ps.setManufacturer(newBrand.getBrandName());
//					ps.setManufacturerAgg(newBrand.getBrandName());
//					isUpdated = true;
//				}
//				if(newBrand.getBrandShort()!=null && !newBrand.getBrandShort().equals(ps.getManufacturerShort())){
//					ps.setManufacturerShort(newBrand.getBrandShort());
//					isUpdated = true;
//				}
//				if(!newBrand.getId().equals(ps.getManufacturerId())){
//					ps.setManufacturerId(newBrand.getId());
//					isUpdated = true;
//				}
//				if(isUpdated){
//					saveList.add(ps);
//					logger.info("品牌转移，ProductStand，新品牌数据更新 oldSpuId:"+spuIdTemp+" newSpuId:"+newSpuId);
//				}else{
//					logger.info("品牌转移，ProductStand，新数据不需要更新 oldSpuId:"+spuIdTemp+" newSpuId:"+newSpuId);
//				}
//			}
//			//既不适旧品牌，也不是新品牌，不处理
//			else{
//				logger.info("品牌转移，ProductStand，不适用，不更新 oldSpuId:"+ps.getSpuId());
//			}
//		}
//		productStandRepository.save(saveList);
//	}
//	
//	/**
//	 * 比较物料的品牌是不是这个
//	 * @param brand
//	 * @param ps
//	 * @param spuId
//	 * @return
//	 * @since 2017年7月13日
//	 * @author tongkun@yikuyi.com
//	 */
//	public boolean isEqualBrand(ProductBrand brand,ProductStand ps,String spuId){
//		if(brand.getBrandName().equals(ps.getManufacturer())||brand.getId().equals(ps.getManufacturerId())||spuId.equals(ps.getSpuId())){
//			return true;
//		}else{
//			return false;
//		}
//	}
//	
//	/**
//	 * 比较商品的品牌是不是这个
//	 * @param brand
//	 * @param p
//	 * @param spuId
//	 * @return
//	 * @since 2017年7月13日
//	 * @author tongkun@yikuyi.com
//	 */
//	public boolean isEqualBrand(ProductBrand brand,Product p,String spuId){
//		if(brand.getBrandName().equals(p.getSpu().getManufacturer())||brand.getId().equals(p.getSpu().getManufacturerId())||spuId.equals(p.getSpu().getSpuId())){
//			return true;
//		}else{
//			return false;
//		}
//	}
//
//	/**
//	 * 转换处理
//	 * @param brandMap
//	 * @param processData
//	 * @since 2017年7月7日
//	 * @author zr.wanghong
//	 */
//	private void process(Map<String, ProductBrand> brandMap, String[] processData) {
//		if ( processData.length != 3) {
//			return;
//		}
//		String oldManufacture = processData[0];
//		String oldPartNumber = processData[1];
//		String newManufacture = processData[2];
//		ProductBrand oldBrand = brandMap.get(oldManufacture.trim().toUpperCase());
//		ProductBrand newBrand = brandMap.get(newManufacture.trim().toUpperCase());
//		
//		//1.检测品牌是否相同，相同则不需要转移
//		if(!isNeedTransfer(oldBrand, newBrand)){
//			return;
//		}
//		
//		//当brandShort为空时取brandName，否则取brandShort
//		String newBrandNameTemp = newBrand.getBrandShort() == null ? newBrand.getBrandName() : newBrand.getBrandShort(); 
//		
//		//2.根据oldName的品牌spuId查询物料信息
//		String oldBrandName = oldBrand.getBrandShort() == null ? oldBrand.getBrandName() : oldBrand.getBrandShort().trim();
//		String oldSpuId = (oldPartNumber.trim()+"-"+ oldBrandName ).toUpperCase();
//		List<ProductStand> oldProductStands = productStandRepository.findProductStandBySpuId(oldSpuId);
//		
//		logger.debug("当前转移的品牌：{}，查询到旧品牌关联的物料信息：{}条",oldBrand.getBrandName()+"=>"+newBrand.getBrandName(),oldProductStands.size());
//		
//		//生成临时表名
//		String tempCollectionName = "product_"+new Date().getTime();
//		
//		//3.旧品牌有物料信息，则需要物料信息转移
//		if(CollectionUtils.isNotEmpty(oldProductStands)){
//			String newSpuId = oldPartNumber.trim().toUpperCase() +"-"+ newBrandNameTemp.trim().toUpperCase();
//			oldProductStands.stream().forEach( oldSpu -> 
//			transferMaterialBrand(newBrand, oldSpu, tempCollectionName, newSpuId)
//			);
//		}
//		
//		//4.合并相互转换的品牌别名中不相同的别名
//		//ProductBrand mergeBrand = this.mergeBrandAlias(oldBrand, newBrand);
//		//brandRepository.save(mergeBrand);
//		
//		//5.查询product表中，spu._id为null，spu.manufacturer为旧的品牌名称的数据，更新为新品牌
//		this.updateSkuForSpuIsNull(oldBrand.getBrandName(), newBrand, newBrandNameTemp,oldPartNumber);
//		
//		
//		
//		//处理product中有对应型号品牌但product_stand中没有
//		processSpuNotfound(oldPartNumber, newBrand, newBrandNameTemp, oldBrandName);
//		
//		
//		//6.将oldName品牌删除 失效
//		//oldBrand.setStatus(0);
//		//brandRepository.save(oldBrand);
//		
//		//7.删除临时表
//		mongoTemplate.dropCollection(tempCollectionName);
//	}
//
//	/**
//	 * 处理product中有spu但spu已不存在的数据
//	 * @param oldPartNumber
//	 * @param newBrand
//	 * @param newBrandNameTemp
//	 * @param oldBrandName
//	 * @since 2017年7月12日
//	 * @author zr.wanghong
//	 */
//	private void processSpuNotfound(String oldPartNumber, ProductBrand newBrand, String newBrandNameTemp,
//			String oldBrandName) {
//		JSONObject json = new JSONObject();
//		JSONObject ne = new JSONObject();
//		String[] nullArr = {null};
//		ne.put("$ne", nullArr);
//		json.put("spu._id", ne);
//		json.put("spu.manufacturer", oldBrandName);
//		json.put("spu.manufacturerPartNumber", oldPartNumber.trim());
//		json.put("status", 1);
//		List<Product> products = productRepository.findListByCondition(json);
//		
//		//判断旧的物料更新为新的quickFindKey是否重复，重复新的有效，不重复直接更新
//		List<Product> resultProducts = disableQuikckfindKeyRepeat(newBrand, products);
//		resultProducts.stream().forEach(product -> {
//			
//			ProductStand spu = productStandRepository.findOne(product.getSpu().getId());
//			if(spu == null){
//				String oldQuickFindkey = product.getQuickFindKey();
//				String newQuickFindkey = this.getQuickFindKey(product, newBrand);
//				Update update = new Update();
//				update.set("quickFindKey", newQuickFindkey);
//				update.set("spu.manufacturerId", newBrand.getId());
//				update.set("spu.manufacturer", newBrand.getBrandName());
//				update.set("spu.manufacturerAgg", newBrand.getBrandName());
//				update.set("spu.manufacturerShort", newBrand.getBrandName());
//				
//				String manufacturerPartNumber = product.getSpu().getManufacturerPartNumber().trim().toUpperCase();
//				String newSpuId = manufacturerPartNumber +"-" + newBrandNameTemp.trim().toUpperCase();
//				update.set("spu.spuId", newSpuId);
//				try{
//					mongoTemplate.updateFirst(query(where("_id").is(product.getId())), update, Product.class);
//					logger.info("品牌转移，Product，没有spu数据的 update:"+update.toString());
//				}catch(Exception e){
//					logger.error("异常：oldQF="+oldQuickFindkey+" newQF="+newQuickFindkey+" 错误信息："+e.getMessage(),e);
//					//throw new SystemException(e);
//				}
//			}
//		});
//	}
//
//	/**
//	 * 转换物料信息品牌
//	 * @param newBrand
//	 * @param productStands
//	 * @param tempCollectionName
//	 * @param newSpuId
//	 * @param productStand
//	 * @since 2017年7月10日
//	 * @author zr.wanghong
//	 */
//	private void transferMaterialBrand(ProductBrand newBrand, ProductStand oldSpu, String tempCollectionName,
//			String newSpuId) {
//		//用转换后的spuId查询是否有物料信息,有物料信息说明转换有冲突，需要将旧的物料信息作废
//		List<ProductStand> newProductStands = productStandRepository.findProductStandBySpuId(newSpuId);
//		if(CollectionUtils.isNotEmpty(newProductStands)){
//			ProductStand newSpu = newProductStands.get(0);
//			JSONObject data = new JSONObject();
//			data.put(FIELD_OLDID, oldSpu.getId());
//			data.put(FIELD_OLDSPUID, oldSpu.getSpuId());
//			data.put(FIELD_NEWID, newSpu.getId());
//			data.put(FIELD_NEWSPUID, newSpu.getSpuId());
//			//先保存临时表
//			mongoTemplate.save(data, tempCollectionName);
//			//查询临时表中的数据
//			List<JSONObject> temps = mongoTemplate.findAll(JSONObject.class, tempCollectionName);
//			//将product表中的spu._id更新成新品牌对应的物料spu的id
//			temps.stream().forEach( temp -> {
//
//				List<String> spuIds = new ArrayList<>();
//				spuIds.add(temp.get(FIELD_OLDSPUID).toString());
//				List<Product> oldProducts = productRepository.findProductBySpuIds(spuIds);
//				//判断旧的物料更新为新的quickFindKey是否重复，重复新的有效，不重复直接更新
//				List<Product> resultProducts = disableQuikckfindKeyRepeat(newBrand, oldProducts);
//				
//				resultProducts.stream().forEach( oldProduct -> {
//					String newQuickFindkey = this.getQuickFindKey(oldProduct, newBrand);
//					
//					Update update = new Update();
//					update.set("quickFindKey", newQuickFindkey);
//					update.set(FIELD_SPU_ID, temp.get(FIELD_NEWID));
//					update.set("spu.manufacturerId", newBrand.getId());
//					update.set("spu.manufacturer", newBrand.getBrandName());
//					update.set("spu.manufacturerAgg", newBrand.getBrandName());
//					update.set("spu.manufacturerShort", newBrand.getBrandName());
//					update.set("spu.spuId", newSpuId);
//					
//					String oldQuickFindkey = oldProduct.getQuickFindKey();
//					try{
//						mongoTemplate.updateFirst(query(where("_id").is(oldProduct.getId())), update, Product.class);
//						logger.info("品牌转移，Product update:"+update.toString());
//					}catch(Exception e){
//						logger.error("异常：oldQF="+oldQuickFindkey+" newQF="+newQuickFindkey+" 错误信息："+e.getMessage(),e);
//						//throw new SystemException(e);
//					}
//				});
//				
//				
//				//将旧品牌对应的spu中的的数据删除
//				productStandRepository.delete(temp.get(FIELD_OLDID).toString());
//			});
//		}else{
//			//转换不冲突，更新旧物料品牌信息为新品牌
//			Update update = new Update();
//			update.set("manufacturerId", newBrand.getId());
//			update.set("manufacturer", newBrand.getBrandName());
//			update.set("manufacturerAgg", newBrand.getBrandName());
//			update.set("manufacturerShort", newBrand.getBrandName());
//			update.set("spuId", newSpuId);
//			mongoTemplate.updateFirst(query(where("_id").is(oldSpu.getId())), update, ProductStand.class);
//			logger.info("品牌转移，ProductStand:"+update.toString());
//		}
//	}
//
//	/**
//	 * 失效quickFindKey重复的数据
//	 * @param newBrand
//	 * @param oldSpuId
//	 * @since 2017年7月11日
//	 * @author zr.wanghong
//	 */
//	private List<Product> disableQuikckfindKeyRepeat(ProductBrand newBrand, List<Product> oldProducts) {
//		
//		List<Product> resultProducts = new ArrayList<>();
//		//判断旧的物料更新为新的quickFindKey是否重复，重复新的有效，不重复直接更新
//		oldProducts.stream().forEach( product -> {
//			String newQuickFindkey = this.getQuickFindKey(product, newBrand);
//			
//			List<String> newQuickFindKeys = new ArrayList<>();
//			newQuickFindKeys.add(newQuickFindkey);
//			List<Product> result = productRepository.findProductByQuickFindKey(newQuickFindKeys);
//			if(CollectionUtils.isNotEmpty(result)){
//				//quickfindkey转换有冲突，失效以前的
//				product.setStatus(0);
//				productRepository.save(product);
//				logger.info("冲突：oldQF="+product.getQuickFindKey()+"  newQF="+newQuickFindkey);
//			}else{
//				//quickfindkey不冲突的数据
//				resultProducts.add(product);
//				logger.info("不冲突：oldQF="+product.getQuickFindKey()+"  newQF="+newQuickFindkey);
//			}
//		});
//		
//		return resultProducts;
//	}
//	
//	
//	/**
//	 * 合并品牌别名
//	 * @param oldBrand
//	 * @param newBrand
//	 * @return
//	 * @since 2017年7月7日
//	 * @author zr.wanghong
//	 */
//	public ProductBrand mergeBrandAlias(ProductBrand oldBrand,ProductBrand newBrand){
//		List<String> alias = oldBrand.getBrandAlias();
//		List<String> newAlias = newBrand.getBrandAlias();
//		alias.stream().forEach( alia -> {
//			//新品牌中没有则加入
//			if(!newAlias.contains(alia)){
//				newAlias.add(alia);
//			}
//		});
//		return newBrand;
//	}
//	
//	/**
//	 * 更新sku表中spuiD为空的数据,将品牌信息更新为新品牌的信息
//	 * @param oldBrandName
//	 * @param newBrand
//	 * @param newBrandNameTemp
//	 * @since 2017年7月7日
//	 * @author zr.wanghong
//	 */
//	public void updateSkuForSpuIsNull(String oldBrandName,ProductBrand newBrand,String newBrandNameTemp ,String oldPartNumber){
//		//查询product表中，spu._id为null，spu.manufacturer为旧的品牌名称的数据
//		JSONObject json = new JSONObject();
//		JSONObject in = new JSONObject();
//		String[] nullArr = {null};
//		in.put("$in", nullArr);
//		json.put("spu._id", in);
//		json.put("spu.manufacturer", oldBrandName);
//		json.put("spu.manufacturerPartNumber", oldPartNumber.trim());
//		
//		List<Product> products = productRepository.findListByCondition(json);
//		
//		if(CollectionUtils.isNotEmpty(products)){
//			//判断旧的物料更新为新的quickFindKey是否重复，重复新的有效，不重复直接更新
//			List<Product> resultProducts = disableQuikckfindKeyRepeat(newBrand, products);
//			
//			resultProducts.stream().forEach( product -> {
//				String newQuickFindkey = this.getQuickFindKey(product, newBrand);
//				
//				Update update = new Update();
//				update.set("quickFindKey", newQuickFindkey);
//				update.set("spu.manufacturerId", newBrand.getId());
//				update.set("spu.manufacturer", newBrand.getBrandName());
//				update.set("spu.manufacturerAgg", newBrand.getBrandName());
//				update.set("spu.manufacturerShort", newBrand.getBrandName());
//				String manufacturerPartNumber = product.getSpu().getManufacturerPartNumber().trim().toUpperCase();
//				String newSpuId = manufacturerPartNumber +"-" + newBrandNameTemp.trim().toUpperCase();
//				update.set("spu.spuId", newSpuId);
//				//转化成新品牌后是否有物料信息
//				List<ProductStand> stands = productStandRepository.findProductStandBySpuId(newSpuId);
//				if(CollectionUtils.isNotEmpty(stands)){
//					update.set("spu._id", stands.get(0).getId());
//				}
//				String oldQuickFindkey = product.getQuickFindKey();
//				try{
//					mongoTemplate.updateFirst(query(where("_id").is(product.getId())), update, Product.class);
//					logger.info("品牌转移，Product，spu._id改变  update:"+update.toString());
//				}catch(Exception e){
//					logger.error("异常：oldQF="+oldQuickFindkey+" newQF="+newQuickFindkey+" 错误信息："+e.getMessage(),e);
//					//throw new SystemException(e);
//				}
//			});
//		}
//	}
//	
//	
//	public String getQuickFindKey(Product product,ProductBrand newBrand){
//		String sourceId = product.getSourceId() !=null ? product.getSourceId():"";
//		String manufacturerId = newBrand.getId()!=null ? newBrand.getId().toString():newBrand.getBrandName();
//		manufacturerId = StringUtils.isNotEmpty(manufacturerId) ? manufacturerId:"";
//		String skuId = product.getSkuId();
//		return (sourceId + "-" + manufacturerId + "-" + skuId).toUpperCase();
//	}
//}
