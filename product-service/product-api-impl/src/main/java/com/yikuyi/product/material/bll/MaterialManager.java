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
package com.yikuyi.product.material.bll;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.util.IOUtils;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.jasig.inspektr.audit.annotation.Audit;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framewrok.springboot.web.RequestHelper;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.index.EsIndexManager;
import com.ictrade.index.IndexProcesserFactory;
import com.ictrade.index.IndexProcesserFactory.IndexFactoryType;
import com.ictrade.tools.FileUtils;
import com.ictrade.tools.excel.ExcelCellData;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactory;
import com.ictrade.tools.leadin.LeadInProcesser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.model.Document.CancelStatus;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.document.model.DocumentTitle;
import com.yikuyi.document.model.ProductDocument;
import com.yikuyi.document.vo.DocumentLogVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.resource.FacilityClient;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.config.FtpAccountConfig;
import com.yikuyi.product.document.bll.DocumentLogManager;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.document.bll.ProductDocumentManager;
import com.yikuyi.product.document.dao.DocumentDao;
import com.yikuyi.product.document.dao.DocumentLogDao;
import com.yikuyi.product.document.dao.DocumentTitleDao;
import com.yikuyi.product.essync.util.EssyncHelp;
import com.yikuyi.product.goods.manager.ProductStandManager;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.template.bll.ProductTemplateManager;
import com.yikuyi.product.vo.ProductEsVo;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
import com.ykyframework.mqservice.sender.MsgSender;
import com.ykyframework.oss.AliyunOSSOperator;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * 物料处理业务类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class MaterialManager {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MaterialManager.class);

	@Autowired
	private MsgSender msgSender;
	
	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private AliyunOSSOperator aliyunOSSOperator;

	@Autowired
	private ProductTemplateManager productTemplateManager;

	@Autowired
	private DocumentManager documentManager;

	@Autowired
	private DocumentLogManager documentLogManager;

	@Autowired
	private ProductDocumentManager productDocumentManager;

	@Autowired
	private BrandManager brandManager;

	@Autowired
	private LeadMaterialParser parser;

	@Autowired
	private RedisCacheManager redisCacheManagerNoTransaction;
	
	@Autowired
	private ProductStandManager productStandManger;
	
	@Autowired
	private MaterialAsyncManager materialAsyncManager;
	
	@Autowired
	private JedisPool jedisPool;

	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopicName;
	
	@Value("${mqProduceConfig.quickCreateProductSub.topicName}")
	private String quickCreateProductTopicName;
	
	@Value("${mqConsumeConfig.uploadFileProduct.topicName}")
	private String uploadFileProductTopicName;

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	@Autowired
	private DocumentTitleDao documentTitleDao;
	
	@Autowired
	private FtpAccountConfig ftpAccountConfig;
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private DocumentLogDao documentLogDao;
	
	/**
	 * ftp下载WPG的vendorId
	 */
	@Value("${downloadFtp.vendorId.wpg}")
	private String wpg;
	
	/**
	 * ftp下载willas的vendorId
	 */
	@Value("${downloadFtp.vendorId.willas}")
	private String willas;
	
	/**
	 * ftp下载rs的vendorId
	 */
	@Value("${downloadFtp.vendorId.rs}")
	private String rs;
	
	/**
	 * 导入库存上传文件路径
	 */
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;
	
	@Value("${api.basedata.serverUrlPrefix}")
	private String basedataServerUrlPrefix;
	
	@Autowired
	private PartyClientBuilder shipmentClientBuilder;
	
	@Autowired 
	private EsIndexManager esIndexManager;
	
	@Autowired
	private InventorySearchManager inventorySearchManager;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<DocumentTitle>> documentTitleOps;
	
	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> aliasFacilityOps;

	// 用来存储某个批次文件解析状态
	public static final String MATERIAL_IMPORT_CACHE_STATUS_KEY = "materialImportCache_status_%s";
	// 用来存储某个批次文件发送Create_Product,MQ次数
	public static final String MATERIAL_IMPORT_CACHE_KEY = "materialImportCache_list_%s";
	//是否是最后一批数据缓存
	public static final String MATERIAL_LASTLINE_CACHE_KEY = "materialLastLineCache_list_%s";
	//仓库缓存
	public static final String MATERIAL_FACILITY_CACHE_KEY = "materialFacilityCache";
	// 用来存储某个批次文件同步elasticsearch次数,如果次数和上面存储的次数一致,代表这个批次号处理完成,更新数据
	public static final String MATERIAL_IMPORT_CACHE_COUNT_KEY = "materialImportCache_list_%s_count";
	// 用来做文件上传redisCache的key
	public static final String MATERIAL_IMPORT_CACHE_NAME_KEY = "materialImportCache";
	//用来做DocumentTitleCache的key
	public static final String MATERIAL_DOCUMENT_TITLE_CACHE_KEY = "materialDocumentTitle_cache_%s";

	private static final int BATCH_SIZE = 100;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> materialCountCacheOps;
	
	/**
	 * 存储发送MQ数量的过程记录的cache
	 */
	public static final String MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY = "material_count_cache_list_cache_%s";

	/**
	 * 文件上传,保存document记录
	 * 
	 * @param fileUrl
	 * @param oriFileName
	 * @param vendorId
	 * @return
	 * @since 2016年12月12日
	 * @author zr.shuzuo@yikuyi.com
	 */
	@Audit(action = "Product Uploadqqq;;;上传了'#materialVo.vendorName'商品数据", actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public String fileUpload(@com.framework.springboot.audit.Param("materialVo") MaterialVo materialVo , DocumentType documentType) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		com.yikuyi.document.model.Document document = new com.yikuyi.document.model.Document();
		document.setId(id);
		document.setTypeId(documentType);
		document.setDocumentLocation(materialVo.getFileUrl());
		document.setDocumentName(materialVo.getOriFileName());
		document.setStatusId(DocumentStatus.DOC_CREATED);
		document.setPartyId(materialVo.getVendorId());
		document.setFacilityId(materialVo.getRegionId());
		document.setCreator(null ==  RequestHelper.getLoginUser() ? "Admin" : RequestHelper.getLoginUser().getUsername());
		document.setLastUpdateUser(RequestHelper.getLoginUserId());
		ProductDocument productDocument = new ProductDocument();
		productDocument.setId(id);
		productDocument.setPartyId(RequestHelper.getLoginUserId());
		productDocument.setCreator(RequestHelper.getLoginUserId());
		productDocument.setLastUpdateUser(RequestHelper.getLoginUserId());

		documentManager.insertDoc(document);
		productDocumentManager.insertProductDoc(productDocument);
		
		materialVo.setDocId(id);
		return id;
	}

	/**
	 * 检测上传文件中的物料数据
	 * 
	 * @param fileUrl
	 * @param outputStream
	 * @since 2017年2月7日
	 * @author zr.wanghong
	 */
	public void materialDetectionNew(String fileUrl, OutputStream outputStream) {
		Long fileId = IdGen.getInstance().nextId();
		ExportProcesser processer = null;

		try {
			String fileName = fileDownload(fileUrl, "" , String.valueOf(fileId));
			LeadInProcesser leadInProcesser = LeadInFactory
					.createProcess(new File(leadMaterialFilePath + File.separator + fileName));
			String[] lineData;
			leadInProcesser.getNext();// 过滤第一行
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, outputStream);
			Map<String, ProductBrand> brand = brandManager.getAliasBrandMap();// 品牌

			//品牌sheet页的序号
			int brandSerialNo = 1;
			//型号sheet页的序号
			int partNoSerialNo = 1;
			//模糊匹配sheet页的序号
			int fuzzyMatchSerialNo = 1;
			// 总记录条数
			int totalLineData = 0;

			// 品牌不标准集合
			List<ExcelCellData[]> noStandardBrandList = new ArrayList<>();
			// 型号不标准集合
			List<ExcelCellData[]> noStandardPartNoList = new ArrayList<>();
			// 模糊匹配结果集合
			List<String[]> fuzzyMatchList = new ArrayList<>();

			while ((lineData = leadInProcesser.getNext()) != null && leadInProcesser.getReadLength() <= 5000) {

				if (lineData != null && lineData.length == 1) {
					if (StringUtils.isEmpty(lineData[0])) {
						continue;
					}
				}
				if (lineData != null && lineData.length == 2) {
					if (StringUtils.isEmpty(lineData[0]) && StringUtils.isEmpty(lineData[1])) {
						continue;
					} else {
						CellStyle cs = processer.createStyle();// 获取样式
						this.setCelllStyle(processer, cs);
						// 型号为空
						if (StringUtils.isEmpty(lineData[0])) {
							noStandardPartNoList.add(new ExcelCellData[] { new ExcelCellData(String.valueOf(partNoSerialNo), null),
											new ExcelCellData(lineData[1], null), new ExcelCellData(lineData[0], cs) });
							partNoSerialNo++;
							totalLineData++;
							continue;
						}
						// 品牌为空
						if (StringUtils.isEmpty(lineData[1])) {
							noStandardBrandList.add(new ExcelCellData[] { new ExcelCellData(String.valueOf(brandSerialNo), null),
											new ExcelCellData(lineData[1], cs), new ExcelCellData(lineData[0], null) });
							brandSerialNo++;
							totalLineData++;
							continue;
						}

					}

				}

				if (lineData != null && lineData.length > 0) {
					totalLineData++;
				}

				// 型号标准，品牌是否标准, true标准,false不标准
				boolean isStandardBrand = false;
				// 型号是否标准, true标准,false不标准
				boolean isStandardPartNo = false;

				if (lineData.length >= 2) {
					// 查询标准库，判断型号是否标准
					List<ProductStand> productStands = productStandManger.findProductStandByNo(lineData[0]);

					// 型号标准
					if (!CollectionUtils.isEmpty(productStands)) {
						isStandardPartNo = true;
						for (ProductStand productStand : productStands) {
							//spu中的品牌名称
							String manufacturer = productStand.getManufacturer();
							ProductBrand productBrand = brand.get(lineData[1].trim().toUpperCase());
							//product_brand中的BrandName
							String brandName = productBrand != null ? productBrand.getBrandName():"";
							if(StringUtils.isNotEmpty(manufacturer) && manufacturer.equalsIgnoreCase(brandName)){
								isStandardBrand = true;
								continue;
							}
						}
						
						if(isStandardBrand){
							continue;
						}
					}
					ExcelCellData cellBrand = null;
					ExcelCellData cellPartNo = null;

					// 型号不标准
					if (!isStandardPartNo) {

						isStandardBrand = brand
								.containsKey(null == lineData[1] ? "" : lineData[1].trim().toUpperCase());
						// 但品牌标准
						if (isStandardBrand) {
							// 根据型号模糊匹配
							List<String> result = new ArrayList<>();
							JSONObject condition = new JSONObject();
							condition.put("keyword", lineData[0]);
							condition.put("brand", brand.get(lineData[1].trim().toUpperCase()).getBrandName().toLowerCase());
							condition.put("size", "20");
							JSONObject response = inventorySearchManager.searchMultiMatchPartNo(condition);
							JSONArray jsonArray = response.getJSONArray("hits");
							if(!CollectionUtils.isEmpty(jsonArray)){
								for (Object object : jsonArray) {
									JSONObject json = (JSONObject)object;
									if(json.get("spu") != null ){
										JSONObject spu = json.getJSONObject("spu");
										String manufacturerPartNumber = (String)spu.get("manufacturerPartNumber");
										result.add(manufacturerPartNumber);
									}
								}
							}
							// list去重
							if (!CollectionUtils.isEmpty(result)) {
								HashSet<String> set = new HashSet<>(result);
								result.clear();
								result.addAll(set);
							}

							List<String> cellArray = new ArrayList<>();
							cellArray.add(String.valueOf(fuzzyMatchSerialNo));
							cellArray.add(lineData[1]);
							cellArray.add(lineData[0]);
							cellArray.addAll(result);

							if (!CollectionUtils.isEmpty(result)) {
								fuzzyMatchList.add((String[]) cellArray.toArray(new String[cellArray.size()]));
								fuzzyMatchSerialNo++;
								continue;
							}
						}

						// 型号品牌都不标准，标红显示型号
						CellStyle cs = processer.createStyle();// 获取样式
						this.setCelllStyle(processer, cs);
						cellPartNo = new ExcelCellData(lineData[0], cs);

						noStandardPartNoList.add(new ExcelCellData[] { new ExcelCellData(String.valueOf(partNoSerialNo), null),
										new ExcelCellData(lineData[1], null), cellPartNo });
						partNoSerialNo++;
					} else {

						// 型号标准，但品牌不标准
						if (!isStandardBrand) {
							CellStyle cs = processer.createStyle();// 获取样式
							this.setCelllStyle(processer, cs);
							cellBrand = new ExcelCellData(lineData[1], cs);

							noStandardBrandList.add(new ExcelCellData[] { new ExcelCellData(String.valueOf(brandSerialNo), null),
											cellBrand, new ExcelCellData(lineData[0], null) });
							brandSerialNo++;
						}

					}

				} else if (lineData.length == 1) {
					CellStyle cs = processer.createStyle();// 获取样式
					this.setCelllStyle(processer, cs);
					noStandardPartNoList.add(new ExcelCellData[] { new ExcelCellData(String.valueOf(partNoSerialNo), null), null,
									new ExcelCellData(lineData[0], cs) });
					partNoSerialNo++;
				} else {
					continue;
				}
			}
			String checkResultStr = "";
			if (totalLineData > 0) {
				int standardMaterialCount = totalLineData- (noStandardBrandList.size() + noStandardPartNoList.size() + fuzzyMatchList.size());
				checkResultStr = "检测完成" + totalLineData + "个料，" + noStandardBrandList.size() + "个品牌非标准，" + ""
						+ noStandardPartNoList.size() + "个型号非标准，" + fuzzyMatchList.size() + "个不完整，"
						+ standardMaterialCount + "个标准物料";
			}

			processer.writeLine("品牌非标准", new ExcelCellData[] { new ExcelCellData(checkResultStr, null) });

			// 空行
			processer.writeLine("品牌非标准", new ArrayList<>());
			processer.writeLine("型号非标准", new ArrayList<>());
			processer.writeLine("模糊匹配", new ArrayList<>());

			for (int i = 0; i < noStandardBrandList.size(); i++) {
				if (i == 0)
					processer.writeLine("品牌非标准", Arrays.asList("序号", "品牌", "型号"));
				processer.writeLine("品牌非标准", noStandardBrandList.get(i));

			}

			for (int i = 0; i < noStandardPartNoList.size(); i++) {
				if (i == 0)
					processer.writeLine("型号非标准", Arrays.asList("序号", "品牌", "型号"));
				processer.writeLine("型号非标准", noStandardPartNoList.get(i));

			}
			for (int i = 0; i < fuzzyMatchList.size(); i++) {
				if (i == 0)
					processer.writeLine("模糊匹配", Arrays.asList("序号", "品牌", "型号", "匹配型号"));
				processer.writeLine("模糊匹配", fuzzyMatchList.get(i));

			}

			processer.output();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			if (null != processer) {
				processer.close();
			}
		}
	}

	private void setCelllStyle(ExportProcesser processer, CellStyle cs) {
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 指定填充方式为单色
		cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景颜色。
		Font font = processer.createFont();// 创建字体
		font.setFontName("黑体");// 字体类型
		font.setFontHeightInPoints((short) 15);// 字号
		font.setColor(HSSFColor.RED.index); // 字体颜色
		cs.setFont(font);// 塞进去
	}

	/**
	 * 解析正常商品上传的文件 <br>
	 * 1:下载阿里云文件到本地目录 <br>
	 * 2:下载文件对应的解析Templater<br>
	 * 3:通过后缀使用excel解析数据到List<String[]> <br>
	 * 4:记录Doc在mysql的原始记录信息<br>
	 * 5:把解析的数据List<String[]>通过模版转换为RawData,把RawData写入mysql,并且发送数据整理MQ<br>
	 * 6:删除文件(目前流没关闭,暂时无法关闭)
	 * 
	 * @param fileUrl
	 * @param docId同等processId
	 * @since 2016年12月13日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public String parseImportFile(MaterialVo materialVo) {
		String fileName = "";
		int minLineNo = 0;
		boolean isCancel = false;
		String isLastLine = "false";
		Map<String, Integer> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
		//add by guobin当仓库Id为空  代表是下载处理，则需用供应商Id和仓库名称去basedata表中查询仓库Id
		Map<String,String> facilityMap = new HashMap<>();
		if (null == materialVo.getRegionId()) {
			FacilityClient client = shipmentClientBuilder.facilityResource();
			client.getFacilityList(materialVo.getVendorId()).stream().forEach(fac->facilityMap.put(StringUtils.isNotBlank(fac.getFacilityNameAlia())?fac.getFacilityNameAlia():fac.getFacilityName(),fac.getId()));
			logger.info("Vendor Facility-end:{}",facilityMap);
		}
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		String countCacheListTotalKey = String.format(MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY,materialVo.getDocId());
		try {
			logger.info("文件解析开始:" + materialVo.getFileUrl());
			long time1 = System.currentTimeMillis();
			fileName = fileDownload(materialVo.getFileUrl(), materialVo.getOriFileName() ,  materialVo.getDocId());
			long time2 = System.currentTimeMillis();
			logger.info("下载文件耗时:" + (time2 - time1));
			ProductTemplate proTemplate = productTemplateManager.geTemplate(materialVo.getVendorId(),materialVo.getRegionId());// 获取模板
			long time3 = System.currentTimeMillis();
			logger.info("加载模板耗时:" + (time3 - time2));
			//如果是压缩文件则进行解压
			if("zip".equalsIgnoreCase(FileUtils.getPrefix(fileName))){
				fileName = FileUtils.extractFirstFile(leadMaterialFilePath, fileName, true);
			}
			 DocumentLogVo documentLogVo = new DocumentLogVo();
			 documentLogVo.setCountCacheList(0);
			 documentLogVo.setIsCancel(isCancel);
			Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
			materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
					DocumentStatus.DOC_IN_PROCESS);// 标记文件正在处理
			int dataSize = readExcelDatas(leadMaterialFilePath + File.separator + fileName, proTemplate, materialVo,minLineNo,quickFindKeyMap,brandMap,facilityMap,documentLogVo,isLastLine);// 读取数据
			long time4 = System.currentTimeMillis();
			logger.info("读取文件完成，总共读取到:" +dataSize + "条数据");
			logger.info("读取文件耗时:" + (time4 - time3));
			long time5 = System.currentTimeMillis();
			logger.info("修改Doc记录耗时:" + (time5 - time4));
			//writeDataToMongodb(datas, proTemplate, materialVo);
			long time6 = System.currentTimeMillis();
			logger.info("文件解析耗时:{}" + (time6 - time5));
		} catch (Exception e) {
			Integer countCacheList = materialCountCacheOps.get(countCacheListTotalKey, countCacheListTotalKey);
			if (null == countCacheList || countCacheList == 0) {
				// 出现异常,并且一个数据也没有发送MQ,直接标记失败
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						DocumentLog.getSizeLengthMsg(e));
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
						DocumentStatus.DOC_PRO_PART_SUCCESS);// 标记文件部分处理失败,放弃后续处理
			}
			logger.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}finally{
			materialCountCacheOps.delete(countCacheListTotalKey, countCacheListTotalKey);
		}
		return fileName;
	}
	
	/**
	 * 阿里云文件下载(步骤1) {@link DocumentManager.parseImportFile}
	 * 
	 * @param fileUrl
	 * @param docId
	 * @return
	 * @throws SystemException
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public String fileDownload(String fileUrl, String oriName, String docId) throws SystemException {
		String fileName = null;
		if(StringUtils.isNotBlank(oriName)){
			fileName = docId + oriName.substring(oriName.lastIndexOf('.'));
		}else{
			fileName = docId + fileUrl.substring(fileUrl.lastIndexOf('.'));
		}
		
		// 应用路径
		int length = 0;
		byte[] bytes = new byte[1024];
		String abPath = leadMaterialFilePath + File.separator + fileName;
		try (FileOutputStream os = new FileOutputStream(abPath);
				InputStream is = this.aliyunOSSOperator.getObject(fileUrl);
			) {
			while ((length = is.read(bytes)) > 0) {
				os.write(bytes, 0, length);
			}
		} catch (Exception e) {
			logger.error("文件下载失败：url是："+fileUrl+" 错误原因是："+e.getMessage(),e);
			throw new SystemException(e.getMessage(), e);
		}
		// 异常情况
		return fileName;
	}
	
	/**
	 * 读取数据(步骤3) {@link DocumentManager.parseImportFile}
	 * 
	 * @param url
	 * @param proTemplate
	 * @return
	 * @throws SystemException
	 * @since 2016年12月13日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private int readExcelDatas(String url, ProductTemplate proTemplate,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException {
		try {
			LeadMaterialExcelReader reader = new LeadMaterialExcelReader();
			return reader.readByFile(url, proTemplate, materialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);// 读取的数据
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	/**
	 * 修改Doc记录信息,标题和文件总行数(步骤4)<br>
	 * {@link DocumentManager.parseImportFile}
	 * {@link DocumentManager.processingFutureJob}
	 * @param docId
	 * @param title
	 * @param size
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */	
	void updateDocInfo(String docId, String[] title){
		try {
			String documentTitleKey = String.format(MATERIAL_DOCUMENT_TITLE_CACHE_KEY,docId);
			
			List<DocumentTitle> list = new ArrayList<>();
			DocumentTitle docTitle = new DocumentTitle();
			docTitle.setId(docId);
			docTitle.setSheetIndex(0);
			docTitle.setSheetName("");
			docTitle.setOriginalTitle(String.join(LeadMaterialParser.VALUE_SEPARATE, title));
			list.add(docTitle);
			documentTitleDao.insertDocTitles(list);
			documentTitleOps.put(documentTitleKey, documentTitleKey, list);
			documentTitleOps.getOperations().expire(documentTitleKey, 1, TimeUnit.DAYS);//设置缓存失效时间为1天
			logger.info(docId+"商品插入documentTitle表"+new Date());
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	/**
	 * 修改doc的数据总数 
	 * @param docId
	 * @param size
	 */
	void updateDocSize(String docId, int size){
		com.yikuyi.document.model.Document doc = new com.yikuyi.document.model.Document();
		doc.setId(docId);
		doc.setDataCount(size);
		documentManager.updateDoc(doc);
	}
	
	/**
	 * 转换datas数据成VO,并且批量调用整理数据MQ(步骤5)
	 * {@link DocumentManager.parseImportFile}
	 * {@link DocumentManager.processingFutureJob}
	 * 
	 * @param datas
	 * @param proTemplate
	 * @param vendorId供应商ID
	 * @param docId文档ID==批次号
	 * @throws SystemException
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private void writeDataToMongodb(List<String[]> datas, ProductTemplate proTemplate, MaterialVo materialVo) {
		Map<String, Integer> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
		List<DocumentLog> batchInsertList = new ArrayList<>();// 初始化批量日志llist
		ArrayList<Document> arrangementDatas = new ArrayList<>();// 初始化批量数据清理list
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
		//add by guobin当仓库Id为空  代表是下载处理，则需用供应商Id和仓库名称去basedata表中查询仓库Id
		Map<String,String> facilityMap = new HashMap<>();
		if (null == materialVo.getRegionId()) {
			logger.info("Vendor Facility-start:{}",materialVo.getVendorId());
			FacilityClient client = shipmentClientBuilder.facilityResource();
			client.getFacilityList(materialVo.getVendorId()).stream().forEach(fac->facilityMap.put(StringUtils.isNotBlank(fac.getFacilityNameAlia())?fac.getFacilityNameAlia():fac.getFacilityName(),fac.getId()));
			logger.info("Vendor Facility-end:{}",facilityMap);
		}
	
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
				DocumentStatus.DOC_IN_PROCESS);// 标记文件正在处理
		Integer countCacheList = 0;// 存储发送MQ数量的过程记录的cache
		boolean isCancel = false;
		try (Jedis jedis = jedisPool.getResource()){
			String lastLineKey = String.format(MATERIAL_LASTLINE_CACHE_KEY, materialVo.getDocId());
			jedis.set(lastLineKey, "true");//将是否是最后一批数据的缓存放进去
			for (int lineNo = 0; lineNo < datas.size(); lineNo++) {
				Document docData = getDocData(materialVo, lineNo, datas.get(lineNo), quickFindKeyMap, proTemplate,
						batchInsertList, brandMap,facilityMap);
				// 数据足够或者数据结束,记录日志
				if (batchInsertList.size() >= BATCH_SIZE || lineNo == datas.size() - 1) {
					documentLogManager.insertDocs(batchInsertList);
					batchInsertList.clear();
				}
				// 如果解析文件返回为NULL,表示数据校验不通过,直接下一个
				if (null == docData && lineNo != datas.size() - 1) {
					continue;
				}
				// 把当前正式对象放入list,便于create_product的MQ发送
				if(null != docData){
					arrangementDatas.add(docData);
				}
				if (arrangementDatas.size() == BATCH_SIZE || lineNo == datas.size() - 1) {// 批量操作
					if(arrangementDatas.isEmpty()){
						continue;
					}
					countCacheList += arrangementDatas.size();// 增加计数
					int testno = 0;
					String key = String.format(MATERIAL_IMPORT_CACHE_KEY, materialVo.getDocId());
					boolean success = false;//是否执行成功
					for(int tryNumber = 0;tryNumber<5;tryNumber++){
						boolean processResult = false;//本次执行是否成功
						boolean breakFlag = false;//本次执行是否需要跳出循环
						//确定执行异常则立刻跳出循环
						try {
							// 如果当前批次为最后一批,更新文档最后状态,否则更新redis缓存处理次数
							jedis.watch(key);
							Transaction trans = jedis.multi();
							trans.incrBy(key, arrangementDatas.size());
							List<Object> result = trans.exec();
							processResult = org.apache.commons.collections.CollectionUtils.isNotEmpty(result);//返回执行是否成功
							logger.debug("上传商品MQ分解：{} key:{} 结果:{} 分解mq的序号：{}",arrangementDatas.size(),key,(result!=null&&!result.isEmpty())?result.get(0):"blank",testno);
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
							breakFlag = true;//不需要再继续执行了，跳出循环
						}
						//执行成功随即跳出循环
						if(processResult){
							success = true;//执行成功，则不需要继续执行
							breakFlag = true;//需要跳出循环
						}
						//如果需要跳出，则break
						if(breakFlag){
							break;
						}
						//如果是线程阻塞，则不会异常，也不会成功，此时需要等待15毫秒后再重试
						try {
							logger.debug("其它线程正在更新，等待重新执行扣减");
							Thread.sleep(15);
						} catch (InterruptedException e) {
							logger.error("扣减等待时间被打断",e);
							throw e;
						}
					}
					testno++;
					jedis.expire(key, 86400);	
					logger.debug("上传附件消息体，条数：{}", arrangementDatas.size());
					if(null != materialVo.getType() && MaterialVoType.FILE_UPLOAD_JOB.toString().equals(materialVo.getType().toString())){
						msgSender.sendMsg(createProductTopicName, arrangementDatas, null);// 发送数据清理MQ
					}else{
						msgSender.sendMsg(quickCreateProductTopicName, arrangementDatas, null);// 发送数据清理MQ
					}
					arrangementDatas.clear();

					isCancel = isCancelImport(materialVo.getDocId());
					if (isCancel) {// 每次提交的时候判断是否已经取消
						break;
					}
				}
			}
			
			// 如果一次计数都没产生,说明所有数据都校验失败,否则代表文件解析完成,改状态为锁定
			if (countCacheList == 0 && !isCancelImport(materialVo.getDocId())) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						"全部数据校验失败,没有发送Create_Product MQ");
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else if (countCacheList == 0 && isCancelImport(materialVo.getDocId())) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_CANCEL, null);
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_LOCKED, null);
			}
		} catch (Exception e) {
			if (countCacheList == 0) {
				// 出现异常,并且一个数据也没有发送MQ,直接标记失败
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						DocumentLog.getSizeLengthMsg(e));
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
						DocumentStatus.DOC_PRO_PART_SUCCESS);// 标记文件部分处理失败,放弃后续处理
			}
			throw new SystemException(e.getMessage(), e);
		}
		if (!isCancel) {
			materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
					DocumentStatus.DOC_PRO_SUCCESS);// 标记文件处理完成
		}
	}

	Document getDocData(MaterialVo materialVo, Integer lineNo, String[] parsms, Map<String, Integer> quickFindKeyMap,
			ProductTemplate proTemplate, List<DocumentLog> batchInsertList, Map<String, ProductBrand> brandMap, Map<String,String> facilityMap) throws JsonProcessingException {
		Document docData;
		// 通过关键字排除重复,如果不重复解析单个文件,并且保存关键字
		String quickFindKey = parser.getQuickFindKey(proTemplate.getTitles(), parsms, materialVo, brandMap , facilityMap);
		docData = parser.parseToRawData(materialVo, lineNo + 2, parsms, proTemplate, batchInsertList, brandMap,facilityMap);// 解析数据成对象,方便插入数据库
		if(null!=docData && StringUtils.isNotBlank(docData.getString(LeadMaterialParser.EXPIRY_DATE))
				&& null!=docData.get(LeadMaterialParser.STOCKS)){
			String expiryDateStr = docData.getString(LeadMaterialParser.EXPIRY_DATE);
			List<Document> stocksList = (List<Document>)docData.get(LeadMaterialParser.STOCKS);
			long qty = Long.parseLong(stocksList.get(0).getString(LeadMaterialParser.QUANTITY));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			String currentTime = sdf.format(new Date());//当前时间
		    Date currentDate;
		    Date expiryDate;
		    long days = 0;
		    boolean expiryDateFlag;
		    try {
		    	currentDate = sdf.parse(currentTime);
				expiryDate=sdf.parse(expiryDateStr);
				days=(expiryDate.getTime()-currentDate.getTime())/(1000*3600*24);//失效日期减去当前时间
			} catch (ParseException e) {
				logger.error("parse date error ,messge is {}",e.getMessage());
				throw new SystemException(e);
			}
			if(qty>0){
				expiryDateFlag = days>0&&days<=15;
				if(!expiryDateFlag){
					parser.addErrorDocLogInList(batchInsertList, materialVo.getDocId(), lineNo + 2, "Expiry Date(失效日期)不能超过15天",
							String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));
					return null;
				}
			}
		} 
		if (null != docData && quickFindKeyMap.containsKey(quickFindKey)) {
			parser.addErrorDocLogInList(batchInsertList, materialVo.getDocId(), lineNo + 2,
					String.format("当前行数据与上传文件%s行数据重复", quickFindKeyMap.get(quickFindKey)),
					String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));
			return null;
		}
		if(null != docData){
			quickFindKeyMap.put(quickFindKey, lineNo + 2);
		}
		/*if (quickFindKeyMap.containsKey(quickFindKey)) {
			parser.addErrorDocLogInList(batchInsertList, materialVo.getDocId(), lineNo + 2,
					String.format("当前行数据与上传文件%s行数据重复", quickFindKeyMap.get(quickFindKey)),
					String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));
			return null;
		} else {
			docData = parser.parseToRawData(materialVo, lineNo + 2, parsms, proTemplate, batchInsertList, brandMap,facilityMap);// 解析数据成对象,方便插入数据库
			quickFindKeyMap.put(quickFindKey, lineNo + 2);
		}*/
		if (null == docData) {
			return null;
		}
		// 补充基本信息
		addConfigurationParameters(docData, materialVo, lineNo + 2, quickFindKey);

		// 校验成功,添加成功记录
		parser.addSuccessDocLogInList(batchInsertList, materialVo.getDocId(), lineNo + 2,
				String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));

		return docData;
	}

	@SuppressWarnings("unchecked")
	void addConfigurationParameters(Document docData, MaterialVo materialVo, int lineNo, String quickFindKey) {
		// 过滤价格和设置单价
		parser.updatePriceLevels(docData);

		String regionTemp = materialVo.getRegionId() == null ? docData.getString(LeadMaterialParser.REGION)
				: materialVo.getRegionId();
		try {
			if(RawData.ProductSourceType.STOCK.getValue().toString().equals(regionTemp)){
				((List<Document>) docData.get(LeadMaterialParser.STOCKS)).get(0).append("source", regionTemp);
			}else{
				((List<Document>) docData.get(LeadMaterialParser.STOCKS)).get(0).append("sourceId", regionTemp);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		

		docData.append("quickFindKey", quickFindKey);
		docData.append("vendorId", materialVo.getVendorId());
		docData.append("vendorName", materialVo.getVendorName());
		if(null == docData.get("skuId")){
			docData.append("skuId", parser.getSkuId(docData.getString(LeadMaterialParser.MANUFACTURER_PART_NUMBER),
			docData.getString(LeadMaterialParser.PACKAGING)));
		}
		docData.append("lineNo", lineNo);
		docData.append("processId", materialVo.getDocId());
		docData.append("cantCreateStand", true);// 不能創建spu數據
		docData.append("partStatus", "Draft");// 状态
	}
	
	/**
	 * 处理future的定时任务 <br>
	 * 1:下载future存储在FTP的ZIP文件 <br>
	 * 2:获取future存储在mongodb的ProductTemplater <br>
	 * 3:读取下载的ZIP内容,转成List<String[]><br>
	 * 4:记录Doc在mysql的原始记录信息<br>
	 * 5:把解析的数据List<String[]>通过模版转换为RawData,把RawData写入mysql,并且发送数据整理MQ<br>
	 * 6:删除文件
	 * 
	 * @param docId==processId
	 * @since 2016年12月13日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void processingJob(MaterialVo materialVo){
		try {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
			logger.info("{}Job开始--文件下载",materialVo.getVendorId());
			long time1 = System.currentTimeMillis();
			String fileName = ftpAccountConfig.downloadFtpFile(materialVo.getVendorId(), materialVo.getDocId(), materialVo.getFtpFileName());
			File attFile = new File(fileName);//取得文件
			if(0==attFile.length()){
				throw new SystemException("文件内容为空");
			}
			long time2 = System.currentTimeMillis();
			logger.info("{}Job下载文件耗时:{}" ,materialVo.getVendorId(),time2 - time1);
			ProductTemplate proTemplate = productTemplateManager.geTemplate(materialVo.getVendorId(),materialVo.getRegionId());// 获取模板
			long time3 = System.currentTimeMillis();
			logger.info("{}Job加载模板耗时:{}" ,materialVo.getVendorId(),time3 - time2);
			//如果是rs供应商走定制的方法
			if(rs.equals(materialVo.getVendorId())){
				int datasSize = readFtpFile(proTemplate, materialVo, fileName);
				updateDocSize(materialVo.getDocId(), datasSize);
				long time4 = System.currentTimeMillis();
				logger.info("{}Job读取文件完成，总共读取到{}条数据.耗时:{}",materialVo.getVendorId(),datasSize,time4 - time3);
				long time5 = System.currentTimeMillis();
				logger.info("{}Job修改Doc记录耗时:{}" ,materialVo.getVendorId(),time5 - time4);
				long time6 = System.currentTimeMillis();
				logger.info("{}Job文件解析耗时:{}" ,materialVo.getVendorId(),time6 - time5);
			}else{
				String countCacheListTotalKey = String.format(MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY,materialVo.getDocId());
				Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
				int minLineNo = 0;
				boolean isCancel = false;
				String isLastLine = "false";
				Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
				Map<String, Integer> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
				Map<String,String> facilityMap = new HashMap<>();
				if (null == materialVo.getRegionId()) {
					FacilityClient client = shipmentClientBuilder.facilityResource();
					client.getFacilityList(materialVo.getVendorId()).stream().forEach(fac->facilityMap.put(StringUtils.isNotBlank(fac.getFacilityNameAlia())?fac.getFacilityNameAlia():fac.getFacilityName(),fac.getId()));
					logger.info("Vendor Facility-end:{}",facilityMap);
				}
				DocumentLogVo documentLogVo = new DocumentLogVo();
				documentLogVo.setCountCacheList(0);
				documentLogVo.setIsCancel(isCancel);
				int dataSize = 0;
				try{
					dataSize = getFileDatasByFileName(fileName, proTemplate,materialVo,minLineNo,quickFindKeyMap,brandMap,facilityMap,documentLogVo,isLastLine);
					updateDocSize(materialVo.getDocId(), dataSize);
				}catch (Exception e){
					Integer countCacheList = materialCountCacheOps.get(countCacheListTotalKey, countCacheListTotalKey);
					if (null == countCacheList || countCacheList == 0) {
						// 出现异常,并且一个数据也没有发送MQ,直接标记失败
						documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
								DocumentLog.getSizeLengthMsg(e));
						msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
					} else {
						materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
								DocumentStatus.DOC_PRO_PART_SUCCESS);// 标记文件部分处理失败,放弃后续处理
					}
					logger.error(e.getMessage(),e);
				}
				if (!isCancelImport(materialVo.getDocId())) {
					materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
							DocumentStatus.DOC_PRO_SUCCESS);// 标记文件处理完成
				}
				long time4 = System.currentTimeMillis();
				logger.info("读取文件完成，总共读取到:" +dataSize + "条数据");
				logger.info("读取文件耗时:" + (time4 - time3));
				long time5 = System.currentTimeMillis();
				logger.info("修改Doc记录耗时:" + (time5 - time4));
				//writeDataToMongodb(datas, proTemplate, materialVo);
				long time6 = System.currentTimeMillis();
				logger.info("文件解析耗时:{}" + (time6 - time5));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,DocumentLog.getSizeLengthMsg(e));
		}
	}
	
	/**
	 * 读取下载的ZIP内容,转成List<String[]>(步骤3)
	 * @param fileName
	 * @param proTemplate
	 * @return
	 * @throws SystemException
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private int getFileDatasByFileName(String fileUrl, ProductTemplate proTemplate,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException {
		
		if("zip".equalsIgnoreCase(FileUtils.getPrefix(fileUrl))){
			return getFutureZipdatas(fileUrl , proTemplate, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);
		}
		return readExcelDatas(fileUrl, proTemplate, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);
	}

	
	/**
	 * 读取下载的ZIP内容,转成List<String[]>(步骤3)
	 * @param fileName
	 * @param proTemplate
	 * @return
	 * @throws SystemException
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private int getFutureZipdatas(String fileName, ProductTemplate proTemplate,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new SystemException("futureZIP文件解析,文件" + fileName + "不存在");
		}
		InputStream zeIn = null;
		// 读取ZIP文件里面的第一个文件
		try (ZipFile zf = new ZipFile(file);
				FileInputStream fileIn = new FileInputStream(file);
				InputStream in = new BufferedInputStream(fileIn);
				ZipInputStream zin = new ZipInputStream(in);) {
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				zeIn = zf.getInputStream(ze);
				return readFutureZipCsvFile(zeIn, proTemplate, materialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);// 读取数据
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(zeIn);
		}
		return Collections.emptyList().size();
	}

	/**
	 * 读取ZIP下的CSV文件
	 * @param fileStream
	 * @param proTemplate
	 * @return
	 * @throws SystemException
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	int readFutureZipCsvFile(InputStream fileStream, ProductTemplate proTemplate,MaterialManager materialManager,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException {
		LeadMaterialExcelReader reader = new LeadMaterialExcelReader();
		return reader.readByFile(fileStream, proTemplate, materialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);// 读取的数据
	}
	
	/**
	 * 删除指定文件
	 * 
	 * @param fileName
	 * @since 2016年11月15日
	 * @author tongkun@yikuyi.com
	 */
	/*public boolean deleteFile(String fileName) {
		boolean result = false;
		try {
			File file = new File(leadMaterialFilePath + File.separator + fileName);
			if (file.exists()) {
				result = file.delete();
			}
		} catch (Exception e) {
			logger.error("delete file " + fileName + " fial;", e);
		}
		return result;
	}*/

	/**
	 * 用来处理文件上传的数据同步索引
	 * 
	 * @param voList(voList如果为Null表示create_product已经处理,但是处理失败)
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void syncElasticsearchProduct(MaterialVo materialVo) {
		List<ProductVo> voList = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			voList = materialVo.getMsg();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
//			logger.error(materialVo.getMsg());
			syncResultProcessing(materialVo.getDocId(), materialVo.getSize());
			return;
		}

		long now = System.currentTimeMillis();
		if(null == voList){
			return;
		}
		String processId = voList.get(0).getProcessId();
		if (StringUtils.isBlank(processId)) {
			throw new SystemException("SyncElasticsearchProduct processId is null");
		}
		List<ProductVo> voToDocErrorList = new ArrayList<>();// 用来记录vo转换document失败记录
		Map<String, Document> docMap = new HashMap<>(voList.size());
		try {
			voList.stream().forEach(vo -> voToDcoument(vo, mapper, voToDocErrorList, docMap));
			List<Document> errorDocList = inventorySearchManager.updateBatch(docMap);
			
			// 如果其中有失败数据,更新log日志
			if (null != errorDocList && !errorDocList.isEmpty()) {
				documentLogManager.updateDocLogsStatusByDoc(errorDocList);
			}
			// 如果其中有失败数据,更新log日志
			if (!voToDocErrorList.isEmpty()) {
				documentLogManager.updateDocLogsStatusByVo(voToDocErrorList,
						"SyncElasticsearchProduct ProductVo to Document error");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 如果异常,整个批次置为失败,并且失败原因一样
			documentLogManager.updateDocLogsStatusByVo(voList, e.getMessage());
		}
		/*syncResultProcessing(processId, materialVo.getSize());*/
		long newTime = System.currentTimeMillis();
		logger.info("syncElasticsearchProduct同步搜索引擎 processId:" + processId + " 大小：" + voList.size() + " 现在时间:"
				+ newTime + " 总共花费：" + (newTime - now));
	}
	
	/**
	 * 用来处理文件上传的数据同步索引
	 * 
	 * @param voList(voList如果为Null表示create_product已经处理,但是处理失败)
	 * @since 2016年12月14日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void syncElasticsearchProductUpdate(MaterialVo materialVo) {
		List<ProductVo> voList = null;
		try {
			voList = materialVo.getMsg();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		}
		if(CollectionUtils.isEmpty(voList)){
			logger.error("传人es同步数据，消息体为Null");
			return;
		}
		
		Map<String, String> docMap = new HashMap<>(voList.size());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			voList.stream().forEach(vo -> productVoToString(vo, docMap , objectMapper));
			inventorySearchManager.updateBatchByString(docMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 如果异常,整个批次置为失败,并且失败原因一样
		}
	}
	
	public void productVoToString(ProductVo vo ,Map<String,String> docMap , ObjectMapper objectMapper){
		try {
			ProductEsVo esVo = new ProductEsVo();
			BeanUtils.copyProperties(vo,esVo);
			ProductStand spu = esVo.getSpu();
			if(null != spu){
				spu.setManufacturerPartNumberNoIndex(spu.getManufacturerPartNumber());
				spu.setIdBack(spu.getId());
			}
			EssyncHelp.addSortLevel(esVo);
			EssyncHelp.analysisDocToElasticsearc(esVo);
			docMap.put(esVo.getId(), objectMapper.writeValueAsString(esVo).replaceAll("http:", ""));
		} catch (Exception e) {
			//异常，数据丢失错误记录
			logger.error(e.getMessage());
		}
	}
	

	/**
	 * 如果已经取消,返回true,否则返回flase
	 * 
	 * @param docId
	 * @return
	 * @since 2017年1月8日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public boolean createProductCancelTheJudgment(List<RawData> rawDatas) {
		String docId = rawDatas.get(0).getProcessId();
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		DocumentStatus documentStatus = materialImportCache.get(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, docId),
				DocumentStatus.class);// 获取状态

		if (DocumentStatus.DOC_CANCEL == documentStatus) {
			try {
				documentLogManager.updateDocLogsStatusByRaw(rawDatas, "取消导入");
				syncResultProcessing(docId, rawDatas.size());
				return true;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 用来处理文件上传的数据监控数据
	 * TODO 临时修改，有待优化
	 * @param processId
	 * @since 2017年1月17日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void syncResultProcessing(String processId, int size) {
		logger.info("syncResultProcessing start");
		String key = String.format(MATERIAL_IMPORT_CACHE_KEY, processId);
		String lastLineKey = String.format(MATERIAL_LASTLINE_CACHE_KEY, processId);//是否是最后一批数据的key
		try(Jedis jedis = jedisPool.getResource()){
			
			//执行更新，成功或异常都不再重试。如果因为线程阻塞则需要再次重试。重试达到最大次数则不再重试
			boolean success = false;
			for(int tryNumber = 0;tryNumber<5;tryNumber++){
				boolean processResult = false;//本次执行是否成功
				boolean breakFlag = false;//本次执行是否需要跳出循环
				//确定执行异常则立刻跳出循环
				try {
					// 如果当前批次为最后一批,更新文档最后状态,否则更新redis缓存处理次数
					jedis.watch(key);
					Transaction trans = jedis.multi();
					trans.decrBy(key, size);
					List<Object> result = trans.exec();
					processResult = result!=null&&!result.isEmpty();//返回执行是否成功
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					breakFlag = true;//不需要再继续执行了，跳出循环
				}
				//执行成功随即跳出循环
				if(processResult){
					success = true;//执行成功，则不需要继续执行
					breakFlag = true;//需要跳出循环
				}
				//如果需要跳出，则break
				if(breakFlag){
					break;
				}
				//如果是线程阻塞，则不会异常，也不会成功，此时需要等待15毫秒后再重试
				try {
					logger.debug("其它线程正在更新，等待重新执行扣减");
					Thread.sleep(15);
				} catch (InterruptedException e) {
					logger.error("扣减等待时间被打断",e);
					throw e;
				}
			}
			
			if(success){
				Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
				Integer disposeCount = Integer.parseInt(jedis.get(key));
				String isLastLine = jedis.get(lastLineKey);
				DocumentStatus docStatus = materialImportCache.get(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, processId), DocumentStatus.class);
				if(null == docStatus){//状态丢失，默认垃圾数据，结束处理
					return;
				}
				logger.info("disposeCount为：{}" + ";processId为：{},docStatus为：{},isLastLine为：{}", disposeCount, processId, docStatus,isLastLine);
				if (disposeCount <=0 && docStatus.equals(DocumentStatus.DOC_PRO_SUCCESS) && "true".equalsIgnoreCase(isLastLine)) {
					logger.info("disposeCount满足条件，准备发送uploadFileProductTopicName的MQ");
					documentManager.updateDocStatus(processId, documentLogManager.findDocStatusByDocId(processId),null);
					jedis.del(String.format(MATERIAL_IMPORT_CACHE_KEY, processId));
					jedis.del(lastLineKey);
					materialImportCache.evict(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, processId));// 清除缓存
					msgSender.sendMsg(uploadFileProductTopicName, processId, null);// 发送上传日志文件到阿里云MQ
				} 
			}
		} catch (Exception e) {
			logger.error("{}--syncResultProcessing size error,{}", processId, e.getMessage(), e);
		}
		logger.info("syncResultProcessing end");
	}

	/**
	 * java8 lambda过程中不能有抛出异常方法,单独出来捕获
	 * 
	 * @param vo
	 * @param mapper
	 * @param voToDocErrorList
	 * @return
	 * @since 2016年12月15日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private void voToDcoument(ProductVo vo, ObjectMapper mapper, List<ProductVo> voToDocErrorList,
			Map<String, Document> docMap) {
		try {
			String jsonString = mapper.writeValueAsString(vo);
			docMap.put(vo.getId(), Document.parse(jsonString));
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			vo.setErrorMsg(e.getMessage());
			voToDocErrorList.add(vo);
		}
	}

	/**
	 * 全量同步mongodb数据到搜索引擎 <br>
	 * 自动创建index,切换index,返回上个index,和当前index
	 * 
	 * @param processId
	 * @since 2016年12月9日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public List<String> syncElasticsearchProduct(String vendorId) {
		MongoClientURI mongoClientURI = new MongoClientURI(mongoUri);
		try (MongoClient mongoClient = new MongoClient(mongoClientURI);) {
			
			//String indexUrl = apiSearchUrl + "/index/PRODUCT";
			String newIndex = esIndexManager.createIndex(IndexProcesserFactory.createIndexProcesser(IndexFactoryType.PRODUCT));
			MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoClientURI.getDatabase());
			//获取所有大类id
			MongoCollection<Document> cateCol = mongoDatabase.getCollection("product_category");
			FindIterable<Document> cates = cateCol.find(new Document("cateLevel",1)).projection(new Document("_id",1));
			List<Integer> cateIds = new ArrayList<>();
			for(Document d:cates){
				cateIds.add(d.getInteger("_id"));
			}
			//循环调用同步搜索引擎（异步
			List<Future<String>> results = new ArrayList<>(); 
			for(Integer cateId:cateIds){
				results.add(materialAsyncManager.syncElasticsearchByCategory(mongoClient, mongoClientURI.getDatabase(), newIndex, cateId,vendorId));
			}
			//等待调用结果
			boolean isDone = false;
			while(isDone==false){
				isDone = true;
				for(Future<String> result:results){
					if(result.isDone()==false)
						isDone = false;
				}
				Thread.sleep(1000);
			}
			
			String[] oldIndex = esIndexManager.getIndexByAliases("PRODUCT");
			String oldIndexStr = Arrays.toString(oldIndex).substring(1);
			oldIndexStr = oldIndexStr.substring(0, oldIndexStr.length() - 1);
			
			//String addUrl = apiSearchUrl + "/index/" + newIndex + "/product";
			esIndexManager.addAlias(newIndex, "product");// 新增索引创建别名
			Thread.sleep(1000);
			String[] strOldIndexStr = null;
			if(StringUtils.isNotBlank(oldIndexStr)){
				strOldIndexStr = oldIndexStr.split(",");
			}
			if(null == strOldIndexStr){
				strOldIndexStr = new String[0];
			}
			String[] strProduct = new String[1];
			strProduct[0] = "product";
			//String removeUrl = apiSearchUrl + "/index?index=" + oldIndexStr + "&aliases=product";
			esIndexManager.removeAlias(strOldIndexStr, strProduct);
			//restTemplate.delete(removeUrl);// 老的索引删除别名
			Thread.sleep(1000);
			Long esCount = esIndexManager.getIndexTypeCount(newIndex, "product");
			List<String> rstList = new ArrayList<>();
			rstList.add(newIndex);
			rstList.add(oldIndexStr);
			rstList.add(String.valueOf(esCount));
			return rstList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	/**
	 * 停止文件导入
	 * 
	 * @param docId
	 * @since 2017年1月18日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void cancelImport(String docId) {
		com.yikuyi.document.model.Document doc = new com.yikuyi.document.model.Document();
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		if(null == materialImportCache.get(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, docId))){
			doc.setStatusId(DocumentStatus.DOC_CANCEL);
		}
		materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, docId), DocumentStatus.DOC_CANCEL);// 标记文件取消

		doc.setId(docId);
		doc.setIsCancel(CancelStatus.YES);
		documentManager.updateDoc(doc);

	}

	/**
	 * 是否已经取消文件上传
	 * 
	 * @param docId
	 * @return
	 * @since 2017年1月18日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public boolean isCancelImport(String docId) {
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		return DocumentStatus.DOC_CANCEL == materialImportCache
				.get(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, docId), DocumentStatus.class);
	}
	
	/**
	 * 解析导入文件
	 * @param materialVo
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public void importsParseImportFile(@RequestBody MaterialVo materialVo) {
		// 判断文件是否取消上传
		if (isCancelImport(materialVo.getDocId())) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_CANCEL, null);
		} else {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
			try {
				parseImportFile(materialVo);
			} catch (Exception e) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						DocumentLog.getSizeLengthMsg(e));
			}
		}
		// materialManager.deleteFile(fileName);//TODO SZ文件解析完成,删除
	}
	
	public int readFtpFile(ProductTemplate proTemplate, MaterialVo materialVo,String fileName) throws IOException{
		Map<String, Integer> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
		int total = 0;
		Integer countCacheListTotal = 0;
		boolean isCancel = false;
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
		Map<String,String> facilityMap = new HashMap<>();
		if (null == materialVo.getRegionId()) {
			logger.info("Vendor Facility-start:{}",materialVo.getVendorId());
			FacilityClient client = shipmentClientBuilder.facilityResource();
			client.getFacilityList(materialVo.getVendorId()).stream().forEach(fac->facilityMap.put(StringUtils.isNotBlank(fac.getFacilityNameAlia())?fac.getFacilityNameAlia():fac.getFacilityName(),fac.getId()));
			logger.info("Vendor Facility-end:{}",facilityMap);
		}
		 DocumentLogVo documentLogVo = new DocumentLogVo();
		 documentLogVo.setCountCacheList(0);
		 documentLogVo.setIsCancel(isCancel);
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
		materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
				DocumentStatus.DOC_IN_PROCESS);// 标记文件正在处理
		
		String separator = ";";
		BufferedReader reader = null;
		InputStreamReader is = null;
		ZipFile zf = null;
		String encode = null== proTemplate.getEncode()?"UTF-8":proTemplate.getEncode();//编码
		
		try (FileInputStream fileIn = new FileInputStream(fileName);
				InputStream in = new BufferedInputStream(fileIn);
				ZipInputStream zin = new ZipInputStream(in);  
				){
			int bufferSize = 20 * 1024 * 1024;//设读取文件的缓存为20MB   
			if("zip".equalsIgnoreCase(FileUtils.getPrefix(fileName))){
				 zf = new ZipFile(fileName);
		         ZipEntry ze;  
		         while ((ze = zin.getNextEntry()) != null) {
		         is = new InputStreamReader(zf.getInputStream(ze),encode);
		         reader = new BufferedReader(is,bufferSize);  
		         }
			}else{
				reader = new BufferedReader(new InputStreamReader(in,encode),bufferSize);
			}
		    String str = null;
		    List<String[]> datas = new ArrayList<>();
		    List<String[]> templateDatas = new ArrayList<>();
		    int batch = 1000;
		    int index = 0;
		    int count = 0;	    
		    int minLineNo = 0;
		    boolean flag = true;
		    String isLastLine = "false";
		    while (true) {
		    	String[] strArr = null;
		    	str = reader.readLine();
		    	if(StringUtils.isNotBlank(str)){
		    	  	str = str.replaceAll("\"","");  
			    	//str = str.replaceAll("<br>", "\n"); 
			    	str = str.replaceAll(",", ""); 
			        strArr = str.split(separator);
		    	}
		        	if(index == batch || StringUtils.isBlank(str)){
		        		if(count>0){
		        			minLineNo = batch*count;
		        		}
		        		if(StringUtils.isBlank(str)){
		        			isLastLine = "true";
		        		}
		        		writeDataToMongodbByFtp(datas, proTemplate, materialVo,minLineNo,quickFindKeyMap,brandMap,facilityMap,documentLogVo,isLastLine);
		        		index = 0;
		        		datas.clear();
		        		count++;
		        		countCacheListTotal += documentLogVo.getCountCacheList();
		        		if (documentLogVo.getIsCancel()) {// 每次提交的时候判断是否已经取消
							break;
						}
		        	}
		        	if(StringUtils.isBlank(str)){
		        		break;
		        	}
		        	if(flag){
		        		templateDatas.add(strArr);
		 			    Reader r = new Reader(proTemplate, templateDatas, materialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);
		 			    List<String> list = java.util.Arrays.asList(strArr);
		 	        	proTemplate = r.getTitlesNew(list);
		 	        	updateDocInfo(materialVo.getDocId(), proTemplate.getShowTitles());
		 	        	flag = false;
		        	}else{
		        		datas.add(strArr);
		        		index++;
		  		        total++;
		        	}
		    }
		 // 如果一次计数都没产生,说明所有数据都校验失败,否则代表文件解析完成,改状态为锁定
			if (countCacheListTotal == 0 && !isCancelImport(materialVo.getDocId())) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						"全部数据校验失败,没有发送Create_Product MQ");
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else if (countCacheListTotal == 0 && isCancelImport(materialVo.getDocId())) {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_CANCEL, null);
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_LOCKED, null);
			}
			if (!documentLogVo.getIsCancel()) {
				materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
						DocumentStatus.DOC_PRO_SUCCESS);// 标记文件处理完成
			}
		} catch (Exception e) {
			if (countCacheListTotal == 0) {
				// 出现异常,并且一个数据也没有发送MQ,直接标记失败
				documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
						DocumentLog.getSizeLengthMsg(e));
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
						DocumentStatus.DOC_PRO_PART_SUCCESS);// 标记文件部分处理失败,放弃后续处理
			}
			throw new SystemException(e.getMessage(), e);
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(zf);
		}
		return total;    
	}
	
	public void writeDataToMongodbByFtp(List<String[]> datas, ProductTemplate proTemplate, MaterialVo materialVo,int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) {
		List<DocumentLog> batchInsertList = new ArrayList<>();// 初始化批量日志llist
		ArrayList<Document> arrangementDatas = new ArrayList<>();// 初始化批量数据清理list
		Integer countCacheList = 0;
		boolean isCancel = false;
		String key = String.format(MATERIAL_IMPORT_CACHE_KEY, materialVo.getDocId());
		try(Jedis jedis = jedisPool.getResource()){
			if("true".equalsIgnoreCase(isLastLine)){
				String lastLineKey = String.format(MATERIAL_LASTLINE_CACHE_KEY, materialVo.getDocId());
				jedis.set(lastLineKey, isLastLine);//将是否是最后一批数据的缓存放进去
				if (!isCancelImport(materialVo.getDocId())) {
					Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MATERIAL_IMPORT_CACHE_NAME_KEY);
					materialImportCache.put(String.format(MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
							DocumentStatus.DOC_PRO_SUCCESS);// 标记文件处理完成
				}
			} 
			for (int lineNo = 0; lineNo < datas.size(); lineNo++,minLineNo++) {
				Document docData = getDocData(materialVo, minLineNo, datas.get(lineNo), quickFindKeyMap, proTemplate,
						batchInsertList, brandMap,facilityMap);
				
				// 数据足够或者数据结束,记录日志
				if (batchInsertList.size() >= BATCH_SIZE || lineNo == datas.size() - 1) {
					documentLogManager.insertDocs(batchInsertList);
					batchInsertList.clear();
				}
				// 如果解析文件返回为NULL,表示数据校验不通过,直接下一个
				if (null == docData && lineNo != datas.size() - 1) {
					continue;
				}
				// 把当前正式对象放入list,便于create_product的MQ发送
				if(null != docData){
					arrangementDatas.add(docData);
				}
				if (arrangementDatas.size() == BATCH_SIZE || lineNo == datas.size() - 1) {// 批量操作
					if(arrangementDatas.isEmpty()){
						continue;
					}
					countCacheList += arrangementDatas.size();// 增加计数
					boolean success = false;//是否执行成功
					for(int tryNumber = 0;tryNumber<5;tryNumber++){
						boolean processResult = false;//本次执行是否成功
						boolean breakFlag = false;//本次执行是否需要跳出循环
						//确定执行异常则立刻跳出循环
						try {
							// 如果当前批次为最后一批,更新文档最后状态,否则更新redis缓存处理次数
							jedis.watch(key);
							Transaction trans = jedis.multi();
							trans.incrBy(key, arrangementDatas.size());
							List<Object> result = trans.exec();
							processResult = result!=null&&!result.isEmpty();//返回执行是否成功
							//logger.info("上传商品MQ分解：{} key:{} 结果:{} 分解mq的序号：{}",arrangementDatas.size(),key,(result!=null&&!result.isEmpty())?result.get(0):"blank",testno);
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
							breakFlag = true;//不需要再继续执行了，跳出循环
						}
						//执行成功随即跳出循环
						if(processResult){
							success = true;//执行成功，则不需要继续执行
							breakFlag = true;//需要跳出循环
						}
						//如果需要跳出，则break
						if(breakFlag){
							break;
						}
						//如果是线程阻塞，则不会异常，也不会成功，此时需要等待15毫秒后再重试
						try {
							logger.debug("其它线程正在更新，等待重新执行扣减");
							Thread.sleep(15);
						} catch (InterruptedException e) {
							logger.error("扣减等待时间被打断",e);
							throw e;
						}
					}
					jedis.expire(key, 86400);
					logger.debug("上传附件消息体，条数：{}", arrangementDatas.size());
					if(null != materialVo.getType() && MaterialVoType.FILE_UPLOAD_JOB.toString().equals(materialVo.getType().toString())){
						msgSender.sendMsg(createProductTopicName, arrangementDatas, null);// 发送数据清理MQ
					}else{
						msgSender.sendMsg(quickCreateProductTopicName, arrangementDatas, null);// 发送数据清理MQ
					}
					arrangementDatas.clear();

					isCancel = isCancelImport(materialVo.getDocId());
					documentLogVo.setIsCancel(isCancel);
					if (isCancel) {// 每次提交的时候判断是否已经取消
						break;
					}
				}
			}
			documentLogVo.setCountCacheList(countCacheList);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	private void uploadOssFile(List<List<String>> dataList,String docId,int page,String template,File excelFile,CSVPrinter printer,FileWriter out){
		String aliyunUrl = exrpotCsv(dataList, excelFile, template, page,printer,out);
		logger.info("生成的输出日志：{}  docId:{}",aliyunUrl,docId);
		if(CollectionUtils.isEmpty(dataList)){
			com.yikuyi.document.model.Document doc = new com.yikuyi.document.model.Document();
			doc.setId(docId);
			doc.setLogLocation(aliyunUrl);
			documentManager.updateDoc(doc);//将阿里云的文件地址更新到数据库
			documentLogDao.deleteHistoryLogById(docId);//删除本批次的日志记录
		}
	}
	
	private String exrpotCsv(List<List<String>> rowDataList,File excelFile,String template,int page,CSVPrinter printer,FileWriter out){
		try{
			 List<List<String>> datas = Lists.newArrayList();
			 List<List<String>> roDataSNew = new ArrayList<>();
			 List<String> templateList = Arrays.asList(template.split("qqq;;;"));
			 List<String> templateListAll = new ArrayList<>();
			 templateListAll.addAll(templateList);
			 templateListAll.add("异常说明");
			 int length = templateListAll.size();//标题长度
			if(1==page){
		        for(List<String> list : rowDataList){
					String[] strArr = list.get(0).split("qqq;;;");
					strArr = Arrays.copyOf(strArr, length);
					strArr[length-1] = list.get(1);//异常说明
					List<String> strList = Arrays.asList(strArr);//原始数据
					roDataSNew.add(strList);
				}
		        datas.add(templateListAll);//标题
			}else{
				  for(List<String> list : rowDataList){
						String[] strArr = list.get(0).split("qqq;;;");
						strArr = Arrays.copyOf(strArr, length);
						strArr[length-1] = list.get(1);//异常说明
						List<String> strList = Arrays.asList(strArr);//原始数据
						roDataSNew.add(strList);
					}
			}
		
	        datas.addAll(roDataSNew);//数据
	        writeCsv(excelFile, datas,printer,out);
            if(CollectionUtils.isEmpty(rowDataList)){
            	return productStandManger.uploadFileAliyun(excelFile, "material.logs");//上传文件到阿里云并返回文件地址
            }
		}catch(Exception e){
			logger.error("写入csv异常，{}",e);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(printer);
			org.apache.commons.io.FileUtils.deleteQuietly(excelFile);
		}
		return null;
	}
	
	public void uploadFile(String docId){
		String documentTitleKey = String.format(MATERIAL_DOCUMENT_TITLE_CACHE_KEY,docId);
		com.yikuyi.document.model.Document doc = documentDao.findById(docId);
		if(null == doc){
			return;
		}
		String template = "";
		List<DocumentTitle> documentTitleResults = documentTitleOps.get(documentTitleKey, documentTitleKey);
		if(CollectionUtils.isEmpty(documentTitleResults)){
			logger.info("生成日志文件，标题缓存不存在 docId:{}",docId);
			return;
		}
		logger.info("文件上传生成日志文件，documentTitleResults长度:{} docId:{}",documentTitleResults.size(),docId);
		template = documentTitleResults.get(0).getOriginalTitle().replaceAll("\\,", "");
		documentTitleOps.delete(documentTitleKey, documentTitleKey);
		int page = 1;
		int pageSize = 1000;
		List<DocumentLog> documentLogs;
		String fileName = "materialLog"+docId+".csv";
		File excelFile = org.apache.commons.io.FileUtils.getFile(fileName);
		String LINE_SEPARATOR = "\n";
		CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(LINE_SEPARATOR);
		FileWriter out = null;
		CSVPrinter printer = null;
		if(1==page){
			byte[] e = new byte[]{-17, -69, -65};
			try {
				out = new FileWriter(excelFile);
				out.write(new String(e));
				printer = new CSVPrinter(out, csvFormat);
			} catch (IOException e1) {
				logger.error("创建IO流错误,错误消息是 ：{}",e1.getMessage());
				throw new SystemException(e1);
			}
		}

		logger.info("开始查询文件导入生成日志  docId:{}",docId);
		do{
			RowBounds rowBouds = new RowBounds((page - 1) * pageSize, pageSize);
			documentLogs = documentLogDao.findLogsByPage(docId, null, DocumentLogStatus.FAIL, rowBouds);
			if(1==page && CollectionUtils.isEmpty(documentLogs)){
				return;
			}
			List<List<String>> dataList = new ArrayList<>();
			for(DocumentLog log : documentLogs){
				List<String> strArr = new ArrayList<>();
				strArr.add(log.getOriginalData());//原始数据
				strArr.add(log.getComments());//异常说明
				dataList.add(strArr);
			}
			uploadOssFile(dataList,docId,page,template,excelFile,printer,out);//上传日志文件到阿里云
			page++;
		}while(!CollectionUtils.isEmpty(documentLogs));
	}
	
	/**
	 * 根据供应商id和仓库名称匹配仓库
	 * @param vendorId
	 * @param facilityName
	 * @return
	 * @since 2017年11月21日
	 * @author tb.lijing@yikuyi.com
	 */
	public synchronized String findFacilityId(String vendorId,String facilityName,String mpn){
		Map<String,String> facilityMap = new HashMap<>();
		String facilityMapKey = MATERIAL_FACILITY_CACHE_KEY;
		String facilityId = "";
		//如果仓库名称值为"100",直接用vendorId和facilityName拼接仓库id并返回
		if(StringUtils.isNotBlank(facilityName) && RawData.ProductSourceType.STOCK.getValue().toString().equals(facilityName)){
			return vendorId + "-" + facilityName;
		}
		StringBuilder sd = new StringBuilder();
		//仓库缓存的key（供应商id+仓库名称）
		String facilityKey = sd.append(vendorId).append("-").append(facilityName).toString();
		facilityId = aliasFacilityOps.get(facilityMapKey, facilityKey);//获取缓存中的仓库id
		if(StringUtils.isBlank(facilityId)){
			//根据供应商ID查找对应的仓库信息
			FacilityClient client = shipmentClientBuilder.facilityResource();
			List<Facility> facilityList = client.getFacilityList(vendorId);
			if(!CollectionUtils.isEmpty(facilityList)){
				for(Facility fac : facilityList){
					StringBuilder sdNameAlia = new StringBuilder();
					String facilityNameAliaKey = sdNameAlia.append(vendorId).append("-")
							.append(StringUtils.isNotBlank(fac.getFacilityNameAlia())?fac.getFacilityNameAlia():fac.getFacilityName()).toString();
					facilityMap.put(facilityNameAliaKey, fac.getId());
				}
			}
			aliasFacilityOps.putAll(facilityMapKey, facilityMap);//将map放入缓存中
			aliasFacilityOps.getOperations().expire(facilityMapKey, 1, TimeUnit.HOURS);//设置缓存失效时间为1小时
			//如果匹配上仓库名称，返回仓库id
			if(facilityMap.containsKey(facilityKey)){
				facilityId = facilityMap.get(facilityKey);
			}else{
				throw new SystemException("供应商id="+vendorId+",型号="+mpn+"的仓库名=【"+facilityName+"】不存在");
			}
		}
		return facilityId;
	}
	
	private static boolean writeCsv(File file, List<List<String>> dataList,CSVPrinter printer,FileWriter out) {
		boolean isSucess = false;

		try {
			if (!CollectionUtils.isEmpty(dataList)) {
				printer.printRecords(dataList);
			}
			isSucess = true;
		} catch (Exception arg10) {
			isSucess = false;
		} finally {
			if(CollectionUtils.isEmpty(dataList)){
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(printer);
			}
		}
		return isSucess;
	}
	
	/**
	 * 修改文档状态等
	 */
	public void handleDocStatus(int countCacheListTotal,MaterialVo materialVo){
		String countCacheListTotalKey = String.format(MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY,materialVo.getDocId());
		materialCountCacheOps.put(countCacheListTotalKey, countCacheListTotalKey, countCacheListTotal);
		materialCountCacheOps.getOperations().expire(countCacheListTotalKey, 1, TimeUnit.DAYS);//设置缓存失效时间为1天
	 // 如果一次计数都没产生,说明所有数据都校验失败,否则代表文件解析完成,改状态为锁定
		if (countCacheListTotal == 0 && !isCancelImport(materialVo.getDocId())) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
					"全部数据校验失败,没有发送Create_Product MQ");
			msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
		} else if (countCacheListTotal == 0 && isCancelImport(materialVo.getDocId())) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_CANCEL, null);
			msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
		} else {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_LOCKED, null);
		}
	}
	
}