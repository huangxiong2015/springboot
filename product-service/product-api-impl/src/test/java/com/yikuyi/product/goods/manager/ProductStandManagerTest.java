/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.model.ProductAttachment;
import com.yikuyi.product.model.ProductDocument;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.vo.ProductStandRequest;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.context.ContextAttributes;
import com.ykyframework.context.RuntimeContextHolder;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.MQConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Rollback
public class ProductStandManagerTest {

	@Autowired
	private ProductStandManager productStandManager;
	
	@Autowired
	private MongoRepository<ProductStand, String> productStandRepository;
	
	@Autowired
    private MongoOperations operations;
	
	@Before
	public void setUpBefore() throws Exception {
		RuntimeContextHolder.setContextAttributes(new ContextAttributes());
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_USER_NAME, "admin");
		RuntimeContextHolder.currentContextAttributes().setAttribute(MQConstants.MSG_HEADER_PASS_WORD, "9999999901");
	}

	/**
	 * 
	 * 物料列表
	 * 1按型号模糊
	 * 2按关键字模糊查询
	 * @since 2017年2月23日
	 * @author zr.wujiajun@yikuyi.com
	 */
	@Test
	public void testList() {
//		//1.按型号又模糊查询	 
		ProductStandRequest standRequest = new ProductStandRequest("3352P-1-wwww",null, null, 
				"2014-08-11", "2017-09-16", 0, null, 
				1, 17, 18, null, null);
		PageInfo<ProductStand> datas = productStandManager.list(standRequest, 1, 20);
		Assert.assertEquals(true, CollectionUtils.isNotEmpty(datas.getList()));
		
		standRequest = new ProductStandRequest("3352P-1-wwww",null, null, 
				null, "2017-09-16", 0, null, 
				1, 17, 18, null, null);
		datas = productStandManager.list(standRequest, 1, 20);
		Assert.assertEquals(true, CollectionUtils.isNotEmpty(datas.getList()));
		
		standRequest = new ProductStandRequest("3352P-1-wwww",null, null, 
				"2014-08-11", null, 0, null, 
				1, 17, 18, null, null);
		datas = productStandManager.list(standRequest, 1, 20);
		Assert.assertEquals(true, CollectionUtils.isNotEmpty(datas.getList()));
		
		
		standRequest = new ProductStandRequest("3352P-1-wwww",null, "333333", 
				"2014-08-11", "2017-09-16", 0, null, 
				1, 17, 18, null, null);
		datas = productStandManager.list(standRequest, 0, 20);
		Assert.assertEquals(true, CollectionUtils.isEmpty(datas.getList()));
	}
	
	@Test
	public void testSetDefaultCategories(){
		ProductStand result = new ProductStand();
		productStandManager.setDefaultCategories(result);
	}
	
	/**
	 * 根据查询条件导出cvs
	 * 
	 * @since 2017年9月15日
	 * @author injor.huang@yikuyi.com
	 */
	
	@Test
	public void exportExcelByConditionTest(){
		ProductStandRequest standRequest = new ProductStandRequest("3352P-1-wwww",null, null, 
				"2014-08-11", "2017-09-16", 0, null, 
				1, 17, 18, null, null);
		File file=null;
		try {
			file = productStandManager.exportExcelByCondition(standRequest, "", "test.cvs");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(true, file != null);
	}
	
	/**
	 * 测试查询单个物料数据
	 * 
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetProductStand(){
		ProductStand stand = this.initData();
		ProductStand info = productStandManager.getProductStand(stand.getId());
		Assert.assertEquals("ManufacturerName",info.getManufacturer());
		Assert.assertEquals("spuId112233",info.getSpuId());
		productStandRepository.delete(stand);
	}
	@Test
	public void testupdateProductStand(){
		ProductStand productStand = initData();
		productStandManager.updateProductStand(productStand);
	}
	
	@Test
	public void testfindProductStandByNo(){
		List<ProductStand> list = productStandManager.findProductStandByNo("2900-253091");
		Assert.assertTrue(CollectionUtils.isNotEmpty(list));
	}
	
	/**
	 * 初始化物料数据
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private ProductStand initData(){
		ProductStand psInfo = new ProductStand();
		psInfo.setId("112233");
		psInfo.setSpuId("spuId112233");
		psInfo.setManufacturer("ManufacturerName");
		psInfo.setManufacturerId(1234);
		psInfo.setManufacturerPartNumber("ManufacturerPartNumber");
		psInfo.setDescription("DescriptionDescription");
		psInfo.setManufacturerAgg("setManufacturerAgg");
		psInfo.setRohs(true);
		//分类
		List<ProductCategory> listCat = new ArrayList<>();
		ProductCategory cat = new ProductCategory();
		cat.setId(1);
		cat.setName("大类");
		cat.setLevel(1);
		cat.setStatus(1);
		listCat.add(cat);
		ProductCategory cat1 = new ProductCategory();
		cat1.setId(2);
		cat1.setName("小类");
		cat1.setLevel(2);
		cat1.setStatus(1);
		listCat.add(cat1);
		ProductCategory cat2 = new ProductCategory();
		cat2.setId(3);
		cat2.setName("次小类");
		cat2.setLevel(3);
		cat2.setStatus(1);
		listCat.add(cat2);
		psInfo.setCategories(listCat);
		//文档
		List<ProductDocument> pdList = new ArrayList<>();
 		ProductDocument pd = new ProductDocument();
 		List<ProductAttachment> attachList = new ArrayList<>();
 		ProductAttachment attach = new ProductAttachment();
 		attach.setName("AttachmentName");
 		attach.setUrl("AttachmentUrl");
 		attachList.add(attach);
 		pd.setAttaches(attachList);
 		pd.setDescription("documentDescription");
 		pd.setName("documentName");
 		pd.setType("documentType");
 		pd.setUrl("documentUrl");
 		pdList.add(pd);
 		psInfo.setDocuments(pdList);
 		//图片
 		List<ProductImage> imageList = new ArrayList<>();
 		ProductImage image = new ProductImage();
 		image.setType("imageType");
 		image.setUrl("imageUrl");
 		imageList.add(image);
 		psInfo.setImages(imageList);
 		//物料参数
 		List<ProductParameter> paramList = new ArrayList<>();
 		ProductParameter param = new ProductParameter();
 		param.setCode("paramCode111");
 		param.setName("paramName111");
 		param.setValue("paramValue111");
 		paramList.add(param);
 		ProductParameter param1 = new ProductParameter();
 		param1.setCode("paramCode222");
 		param1.setName("paramName222");
 		param1.setValue("paramValue222");
 		paramList.add(param1);
 		psInfo.setParameters(paramList);
 		productStandRepository.delete(psInfo);
 		productStandRepository.insert(psInfo);
		return psInfo;
	}
	
	
	/**
	 * 初始化物料数据
	 * @return
	 * @since 2017年2月23日
	 * @author zr.wenjiao@yikuyi.com
	 */
	private RawData initRawData(){
		RawData psInfo = new RawData();
		psInfo.setManufacturer("PANJIT");
		psInfo.setManufacturerPartNumber("Manufact"+new Random().nextInt(1000));
		psInfo.setDescription("DescriptionDescription");
		psInfo.setRohs("true");
		psInfo.setFrom("BASIS_FILE_UPLOAD");
		
		//分类
		List<ProductCategory> listCat = new ArrayList<>();
		ProductCategory cat1 = new ProductCategory();
		cat1.setName("wifi模块");
		cat1.setLevel(2);
		cat1.setStatus(1);
		listCat.add(cat1);
		ProductCategory cat2 = new ProductCategory();
		cat2.setName("wifi模块");
		cat2.setLevel(3);
		cat2.setStatus(1);
		listCat.add(cat2);
		psInfo.setVendorCategories(listCat);
		//文档
		List<ProductDocument> pdList = new ArrayList<>();
 		ProductDocument pd = new ProductDocument();
 		List<ProductAttachment> attachList = new ArrayList<>();
 		ProductAttachment attach = new ProductAttachment();
 		attach.setName("AttachmentName");
 		attach.setUrl("AttachmentUrl");
 		attachList.add(attach);
 		pd.setAttaches(attachList);
 		pd.setDescription("documentDescription");
 		pd.setName("documentName");
 		pd.setType("documentType");
 		pd.setUrl("documentUrl");
 		pdList.add(pd);
 		psInfo.setDocuments(pdList);
 		//图片
 		List<ProductImage> imageList = new ArrayList<>();
 		ProductImage image = new ProductImage();
 		image.setType("imageType");
 		image.setUrl("imageUrl");
 		imageList.add(image);
 		psInfo.setImages(imageList);
 		//物料参数
 		List<ProductParameter> paramList = new ArrayList<>();
 		ProductParameter param = new ProductParameter();
 		param.setCode("paramCode111");
 		param.setName("paramName111");
 		param.setValue("paramValue111");
 		paramList.add(param);
 		ProductParameter param1 = new ProductParameter();
 		param1.setCode("paramCode222");
 		param1.setName("paramName222");
 		param1.setValue("paramValue222");
 		paramList.add(param1);
 		psInfo.setParameters(paramList);
		return psInfo;
	}
	/**
	 * 创建物料
	 */
	@Test
	public void  testCreateProductStand(){
		List<RawData> rawdatas = new ArrayList<>();
		RawData data = initRawData();
		rawdatas.add(data);
		Assert.assertTrue(StringUtils.isBlank(productStandManager.createProductStand(rawdatas)));
	}
	
	/**
	 * 导出
	 * @throws BusinessException
	 */
	@Test
	public void testExport(){
		ProductStandRequest request= new ProductStandRequest();
		request.setIds("1483689359095159");
		try {
			productStandManager.export(request, "123");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行下载任务
	 */
	@Test
	public void testDoExport(){
		ProductStandRequest request= new ProductStandRequest();
		request.setIds("1483689358626098");
		request.setTaskId("911124658751799296");
		productStandManager.doExport(request);
		
		request= new ProductStandRequest();
		request.setAuditUserName("gesfe");
		request.setTaskId("911124658751799296");
		productStandManager.doExport(request);
	}
	
	@Test
	public void testUploadFileAliyun(){
		File excelFile = new File("http://ictrade-private-hz-uat.oss-cn-hangzhou.aliyuncs.com/sit/product/export/201711/17/392fd98f54ffbbdbe95fdb81b8789e58.csv");
		try {
			productStandManager.uploadFileAliyun(excelFile, "product.export");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 合并参数
	 */
	@Test
	public void  testMergeParameterData(){
		List<ProductParameter> parameters = Lists.newArrayList();
		ProductParameter par = new ProductParameter();
		par.setName("1");
		parameters.add(par);
		par = new ProductParameter();
		par.setName("4");
		parameters.add(par);
		List<ProductParameter> orignParameters = Lists.newArrayList();
		par = new ProductParameter();
		par.setName("1");
		orignParameters.add(par);
		par = new ProductParameter();
		par.setName("2");
		orignParameters.add(par);
		par = new ProductParameter();
		par.setName("3");
		orignParameters.add(par);
		List<ProductParameter> list = productStandManager.mergeParameterData(parameters, orignParameters);
		Assert.assertTrue(CollectionUtils.isNotEmpty(list));
	}
	
	@Test
	public void testDealErrorList(){
		List<ProductStand> standList = Lists.newArrayList();
		ProductStand stand = new ProductStand();
		stand.setManufacturer("2");
		stand.setManufacturerPartNumber("1");
		standList.add(stand);
		List<ProductStandAudit> standAuditList=Lists.newArrayList();
		ProductStandAudit standAudit = new ProductStandAudit();
		standAudit.setManufacturer("2");
		standAudit.setManufacturerPartNumber("1");
		standAuditList.add(standAudit);
		List<RawData> rawdatas=Lists.newArrayList();
		RawData  data= new RawData();
		data.setManufacturer("2");
		data.setManufacturerPartNumber("1");
		rawdatas.add(data);
		Exception e = new Exception("error");
		List<RawData> datas = productStandManager.dealErrorList(standList, standAuditList, rawdatas, e);
		Assert.assertTrue(CollectionUtils.isNotEmpty(datas));
	}
	
	/**
	 * 更新ProductStand里面的品牌
	 * @since 2017年4月1日
	 * @author tongkun@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpdateProductStandByBrandId() throws BusinessException{
		ProductStand productStand = new ProductStand(); 
		productStand.setManufacturerId(870);
		productStandManager.updateProductStandByBrandId(productStand);
	}
	
	@Test
	public void doProcess(){
        ExcelReader eh = new ExcelReader("D:\\333.xlsx","Sheet1");
        for(int i = 2; i<10000;i++){
        	JSONObject json = new JSONObject();
        	String mpn = eh.getCellData(i, 1);
        	String mpr = eh.getCellData(i, 2);
        	//受控
//        	String control = eh.getCellData(i, 3);
        	//关税
        	String tax = eh.getCellData(i, 3);
        	//商检
        	String inspection = eh.getCellData(i, 4);
        	if(StringUtils.isEmpty(mpn)){
        		break;
        	}
        	json.put("mpr", StringUtils.trimToEmpty(mpr));
        	json.put("mpn", StringUtils.trimToEmpty(mpn));
//        	json.put("control", StringUtils.trimToEmpty(control));
        	json.put("tax", StringUtils.trimToEmpty(tax));
        	json.put("inspection", StringUtils.trimToEmpty(inspection));
        	operations.save(json, "product_stand_hx_temp");
        }
	}
}
