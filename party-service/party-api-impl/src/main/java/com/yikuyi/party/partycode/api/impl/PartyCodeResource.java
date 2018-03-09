package com.yikuyi.party.partycode.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.partycode.api.IPartyCodeResource;
import com.yikuyi.party.partycode.bll.PartyCodeManager;
import com.ykyframework.exception.BusinessException;

/**
 * 定义客户编码的相关接口
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/partycode")
public class PartyCodeResource implements IPartyCodeResource {

	@Autowired
	private PartyCodeManager partyCodeManager;

	/**
	 * 获取易库易编码
	 */
	@Override
	@RequestMapping(value = "/{partyCode}", method = RequestMethod.GET)
	public String getPartyCode(@PathVariable("partyCode") String partyCode) {
		return partyCodeManager.getPartyCode(partyCode);
	}

	/**
	 * 保存YKY客户编码
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public String savePartyCode(@RequestParam(required = true) String partyId, @RequestParam(required = true) String partyCode) throws BusinessException {
		return partyCodeManager.savePatyCode(partyId, partyCode);
	}

}