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
package com.yikuyi.product.basicmaterial.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.vo.DocumentLogVo;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentTitle;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.document.bll.DocumentLogManager;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.document.dao.DocumentTitleDao;
import com.yikuyi.product.material.bll.LeadMaterialParser;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.template.bll.ProductTemplateManager;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.SystemException;
import com.ykyframework.mqservice.sender.MsgSender;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * 基础物料处理业务类
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional
public class BasicMaterialManager {

	private static final Logger logger = LoggerFactory.getLogger(BasicMaterialManager.class);

	private static final int BATCH_SIZE = 100;

	private static final String BASIS_TEMPLATE_ID = "basisTemplateId";

	/**
	 * 导入库存上传文件路径
	 */
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;
	
	@Autowired
	private BasicMaterialManager basicMaterialManager;

	@Autowired
	private MsgSender msgSender;

	@Autowired
	private MaterialManager materialManager;

	@Autowired
	private ProductTemplateManager productTemplateManager;

	@Autowired
	private DocumentManager documentManager;
	
	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private DocumentTitleDao documentTitleDao;

	@Autowired
	private RedisCacheManager redisCacheManagerNoTransaction;

	@Autowired
	private LeadBasicMaterialParser leadBasicMaterialParser;

	@Autowired
	private BrandManager brandManager;

	@Autowired
	private DocumentLogManager documentLogManager;
	
	@Value("${mqProduceConfig.createProductSub.topicName}")
	private String createProductTopicName;
	
	@Value("${mqConsumeConfig.uploadFileProduct.topicName}")
	private String uploadFileProductTopicName;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<DocumentTitle>> documentTitleOps;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> materialCountCacheOps;
	
	//是否是最后一批数据缓存
	public static final String MATERIAL_LASTLINE_CACHE_KEY = "materialLastLineCache_list_%s";
	
	//用来做DocumentTitleCache的key
	public static final String MATERIAL_DOCUMENT_TITLE_CACHE_KEY = "materialDocumentTitle_cache_%s";

	/**
	 * 解析正常基础物料上传的文件 <br>
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
		Map<String, String> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
		String countCacheListTotalKey = String.format(MaterialManager.MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY,
				materialVo.getDocId());
		Cache materialImportCache = redisCacheManagerNoTransaction
				.getCache(MaterialManager.MATERIAL_IMPORT_CACHE_NAME_KEY);
		try {
			logger.info("基础物料文件解析开始:" + materialVo.getFileUrl());
			long time1 = System.currentTimeMillis();
			fileName = materialManager.fileDownload(materialVo.getFileUrl(), materialVo.getOriFileName(), materialVo.getDocId());
			long time2 = System.currentTimeMillis();
			logger.info("下载文件耗时:" + (time2 - time1));
			ProductTemplate proTemplate = productTemplateManager.geTemplate(BASIS_TEMPLATE_ID);// 获取模板
			long time3 = System.currentTimeMillis();
			logger.info("加载模板耗时:" + (time3 - time2));
			Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
			DocumentLogVo documentLogVo = new DocumentLogVo();
			documentLogVo.setCountCacheList(0);
			documentLogVo.setIsCancel(isCancel);
			LeadBasicMaterialExcelReader reader = new LeadBasicMaterialExcelReader();
			materialImportCache.put(
					String.format(MaterialManager.MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
					DocumentStatus.DOC_IN_PROCESS);// 标记文件正在处理
			reader.readByFile(leadMaterialFilePath + File.separator + fileName, proTemplate, basicMaterialManager,
					materialVo, minLineNo, quickFindKeyMap, brandMap, documentLogVo, isLastLine);// 读取的数据
			long time4 = System.currentTimeMillis();
			logger.info("读取文件完成，总共读取到:" + reader.getCount() + "条数据");
			logger.info("读取文件耗时:" + (time4 - time3));
			updateDocInfo(materialVo.getDocId(), proTemplate.getMoreSheetShowTitles(), reader.getCount());
			long time5 = System.currentTimeMillis();
			logger.info("修改Doc记录耗时:" + (time5 - time4));
			// writeDataToMongodb(datas, proTemplate, materialVo);
			long time6 = System.currentTimeMillis();
			logger.info("文件解析耗时:{}" + (time6 - time5));
		} catch (Exception e) {
			int countCacheList = null==materialCountCacheOps.get(countCacheListTotalKey, countCacheListTotalKey)?0:materialCountCacheOps.get(countCacheListTotalKey, countCacheListTotalKey).intValue();
			if (countCacheList == 0) {
				// 出现异常,并且一个数据也没有发送MQ,直接标记失败
				msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
			} else {
				materialImportCache.put(
						String.format(MaterialManager.MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
						DocumentStatus.DOC_PRO_PART_SUCCESS);// 标记文件部分处理失败,放弃后续处理
			}
			logger.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		} finally {
			materialCountCacheOps.delete(countCacheListTotalKey, countCacheListTotalKey);
		}
		return fileName;
	}

	/**
	 * 修改Doc记录信息,标题和文件总行数(步骤4)<br>
	 * {@link DocumentManager.parseImportFile}
	 * {@link DocumentManager.processingFutureJob}
	 * 
	 * @param docId
	 * @param title
	 * @param size
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	void updateDocInfo(String docId, Map<String, String[]> title, int size) {
		try {
			com.yikuyi.document.model.Document doc = new com.yikuyi.document.model.Document();
			doc.setId(docId);
			doc.setDataCount(size);
			documentManager.updateDoc(doc);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	void addTitlesToRedis(String docId, Map<String, String[]> title) {
		try {
			List<DocumentTitle> list = new ArrayList<>();
			for (Map.Entry<String, String[]> entry : title.entrySet()) {
				String mapKey = entry.getKey();
				String sheetName = mapKey.split(LeadMaterialParser.VALUE_SEPARATE)[0];
				String sheetIndex = mapKey.split(LeadMaterialParser.VALUE_SEPARATE)[1];
				DocumentTitle docTitle = new DocumentTitle();
				docTitle.setId(docId);
				docTitle.setSheetIndex(Integer.parseInt(sheetIndex));
				docTitle.setSheetName(sheetName);
				docTitle.setOriginalTitle(String.join(LeadMaterialParser.VALUE_SEPARATE, entry.getValue()));
				list.add(docTitle);
			}
			logger.info(docId+"物料插入documentTitle表"+new Date());
			documentTitleDao.insertDocTitles(list);
			String documentTitleKey = String.format(MATERIAL_DOCUMENT_TITLE_CACHE_KEY,docId);
			documentTitleOps.put(documentTitleKey, documentTitleKey, list);
			documentTitleOps.getOperations().expire(documentTitleKey, 1, TimeUnit.DAYS);//设置缓存失效时间为1天
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	public void writeDataToMongodb(Map<String, List<String[]>> datas, ProductTemplate proTemplate,
			MaterialVo materialVo,	int minLineNo,Map<String, String> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			DocumentLogVo documentLogVo,String isLastLine) throws SystemException {
		Cache materialImportCache = redisCacheManagerNoTransaction.getCache(MaterialManager.MATERIAL_IMPORT_CACHE_NAME_KEY);
		ArrayList<RawData> arrangementDatas = new ArrayList<>();// 初始化批量数据清理list
		Integer countCacheList = 0;// 存储发送MQ数量的过程记录的cache
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		List<DocumentLog> batchInsertList = null;
		RawData docData = null;
		Integer sheetIndex = null;
		try(Jedis jedis = jedisPool.getResource()) {
			if("true".equalsIgnoreCase(isLastLine)){
				String lastLineKey = String.format(MATERIAL_LASTLINE_CACHE_KEY, materialVo.getDocId());
				jedis.set(lastLineKey, isLastLine);//将是否是最后一批数据的缓存放进去
			} 
			for (Map.Entry<String, List<String[]>> entry : datas.entrySet()) {
				batchInsertList = new ArrayList<>();// 初始化批量日志llist
				for (int lineNo = 0; lineNo < entry.getValue().size(); lineNo++,minLineNo++) {
					sheetIndex = Integer.parseInt(entry.getKey().split(LeadMaterialParser.VALUE_SEPARATE)[1]);
					docData = getDocData(materialVo, sheetIndex, entry.getKey(), minLineNo, entry.getValue().get(lineNo),
							quickFindKeyMap, proTemplate, batchInsertList, brandMap);
					// 用户Id
					if (null != docData) {
						docData.setUserId(materialVo.getUserId());
						docData.setUserName(materialVo.getUserName());
					}
					// 数据足够或者数据结束,记录日志
					if (batchInsertList.size() >= BATCH_SIZE || lineNo == entry.getValue().size() - 1) {
						documentLogManager.insertDocs(batchInsertList);
						batchInsertList.clear();
					}
					// 如果解析文件返回为NULL,表示数据校验不通过,直接下一个
					if (null == docData && lineNo != entry.getValue().size() - 1) {
						continue;
					}
					// 把当前正式对象放入list,便于create_product的MQ发送
					if (null != docData) {
						arrangementDatas.add(docData);
					}
					if (arrangementDatas.size() == BATCH_SIZE || lineNo == entry.getValue().size() - 1) {// 批量操作
						if (arrangementDatas.isEmpty()) {
							continue;
						}
						countCacheList += arrangementDatas.size();// 增加计数
						materialVo.setSize(arrangementDatas.size());
						int testno = 0;
						String key = String.format(MaterialManager.MATERIAL_IMPORT_CACHE_KEY, materialVo.getDocId());
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
								logger.info("上传物料MQ分解：{} key:{} 结果:{} 分解mq的序号：{}",arrangementDatas.size(),key,(result!=null&&!result.isEmpty())?result.get(0):"blank",testno);
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
						logger.info("上传附件消息体，条数：{}", arrangementDatas.size());
						
						//这个内容千万别打在info以上的
						for(RawData data:arrangementDatas){
							logger.info("上传的物料型号：{}", data.getManufacturerPartNumber());
						}
						msgSender.sendMsg(createProductTopicName,arrangementDatas, null);// 发送数据清理MQ
						arrangementDatas.clear();
					}
				}
			}
			documentLogVo.setCountCacheList(countCacheList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if("true".equalsIgnoreCase(isLastLine)){
			materialImportCache.put(
					String.format(MaterialManager.MATERIAL_IMPORT_CACHE_STATUS_KEY, materialVo.getDocId()),
					DocumentStatus.DOC_PRO_SUCCESS);// 标记文件处理完成
		}
	}

	RawData getDocData(MaterialVo materialVo, Integer sheetIndex, String mapKey, Integer minLineNo, String[] parsms,
			Map<String, String> quickFindKeyMap, ProductTemplate proTemplate, List<DocumentLog> batchInsertList,
			Map<String, ProductBrand> brandMap) throws JsonProcessingException {
		RawData docData;
		String quickFindKey = leadBasicMaterialParser.getQuickFindKey(proTemplate.getMoreSheetTitles().get(mapKey),
				parsms, brandMap);
		// 通过关键字排除重复,如果不重复解析单个文件,并且保存关键字
		if (quickFindKeyMap.containsKey(quickFindKey)) {
			String msg;
			if(String.valueOf(sheetIndex).equals(quickFindKeyMap.get(quickFindKey).split(LeadBasicMaterialParser.VALUE_SEPARATE)[0])){
				 msg = String.format("当前行数据与上传文件的第%s行数据重复", quickFindKeyMap.get(quickFindKey).split(LeadBasicMaterialParser.VALUE_SEPARATE)[1]);
			}else{
				msg = String.format("当前行数据与上传文件的第%s个Sheet页的第%s行数据重复", sheetIndex , quickFindKeyMap.get(quickFindKey).split(LeadBasicMaterialParser.VALUE_SEPARATE)[1]); 
			}
			
			leadBasicMaterialParser.addErrorDocLogInList(batchInsertList, materialVo.getDocId(), sheetIndex, minLineNo + 2,
					msg, String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));
			return null;
		} else {
			docData = leadBasicMaterialParser.parseToRawData(materialVo, sheetIndex, mapKey, minLineNo + 2, parsms,
					proTemplate, batchInsertList, brandMap);// 解析数据成对象,方便插入数据库
			quickFindKeyMap.put(quickFindKey, sheetIndex + LeadBasicMaterialParser.VALUE_SEPARATE + (minLineNo + 2));
		}
		if (null == docData) {
			return null;
		}

		// 校验成功,添加成功记录
		leadBasicMaterialParser.addSuccessDocLogInList(batchInsertList, materialVo.getDocId(), sheetIndex, minLineNo + 2,
				String.join(LeadMaterialParser.VALUE_SEPARATE, parsms));

		return docData;
	}
	
	/**
	 * 解析导入文件
	 * @param materialVo
	 * @since 2017年3月30日
	 * @author tb.lijing@yikuyi.com
	 */
	public void basicMaterialParseImportFile(@RequestBody MaterialVo materialVo) {
		documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_IN_PROCESS, null);
		try {
			parseImportFile(materialVo);
		} catch (Exception e) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
					DocumentLog.getSizeLengthMsg(e));
		}
	}
	
	public void handleDocStatus(int countCacheList,MaterialVo materialVo){
		String countCacheListTotalKey = String.format(MaterialManager.MATERIAL_COUNT_CACHE_LIST_TOTAL_KEY,materialVo.getDocId());
		materialCountCacheOps.put(countCacheListTotalKey, countCacheListTotalKey, countCacheList);
		// 如果一次计数都没产生,说明所有数据都校验失败,否则代表文件解析完成,改状态为锁定
		if (countCacheList == 0) {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_PRO_FAILED,
					"全部数据校验失败,没有发送Create_Product MQ");
			msgSender.sendMsg(uploadFileProductTopicName, materialVo.getDocId(), null);// 发送上传日志文件到阿里云MQ
		} else {
			documentManager.updateDocStatus(materialVo.getDocId(), DocumentStatus.DOC_LOCKED, null);
		}
	}
}

