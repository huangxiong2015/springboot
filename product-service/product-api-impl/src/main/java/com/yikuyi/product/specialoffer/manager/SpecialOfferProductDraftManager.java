/*
 * Created: 2017年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer.manager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactorySax;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.activity.bll.ActivityProductReader;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.specialoffer.dao.SpecialOfferProductDraftClient;
import com.yikuyi.product.specialoffer.repository.SpecialOfferProductDraftRepository;
import com.yikuyi.product.template.dao.ProductTemplateRepository;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.specialoffer.model.SpecialOfferProduct.Status;
import com.yikuyi.specialoffer.model.SpecialOfferProductDraft;
import com.yikuyi.specialoffer.vo.SpecialOfferProductDraftVo;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;
/**
 * 专属特价草稿处理业务类
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SpecialOfferProductDraftManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SpecialOfferProductDraftManager.class);
	
	@Autowired
	private SpecialOfferProductDraftRepository specialOfferProductDraftRepository;
	
	@Autowired
	private SpecialOfferProductDraftClient specialOfferProductDraftClient;
	
	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private BrandManager brandManager;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductTemplateRepository productTemplateRepository;
	
	@Autowired
	private DocumentManager documentManager;
	
	/**
	 * 阿里云文件路径
	 */
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;
	
	/**
	 * mongodb中存的专属特价商品上传文件用的，模板id
	 */
	private static final String TEMPLATE_ID = "specialOfferProduct";
	
	private static final String EXPORT_TEMPLATE = "*ManufacturerName（制造商）,*MPN（型号）,*Distribution Name（分销商）,Storehouse 仓库,";
	/**
	 * 上传的文件解析
	 * @param spDraftVo
	 * @throws BusinessException
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public void parseFile(SpecialOfferProductDraftVo spDraftVo) throws BusinessException{
		MongoCollection<Document> specialOfferProductDraftCollection = specialOfferProductDraftClient.getCollection();
		String userId = RequestHelper.getLoginUserId();
		String userName = RequestHelper.getLoginUser().getUsername();
		String currentTimeMillis = Long.toString(System.currentTimeMillis());
		// 生成id用于下载的文件名称
		String docId = String.valueOf(IdGen.getInstance().nextId());
		// 从阿里云下载文件
		String fileName = materialManager.fileDownload(spDraftVo.getFileUrl(), spDraftVo.getOriFileName(), docId);
		if(StringUtils.isBlank(fileName)){
			// 当文件不存在时，抛出异常
			throw new BusinessException(BusiErrorCode.FILE_NULL, "文件不存在");
		}
		// 取得文件
		File attFile = new File(leadMaterialFilePath + File.separator + fileName);
		// 获取模板
		ProductTemplate template = productTemplateRepository.findOne(TEMPLATE_ID);
		ActivityProductReader reader = new ActivityProductReader(template, 1000);
		LeadInFactorySax.createProcess(reader, attFile, null);
		String error = reader.getErrorMsg();
		if (StringUtils.isNotBlank(error)) {
			// 当标题错误时，抛出异常
			throw new BusinessException(BusiErrorCode.TITLE_ERROR, error);
		}
	/*	SpecialOfferProductDraftVo tempI = new SpecialOfferProductDraftVo();
		SpecialOfferProductDraftVo tempJ = new SpecialOfferProductDraftVo();*/
		List<SpecialOfferProductDraftVo> listResultTemp = new ArrayList<>();
		// 获取已经读取的数据
		List<String[]> data = reader.getDatas();
		if(data.size()<=1){
			return;
		}
		//StringBuilder repeatError = new StringBuilder();
		int rowNum = 1;
		// 品牌
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();
		//结合实际业务场景,一个文件很少出现多个供应商，避免使用全量供应商查询
		//定义一个全局变量，没有查询查询到了，本次文件解析直接再次使用
		//Key用供应商名称，Value存基本信息
		Map<String,SupplierVo> suppliers = new HashMap<>();
		Map<String,String> supplierNames = new CaseInsensitiveMap<>(partyClientBuilder.supplierClient().getSupplierNames());
		//标记重复的行数
		Map<String, Integer> reperatStringMap = new HashMap<>();
		//标记重复的商品
		Map<String, Integer> reperatProductMap = new HashMap<>();
		//规则Id
		String ruleId = spDraftVo.getRuleId();
		
		for (String[] temp : data){
			String sourceName = "";
			Integer inputBrandId = null;
			rowNum++;
			StringBuilder errorDesc = new StringBuilder();
			
			SpecialOfferProductDraftVo spDraft = new SpecialOfferProductDraftVo();
			//主键id
			String id = String.valueOf(IdGen.getInstance().nextId());
			spDraft.setId(id);
			//规则Id
			spDraft.setRuleId(ruleId);
			//批次号
			spDraft.setDocumentId(docId);
			spDraft.setRowNum(rowNum);
			//品牌名称标准化
			String inputBrand = StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase()) ? brandMap.get(temp[0].trim().toUpperCase()).getBrandName() : temp[0];
			if(StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase())){
				//品牌id
				inputBrandId = brandMap.get(temp[0].trim().toUpperCase()).getId();
			}
			//数据校验
			String checkMsg = checkData(temp);
			if(StringUtils.isNotBlank(checkMsg)){
				addErrorVo(spDraft, inputBrand, temp, checkMsg, listResultTemp);
				continue;
			}
			String key = getRepeatKey(temp);
			if(reperatStringMap.containsKey(key)){
				errorDesc.append("第" + rowNum + "行" + "与第" + reperatStringMap.get(key) + "行数据重复。\n");
				addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
				continue;
			}else{
				reperatStringMap.put(key, rowNum);
			}
			if(temp.length > 0 && !brandMap.containsKey(temp[0].trim().toUpperCase())){
				errorDesc.append("ManufacturerName（制造商）不存在；");
				addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
				continue;
			}
			if (temp.length > 2 && StringUtils.isNotBlank(temp[2])){
				if (!supplierNames.containsKey(temp[2])) {
					errorDesc.append("Distribution Name（分销商）不存在；");
					addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
					continue;
				}else{
					String sourceId = "";
					// 分销商名称
					String supplierName = temp[2];
					String supplierId = supplierNames.get(supplierName);
					if(!supplierId.equalsIgnoreCase(spDraftVo.getVendorId())){
						errorDesc.append("当前数据的供应商与规则供应商不一致；");
						addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
						continue;
					}
					if(!suppliers.containsKey(supplierId)){
						suppliers.putAll(partyClientBuilder.supplierClient().getSupplierSimple(Arrays.asList(supplierId)));
					}
					// 当仓库不为空时，并且匹配到仓库时，给仓库id赋值
					if (temp.length > 3 && StringUtils.isNotBlank(temp[3])) {
						Map<String,Facility> facilitys = new CaseInsensitiveMap<>(suppliers.get(supplierId).getFacilityNameMap());
						if(facilitys.containsKey(temp[3])){
							sourceId = facilitys.get(temp[3]).getId();
						}else{
							errorDesc.append("Storehouse(仓库)填写不正确；");
							addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
							continue;
						}
							
								
						List<ProductVo> productVoList = new ArrayList<>();
						// 当制造商、型号、分销商id、仓库id都有值的时候，查询商品数据
						if (null!=inputBrandId && StringUtils.isNotBlank(temp[1]) && StringUtils.isNotBlank(supplierId) && StringUtils.isNotBlank(sourceId)) {
							// 根据型号、制造商、供应商ID、仓库ID、状态查询有效的商品数据
							productVoList = productRepository.findProductsByMPNandManufacturer(inputBrandId, temp[1].trim(), supplierId.trim(), sourceId.trim(), 1);
						} else if (null!=inputBrandId && StringUtils.isNotBlank(temp[1]) && StringUtils.isNotBlank(supplierId)) {
							// 根据型号、制造商、供应商ID、状态查询有效的商品数据
							productVoList = productRepository.findProductsByCondition(inputBrandId, temp[1].trim(), supplierId.trim(), 1);
						}
						
						if (CollectionUtils.isEmpty(productVoList)) {
							errorDesc.append("商品数据不存在；");
							addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
							continue;
						} else if (productVoList.size() > 1) {
							errorDesc.append("商品数据为多条；");
							addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
							continue;
						} else if (1 == productVoList.size()){
							ProductVo productVo = productVoList.get(0);
							if (null != productVo.getSpu() && StringUtils.isBlank(productVo.getSpu().getId())) {
								errorDesc.append("商品数据非标准；");
								addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
								continue;
							}
							// 当仓库未填写，并且查询到的仓库数据只有一条时，给仓库id赋值
							if ((temp.length > 3 && StringUtils.isBlank(temp[3])) || temp.length < 4) {
								if (null != productVo && StringUtils.isNotBlank(productVo.getSourceId())) {
									sourceName = Optional.ofNullable(suppliers.get(supplierId).getFacilityIdMap().get(productVo.getSourceId())).map(Facility::getFacilityName).orElse(StringUtils.EMPTY);
									
								}
							}
							if(reperatProductMap.containsKey(productVo.getId())){
								errorDesc.append("第" + rowNum + "行" + "与第" + reperatStringMap.get(key) + "行数据重复。\n");
								addErrorVo(spDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
								continue;
							}else{
								reperatProductMap.put(productVo.getId(), rowNum);
							}
							spDraft.setVendorId(productVo.getVendorId());
							spDraft.setSourceId(productVo.getSourceId());
							if (StringUtils.isNotBlank(sourceName)) {
								spDraft.setSourceName(sourceName);
							}
							spDraft.setProductId(productVo.getId());
							addSuccessVo(spDraft, inputBrand, temp, listResultTemp);
						}
					}
			} 
		}
		}
		
		/**
		 * 判断上传的文件中是否有重复数据
		 */
	/*	String tempIStr = "";
		String tempJStr = "";
		if (CollectionUtils.isNotEmpty(listResultTemp)) {
			for (int i = 0; i < listResultTemp.size(); i++) {
				tempI = listResultTemp.get(i);
				tempIStr = tempI.getBrandName() + tempI.getMpn() + tempI.getVendorName() + tempI.getSourceName();
				for (int j = i + 1; j < listResultTemp.size(); j++) {
					tempJ = listResultTemp.get(j);
					tempJStr = tempJ.getBrandName() + tempJ.getMpn() + tempJ.getVendorName() + tempJ.getSourceName();
					if (StringUtils.isNotBlank(tempIStr) && StringUtils.isNotBlank(tempJStr) && tempIStr.equalsIgnoreCase(tempJStr)) {
						repeatError.append("第" + tempI.getRowNum() + "行" + "与第" + tempJ.getRowNum() + "行数据重复。\n");
						continue;
					}
				}
			}
		}
		if (repeatError.length() > 0) {
			// 当上传的数据有重复时，抛出异常
			throw new BusinessException(BusiErrorCode.REPEAT_ERROR, repeatError.toString());
		}*/
		List<Document> documents = new ArrayList<>();
		for (SpecialOfferProductDraftVo draft : listResultTemp){
			Document document = new Document();
			Document insertDocument = new Document();
			insertDocument.put("_id", draft.getId());
			if (StringUtils.isNotBlank(draft.getVendorId())) {
				insertDocument.put("vendorId", draft.getVendorId());
			}
			if (StringUtils.isNotBlank(draft.getSourceId())) {
				insertDocument.put("sourceId", draft.getSourceId());
			}
			if (StringUtils.isNotBlank(draft.getRemark())) {
				insertDocument.put("remark", draft.getRemark());
			}
			if (StringUtils.isNotBlank(draft.getProductId())) {
				insertDocument.put("productId", draft.getProductId());
			}
			insertDocument.put("documentId", draft.getDocumentId());
			insertDocument.put("status", draft.getStatus().toString());
			if (StringUtils.isNotBlank(draft.getBrandName())) {
				insertDocument.put("brandName", draft.getBrandName());
			}
			if (StringUtils.isNotBlank(draft.getMpn())) {
				document.put("mpn", draft.getMpn());
				insertDocument.put("mpn", draft.getMpn());
			}
			if (StringUtils.isNotBlank(draft.getVendorName())) {
				document.put("vendorName", draft.getVendorName());
				insertDocument.put("vendorName", draft.getVendorName());
			}
			insertDocument.put("creator", userId);
			insertDocument.put("lastUpdateUser", userId);
			insertDocument.put("creatorName", userName);
			insertDocument.put("lastUpdateUserName", userName);
			insertDocument.put("createdTimeMillis", currentTimeMillis);
			insertDocument.put("updatedTimeMillis", currentTimeMillis);
			document.put("ruleId", draft.getRuleId());
			insertDocument.put("ruleId", draft.getRuleId());
			if(StringUtils.isNotBlank(draft.getSourceName())){
				document.put("sourceName", draft.getSourceName());
				insertDocument.put("sourceName", draft.getSourceName());
			}
			documents.add(insertDocument);
			if(StringUtils.isNotBlank(draft.getBrandName()) && StringUtils.isNotBlank(draft.getMpn()) 
					&& StringUtils.isNotBlank(draft.getVendorName()) && StringUtils.isNotBlank(draft.getSourceName())){
				//删除重复的数据
				specialOfferProductDraftCollection.deleteMany(document);
			}else if(StringUtils.isNotBlank(draft.getBrandName()) && StringUtils.isNotBlank(draft.getMpn()) 
					&& StringUtils.isNotBlank(draft.getVendorName())){
				//删除重复的数据
				specialOfferProductDraftCollection.deleteMany(document);
			}
		}
		if(CollectionUtils.isNotEmpty(documents)){
			//保存数据
			specialOfferProductDraftCollection.insertMany(documents);
			/**
			 * 将文档的数据插入到Document表中
			 */
			com.yikuyi.document.model.Document doc = new com.yikuyi.document.model.Document();
			String documentId = String.valueOf(IdGen.getInstance().nextId());
			doc.setId(documentId);
			doc.setTypeId(DocumentType.SPECIAL_OFFERP_RODUCT);
			doc.setDocumentLocation("http:" + spDraftVo.getFileUrl());
			doc.setDocumentName(spDraftVo.getOriFileName());
			doc.setStatusId(DocumentStatus.DOC_PRO_SUCCESS);
			doc.setCreator(userId);
			doc.setLastUpdateUser(userId);
			documentManager.insertDoc(doc);
		}
	}
	
	/**
	 * 校验数据
	 * @param temp
	 * @return
	 * @since 2017年12月21日
	 * @author tb.lijing@yikuyi.com
	 */
	private String checkData(String[] temp){
		StringBuilder errorDesc = new StringBuilder();
		if (temp.length > 0 && StringUtils.isBlank(temp[0])) {
			errorDesc.append("未填ManufacturerName（制造商）；");
		}else if ((temp.length > 1 && StringUtils.isBlank(temp[1])) || temp.length == 1) {
			errorDesc.append("未填MPN（型号）；");
		}else if ((temp.length > 2 && StringUtils.isBlank(temp[2])) || (temp.length > 0 && temp.length < 3)) {
			errorDesc.append("未填Distribution Name（分销商）；");
		}
		return errorDesc.toString();
	}
	
	/**
	 * 添加错误数据
	 * @param specialOfferProductDraftVo
	 * @param inputBrand
	 * @param temp
	 * @param errorMsg
	 * @param results
	 * @since 2017年12月21日
	 * @author tb.lijing@yikuyi.com
	 */
	private void addErrorVo(SpecialOfferProductDraftVo specialOfferProductDraftVo, String inputBrand, String[] temp , String errorMsg , List<SpecialOfferProductDraftVo> results){
		specialOfferProductDraftVo.setRemark(errorMsg);
		specialOfferProductDraftVo.setStatus(Status.UNABLE);
		addParpertion(specialOfferProductDraftVo, inputBrand, temp);
		results.add(specialOfferProductDraftVo);
	}
	
	/**
	 * 添加成功数据
	 * @param specialOfferProductDraftVo
	 * @param inputBrand
	 * @param temp
	 * @param results
	 * @since 2017年12月21日
	 * @author tb.lijing@yikuyi.com
	 */
	private void addSuccessVo(SpecialOfferProductDraftVo specialOfferProductDraftVo, String inputBrand, String[] temp , List<SpecialOfferProductDraftVo> results){
		specialOfferProductDraftVo.setStatus(Status.ENABLE);
		addParpertion(specialOfferProductDraftVo, inputBrand, temp);
		results.add(specialOfferProductDraftVo);
	}
	/**
	 * 赋值
	 * @param spDraft
	 * @param inputBrand
	 * @param temp
	 * @since 2017年12月21日
	 * @author tb.lijing@yikuyi.com
	 */
	private void addParpertion(SpecialOfferProductDraftVo spDraft, String inputBrand, String[] temp){
		if (temp.length > 0 && StringUtils.isNotBlank(inputBrand)) {
			spDraft.setBrandName(inputBrand.trim());
		}
		if (temp.length > 1 && StringUtils.isNotBlank(temp[1])) {
			spDraft.setMpn(temp[1].trim());
		}
		if (temp.length > 2 && StringUtils.isNotBlank(temp[2])) {
			spDraft.setVendorName(temp[2]);
		}
		if (temp.length > 3 && StringUtils.isNotBlank(temp[3])) {
			spDraft.setSourceName(temp[3]);
		}
	}
	
	/**
	 * 分页查询专属特价商品草稿数据
	 * @param ruleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public PageInfo<SpecialOfferProductDraft> findSpecialOfferProductDraftByPage(String ruleId, int page, int pageSize){
		Query query  = new Query();
		Criteria criteria = new Criteria();
		criteria.and("ruleId").is(ruleId);
		int pageNo = 0;
		if(page>0){
			pageNo = page-1;
		}
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		query.addCriteria(criteria);
		Page<SpecialOfferProductDraft> pageInfo = specialOfferProductDraftRepository.findSpecialOfferProductDraftByPage(query.getQueryObject(), pageable);
		List<SpecialOfferProductDraft> specialOfferProductDrafts = pageInfo.getContent();
		if(CollectionUtils.isEmpty(specialOfferProductDrafts)){
			PageInfo<SpecialOfferProductDraft> pageResult = new PageInfo<>(Collections.emptyList());
			pageResult.setPageNum(page);
			pageResult.setPageSize(pageSize);
			pageResult.setTotal(pageInfo.getTotalElements());	
			return pageResult;
		}else{
			specialOfferProductDrafts.stream().forEach(a -> {
				a.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(a.getCreatedTimeMillis()));
				a.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(a.getUpdatedTimeMillis()));
			});
		}
		PageInfo<SpecialOfferProductDraft> pageResult = new PageInfo<>(specialOfferProductDrafts);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(pageInfo.getTotalElements());	
		return pageResult;
	}
	
	/**
	 * 删除专属特价商品草稿数据
	 * @param ids
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public void deleteSpecialOfferProductDraft(List<String> ids){
		MongoCollection<Document> specialOfferProductDraftCollection = specialOfferProductDraftClient.getCollection();
		specialOfferProductDraftCollection.deleteMany(new Document("_id",new Document("$in",ids)));
	}

	/**
	 * 添加草稿商品信息
	 * @param productDraft
	 * @since 2017年12月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void addSpecialOfferProductDraft(SpecialOfferProductDraft productDraft) {
		try {
			LoginUser userInfo = RequestHelper.getLoginUser();
			productDraft.setId(String.valueOf(IdGen.getInstance().nextId()));
			productDraft.setCreator(userInfo.getId());
			productDraft.setCreatorName(userInfo.getUsername());
			productDraft.setCreatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			productDraft.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			productDraft.setLastUpdateUser(userInfo.getId());
			productDraft.setLastUpdateUserName(userInfo.getUsername());
			//添加默认有效
			productDraft.setStatus(Status.ENABLE);
			//处理仓库名称
			if(productDraft.getSourceId()!=null){
				List<String> list = new ArrayList<>();
				list.add(productDraft.getSourceId());
				List<Facility> facilityList = partyClientBuilder.facilityResource().getFacility(list);
				if(CollectionUtils.isNotEmpty(facilityList)){
					for (int i = 0; i < facilityList.size(); i++) {
						productDraft.setSourceName(facilityList.get(0).getFacilityName());
					}
				}
			}
			specialOfferProductDraftRepository.save(productDraft);
		} catch (Exception e) {
			logger.error("添加草稿商品信息：{}", e);
		}
	}
	
	private FindIterable<Document> getProductList(String ids, String ruleId,Status status){
		MongoCollection<Document> specialOfferProductDraftCollection = specialOfferProductDraftClient.getCollection();
		FindIterable<Document> result = null;
		if(null!=status && status == Status.UNABLE){
			result = specialOfferProductDraftCollection.find(new Document("ruleId",ruleId).append("status", Status.UNABLE.name()))
					.projection(new Document("mpn",1).append("brandName", 1).append("vendorName", 1).append("sourceName", 1).append("remark", 1));
		}else{
			if(StringUtils.isNotBlank(ids)){
				String[] strs = ids.split(",");
				result = specialOfferProductDraftCollection.find(new Document("_id",new Document("$in",Arrays.asList(strs))))
				.projection(new Document("mpn",1).append("brandName", 1).append("vendorName", 1).append("sourceName", 1).append("remark", 1));
			}else{
				result = specialOfferProductDraftCollection.find(new Document("ruleId",ruleId))
						.projection(new Document("mpn",1).append("brandName", 1).append("vendorName", 1).append("sourceName", 1).append("remark", 1));
			}
		}
		return result;
	}
	
	/**
	 * 导出专属特价商品草稿数据
	 * @param ids
	 * @param ruleId
	 * @param status
	 * @param response
	 * @throws IOException
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public void exportSpecialOfferProductDrafts(String ids, String ruleId, Status status, 
			HttpServletResponse response)throws IOException {
		FindIterable<Document> result = getProductList(ids, ruleId, status);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "Products.xls"));
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		this.exportProductExcelXls(result, response.getOutputStream());
	}
	
	private void exportProductExcelXls(FindIterable<Document> list, OutputStream os) {
		ExportProcesser processer = null;
		try {
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);
			// 标题
			processer.writeLine("Sheet1", EXPORT_TEMPLATE.split(","));
			//写入文件
			this.productDataList(list,processer);
			processer.output();
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new SystemException("exportProductExcelXls Exception", e);
		} finally {
			if (null != processer) {
				processer.close();
			}
		}
	}
	
	private List<List<String>> productDataList(FindIterable<Document> list,ExportProcesser processer){
		List<List<String>> rowDataList = Lists.newArrayList();
		list.forEach(new Consumer<Document>(){
			@Override
			public void accept(Document t) {
				List<String> rowData = Lists.newArrayList();;
				// 制造商
				rowData.add(t.getString("brandName"));
				// 型号
				rowData.add(t.getString("mpn"));
				// 分销商
				rowData.add(t.getString("vendorName"));
				// 仓库
				rowData.add(t.getString("sourceName"));
				processer.writeLine("Sheet1", rowData);
			}
		});
		return rowDataList;
	}
	
	/**
	 * 拿到判断重复的key
	 * 为避免型号品牌包含特殊符号，自定义特殊符号分割  qqq;;;
	 * @param temp
	 * @return
	 * @since 2017年12月28日
	 * @author tb.lijing@yikuyi.com
	 */
	private String getRepeatKey(String[] temp){
		StringBuilder repeatKey = new StringBuilder().append(String.valueOf(temp[0]).toUpperCase()).append("qqq;;;")
		.append(String.valueOf(temp[1]).toUpperCase()).append("qqq;;;")
		.append(String.valueOf(temp[2]).toUpperCase()).append("qqq;;;");
		if(temp.length>3 && StringUtils.isNotBlank(temp[3])){
			repeatKey.append(String.valueOf(temp[3]).toUpperCase()).append("qqq;;;");
		}
		return repeatKey.toString(); 
	}

}