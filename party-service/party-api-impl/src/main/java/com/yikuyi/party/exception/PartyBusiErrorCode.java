/*
 * Created: 2017年6月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.exception;

public class PartyBusiErrorCode {

	/**
	 * 图片验证失败
	 */
	public static final String VERIFY_FAIL = "verifyFail";
	/**
	 * 邮箱为空
	 */
	public static final String EMAIL_EMPTY = "emailEmpty";
	/**
	 * 邮箱验证码为空
	 */
	public static final String CODE_EMPTY = "codeEmpty";
	/**
	 * 邮箱已经存在
	 */
	public static final String EMAIL_EXIST = "emailExist";
	/**
	 * 用户id或者账号为空
	 */
	public static final String PARTY_OR_ACCOUNT_EMPTY = "partyOrAccountEmpty";
	/**
	 * 手机为空
	 */
	public static final String TEL_EMPTY = "TelEmpty";
	/**
	 * 用户不存在
	 */
	public static final String PARTYID_NOT_EXIST = "partyIdNotExist";
	


	
	/**
	 * 没有添加邮箱
	 */
	public static final String ACCOUNT_SON_EMPTY = "empty";
	
	/**
	 * 当前账号不是主账号
	 */
	public static final String ACCOUNT_NOT_MAIN = "notMainAccount";
	
	
	/**
	 * 当前用户的企业id为空
	 */
	public static final String ACCOUNT_ENTID_EMPTY = "entIdEmpty";
	
	/**
	 * 加入的用户为主账号，主账号不能加入主账号
	 */
	public static final String ACCOUNT_MAIN_REJOINED = "accountMainJoined";
	
	/**
	 * 加入的用户已经是其他企业的子账号
	 */
	public static final String ACCOUNT_SON_REJOINED = "accountSonJoined";
	

	/**
	 * 已有同名供应商，请重新输入
	 */
	public static final String REPEAT_SUPPLIER = "repeatSupplier";
	
	/**
	 * 供应商编码必填
	 */
	public static final String SUPPLIERCODE_IS_NOT_EMPTY = "supplierCodeIsNotEmpty";
	

	/**
	 * menuId为空
	 */
	public static final String MENUID_EMPTY = "menuIdEmpty";
	
	/**
	 * roleIdList为空
	 */
	public static final String ROLEIDLIST_EMPTY = "roleIdListEmpty";
	
	
	/**
	 * 密码为空
	 */
	public static final String CIPCODE_EMPTY = "passwordEmpty";
	
	/**
	 * 原密码不存在
	 */
	public static final String OLDCIPCODE_NOT_EXIST = "oldPasswordNotExist";
	
	/**
	 * 用户输入密码与原密码不一致，请重新输入
	 */
	public static final String CIPCODE_NOT_SAME = "passwordNotSame";
	
	/**
	 * 修改密码失败
	 */
	public static final String CIPCODE_UPDATE_FAILED = "passwordUpdateFailed";
	

	/**
	 * 供应商全称被使用
	 */
	public static final String SUPPLIER_FULLNAME_USEED = "supplierFullNameUseed";
	
	/**
	 * 供应商不存在,不允许修改
	 */
	public static final String SUPPLIER_IS_EMPTY = "supplierIsEmpty";

	private PartyBusiErrorCode(){}
}
