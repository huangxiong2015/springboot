package com.yikuyi.party.notice.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.party.notice.api.INoticeResource;
import com.yikuyi.party.notice.bll.NoticeManager;
import com.yikuyi.party.partyExpand.model.PartyExpand;
import com.ykyframework.exception.BusinessException;

/**
 * 用户权限信息接口
 * 
 * @author elvin.tang@yikuyi.com
 */
@RestController
@RequestMapping("v1/notice")
public class NoticeResource implements INoticeResource {

	@Autowired
	private NoticeManager noticeManager;

	/**
	 * 查询用户通知
	 * @param partyId
	 * @return
	 * @since 2017年11月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping( method = RequestMethod.GET)
	public List<PartyExpand> getPartyExpandList(@RequestParam("partyId") String partyId) {
		return noticeManager.getPartyExpandList(partyId);
	}

	

	/**
	 * 新增通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody PartyExpand partyExpand) throws BusinessException{
		noticeManager.insert(partyExpand);
	}
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody PartyExpand partyExpand)throws BusinessException{
		noticeManager.update(partyExpand);
	}
	
	/**
	 * 删除通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteNotice(@PathVariable(value = "id")String id){
		noticeManager.deleteNotice(id);
	}


	@Override
	@RequestMapping(value = "/isExistMail", method = RequestMethod.GET)
	public PartyExpand isExistMail(@RequestParam(value = "email", required = true) String email) {
		return noticeManager.isExistMail(email);
	}

}