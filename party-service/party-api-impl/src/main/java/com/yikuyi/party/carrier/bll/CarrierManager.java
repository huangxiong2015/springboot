package com.yikuyi.party.carrier.bll;

import java.util.Date;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.vo.PartyCarrierParamVo;
import com.yikuyi.party.vo.PartyCarrierVo;

@Service
@Transactional
public class CarrierManager {

	private static final Logger logger = LoggerFactory.getLogger(CarrierManager.class);
	
	@Autowired
	private PartyDao partyDao;
	
	@Autowired
	private PartyGroupDao partyGroupDao;

	/**
	 * 查询物流信息列表
	 * @param carrierVo
	 * @param rowBounds
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<PartyCarrierVo> getPartyCarrierList(PartyCarrierParamVo param, RowBounds rowBounds) {
		return new PageInfo<>(partyDao.getPartyCarrierList(param, rowBounds));
	}

	
	/**
	 * 根据partyId查询相应的物流信息
	 * @param partyId
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PartyCarrierVo getPartyCarriorVoInfo(String partyId) {
		return partyDao.getPartyCarriorVoInfo(partyId);
	}


	/**
	 * 物流公司信息-启用/停用
	 * @param partyId
	 * @param statusId
	 * @return
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updateStatusId(String partyId, PartyCarrierVo partyCarrierVo) {
		if(partyCarrierVo!=null && partyCarrierVo.getPartyStatus()!=null){
				try {
					if((PartyStatus.PARTY_ENABLED.toString()).equals(partyCarrierVo.getPartyStatus().toString()) 
							|| (PartyStatus.PARTY_DISABLED.toString()).equals(partyCarrierVo.getPartyStatus().toString())){
					partyDao.updateStateId(partyId,partyCarrierVo.getPartyStatus().toString());
					}
				} catch (Exception e) {
					logger.error("物流公司状态操作:{}", e);
				}
		}else if(partyCarrierVo!=null){
			try {
				if(partyCarrierVo.getGroupName()!=null || partyCarrierVo.getCreator()!=null){
					Party party = new Party();
					party.setId(partyId);
					PartyGroup group = new PartyGroup();
					group.setGroupName(partyCarrierVo.getGroupName());
					group.setCreator(partyCarrierVo.getCreator());
					group.setLastUpdateDate(new Date());
					party.setPartyGroup(group);
					partyGroupDao.updatePartyGroup(party);
				}
			} catch (Exception e) {
				logger.error("修改物流公司名称:{}",e);
			}
		}
	}

	

	
}
