package com.yikuyi.party.partygroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.model.PartyRelationship;

@Mapper
public interface PartyRelationshipDao {
    /**
     * 新增关系
     * @author 张伟
     * @param record
     * @return
     */
    int insert(PartyRelationship record);

    /**
     * 查询会员关系信息
     * @param id
     * @return
     * @since 2017年1月20日
     * @author zr.aoxianbing@yikuyi.com
     */
	public List<PartyRelationship> getPartyRelationship(PartyRelationship partyRelationship);
	
	/**
	 * 修改partyRelationship
	 * @param partyRelationship
	 * @return
	 * @since 2017年2月15日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void updateRelationShip(PartyRelationship partyRelationship);
	
	 /**
     * 查询申请会员不是激活企业账户的企业
     * @param id
     * @return
     * @since 2017年1月20日
     * @author zr.helinmei@yikuyi.com
     */
	public List<PartyRelationship> findByApplyId(PartyRelationship partyRelationship);
	
	/**
	 * 根据企业id查询partyid
	 * @param enterpriseId
	 * @return
	 * @since 2017年2月21日
	 * @author tb.yumu@yikuyi.com
	 */
	public PartyRelationship findpartyIdByEnterpriseId(String enterpriseId);

	/**
	 * 查询登陆用户下的子账号
	 * @param userId 登陆用户id
	 * @param userIds 用户集合id
	 * @param default1
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<String> getAllPartyRelationship(@Param("userId")String userId, @Param("userIds")List<String> userIds, RowBounds rowBounds);
	
	/**
	 * 根据partyIdfromTo and RELATIONSHIP_NAME 查询PartyRelationship
	 * @param partyIdTo
	 * @param name
	 * @return
	 * @since 2017年3月21日
	 * @author tb.yumu@yikuyi.com
	 */
	List<PartyRelationship> getAgentByPartyfromTo(@Param("partyIdTo")String partyIdTo,@Param("name")String name);
	
	/**
	 * 根据用户ID获取用户部门关系
	 * @param partyIdFrom
	 * @return
	 * @since 2017年5月16日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<PartyRelationship> getUserRelationship(@Param("partyIdFrom")String partyIdFrom);
	
	/**
	 * 根据partyIdfromTo and PARTY_RELATIONSHIP_TYPE_ID 查询PartyRelationship
	 * @param partyIdTo
	 * @param PARTY_RELATIONSHIP_TYPE_ID
	 * @return
	 * @since 2017年10月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyRelationship> listPartyIdFrom(@Param("partyIdTo")String partyIdTo,@Param("partyRelationshipTypeId")String partyRelationshipTypeId);
	
}