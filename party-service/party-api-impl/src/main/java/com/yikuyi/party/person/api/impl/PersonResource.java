package com.yikuyi.party.person.api.impl;




import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.person.api.IPersonResource;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.userLogin.bll.UserLoginManager;
import com.yikuyi.party.vendor.vo.Vendor.Currency;


/**
 * 定义收货地址的相关接口
 * 
 * @author zr.aoxianbing@yikuyi.com
 *
 */


@RestController
@RequestMapping("v1/person")
public class PersonResource implements IPersonResource {

	@Autowired
	private UserLoginManager userLoginManager;

	/**
	 * 查询账号列表
	 * @param name
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public PageInfo<UserVo> search(@RequestParam(value ="name", required = false) String name,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		RowBounds rowBouds = new RowBounds((page-1)*size, size);
		String userId = RequestHelper.getLoginUserId();
		return userLoginManager.search(name,rowBouds,userId,page,size);
	}
	
	/**
	 * 查询角色下所有邮件集合
	 */
	@Override
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public List<Person> getEmailListByRoleType(
			@RequestParam(value = "roleType", required = true) String roleType) {
		return userLoginManager.getEmailListByRoleType(roleType);
	}
	
	/**
	 * 新增账号
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public String save(@RequestBody UserVo userVo) {
		String userId = RequestHelper.getLoginUserId();
		userVo.setId(userId);
		return userLoginManager.save(userVo);
	}
	/**
	 * 修改账号
	 * @param userVo
	 * @return
	 * @since 2017年3月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/user",method=RequestMethod.PUT)
	public String update(@RequestBody UserVo userVo) {
		String userId = RequestHelper.getLoginUserId();
		userVo.setPartyId(userId);
		return userLoginManager.update(userVo);
	}

	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public UserVo getPerson(@PathVariable("id") String id) {
		return userLoginManager.getPerson(id);
	}
	/**
	 * 重置密码
	 * @param userVo
	 * @return
	 * @since 2017年3月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String updatePwd(@PathVariable("id") String id,@RequestParam(value ="passWord", required = true)String passWord) {
		String userId = RequestHelper.getLoginUserId();
		return userLoginManager.updatePwd(userId,passWord,id);
	}
	/**
	 * 查询供应商可分配人员
	 */
	@Override
	@RequestMapping(value="/reportsto" , method = RequestMethod.GET)
	public List<UserExtendVo> getReportsTo(@RequestParam(value = "partyId" , required = true)String partyId) {
		return userLoginManager.getReportsTo(partyId);
	}

	/**
	 * 根据partId获取用户信息
	 */
	@Override
	@RequestMapping(value ="/batch", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<UserVo> getPartyByIds(@RequestBody List<String> ids) {
		return userLoginManager.getPartyByIds(ids);
	}

	
	/**
	 *  新增部门下相关用户
	 */
	@Override
	@RequestMapping(value ="/adduser", method = RequestMethod.POST)
	public void addUser(@RequestBody UserVo vo) {
		userLoginManager.addUser(vo);
		
	}

	
	/**
	 * 根据ID查询用户详情
	 */
	@Override
	@RequestMapping(value ="/detail", method = RequestMethod.GET)
	public UserVo getUserDetail(@RequestParam(value="partyId" , required = true)String partyId) {
		return userLoginManager.getUserDetail(partyId);
	}

	
	/**
	 * 编辑用户信息
	 */
	@Override
	@RequestMapping(value ="/updateuser", method = RequestMethod.PUT)
	public void updateUser(@RequestBody UserVo vo) {
		userLoginManager.updateUser(vo);
		
	}

	/**
	 *  根据partyId查询账期信息
	 */
	@Override
	@RequestMapping(value="/{partyId}/credit" , method = RequestMethod.GET)
	public PartyCreditVo getPartyCreditInfo(@PathVariable(value="partyId",required=true)String partyId,
			@RequestParam(value="currency" , required = false)Currency currency) {
		return userLoginManager.getPartyCreditVo(partyId,currency);
	}
	
	/**
	 * 批量查询用户的账期信息
	 */
	@Override
	@RequestMapping(value="/credit/list" , method = RequestMethod.POST)
	public List<PartyCreditVo> getPartyCreditInfoList(@RequestBody String partyList) {
		return userLoginManager.getPartyCreditInfoList(partyList);
	}

	/**
	 * 根据partyId修改用户的账期信息
	 * 
	 */
	@Override
	@RequestMapping(value="/credit" , method = RequestMethod.PUT)
	public void updatePartyCredit(@RequestBody PartyCredit partyCredit) {
		userLoginManager.updatePartyCredit(partyCredit);
	}

	

	
}
