package com.yikuyi.party.vendorManage.api;


import java.util.List;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.vo.CheckManage;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckStartOrLose.StartOrLose;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.PartySupplierAlias;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.Vendor.VendorApplyType;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.workflow.Apply;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 供应商管理的相应接口
 * 
 * @author tb.huangqingfeng
 *
 */
public interface IVendorManageResource {
	
	
	/**
	 * 供应商管理【新增】  为审核内容，建档状态
	 * 
	 * 供应商建档、失效、启用由供应商认证员操作，需经过莫总和谈总审批，审批通过后生效。
	 * 
	 * 默认字段参数
	 * 审核内容：isCheckContent  YSE
	 * 申请类型：checkManageState NEW
	 * 
	 * @author tb.huangqingfeng
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "供应商管理【新增】", notes = "供应商管理【新增】", response = Vendor.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public Vendor addVendor(
			@ApiParam(value = "Vendor实体类", required = true) Vendor vendor)throws BusinessException;

	
	
	/**
	 * 供应商编辑销售信息【变更】
	 * 
                     操作权限：创建人可修改供应商所有信息，业务负责人和询价员可修改销售基本信息
                     
	 * 供应商变更（修改供应商名称、供应商编码、供应商类型变更、是否为核心供应商、产品线）也需审核通过后才能生效。
 
                       其他信息变更修改不需要审核。。 其他人抛异常
         
	 * 
	 * 
	 * @author tb.huangqingfeng
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "供应商编辑销售信息【变更】", notes = "供应商编辑销售信息【变更】", response = VendorSaleInfoVo.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public VendorSaleInfoVo updateVendorSaleInfoVo(
			@ApiParam(value = "VendorSaleInfoVo", required = true) VendorSaleInfoVo vendorSaleInfoVo)throws BusinessException;
	
	/**
	 * 供应商信用信息【变更】
	 * 
                     操作权限：创建人可修改供应商所有信息，业务负责人和询价员可修改销售基本信息
                     
	 * 供应商变更（修改供应商名称、供应商编码、供应商类型变更、是否为核心供应商、产品线）也需审核通过后才能生效。
 
                       其他信息变更修改不需要审核。。 其他人抛异常
	 * 
	 * 
	 * @author tb.huangqingfeng
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "供应商信用信息【变更】", notes = "供应商信用信息【变更】", response = VendorCreditVo.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public VendorCreditVo updateVendorCreditVo(
			@ApiParam(value = "VendorCreditVo", required = true) VendorCreditVo vendorCreditVo)throws BusinessException;
	
	/**
	 *  供应商产品线【变更】
	 * 
                     操作权限：创建人可修改供应商所有信息，业务负责人和询价员可修改销售基本信息
                     
	 * 供应商变更（修改供应商名称、供应商编码、供应商类型变更、是否为核心供应商、产品线）也需审核通过后才能生效。
 
                       其他信息变更修改不需要审核。。 其他人抛异常
	 * 
	 * 
	 * @author tb.huangqingfeng
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "供应商产品线【变更】", notes = "供应商产品线【变更】", response = PartyProductLineVo.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PartyProductLineVo updatePartyProductLine(
			@ApiParam(value = "PartyProductLineVo", required = true) PartyProductLineVo partyProductLineVo)throws BusinessException;
	
	/**
	 * 供应商基础信息【变更】
	 * 
                     操作权限：创建人可修改供应商所有信息，业务负责人和询价员可修改销售基本信息
                     
	 * 供应商变更（修改供应商名称、供应商编码、供应商类型变更、是否为核心供应商、产品线）也需审核通过后才能生效。
 
                       其他信息变更修改不需要审核。。 其他人抛异常
	 * 
	 * 
	 * @author tb.huangqingfeng
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "供应商基础信息【变更】", notes = "供应商基础信息【变更】", response = VendorInfoVo.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public VendorInfoVo updateVendorInfoVo(
			@ApiParam(value = "VendorInfoVo", required = true) VendorInfoVo vendorInfoVo)throws BusinessException;
	
	/**
	 * 供应商管理列表的启动【启动、失效】
	 * 
	 * 供应商建档、失效、启用由供应商认证员操作，需经过莫总和谈总审批，审批通过后生效
	 * 
	 * 该供应商下存在上架商品，请通知相关人员下架商品后再将供应商失效。
	 * 
	 * 启用供应商需走审批流程，审核通过后，可上传该供应商的商品
	 * 
	 * @author tb.huangqingfeng
	 * @param partyGroup
	 * @return
	 */
	@ApiOperation(value = "供应商管理列表操作【启动、失效】", notes = "供应商管理列表操作【启动、失效】", response = CheckManage.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public CheckStartOrLose startOrLose(
			@ApiParam(value = "供应商ID", required = true) String partyId,
			@ApiParam(value = "启动START或失效LOSE", required = true) StartOrLose startOrLose,
			@ApiParam(value = "描述", required = false) String describe)throws BusinessException;
	
	
	
	
	
	/**
	 * 供应商审核【审核通过或不通过】
	 *  供应商建档、失效、启用由供应商认证员操作，需经过莫总和谈总审批，审批通过后生效
	 * 
	 * 通过则把审核的内容更新到主内容上，
	 * 
	 * 根据ID查询 内容  后台判断审核状态  返回对应的数据;
	 * 
	 * @author tb.huangqingfeng
	 * @param partyGroup
	 * @return
	 */
	@ApiOperation(value = "供应商审核【审核通过或不通过】", notes = "供应商审核【审核通过或不通过】", response = Vendor.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public Vendor checkPassOrNotPass(
			@ApiParam(value = "供应商审核ID", required = true) String checkManageId,
			@ApiParam(value = "通过PASS或不通过NOT_PASS", required = true) String checkresult,
			@ApiParam(value = "备注", required = true) String checkRemark)throws BusinessException;
	
	/**
	 * 供应商管理产品线文件上传解析【产品线上传文件解析】
	 * 上传  只传couponId是详情回显页面;只传fileUrl是新增页面;同时传couponId和fileUrl是修改页面上传文件
	 * @param 
	 * @param 
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@ApiOperation(value = "供应商管理产品线文件上传解析【产品线上传文件解析】 ", notes = "只传partyId是详情回显页面;只传fileUrl是新增页面;同时传partyId和fileUrl是修改页面上传文件 ", response = PartyProductLine.class, responseContainer="List")
	public PageInfo<PartyProductLine> parseProductsFile(@ApiParam(value="只传partyId是详情回显页面;只传fileUrl是新增页面;同时传partyId和fileUrl是修改页面上传文件", required=false)String partyId,
			                                           @ApiParam(value="只传partyId是详情回显页面;只传fileUrl是新增页面;同时传partyId和fileUrl是修改页面上传文件", required=true)String fileUrl,
			                                           @ApiParam(value="代理线类型", required=true)Type type) throws BusinessException;


	@ApiOperation(value = "建档信息审核通过保存", notes = "建档信息审核通过保存")
	public void editVendorApplySave(@ApiParam(value = "Apply实体对象", required = true) Apply apply) throws BusinessException;
	
	@ApiOperation(value = "启动通过保存", notes = "启动通过保存")
	public void editStartApplySave(@ApiParam(value = "Apply实体对象", required = true) Apply apply) throws BusinessException;
	
	@ApiOperation(value = "失效通过保存", notes = "失效通过保存")
	public void editLoseApplySave(@ApiParam(value = "Apply实体对象", required = true) Apply apply) throws BusinessException;
	
	@ApiOperation(value = "基本信息审核通过保存", notes = "基本信息审核通过保存")
	public void editVendorInfoApplySave(@ApiParam(value = "Apply实体对象", required = true) Apply apply);


	@ApiOperation(value = "供应商信息审核通过保存", notes = "供应商信息审核通过保存")
	public void editProductLineApplySave(@ApiParam(value = "Apply实体对象", required = true) Apply apply);
		
	/**
	 * 供应商管理列表【查询】
	 * @param vendor
	 * @param page
	 * @param size
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "供应商管理列表【查询】", notes = "供应商管理列表【查询】", response = VendorResponVo.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PageInfo<VendorResponVo> getVendorManageList(
			@ApiParam(value = "type", required = false) String type,
			@ApiParam(value = "groupName", required = false) String groupName,
			@ApiParam(value = "partyCode", required = false) String partyCode,
			@ApiParam(value = "category", required = false) String category,
			@ApiParam(value = "region", required = false) String region,
			@ApiParam(value = "isCore", required = false) String isCore,
			@ApiParam(value = "payType", required = false) String payType,
			@ApiParam(value = "enquiryName", required = false) String enquiryName,
			@ApiParam(value = "beginTime", required = false) String beginTime,
			@ApiParam(value = "endTime", required = false) String endTime,
			@ApiParam(value = "beginDealTime", required = false) String beginDealTime,
			@ApiParam(value = "endDealTime", required = false) String endDealTime,
			@ApiParam(value = "creatorName", required = false) String creatorName,
			@ApiParam(value = "page", required = false, defaultValue = "1") int page,
			@ApiParam(value = "size", required = false, defaultValue = "10") int size)throws BusinessException;
	
		/**
		 * 销售管理列表【查询】
		 * @param vendor
		 * @param page
		 * @param size
		 * @return
		 * @throws BusinessException
		 * @since 2017年8月17日
		 * @author zr.zhanghua@yikuyi.com
		 */
		@ApiOperation(value = "销售管理列表【查询】", notes = "销售管理列表【查询】", response = VendorResponVo.class, responseContainer = "List")
		@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
		public PageInfo<VendorResponVo> getSellManageList(
				@ApiParam(value = "type", required = false) String type,
				@ApiParam(value = "groupName", required = false) String groupName,
				@ApiParam(value = "partyCode", required = false) String partyCode,
				@ApiParam(value = "category", required = false) String category,
				@ApiParam(value = "region", required = false) String region,
				@ApiParam(value = "isCore", required = false) String isCore,
				@ApiParam(value = "payType", required = false) String payType,
				@ApiParam(value = "enquiryName", required = false) String enquiryName,
				@ApiParam(value = "beginTime", required = false) String beginTime,
				@ApiParam(value = "endTime", required = false) String endTime,
				@ApiParam(value = "beginDealTime", required = false) String beginDealTime,
				@ApiParam(value = "endDealTime", required = false) String endDealTime,
				@ApiParam(value = "creatorName", required = false) String creatorName,
				@ApiParam(value = "orderVerify", required = false) String orderVerify,
				@ApiParam(value = "page", required = false, defaultValue = "1") int page,
				@ApiParam(value = "size", required = false, defaultValue = "10") int size)throws BusinessException;
	

	
	/**
	 * 查询供应商是否审核【查询】
	 * @param partyIds
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "查询供应商是否审核", notes = "查询供应商是否审核", response = Vendor.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<PartySupplier> getOrderVerify(
			@ApiParam(value = "供应商审核ID", required = true) List<String> partyIds
			)throws BusinessException;
	
	/**
	 * 供应商列表失效启用操作
	 * @param partyId
	 * @param description
	 * @param partyStatus
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public void enableOrlose(String partyId,String description,PartyStatus partyStatus)throws BusinessException;
	
	/**
	 * 第二次审核发邮件使用
	 * @param 
	 * @param 
	 * @param 
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@ApiOperation(value = "第二次审核发邮件使用", notes = "第二次审核发邮件使用")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public void sendMailTwo(String companyName,RoleTypeEnum roleTypeEnum,VendorApplyType vendorApplyType,String applyUserId)throws BusinessException;
	
	/**
	 * 供应商ID获取其产品线所有信息
	 * @param 
	 * @param 
	 * @param 
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@ApiOperation(value = "供应商ID获取其产品线所有信息", notes = "供应商ID获取其产品线所有信息", response = PartyProductLineVo.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PartyProductLineVo findProductLineByPartyId(@ApiParam(value = "供应商审核ID", required = true)String  partyId)throws BusinessException;
	
	
	/**
	 * 查询供应商简称  是否唯一性
	 * 
	 */
	@ApiOperation(value = "查询供应商简称  是否唯一性", notes = "传供应商ID时，排除该ID下的简称", response = PartyGroup.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public PartyGroup findGroupName(@ApiParam(value = "供应商ID，传供应商ID时，排除该ID下的简称", required = false)String  partyId,
			                        @ApiParam(value = "供应商简称", required = true)String  groupName)throws BusinessException;

	
	/**
	 * 通过供应商id集合查询所有logo相关信息提供sdk
	 * @param partyIds
	 * @return
	 * @throws BusinessException
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "通过供应商id集合查询所有logo相关信息", notes = "通过供应商id集合查询所有logo相关信息", response = Vendor.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) })
	public List<VendorResponVo> listVendorManage(
			@ApiParam(value = "供应商ID集合", required = true) List<String> partyIds
			)throws BusinessException;
	
	/**
	 * 供应商别名保存
	 * @param 
	 * @return
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "供应商别名保存", notes = "供应商别名保存", response = PartySupplierAlias.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void saveSupplierAlias(@ApiParam(value = "供应商别名保存", required = true) List<PartySupplierAlias> supplierAliasList) throws BusinessException;

	/**
	 * 验证别名是否重复
	 * @param supplierAlias
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "验证供应商别名是否重复", notes = "验证供应商别名是否重复", response = PartySupplierAlias.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = PartySupplierAlias.class) 
			})
	public PartySupplierAlias isExistAliasName(@ApiParam(value = "供应商别名", required = true) String aliasName);

	/**
	 * 模糊查询供应商接口
	 * @param supplierAlias
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "模糊查询供应商别名接口", notes = "模糊查询供应商别名接口", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 404, message = "访问的页面不存在", response = List.class) 
			})
	public List<PartySupplierAlias> supperAliasNameList(@ApiParam(value = "供应商别名", required = false) String aliasName,@ApiParam(value = "供应商Id", required = false) String partyId);

	/**
	 * 删除供应商别名接口
	 * @param partyExpand
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "删除供应商别名", notes = "删除供应商别名")
	public void deleteSupperAlias(@ApiParam(value = "删除供应商别名", required = true)String supplierAliasId);
	
	/**
	 * 导出代理和不代理产品线数据
	 * @since 2018年1月10日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "导出代理和不代理产品线数据", notes = "作者：李京<br>导出代理和不代理产品线数据" ,response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void exportProductLines(@ApiParam(value = "供应商id", required = true) String partyId,@ApiParam(value = "供应商名称", required = true) String partyName);
	
}