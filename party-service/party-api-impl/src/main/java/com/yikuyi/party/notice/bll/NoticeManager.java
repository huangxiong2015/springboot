package com.yikuyi.party.notice.bll;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.party.notice.dao.NoticeDao;
import com.yikuyi.party.partyExpand.model.PartyExpand;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;

/**
 * 用户权限信息业务处理类
 * 
 * @author elvin.tang@yikuyi.com
 */
@Service
@Transactional
public class NoticeManager {
	
	@Autowired
	private NoticeDao noticeDao;
	
	

	/**
	 * 查询用户菜单角色
	 * @param role
	 * @return
	 * @since 2017年4月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyExpand> getPartyExpandList(String partyId) {
		return noticeDao.getPartyExpandList(partyId);
	}
	
	/**
	 * 新增通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 */
	public void insert(PartyExpand partyExpand) throws BusinessException{
		if(null != partyExpand){
			String userId = RequestHelper.getLoginUserId();
			String id = String.valueOf(IdGen.getInstance().nextId());
			partyExpand.setId(id);
			partyExpand.setPartyId(userId);
			partyExpand.setCreatedDate(new Date());
			partyExpand.setCreator(userId);
			PartyExpand	partyExpandNew = noticeDao.isExistMail(partyExpand.getEmail());
			if(null != partyExpandNew){
				throw new BusinessException("邮箱已经存在","邮箱已经存在");		
			}
			noticeDao.insert(partyExpand);
		}
		
	}
	
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 */
	public void update(PartyExpand partyExpand) throws BusinessException{
		if(null != partyExpand){
			String userId = RequestHelper.getLoginUserId();
			partyExpand.setLastUpdateDate(new Date());
			partyExpand.setLastUpdateUser(userId);
			PartyExpand	partyExpandNew = noticeDao.isExistMail(partyExpand.getEmail());
			if(null != partyExpandNew && !partyExpandNew.getId().equals(partyExpand.getId())){
				throw new BusinessException("邮箱已经存在","邮箱已经存在");		
			}
			noticeDao.update(partyExpand);
		}
		
	}
	/**
	 * 删除
	 * @param id
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void deleteNotice(String id){
	   noticeDao.deleteNotice(id);
	}
	

	/**
	 * 是否存在重复的邮箱
	 * @param email
	 * @return
	 * @since 2017年12月4日
	 * @author zr.helinmei@yikuyi.com
	 */
	public PartyExpand isExistMail(String email){
		return noticeDao.isExistMail(email);
	}
	
}