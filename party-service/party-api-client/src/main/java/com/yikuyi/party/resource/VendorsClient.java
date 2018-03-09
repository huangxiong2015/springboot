package com.yikuyi.party.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 供应商sdk定义
 * 
 * @author zr.helinmei@yikuyi.com
 */
@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface VendorsClient {
	
	/**
	 * 查询分销商的父子关系
	 * @param id
	 * @param relationshipType
	 * @return
	 * @since 2017年8月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("GET /v1/vendors/getParentRelationInfo/{id}/{relationshipType}")
	public PartyRelationship getParentRelationInfo(@Param("id") String id,@Param("relationshipType") PartyRelationshipTypeEnum relationshipType);
	
	/**
	 * 根据供应商ID,查询详情
	 * @param id
	 * @return
	 */
	@RequestLine("GET /v1/vendors/detail?id={id}")
	@Headers({ "Authorization: Basic {authToken}" })
	public Party getVendorDetail(@Param("id") String id , @Param("authToken") String authToken);

	/**
	 * 查询供应商是否审核【查询】
	 * @param partyIds
	 * @param authToken
	 * @return
	 * @since 2017年8月22日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@RequestLine("POST v1/vendorManage/getOrderVerify")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartySupplier> getOrderVerify(@RequestBody List<String> partyIds,@Param("authToken") String authToken);
	
	/**
	 * 根据供应商ID,查询产品线信息
	 * @param partyId
	 * @return
	 */
	@RequestLine("GET /v1/vendorManage/findProductLineByPartyId/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PartyProductLineVo findProductLineByPartyId(@Param("partyId") String partyId , @Param("authToken") String authToken);
	
	/**
	 * 查询【供应商管理】产品线去掉重复的数据
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年10月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/vendorManage/onlyProductLine/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartyProductLineVo> onlyProductLine(@Param("partyId") String partyId, @Param("authToken") String authToken);
	
	
	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年10月27日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@RequestLine("GET /v1/vendorManage/productLine/category/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<PartyProductLineVo> onlyProductLineCategoryList(@Param("partyId") String partyId, @Param("authToken") String authToken);
	
	/**
	 * 通过供应商id集合查询所有信息提供sdk
	 * @param partyId
	 * @param authToken
	 * @return
	 * @since 2017年10月27日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestLine("PUT /v1/vendorManage/listVendorManage")
	public List<VendorResponVo> listVendorManage(@RequestBody List<String> partyIds);
	
	
	/**
	 * 根据供应商ID,查询产品线信息
	 * @param partyId
	 * @return
	 */
	@RequestLine("POST /v1/vendors/batch")
	@Headers({ "Authorization: Basic {authToken}" })
	public List<VendorInfoVo> vendorBatchList(@RequestBody List<String> partyIds , @Param("authToken") String authToken);
	
	@RequestLine("GET /v1/vendors/vendorSaleInfo/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public VendorSaleInfoVo vendorSaleInfo(@Param("partyId") String partyId, @Param("authToken") String authToken);
	
	@RequestLine("GET /v1/vendors/vendorCredit/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public VendorCreditVo vendorCredit(@Param("partyId") String partyId, @Param("authToken") String authToken);
	
	@RequestLine("GET /v1/vendors/vendorInfo/{partyId}")
	@Headers({ "Authorization: Basic {authToken}" })
	public VendorInfoVo vendorInfo(@Param("partyId") String partyId, @Param("authToken") String authToken);
	
}
