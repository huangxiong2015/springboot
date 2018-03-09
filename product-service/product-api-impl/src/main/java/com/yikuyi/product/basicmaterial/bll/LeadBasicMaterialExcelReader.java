package com.yikuyi.product.basicmaterial.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.excel.IRowReader;
import com.ictrade.tools.leadin.LeadInFactorySax;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.model.DocumentTitle;
import com.yikuyi.document.vo.DocumentLogVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.material.bll.LeadMaterialParser;
import com.yikuyi.template.model.ProductTemplate;
import com.yikuyi.template.model.ProductTemplate.TemplateType;
import com.ykyframework.exception.SystemException;

/**
 * 导入库存的excel解析
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public class LeadBasicMaterialExcelReader {
	private static final Logger logger = LoggerFactory.getLogger(LeadBasicMaterialExcelReader.class);
	
	private int count = 0;
	
	/**
	 * 从文件读取的数据集合
	 */
	private Map<String, List<String[]>> datas;
	
	/**
	 * 构造方法
	 */
	public LeadBasicMaterialExcelReader(){
		datas = new LinkedHashMap<>();
	}
	
	/**
	 * 通过文件读取
	 * @param fileUrl 文件url
	 * @return 读取的数据
	 * @throws SystemException
	 * @since 2016年5月31日
	 * @author zr.tongkun@yikuyi.com
	 */
	public Map<String, List<String[]>> readByFile(String fileUrl,ProductTemplate emTemplate,BasicMaterialManager basicMaterialManager,MaterialVo materialVo,
			int minLineNo,Map<String, String> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			DocumentLogVo documentLogVo,String isLastLine) throws SystemException{
		File attFile = new File(fileUrl);//取得文件
		if(!attFile.exists()){
			throw new SystemException("文件不存在");
		}
		Reader reader = new Reader(emTemplate,datas, basicMaterialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, documentLogVo, isLastLine);
		LeadInFactorySax.createProcess(reader,attFile,null);
		String error = reader.getErrorMsg();
		int countCacheListTotal = reader.getCountCacheListTotal();
		if(error!=null){
			throw new SystemException(error);
		}
		/**
		 * 修改文档状态等
		 */
		basicMaterialManager.handleDocStatus(countCacheListTotal, materialVo);
		count = reader.getRowNum();
//		for(Entry<String,List<String[]>> entry:datas.entrySet()){
//			for(String[] row:entry.getValue()){
//				StringBuilder rowStr = new StringBuilder();
//				for(String value:row){
//					rowStr.append(value).append("   ");
//				}
//				logger.info("读取到内容："+rowStr);
//			}
//		}
		return datas;
	}

	public int getCount() {
		return count;
	}
	
}

/**
 * 读取内部类，用户业务处理
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.1
 */
class Reader implements IRowReader{
	
	/**
	 * 是否已经打断读取
	 */
	private boolean breakRead = false;
	
	/**
	 * 当前的读取行数
	 */
	private int rowNum = 0;
	
	private int count = 0;	
	
	private int countCacheListTotal = 0;
	
	/**
	 * 模板对象
	 */
	private ProductTemplate emTemplate;
	
	/**
	 * 读取时的错误信息
	 */
	private String errorMsg = null;
	
	/**
	 * 读取到的数据
	 */
	private Map<String, List<String[]>> datas = null;
	
	private int lastSheetIndex = -1;
	
	private static final int BATCH_SIZE = 1000;
	
	private String isLastLine;
	
	private BasicMaterialManager basicMaterialManager;
	
	private MaterialVo materialVo;
	
	private int minLineNo;
	
	private Map<String, String> quickFindKeyMap;
	
	private Map<String, ProductBrand> brandMap;
	
	private DocumentLogVo documentLogVo;
	
	/**
	 * 构造函数
	 * @param emTemplate 模板对象
	 */
	public Reader(ProductTemplate emTemplate,Map<String, List<String[]>> datas,BasicMaterialManager basicMaterialManager,MaterialVo materialVo,
			int minLineNo,Map<String, String> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			DocumentLogVo documentLogVo,String isLastLine){
		this.emTemplate = emTemplate;
		this.datas = datas;
		this.basicMaterialManager = basicMaterialManager;
		this.materialVo = materialVo;
		this.minLineNo = minLineNo;
		this.quickFindKeyMap = quickFindKeyMap;
		this.brandMap = brandMap;
		this.documentLogVo = documentLogVo;
		this.isLastLine = isLastLine;
	}
	
	/**
	 * 获取错误信息
	 * @return 错误信息
	 * @since 2016年6月1日
	 * @author zr.tongkun@yikuyi.com
	 */
	public String getErrorMsg(){
		return errorMsg;
	}
	
	public int getCountCacheListTotal(){
		return countCacheListTotal;
	}
	
	/**
	 * 是否是空行
	 * @param row 行数据
	 * @return 如果是空行返回true，非空行返回false
	 * @since 2016年6月1日
	 * @author zr.tongkun@yikuyi.com
	 */
	private boolean isBlankRow(List<String> row){
		if(CollectionUtils.isNotEmpty(row)){
			for(String cell:row){
				if(StringUtils.isNotBlank(cell)){
					return false;
				}
			}
		}
		return true;
	}
	
	/* 
	 * 读取方法，由poi自动调用
	 * (non-Javadoc)
	 * @see com.ictrade.common.utils.excel.IRowReader#getRows(int, int, java.util.List)
	 */
	@Override
	public void getRows(int sheetIndex, String sheetName, int curRow, List<String> row) {
		if(breakRead){
			return;//如果中断读取了则不继续读取
		}
		String mapKey = sheetName + LeadMaterialParser.VALUE_SEPARATE + sheetIndex;
		if(null!=datas.get(mapKey) && (BATCH_SIZE == datas.get(mapKey).size() || (isBlankRow(row) && datas.get(mapKey).size()>0))){
			if(isBlankRow(row)){
				isLastLine = "true";
			}
			if(count>0){
    			minLineNo = BATCH_SIZE*count;
    		}
			basicMaterialManager.writeDataToMongodb(datas, emTemplate, materialVo, minLineNo, quickFindKeyMap, brandMap, documentLogVo, isLastLine);
			countCacheListTotal += documentLogVo.getCountCacheList();
			datas.clear();
			count++;
		}
		if(isBlankRow(row)){
			return;//如果是空行则不读取
		}
		//读取列头
		if(lastSheetIndex != sheetIndex && !emTemplate.getMoreSheetTitles().containsKey(mapKey) && getTitles(mapKey,row)){
			lastSheetIndex++;
			return;
		}
		//加入数据
		if(!datas.containsKey(mapKey)){
			datas.put(mapKey, new ArrayList<>());
		}
		datas.get(mapKey).add(row.toArray(new String[]{}));
		
		rowNum++;//行数增加
	}
	
	/**
	 * 由于上传数据中带有列头，而且列头会存在于前两行，所以如果当前读取到了第1行或第二行，要先判断是否是列头
	 * 根据所有整理过的上传格式，穷举出所有可能出现的列头，并以json的形式配置在常量中：DEFAULT_TITLE_JSON
	 * 判断方法：将这一行所有数据与DEFAULT_TITLE_JSON里的值进行比较，如果有一个或以上的数据在json中存在，这认为这一行就是列头
	 * 如果前两行都不是列头，则使用常量DEFAULT_TITLES来作为列头，因此其顺序和内容与页面上下载的模板是一致的
	 * @param row 行数据
	 * @return 是否是标题行
	 * @since 2016年6月1日
	 * @author zr.tongkun@yikuyi.com
	 */
	private boolean getTitles(String mapKey , List<String> row) {
		boolean result = false;// 结果
		String[] titles = new String[row.size()];
		String[] showTitles = new String[row.size()];
		boolean haveTitle = false;// 是否是标题
		for (int i = 0; i < row.size(); i++) {
			//非空判断
			String cell = null == row.get(i) ? null : row.get(i).trim().replaceAll("\\*", "");
			String data = row.get(i);
			
			if (data != null) {
				cell = data.trim().replaceAll("\\*", "");
			}
			showTitles[i] = cell;
			// 从配置中取出通用列名
			String title = emTemplate.getConfigMap(TemplateType.DEFAULT_TITLE).get(cell);
			// 如果有对应的
			if (title != null) {
				haveTitle = true;
				titles[i] = title;
			} else {// 如果没有对应的
				titles[i] = cell;
			}
		}
		// 如果已经找到标题
		if (haveTitle && !haveNotEmptyTitle(titles)) {
			result = true;
		} else {// 如果不是标题则使用默认标题
			titles = emTemplate.getConfigMap(TemplateType.DEFAULT_TITLE).keySet().toArray(titles);
			showTitles = emTemplate.getConfigMap(TemplateType.SHOW_TITLE).keySet().toArray(showTitles);
		}
		// 设置值
		emTemplate.getMoreSheetTitles().put(mapKey, titles);
		emTemplate.getMoreSheetShowTitles().put(mapKey, showTitles);
		basicMaterialManager.addTitlesToRedis(materialVo.getDocId(), emTemplate.getMoreSheetShowTitles());
		// 返回结果
		return result;
	}
	
	//是否有必填字段没有
	private boolean haveNotEmptyTitle(String[] titles){
		boolean haveNotEmptyTitle;
		for(String notEmptyTitle : emTemplate.getNotEmptyList()) {
			haveNotEmptyTitle = false;
			// 查找是否有必填列
			for (String title : titles) {
				if (notEmptyTitle.equals(title)) {
					haveNotEmptyTitle = true;
					break;
				}
			}
			// 如果必填列在文件中没有找到，则报错
			if (!haveNotEmptyTitle) {
				breakRead = true;
				errorMsg = "文件内容不正确，缺少必要的字段：" + emTemplate.getConfigMap(TemplateType.SHOW_TITLE).get(notEmptyTitle);
			}
		}
		return breakRead;
	}

	public int getRowNum() {
		return rowNum;
	}
}