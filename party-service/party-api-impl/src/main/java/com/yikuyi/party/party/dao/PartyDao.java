package com.yikuyi.party.party.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.EnterpriseVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.vo.EnterpriseExpiredVo;
import com.yikuyi.party.vo.PartyCarrierParamVo;
import com.yikuyi.party.vo.PartyCarrierVo;

@Mapper
public interface PartyDao {
	  
	 public void insert(Party party);
	/**
	 * 获取party(企业)详情信息
	 * @param id        
	 * @return
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Party getPartyDetail(@Param("id")String id,@Param("partyType")PartyType partyType);
	/**
	 * 获取party(个人)详情信息
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Party getPartyPersonDetail(@Param("id")String id);
	
	/**
	 * 修改企业信息
	 * @param id        
	 * @return
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void updateParty(Party party);
	
	/**
	 * 后台查询企业管理员列表
	 * @param param
	 * @param rowBounds
	 * @since 2017年2月13日
	 * @author gongtianyu@yikuyi.com
	 */
	public List<EnterpriseVo> getEnterpriseList(EnterpriseParamVo param, RowBounds rowBounds);
	/**
	 * 查询企业管理员列表
	 * @param param
	 * @return
	 * @since 2017年4月13日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<EnterpriseVo> getEntList(EnterpriseParamVo param);
	/**
	 * 根据公司名称获得所有partyId
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Party> getPartyByCompanyName(@Param("id")String id,@Param("name")String name);
	
	/**
	 * 根据企业ID查询子账号以及自己
	 * @param entId
	 * @return
	 * @since 2017年2月16日
	 * @author gongtianyu@yikuyi.com
	 */
	public List<UserExtendVo> getEnterpriseAccountsList(String entId);
	
	/**
	 * 后台查询认证的企业
	 * @param param
	 * @param rowBounds
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<EnterpriseVo> entCertificationList(EnterpriseParamVo param, RowBounds rowBounds);

	/**
	 * 后台查询企业账户审核列表
	 * @param param
	 * @param rowBounds
	 * @since 2017年5月5日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<EnterpriseVo> getAccountApplyList(EnterpriseParamVo param, RowBounds rowBounds);
	
	/**
	 * [后端]用户管理-用户启用/停用/删除接口
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年5月10日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void updateStateId(@Param("partyId")String partyId,@Param("statusId")String statusId);
	
	/**
	 * 查询失效的企业信息
	 * @return
	 * @since 2017年5月11日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public List<EnterpriseExpiredVo> getEnterpriseDocumentsExpiredList();
	
	/**
	 * 获取认证企业易库易编码
	 * @param partyCode   YKY客户编码
	 * @return
	 * @since 2017年7月20日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public String getPartyCode(String partyCode);
	
	/**
	 * 导出认证企业列表list
	 * @param vo
	 * @return
	 * @since 2017年7月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<EnterpriseVo> getEntCertificationList(EnterpriseParamVo vo);
	
	/**
	 * 查询物流公司信息
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PartyCarrierVo> getPartyCarrierList(PartyCarrierParamVo param, RowBounds rowBounds);
	
	/**
	 * 根据partyId查询相应的物流信息
	 * @param partyId
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PartyCarrierVo getPartyCarriorVoInfo(String partyId);
	
	/**
	 * 根据企业Id获取企业信息，包含账期信息
	 * @param corporationId
	 * @param partyType
	 * @return
	 * @since 2017年8月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public Party getPartyByCorporationId(@Param("corporationId")String corporationId,
			@Param("partyType")PartyType partyType,@Param("accountPeriodStatus")AccountPeriodStatus accountPeriodStatus);
	
	
	/**
	 * 根据partyId查询enterpriseVo
	 * @param partyId
	 * @return
	 * @since 2017年8月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public EnterpriseVo getEnterpriseVoInfo(@Param("partyId")String partyId);
	
	
	
	/**
	 * 通过企业id，查询用户列表
	 * @param corporationId
	 * @return
	 * @since 2017年8月31日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<UserExtendVo> listAccountByEntId(String entId);
	
	/**
	 * 根据ID删除  （专用）
	 * @param partyId
	 * @return
	 * @since 2017年9月7日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void deleteByPartyId(@Param("partyId")String partyId);
	
}