package com.yikuyi.party.vendor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.vendor.vo.ContactPersonInfo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorQueryVo;
import com.yikuyi.party.vendor.vo.VendorResponVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;

@Mapper
public interface VendorDao {
	  
	
	/**
	 * 根据partyID获取供应商基本信息
	 * @param partyId
	 * @return
	 * @since 2017年8月11日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public VendorInfoVo getVendorInfo(@Param("partyId")String partyId);	
	
	
	/**
	 * 供应商销售信息
	 * @param partyId
	 * @return
	 * @since 2017年8月15日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public VendorSaleInfoVo getVendorSaleInfoVo(@Param("partyId")String partyId);	
	
	/**
	 * 供应商销售信息联系人
	 * @param partyId
	 * @return
	 * @since 2017年8月15日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public  ContactPersonInfo getVendorPersonList(@Param("partyId")String partyId);
	
	
	/**
	 * 供应商管理列表
	 * @param vendorResponVo
	 * @return
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public  List<VendorResponVo> getVendorManageList(VendorQueryVo vendorQueryVo,RowBounds rowBounds);
	
	/**
	 * 销售管理列表
	 * @param vendorResponVo
	 * @return
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public  List<VendorResponVo> getSellManageList(VendorQueryVo vendorQueryVo,RowBounds rowBounds);
	
	/**
	 * 销售管理列表
	 * @param vendorResponVo
	 * @return
	 * @since 2017年8月17日
	 * @author zr.zhanghua@yikuyi.com
	 */
	public  List<VendorResponVo> listVendorManage(@Param("partyIds")List<String> partyIds);
	
	
	/**
	 * 根据partyID获取供应商基本信息
	 * @param partyId
	 * @return
	 * @since 2017年12月21日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<VendorInfoVo> vendorBatchList(@Param("partyIds")List<String> partyIds);	
	
}