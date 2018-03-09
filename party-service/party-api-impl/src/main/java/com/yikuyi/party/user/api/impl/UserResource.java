package com.yikuyi.party.user.api.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.party.bll.PartyAttributeManager;
import com.yikuyi.party.user.bll.UserManager;

@EnableAutoConfiguration
@RestController
@RequestMapping("v1/users")
public class UserResource {

	@Autowired
	private UserManager userManager;

	@Autowired
	private PartyAttributeManager partyAttributeManager;

	/**
	 * 根据用户名获取用户id
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/validated/{username}", method = RequestMethod.GET)
	public String getUserByAccount(@PathVariable String username) {
		String usernameStr = new String(Base64Utils.decodeFromString(username));// 解密
		return userManager.getUserByAccount(usernameStr);
	}

	/**
	 * 根据用户Id,获取用户和企业信息
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUserInfo(@PathVariable String userId) {
		return userManager.getUserInfo(userId);
	}


	/**
	 * 根据用户Id,获取用户属性信息
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/{userId}/attributes/{attrName}", method = RequestMethod.GET)
	public PartyAttribute getUserAttributes(@PathVariable String userId, @PathVariable String attrName) {
		// String attrName = "SALES-COUPON-CODE"; //3位销售人员编码
		List<PartyAttribute> list = partyAttributeManager.getPartyAttributelist(userId, attrName);
		if (!CollectionUtils.isEmpty(list)) {
			for (PartyAttribute partyAttribute : list) {
				if (attrName.equals(partyAttribute.getKey())) {
					return partyAttribute;
				}
			}
		}
		PartyAttribute pa = new PartyAttribute();
		pa.setKey("SALES-COUPON-CODE");
		// 因soner扫描问题改一下实现方式
		pa.setValue(String.valueOf(new java.util.Random().nextInt(900) + 100));
		return pa;
	}
}