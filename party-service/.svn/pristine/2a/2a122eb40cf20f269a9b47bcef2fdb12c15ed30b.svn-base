package com.yikuyi.party.partygroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.party.contact.vo.UserManage;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.partycode.vo.PartyCodeVo;
import com.yikuyi.party.person.model.Person;
import com.yikuyi.party.vo.DeptVo;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.party.vo.SupplierVo;

@Mapper
public interface PartyGroupDao {
	
	 int insert(Party party);
	 
   /**
	 * 修改partyGoup(对条件做了非空判断)
	 * @param addressType
	 * @return
	 * @since 2016年12月12日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void updatePartyGroup(Party party);
	
	/**
	 * 修改partyGoup(条件不做非空判断)
	 * @param party
	 * @since 2017年7月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updatePartyGroupNull(Party party);
	
	/**
	 * 批量获取供应商名称或者编码
	 * @param partyIds
	 * @return
	 * @since 2017年7月13日
	 * @author jik.shu@yikuyi.com
	 */
	public List<PartyVo> getVendorNameListByIds(List<String> partyIds);
	
	/**
	 * 根据用户输入联想公司名字
	 * @return
	 * @since 2017年1月23日
	 * @author tb.yumu@yikuyi.com
	 */
	List<Party> associateEnterpriseList(@Param(value="name")String name,@Param(value="page")int page , @Param(value="size")int size);
	
	/**
	 * 根据partyid查询partygroup对象
	 * @param partyId
	 * @return
	 * @since 2017年2月7日
	 * @author tb.yumu@yikuyi.com
	 */
	Party findPartyGroupByPartyId(@Param(value="partyId")String partyId);
	
	/**
	 * 根据组织名称查找记录
	 * @author 张伟
	 * @param groupName
	 * @return
	 */
	List<Party> findPartyGroupByName(@Param(value="groupName")String groupName);
	
	/**
	 * 根据组织名称查找记录
	 * @author 张伟
	 * @param groupName
	 * @return
	 */
	List<Party> findPartyGroupByNameFull(@Param(value="groupNameFull")String groupNameFull);
	
	/**
	 * 根据goupId获取group
	 * @param groupName
	 * @return
	 */
	Party getPartyGroupByGroupId(@Param(value="partyGroupId")String partyGroupId);
	
	/**
	 * 根据条件查找详细的party信息
	 * @author 张伟
	 * @param paramPartyGroup
	 * @return
	 */
	List<PartyVo> getAllPartyVoList(PartyGroupVo param,RowBounds rowBounds);
	
	/**
	 * 如果PartyIdFrom 为空查询全部有效的供应商
	 * 如果 不为空， 负责人， 询价员，报价员 对应的供应商
	 * @param param
	 * @param rowBounds
	 * @return
	 * @since 2017年9月20日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	List<PartyVo> getVendorVoList(PartyGroupVo param,RowBounds rowBounds);
	
	/**
	 * 根据条件查找详细的party信息
	 * @author 张伟
	 * @param paramPartyGroup
	 * @return
	 */
	List<Party> getAllPartyGroupList(PartyGroupVo param,RowBounds rowBounds);
	
	
	/**
	 * 根据partyId获取公司名字
	 * @param partyId
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	String getEnterpriseNameByPartyId(@Param(value="partyId")String partyId);
	
	/**
	 * 查询供应商列表  部门主管，查询自己创建，代理，及下属创建的
	 * @param partyId
	 * @param vendorName
	 * @param vendorCode
	 * @param rowBounds
	 * @return
	 * @since 2017年7月18日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	List<SupplierVo> getVendorList(@Param(value="partyId")String partyId,@Param(value="vendorName")String vendorName,@Param(value="vendorCode")String vendorCode,RowBounds rowBounds);
	
	/**
	 * 查询供应商列表   个人  ，查询自己创建和代理的
	 * @param partyId
	 * @param vendorName
	 * @param vendorCode
	 * @param rowBounds
	 * @return
	 * @since 2017年7月18日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	List<SupplierVo> getMyVendorList(@Param(value="partyId")String partyId ,@Param(value="vendorName")String vendorName,@Param(value="vendorCode")String vendorCode,RowBounds rowBounds);
	
	/**
	 * 查询供应商详情
	 * @param partyId
	 * @return
	 * @since 2017年3月28日
	 * @author tb.yumu@yikuyi.com
	 */
	Party getVendorDetail(@Param(value="partyId")String partyId);
	
	/**
	 * 查询部门树形结构
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	List<DeptVo> deptList(@Param(value="partyId")String partyId);
	
	/**
	 * 获取子部门的结构
	 * @param parentId
	 * @param relactionName
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	List<DeptVo> findSonDeptList(@Param(value="parentId")String parentId,@Param(value="relactionName")PartyRelationshipTypeEnum relactionName);
	
	/**
	 * 获取部门详情
	 * @param partyId
	 * @return
	 * @since 2017年5月5日
	 * @author tb.yumu@yikuyi.com
	 */
	DeptVo deptDetail(@Param(value="partyId")String partyId);
	
	/**
	 * 根据部门ID获取包括本部门及下面的所有子类部门ID
	 * @param partyId
	 * @return
	 * @since 2017年5月9日
	 * @author tb.yumu@yikuyi.com
	 */
	String findSubDeptId(@Param(value="deptId")String deptId);
	
	/**
	 * 获取部门详情
	 * @param 数组partyId
	 * @return
	 * @since 2017年5月9日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	List<UserManage> findCustomerByDeptId(@Param(value="deptId")String[] deptId,RowBounds rowBounds);
	
	/**
	 * 格局用户ID获取部门详情      所有数据
	 * @param 数组partyId
	 * @return
	 * @since 2017年5月16日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	UserManage findRoleNameByPartyId(@Param(value="partyId")String partyId);
	
	/**
	 * 查找部门编码
	 * @param PartyCodeVo
	 * @return
	 * @since 2017年5月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	PartyCodeVo findCodeVoById(String id);
	
	/**
	 * 根据角色名称找用户信息list   redis监控用
	 * @param roleName
	 * @return
	 * @since 2017年6月15日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	List<Person> findPersonByRoleName(String roleName);
	
	
	/**
	 * 失效账号
	 * @param role
	 * @return
	 * @since 2017年6月28日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void invalidAccount(@Param("id") String id,@Param("comments") String comments,@Param("verifyStatus") ActiveStatus verifyStatus);
	
	/**
	 * 插入供应商   专用(供应商管理)
	 * @param role
	 * @return
	 * @since 2017年8月14日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void insertPartyGroup(PartyGroup partyGroup);
	
	/**
	 * 根据部门ID 查询 部门名称信息
	 * @param partyId
	 * @return
	 * @since 2017年8月16日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public DeptVo findDeptInfo(@Param("partyId") String partyId);
	
	/**
	 * 更新供应商  专用(供应商基础信息)
	 * @param 
	 * @return
	 * @since 2017年8月22日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void updateVendor(PartyGroup partyGroup);
	
	/**
	 * 查找供应商的激活状态  账号状态
	 * @param 
	 * @return
	 * @since 2017年8月28日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public PartyGroup findStatus(@Param("partyId") String partyId);
	
	/**
	 * 更改供应商   激活状态，账号状态
	 * @param role
	 * @return
	 * @since 2017年8月29日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void updateStatus(PartyGroup partyGroup);
	
	/**
	 * 根据id，删除用户（专用）
	 * @param partyId
	 * @return
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public void deleteByPartyId(@Param("partyId")String partyId);
	
	/**
	 * 查询供应商简称  是否唯一性
	 * @param partyId
	 * @param 
	 * @return
	 * @since 2017年8月31日
	 * @author tb.huangqingfeng@yikuyi.com
	 */
	public PartyGroup findGroupName(@Param("partyId")String partyId,@Param("groupName")String groupName);
	
	/**
	 * 查询供应商列表，交期准确率数据
	 * @param vendorName
	 * @return PageInfo<PartyVo>
	 * @since 2017年11月14日
	 * @author zr.helinmei@yikuyi.com
	 */
	List<SupplierVo> getPartySupplierList(@Param(value="vendorName")String vendorName,@Param(value="orderSq")String orderSq,RowBounds rowBounds);
	
}