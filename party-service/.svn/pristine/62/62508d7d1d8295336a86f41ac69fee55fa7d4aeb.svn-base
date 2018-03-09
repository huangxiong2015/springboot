package com.yikuyi.party.supplier.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;

/**
 * 供应商Dao类
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface SupplierDao {

	/**
	 * 根据供应商ID查询供应商简单信息 如果没有传入参数，查询所有供应商
	 * 
	 * @param supplierIds
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 */
	public List<SupplierVo> searchSupplierSimple(@Param("supplierIds") Collection<String> supplierIds);

	/**
	 * 获取供应商负责人邮箱
	 * 
	 * @param supplierId
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	public SupplierMailVo getSuplierrelationShipMail(@Param("supplierId") String supplierId);

}