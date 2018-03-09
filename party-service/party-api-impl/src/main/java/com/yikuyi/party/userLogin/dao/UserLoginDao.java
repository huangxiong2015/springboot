package com.yikuyi.party.userLogin.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.contact.vo.EnterpriseParamVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.login.model.UserLogin;


@Mapper
public interface UserLoginDao {

	/**
	 * 根据账号查询count（手机、邮箱）
	 * @param account
	 * @return
	 * @since 2017年1月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Integer isExist(@Param("account") String account);
	
	/**
	 * 根据账户查询用户密码
	 * @param loginId
	 * @return
	 * @since 2017年1月12日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<UserLogin> findUserPassword(@Param("partyId")String partyId);
	
	/**
	 * 根据用户id修改密码
	 * @param loginId
	 * @return
	 * @since 2017年1月13日
	 * @author tb.yumu@yikuyi.com
	 */
	public Integer changePasswordByLoginId(@Param("password")String password,@Param("pwdStrength")String pwdStrength, @Param("partyId")String partyId);
	
	/**
	 * 查询用户基本信息
	 * @param loginId
	 * @return
	 * @since 2017年1月19日
	 * @author tb.yumu@yikuyi.com
	 */
	public UserLogin getUserSummeryInfo(String loginId);
	/**
	 * 根据用户id查询用户基本信息
	 * @param loginId
	 * @return
	 * @since 2017年1月22日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserLogin findEntityById(String id);

	/**
	 * 保存
	 * @param userLogin
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void insert(UserLogin userLogin);

	/**
	 * 根据id更新
	 * @param userLogin
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void update(UserLogin userLogin);
	/**
	 * 根据partid 更新
	 * @param userLogin
	 * @since 2017年1月19日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updateByPartyId(UserLogin userLogin);
	
	/**
	 * 分页查询个人用户列表
	 * @param phone
	 * @param mail
	 * @param name
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @param rowBounds
	 * @return
	 * @since 2017年2月24日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<UserExtendVo> findCustomerUser(EnterpriseParamVo param, RowBounds rowBounds);
	
	/**
	 * 不分页查询个人用户列表
	 * @param phone
	 * @param mail
	 * @param name
	 * @param status
	 * @param registerStart
	 * @param registerEnd
	 * @param lastLoginStart
	 * @param lastLoginEnd
	 * @return
	 * @since 2017年2月24日
	 * @author tb.yumu@yikuyi.com
	 */
	List<UserLogin> findCustomerUser(@Param("phone") String phone,
			@Param("mail") String mail,
			@Param("name") String name,
			@Param("status") String status,
			@Param("registerStart") String registerStart,
			@Param("registerEnd") String registerEnd,
			@Param("lastLoginStart") String lastLoginStart,
			@Param("lastLoginEnd") String lastLoginEnd);

	List<UserLogin> findUserBypartyid(@Param("list")List<String> partyIds);
	
	/**
	 * 获取唯一的userLoingId
	 * 
	 * @param id,type登录类型
	 * @return
	 * @since 2016年1月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	public String findUserLogin(@Param("id")String id,@Param("type") String type);
	
	
	/**
	 * 根据用户login登录信息，获取用户基本信息
	 * @param userId
	 * @return
	 * @since 2017年3月13日
	 * @author guowenyao@yikuyi.com
	 */
	public User findUserInfo(@Param("userId")String userId);

	/**
	 *  根据用户名称或者邮箱账号，获取用户id集合
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<String> getUserIdsByName(@Param("name")String name);

	/**
	 * 根据用户id集合查询用户信息
	 * @param ids
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<UserVo> getUserLoginsByIds(@Param("ids")List<String> ids);
	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public UserVo getPerson(@Param("id")String id);

	/**
	 * 获取当天注册企业数量
	 * @return
	 * @since 2017年3月23日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public Long getRegNumToday();
	
	/**
	 * 获取今天登录数量
	 * @return
	 * @since 2017年3月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getLoginNumToday();

	/**
	 * 更新账号
	 * @param userLogin
	 * @since 2017年5月3日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updateAccount(UserLogin userLogin);
	
	/**
	 * 获取运营后台新增的用户详情
	 * @param partyId
	 * @return
	 * @since 2017年5月11日
	 * @author tb.yumu@yikuyi.com
	 */
	public List<UserVo> getUserDetail(@Param("partyId")String partyId);
	/**
	 * 根据id和登陆类型查询用户
	 * @param id
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com  
	 */
	public UserLogin findEntityByIdAndType(@Param("partyId")String partyId, @Param("userLoginMethod")String userLoginMethod);
	
	public UserLogin findEntityByIdAndAccount(@Param("partyId")String partyId, @Param("id")String id);
	
	/**
	 * 获取个人当天注册数或者总数
	 * @param flag 不为空则查询当天个人会员注册数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getPersonalData(@Param("flag")String flag);
	
	
	
	/**
	 * 获取认证会员当天数或者总数
	 * @param flag 不为空则查询当天认证会员数
	 * @return
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getAuthenticationData(@Param("flag")String flag);
	
	/**
	 * 获取昨天登录数量
	 * @return
	 * @since 2017年3月23日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Long getLoginNumYesterday();
	
}