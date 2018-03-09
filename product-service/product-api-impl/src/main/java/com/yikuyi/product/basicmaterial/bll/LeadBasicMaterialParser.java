package com.yikuyi.product.basicmaterial.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.document.model.DocumentLog;
import com.yikuyi.document.model.DocumentLog.DocumentLogStatus;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.document.bll.DocumentManager;
import com.yikuyi.product.model.ProductParameter;
import com.yikuyi.product.uploadutils.UploadUtils;
import com.yikuyi.product.uploadutils.ValidateConfig;
import com.yikuyi.product.vo.RawData;
import com.yikuyi.template.model.ProductTemplate;
import com.yikuyi.template.model.ProductTemplate.TemplateType;

/**
 * 导入库存的验证类
 * 
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
@Component
public class LeadBasicMaterialParser {

	public static final String VALUE_SEPARATE = "qqq;;;";

	@Autowired
	private ValidateConfig validateConfig;

	// mongodb列名称
	private static final String CATEGORIES1 = "categories1";
	private static final String CATEGORIES2 = "categories2";
	private static final String DESCRIPTION = "description";
	private static final String ROHS = "rohs";
	private static final String MANUFACTURER = "manufacturer";
	private static final String MANUFACTURER_PART_NUMBER = "manufacturerPartNumber";
	private static final String RESTRICT_MATERIAL_TYPE = "restrictMaterialType";

	/**
	 * 将一条导入数据转换为基础物料VO
	 * 
	 * @param materialVo
	 * @param sheetIndex
	 * @param lineNo
	 * @param params
	 * @param emTemplate
	 * @param batchInsertList
	 * @param brand
	 * @return
	 * @throws JsonProcessingException
	 * @since 2017年2月22日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public RawData parseToRawData(MaterialVo materialVo, Integer sheetIndex, String mapKey, Integer lineNo, String[] params,
			ProductTemplate emTemplate, List<DocumentLog> batchInsertList, Map<String, ProductBrand> brandMap)
			throws JsonProcessingException {
		String title;// 标题
		String showTitle;// 显示标题
		String param;// 值
		String validateResult;// 字段验证结果
		RawData docData = new RawData();
		docData.setFrom("Basic_Upload");
		//docData.setRohs("false");//默认不符合标准
		docData.setProcessId(materialVo.getDocId());
		docData.setLineNo(Long.parseLong(String.valueOf(lineNo)));
		docData.setSheetIndex(sheetIndex);
		List<ProductParameter> parametersList = new ArrayList<>();
		docData.setParameters(parametersList);
		List<ProductCategory> vendorCategories = new ArrayList<>();
		docData.setVendorCategories(vendorCategories);
		for (int i = 0; i < emTemplate.getMoreSheetTitles().get(mapKey).length; i++) {
			title = emTemplate.getMoreSheetTitles().get(mapKey)[i];// 获取标题
			if (null == title) {
				continue;
			}
			showTitle = emTemplate.getMoreSheetShowTitles().get(mapKey)[i];// 获取显示标题
			param = UploadUtils.getParam(i, params);// 获取标题行数对应内容
			// 验证数据
			validateResult = validateConfig.validate(title, showTitle, param,
					emTemplate.getConfigMap(TemplateType.VALIDATE));
			// 如果校验失败，则不进行录入数据库
			if (validateResult != null) {
				addErrorDocLogInList(batchInsertList, materialVo.getDocId(), sheetIndex, lineNo, validateResult,
						String.join(VALUE_SEPARATE, params));
				return null;
			}
			if(ROHS.equalsIgnoreCase(title)){//rohs忽略大小写
				if("Y".equalsIgnoreCase(param)){
					docData.setRohs("true");
				}else if("N".equalsIgnoreCase(param)){
					docData.setRohs("false");
				}
				continue;
			}
			switch (title) {
			case CATEGORIES1:
				ProductCategory productCategory1 = new ProductCategory();
				productCategory1.setLevel(1);
				productCategory1.setName(param);
				vendorCategories.add(productCategory1);
				break;
			case CATEGORIES2:
				ProductCategory productCategory2 = new ProductCategory();
				productCategory2.setLevel(2);
				productCategory2.setName(param);
				vendorCategories.add(productCategory2);
				break;
			case MANUFACTURER:
				//不修改上传填写的品牌名称，bug地址：http://develop.yikuyi.com/zentao/bug-view-2120.html
//				docData.setManufacturer(getStandBrandName(param, brandMap));
				docData.setManufacturer(param);
				break;
			case MANUFACTURER_PART_NUMBER:
				if(params.length>i&&params[i]!=null){
					param = params[i].trim();
				}
				docData.setManufacturerPartNumber(param);
				break;
			case DESCRIPTION:
				docData.setDescription(param);
				break;
			case RESTRICT_MATERIAL_TYPE:
				docData.setRestrictMaterialType(param);
				break;
			case DocumentManager.ERRORDES://忽悠文件上传错误原因列
				break;
			default:
				ProductParameter parameter = new ProductParameter();
				parameter.setCode(title);
				parameter.setName(title);
				parameter.setValue(param);
				parametersList.add(parameter);
				break;
			}
		}
		return docData;
	}

	public void addErrorDocLogInList(List<DocumentLog> batchInsertList, String processId, Integer sheetIndex, Integer lineNo,
			String msg, String originalData) {
		DocumentLog docLog = new DocumentLog();
		docLog.setId(processId);
		docLog.setSheetIndex(sheetIndex);
		docLog.setLineNo(lineNo);
		docLog.setStatus(DocumentLogStatus.FAIL);
		docLog.setComments(msg);
		docLog.setOriginalData(originalData);
		batchInsertList.add(docLog);
	}

	public void addSuccessDocLogInList(List<DocumentLog> batchInsertList, String processId, Integer sheetIndex, int lineNo,
			String originalData) {
		DocumentLog docLog = new DocumentLog();
		docLog.setId(processId);
		docLog.setSheetIndex(sheetIndex);
		docLog.setLineNo(lineNo);
		docLog.setStatus(DocumentLogStatus.SUCCESS);
		docLog.setOriginalData(originalData);
		batchInsertList.add(docLog);
	}

	public String getQuickFindKey(String[] titles, String[] params, Map<String, ProductBrand> brandMap) {
		String title;
		String param;
		String manufacturer = "";
		String manufacturerPartNumber = "";
		for (int i = 0; i < titles.length; i++) {
			title = titles[i];// 获取标题
			// 做非空判断
			if (null == title) {
				continue;
			}
			param = UploadUtils.getParam(i, params);// 获取标题行数对应内容
			switch (title) {
			case MANUFACTURER:
				manufacturer = getStandBrandName(param, brandMap)+MANUFACTURER;
				break;
			case MANUFACTURER_PART_NUMBER:
				manufacturerPartNumber = param+MANUFACTURER_PART_NUMBER;
				break;
			default:
				break;
			}
		}
		return (manufacturer == null ? "" : manufacturer) + manufacturerPartNumber;
	}

	static String getStandBrandName(String param, Map<String, ProductBrand> brandMap) {
		String manufacturer = param;
		ProductBrand brand = brandMap.get(null == manufacturer ? "" : manufacturer.trim().toUpperCase());
		if (brand != null) {
			manufacturer = brand.getBrandName();
		}
		return manufacturer;
	}
}