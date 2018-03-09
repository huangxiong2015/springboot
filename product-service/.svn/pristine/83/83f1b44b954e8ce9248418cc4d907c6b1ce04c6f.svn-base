/*
 * Created: 2017年8月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.manager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ictrade.tools.export.CsvFilePrinter;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductPrice;
import com.yikuyi.product.model.ProductPriceLevel;
import com.yikuyi.product.model.ProductStand;
import com.ykyframework.exception.BusinessException;

@Service
public class ExcelExportManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelExportManager.class);

	
	/**
	 * 解析数据
	 * @param standAudits
	 * @since 2017年8月18日
	 * @author injor.huang@yikuyi.com
	 */
	public List<List<String>> parseMaterialData(List<ProductStand> stands)throws BusinessException{
		List<List<String>> rowDataList = Lists.newArrayList();
		stands.forEach(stand -> {
			//按照顺序导出excel标题字段
			List<String> rowData = Lists.newArrayList();
			List<ProductCategory> categories = stand.getCategories();
			 categories.forEach(cate -> {
				 String cateName = cate.getName();
				 if(cate.getLevel() == 1){
					//大类
					rowData.add(StringUtils.defaultIfBlank(cateName, "未分类"));
				 }else if(cate.getLevel() == 2){
					//小类
					rowData.add(StringUtils.defaultIfBlank(cateName, "未分类"));
				 }else if(cate.getLevel() == 3){
					//次小类
					rowData.add(StringUtils.defaultIfBlank(cateName, "未分类"));
				 }
			 });
			
			//品牌 StringEscapeUtils.escapeCsv(stand.getManufacturer())解决符号问题
			rowData.add(StringUtils.defaultIfBlank(stand.getManufacturer(), ""));
			//型号
			rowData.add(StringUtils.defaultIfBlank(stand.getManufacturerPartNumber(), ""));
			//描述
			rowData.add(StringUtils.defaultIfBlank(StringUtils.replace(stand.getDescription(), "\"", "”"), ""));
			//Rohs
			rowData.add(StringUtils.defaultIfBlank(ProductStand.Rohs.getName(stand.getRohs()),""));
			//状态 没有值就是有效，因为之前的老数据是没有刷状态的，默认都是有效
			rowData.add(StringUtils.defaultIfBlank(ProductStand.Status.getName(stand.getStatus()),"有效"));
			//限制物料 没有值就是否，因为之前的老数据是没有刷状态的，默认都是非限制物料
			rowData.add(StringUtils.defaultIfBlank(ProductStand.RestrictMaterialType.getName(stand.getRestrictMaterialType()),"否"));
			//跟更新时间
			rowData.add(StringUtils.isNotBlank(stand.getUpdatedTimeMillis())?DateFormatUtils.format(Long.valueOf(stand.getUpdatedTimeMillis()), "yyyy-MM-dd HH:mm:ss"):"");
			rowDataList.add(rowData);
		});
		return rowDataList;
	}
	
	/**
	 * 解析出来的数据
	 * @param products
	 * @return
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
	private List<List<String>> parseProductData(List<Product> products)throws BusinessException  {
		List<List<String>> rowDataList = Lists.newArrayList();
		long time = System.currentTimeMillis();
		products.forEach(prodct -> {
			//按照顺序导出excel标题字段
			List<String> rowData = Lists.newArrayList();
//			*ManufacturerName（制造商）
			rowData.add(StringUtils.defaultIfBlank(prodct.getSpu().getManufacturer(), ""));
//			*MPN（型号）
			rowData.add(StringUtils.defaultIfBlank(prodct.getSpu().getManufacturerPartNumber(), ""));
//			SPN（供应商型号）
			rowData.add(StringUtils.defaultIfBlank(prodct.getSkuId(), ""));
//			*STOCK QTY（库存）
			rowData.add(StringUtils.defaultIfBlank(this.parseNumber(prodct.getQty()), ""));
//			*MOQ（最小起订量）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMinimumQuantity(), "")));
//			UNIT（单位）
			rowData.add(StringUtils.defaultIfBlank(prodct.getUnit(), ""));
//			DateCode（批号）
			rowData.add(StringUtils.defaultIfBlank(prodct.getDateCode(), ""));
//			SPQ（标准包装数量）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getSpq(), "")));
//			MOV（最小订单金额）
			rowData.add(StringUtils.defaultIfBlank(prodct.getMov(), ""));
//			Packaging（包装）
			rowData.add(StringUtils.defaultIfBlank(prodct.getPackaging(), ""));
//			*Currency（币种）
			rowData.add(StringUtils.defaultIfBlank(prodct.getCurrencyCode(), ""));
//			Description（描述）
			rowData.add(StringUtils.defaultIfBlank( StringUtils.replace(prodct.getRemark(), "\"", "”"), ""));
			//阶梯价格
			this.setProductPriceLevel(prodct.getPrices(), rowData);
			
//			ROHS（Y/N）
			rowData.add(StringUtils.defaultIfBlank(ProductStand.Rohs.getName(prodct.getSpu().getRohs()) , ""));
//			MinLeadTime-ML（内地最小交期/天数）
			rowData.add(String.valueOf( ObjectUtils.defaultIfNull(prodct.getMinLeadTimeML(), "")));
//			MaxLeadTime-ML（内地最大交期/天数）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMaxLeadTimeML(), "")));
//			MinLeadTime-HK（香港最小交期/天数）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMinLeadTimeHK(), "")));
//			MaxLeadTime-HK（香港最大交期/天数）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMaxLeadTimeHK(), "")));
//			FactoryLeadTime-ML（内地最小工厂交期/周）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMinFactoryLeadTimeML(), "")));
//			FactoryLeadTime-ML（内地最大工厂交期/周）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMaxFactoryLeadTimeML(), "")));
//			FactoryLeadTime-HK（香港最小工厂交期/周）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMinFactoryLeadTimeHK(), "")));
//			FactoryLeadTime-HK（香港最大工厂交期/周）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getMaxFactoryLeadTimeHK(), "")));
			//上传时间
			String updatedTimeMillis = prodct.getUpdatedTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(StringUtils.isNotEmpty(updatedTimeMillis)){
				rowData.add(String.valueOf(sdf.format(new Date(Long.valueOf(updatedTimeMillis)))));
			}else{
				rowData.add("");
			}
			//数据类型
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(prodct.getCostType(), "")));
			//失效日期
			if(Optional.ofNullable(prodct.getExpiryDate()).isPresent()){
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				rowData.add(String.valueOf(sdf.format(new Date(Long.valueOf(prodct.getExpiryDate())))));
			}else{
				rowData.add("");
			}
			rowDataList.add(rowData);
		});
		logger.info("数据拼装,耗时:{}",(System.currentTimeMillis()-time));
		return rowDataList;
	}
	/**
	 * 阶梯价格
	 * @param prices
	 * @param rowData
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
	public void setProductPriceLevel(List<ProductPrice> prices,List<String> rowData){
		int size = 10;//价格阶梯默认有10个
		//剩下需要补充的空白单元格,*2是因为每次循环都要补充两个字段,默认20列
		int cells = size * 2;
		if(CollectionUtils.isEmpty(prices)){
			//补充剩下的补充单元格
			this.ComplementaryCells(cells, rowData);
			return;
		}
		//价格截阶梯，有美元优先美元
		List<ProductPriceLevel> priceLevels = Lists.newArrayList();
		for (ProductPrice price : prices) {
			List<ProductPriceLevel> list= price.getPriceLevels();
			if(CollectionUtils.isEmpty(list)){
				continue;
			}
			if(StringUtils.equalsIgnoreCase(price.getCurrencyCode(), "USD")){
				priceLevels = Lists.newArrayList();
				priceLevels.addAll(list);
				break;
			}
			priceLevels.addAll(list);
		}
		
		if(CollectionUtils.isEmpty(priceLevels)){
			//补充剩下的补充单元格
			this.ComplementaryCells(cells, rowData);
			return;
		}
		for (ProductPriceLevel priceLevel : priceLevels) {
			//*QtyBreak1（数量区间）
			//*PriceBreak1（价格区间）
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(priceLevel.getBreakQuantity(), "")));
			rowData.add(String.valueOf(ObjectUtils.defaultIfNull(priceLevel.getPrice(), "")));
		}
		//剩下需要补充的空白单元格,*2是因为每次循环都要补充两个字段
		cells = (size-priceLevels.size()) * 2;
		this.ComplementaryCells(cells, rowData);
	}
	/**
	 * 补充单元格
	 * @param cells
	 * @param rowData
	 * @since 2017年9月1日
	 * @author injor.huang@yikuyi.com
	 */
	public void ComplementaryCells( int cells ,List<String> rowData){
		for (int i = 0; i < cells; i++) {
			rowData.add("");
		}
	}

	/**
	 * 导出excel
	 * @param rowDataList
	 * @param fileName
	 * @return
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
//	public File exrpotExcel(List<List<String>> rowDataList,String fileName,String template){
//		ExportProcesser processer= null;
//		File excelFile = FileUtils.getFile(fileName);
//		FileOutputStream fos = null;
//		try{
//			long time = System.currentTimeMillis();
//			logger.info("写excel开始时间："+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//			fos = new FileOutputStream(excelFile);
//			processer = ExportFactory.getProcesserByOutputStream(ExportProcesserXls.FILE_TYPE,fos);
//			//标题
//	        processer.writeLine("Sheet1",template.split(","));
//	        //标题
//	        processer.writeLine("Sheet2", template.split(","));
//	        //标题
//	        processer.writeLine("Sheet3", template.split(","));
//	        //标题
//	        processer.writeLine("Sheet4", template.split(","));
//            int sheetNum = 1;
//            for(int i=0;i<rowDataList.size();i++){
//            	//因为老版excel最大条数是65536，所以默认一个sheet 50000行
//            	processer.writeLine("Sheet"+(sheetNum+i/EXCEL_MAX_ROW_NUM), rowDataList.get(i));
//            }
//            logger.info("写excel,耗时:"+(System.currentTimeMillis()-time));
//			processer.output();
//			return excelFile;
//		}catch(Exception e){
//			logger.error("导出excel异常，{}",e);
//		}finally {
//			long time = System.currentTimeMillis();
//			IOUtils.closeQuietly(processer);
//			IOUtils.closeQuietly(fos);
//			logger.info("excel写完关闭流,耗时:"+(System.currentTimeMillis()-time));
//			
//		}
//		return null;
//	}
	
	/**
	 * 导出excel
	 * @param rowDataList
	 * @param fileName
	 * @return
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
//	public File exrpotCsv(List<List<String>> rowDataList,String fileName,String template){
//		try{
//			File excelFile = FileUtils.getFile(fileName);
//			long time = System.currentTimeMillis();
//	        List<List<String>> datas = Lists.newArrayList();
//	        datas.add(Arrays.asList(template.split(",")));//标题
//	        datas.addAll(rowDataList);//数据
//            CSVUtils.exportCsv(excelFile, datas);
//            logger.info("写csv,耗时:"+(System.currentTimeMillis()-time));
//			return excelFile;
//		}catch(Exception e){
//			logger.error("导出csv异常，{}",e);
//		}
//		return null;
//	}
	/**
	 *  10000000 
	 * @param value
	 * @return 10,000,000
	 * @since 2017年8月31日
	 * @author injor.huang@yikuyi.com
	 */
	public String parseNumber(Long value) {
		if(value == null){
			return null;
		}
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(value);
	}
	
	/**
	 * 写数据
	 * @param stands
	 * @param filePrinter
	 * @since 2017年9月25日
	 * @author injor.huang@yikuyi.com
	 */
	public void writeMaterialData(List<ProductStand> stands,CsvFilePrinter filePrinter)throws BusinessException{
		//数据解析
		List<List<String>> lists = this.parseMaterialData(stands);
		lists.forEach(a -> {
			try {
				filePrinter.write(a);
			} catch (Exception e) {
				logger.error("写csv文件异常：{}",e);
			}
		});
	}
	
	/**
	 *  写数据
	 * @param products
	 * @param filePrinter
	 * @since 2017年9月25日
	 * @author injor.huang@yikuyi.com
	 */
	public void writeProductData(List<Product> products,CsvFilePrinter filePrinter)throws BusinessException {
		//数据解析
		List<List<String>> lists = this.parseProductData(products);
		lists.forEach(a -> {
			try {
				filePrinter.write(a);
			} catch (Exception e) {
				logger.error("写csv文件异常：{}",e);
			}
		});
	}
	
}
