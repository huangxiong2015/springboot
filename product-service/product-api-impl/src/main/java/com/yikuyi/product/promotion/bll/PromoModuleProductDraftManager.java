
package com.yikuyi.product.promotion.bll;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framewrok.springboot.web.RequestHelper;
import com.google.common.collect.Lists;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactorySax;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.document.model.Document;
import com.yikuyi.document.model.Document.DocumentStatus;
import com.yikuyi.document.model.Document.DocumentType;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.activity.bll.ActivityProductReader;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.common.utils.BusiErrorCode;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.goods.manager.ProductManager;
import com.yikuyi.product.material.bll.MaterialManager;
import com.yikuyi.product.model.ProductImage;
import com.yikuyi.product.promotion.dao.PromoModuleProductDraftDao;
import com.yikuyi.product.promotion.dao.PromotionDao;
import com.yikuyi.product.promotion.repository.PromotionModuleContentDraftRepository;
import com.yikuyi.product.template.dao.ProductTemplateRepository;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.vo.CategoryVo;
import com.yikuyi.promotion.vo.ConditionVo;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;

@Service
public class PromoModuleProductDraftManager {
	private static final Logger logger = LoggerFactory.getLogger(PromoModuleProductDraftManager.class);

	@Autowired
	private PromotionModuleContentDraftRepository moduleDraftRepository;

	@Autowired
	private BrandManager brandManager;

	@Autowired
	private DocumentManager documentManager;

	@Autowired
	private ProductManager productManager;

	@Autowired
	private ProductTemplateRepository productTemplateRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PromoModuleProductDraftDao promoModuleProductDraftDao;

	@Autowired
	private PromotionCacheManager promotionCacheManager;
	@Autowired
	private PromoModuleProductManager promoModuleProductManager;

	@Autowired
	private PartyClientBuilder partyClientBuilder;
	
	@Autowired
	private MaterialManager materialManager;

	/**
	 * mongodb中存的商品上传文件用的，模板id
	 */
	private static final String TEMPLATE_ID = "activityPromoProduct";

	private static final String EXPORT_TEMPLATE = "ManufacturerName（制造商）,MPN（型号）,*Distribution Name（分销商）,*STOCK QTY（库存）,Storehouse 仓库,"
			+ "*Currency（币种）,*QtyBreak1（数量区间1）,*PriceBreak1（价格区间1）,QtyBreak2（数量区间2）,PriceBreak2（价格区间2）,"
			+ "QtyBreak3（数量区间3）,PriceBreak3（价格区间3）,QtyBreak4（数量区间4）,PriceBreak4（价格区间4）,QtyBreak5（数量区间5）,"
			+ "PriceBreak5（价格区间5）";

	// 阿里云文件路径
	@Value("${leadMaterialFilePath}")
	private String leadMaterialFilePath;

	private static final String JSON_KEY_CONDITION = "condition";
	private static final String JSON_KEY_CONTENTSET = "contentSet";
	private static final ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	private static final String JSON_KEY_DATASOURCE = "dataSource";
	@Autowired
	private PromotionDao promotionDao;

	/**
	 * 编辑商品草稿模块
	 * 
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException
	 */
	public void save(PromotionModuleContentDraft moduleContentDraft) throws BusinessException {
		if (null == moduleContentDraft) {
			return;
		}
		//判断上传商品是否全部是异常商品
		boolean isUpload = isAllUploadProductError(moduleContentDraft);
		try {
			//配置上传商品和系统商品预览和正式路径
			setSearchUrl(moduleContentDraft);
		} catch (JsonMappingException e1) {
			logger.error("json格式转换异常：{}",e1);
			
		} catch (IOException e1) {
			logger.error("json格式转换异常:{}",e1);
		}
        
		PromotionModuleContentDraft module = moduleDraftRepository.findOne(moduleContentDraft.getPromoModuleId());
		String userId = RequestHelper.getLoginUserId();
		moduleContentDraft.setStatus("ENABLE");
        //查询活动模块下的所有商品。用来设置缓存
		PromoModuleProductDraft moduleProductDraft = new PromoModuleProductDraft();
		moduleProductDraft.setPromotionId(moduleContentDraft.getPromotionId());
		moduleProductDraft.setPromoModuleId(moduleContentDraft.getPromoModuleId());
		List<PromoModuleProductDraft> listDraft = promoModuleProductDraftDao.getPromoModuleProductDraftList(moduleProductDraft);
		
		//进行新增或者编辑操作
		if (null != module) {
			moduleContentDraft.setLastUpdateDate(new Date());
			moduleContentDraft.setLastUpdateUser(userId);
			moduleDraftRepository.save(moduleContentDraft);
			// isUpload为true删除上传活动缓存
			try {
				if (isUpload) {
					promotionCacheManager.deletePreviewProductCache(moduleContentDraft.getPromotionId(), listDraft);
				} else {
					List<PromotionModuleContentDraft> contentVos = new ArrayList<>();
					contentVos.add(module);
					promotionCacheManager.deletePreviewRuleCache(moduleContentDraft.getPromotionId(), contentVos);
				}
			} catch (Exception e) {
				logger.error("删除缓存异常：{},{}", e.getMessage(), e);
			}
		} else {
			moduleContentDraft.setCreator(userId);
			moduleContentDraft.setCreatedDate(new Date());
			moduleContentDraft.setLastUpdateDate(new Date());
			moduleContentDraft.setLastUpdateUser(userId);
			moduleDraftRepository.insert(moduleContentDraft);

		}
		// isUpload为true上传活动商品,添加上传活动缓存
		try {
			if (isUpload) {
				promotionCacheManager.addPreviewProductCache(promoModuleProductManager.getAllModuleProductByDraft(listDraft));
			} else {
				// 上传系统商品新增缓存
				// 系统缓存
				List<PromotionModuleContentDraft> contentVos = new ArrayList<>();
				contentVos.add(moduleContentDraft);
				Promotion promotion = promotionDao.getPromotion(moduleContentDraft.getPromotionId());
				promotionCacheManager.addPreviewRuleCache(promotion, contentVos);

			}
		} catch (Exception e) {
			logger.error("新增缓存异常：{},{}", e.getMessage(), e);
		}

	}

	private boolean isAllUploadProductError(PromotionModuleContentDraft moduleContentDraft) throws BusinessException {
		JSONObject jsonObject = moduleContentDraft.getPromotionContent();
		JSONObject secondJson = jsonObject.getJSONObject(JSON_KEY_CONTENTSET);

		boolean isUpload = false;// 用来区分是上传商品还是系统商品
		if (null != secondJson) {
			String dataSource = (String) secondJson.get(JSON_KEY_DATASOURCE);
			if ("GET_BY_UPLOAD".equals(dataSource)) {
				isUpload = true;
				// 如果上传的商品都是异常商品则抛出异常
				PromoModuleProductDraft draft = new PromoModuleProductDraft();
				draft.setPromoModuleId(moduleContentDraft.getPromoModuleId());
				draft.setPromotionId(moduleContentDraft.getPromotionId());
				Integer productCount = promoModuleProductDraftDao.productCount(draft);

				draft.setStatus(ModuleProductStatus.UNABLE);
				Integer draftProductCount = promoModuleProductDraftDao.productCount(draft);

				if (draftProductCount != 0 && productCount == draftProductCount) {
					throw new BusinessException(BusiErrorCode.PRODUCT_ALL_ERROR, "上传的商品都是异常产品");//
				}

			}
		}
		return isUpload;
	}

	private PromotionModuleContentDraft setSearchUrl(PromotionModuleContentDraft moduleContentDraft)
			throws JsonMappingException, IOException {
		try {
			JSONObject rootJsonObejct = moduleContentDraft.getPromotionContent();
			JSONObject contentSetObj = rootJsonObejct.getJSONObject(JSON_KEY_CONTENTSET);
			ConditionVo conditionVo = mapper.readValue(contentSetObj.getString(JSON_KEY_CONDITION), ConditionVo.class);

			String dataSource = (String) contentSetObj.get(JSON_KEY_DATASOURCE);
			if (!StringUtils.isNotEmpty(dataSource)) {
				return moduleContentDraft;
			}
			String previewApi = "";
			String api = "";
			Integer pageNum;
			Integer perPage = 0;
			Integer totalPage = 0;
			JSONObject pageJson = contentSetObj.getJSONObject("page");
			if (null != pageJson) {
				pageNum = pageJson.getInteger("pageNum");
				perPage = pageJson.getInteger("perPage");
				totalPage = pageNum * perPage;
			}
			// 如果是上传使用新路径
			if ("GET_BY_UPLOAD".equals(dataSource)) {
				previewApi = "/v1/promotions/" + moduleContentDraft.getPromotionId() + "/module/"
						+ moduleContentDraft.getPromoModuleId() + "/product?draft=Y&price=Y&pageSize=" + perPage
						+ "&totalPage=" + totalPage;
				api = "/v1/promotions/" + moduleContentDraft.getPromotionId() + "/module/"
						+ moduleContentDraft.getPromoModuleId() + "/product?draft=N&price=Y&pageSize=" + perPage
						+ "&totalPage=" + totalPage;
			} else {
				// 使用老路径
				if (null == conditionVo) {
					return moduleContentDraft;
				}
				//获取拼接的路径
				String apiValue = getApiString(conditionVo);
				if (StringUtils.isNotEmpty(apiValue)) {
					previewApi = "/v2/inventory?" + apiValue.substring(1, apiValue.length())
							+ "&isPreview=Y&pageSize=" + perPage + "&totalPage=" + totalPage;
					api = "/v2/inventory?" + apiValue.substring(1, apiValue.length()) + "&isPreview=N&pageSize="
							+ perPage + "&totalPage=" + totalPage;
				}

			}
			JSONObject searchObject = contentSetObj.getJSONObject("search");
			searchObject.put("previewApi", previewApi);
			searchObject.put("api", api);
			contentSetObj.put("search", searchObject);
			rootJsonObejct.put("contentSet", contentSetObj);
		} catch (Exception e) {
			logger.error("路径拼接异常：{},{}", e.getMessage(), e);
		}

		return moduleContentDraft;
	}

	private String getApiString(ConditionVo conditionVo) {
		List<String> vendorList = conditionVo.getVendor();
		List<String> brandList = conditionVo.getBrand();
		List<CategoryVo> categoryList = conditionVo.getCategory();

		StringBuilder vendorSb = new StringBuilder();
		vendorList.stream().forEach(vendor -> {
			vendorSb.append(vendor + ",");
		});
		String vendorValue = "";
		if (StringUtils.isNotEmpty(vendorSb.toString())) {
			vendorValue = vendorSb.substring(0, vendorSb.length() - 1);
		}

		StringBuilder brandSb = new StringBuilder();
		brandList.stream().forEach(brand -> {
			brandSb.append(brand + ",");
		});
		String brandValue = "";
		if (StringUtils.isNotEmpty(brandSb.toString())) {
			brandValue = brandSb.substring(0, brandSb.length() - 1);
		}

		StringBuilder categorySb = new StringBuilder();
		categoryList.stream().forEach(categoryVo -> {
			String[] cates = categoryVo.getId().split("/");
			if (cates.length > 0) {
				categorySb.append(cates[cates.length - 1] + ",");
			}
		});
		String categoryValue = "";
		if (StringUtils.isNotEmpty(categorySb.toString())) {
			categoryValue = categorySb.substring(0, categorySb.length() - 1);
		}
		StringBuilder apiValue = new StringBuilder()
				.append(StringUtils.isEmpty(vendorValue) ? "" : "&vendorId=" + vendorValue)
				.append(StringUtils.isEmpty(brandValue) ? "" : "&manufacturer=" + brandValue)
				.append(StringUtils.isEmpty(categoryValue) ? "" : "&cat=" + categoryValue);
		return apiValue.toString();
	}
	
	
	private String verifyDate(String[] temp) {
		StringBuilder errorDesc = new StringBuilder();
		if (temp.length > 0 && StringUtils.isBlank(temp[0])) {
			errorDesc.append("未填ManufacturerName（制造商）；");
		} else if ((temp.length > 1 && StringUtils.isBlank(temp[1])) || temp.length == 1) {
			errorDesc.append("未填MPN（型号）；");
		} else if ((temp.length > 2 && StringUtils.isBlank(temp[2])) || (temp.length > 0 && temp.length < 3)) {
			errorDesc.append("未填Distribution Name（分销商）；");
		} else if ((temp.length > 4 && StringUtils.isBlank(temp[4])) || (temp.length > 0 && temp.length < 5)) {
			errorDesc.append("未填STOCK QTY（库存）；");
		} else if (temp.length > 8 && StringUtils.isNotBlank(temp[7]) && StringUtils.isNotBlank(temp[8]) && StringUtils.isBlank(temp[6])) {
			errorDesc.append("未填Currency（币种）；");
		} else if (temp.length > 8 && StringUtils.isNotBlank(temp[8]) && StringUtils.isBlank(temp[7])) {
			errorDesc.append("未填QtyBreak1（数量区间1）；");
		} else if ((temp.length == 8 && StringUtils.isNotBlank(temp[7])) || (temp.length > 8 && StringUtils.isNotBlank(temp[7]) && StringUtils.isBlank(temp[8]))) {
			errorDesc.append("未填PriceBreak1（价格区间1）；");
		} else if ((temp.length == 10 && StringUtils.isNotBlank(temp[9])) || (temp.length > 10 && StringUtils.isNotBlank(temp[9]) && StringUtils.isBlank(temp[10]))) {
			errorDesc.append("未填PriceBreak2（价格区间2）；");
		} else if (temp.length > 10 && StringUtils.isNotBlank(temp[10]) && StringUtils.isBlank(temp[9])) {
			errorDesc.append("未填QtyBreak2（数量区间2）；");
		} else if ((temp.length == 12 && StringUtils.isNotBlank(temp[11])) || (temp.length > 12 && StringUtils.isNotBlank(temp[11]) && StringUtils.isBlank(temp[12]))) {
			errorDesc.append("未填PriceBreak3（价格区间3）；");
		} else if (temp.length > 12 && StringUtils.isNotBlank(temp[12]) && StringUtils.isBlank(temp[11])) {
			errorDesc.append("未填QtyBreak3（数量区间3）；");
		} else if ((temp.length == 14 && StringUtils.isNotBlank(temp[13])) || (temp.length > 14 && StringUtils.isNotBlank(temp[13]) && StringUtils.isBlank(temp[14]))) {
			errorDesc.append("未填PriceBreak4（价格区间4）；");
		} else if (temp.length > 14 && StringUtils.isNotBlank(temp[14]) && StringUtils.isBlank(temp[13])) {
			errorDesc.append("未填QtyBreak4（数量区间4）；");
		} else if ((temp.length == 16 && StringUtils.isNotBlank(temp[15])) || (temp.length > 16 && StringUtils.isNotBlank(temp[15]) && StringUtils.isBlank(temp[16]))) {
			errorDesc.append("未填PriceBreak5（价格区间5）；");
		} else if (temp.length > 16 && StringUtils.isNotBlank(temp[16]) && StringUtils.isBlank(temp[15])) {
			errorDesc.append("未填QtyBreak5（数量区间5）；");
		} else if (temp.length > 4 && StringUtils.isNotBlank(temp[4]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[4])) {
			errorDesc.append("STOCK QTY（库存）只能填写正整数；");
		} else if (temp.length > 6 && StringUtils.isNotBlank(temp[6]) && !"RMB".equalsIgnoreCase(temp[6]) && !"CNY".equalsIgnoreCase(temp[6]) && !"USD".equalsIgnoreCase(temp[6])) {
			errorDesc.append("Currency（币种）只能填写RMB、CNY、USD；");
		} else if (temp.length > 7 && StringUtils.isNotBlank(temp[7]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[7])) {
			errorDesc.append("QtyBreak1（数量区间1）只能填写正整数；");
		} else if (temp.length > 8 && StringUtils.isNotBlank(temp[8]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[8]) || "0".equals(temp[8]))) {
			errorDesc.append("PriceBreak1（价格区间1）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
		} else if (temp.length > 9 && StringUtils.isNotBlank(temp[9]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[9])) {
			errorDesc.append("QtyBreak2（数量区间2）只能填写正整数；");
		} else if (temp.length > 10 && StringUtils.isNotBlank(temp[10]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[10]) || "0".equals(temp[10]))) {
			errorDesc.append("PriceBreak2（价格区间2）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
		} else if (temp.length > 11 && StringUtils.isNotBlank(temp[11]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[11])) {
			errorDesc.append("QtyBreak3（数量区间3）只能填写正整数；");
		} else if (temp.length > 12 && StringUtils.isNotBlank(temp[12]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[12]) || "0".equals(temp[12]))) {
			errorDesc.append("PriceBreak3（价格区间3）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
		} else if (temp.length > 13 && StringUtils.isNotBlank(temp[13]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[13])) {
			errorDesc.append("QtyBreak4（数量区间4）只能填写正整数；");
		} else if (temp.length > 14 && StringUtils.isNotBlank(temp[14]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[14]) || "0".equals(temp[14]))) {
			errorDesc.append("PriceBreak4（价格区间4）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
		} else if (temp.length > 15 && StringUtils.isNotBlank(temp[15]) && !com.ictrade.tools.StringUtils.isPositiveInteger(temp[15])) {
			errorDesc.append("QtyBreak5（数量区间5）只能填写正整数；");
		} else if (temp.length > 16 && StringUtils.isNotBlank(temp[16]) && (!com.ictrade.tools.StringUtils.isPositiveSevenDecimal(temp[16]) || "0".equals(temp[16]))) {
			errorDesc.append("PriceBreak5（价格区间5）只能填写正数，且应不小于0.00001，最大值整数位不能超过7位，小数位不能超过5位；");
		}
		return errorDesc.toString();
	}
	
	/**
	 * 拿到判断重复的key
	 * 为避免型号品牌包含特殊符号，自定义特殊符号分割  qqq;;;
	 * @param temp
	 * @return
	 * @since 2017年10月25日
	 * @author jik.shu@yikuyi.com
	 */
	private String getRepeatKey(String[] temp){
		StringBuilder repeatKey = new StringBuilder().append(String.valueOf(temp[0]).toUpperCase()).append("qqq;;;")
		.append(String.valueOf(temp[1]).toUpperCase()).append("qqq;;;")
		.append(String.valueOf(temp[2]).toUpperCase()).append("qqq;;;");
		if(temp.length>4 && StringUtils.isNotBlank(temp[4])){
			repeatKey.append(String.valueOf(temp[4]).toUpperCase()).append("qqq;;;");
		}
		if(temp.length>5 && StringUtils.isNotBlank(temp[5])){
			repeatKey.append(String.valueOf(temp[5]).toUpperCase()).append("qqq;;;");
		}
		return repeatKey.toString(); 
	}
	
	private void addSuccessVo(PromoModuleProductDraft promoModuleProductDraft, String inputBrand, String[] temp , List<PromoModuleProductDraft> results){
		promoModuleProductDraft.setStatus(ModuleProductStatus.ENABLE);
		addParpertion(promoModuleProductDraft, inputBrand, temp);
		results.add(promoModuleProductDraft);
	}
	
	private void addErrorVo(PromoModuleProductDraft promoModuleProductDraft, String inputBrand, String[] temp , String errorMsg , List<PromoModuleProductDraft> results){
		promoModuleProductDraft.setErrorDescription(errorMsg);
		promoModuleProductDraft.setStatus(ModuleProductStatus.UNABLE);
		addParpertion(promoModuleProductDraft, inputBrand, temp);
		results.add(promoModuleProductDraft);
	}
	
	private void addParpertion(PromoModuleProductDraft promoModuleProductDraft, String inputBrand, String[] temp){
		if (temp.length > 4 && StringUtils.isNotBlank(temp[4])) {
			promoModuleProductDraft.setTotalQty(temp[4]);
			promoModuleProductDraft.setQty(temp[4]);
		}
		if (temp.length > 1 && StringUtils.isNotBlank(temp[1])) {
			promoModuleProductDraft.setManufacturerPartNumber(temp[1].trim());
		}
		if (temp.length > 0 && StringUtils.isNotBlank(inputBrand)) {
			promoModuleProductDraft.setManufacturer(inputBrand.trim());
		}
		if (temp.length > 2 && StringUtils.isNotBlank(temp[2])) {
			promoModuleProductDraft.setVendorName(temp[2]);
		}
		if (temp.length > 5 && StringUtils.isNotBlank(temp[5])) {
			promoModuleProductDraft.setSourceName(temp[5]);
		}
		if (temp.length > 6 && StringUtils.isNotBlank(temp[6])) {
			if ("RMB".equalsIgnoreCase(temp[6])) {
				promoModuleProductDraft.setCurrencyUomId("CNY");
			} else {
				promoModuleProductDraft.setCurrencyUomId(temp[6].toUpperCase());
			}
		}
		if (temp.length > 7 && StringUtils.isNotBlank(temp[7])) {
			promoModuleProductDraft.setQtyBreak1(temp[7]);
		}
		if (temp.length > 8 && StringUtils.isNotBlank(temp[8])) {
			promoModuleProductDraft.setPriceBreak1(temp[8]);
		}
		if (temp.length > 9 && StringUtils.isNotBlank(temp[9])) {
			promoModuleProductDraft.setQtyBreak2(temp[9]);
		}
		if (temp.length > 10 && StringUtils.isNotBlank(temp[10])) {
			promoModuleProductDraft.setPriceBreak2(temp[10]);
		}
		if (temp.length > 11 && StringUtils.isNotBlank(temp[11])) {
			promoModuleProductDraft.setQtyBreak3(temp[11]);
		}
		if (temp.length > 12 && StringUtils.isNotBlank(temp[12])) {
			promoModuleProductDraft.setPriceBreak3(temp[12]);
		}
		if (temp.length > 13 && StringUtils.isNotBlank(temp[13])) {
			promoModuleProductDraft.setQtyBreak4(temp[13]);
		}
		if (temp.length > 14 && StringUtils.isNotBlank(temp[14])) {
			promoModuleProductDraft.setPriceBreak4(temp[14]);
		}
		if (temp.length > 15 && StringUtils.isNotBlank(temp[15])) {
			promoModuleProductDraft.setQtyBreak5(temp[15]);
		}
		if (temp.length > 16 && StringUtils.isNotBlank(temp[16])) {
			promoModuleProductDraft.setPriceBreak5(temp[16]);
		}
	}
	
	/**
	 * 商品上传的文件解析
	 * 
	 * @param promoModuleId
	 * @param promotionId
	 * @param fileUrl
	 * @param oriFileName
	 * @since 2017年10月11日
	 * @author tb.lijing@yikuyi.com
	 */
	public void parseFile(String promoModuleId, String promotionId, String fileUrl, String oriFileName)
			throws BusinessException {
		List<PromoModuleProductDraft> listResultTemp = new ArrayList<>();
		String processId = String.valueOf(IdGen.getInstance().nextId());// 文件上传批次号
		String docId = String.valueOf(IdGen.getInstance().nextId());// 生成id用于下载的文件名称
		String fileName = materialManager.fileDownload(fileUrl, oriFileName, docId);// 从阿里云下载文件
		File attFile = new File(leadMaterialFilePath + File.separator + fileName);// 取得文件
		ProductTemplate template = productTemplateRepository.findOne(TEMPLATE_ID);// 获取模板
		
		ActivityProductReader reader = new ActivityProductReader(template, 1000);
		LeadInFactorySax.createProcess(reader, attFile, null);
		String error = reader.getErrorMsg();
		if (error != null) {
			throw new BusinessException(BusiErrorCode.TITLE_ERROR, error);// 当标题错误时，抛出异常
		}
		List<String[]> uploadData = reader.getDatas();// 获取已经读取的数据
		
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 获取品牌
		Map<String, Integer> reperatStringMap = new HashMap<>();//标记重复的行数
		Map<String, Integer> reperatProductMap = new HashMap<>();//标记重复的商品
		
		//结合实际业务场景,一个文件很少出现多个供应商，避免使用全量供应商查询
		//定义一个全局变量，没有查询查询到了，本次文件解析直接再次使用
		//Key用供应商名称，Value存基本信息
		Map<String,SupplierVo> suppliers = new HashMap<>();
		Map<String,String> supplierNames = new CaseInsensitiveMap<>(partyClientBuilder.supplierClient().getSupplierNames());
		
		for(int i = 0 ;i < uploadData.size() ; i++){
			String[] temp = uploadData.get(i);
			Integer inputBrandId = null;
			//品牌名称标准化
			String inputBrand = StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase()) ? brandMap.get(temp[0].trim().toUpperCase()).getBrandName() : temp[0];
			if(StringUtils.isNotBlank(temp[0]) && brandMap.containsKey(temp[0].trim().toUpperCase())){
				inputBrandId = brandMap.get(temp[0].trim().toUpperCase()).getId();//品牌id
			}
			//构建标准Module
			String promoModuleProductId = String.valueOf(IdGen.getInstance().nextId());// 促销模块商品id
			PromoModuleProductDraft promoModuleProductDraft = new PromoModuleProductDraft();
			promoModuleProductDraft.setProcessId(processId);
			promoModuleProductDraft.setPromoModuleProductId(promoModuleProductId);
			promoModuleProductDraft.setPromoModuleId(promoModuleId);
			promoModuleProductDraft.setPromotionId(promotionId);
			promoModuleProductDraft.setRowNum(i+2);
			
			//数据校验
			String valideMsg = verifyDate(temp);
			if(StringUtils.isNotEmpty(valideMsg)){
				addErrorVo(promoModuleProductDraft, inputBrand, temp, valideMsg, listResultTemp);
				continue;
			}
			//重复校验
			StringBuilder errorDesc = new StringBuilder();
			String key = getRepeatKey(temp);
			if(reperatStringMap.containsKey(key)){
				errorDesc.append("第" + i+2 + "行" + "与第" + reperatStringMap.get(key) + "行数据重复。\n");
				addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
				continue;
			}else{
				reperatStringMap.put(key, i+2);
			}
			String sourceName = "";
			
			//校验供应商是否存在
			if (temp.length > 2 && StringUtils.isNotBlank(temp[2]) && supplierNames.containsKey(temp[2])) {
				String sourceId = "";
				String supplierName = temp[2];// 分销商名称
				String supplierId = supplierNames.get(supplierName);
				if(!suppliers.containsKey(supplierId)){
					suppliers.putAll(partyClientBuilder.supplierClient().getSupplierSimple(Arrays.asList(supplierId)));
				}
				Map<String,Facility> facilityList = new CaseInsensitiveMap<>(suppliers.get(supplierId).getFacilityNameMap());
				//供应商存在，在校验仓库
				if (temp.length > 5 && StringUtils.isNotBlank(temp[5])) {
					if(facilityList.containsKey(temp[5])){
						sourceId = facilityList.get(temp[5]).getId();
					}else{
						errorDesc.append("Storehouse(仓库)填写不正确；");
						addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
						continue;
					}
				}
				List<ProductVo> productVoList = new ArrayList<>();
				// 当制造商、型号、分销商id、仓库id、供应商料号（skuid）都有值的时候，查询商品数据
				if (temp.length > 3 && StringUtils.isNotBlank(temp[3]) && null!=inputBrandId
						&& StringUtils.isNotBlank(temp[1]) && StringUtils.isNotBlank(supplierId)
						&& StringUtils.isNotBlank(sourceId)) {
					productVoList = productRepository.findProductsByCondition(inputBrandId,
							temp[1].trim(), supplierId.trim(), sourceId.trim(), temp[3].trim().toUpperCase(), 1);
					// 当制造商、型号、分销商id、仓库id都有值的时候，查询商品数据
				} else if (null!=inputBrandId && StringUtils.isNotBlank(temp[1])
						&& StringUtils.isNotBlank(supplierId) && StringUtils.isNotBlank(sourceId)) {
					// 根据型号、制造商、供应商ID、仓库ID、状态查询有效的商品数据
					productVoList = productRepository.findProductsByMPNandManufacturer(inputBrandId,
							temp[1].trim(), supplierId.trim(), sourceId.trim(), 1);
				} else if (null!=inputBrandId && StringUtils.isNotBlank(temp[1])
						&& StringUtils.isNotBlank(supplierId)) {
					// 根据型号、制造商、供应商ID、状态查询有效的商品数据
					productVoList = productRepository.findProductsByCondition(inputBrandId,
							temp[1].trim(), supplierId.trim(), 1);
				}

				if (productVoList.isEmpty()) {
					errorDesc.append("商品数据不存在；");
					addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
					continue;
				} else if (productVoList.size() > 1) {
					errorDesc.append("商品数据为多条；");
					addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
					continue;
				} else if (1 == productVoList.size()) {
					ProductVo productVo = productVoList.get(0);
					if (null != productVo.getSpu() && StringUtils.isBlank(productVo.getSpu().getId())) {
						errorDesc.append("商品数据非标准；");
						addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
						continue;
					}
					// 当仓库未填写，并且查询到的仓库数据只有一条时，给仓库id赋值
					if (temp.length > 5 && StringUtils.isBlank(temp[5]) && null != productVo && StringUtils.isNotBlank(productVo.getSourceId())) {
						// 仓库名称
						String sourceIdTemp = productVo.getSourceId().trim();
						String tempSourceNam = Optional.ofNullable(suppliers.get(supplierId).getFacilityIdMap().get(sourceIdTemp)).map(Facility::getFacilityName).orElse(StringUtils.EMPTY);
						sourceName = StringUtils.isEmpty(tempSourceNam) ? sourceName : tempSourceNam;
					}
					if(reperatProductMap.containsKey(productVo.getId())){
						errorDesc.append("第" + i+2 + "行" + "与第" + reperatStringMap.get(key) + "行数据重复。\n");
						addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
						continue;
					}else{
						reperatProductMap.put(productVo.getId(), i+2);
					}
					promoModuleProductDraft.setVendorId(productVo.getVendorId());
					promoModuleProductDraft.setSourceId(productVo.getSourceId());
					if (StringUtils.isNotBlank(sourceName)) {
						promoModuleProductDraft.setSourceName(sourceName);
					}
					promoModuleProductDraft.setProductId(productVo.getId());
					if (null != productVo.getSpu() && StringUtils.isNotBlank(productVo.getSpu().getDescription())) {
						promoModuleProductDraft.setDescription(productVo.getSpu().getDescription());
					}
					// 处理分类 add by zr.aoxianbing@yikuyi.com
					if (null != productVo.getSpu() && CollectionUtils.isNotEmpty(productVo.getSpu().getCategories())) {
						List<ProductCategory> categoryList = productVo.getSpu().getCategories();
						for (ProductCategory productCategory : categoryList) {
							if (Integer.valueOf(3).equals(productCategory.getLevel()) && null != productCategory.getId()) {
								promoModuleProductDraft.setCategory3Id(productCategory.getId().toString());
								promoModuleProductDraft.setCategory3Name(productCategory.getName());
							}
						}
					}
					// 处理图片
					List<ProductImage> spuImage = productVo.getSpu().getImages();
					// 图片地址加上前缀后返回
					List<ProductImage> spuImageNew = productManager.handleImageUrl(spuImage);
					for (ProductImage image : spuImageNew) {
						if ("large".equalsIgnoreCase(image.getType())) {
							promoModuleProductDraft.setImage1(image.getUrl());
						}
					}
					addSuccessVo(promoModuleProductDraft, inputBrand, temp, listResultTemp);
				}
			}else{
				errorDesc.append("分销商不存在；");
				addErrorVo(promoModuleProductDraft, inputBrand, temp, errorDesc.toString(), listResultTemp);
				continue;
			}
		}
		
		//删除所有重复数据
		if(CollectionUtils.isNotEmpty(reperatProductMap.keySet())){
			promoModuleProductDraftDao.deletePromoModuleProductDraft(promoModuleId ,reperatProductMap.keySet());
		}
		
		if(listResultTemp.size()<=100){
			promoModuleProductDraftDao.inserts(listResultTemp);
		}else{
			List<PromoModuleProductDraft> list = new ArrayList<>();
			for(int i = 0;i<listResultTemp.size();i++){
				list.add(listResultTemp.get(i));
				if((i!= 0 && i%100==0) || i==listResultTemp.size()-1){
					promoModuleProductDraftDao.inserts(list);
					list.clear();
				}
				
			}
		}
		
		/**
		 * 将文档的数据插入到Document表中
		 */
		Document doc = new Document();
		String documentId = String.valueOf(IdGen.getInstance().nextId());
		doc.setId(documentId);
		doc.setTypeId(DocumentType.PROMOTION_DOCUMENT);
		doc.setDocumentLocation(fileUrl);
		doc.setDocumentName(oriFileName);
		doc.setStatusId(DocumentStatus.DOC_PRO_SUCCESS);
		doc.setCreator(RequestHelper.getLoginUserId());
		doc.setLastUpdateUser(RequestHelper.getLoginUserId());
		documentManager.insertDoc(doc);
	}

	/**
	 * 批量删除活动装修商品草稿信息
	 * 
	 * @param promoModuleProductIds
	 * @since 2017年10月12日
	 * @author tb.lijing@yikuyi.com
	 */

	public void deletePromoModuleProductDraft(String promoModuleId, String promotionId,
			List<String> promoModuleProductIds) {
		if (CollectionUtils.isNotEmpty(promoModuleProductIds)) {
			List<PromoModuleProductDraft> result = promoModuleProductDraftDao.findPromoModuleProductsByIds(
					promoModuleProductIds.toArray(new String[] {}), ModuleProductStatus.ENABLE);
			promotionCacheManager.deletePreviewProductCache(promotionId, result);// 删除商品的缓存
			promoModuleProductDraftDao.deletePromoModuleProductDraftBatch(promoModuleProductIds);
		}
	}

	/**
	 * 导出活动装修商品草稿信息
	 * 
	 * @param promoModuleProductIds
	 * @param promoModuleId
	 * @param promotionId
	 * @param status
	 * @param response
	 * @throws IOException
	 * @since 2017年10月18日
	 * @author tb.lijing@yikuyi.com
	 */
	public void exportProducts(String promoModuleProductIds, String promoModuleId, String promotionId,
			ModuleProductStatus status, HttpServletResponse response) throws IOException {
		List<PromoModuleProductDraft> list = getProductList(promoModuleProductIds, promoModuleId, promotionId, status);
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "Products.xls"));
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		this.exportProductExcelXls(list, response.getOutputStream(), status);

	}

	private List<PromoModuleProductDraft> getProductList(String promoModuleProductIds, String promoModuleId,
			String promotionId, ModuleProductStatus status) {
		// 判断是否为导出异常商品
		List<PromoModuleProductDraft> list = null;

		if (status != null && status == ModuleProductStatus.UNABLE) {
			// 获取异常商品
			PromoModuleProductDraft promoModuleProductDraft = new PromoModuleProductDraft();
			promoModuleProductDraft.setPromoModuleId(promoModuleId);
			promoModuleProductDraft.setPromotionId(promotionId);
			promoModuleProductDraft.setStatus(status);

			list = promoModuleProductDraftDao.findPromoModuleProductDraftByCondition(promoModuleProductDraft);
		} else {
			// 获取正常商品
			if (StringUtils.isNotBlank(promoModuleProductIds)) {
				// 根据ids 查询商品
				String[] strs = promoModuleProductIds.split(",");
				list = promoModuleProductDraftDao.findPromoModuleProductsByIds(strs, null);
			} else {
				PromoModuleProductDraft promoModuleProductDraft = new PromoModuleProductDraft();
				promoModuleProductDraft.setPromoModuleId(promoModuleId);
				promoModuleProductDraft.setPromotionId(promotionId);

				list = promoModuleProductDraftDao.findPromoModuleProductDraftByCondition(promoModuleProductDraft);
			}
		}
		return list;
	}

	private void exportProductExcelXls(List<PromoModuleProductDraft> list, OutputStream os,
			ModuleProductStatus status) {
		ExportProcesser processer = null;
		try {
			processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, os);
			// 标题
			processer.writeLine("Sheet1", EXPORT_TEMPLATE.split(","));

			List<List<String>> rowDataList;
			rowDataList = this.productDataList(list, status);
			for (List<String> rowData : rowDataList) {
				processer.writeLine("Sheet1", rowData);
			}
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

	private List<List<String>> productDataList(List<PromoModuleProductDraft> list, ModuleProductStatus status) {
		List<List<String>> rowDataList = Lists.newArrayList();
		if (!CollectionUtils.isNotEmpty(list)) {
			return rowDataList;
		}
		List<String> rowData;
		for (PromoModuleProductDraft promoModuleProductDraft : list) {
			rowData = Lists.newArrayList();
			// 制造商
			rowData.add(promoModuleProductDraft.getManufacturer());
			// 型号
			rowData.add(promoModuleProductDraft.getManufacturerPartNumber());
			// 分销商
			rowData.add(promoModuleProductDraft.getVendorName());
			// 库存
			rowData.add(promoModuleProductDraft.getTotalQty());
			// 仓库
			rowData.add(promoModuleProductDraft.getSourceName());
			// 币种
			rowData.add(promoModuleProductDraft.getCurrencyUomId());
			// 数量区间一
			rowData.add(promoModuleProductDraft.getQtyBreak1());
			// 限时价一
			rowData.add(promoModuleProductDraft.getPriceBreak1());
			// 数量区间二
			rowData.add(promoModuleProductDraft.getQtyBreak2());
			// 限时价二
			rowData.add(promoModuleProductDraft.getPriceBreak2());
			// 数量区间三
			rowData.add(promoModuleProductDraft.getQtyBreak3());
			// 限时价三
			rowData.add(promoModuleProductDraft.getPriceBreak3());
			// 数量区间四
			rowData.add(promoModuleProductDraft.getQtyBreak4());
			// 限时价四
			rowData.add(promoModuleProductDraft.getPriceBreak4());
			// 数量区间五
			rowData.add(promoModuleProductDraft.getQtyBreak5());
			// 限时价五
			rowData.add(promoModuleProductDraft.getPriceBreak5());
			rowDataList.add(rowData);
		}
		return rowDataList;
	}

}
