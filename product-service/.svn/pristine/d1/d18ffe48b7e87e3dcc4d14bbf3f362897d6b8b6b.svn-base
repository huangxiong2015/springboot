package com.yikuyi.product.material.bll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.party.facility.model.Facility;
import com.yikuyi.party.resource.FacilityClient;
import com.yikuyi.product.model.ProductUtils;
import com.yikuyi.product.uploadutils.PutDataInterface;
import com.yikuyi.product.uploadutils.UploadUtils;
import com.yikuyi.product.uploadutils.ValidateConfig;
import com.yikuyi.template.model.ProductTemplate;
import com.yikuyi.template.model.ProductTemplate.TemplateType;
import com.ykyframework.exception.SystemException;

/**
 * 导入库存的验证类
 * 
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
@Component
public class LeadMaterialParser {
	
	private static final Logger logger = LoggerFactory.getLogger(LeadMaterialParser.class);
	
	@Autowired
	private ValidateConfig validateConfig;
	
	@Value("${api.basedata.serverUrlPrefix}")
	public String basedataServerUrlPrefix;
	
	@Autowired
	private PartyClientBuilder shipmentClientBuilder;
	
	
	/**
	 * (KEY来源模版defaultTitle转换)KEY对应数据库配置的字段,VALUE对应mongodb存储字段名称
	 */
	private static final Map<String, String> DEFAULT_PUT_DATA_IN_DOC_MAP = new HashMap<>(17);
	private static final Map<String, PutDataInterface> SPECIAL_PUT_DATA_IN_DOC_MAP = new HashMap<>(16);
	
	public static final String VALUE_SEPARATE = "qqq;;;";
	
	//mongodb列名称
	private static final String VENDOR_NAME = "vendorName";
	private static final String MANUFACTURER = "manufacturer";
	public static final String MANUFACTURER_PART_NUMBER = "manufacturerPartNumber";
	public static final String SKUID = "skuId";
	public static final String ATS = "ats";
	public static final String QUANTITY = "quantity";
	private static final String MINIMUM_QUANTITY = "minimumQuantity";
	private static final String UNIT = "unit";
	private static final String SPQ = "spq";
	public static final String PACKAGING_UNIT = "packagingUnit";
	private static final String MOV = "mov";
	private static final String CURRENCY = "currency";
	private static final String CURRENCY_CODE = "currencyCode";
	public static final String PACKAGING = "packaging";
	private static final String DESCRIPTION = "description";
	private static final String LEAD_TIME = "leadTime";
	private static final String MIN_LEAD_TIME_ML = "minLeadTimeML";
	private static final String MAX_LEAD_TIME_ML = "maxLeadTimeML";
	private static final String MIN_FACTORY_LEAD_TIME_ML = "minFactoryLeadTimeML";
	private static final String MAX_FACTORY_LEAD_TIME_ML = "maxFactoryLeadTimeML";
	private static final String MIN_LEAD_TIME_HK = "minLeadTimeHK";
	private static final String MAX_LEAD_TIME_HK = "maxLeadTimeHK";
	private static final String MIN_FACTORY_LEAD_TIME_HK = "minFactoryLeadTimeHK";
	private static final String MAX_FACTORY_LEAD_TIME_HK = "maxFactoryLeadTimeHK";
	private static final String ROHS = "rohs";
	public static final String REGION = "region";
	
	private static final String PRICE_LEVELS = "priceLevels";
	private static final String BREAK_QUANTITY = "breakQuantity";
	private static final String PRICE = "price";
	private static final String UNIT_PRICE = "unitPrice";
	
	private static final String PRICES = "prices";
	public static final String STOCKS = "stocks";
	
	public static final String DATE_CODE = "dateCode";
	
	public static final String COST_TYPE = "costType";
	
	public static final String EXPIRY_DATE = "expiryDate";
	
	static{
		//设置的验证规则
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(VENDOR_NAME, VENDOR_NAME);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MANUFACTURER_PART_NUMBER, MANUFACTURER_PART_NUMBER);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(SKUID, SKUID);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MINIMUM_QUANTITY, MINIMUM_QUANTITY);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(UNIT, UNIT);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MIN_LEAD_TIME_ML, MIN_LEAD_TIME_ML);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MAX_LEAD_TIME_ML, MAX_LEAD_TIME_ML);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MIN_FACTORY_LEAD_TIME_ML, MIN_FACTORY_LEAD_TIME_ML);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MAX_FACTORY_LEAD_TIME_ML, MAX_FACTORY_LEAD_TIME_ML);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MIN_LEAD_TIME_HK, MIN_LEAD_TIME_HK);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MAX_LEAD_TIME_HK, MAX_LEAD_TIME_HK);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MIN_FACTORY_LEAD_TIME_HK, MIN_FACTORY_LEAD_TIME_HK);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MAX_FACTORY_LEAD_TIME_HK, MAX_FACTORY_LEAD_TIME_HK);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(SPQ, SPQ);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(PACKAGING_UNIT, PACKAGING_UNIT);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(MOV, MOV);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(PACKAGING, PACKAGING);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(DESCRIPTION, DESCRIPTION);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(DATE_CODE, DATE_CODE);
		
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(COST_TYPE, COST_TYPE);
		DEFAULT_PUT_DATA_IN_DOC_MAP.put(EXPIRY_DATE, EXPIRY_DATE);
		
		PutStock putStock = new PutStock();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(ATS, putStock);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(LEAD_TIME, putStock);
		
		PutPrice putPrice = new PutPrice();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("qtyBreak1", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("qtyBreak2", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("qtyBreak3", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("qtyBreak4", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("qtyBreak5", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("priceBreak5", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("priceBreak4", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("priceBreak3", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("priceBreak2", putPrice);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put("priceBreak1", putPrice);
		
		PutCurrency pustCurrency = new PutCurrency();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(CURRENCY , pustCurrency);
		
		PutRohs putRohs = new PutRohs();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(ROHS, putRohs);
		
		PutManufacturer putManufacturer = new PutManufacturer();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(MANUFACTURER, putManufacturer);
		
		PutRegion putRegion = new PutRegion();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(REGION, putRegion);
	/*	PutLeadtime putLeadtime = new PutLeadtime();
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(MIN_LEAD_TIME,putLeadtime);
		SPECIAL_PUT_DATA_IN_DOC_MAP.put(MAX_LEAD_TIME,putLeadtime);*/
	}
	
	/**
	 * 将一条导入数据转换为库存对象
	 * @param processId
	 * @param lineNo
	 * @param params
	 * @param emTemplate
	 * @param batchInsertList
	 * @param brand
	 * @return
	 * @throws SystemException
	 * @since 2017年1月13日
	 * @author zr.shuzuo@yikuyi.com
	 * @throws JsonProcessingException 
	 */
	Document parseToRawData(MaterialVo materialVo, int lineNo, String[] params ,ProductTemplate emTemplate,List<DocumentLog> batchInsertList,Map<String, ProductBrand> brand,Map<String,String> facilityMap)
					throws JsonProcessingException {
		String title;// 标题
		String titleNew;
		String showTitle;// 显示标题
		String param;// 值
		String validateResult;// 字段验证结果
		Document docData = new Document();
		docData.append(CURRENCY_CODE, "USD");// 默认美元
		// docData.append(MINIMUM_QUANTITY, "1");
		// docData.append(SPQ, "1");
		//docData.append(MOV, "0.01");

		for (int i = 0; i < emTemplate.getTitles().length; i++) {
			title = emTemplate.getTitles()[i];// 获取标题
			String[] titleArr = title.split(",");
			if (0 == titleArr.length) {
				continue;
			}
			for (String temp : titleArr) {
				titleNew = temp;

				showTitle = emTemplate.getShowTitles()[i];// 获取显示标题
				param = UploadUtils.getParam(i, params);// 获取标题行数对应内容
				// 验证数据
				validateResult = validateConfig.validate(titleNew, showTitle, param,
						emTemplate.getConfigMap(TemplateType.VALIDATE));
				// 如果校验失败，则不进行录入数据库
				if (validateResult != null) {
					addErrorDocLogInList(batchInsertList, materialVo.getDocId(), lineNo, validateResult,
							String.join(VALUE_SEPARATE, params));
					return null;
				}
				//型号不要去掉末尾的0
				if(MANUFACTURER_PART_NUMBER.equals(titleNew)){
					if(params.length>i&&params[i]!=null){
						param = params[i].trim();
					}
				}
				// 根据列名进行设值
				if (DEFAULT_PUT_DATA_IN_DOC_MAP.containsKey(titleNew)) {
					operation(docData, DEFAULT_PUT_DATA_IN_DOC_MAP.get(titleNew), param);
				} else if (SPECIAL_PUT_DATA_IN_DOC_MAP.containsKey(titleNew)) {
					SPECIAL_PUT_DATA_IN_DOC_MAP.get(titleNew).operation(docData, titleNew, param, brand,
							materialVo.getVendorId(), facilityMap);
				}
			}
		}
		return docData;
	}
	
	@SuppressWarnings("unchecked")
	//针对有阶梯或者没有价格,有价格没有阶梯的数据过滤
	void updatePriceLevels(Document docData){
		List<Document> costPrices = (List<Document>)docData.get(PRICES);
		if (null != costPrices) {
			//去掉空数据
			List<Document> newList = ((List<Document>) costPrices.get(0).get(PRICE_LEVELS)).stream().filter(s -> s.containsKey(PRICE) && s.containsKey(BREAK_QUANTITY)).collect(Collectors.toList());
			costPrices.get(0).put(PRICE_LEVELS,newList);
			
			//设置币种
			costPrices.get(0).append(CURRENCY_CODE, docData.getString(CURRENCY_CODE));
			//设置单价
			if(null != costPrices.get(0).get(PRICE_LEVELS)
					&& !((List<Document>) costPrices.get(0).get(PRICE_LEVELS)).isEmpty()){
				costPrices.get(0).append(UNIT_PRICE,
					((List<Document>) costPrices.get(0).get(PRICE_LEVELS)).get(0).getString(PRICE));
			}
		}
	}

	void addErrorDocLogInList(List<DocumentLog> batchInsertList ,String processId , int lineNo, String msg , String originalData){
		DocumentLog docLog = new DocumentLog();
		docLog.setId(processId);
		docLog.setLineNo(lineNo);
		docLog.setStatus(DocumentLogStatus.FAIL);
		docLog.setComments(msg);
		docLog.setOriginalData(originalData);
		batchInsertList.add(docLog);
	}
	
	void addSuccessDocLogInList(List<DocumentLog> batchInsertList ,String processId , int lineNo, String originalData){
		DocumentLog docLog = new DocumentLog();
		docLog.setId(processId);
		docLog.setLineNo(lineNo);
		docLog.setStatus(DocumentLogStatus.SUCCESS);
		docLog.setOriginalData(originalData);
		batchInsertList.add(docLog);
	}

	String getQuickFindKey(String[] titles, String[] params, MaterialVo materialVo, Map<String, ProductBrand> brandMap , Map<String, String> facilityMap) {
		String title;
		String param;
		String manufacturer = "";
		String manufacturerPartNumber = "";
		String skuId = "";
		String region = materialVo.getRegionId();
		for (int i = 0; i < titles.length; i++) {
			title = titles[i];// 获取标题
			//做非空判断
			if(null == title){
				continue;
			}
			param = UploadUtils.getParam(i, params);// 获取标题行数对应内容
			switch (title) {
			case MANUFACTURER:
				manufacturer = getStandBrandId(param , brandMap,materialVo.getVendorId());
				break;
			case MANUFACTURER_PART_NUMBER:
				if(params.length>i&&params[i]!=null){
					manufacturerPartNumber = params[i].trim();
				}
				break;
			case SKUID:
				skuId = param;
				break;
			case REGION:
				if(region == null && facilityMap.containsKey(param)){
					region = facilityMap.get(param);
				}else if(region == null && !facilityMap.containsKey(param) && StringUtils.isNotBlank(param)){
					Facility facility = new Facility();
					facility.setFacilityName(param);
					facility.setOwnerPartyId(materialVo.getVendorId());
					FacilityClient client = shipmentClientBuilder.facilityResource();
					region = String.valueOf(client.addFacilityFromLeadMaterial(facility).getId());
					//新增仓库 value=仓库名   valTemp=仓库id  对应basedata表Facility
					facilityMap.put(param, region);
					logger.info("FTP自动下载,创建了供应商{}的{}仓库{}",materialVo.getVendorId(),param,region);
				}
				break;
			default:
				break;
			}
		}
		return ProductUtils.getProductQuickFindKey(StringUtils.isBlank(skuId)? manufacturerPartNumber :skuId, manufacturer, region);
	}
	
	String getSkuId(String manufacturerPartNumber , String packaging){
		String skuId = manufacturerPartNumber;
//		型号即是skuId
//		if (StringUtils.isNotEmpty(packaging)) {
//			skuId = skuId + "-" + packaging.toUpperCase();
//		}
		return skuId;
	}
	
	static ProductBrand getStandBrand(String param, Map<String, ProductBrand> brandMap,String vendorId){
		ProductBrand brand = null;
		//param就是制造商名称
		if(param!=null){
			String key = "";
			if(vendorId!=null)
				key+=vendorId+"-";
			key+=param;
			key = key.toUpperCase();
			brand = brandMap.get(key);//获取待供应商的制造商名称
			//制造商
			if(brand==null)
				brand = brandMap.get(param.toUpperCase());
		}
		return brand;
	}
	
	static String getStandBrandId(String param, Map<String, ProductBrand> brandMap,String vendorId){
		String manufacturer = param;
		ProductBrand brand = getStandBrand(param,brandMap,vendorId);
		if(brand!=null){
			manufacturer = brand.getId().toString();
		}
		return manufacturer;
	}
	
	static String getStandBrandName(String param, Map<String, ProductBrand> brandMap,String vendorId){
		String manufacturer = param;
		ProductBrand brand = getStandBrand(param,brandMap,vendorId);
		if(brand!=null){
			manufacturer = brand.getBrandName();
		}
		return manufacturer;
	}
	
	private void operation(Document doc , String key , Object value){
		if(null != value && StringUtils.isNotBlank(value.toString())){
			doc.append(key, value);
		}
	}
	
	//价格存储规则
	static class PutPrice implements PutDataInterface {
		//EXCEL名称qtyBreak1,qtyBreak2,qtyBreak3....
		private static final String QTY_BREAK = "qtyBreak";
		private static final String PRICE_BREAK = "priceBreak";
		
		private static final Map<String, String> fieldNameMap = new HashMap<>(2);
		static{
			fieldNameMap.put(QTY_BREAK, BREAK_QUANTITY);
			fieldNameMap.put(PRICE_BREAK, PRICE);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void operation(Object obj , String key , String value, Object...args){
			Document doc = (Document) obj;
			String keyPrefix = key.substring(0, key.length()-1);
			int keyNum = Integer.parseInt(key.substring(key.length()-1))-1;
			List<Document> pricesList = (List<Document>) doc.get(PRICES);
			pricesList = isNullList(pricesList);
			if(!doc.containsKey(PRICES)){
				doc.append(PRICES, pricesList);
			}
			if(QTY_BREAK.equals(keyPrefix)){
				getQtyParam(pricesList , keyNum , value);
			}else{
				getPricesParam(pricesList , keyNum , value);
			}
		}
		
		
		
		@SuppressWarnings("unchecked")
		private void getPricesParam(List<Document> costPrices , int i , String params){//过滤数据小于等于0的数据
			try {
				if(null != params && params.length()>0){
					BigDecimal prices = new BigDecimal(params);
					if(prices.compareTo(new BigDecimal("0.00001")) >= 0){
						((List<Document>) costPrices.get(0).get(PRICE_LEVELS)).get(i).append(PRICE, prices.toString());
					}
				}
			} catch (NumberFormatException e) {
				//防止整条数据异常,捕获异常不做任何处理
			}
		}
		
		@SuppressWarnings("unchecked")
		private void getQtyParam(List<Document> costPrices , int i , String params){//过滤数据小于等于0的数据
			try {
				if(null != params && params.length()>0){
					Integer prices = Integer.parseInt(params);
					if(prices>0){
						((List<Document>) costPrices.get(0).get(PRICE_LEVELS)).get(i).append(BREAK_QUANTITY, prices);
					}
				}
			} catch (NumberFormatException e) {
				//防止整条数据异常,捕获异常不做任何处理
			}
		}
		
		private List<Document> isNullList(List<Document> list) {
			if (null == list) {
				return getInitList();
			}else{
				return list;
			}
		}
		
		private List<Document> getInitList() {
			List<Document> rstList = new ArrayList<>(1);

			Document productPrice = new Document();

			List<Document> priceLevels = new ArrayList<>(5);
			Document productPriceLevel1 = new Document();
			Document productPriceLevel2 = new Document();
			Document productPriceLevel3 = new Document();
			Document productPriceLevel4 = new Document();
			Document productPriceLevel5 = new Document();

			priceLevels.add(productPriceLevel1);
			priceLevels.add(productPriceLevel2);
			priceLevels.add(productPriceLevel3);
			priceLevels.add(productPriceLevel4);
			priceLevels.add(productPriceLevel5);

			productPrice.append(PRICE_LEVELS, priceLevels);
			rstList.add(productPrice);
			return rstList;
		}
	}
	
	//库存存储规则
	static class PutStock implements PutDataInterface {
		
		private static final Map<String, String> fieldNameMap = new HashMap<>();
		static{
			fieldNameMap.put(ATS, QUANTITY);
			fieldNameMap.put(LEAD_TIME, LEAD_TIME);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void operation(Object obj , String key , String value, Object...args){
			Document doc = (Document) obj;
			String tempVal = value;
			List<Document> stocksList = (List<Document>)doc.get(STOCKS);
			if(null == stocksList){
				Document stockDocData = new Document();
				// 库存
				stocksList = new ArrayList<>(1);
				stocksList.add(stockDocData);
				doc.append(STOCKS, stocksList);
			}
			if(StringUtils.isBlank(tempVal)){
				tempVal = "0";
			}
			stocksList.get(0).append(fieldNameMap.get(String.valueOf(key)), String.valueOf(tempVal));
		}
	}
	
	//币种存储规则(RMB转CNY处理)
	static class PutCurrency implements PutDataInterface {
		@Override
		public void operation(Object obj , String key , String value, Object...args){
			Document doc = (Document) obj;
			String tempVal = value;
			if(StringUtils.isBlank(tempVal)){
				tempVal = "USD";
			}
			String valueTemp = "RMB".equalsIgnoreCase(tempVal) ? "CNY" : tempVal.toUpperCase();
			doc.append(CURRENCY_CODE, valueTemp);
		}
	}
	
	//ROHS存储规则
	static class PutRohs implements PutDataInterface {

		@Override
		public void operation(Object obj, String key, String value, Object... args) {
			Document doc = (Document) obj;
			if (StringUtils.isBlank(value)) {
				doc.append(ROHS, "");
			} else {
				doc.append(ROHS, "Y".equalsIgnoreCase(value) ? true : false);
			}
		}

	}
	
	///制造商/品牌处理
	static class PutManufacturer implements PutDataInterface {
		
		@SuppressWarnings("unchecked")
		@Override
		public void operation(Object obj, String key, String value, Object... args) {
			Document doc = (Document) obj;
			String manufacturer = getStandBrandName(value, (Map<String, ProductBrand>)args[0],(String)args[1]);
			doc.append(key, manufacturer);
		}
	}
	
	///仓库ID,特殊处理
	static class PutRegion implements PutDataInterface {
		@SuppressWarnings("unchecked")
		@Override
		public void operation(Object obj, String key, String value, Object... args) {
			Document doc = (Document) obj;
			Map<String,String> facilityMap = (Map<String,String>)args[2];
			String valTemp = value.length() == 0 ?  value : facilityMap.get(value);
			doc.append(key, valTemp);
	}
	
	///仓库ID,特殊处理
	/*static class PutLeadtime implements PutDataInterface {
		@Override
		public void operation(Object obj, String key, String value, Object... args) {
			Document doc = (Document) obj;
			if(null != value){
				doc.append(key, value);
			}
		}
	}*/
	}
}