package com.yikuyi.party.carrier.api.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.carrier.ICarrierResource;
import com.yikuyi.party.carrier.bll.CarrierManager;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyCarrierParamVo;
import com.yikuyi.party.vo.PartyCarrierVo;

/**
 * 物流信息管理
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/carrier")
public  class CarrierResource  implements ICarrierResource{

	
	@Autowired
	private CarrierManager carrierManager;
	
	/**
	 * 查询物流信息列表
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<PartyCarrierVo> getPartyCarrierList(@RequestParam(value = "groupName" , required = false)String groupName, 
			@RequestParam(value = "partyStatus" , required = false)PartyStatus partyStatus,
			@RequestParam(value = "createDateStart" , required = false)String createDateStart,
			@RequestParam(value = "createDateEnd" , required = false)String createDateEnd,
			@RequestParam(value = "lastUpdateDateStart" , required = false)String lastUpdateDateStart,
			@RequestParam(value = "lastUpdateDateEnd" , required = false)String lastUpdateDateEnd,
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "10")int size) {
		
			PartyCarrierParamVo carrierVo = new PartyCarrierParamVo();
			carrierVo.setGroupName(groupName);
			carrierVo.setPartyStatus(partyStatus);
			carrierVo.setCreateDateStart(createDateStart);
			carrierVo.setCreateDateEnd(createDateEnd);
			carrierVo.setLastUpdateDateStart(lastUpdateDateStart);
			carrierVo.setLastUpdateDateEnd(lastUpdateDateEnd);
			RowBounds rowBounds = new RowBounds((page-1)*size, size);
		    return carrierManager.getPartyCarrierList(carrierVo,rowBounds);
	}

	
	/**
	 * 根据partyId查询相应的物流信息
	 */
	@Override
	@RequestMapping(value="{partyId}", method = RequestMethod.GET)
	public PartyCarrierVo getPartyCarrierVoInfo(@PathVariable(value = "partyId") String partyId) {
		return carrierManager.getPartyCarriorVoInfo(partyId);
	}


	/**
	 * 物流公司信息-启用/停用
	 */
	@Override
	@RequestMapping(value = "/{partyId}",method = RequestMethod.PUT)
	public void updateStateId(@PathVariable("partyId") String partyId,@RequestBody PartyCarrierVo partyCarrierVo) {
		 carrierManager.updateStatusId(partyId,partyCarrierVo);
	}

	
	
	

	
	


}
