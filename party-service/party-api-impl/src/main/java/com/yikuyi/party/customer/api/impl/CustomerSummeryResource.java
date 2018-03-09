package com.yikuyi.party.customer.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.contact.vo.CustomersInfoVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.api.ICustomerSummeryResource;
import com.yikuyi.party.customer.bll.CustomerSummeryManager;
import com.ykyframework.exception.BusinessException;

@RestController
@RequestMapping("v1/customersummery")
public class CustomerSummeryResource implements ICustomerSummeryResource {

	@Autowired
	private CustomerSummeryManager customerSummeryManager;

	/**
	 * 账户基本信息
	 * 
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public UserExtendVo accountSummary() {
		String partyId = RequestHelper.getLoginUserId();
		String loginId = RequestHelper.getLoginUser().getUsername();
		return customerSummeryManager.getUserSummeryInfo(loginId, partyId);
	}

	
	/**
	 * 修改密码
	 * 
	 * @param vo
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public void changePassword(@RequestBody UserVo vo) throws BusinessException{
		String partyId = RequestHelper.getLoginUserId();
		customerSummeryManager.changePassword(partyId, vo);
	}

	/**
	 * 校验原密码是否正确
	 * 
	 * @param password
	 * @return
	 * @since 2017年1月11日
	 * @author tb.yumu@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/password/checked", method = RequestMethod.PUT)
	public void checkedOldPwd(@RequestParam(value = "password", required = true) String password) throws BusinessException{
		String partyId = RequestHelper.getLoginUserId();
		customerSummeryManager.checkedOldPassword(partyId, password);
	}

	
	/**
	 * 查询用户登录信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public UserExtendVo getUserLoginInfo() {
		LoginUser user = RequestHelper.getLoginUser();
		return customerSummeryManager.getUserLoginInfo(user);
	}


	/**
	 * 根据id查询用户信息
	 */
	@Override
	@RequestMapping(value = "/{partyId}/info", method = RequestMethod.GET)
	public UserParamVo getAccountByPartyId(@PathVariable(required=true) String partyId) {
		return customerSummeryManager.getAccountByPartyId(partyId);
	}

	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{partyId}/customersInfo", method = RequestMethod.GET)
	public CustomersInfoVo getCustomersInfoById(@PathVariable(required=true) String partyId) {
		return customerSummeryManager.getCustomersInfoById(partyId);
	}
}