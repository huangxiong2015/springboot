package com.yikuyi.party.credit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.credit.model.PartyAttachment;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditParamVo;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.yikuyi.party.vendor.vo.VendorCreditVo;



@Mapper
public interface PartyCreditDao {
	/**
	 * 账期插入
	 * @param id        
	 * @return
	 * @since 2017年8月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void insert(PartyCredit partyCredit);

	 /**
	  * 查询账期信息
	  * @param partyId
	  * @param object
	  * @return
	  * @since 2017年8月9日
	  * @author zr.wuxiansheng@yikuyi.com
	  */
	 public PartyCredit getPartyCredit(@Param("partyId")String partyId, @Param("currency")Currency currency);
	 

	/**
	 * 账期修改
	 * @param id        
	 * @return
	 * @since 2017年8月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void update(PartyCredit partyCredit);
	 
	 
	/**
	 * 账期插入
	 * @param id        
	 * @return
	 * @since 2017年8月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void insertAttrchment(@Param("id")String id,@Param("attachmentName")String attachmentName,@Param("attachmentUrl")String attachmentUrl,@Param("userId")String userId,@Param("attachmentType")String attachmentType,@Param("partyId")String partyId);
	 
    /**
	 * 账期删除
	 * @param id        
	 * @return
	 * @since 2017年8月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void deleteAttrchment(@Param("id")String id,@Param("attachmentType")String attachmentType);
	 
	 /**
	  * 账期订单列表查询
	  * @param param
	  * @param rowBounds
	  * @return
	  * @since 2017年8月9日
	  * @author zr.wuxiansheng@yikuyi.com
	  */
	 public List<PartyCreditVo> getPartyCreditVoList(PartyCreditParamVo param, RowBounds rowBounds);

	 
	 /**
	  * 
	  * @param partyCreditVo
	  * @since 2017年8月14日
	  * @author zr.wuxiansheng@yikuyi.com
	  */
	 public void updatePartyCreditByPartyId(PartyCredit partyCredit);


	 /**
	  * 插入PARTY_RELATIONSHIP     供应商（分管部门，负责人，询价员）
	  * 
	  * @param PartyRelationship
	  * @since 2017年8月14日
	  * @author tb.huangqingfeng@yikuyi.com
	  */
	 public void insertVendorRel(PartyRelationship partyRelationship);
	 
	 /**
	 * 账期插入  实体类插入
	 * @param id        
	 * @return
	 * @since 2017年8月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	 public void insertVo(PartyAttachment partyCreditAttachment);

	 /**
	  * 查询PARTY_ID_TO
	  * @param partyRelationship
	  * @return
	  * @since 2017年8月16日
	  * @author zr.chenxuemin@yikuyi.com
	  */
	 public String getPartyIdTo(@Param("partyIdFrom")String partyIdFrom,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
	
	 public String getPartyIdFrom(@Param("partyIdTo")String partyIdTo,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);

	 /**
	 * 供应商信用信息修改，根据供应商partyID
	 * @param        
	 * @return
	 * @since 2017年8月21日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	 public void updateByPartyId(VendorCreditVo partyCredit);
	 
	 
	 /**
	 *  删除 供应商协议   根据供应商partyId删除PARTY_ATTACHMENT的数据
	 * @param id        
	 * @return
	 * @since 2017年8月3日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void delByPartyId(@Param("partyId")String partyId,@Param("attachmentType")String attachmentType);
	 
	 /**
	 *  删除 根据供应商partyId删除 PARTY_CREDIT
	 * @param id        
	 * @return
	 * @since 2017年9月7日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	 public void deleteByPartyId(@Param("partyId")String partyId);
	 
	 
}