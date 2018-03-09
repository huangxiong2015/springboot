package com.yikuyi.party.vendors.api.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.partygroup.bll.PartyGroupManager;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vendors.api.IVendorResource;
import com.yikuyi.party.vendors.bll.PartyProductLineManager;
import com.yikuyi.party.vendors.bll.VendorManager;
import com.yikuyi.party.vendors.bll.VendorsManager;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.party.vo.SupplierVo;
import com.yikuyi.party.vo.VendorVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;

@RestController
@RequestMapping("v1/vendors")
public class VendorsResource implements IVendorResource {

	
	@Autowired
	private VendorsManager vendorsManager;
	
	@Autowired
	private VendorManager vendorManager;
	
	@Autowired
	private PartyGroupManager partyGroupManager;
	@Autowired
	private PartyProductLineManager partyProductLineManager;
	
	/**
	 * 查看供应商列表
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<PartyVo> getPartyList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		PartyGroupVo param = new PartyGroupVo();
		//String partyId = RequestHelper.getLoginUserId();
		//param.setPartyIdFrom(partyId);
		//param.setStatus(PartyStatus.PARTY_ENABLED);//默认启用
		//param.setRoleTypeIdTo(RoleTypeEnum.SUPPLIER.toString());
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		return vendorsManager.getPartyList(param, rowBounds);
		
	}
	
	/**
	 * 获取单个供应商信息
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Party getPartyByPartyId(@PathVariable("id") String id) {
		String partyId = RequestHelper.getLoginUserId();
		return vendorsManager.getPartyByPartyId(id, partyId);
	}
	
	/**
	 * 根据供应商id获取单条供应商详情
	 */
	@Override
	@RequestMapping(value = "/name/batch", method = RequestMethod.POST)
	public List<PartyVo> getVendorNameListByIds(@RequestBody List<String> partyIds) {
		return vendorsManager.getVendorNameListByIds(partyIds);
	}

	/**
	 * 查询供应商
	 */
	@Override
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public Party getVendorDetail(@RequestParam(value = "id" , required = true) String id) {
		return vendorsManager.getVendorDetail(id);
	}

	/**
	 * 保存供应商信息
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody VendorVo vendorVo) throws BusinessException {
		Long vendorId = IdGen.getInstance().nextId();
		 // 当前登录用户
	    String partyId = RequestHelper.getLoginUserId();
	    vendorVo.setVendorId(vendorId.toString());	
	    vendorsManager.save(vendorVo,partyId);
	}

	/**
	 * 根据id修改供应商信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody VendorVo vendorVo) throws BusinessException {
		//查找需要修改的供应商的信息
		Party party = partyGroupManager.getPartyGroupByGroupId(vendorVo.getVendorId());
		if(null == party){
			throw new BusinessException(PartyBusiErrorCode.SUPPLIER_IS_EMPTY);
		}
		// 当前登录用户
	    String partyId = RequestHelper.getLoginUserId();
	    vendorsManager.update(vendorVo, partyId);
	}

	/**
	 * 删除供应商信息
	 */
	@Override
	@RequestMapping(value = "del", method = RequestMethod.PUT)
	public void delete(@RequestBody List<String> ids) {
	}

	/**
	 * 保存分享关系
	 */
	@Override
	@RequestMapping(value = "/{id}/save" , method = RequestMethod.POST)
	public String saveSupplierShare(@PathVariable(value = "id" , required=true)String id,@RequestBody UserVo userVo) {
		return vendorsManager.saveSupplierShare(id,userVo);
	}

	/**
	 * 查询供应商分享关系
	 */
	@Override
	@RequestMapping(value = "/{id}/relation" ,method = RequestMethod.GET)
	public List<PartyRelationship> findSupplierShare(@PathVariable(value = "id" , required=true)String id) {
		return vendorsManager.findSupplierShare(id);
	}

	/**
	 * 根据条件查询供应商列表
	 */
	@Override
	@RequestMapping(value = "/list" ,method = RequestMethod.GET)
	public PageInfo<SupplierVo> getVendorList(@RequestParam(value="id",required=true)String id, 
			@RequestParam(value="vendorName",required=false)String vendorName,
			@RequestParam(value="vendorCode",required=false)String vendorCode,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="size",required=false,defaultValue="10") int size) {
		return vendorsManager.getVendorList(id, vendorName, vendorCode, page, size);
	}
	
	/**
	 * 查询【供应商管理】基本信息
	 */
	@Override
	@RequestMapping(value = "/vendorInfo/{partyId}", method = RequestMethod.GET)
	public VendorInfoVo vendorInfo(@PathVariable("partyId") String partyId) throws BusinessException {
		return vendorManager.getVendorInfo(partyId);
	}

	/**
	 * 查询【供应商管理】产品线
	 */
	@Override
	@RequestMapping(value = "/productLine/{partyId}", method = RequestMethod.GET)
	public  List<PartyProductLine> productLine(@PathVariable("partyId") String partyId,@RequestParam(value="type",required=false)Type type) {
		PartyProductLine partyProductLine= new PartyProductLine();
		partyProductLine.setPartyId(partyId);
		if(null!=type){
			partyProductLine.setType(type);
		}
		return partyProductLineManager.findByEntity(partyProductLine);
	}

	/**
	 * 查询【供应商管理】信用
	 */
	@Override
	@RequestMapping(value = "/vendorCredit/{partyId}", method = RequestMethod.GET)
	public VendorCreditVo vendorCredit(@PathVariable("partyId") String partyId) {
		return vendorManager.getVendorCredit(partyId);
		
	}

	/**
	 * 查询【供应商管理】销售
	 */
	@Override
	@RequestMapping(value = "/vendorSaleInfo/{partyId}", method = RequestMethod.GET)
	public VendorSaleInfoVo vendorSaleInfo(@PathVariable("partyId") String partyId) throws BusinessException {
		return vendorManager.getVendorSaleInfoVo(partyId);

	}
	/**
	 * 查询分销商的父子关系
	 * @param id
	 * @param relationshipType
	 * @return
	 * @since 2017年8月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/getParentRelationInfo/{id}/{relationshipType}", method = RequestMethod.GET)
	public PartyRelationship getParentRelationInfo(@PathVariable(value = "id")String id, @PathVariable(value = "relationshipType")PartyRelationshipTypeEnum relationshipType) {
		return vendorsManager.getParentRelationInfo(id, relationshipType);
	}
	
	/**
	 * 查询【供应商管理】产品线去掉重复的数据
	 * @param partyId
	 * @return
	 * @since 2017年9月27日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/onlyProductLine/{partyId}", method = RequestMethod.GET)
	public  List<PartyProductLineVo> onlyProductLine(@PathVariable("partyId") String partyId,@RequestParam(value="type",required=false)Type type) {
		PartyProductLine partyProductLine= new PartyProductLine();
		partyProductLine.setPartyId(partyId);
		if(null!=type){
			partyProductLine.setType(type);
		}
		return partyProductLineManager.onlyProductLine(partyProductLine);
	}


	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 */
	@Override
	@RequestMapping(value = "/productLine/category/{partyId}", method = RequestMethod.GET)
	public List<PartyProductLineModel> onlyProductLineCategoryList(@PathVariable("partyId") String partyId) {
		PartyProductLineModel partyProductLine= new PartyProductLineModel();
		partyProductLine.setPartyId(partyId);
		return partyProductLineManager.onlyProductCategoryList(partyProductLine);
	}

	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param brandId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/productLine/supplier/{brandId}", method = RequestMethod.GET)
	public List<PartyProductLineVo> listSupplierByBrandId(@PathVariable("brandId")String brandId) {
		return partyProductLineManager.listSupplierByBrandId(brandId);
	}
	/**
	 * 根据制造商id,查询所有分类过滤重复
	 * @param partyId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/productLine/supplier/category/{brandId}", method = RequestMethod.GET)
	public List<PartyProductLineVo> listCategoryByBrandId(@PathVariable("brandId")String brandId) {
		return partyProductLineManager.listCategoryByBrandId(brandId);
	}


	/**
	 * 查询供应商列表，交期准确率数据
	 * @param vendorName
	 * @return PageInfo<PartyVo>
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/supplier/list" ,method = RequestMethod.GET)
	public PageInfo<SupplierVo> getPartySupplierList(@RequestParam(value="vendorName",required=false)String vendorName,@RequestParam(value="orderSq",required=false)String orderSq,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="size",required=false,defaultValue="10") int size) {
		return vendorsManager.getPartySupplierList(vendorName, orderSq, page, size);
	}

	/**
	 * 新增/编辑/删除交期正确率
	 * 
	 * @param partyGroup
	 * @return 
	 */
	@Override
	@RequestMapping(value = "/updateLeadTime", method = RequestMethod.PUT)
	public void updateLeadTime(@RequestBody PartyGroup partyGroup) {
		vendorsManager.updateLeadTime(partyGroup);
	}
	/**
	 * 定时波动供应商交期准确率
	 * @param 
	 * @return void
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/supplierLeadTimejob", method = RequestMethod.GET)
	public void supplierLeadTimejob() throws BusinessException {
		vendorsManager.supplierLeadTimejob();
	}

	/**
	 * 根据批量供应商id查询数据
	 * @param 
	 * @return void
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	public List<VendorInfoVo> vendorBatchList(@RequestBody List<String> ids) {
		return vendorManager.vendorBatchList(ids);
	}

	/**
	 * 查询mov设置产品线品牌信息
	 * @param partyId
	 * @return
	 * @since 2018年1月12日
	 * @author tb.lijing@yikuyi.com
	 */
/*	@Override
	@RequestMapping(value = "/productLineBrand/{partyId}", method = RequestMethod.GET)
	public List<?> findProductLineBrand(@PathVariable("partyId") String partyId) {
		PartyProductLine partyProductLine= new PartyProductLine();
		partyProductLine.setPartyId(partyId);
		return partyProductLineManager.findProductLineBrand(partyProductLine);
	}*/
}
