package com.yikuyi.party.partygroup.dao;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.party.group.model.SupplierLeadtimeRate;

@Mapper
public interface SupplierLeadtimeRateDao {
	 
   /**
	 * 新增修改交期记录表SUPPLIER_LEADTIME_RATE
	 * @param supplierLeadtimeRate
	 * @return
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
    int insert(SupplierLeadtimeRate supplierLeadtimeRate);
}
