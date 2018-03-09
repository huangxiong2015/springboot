package com.yikuyi.party.register.api.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.register.api.IRegisterResource;
import com.yikuyi.party.register.bll.RegisterManager;
import com.yikuyi.party.user.UserActionVo;
import com.yikuyi.party.user.UserActionVo.Action;
import com.yikuyi.party.user.UserActionVo.ActionType;
import com.yikuyi.quotationtool.inquiry.vo.InquiryCustomerMqVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 定义收货地址的相关接口
 * 
 * @author zr.aoxianbing@yikuyi.com
 *
 */

@RestController
@RequestMapping("v1/customer")
public class RegisterResource implements IRegisterResource {

	@Autowired
	private RegisterManager registerManager;

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.inquiryCustomerUpdate.topicName}")
	private String inquiryUpdateTopicName;

	@Value("${mqConsumeConfig.partyUserAction.topicName}")
	private String partyUserActionTopicName;

	/**
	 * 个人注册
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public UserVo save(@RequestBody UserVo userVo) throws BusinessException {
		String userId = registerManager.save(userVo);
		userVo.setId(userId);

		// 注册成功发送MQ
		if (StringUtils.isNotEmpty(userId)) {
			UserActionVo userAction = new UserActionVo();
			userAction.setAction(Action.REGISTER);
			userAction.setActionType(ActionType.COUPON_RECOMMEND);
			userAction.setUserId(userId);
			userAction.setEnterpriseCustomer(false);
			
			msgSender.sendMsg(partyUserActionTopicName, userAction, null);
			
			userAction.setLoginName(userVo.getMobile());
			userAction.setActionType(ActionType.PRODUCT_RECOMMEND);
			msgSender.sendMsg(partyUserActionTopicName, userAction, null);

			InquiryCustomerMqVo vo = new InquiryCustomerMqVo();
			vo.setMobileOrEmail(userVo.getMobile());
			vo.setUserId(userId);
			msgSender.sendMsg(inquiryUpdateTopicName, vo, null);
		}
		return userVo;
	}

	/**
	 * 企业注册
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/enterprise", method = RequestMethod.POST)
	public UserVo saveEnt(@RequestBody UserVo userVo) throws BusinessException {
		String userId = registerManager.saveEnt(userVo);
		userVo.setId(userId);
		// 发送mq
		if (StringUtils.isNotEmpty(userId)) {
			UserActionVo userAction = new UserActionVo();
			userAction.setAction(Action.REGISTER);
			userAction.setActionType(ActionType.COUPON_RECOMMEND);
			userAction.setUserId(userId);
			userAction.setEnterpriseCustomer(true);
			
			msgSender.sendMsg(partyUserActionTopicName, userAction, null);
			
			userAction.setLoginName(userVo.getMail());
			userAction.setActionType(ActionType.PRODUCT_RECOMMEND);
			msgSender.sendMsg(partyUserActionTopicName, userAction, null);

			InquiryCustomerMqVo vo = new InquiryCustomerMqVo();
			vo.setMobileOrEmail(userVo.getMail());
			vo.setUserId(userId);
			msgSender.sendMsg(inquiryUpdateTopicName, vo, null);
		}
		return userVo;
	}

	/**
	 * 重新发送注册邮件
	 * 
	 * @param userVo
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/reSend", method = RequestMethod.POST)
	public String reSend(@RequestBody UserVo userVo) {
		return registerManager.reSend(userVo);
	}

	/**
	 * 加入主账号
	 * 
	 * @param entId
	 * @param account
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/join", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String joinMainAccount(@RequestParam(value = "entId", required = true) String entId, @RequestParam(value = "account", required = true) String account, @RequestParam(value = "applyId", required = true) String applyId) {
		return registerManager.joinMainAccount(entId, account, applyId);
	}

	/**
	 * 生成登陆账号（根据账号）
	 * 
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/upgrade", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void upgrade(@RequestParam(value = "partyId", required = true) String partyId, @RequestParam(value = "account", required = true) String account) throws BusinessException {
		registerManager.upgrade(partyId, account);
	}

	/**
	 * 根据账号创建子账号(子账号设置密码)
	 * 
	 * @param account
	 * @param entId
	 * @return
	 * @since 2017年5月5日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/saveAccout", method = RequestMethod.POST)
	public String saveAccout(@RequestBody UserVo userVo) {
		return registerManager.saveAccout(userVo);
	}

}
