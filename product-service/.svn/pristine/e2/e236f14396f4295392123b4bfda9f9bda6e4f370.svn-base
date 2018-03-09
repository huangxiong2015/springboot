package com.yikuyi.template.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "product_template")
public class ProductTemplate {
	
	@Id
	@ApiModelProperty(value = "模版ID")
	private String id;

	@ApiModelProperty(value = "供应商名称")
	private String name;
	
	@ApiModelProperty(value = "编码")
	private String encode;
	
	@ApiModelProperty(value = "模版数据")
	private Map<String, Map<String,String>> template;
	
	public enum TemplateType {
		/**
		 * 默认标题(对应数据库字段)
		 */
		DEFAULT_TITLE("defaultTitle"),
		/**
		 * 数据库字段对应EXCEL名称
		 */
		SHOW_TITLE("showTitle"),
		/**
		 * 校验规则
		 */
		VALIDATE("validate");

		private final String value;

		TemplateType(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	public enum TemplaterValieKey{
		/**
		 * 不为空
		 */
		NOT_EMPTY("NotEmpty"),
		/**
		 * 最小长度
		 */
		MIN("Min"),
		/**
		 * 最大长度
		 */
		MAX("Max"),
		/**
		 * 必须是数字
		 */
		IS_NUMBER("IsNumber"),
		/**
		 * 最小数字
		 */
		MIN_NUM("MinNum"),
		/**
		 * 最大数字
		 */
		MAX_NUM("MaxNum"),
		/**
		 * 过滤数据为0的数据
		 */
		MORE_THAN_ZERO("MoreThanZero"),
		/**
		 * 过滤数据大于0的正整数数据
		 */
		MORE_THAN_ZERO_INT("MoreThanZeroInt"),
		/**
		 * 过滤数据大于等于0的正整数数据
		 */
		MORE_THAN_ZERO_AND_ZERO("MoreThanZeroAndZero"),
		/**
		 * 过滤大于等于-1的整数数据
		 */
		MORE_THAN_MINUSONE_AND_MINUSONE("MoreThanMinusOneAndMinusOne"),
		
		/**
		 * 过滤不满足正则表达式的数据
		 */
		REGEX("regex"),
		
		/**
		 * 校验失效日期
		 */
		VALIDAT_EEXPIRY_DATE("validateExpiryDate"),
		
		/**
		 * 校验日期格式
		 */
		VALIDATE_DATE_FORMAT("validateDateFormat"),
		
		
		/**
		 * 限定传入字符(XXX-YYY-ZZZ)
		 */
		QUALIFY("Qualify");
		
		private final String value;

		TemplaterValieKey(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * 各个列的标题
	 */
	private String[] titles = null;
	
	/**
	 * 各个列的标题
	 */
	private Map<String, String[]> moreSheetTitles = new HashMap<>();
	
	/**
	 * 各个列的显示标题
	 */
	private String[] showTitles = null;
	
	/**
	 * 各个列的标题
	 */
	private Map<String, String[]> moreSheetShowTitles = new HashMap<>();
	
	/**
	 * 非空列名
	 */
	private List<String> notEmptyList;
	
	
	public String[] getShowTitles() {
		return showTitles;
	}

	public void setShowTitles(String[] showTitles) {
		this.showTitles = showTitles;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	
	public Map<String, String[]> getMoreSheetTitles() {
		return moreSheetTitles;
	}

	public void setMoreSheetTitles(Map<String, String[]> moreSheetTitles) {
		this.moreSheetTitles = moreSheetTitles;
	}
	
	public Map<String, String[]> getMoreSheetShowTitles() {
		return moreSheetShowTitles;
	}

	public void setMoreSheetShowTitles(Map<String, String[]> moreSheetShowTitles) {
		this.moreSheetShowTitles = moreSheetShowTitles;
	}

	//根据模版内容的type,获取对应的defaultTitle,showTitle,validate的Map数据
	public Map<String, String> getConfigMap(TemplateType type){
		if(template==null){
			return Collections.emptyMap();
		}
		return template.get(type.getValue());
	}
	
	//获取validate设置的非空列名
	public List<String> getNotEmptyList(){
		if(notEmptyList==null){
			notEmptyList = new ArrayList<>();
			//算出不能为空的列
			for(Entry<String, String> entry : this.getConfigMap(TemplateType.VALIDATE).entrySet()){
				if(entry.getValue().indexOf(TemplaterValieKey.NOT_EMPTY.getValue()) >=0 ){
					this.notEmptyList.add(entry.getKey());
				}
			}
		}
		return this.notEmptyList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Map<String, String>> getTemplate() {
		return template;
	}

	public void setTemplate(Map<String, Map<String, String>> template) {
		this.template = template;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}
}