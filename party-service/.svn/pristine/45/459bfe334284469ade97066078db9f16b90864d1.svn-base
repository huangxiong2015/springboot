package com.yikuyi.party.vendor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.vendor.vo.PartyBankAccount;

@Mapper
public interface PartyBankAccountDao {
	  
	
	/**
	 * 根据partyID获取银行账号信息
	 * @param partyId
	 * @return
	 * @since 2017年8月14日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<PartyBankAccount> findPartyBankAccountList(@Param("partyId")String partyId);
	
	/**
	 * 新增银行信息
	 * @param 
	 * @return
	 * @since 2017年8月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void insert(PartyBankAccount partyBankAccount);	
	
	/**
	 * 删除银行信息   根据供应商的partyId
	 * @param 
	 * @return
	 * @since 2017年8月21日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void delByPartyId(String partyId);	
}