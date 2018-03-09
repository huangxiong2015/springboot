package com.yikuyi.party.partygroup.api.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.contact.vo.MsgResultVo;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.partygroup.api.IPartyGroupResource;
import com.yikuyi.party.partygroup.bll.PartyGroupManager;
import com.yikuyi.party.vo.PartyVo;
import com.ykyframework.exception.BusinessException;

/**
 * 定义收货地址的相关接口
 * 
 * @author guowenyao
 *
 */
@RestController
@RequestMapping("v1")
public class PartyGroupResource implements IPartyGroupResource {

	@Autowired
	private PartyGroupManager partyGroupManager;


	/**
	 * 根据partId获取供应商列表
	 * 
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "partyGroup", method = RequestMethod.PUT)
	public List<PartyVo> getPartyGroupList(@RequestBody PartyGroupVo vo) {
		return  partyGroupManager.getAllPartyGroupList(vo, RowBounds.DEFAULT);
	}

	/**
	 * 根据组织名称查找记录
	 * 
	 * @author 张伟
	 * @param groupName
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value = "groups/groupname", method = RequestMethod.GET)
	public void findPartyGroupByName(@RequestParam(required = false) String groupName) throws BusinessException {
	   List<Party> party = partyGroupManager.findPartyGroupByName(groupName);
		if(CollectionUtils.isNotEmpty(party)){
			//有重复供应商
			throw new BusinessException(PartyBusiErrorCode.REPEAT_SUPPLIER);
		}
	}

	/**
	 * 物流公司
	 */
	@Override
	@RequestMapping(value = "party/allparty", method = RequestMethod.PUT)
	public List<PartyVo> getAllPartyGroupList(@RequestBody PartyGroupVo vo) {
		return partyGroupManager.getAllPartyGroupList(vo, RowBounds.DEFAULT);
	}
	
	/**
	 * 物流公司
	 */
	@Override
	@RequestMapping(value = "party/partygroups", method = RequestMethod.PUT)
	public List<PartyVo> partyGroups(@RequestBody PartyGroupVo vo) {
		return  partyGroupManager.getAllPartyGroupList(vo, RowBounds.DEFAULT);
	}
	
	
	  /**
     *购物车下定的权限控制 
     */
	@Override
	@RequestMapping(value = "party/permissions", method = RequestMethod.GET)
	public MsgResultVo orderPermissions(@RequestParam(required = false) String  partyId) {
		return partyGroupManager.orderPermissions(partyId);
	}

	/**
	 * 新增物流公司信息
	 */
	@Override
	@RequestMapping(value="/party/logistics" , method = RequestMethod.POST)
	public void insertLogisticsCompany(@RequestBody PartyGroupVo vo) {
		partyGroupManager.insertLogisticsCompany(vo);
	}

	/**
	 * 编辑物流公司信息
	 */
	@Override
	@RequestMapping(value="/party/logistics" , method = RequestMethod.PUT)
	public void updateLogisticsCompany(@RequestBody PartyGroupVo vo) {
		partyGroupManager.updateLogisticsCompany(vo);
	}

	/**
	 * 启用/停用物流公司信息
	 */
	@Override
	@RequestMapping(value="/party/logistics/status" , method = RequestMethod.PUT)
	public void changeLogisticsCompanyStatus(@RequestBody PartyGroupVo vo) {
		partyGroupManager.changeLogisticsCompanyStatus(vo);
	}

}
