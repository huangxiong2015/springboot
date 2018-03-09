package com.yikuyi.party.vendor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.vendor.vo.PartySupplierAlias;

@Mapper
public interface PartySupplierAliasDao {
	/**
	 * 新增供应商别名
	 * @param PartySupplierAlias
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(PartySupplierAlias supplierAlias);
	
	/**
	 * 修改供应商别名
	 * @param supplierAlias
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void update(PartySupplierAlias supplierAlias);
	
	/**
	 * 验证别名是否重复
	 * @param aliasName
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public  PartySupplierAlias isExistAliasName(@Param(value="aliasName")String aliasName);

	/**
	 * 根据别名模糊搜索
	 * @param aliasName
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public  List<PartySupplierAlias> supperAliasNameList(@Param(value="aliasName")String aliasName,@Param(value="partyId")String partyId);

	
	/**
	 * 删除供应商别名
	 * @param id
	 * @since 2017年12月8日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void deleteSupperAlias(@Param(value="supplierAliasId")String supplierAliasId);
	
}
