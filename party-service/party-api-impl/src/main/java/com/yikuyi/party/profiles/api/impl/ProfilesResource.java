package com.yikuyi.party.profiles.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.PartyProfileDefault;
import com.yikuyi.party.model.PartyProfileDefault.DefaultAddressType;
import com.yikuyi.party.profiles.api.IProfilesResource;
import com.yikuyi.party.profiles.bll.PartyProfileDefaultManager;


/**
 * 定义收货地址的相关接口
 * 
 * @author guowenyao
 *
 */

@Profile({ "dev", "sit", "uat", "hz-uat", "prod" })
@RestController
@RequestMapping("v1/profiles")
public class ProfilesResource implements IProfilesResource {

	@Autowired
	private PartyProfileDefaultManager partyProfileDefaultManager;

	/**
	 * 设置默认地址
	 */
	@Override
	@RequestMapping(value = "/default", method = RequestMethod.PUT)
	public PartyProfileDefault defaultAdress(@RequestParam(value = "id",required=true) String id, @RequestParam(value = "defaultAddress",required = true) DefaultAddressType defaultAddress) {
		// 当前登录用户
		String partyId = RequestHelper.getLoginUserId();		
		PartyProfileDefault entity = new PartyProfileDefault();
		
		entity.setPartyId(partyId);
		
		if (defaultAddress.toString().equals(DefaultAddressType.DEFAULT_SHIP_ADDR_CNY.toString())) {
			ContactMech defaultShipAddr = new ContactMech();
			defaultShipAddr.setId(id);
			entity.setDefaultShipAddrCNY(defaultShipAddr);
		} else if (defaultAddress.toString().equals(DefaultAddressType.DEFAULT_SHIP_ADDR_HK.toString())) {
			ContactMech defaultBillAddr = new ContactMech();
			defaultBillAddr.setId(id);
			entity.setDefaultShipAddrHK(defaultBillAddr);
		}
		return partyProfileDefaultManager.insertProfileDefault(entity);
	}

	
	/**
	 *  获取默认地址
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PartyProfileDefault getDefaultAdress() {
		String partyId = RequestHelper.getLoginUserId();
		return partyProfileDefaultManager.getProfileDefault(partyId);
	}

}
