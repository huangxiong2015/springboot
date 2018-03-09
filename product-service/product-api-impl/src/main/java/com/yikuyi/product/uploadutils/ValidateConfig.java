package com.yikuyi.product.uploadutils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yikuyi.template.model.ProductTemplate.TemplaterValieKey;

/**
 * 校验配置
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@Component
public final class ValidateConfig {
	
	/**
	 * 属性默认最大长度
	 */
	private static final String DEFAULT_ENTITY = "DEFAULT";
	private static final int DEFAULT_ENTITY_MAX_LENGTH = 1000;
	
	private static final Map<String, ValidateClass> VALIDATE_HELP_MAP = new HashMap<>(12);
	
	
	private ValidateConfig(){
		
	}
	
	static{
		//设置的验证规则
		VALIDATE_HELP_MAP.put(TemplaterValieKey.IS_NUMBER.getValue(), new ValidateClass(ValidateClass.IS_NUMBER_STR,ValidateClass.Rules::isNumber));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.NOT_EMPTY.getValue(), new ValidateClass(ValidateClass.NOT_EMPTY_STR,ValidateClass.Rules::notEmpty));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MORE_THAN_ZERO.getValue(), new ValidateClass(ValidateClass.MORE_THANZERO_STR, ValidateClass.Rules::moreThanZero));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MORE_THAN_ZERO_INT.getValue(), new ValidateClass(ValidateClass.MORE_THANZEROINT_STR, ValidateClass.Rules::moreThanZeroInt));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MORE_THAN_ZERO_AND_ZERO.getValue(), new ValidateClass(ValidateClass.MORE_THANZEROINTANDZERO_STR, ValidateClass.Rules::moreThanZeroAndZero));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MORE_THAN_MINUSONE_AND_MINUSONE.getValue(), new ValidateClass(ValidateClass.MORE_THANMINUSONEANDMINUSONE_STR, ValidateClass.Rules::moreThanMinusOneAndMinusOne));
		
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MIN.getValue(), new ValidateClass(ValidateClass.MIN_STR, ValidateClass.Rules::min));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MAX.getValue(), new ValidateClass(ValidateClass.MAX_STR, ValidateClass.Rules::max));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MIN_NUM.getValue(), new ValidateClass(ValidateClass.MINNUM_STR, ValidateClass.Rules::minNum));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.MAX_NUM.getValue(), new ValidateClass(ValidateClass.MAXNUM_STR, ValidateClass.Rules::maxNum));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.QUALIFY.getValue(), new ValidateClass(ValidateClass.QUALIFY_STR, ValidateClass.Rules::qualify));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.REGEX.getValue(), new ValidateClass(ValidateClass.REXGEX_STR, ValidateClass.Rules::regex));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.VALIDAT_EEXPIRY_DATE.getValue(), new ValidateClass(ValidateClass.VALIDAT_EEXPIRY_DATE_STR, ValidateClass.Rules::validateExpiryDate));
		VALIDATE_HELP_MAP.put(TemplaterValieKey.VALIDATE_DATE_FORMAT.getValue(), new ValidateClass(ValidateClass.VALIDAT_DATE_FORMAT_STR, ValidateClass.Rules::validateDateFormat));
		VALIDATE_HELP_MAP.put("DEFAULT", new ValidateClass(ValidateClass.DEFAULTRULES_STR, ValidateClass.Rules::defaultRules));
	}
	
	/**
	 * 配置校验规则
	 * @param title
	 * @param showTitle
	 * @param param
	 * @param validateMap
	 * @return
	 * @since 2017年1月5日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private String validateByConfig(String title, String showTitle, String param, Map<String, String> validateMap){
		String[] validateKeys = validateMap.get(title).split(",");
		for (String validateKey : validateKeys) {
			String keyStr = validateKey.split("=")[0];
			if(VALIDATE_HELP_MAP.containsKey(keyStr) && !VALIDATE_HELP_MAP.get(keyStr).getRst(param,validateKey)){
				return VALIDATE_HELP_MAP.get(keyStr).getMsg(showTitle,validateKey);
			}
		}
		return null;
	}
	
	/**
	 * 默认规则
	 * @param showTitle
	 * @param param
	 * @return
	 * @since 2017年1月5日
	 * @author zr.shuzuo@yikuyi.com
	 */
	private String validateByDefault(String showTitle, String param){
		if(VALIDATE_HELP_MAP.containsKey(DEFAULT_ENTITY) && !VALIDATE_HELP_MAP.get(DEFAULT_ENTITY).getRst(param,null)){
			return VALIDATE_HELP_MAP.get(DEFAULT_ENTITY).getMsg(showTitle,DEFAULT_ENTITY_MAX_LENGTH);
		}
		return null;
	}
	
	/**
	 * 验证字段的有效性 每个字段的验证方式定义在json常量中
	 * <p>json数据格式如下,IsNumber,Min=0,Max=15,MinNum=0,MaxNum=10000000</p>
	 * 
	 * @param title 对应的数据库字段
	 * @param showTitle 用户输入的列头
	 * @param param 用户输入的值
	 * @return 错误消息，如果没有错误则返回null
	 * @since 2016年3月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	public final String validate(String title, String showTitle, String param, Map<String, String> validateMap) {
		//如果没有验证方式，使用默认的验证方式
		if(validateMap.containsKey(title)){
			return validateByConfig(title, showTitle, param, validateMap);
		}else{
			if(validateMap.containsKey("basisMaterielValidate")){
				return validateByConfig("basisMaterielValidate",showTitle,param,validateMap);
			}
			return validateByDefault(showTitle, param);
		}
	}
}