package com.yikuyi.party.customer.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.api.IFindPasswordResource;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;
import com.ykyframework.exception.BusinessException;


@RestController
@RequestMapping("v1/findpassword")
public class FindPasswordResource implements IFindPasswordResource {
	
	@Autowired
	private CustomerSummeryManager customerSummeryManager;
	
	/**
	 * 找回密码
	 * @param loginId
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{account}/password" , method = RequestMethod.PUT)
	public void reSetPassword(@PathVariable(value="account" , required = true) String account, @RequestBody UserVo vo) throws BusinessException{
		customerSummeryManager.findPassword(account, vo);
	}

	/**
	 * 发送邮件
	 */
	@Override
	@RequestMapping(value="/{mail}/mail" , method = RequestMethod.PUT)
	public void sendMail(@PathVariable(value = "mail" , required = true)String mail) throws BusinessException{
		customerSummeryManager.sendMail(mail);
	}
}