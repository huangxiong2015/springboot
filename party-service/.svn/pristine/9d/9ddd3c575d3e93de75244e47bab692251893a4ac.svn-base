package com.yikuyi.party.partySupplier.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.framework.springboot.audit.Param;
import com.yikuyi.party.vendor.vo.PartySupplier;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;

@Mapper
public interface PartySupplierDao {
	
	/*
	 * 供应商新增
	 * */
   int insert(PartySupplier partySupplier);
   
   
   /*
	 * 供应商编辑销售信息【变更】
	* */
  int updateVendorSaleInfoVo(VendorSaleInfoVo vendorSaleInfoVo);
  
  
  List<PartySupplier> getOrderVerify(List<String> partyIds);
  
  /*
   * 查询供应商基础信息    基础信息需要审核的内容
   */ 
  VendorInfoVo getCheckInfo(@Param(value="partyId")String partyId);
  
  
  /*
   * 更新应商基础信息    （专用）
   */ 
  void updateVendorInfoVo(PartySupplier partySupplier);
  
	/*
	 * 供应商ID 查找编号和所属地区（专用）
	 *
	 * */
   PartySupplier findCodeAndRegion(@Param(value="partyId")String partyId);
   
   /**
    * 修改是否核心供应商
    * @param party
    * @since 2017年8月22日
    * @author zr.zhanghua@yikuyi.com
    */
   void update(PartySupplier partySupplier);
   
   /**
	 * 根据id，删除（专用）
	 * @param partyId
	 * @return
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void deleteByPartyId(@Param("partyId")String partyId);
	
	
	/*
	 * 供应商ID 查找编号和所属地区（专用）
	 *
	 * */
   PartySupplier getSupplierDetail(@Param(value="partyId")String partyId);
   

}