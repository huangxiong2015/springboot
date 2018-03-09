package com.yikuyi.party.customerssync.api.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.customerssync.api.ICustomersSyncResource;
import com.yikuyi.party.customerssync.bll.CustomersSyncManager;



/**
 * 客户数据同步
 * @author zr.helinmei@yikuyi.com
 */
@RestController
@RequestMapping("v1/customer")
public class CustomersSyncResource implements ICustomersSyncResource{
	

	@Autowired
	private CustomersSyncManager customeSyncManager;
	

	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/enterprise/fullsync", method = RequestMethod.POST)
	public long getAllEntList(@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "100")int size) {
		return customeSyncManager.getAllEntList(page,size);
	}
	
	/**
	 * 全量查询个人信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/individual/fullsync", method = RequestMethod.POST)
	public long getAllPersonalList(@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "100")int size) {
		return customeSyncManager.getAllPersonalList(page,size);
	}


	/**
	 * 查询企业数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/enterprise/incrsync", method = RequestMethod.POST)
	public long getIncrementEntList(@RequestParam(value = "createStart", required = false)String createStart,
			@RequestParam(value = "createEnd", required = false)String createEnd,
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "100")int size) {
		return customeSyncManager.getIncrementEntList(createStart,createEnd,page,size);
	}

	/**
	 * 查询个人数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/individual/incrsync", method = RequestMethod.POST)
	public long getIncrementPersonalList(@RequestParam(value = "createStart", required = false)String createStart,
			@RequestParam(value = "createEnd", required = false)String createEnd,
			@RequestParam(value = "page" , defaultValue = "1")int page, 
			@RequestParam(value = "size" , defaultValue = "100")int size) {
		return customeSyncManager.getIncrementPersonalList(createStart,createEnd,page,size);
	}
	
	
}
