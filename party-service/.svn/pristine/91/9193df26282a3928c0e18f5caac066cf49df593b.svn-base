package com.yikuyi.party.shipAddress.api.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;
import com.yikuyi.party.shipAddress.api.IPartyAddressResource;
import com.yikuyi.party.shipAddress.bll.PartyContactMechManager;

/**
 * 定义收货地址的相关接口
 * 
 * @author guowenyao
 *
 */

@RestController
@RequestMapping("v1/address")
public class PartyAddressResource implements IPartyAddressResource {

	private static final Logger logger = LoggerFactory.getLogger(PartyAddressResource.class);

	@Autowired
	private PartyContactMechManager partyContactMechManager;

	/**
	 * purposeType 地址类型 currency 币种
	 */
	@RequestMapping(method = RequestMethod.GET)
	@Override
	public List<PartyContactMech> getShipAddressList(@RequestParam(required = true) PurposeType purposeType, @RequestParam(required = false) String userId, @RequestParam(required = false) Currency currency) {
		return partyContactMechManager.selectPartyContactMechByType(purposeType, userId, currency);
	}

	/**
	 * 查询SDK用户地址列表
	 */
	@RequestMapping(value = "/batch", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@Override
	public List<ContactMech> getBatchAddressList(@RequestBody List<String> contactMechIds) {
		return partyContactMechManager.selectPartyContactMechList(contactMechIds);
	}

	/**
	 * purposeType 地址类型
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Override
	public PartyContactMech getShipAddressById(@PathVariable("id") String id, @RequestParam(value = "addressType") PurposeType addressType, @RequestParam(required = false) Currency currency) {
		try {
			return partyContactMechManager.selectPartyContactMechByIdAndType(id, addressType, currency);
		} catch (IOException e) {
			logger.error("查询地址详情异常：{}", e);
			return null;
		}
	}

	/**
	 * partyContactMech需要保存的用户地址信息
	 */
	@RequestMapping(method = RequestMethod.POST)
	@Override
	public PartyContactMech save(@RequestBody PartyContactMech partyContactMech) {

		return partyContactMechManager.insert(partyContactMech);
	}

	/**
	 * id 需要更新的地址id addressType 需要更新的地址 类型 partyContactMech 需要更新的地址对象
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Override
	public PartyContactMech update(@PathVariable String id, @RequestBody PartyContactMech partyContactMech) {
		return partyContactMechManager.updateDueTime(id, partyContactMech);
	}

	/**
	 * id 需要更新的地址id addressType 需要更新的地址 类型
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Override
	public void delete(@PathVariable String id) {
		partyContactMechManager.deletePartyContactMech(id);
	}
}