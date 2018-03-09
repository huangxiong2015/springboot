package com.yikuyi.party.role.model;

/**
 * 角色类型
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
public enum RoleTypeEnum {
	/**
	 * 管理员
	 */
	ADMIN,
	/**
	 * 代理商
	 */
	AGENT,
	/**
	 * 采购员
	 */
	BUYER,
	/**
	 * 物流公司
	 */
	CARRIER,
	/**
	 * 公司
	 */
	CORPORATION,
	/**
	 * 客户
	 */
	CUSTOMER,
	/**
	 * 企业
	 */
	REGISTER,
	/**
	 * 分销商
	 */
	DISTRIBUTOR,
	/**
	 * 雇员
	 */
	EMPLOYEE,
	/**
	 * 企业客户
	 */
	ENTERPRISE_CUST,
	/**
	 * 个人客户
	 */
	INDIVIDUAL_CUST,
	/**
	 * 个人升级企业客户
	 */
	UPGRADE_CUST,
	/**
	 * 主账号
	 */
	MAIN_ROLE,
	/**
	 * 制造商
	 */
	MANUFACTURER,
	/**
	 * 运营代理
	 */
	OPERATION_AGENT,
	/**
	 * 运营人员
	 */
	OPERATION_REP,
	/**
	 * 订单处理专员
	 */
	ORDER_CLERK,
	/**
	 * 组织机构
	 */
	ORGANIZATION_ROLE,
	/**
	 * 商店
	 */
	PRODUCT_STORE,
	/**
	 * 销售代表
	 */
	SALES_REP,
	/**
	 * 发货专员
	 */
	SHIPMENT_CLERK,
	/**
	 * 供应商
	 */
	SUPPLIER,
	/**
	 * 销售员
	 */
	MARKETING_SPECAILIST,
	/**
	 * 营销经理
	 */
	MARKETING_MANAGER,
	/**
	 * 日志管理员
	 */
	LOG_ADMIN,
	/**
	 * 扩展专员
	 */
	B_EXTEN_SPECIALIST,
	/**
	 * 呼叫中心经理
	 */
	CALL_CENTER_MANAGER,
	/**
	 * 业务扩展部总监
	 */
	CBEO,
	/**
	 * 认证总监
	 */
	CCO,
	/**
	 * 市场总监
	 */
	CMO,
	/**
	 * 运营总监
	 */
	COO,
	/**
	 * 认证经理
	 */
	CERTIFIED_MANAGER,
	/**
	 * 客服主任
	 */
	CS_OFFICER,
	/**
	 * 客服
	 */
	CUSTOMER_SERVICE,
	/**
	 * 客户认证员
	 */
	CUST_CERT_SPECIALIST,
	/**
	 * 客户维护员
	 */
	CUST_MT_SPECIALIST,
	/**
	 * 设计经理
	 */
	DESIGN_MANAGER,

	/**
	 * 询价跟单员
	 */
	INQUIRY_SPECIALIST,
	/**
	 * 市场专员
	 */
	MARKETING_SPECIALIST,
	/**
	 * 运营经理
	 */
	OPERATION_MANAGER,
	/**
	 * 运营维护员
	 */
	OPERATION_SPECIALIST,
	/**
	 * 订单通知接收人,订单已支付、转账凭证
	 */
	ORDER_SUB_USER,
	/**
	 * 采购主任
	 */
	PURCHASING_OFFICER,
	/**
	 * 品质认证工程师
	 */
	QA_ENGINEER,
	/**
	 * 品质认证主管
	 */
	QA_SUPERVISOR,
	/**
	 * 主管
	 */
	EXECUTIVE_DIRECTOR,

	/**
	 * 品质认证经理
	 */
	QA_MANAGER,
	/**
	 * 品质认证员
	 */
	QA_SPECIALIST,
	/**
	 * 报价专员
	 */
	QUOTATION_SPECIALIST,
	/**
	 * 供应商认证员
	 */
	SUPP_CERT_SPECIALIST,
	/**
	 * 供应商维护员
	 */
	SUPP_MT_SPECIALIST,
	/**
	 * 系统维护专员
	 */
	SYSCONFIG_ADMIN,
	/**
	 * 仓储物流主任
	 */
	WMS_OFFICER,
	/**
	 * 仓储物流专员
	 */
	WMS_SPECIALIST,
	/**
	 * 扩展部经理
	 */
	B_EXTENTION_MANAGER,
	/**
	 * 角色菜单
	 */
	OPERATION_FUNCTION,

	/**
	 * 角色
	 */
	ROLE,

	/**
	 * 认证企业
	 */
	VIP_CORPORATION,
	
	/**
	 * 分管部门
	 */
	DEPT,
	/**
	 * 负责人
	 */
	PRINCIPAL,
	/**
	 * 产品线
	 */
	PRODUCTLINE,
	/**
	 * 审核
	 */
	CHECK,
	/**
	 *  商务专员
	 */
	BUSINESS_ASSISTANT,
	/**
	 *  账期
	 */
	ACCOUNT_CUST,

	/**
	 * CEO
	 */
	CEO,
	/**
	 * 询报价主管
	 */
	INQUIRY_QUOTATION_MG,
	/**
	 * 物料审核接收员：管制物料待审核、
	 */
	CONTROL_ORDER_AR,
	/**
	 * 账期订单审核接收员,账期审核接收员：账期订单审核
	 */
	ACCOUNT_ORDER_AR,
	
	/**
	 * 信用认证经理
	 */
	CREDIT_MANAGER,
	
	/**
	 * 供应商认证经理
	 */
	SUPPLIER_MANAGER
}