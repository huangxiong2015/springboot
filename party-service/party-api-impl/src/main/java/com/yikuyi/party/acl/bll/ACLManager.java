package com.yikuyi.party.acl.bll;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.dept.dao.RoleDao;
import com.yikuyi.party.enterprise.bll.EnterpriseManager;
import com.yikuyi.party.exception.PartyBusiErrorCode;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.party.dao.PartyPermissionDao;
import com.yikuyi.party.party.dao.PartyRoleDao;
import com.yikuyi.party.vo.RoleTypeVo;
import com.yikuyi.party.vo.RoleVo;
import com.ykyframework.exception.BusinessException;

/**
 * 用户权限信息业务处理类
 * 
 * @author elvin.tang@yikuyi.com
 */
@Service
@Transactional
public class ACLManager {
	
	//资源ID固定格式	例：资源类型:操作类型:资源ID
	private static final String PERMISSION_ID = "%S:%S:%S";
	
	
	@Autowired
	private PartyRoleDao partyRoleDao;
	
	@Autowired
	private PartyPermissionDao partyPermissionDao;
	
	@Autowired
	private EnterpriseManager enterpriseManager;
	
	@Autowired
	private RoleDao roleDao;
	
    /**
     * 获取当前用户所有的角色数据
     * @param partyId
     * @return
     * @since 2017年6月16日
     * @author jik.shu@yikuyi.com
     */
	@Cacheable(value="userShiroAuthorityInfoCache", key="'roles-user-[' + #partyId + ']'")
    public Set<String> getUserRoleList(String partyId){
    	return partyRoleDao.findRoleByPartyId(partyId);
    }
    
	/**
	 * 获取当前用户所有权限数据
	 * @param partyId
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	@Cacheable(value="userShiroAuthorityInfoCache",key="'permissions-user-[' + #partyId + ']'")
	 public Set<String> getUserPermissionList(String partyId){
		 String enterpriseId = enterpriseManager.getEnterpriseIdByPartyId(partyId);
		 String partyGroupId = StringUtils.isNotEmpty(enterpriseId) ? enterpriseId : partyId;
		 return partyPermissionDao.findPermissionByPartyId(partyGroupId, partyId);
	 }

	/**
	 * 根据角色id查询用户
	 * @param role
	 * @return
	 * @since 2017年2月28日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<Party> getUserByRole(String role) {
		return partyRoleDao.getUserByRole(role);
	}
	
	/**
	 * 查询用户菜单角色
	 * @param role
	 * @return
	 * @since 2017年4月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<RoleTypeVo> getMenuRole(String roleType) {
		return partyRoleDao.getMenuRole(roleType);
	}
	
	/**
	 * 新增角色和菜单的权限关系
	 * @param vo
	 * @return
	 * @since 2017年6月16日
	 * @author jik.shu@yikuyi.com
	 */
	public void addRoelMenuRelaction(RoleVo vo) throws BusinessException{
			//先删除之前分配的角色菜单关系
			String menuId = String.format(PERMISSION_ID, vo.getMenuType(),vo.getMenuPower(),vo.getMenuId()); 
			if(menuId==null){
				throw new BusinessException(PartyBusiErrorCode.MENUID_EMPTY);
			}
			roleDao.deleteRoleByMenuId(menuId);
			String userId = RequestHelper.getLoginUserId();
			vo.setMenuId(menuId);
			vo.setPartyId(EnterpriseManager.YKY_ENTERPRISE_ID);
			vo.setCreator(userId);
			vo.setLastUpdateUser(userId);
			if(vo.getRoleIdList()!=null && CollectionUtils.isNotEmpty(vo.getRoleIdList())){
				roleDao.securityRolePermissionSave(vo);
			}else{
				throw new BusinessException(PartyBusiErrorCode.ROLEIDLIST_EMPTY);
			}
	}
}