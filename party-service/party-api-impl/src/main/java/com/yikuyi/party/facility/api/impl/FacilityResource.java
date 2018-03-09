package com.yikuyi.party.facility.api.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.facility.api.IFacilityResource;
import com.yikuyi.party.facility.bll.FacilityManager;
import com.yikuyi.party.facility.model.Facility;

/**
 * 定义收货地址的相关接口
 * 
 * @author guowenyao
 *
 */


@RestController
@RequestMapping("v1/facility")
public class FacilityResource implements IFacilityResource {
	
	@Autowired
	private FacilityManager facilityManager;
	
	/**
	 * 根据partId获取供应商列表
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping( method = RequestMethod.GET)
	public List<Facility> getFacilityList(@RequestParam(required = true) String id) {
		return facilityManager.getFacilityList(id);
	}
	/**
	 * 根据Id获取仓库列表
	 * @param ids
	 * @return
	 * @since 2016年12月17日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value ="/batch", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<Facility> getFacility(@RequestBody List<String> ids) {
		return facilityManager.getFacility(ids);
	}
	
	/**
	 * 添加仓库信息
	 */
	@Override
	@RequestMapping(method=RequestMethod.POST)
	public Facility addFacility(@RequestBody Facility facility) {
		if (StringUtils.isNotEmpty(RequestHelper.getLoginUser().getUsername()) && StringUtils.isNotEmpty(facility.getOwnerPartyId())) {
			facility.setCreator(RequestHelper.getLoginUser().getUsername());
			facility.setLastUpdateUser(facility.getCreator());
			facility.setCreatedDate(new Date());
			facility.setLastUpdateDate(new Date());
			return facilityManager.addFacility(facility);
		}
		return facility;
	}
	
	/**
	 * 添加仓库信息
	 */
	@Override
	@RequestMapping(value="/leadMaterial",method=RequestMethod.POST)
	public Facility addFacilityFromLeadMaterial(@RequestBody Facility facility) {
		//模拟登入信息
		LoginUser loginUser = new LoginUser("9999999988", "restTemplateUser", "9999999988", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		if (StringUtils.isNotEmpty(facility.getOwnerPartyId())) {
			facility.setCreator(loginUser.getId());
			facility.setLastUpdateUser(facility.getCreator());
			facility.setCreatedDate(new Date());
			facility.setLastUpdateDate(new Date());
			return facilityManager.addFacility(facility);
		}
		return facility;
	}
	
	/**
	 * 根据仓库ID删除仓库信息
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public boolean delFacilityById(@PathVariable String  id) {
		if (StringUtils.isNotEmpty(RequestHelper.getLoginUser().getUsername()) && StringUtils.isNotEmpty(id)) {
			Facility facility= new Facility();
			facility.setId(id);
			facility.setThruDate(new Date());
			facility.setLastUpdateDate(new Date());
			facility.setLastUpdateUser(RequestHelper.getLoginUser().getId());
			 facilityManager.update(facility);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据仓库ID删除仓库信息
	 */
	@Override
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public boolean upFacilityById(@PathVariable String  id,@RequestBody Facility facility) {
		if (StringUtils.isNotEmpty(RequestHelper.getLoginUser().getUsername()) && StringUtils.isNotEmpty(id)) {
			facility.setId(id);
			facility.setLastUpdateDate(new Date());
			facility.setLastUpdateUser(RequestHelper.getLoginUser().getId());
			 facilityManager.update(facility);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据ownerPartyId删除仓库信息
	 */
	@Override
	@RequestMapping(value="ownerParty/{ownerPartyId}",method=RequestMethod.DELETE)
	public boolean delFacilityByPartyId(@PathVariable String  ownerPartyId) {
		if (StringUtils.isNotEmpty(RequestHelper.getLoginUser().getUsername()) && StringUtils.isNotEmpty(ownerPartyId)) {
			Facility facility= new Facility();
			facility.setOwnerPartyId(ownerPartyId);
			facility.setThruDate(new Date());
			facility.setLastUpdateDate(new Date());
			facility.setLastUpdateUser(RequestHelper.getLoginUser().getId());
			facilityManager.update(facility);
			return true;
		}else{
			return false;
		}
	}



}
