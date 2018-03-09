package com.yikuyi.party.customer.bll;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSONObject;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.basedata.ShipmentClientBuilder;
import com.yikuyi.basedata.category.model.Category;
import com.yikuyi.message.MessageClientBuilder;
import com.yikuyi.message.mail.vo.MailInfoVo;
import com.yikuyi.message.sms.vo.MailAddressValidVO;
import com.yikuyi.party.contact.vo.CompanyInfoVo;
import com.yikuyi.party.contact.vo.CreditVo;
import com.yikuyi.party.contact.vo.CustomerBaseVo;
import com.yikuyi.party.contact.vo.CustomersInfoVo;
import com.yikuyi.party.contact.vo.PersonInfoVo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserParamVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.customer.dao.PersonDao;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.login.model.UserLogin.PwdStrength;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyType;
import com.yikuyi.party.party.dao.PartyDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.partygroup.dao.PartyGroupDao;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.userLogin.dao.UserLoginDao;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.sender.MsgSender;

@Service
public class CustomerSummeryManager {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerSummeryManager.class);
	
	@Autowired
	private UserLoginDao userLoginDao;
	
	@Autowired
	private PartyGroupDao partyGroupDao;
	
	@Autowired
	private PartyRoleDao partyRoleDao;
	
	@Autowired
	private PartyDao partyDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private MsgSender msgSender;
	
	// 手机、邮箱验证码服务地址
	@Value("${api.message.serverUrlPrefix}")
	private String messageServerUrl;
	
	@Value("${customer.serverUrlPrefix}")
	private String customerUrl;
	
	@Value("${mqConsumeConfig.sendMsgAndEmail.topicName}")
	private String sendMsgAndEmailTopicName;
	
	
	//密码做正则验证符合的数量
	private static final int WEAK=1;
	private static final int MDAIAM=2;
	private static final int HIGH=3;
	//倍数
	private static final int MULTIPLE = 20;
	
	private static final int DEFAULT_SCORE = 40;
	
	
	/**
	 * 邮件模板类型
	 */
	private static final String TEMPLATEID = "RESET_PASSWORD";
	
	/**
	 * 类型（邮件：email或站内信：msg）
	 */
	private static final String MAIL_TYPE = "EMAIL";
	
	/**
	 * 邮箱验证码校验地址
	 */
	private static final String VAILD_MAIL_URL = "/getpwd.htm?action=mailvalid&sid=";
	
	
	@Autowired
	private MessageClientBuilder messageClientBuilder;
	
	@Autowired
	private ShipmentClientBuilder shipmentClientBuilder;
	
	/**
	 * 获取用户基本信息
	 * @param partyId
	 * @return
	 * @since 2017年2月6日
	 * @author tb.yumu@yikuyi.com
	 * @throws IOException 
	 */
	public UserExtendVo getUserSummeryInfo(String loginId , String partyId){
		UserLogin userLogin = userLoginDao.getUserSummeryInfo(loginId);
		if (userLogin!=null) {
			//判断是否为企业账号
			UserExtendVo vo = new UserExtendVo();
			List<String> roleTypes = userLogin.getParty()==null?null:userLogin.getParty().getRoleTypeList();
			String type = this.checkRoleType(roleTypes);
			vo.setAccounttype(type);
			Party party = userLogin.getParty();
			if (type!=null&&RoleTypeEnum.INDIVIDUAL_CUST.toString().equals(type)) {
				
				vo.setMobile(userLogin.getId());
				Person person =null;
				if (party != null) {
					person = party.getPerson();
				}
				if (person!=null) {
					vo.setName(person.getLastNameLocal());
					vo.setMail(person.getMail());
					vo.setTel(person.getTel());
					vo.setTelStatus(person.getTelStatus());
					vo.setMailStatus(person.getMailStatus());
					vo.setPersonType(person.getPersonTypeStatus().toString());
					
				}
			}else {
				vo.setMail(userLogin.getId());
				try {
						//获取企业id
						String entId = partyDao.getPartyPersonDetail(partyId).getCorporationId();
						if(StringUtils.isNotBlank(entId))
						{
							Party enterprise = partyDao.getPartyDetail(entId, PartyType.CORPORATION);
							if(null==enterprise || null==enterprise.getPartyGroup() || null==enterprise.getPartyGroup().getGroupName()) {
								logger.error("获取企业group是否有值:企业ID：{}",entId);
							}
							
							String statusId="";
							String name ="";
							if(null != enterprise && null != enterprise.getPartyGroup() && null != enterprise.getPartyGroup().getActiveStatus())
							{
								statusId = enterprise.getPartyGroup().getActiveStatus().toString();
							}
							if(null != enterprise && null != enterprise.getPartyGroup() && null != enterprise.getPartyGroup().getGroupName())
							{
								name = enterprise.getPartyGroup().getGroupName();
							}
							vo.setCompanyName(name);
							vo.setStatusId(statusId);
							if(enterprise!=null && enterprise.getPartyGroup()!=null && enterprise.getPartyGroup().getAccountPeriodStatus()!=null){
								vo.setAccountPeriodStatus(enterprise.getPartyGroup().getAccountPeriodStatus());
							}
						}
				} catch (Exception e) {
					logger.error("获取公司名字异常{partyId}：{}",partyId,e);
				}
			}
			if (party != null) {
				vo.setPartyId(party.getId());
				Person person = party.getPerson();
				if(person != null){
					vo.setLogoUrl(person.getLogoImageUrl());
					vo.setMail(person.getMail());
					vo.setTel(person.getTel());
					vo.setTelStatus(person.getTelStatus());
					vo.setMailStatus(person.getMailStatus());
					vo.setPersonType(person.getPersonTypeStatus()==null?null:person.getPersonTypeStatus().toString());
				}
				
			}
			vo.setScore(this.calcScore(userLogin.getPwdStrength()));
			vo.setStrongPwd(userLogin.getPwdStrength()==null?null:userLogin.getPwdStrength().toString());
			vo.setLastLoginTime(userLogin.getLastUpdateDate());
			return vo;
		}
		return null;
	}
	
	/**
	 * 根据partyid获取公司名字
	 * @param partyId
	 * @return
	 * @since 2017年2月6日
	 * @author tb.yumu@yikuyi.com
	 */
	public String getCompanyNameByPartyId(String partyId){
		Party party = partyGroupDao.findPartyGroupByPartyId(partyId);
		return null == party ? null : party.getPartyGroup().getGroupName();
	}
	
	/**
	 * 查找出权限中的
	 * @param roleTypes
	 * @return
	 * @since 2017年1月19日
	 * @author tb.yumu@yikuyi.com
	 */	
	public String checkRoleType(List<String> roleTypes){
		String type = null;
		if (CollectionUtils.isNotEmpty(roleTypes)) {
			for (String roleType : roleTypes) {
				if (RoleTypeEnum.ENTERPRISE_CUST.toString().equals(roleType) || RoleTypeEnum.INDIVIDUAL_CUST.toString().equals(roleType)) {
					type = roleType;
					break;
				}
			}
		}
		return type;
	}
	
	public Integer calcScore(PwdStrength pwdStrength){
		int score = DEFAULT_SCORE;
		if (pwdStrength==PwdStrength.WEAK) {
			score = score + (MULTIPLE*WEAK);
		}else if (pwdStrength==PwdStrength.MDAIAM) {
			score = score + (MULTIPLE*MDAIAM);
		}else if (pwdStrength==PwdStrength.HIGH) {
			score = score + (MULTIPLE*HIGH);
		}
		return score;
	}
	/**
	 * 账户安全 - 修改密码服务
	 * @param loginId
	 * @param userVo
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public void changePassword(String partyId,UserVo userVo) throws BusinessException{
			//原密码为空直接返回失败
			if (StringUtils.isEmpty(userVo.getPassword().trim())){
				throw new BusinessException(PartyBusiErrorCode.CIPCODE_EMPTY);
			}
			//校验原密码是否正确
		    this.checkedOldPassword(partyId, userVo.getPassword());
		    this.changePasswordByLoginId(partyId, userVo);
			
	}
	
	
	/**
	 * 找回密码
	 * @param loginId
	 * @param userVo
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public void findPassword(String loginId , UserVo userVo) throws BusinessException{
			if (StringUtils.isEmpty(loginId.trim())) {
				throw new BusinessException(PartyBusiErrorCode.PARTY_OR_ACCOUNT_EMPTY);
			}
			//解密
			String loginName = new String(Base64Utils.decodeFromString(loginId));
			//根据账户查询用户partyid
			UserLogin userLogin = userLoginDao.findEntityById(loginName);
			if(userLogin!=null && userLogin.getParty()!=null && userLogin.getParty().getId()!=null){
				this.changePasswordByLoginId(userLogin.getParty().getId(), userVo);	
			}else{
				throw new BusinessException(PartyBusiErrorCode.PARTYID_NOT_EXIST);
			}
	}
	
	/**
	 * 校验用户输入密码与原密码是否一致
	 * @param loginName
	 * @param password
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public void checkedOldPassword(String partyId,String password) throws BusinessException{

			List<UserLogin> findUserLogins = userLoginDao.findUserPassword(partyId);
			if (findUserLogins==null) {
				throw new BusinessException(PartyBusiErrorCode.OLDCIPCODE_NOT_EXIST);
			}
			String findpassword = findUserLogins.get(0).getBcryptPassword();
			boolean flag = BCrypt.checkpw(password, findpassword);
			if (!flag) {
				throw new BusinessException(PartyBusiErrorCode.CIPCODE_NOT_SAME);
			}
	
	} 
	
	/**
	 * 根据账户修改密码
	 * @param loginId
	 * @param userVo
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public void changePasswordByLoginId(String partyId,UserVo userVo) throws BusinessException{
			//为空直接返回失败
			if (StringUtils.isEmpty(userVo.getNewPassword().trim())) {
				throw new BusinessException(PartyBusiErrorCode.CIPCODE_EMPTY);
			}
			UserLogin login = new UserLogin();
			login.setCurrentPassword(userVo.getNewPassword());
			PwdStrength pwdStrength = this.checkedPwdStrong(userVo.getNewPassword());
			Integer num = userLoginDao.changePasswordByLoginId(login.getCurrentPassword(), pwdStrength.toString(), partyId);
			if (num<=0) {
				throw new BusinessException(PartyBusiErrorCode.CIPCODE_UPDATE_FAILED);
			}
	}
	
	/**
	 * 发送邮件
	 * @param mailAddr
	 * @return
	 * @since 2017年2月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public void sendMail(String mailAddr){
		String mail = new String(Base64Utils.decodeFromString(mailAddr));//base64解密邮件地址
		String mailValidCode = this.getMailCode(mail);//获取验证码
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendDate = format.format(new Date());
		//参数顺序:1 邮箱地址，  2邮箱验证码    3发送时间		
		String url =  Base64Utils.encodeToString((mail+"qqq;;;"+mailValidCode+"qqq;;;"+sendDate).getBytes());
		MailInfoVo vo = new MailInfoVo();
		vo.setTemplateId(TEMPLATEID);
		vo.setType(MAIL_TYPE);
		vo.setTo(mail);
		JSONObject content = new JSONObject();
		content.put("url", customerUrl+VAILD_MAIL_URL+url);//项目前缀+链接地址+参数
		vo.setContent(content);
		msgSender.sendMsg(sendMsgAndEmailTopicName, vo, null);
	}
	
	/**
	 * 获取邮箱验证码
	 * @param mail
	 * @return
	 * @since 2017年2月8日
	 * @author tb.yumu@yikuyi.com
	 */
	public String getMailCode(String mail){
		MailAddressValidVO vo = new MailAddressValidVO();
		String uuid = UUID.randomUUID().toString();
		vo.setMailAddress(mail);
		vo.setUuid(uuid);
		String code = "";
		try {
			code =  messageClientBuilder.mailResource(String.class).getVerifyCode(vo);
		} catch (Exception e) {
			logger.error("调用发送邮箱验证码服务错误：{},服务地址为：{}",e,code);
		}
		return code;
	}
	
	
	public UserExtendVo getUserLoginInfo(LoginUser user){
		if (user == null) {
			return null;
		}
		UserExtendVo userExtendVo = new UserExtendVo();
		Integer userType = partyRoleDao.isPersonal(user.getId());
		userExtendVo.setUserType(userType);
		if (userType==0) {
			userExtendVo.setAccounttype(RoleTypeEnum.ENTERPRISE_CUST.toString());
			userExtendVo.setMail(RequestHelper.getLoginUser().getUsername());
			//设置公司名字
			userExtendVo.setCompanyName(partyGroupDao.getEnterpriseNameByPartyId(user.getId()));
		}else if(userType==1){
			userExtendVo.setAccounttype(RoleTypeEnum.INDIVIDUAL_CUST.toString());
			userExtendVo.setMobile(RequestHelper.getLoginUser().getUsername());
		}		
		return userExtendVo;
	}
	
	/**
	 * 计算密码强度
	 * @param userPwd
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public PwdStrength checkedPwdStrong(String userPwd){
		//定义初始强度
		PwdStrength pwdStrength = PwdStrength.HIGH;
		
		//进行正则验证
		String numRegEx = "([0-9])";//数字验证规则
		String bigRegEx = "([A-Z])";//大写验证规则
		String smallRegEx = "([a-z])";//小写验证规则
		String specialRegEx="([`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？])"; //特殊字符的验证规则
		Pattern pattern1 = Pattern.compile(numRegEx);
		Matcher numFlag = pattern1.matcher(userPwd);
		Pattern pattern2 = Pattern.compile(bigRegEx);
		Matcher bigFlag = pattern2.matcher(userPwd);
		Pattern pattern3 = Pattern.compile(smallRegEx);
		Matcher smallFlag = pattern3.matcher(userPwd);
		Pattern pattern4 = Pattern.compile(specialRegEx);
		Matcher specialFlag = pattern4.matcher(userPwd);
		boolean[] array = {numFlag.find(),bigFlag.find(),smallFlag.find(),specialFlag.find()};
		int result = 0;
		for (boolean b : array) {
			if (b) {
				result++;
			}
		}
		//强度计算
		switch (result) {
		case WEAK:
			pwdStrength = PwdStrength.WEAK;
			break;
		case MDAIAM:
			pwdStrength = PwdStrength.MDAIAM;
			break;
		case HIGH:
			pwdStrength = PwdStrength.HIGH;
			break;
		default:
			break;
		}
		return pwdStrength;
	}
	
	/**
	 * 隐藏邮箱
	 * @param mail
	 * @return
	 * @since 2017年1月19日
	 * @author tb.yumu@yikuyi.com
	 */
	public String hideMail(String mail){
		if (!StringUtils.isEmpty(mail)) {
			if (checkEmail(mail)) {
				String[] mailArray = mail.split("@");
				int length = mailArray[0].length();
				return mailArray[0].substring(0, 2)+"****"+mailArray[0].substring(length-1, length)+"@"+mailArray[1];
			}else {
				return hidePhone(mail);
			}
		}
		return null;
	}

	/**
	 * 隐藏手机号
	 * @param mobile
	 * @return
	 * @since 2017年1月19日
	 * @author tb.yumu@yikuyi.com
	 */
	public String hidePhone(String mobile){
		if (!StringUtils.isEmpty(mobile)) {
			int length = mobile.length();
			return mobile.substring(0,3)+"****"+mobile.substring(length-4,length);
		}
		return null;
	}	
	
	/**
	 * 正则验证邮箱
	 * @param email
	 * @return
	 * @since 2017年2月8日
	 * @author tb.yumu@yikuyi.com
	 */
	 public boolean checkEmail(String email){
	    boolean flag = false;
	    try{
	        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	        Pattern regex = Pattern.compile(check);
	        Matcher matcher = regex.matcher(email);
	        flag = matcher.matches();
	    }catch(Exception e){
	    	logger.error("验证邮箱：{}",e);
	        flag = false;
	    }
        return flag;
	 }

	 /**
	  * 根据id查询用户信息
	  * @param partyId
	  * @return
	  * @since 2017年7月17日
	  * @author zr.aoxianbing@yikuyi.com
	  */
	public UserParamVo getAccountByPartyId(String partyId) {
		UserParamVo vo = new UserParamVo();
		vo.setPartyId(partyId);
	    //判断是企业还是个人 //如果userType为1为个人，否则为企业
		Integer userType = partyRoleDao.isPersonal(partyId);
		if (userType==0) {
			vo.setGroupName(partyGroupDao.getEnterpriseNameByPartyId(partyId));
		}else if(userType > 0){
			Party party = personDao.findPersonById(partyId);
			if(party != null && party.getPerson() != null){
				vo.setTel(party.getPerson().getTel());
			}
		}
		return vo;
	}
	
	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 * @since 2018年1月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public CustomersInfoVo getCustomersInfoById(String partyId) {
		CustomersInfoVo vo = new CustomersInfoVo();
		
		//查询用户信息
		CompanyInfoVo customerVo = personDao.getCompanyInfoById(partyId);
		String partyCode ="";
		String companyType="";
		if(null !=customerVo){
			partyCode = customerVo.getPartyCode();
			customerVo.setPartyCode(null);
			//查询企业下的所有用户
			List<PersonInfoVo> personalList = personDao.getUserByIdList(customerVo.getId());
			if(CollectionUtils.isNotEmpty(personalList)){
				customerVo.setPersonalList(personalList);
			}
			
			//根据企业id查询账期信息
			CreditVo creditVo = personDao.getPartyCreditByEntId(customerVo.getId());
			if(null !=creditVo){
				vo.setCredit(creditVo);
			}
			
			List<Category> corCategoryList = this.getCategorys("CORPORATION_CATEGORY");
			Map<String, String> corMap = categoryToMap(corCategoryList);
			// 获取公司类型
			if (!StringUtils.isEmpty(customerVo.getCustomerType())) {
				String customerType = getCategory(corMap, customerVo.getCustomerType());
				if (customerVo.getCustomerType().contains("2006")) {
					companyType = customerType + "(" + customerVo.getCustomerTypeOther() + ")";
				} else {
					companyType = customerType;
				}
			}
			customerVo.setCustomerType(null);
			
		}
		vo.setCompanyInfo(customerVo);
		CustomerBaseVo baseVo = new CustomerBaseVo();
		//所属区域
		baseVo.setArea("中国");
		//总公司
		String headOffice = personDao.getHeadOfficeByEntId(partyId);
		if(StringUtils.isNotEmpty(headOffice)){
			baseVo.setHeadOffice(headOffice);
		}
		//易库易编码
		baseVo.setPartyCode(partyCode);
		//分类
		baseVo.setCompanyType(companyType);
		
		//查询用户的创建修改日期
		Party party = personDao.findPersonById(partyId);
		if(null !=party && null != party.getPerson()){
			Person person = party.getPerson();
			//客户名称
			baseVo.setCustomerName(person.getLastNameLocal());
			vo.setCreatedDate(person.getCreatedDate());
			vo.setLastUpdateDate(person.getLastUpdateDate());
			if(StringUtils.isNotEmpty(person.getCreator())){
				Party partyCreate = personDao.findPersonById(person.getCreator());
				if(null !=partyCreate && null != partyCreate.getPerson() && StringUtils.isNotEmpty(partyCreate.getPerson().getLastNameLocal())){
					vo.setCreator(partyCreate.getPerson().getLastNameLocal());
				}
			}
			
			if(StringUtils.isNotEmpty(person.getLastUpdateUser())){
				Party partyUpdate = personDao.findPersonById(person.getLastUpdateUser());
				if(null !=partyUpdate && null != partyUpdate.getPerson() && StringUtils.isNotEmpty(partyUpdate.getPerson().getLastNameLocal())){
					vo.setLastUpdateUser(partyUpdate.getPerson().getLastNameLocal());
				}
			}
			
			
		}
		
		//基础信息
		vo.setCustomerBase(baseVo);
	
		return vo;
	}
	/**
	 * 
	 * @param categoryStr
	 * @param category
	 * @return
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public static String getCategory(Map<String, String> map, String categoryStr) {
		String cateStr = "";
		StringBuilder strBuffer = new StringBuilder();
		Map<String, String> newMap = map;
		if (StringUtils.isEmpty(categoryStr)) {
			return null;
		}
		if (newMap == null) {
			newMap = new HashMap<>();
		}
		String[] arrStr = categoryStr.split(",");
		for (String str : arrStr) {
			if (StringUtils.isEmpty(str)) {
				continue;
			}
			String value = newMap.get(str);
			if (StringUtils.isEmpty(value)) {
				strBuffer.append("/" + str);
			} else {
				strBuffer.append("/" + value);
			}

		}
		if (null !=strBuffer) {
			cateStr = strBuffer.toString();
			cateStr = cateStr.substring(1);
		}
		return cateStr;
	}

	/**
	 * 根据业务类型获取维度数据
	 * 
	 * @param category
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Category> getCategorys(String category) {
		List<Category> list = null;
		try {
			return shipmentClientBuilder.categoryResource().categoryList(category);
		} catch (Exception e) {
			logger.error("调用查询业务类型失败：{}", e);
		}
		return list;
	}
	/**
	 * 业务类型转换map
	 * 
	 * @param list
	 * @return
	 * @since 2017年4月14日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public static Map<String, String> categoryToMap(List<Category> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyMap();
		}
		return list.stream().filter(v-> !StringUtils.isEmpty(v.getCategoryId())).collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryName));
	}
}