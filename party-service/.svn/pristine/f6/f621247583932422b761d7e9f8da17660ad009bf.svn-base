package com.yikuyi.party.enterprise;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.pagehelper.PageInfo;
import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.group.model.PartyGroup;

public class EnterpriseResourceTest extends BaseTest {
	

	/**
	 * 根据用户的id判断是否为管理员
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void isAdmin() {
		Boolean falg = super.partyClient.enterpriseClient().isAdmin("YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(false,falg);
	}
	
	/**
	 * 根据用户的id判断是否为首次去激活或者关联
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void isFristActive() {
		Boolean falg = super.partyClient.enterpriseClient().isFristActive("YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(false,falg);
	}
	
	/**
	 * 根据登录用户查找公司名称
	 * @param id
	 * @return
	 * @since 2017年1月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getEntBaseInfo() {
		PartyGroup party= super.partyClient.enterpriseClient().getEntBaseInfo("9999999901");
		Assert.assertEquals("易库易",party.getGroupName());
	}
	
	
	/**
	 * 
	 * 账期订单列表查询(带分页)
	 * @since 2017年8月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyCreditVoListTest(){
		PageInfo<PartyCreditVo> pageInfo = super.partyClient.enterpriseClient().getPartyCreditVoList(null, null, null, 1, 10, "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(pageInfo.getList().size() > 0);
	}
	
	
	/**
	 * 根据企业id(corporationId查询用户的账期信息)
	 * 
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyCreditVoByCorporationIdTest(){
		PartyCreditVo partyCreditVo = super.partyClient.enterpriseClient().getPartyCreditVoByCorporationId("707532139826227201", "YWRtaW46OTk5OTk5OTkwMQ==");
		if(partyCreditVo!=null){
			Assert.assertEquals("9999999901",partyCreditVo.getCreator());
		}
	}
	
	
	
	/**
	 * 根据企业id查询partyIds
	 * @since 2017年8月21日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyIdListTest(){
		List<String> list = super.partyClient.enterpriseClient().getPartyIdList("897361616515891200", "YWRtaW46OTk5OTk5OTkwMQ==");
		if(list!=null && list.size()>0){
			Assert.assertTrue(list.size()>0);
		}
	}
	

}