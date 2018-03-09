/*
 * Created: 2017年3月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.common.utils;
/**
 * 
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
public class BusiErrorCode {
	/**
	 * 已经存在发布，或者推荐的，不能再发布
	 */
	public static final String EXIST_OTHER_PUBLISH="EXIST_OTHER_PUBLISH";
	
	/**
	 * 制造商名称已存在，不能再新增
	 */
	public static final String BRANDNAME_EXIST = "BRANDNAME_EXIST";
	
	/**
	 * 制造商别名已存在，不能再新增
	 */
	public static final String BRANDALIAS_EXIST = "BRANDALIAS_EXIST";
	
	/**
	 * 公告咨询ID已存在，不能再新增
	 */
	public static final String NEWSID_EXIST = "NEWSID_EXIST";
	
	/**
	 * 分销商名已存在，不能重复增加
	 */
	public static final String EXIST_OTHER_DISTRIBUTORNAME = "EXIST_OTHER_NAME";
	
	/**
	 * 广告标题已存在，不能重复新增
	 */
	public static final String  ADVERTISEMENTTITLE_EXIST = "ADVERTISEMENTTITLE_EXIST";
	
	/**
	 * 活动结束时间是否小于当前时间
	 */
	public static final String  EXPIRYDATE_OUT = "EXPIRYDATE_OUT";
	
	/**
	 * 秒杀活动上传Excel，标题不正确
	 */
	public static final String TITLE_ERROR = "TITLE_ERROR";
	
	/**
	 * 秒杀活动上传Excel，数据重复
	 */
	public static final String REPEAT_ERROR = "REPEAT_ERROR";
	
	/**
	 * 活动时间错误
	 */
	public static final String DATE_ERROR = "DATE_ERROR";
	
	/**
	 * 活动不存在
	 */
	public static final String ACTIVITY_DOES_NOT_EXIST = "ACTIVITY_DOES_NOT_EXIST";
	
	/**
	 * 活动修改,更新缓存失败
	 */
	public static final String ACTIVITY_CACHE_MODIFY_ERROR = "ACTIVITY_CACHE_MODIFY_ERROR";

	/**
	 * 该mov的仓库和制造商已经存在
	 */
	public static final String SOURCE_BRAND_EXIST = "SOURCE_BRAND_EXIST";
	
	/**
	 * 上传的商品都是异常产品
	 */
	public static final String PRODUCT_ALL_ERROR = "PRODUCT_ALL_ERROR";
	
	/**
	 * 上传的商品都是异常产品
	 */
	public static final String PROMOTION_STATUS_NOT_ENABLE = "PROMOTION_STATUS_NOT_ENABLE";
	
	/**
	 * 创建方式不是模板创建
	 */
	public static final String CREATETYPE_NOT_TEMPLATE = "CREATETYPE_NOT_TEMPLATE";
	
	/**
	 * 活动没有选择优惠券
	 */
	public static final String PROMOTION_NOT_COUPON = "PROMOTION_NOT_COUPON";
	
	/**
	 * 型号信息不存在
	 */
	public static final String MANUFACTUREPARTNUMBER_LIST_NULL = "MANUFACTUREPARTNUMBER_LIST_NULL";
	
	/**
	 * 品牌信息不存在
	 */
	public static final String BRANDNAME_LIST_NULL = "BRANDNAME_LIST_NULL";
	
	/**
	 * 特殊spu已存在
	 */
	public static final String SPU_WHITE_LIST_EXIST = "SPU_WHITE_LIST_EXIST";
	
	/**
	 * 专属特价信息不能为空
	 */
	public static final String SPECIAL_OFFER_RULE_NULL = "SPECIAL_OFFER_RULE_NULL";
	
	/**
	 * 草稿商品信息不能为空
	 */
	public static final String SPECIAL_PRODUCT_DRAFT_NULL = "SPECIAL_PRODUCT_DRAFT_NULL";
	
	/**
	 * 文件不存在
	 */
	public static final String FILE_NULL = "FILE_NULL";
	
	/**
	 * 将商品信息从正式表迁移到草稿表抛异常
	 */
	public static final String COPY_PRODUCT_TO_DRAFT = "COPY_PRODUCT_TO_DRAFT";
	
	/**
	 * 将商品信息迁移到正式表报错
	 */
	public static final String COPY_PRODUCT_DRAFT = "COPY_PRODUCT_DRAFT";
	/**
	 * 草稿商品表中没有有效的数据
	 */
	public static final String SPECIAL_PRODUCT_ENABLE_NULL = "SPECIAL_PRODUCT_ENABLE_NULL";
	
	/**
	 * 非停用状态下，不能删除
	 */
	public static final String STRATEGY_STATUS_NOT_HOLD = "STRATEGY_STATUS_NOT_HOLD";
	
	/**
	 * 活动结束时间大于活动开始时间
	 */
	public static final String ENDDATE_BEFORE_STARTDATE = "ENDDATE_BEFORE_STARTDATE";
	
	/**
	 * 当前时间大于活动结束时间，不能启用
	 */
	public static final String ENDDATE_BEFORE_CURRENT = "ENDDATE_BEFORE_CURRENT";
	
	
}
