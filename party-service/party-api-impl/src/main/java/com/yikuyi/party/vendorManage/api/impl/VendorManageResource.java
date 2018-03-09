package com.yikuyi.party.vendorManage.api.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.vo.CheckStartOrLose;
import com.yikuyi.party.vendor.vo.CheckStartOrLose.StartOrLose;
import com.yikuyi.party.vendor.vo.CheckVendorInfoVo;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineJS;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.PartySupplierAlias;
import com.yikuyi.party.vendor.vo.Vendor;
import com.yikuyi.party.vendor.vo.Vendor.VendorApplyType;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorQueryVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vendorManage.api.IVendorManageResource;
import com.yikuyi.party.vendorManage.bll.SendMail;
import com.yikuyi.party.vendorManage.bll.VendorManage2;
import com.yikuyi.workflow.Apply;
import com.yikuyi.workflow.Apply.ApplyStatus;
import com.ykyframework.exception.BusinessException;

/**
 * 定义收货地址的相关接口
 * 
 * @author zr.aoxianbing@yikuyi.com
 *
 */

@RestController
@RequestMapping("v1/vendorManage")
public class VendorManageResource implements IVendorManageResource {
	
	@Autowired
	private SendMail sendMail;
	
    @Autowired
    private VendorManage2 vendorManage;
    
    /*
	 * 应商管理【新增】  为审核内容，建档状态
	 */
	@Override
	@RequestMapping(value = "/addVendor",method = RequestMethod.POST)
	public Vendor addVendor(@RequestBody Vendor vendor) throws BusinessException {		
		return vendorManage.addVendor(vendor);		
		
	}
	
	/**
	 * 供应商建档信息通过保存
	 * @return
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/apply/vendor/callback",method = RequestMethod.POST)
	public void editVendorApplySave(@RequestBody Apply apply) throws BusinessException{
		 vendorManage.editVendorApplySave(apply);	
		
	}
	
	/**
	 * 供应商启动通过保存
	 * @return
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/apply/start/callback",method = RequestMethod.POST)
	public void editStartApplySave(@RequestBody Apply apply) throws BusinessException{
		 vendorManage.editStartApplySave(apply);	
		
	}
	
	/**
	 * 供应商失效通过保存
	 * @return
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/apply/lose/callback",method = RequestMethod.POST)
	public void editLoseApplySave(@RequestBody Apply apply) throws BusinessException{
		 vendorManage.editLoseApplySave(apply);	
		
	}

	/**
	 * 供应商审核基本信息通过保存
	 * @return
	 * @since 2017年8月18日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/apply/basedata/callback",method = RequestMethod.POST)
	public void editVendorInfoApplySave(@RequestBody Apply apply){
		//vendorInfo 里面只根据PARTYID 修改 供应商全称，供应商简称，供应商编码，核心供应商
		
		if(ApplyStatus.APPROVED.name().equals(apply.getStatus().name())){//审核通过
			String applyContent = apply.getApplyContent();
			// 把获取的内容转化为json格式
			JSONObject json = JSON.parseObject(applyContent);
			
			// 把jsonObject转化成对应的实体
			CheckVendorInfoVo checkVendorInfoVo = JSONObject.parseObject(json.toString(), CheckVendorInfoVo.class);
			
			vendorManage.editVendorInfoApplySave(checkVendorInfoVo);
			//发送通过邮件
			sendMail.changePass(apply);
		}else{
			//发送不通过邮件
			sendMail.changeNotPass(apply);
		}
		
	}
	
	/**
	 * 供应商审核产品线信息通过编辑
	 * @return
	 * @since 2017年8月18日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/apply/productline/callback",method = RequestMethod.POST)
	public void editProductLineApplySave(@RequestBody Apply apply){
		if(ApplyStatus.APPROVED == apply.getStatus()){
			String applyContent = apply.getApplyContent();
			//把获取的内容转化为json格式	
			JSONObject json = JSON.parseObject(applyContent);
			//获取产品线数组
			JSONArray jsonList = json.getJSONArray("partyProductLineList");
		
			//把json转化成对应的实体
			List<PartyProductLineJS> partyProductLineList =  JSONObject.parseArray(jsonList.toString(), PartyProductLineJS.class);
				
			//涉及表 PARTY_PRODUCT_LINE，根据partyID 先删除再新增
			vendorManage.editProductLineApplySave(partyProductLineList);
			
			//发送通过邮件
			sendMail.changePass(apply);
			
			
		}else{
			//发送不通过邮件
			sendMail.changeNotPass(apply);
		}
		
	}
	
	
	/*
	 * 供应商编辑销售信息【变更】
	 * */
	@Override
	@RequestMapping(value = "/updateVendorSaleInfoVo",method = RequestMethod.POST)
	public VendorSaleInfoVo updateVendorSaleInfoVo(@RequestBody VendorSaleInfoVo vendorSaleInfoVo) throws BusinessException {
		return vendorManage.updateVendorSaleInfoVo(vendorSaleInfoVo);	
	}
	
	
	/*供应商信用信息【变更】*/
	@Override
	@RequestMapping(value = "/updateVendorCreditVo",method = RequestMethod.POST)
	public VendorCreditVo updateVendorCreditVo(@RequestBody VendorCreditVo vendorCreditVo) throws BusinessException {
		return vendorManage.updateVendorCreditVo(vendorCreditVo);	
	}
	
	/*供应商产品线【变更】*/
	@Override
	@RequestMapping(value = "/updatePartyProductLine",method = RequestMethod.POST)
	public PartyProductLineVo updatePartyProductLine(@RequestBody PartyProductLineVo partyProductLineVo) throws BusinessException {
		return vendorManage.updatePartyProductLine(partyProductLineVo);	
	}
	
	/*供应商基础信息【变更】*/
	@Override
	@RequestMapping(value = "/updateVendorInfoVo",method = RequestMethod.POST)
	public VendorInfoVo updateVendorInfoVo(@RequestBody VendorInfoVo vendorInfoVo) throws BusinessException {
		return vendorManage.updateVendorInfoVo(vendorInfoVo);	
	}
	
	/*供应商列表【启动或失效】*/
	@Override
	@RequestMapping(value = "/startOrLose",method = RequestMethod.POST)
	public CheckStartOrLose startOrLose(@RequestParam(value = "partyId", required = true)String partyId,
			@RequestParam(value = "startOrLose", required = true)StartOrLose startOrLose,
			@RequestParam(value = "describe", required = false) String describe) throws BusinessException {
		CheckStartOrLose checkStartOrLose = new CheckStartOrLose();
		return vendorManage.startOrLose(partyId,startOrLose,describe,checkStartOrLose);
	}

	

	@Override
	public Vendor checkPassOrNotPass(String checkManageId, String checkresult, String checkRemark)
			throws BusinessException {
		return null;
	}

	/*产品线上传*/
	@Override
	@RequestMapping(value = "/uploadLine",method = RequestMethod.POST)
	public PageInfo<PartyProductLine> parseProductsFile(
			@RequestParam(value = "partyId", required = false)String partyId, 
			@RequestParam(value = "fileUrl", required = true)String fileUrl,
			@RequestParam(value = "type", required = false)Type type) throws BusinessException {
		return vendorManage.parseProductsFile(partyId,fileUrl,type);
	}
	
	/**
	 * 供应商管理列表【查询】
	 * zhanghua
	 */
	@Override
	@RequestMapping(value = "/getVendorManageList",method = RequestMethod.GET)
	public PageInfo<VendorResponVo> getVendorManageList(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "partyCode", required = false) String partyCode,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "isCore", required = false) String isCore,
			@RequestParam(value = "payType", required = false) String payType,
			@RequestParam(value = "enquiryName", required = false) String enquiryName,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "beginDealTime", required = false) String beginDealTime,
			@RequestParam(value = "endDealTime", required = false) String endDealTime,
			@RequestParam(value = "creatorName", required = false) String creatorName,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="size",required=false,defaultValue="10") int size)
			throws BusinessException {		
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		VendorQueryVo vendorQueryVo = new VendorQueryVo();
		vendorQueryVo.setGroupName(groupName);
		vendorQueryVo.setPartyCode(partyCode);
		vendorQueryVo.setCategory(category);
		vendorQueryVo.setRegion(region);
		vendorQueryVo.setIsCore(isCore);
		vendorQueryVo.setPayType(payType);
		vendorQueryVo.setEnquiryName(enquiryName);
		vendorQueryVo.setBeginTime(beginTime);
		vendorQueryVo.setEndTime(endTime);
		vendorQueryVo.setBeginDealTime(beginDealTime);
		vendorQueryVo.setEndDealTime(endDealTime);
		vendorQueryVo.setCreatorName(creatorName);		

		vendorQueryVo.setCreater(RequestHelper.getLoginUserId());

		return vendorManage.getVendorManageList(vendorQueryVo,rowBounds);
	}
	
	/**
	 * 销售管理列表【查询】
	 * zhanghua
	 */
	@Override
	@RequestMapping(value = "/getSellManageList",method = RequestMethod.GET)
	public PageInfo<VendorResponVo> getSellManageList(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "partyCode", required = false) String partyCode,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "isCore", required = false) String isCore,
			@RequestParam(value = "payType", required = false) String payType,
			@RequestParam(value = "enquiryName", required = false) String enquiryName,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "beginDealTime", required = false) String beginDealTime,
			@RequestParam(value = "endDealTime", required = false) String endDealTime,
			@RequestParam(value = "creatorName", required = false) String creatorName,
			@RequestParam(value = "orderVerify", required = false) String orderVerify,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="size",required=false,defaultValue="10") int size)
			throws BusinessException {		
		RowBounds rowBounds = new RowBounds((page-1)*size, size);
		VendorQueryVo vendorQueryVo = new VendorQueryVo();
		vendorQueryVo.setGroupName(groupName);
		vendorQueryVo.setPartyCode(partyCode);
		vendorQueryVo.setCategory(category);
		vendorQueryVo.setRegion(region);
		vendorQueryVo.setIsCore(isCore);
		vendorQueryVo.setPayType(payType);
		vendorQueryVo.setEnquiryName(enquiryName);
		vendorQueryVo.setBeginTime(beginTime);
		vendorQueryVo.setEndTime(endTime);
		vendorQueryVo.setBeginDealTime(beginDealTime);
		vendorQueryVo.setEndDealTime(endDealTime);
		vendorQueryVo.setCreatorName(creatorName);		
		vendorQueryVo.setOrderVerify(orderVerify);	
		vendorQueryVo.setCreater(RequestHelper.getLoginUserId());

		return vendorManage.getSellManageList(vendorQueryVo,rowBounds);
	}


	/**
	 * 查询供应商是否审核【查询】
	 * zhanghua
	 */
	@Override
	@RequestMapping(value = "/getOrderVerify",method = RequestMethod.POST)
	public List<PartySupplier> getOrderVerify(@RequestBody List<String> partyIds) throws BusinessException {	
		return vendorManage.getOrderVerify(partyIds);
	}

	
	/**
	 * 供应商列表停用启用操作
	 * zhanghua
	 */
	@Override
	@RequestMapping(value = "/enableOrlose/{partyId}/{partyStatus}/{description}",method = RequestMethod.PUT)
	public void enableOrlose(@PathVariable(required=true)String partyId,@PathVariable(required=false)String description,@PathVariable(required=true)PartyStatus partyStatus) throws BusinessException {	
		Party party = new Party();
		party.setId(partyId);
		party.setDescription(description);
		party.setPartyStatus(partyStatus);
	    vendorManage.enableOrlose(party);
	}

	/**
	 * 审核流程发邮件  第二流程
	 * 
	 */
	@Override
	@RequestMapping(value = "/sendMailTwo/{companyName}",method = RequestMethod.POST)
	public void sendMailTwo(@PathVariable(required=true)String companyName, 
			@RequestParam(value = "roleTypeEnum", required = true)RoleTypeEnum roleTypeEnum, 
			@RequestParam(value = "vendorApplyType", required = true)VendorApplyType vendorApplyType,
			@RequestParam(value = "applyUserId", required = true)String applyUserId) throws BusinessException {
		if(!roleTypeEnum.name().equals(RoleTypeEnum.CEO.name())){
			throw new BusinessException("发送邮件角色目前只支持CEO，其他角色陆续开放","发送邮件角色目前只支持CEO，其他角色陆续开放");	
		}
		sendMail.checkApply(companyName, roleTypeEnum, vendorApplyType,applyUserId);		
	}

	@Override
	@RequestMapping(value = "/findProductLineByPartyId/{partyId}",method = RequestMethod.GET)
	public PartyProductLineVo findProductLineByPartyId(@PathVariable(value = "partyId", required = true)String partyId) throws BusinessException {
		return vendorManage.findProductLineByPartyId(partyId);
	}
	
	
	/**
	 * 查询供应商简称  是否唯一性
	 * 
	 */
	@Override
	@RequestMapping(value = "/findGroupName",method = RequestMethod.GET)
	public PartyGroup findGroupName(@RequestParam(value = "partyId", required = false)String partyId,
			              @RequestParam(value = "groupName", required = true)String groupName) throws BusinessException {
		return vendorManage.findGroupName(partyId,groupName);
	}

	/**
	 * 通过供应商id集合查询所有信息提供sdk
	 * zr.helinmei@yikyi.com
	 */
	@Override
	@RequestMapping(value = "/listVendorManage",method = RequestMethod.PUT)
	public List<VendorResponVo> listVendorManage(@RequestBody List<String> partyIds){		
		return vendorManage.listVendorManage(partyIds);
	}

	/**
	 * 供应商别名保存
	 * @param 
	 * @return
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/saveSupplierAlias",method = RequestMethod.POST)
	public void saveSupplierAlias(@RequestBody List<PartySupplierAlias> supplierAliasList) throws BusinessException{
		vendorManage.saveSupplierAlias(supplierAliasList);
	}
	/**
	 * 查询供应商别名接口
	 * @param supplierAlias
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/isExistAliasName",method = RequestMethod.GET)
	public PartySupplierAlias isExistAliasName(@RequestParam(value = "aliasName", required = true)String aliasName) {
		return vendorManage.isExistSupplier(aliasName);
	}
	/**
	 * 查询供应商别名接口
	 * @param supplierAlias
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/supperAliasNameList",method = RequestMethod.GET)
	public List<PartySupplierAlias> supperAliasNameList(@RequestParam(value = "aliasName", required = false)String aliasName,@RequestParam(value = "partyId", required = false)String partyId) {
		return vendorManage.supperAliasNameList(aliasName,partyId);
	}
	/**
	 * 删除供应商别名接口
	 * @param partyExpand
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/deleteSupperAlias/{supplierAliasId}", method = RequestMethod.DELETE)
	public void deleteSupperAlias(@PathVariable(value = "supplierAliasId")String supplierAliasId) {
		vendorManage.deleteSupperAlias(supplierAliasId);
	}

	/**
	 * 导出代理和不代理产品线数据
	 * @since 2018年1月10日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void exportProductLines(@RequestParam(value = "partyId", required = true)String partyId,@RequestParam(value = "partyName", required = true)String partyName) {
		vendorManage.exportProductLines(partyId, partyName);
	}

}