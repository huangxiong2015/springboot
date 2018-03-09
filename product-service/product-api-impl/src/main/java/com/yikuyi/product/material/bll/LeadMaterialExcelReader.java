package com.yikuyi.product.material.bll;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.ictrade.tools.excel.IRowReader;
import com.ictrade.tools.leadin.LeadInFactorySax;
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.vo.DocumentLogVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.template.model.ProductTemplate;
import com.yikuyi.template.model.ProductTemplate.TemplateType;
import com.ykyframework.exception.SystemException;

/**
 * 导入库存的excel解析
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
public class LeadMaterialExcelReader {
	/**
	 * 从文件读取的数据集合
	 */
	private List<String[]> datas;
	
	/**
	 * 构造方法
	 */
	public LeadMaterialExcelReader(){
		datas = new ArrayList<>();
	}
	
	/**
	 * 通过文件读取
	 * @param fileUrl 文件url
	 * @return 读取的数据
	 * @throws SystemException
	 * @since 2016年5月31日
	 * @author zr.tongkun@yikuyi.com
	 */
	public int readByFile(String fileUrl,ProductTemplate emTemplate,MaterialManager materialManager,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException{
		File attFile = new File(fileUrl);//取得文件
		if(!attFile.exists()){
			throw new SystemException("文件不存在");
		}
		Reader reader = new Reader(emTemplate,datas,materialManager,materialVo,minLineNo,quickFindKeyMap,brandMap,facilityMap,documentLogVo,isLastLine);
		LeadInFactorySax.createProcess(reader,attFile,null);
		String error = reader.getErrorMsg();
		int dataSize = reader.getDataSize();
		int countCacheListTotal = reader.getCountCacheListTotal();
		if(error!=null){
			throw new SystemException(error);
		}
		/**
		 * 修改文档状态等
		 */
		materialManager.handleDocStatus(countCacheListTotal, materialVo);
		return dataSize;
	}
	
	/**
	 * 通过文件读取
	 * @param fileUrl 文件url
	 * @return 读取的数据
	 * @throws SystemException
	 * @since 2016年5月31日
	 * @author zr.tongkun@yikuyi.com
	 */
	public int readByFile(InputStream fileStream,ProductTemplate emTemplate,MaterialManager materialManager,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine) throws SystemException{
		Reader reader = new Reader(emTemplate,datas,materialManager,materialVo,minLineNo,quickFindKeyMap,brandMap,facilityMap,documentLogVo,isLastLine);
		LeadInFactorySax.createCsvStreamProcess(reader, fileStream);
		String error = reader.getErrorMsg();
		int dataSize = reader.getDataSize();
		int countCacheListTotal = reader.getCountCacheListTotal();
		if(error!=null){
			throw new SystemException(error);
		}
		/**
		 * 修改文档状态等
		 */
		materialManager.handleDocStatus(countCacheListTotal, materialVo);
		return dataSize;
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
	
	/**
	 * 当前sheet页总行数
	 */
	private int maxRowNum = 0;
	
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
	private List<String[]> datas = null;
	
	private MaterialManager materialManager;
	
	private MaterialVo materialVo;
	
	private int minLineNo; 
	
	private Map<String, Integer> quickFindKeyMap; 
	
	private Map<String, ProductBrand> brandMap;
	
	private Map<String,String> facilityMap;
	
	private DocumentLogVo documentLogVo;
	
	private String isLastLine;
	
	private static final int BATCH_SIZE = 1000;
	
	/**
	 * 构造函数
	 * @param emTemplate 模板对象
	 */
	public Reader(ProductTemplate emTemplate,List<String[]> datas,MaterialManager materialManager,MaterialVo materialVo,
			int minLineNo,Map<String, Integer> quickFindKeyMap,Map<String, ProductBrand> brandMap,
			Map<String,String> facilityMap,DocumentLogVo documentLogVo,String isLastLine){
		this.emTemplate = emTemplate;
		this.datas = datas;
		this.materialManager = materialManager;
		this.materialVo = materialVo;
		this.minLineNo = minLineNo;
		this.quickFindKeyMap = quickFindKeyMap;
		this.brandMap = brandMap;
		this.facilityMap = facilityMap;
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
	
	public int getDataSize(){
		return rowNum;
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
	public void getRows(int sheetIndex ,String sheetName, int curRow, List<String> row) {
		if(breakRead){
			return;//如果中断读取了则不继续读取
		}
		//读取列头
		if(rowNum<=1 && emTemplate.getTitles()==null && getTitles(row)){
			materialManager.updateDocInfo(materialVo.getDocId(), emTemplate.getShowTitles());
			return;
		}
		if(CollectionUtils.isNotEmpty(row)){
			//加入数据
			datas.add(row.toArray(new String[]{}));
			rowNum++;//行数增加
		}
		if(BATCH_SIZE == datas.size() || CollectionUtils.isEmpty(row) || curRow == maxRowNum-1){
			if(CollectionUtils.isEmpty(row) || curRow == maxRowNum-1){
				isLastLine = "true";
			}
			/**
			 * 判断是否已经取消
			 */
			if(!materialManager.isCancelImport(materialVo.getDocId())){
				if(count>0){
        			minLineNo = BATCH_SIZE*count;
        		}
				materialManager.writeDataToMongodbByFtp(datas, emTemplate, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);
				countCacheListTotal += documentLogVo.getCountCacheList();
				datas.clear();
				count++;
			}
			if(isBlankRow(row)){
				return;//如果是空行则不读取
			}
		}
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
	private boolean getTitles(List<String> row) {
		boolean result = false;// 结果
		String[] titles = new String[row.size()];
		String[] showTitles = new String[row.size()];
		boolean haveTitle = false;// 是否是标题
		for (int i = 0; i < row.size(); i++) {
			//非空判断
			String cell = null == row.get(i) ? null : row.get(i).trim().replaceAll("\\*", "");
			String data = row.get(i);
			
			if (data != null) {
				cell = data.trim().replaceAll("\\*", "").replaceAll("\\.", "");
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
		emTemplate.setTitles(titles);
		emTemplate.setShowTitles(showTitles);
		// 返回结果
		return result;
	}
	
	//是否有必填字段没有
	private boolean haveNotEmptyTitle(String[] titles){
		boolean haveNotEmptyTitle;
		for(String notEmptyTitle : emTemplate.getNotEmptyList()) {
			haveNotEmptyTitle = false;
			// 查找是否有必填列
			mark:
			for (String title : titles) {
					String[] titleArr = title.split(",");
					for(String temp : titleArr){
						if (notEmptyTitle.equals(temp)) {
							haveNotEmptyTitle = true;
							break mark;
						}
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
	
	public ProductTemplate getTitlesNew(List<String> row) {
		boolean result = false;// 结果
		String[] titles = new String[row.size()];
		String[] showTitles = new String[row.size()];
		boolean haveTitle = false;// 是否是标题
		for (int i = 0; i < row.size(); i++) {
			//非空判断
			String cell = null == row.get(i) ? null : row.get(i).trim().replaceAll("\\*", "");
			String data = row.get(i);
			
			if (data != null) {
				cell = data.trim().replaceAll("\\*", "").replaceAll("\\.", "");
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
		emTemplate.setTitles(titles);
		emTemplate.setShowTitles(showTitles);
		// 返回结果
		return emTemplate;
	}
	
	@Override
	public void setMaxNum(int sheetIndex, int num) {
		this.maxRowNum =  num;
	}
}