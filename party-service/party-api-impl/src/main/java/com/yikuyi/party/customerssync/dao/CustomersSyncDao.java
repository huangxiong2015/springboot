package com.yikuyi.party.customerssync.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.contact.vo.CustomerSyncVo;
import com.yikuyi.party.integration.ContactsVo;
import com.yikuyi.party.integration.CustomersVo;

/**
 * 客户数据同步
 * @author zr.helinmei@yikuyi.com
 */
@Mapper
public interface CustomersSyncDao {
	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<CustomersVo> getAllEntList(CustomerSyncVo vo,RowBounds rowBounds);
	/**
	 * 查询企业下的所有用户同步数据方法
	 * @param rowBounds
	 * @return List<ContactsVo>
	 * @since 2017年12月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<ContactsVo> getUserByIdList(@Param("id")String id);
	
	
	/**
	 * 全量查询个人信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<ContactsVo> getAllUserList(CustomerSyncVo vo,RowBounds rowBounds);
	
	
	
}
