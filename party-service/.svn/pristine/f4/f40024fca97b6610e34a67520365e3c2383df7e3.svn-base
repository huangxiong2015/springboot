package com.yikuyi.party.customerssync.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.github.pagehelper.PageInfo;

import com.yikuyi.party.contact.vo.CustomerSyncVo;
import com.yikuyi.party.customerssync.dao.CustomersSyncDao;
import com.yikuyi.party.integration.ContactsVo;
import com.yikuyi.party.integration.CustomersSyncVo;
import com.yikuyi.party.integration.CustomersSyncVo.Flag;
import com.yikuyi.party.integration.CustomersVo;
import com.ykyframework.mqservice.sender.MsgSender;
import org.springframework.beans.factory.annotation.Value;

/**
 * 客户数据同步
 * 
 * @author zr.helinmei@yikuyi.com
 */
@Service
@Transactional
public class CustomersSyncManager {
	private static final Logger logger = LoggerFactory.getLogger(CustomersSyncManager.class);

	@Autowired
	private CustomersSyncDao customersSyncDao;

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.sendIntegrateCustomers.topicName}")
	private String sendIntegrateCustomers;

	private static final String INCREMENT_FLAG = "INCREMENT_FLAG";//用来判断增量数据
	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public long getAllEntList(int page, int size) {
		CustomerSyncVo vo = new CustomerSyncVo();
		long startTime = System.currentTimeMillis();
		long result = entCommonFunction(page, size, vo,Flag.CUSTOMERS_SYNC_ALL_ENT);
		logger.info("全量同步企业数据完成，耗时:{}" , (System.currentTimeMillis() - startTime));
		return result;
	}

	private long entCommonFunction(int page, int size, CustomerSyncVo vo,Flag flag) {
		//用变量接收
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		List<CustomersVo> list = customersSyncDao.getAllEntList(vo, rowBounds);
		if (CollectionUtils.isEmpty(list)) {
			return 0;
		}
		PageInfo<CustomersVo> pageInfo = new PageInfo<>(list);
		//查询到数据，发送mq
		CustomersSyncVo voData = new CustomersSyncVo();
		voData.setCustomers(pageInfo.getList());
		
		// 用来区分全量同步企业监听
		voData.setFlag(flag);
		// 发送mq
		msgSender.sendMsg(sendIntegrateCustomers, voData, null);
		if(pageInfo.getPageNum() != pageInfo.getPages()){
			entCommonFunction(pageInfo.getPageNum()+1,size,vo,flag);
		}
		return pageInfo.getTotal();
	}

	/**
	 * 查询企业数据增量同步数据方法
	 * 
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public long getIncrementEntList(String selectStart, String selectEnd, int page, int size) {
		CustomerSyncVo vo = new CustomerSyncVo();
		// 如果没有时间则查询昨天的数据或者被修改的数据,有时间则查询选择的时间的数据
		if (!StringUtil.isNullOrEmpty(selectStart) || !StringUtil.isNullOrEmpty(selectEnd)) {
			vo.setCreateStart(selectStart);
			vo.setCreateEnd(selectEnd);
		} else {
			vo.setFlag(INCREMENT_FLAG);
		}
		long startTime = System.currentTimeMillis();
		long result = entCommonFunction(page, size, vo,Flag.CUSTOMERS_SYNC_INCREMENT_ENT);
		logger.info("增量同步企业数据完成，耗时:{}" , (System.currentTimeMillis() - startTime));
		return result;
	}

	/**
	 * 全量查询个人信息全量同步数据方法
	 * 
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	public long getAllPersonalList(int page, int size) {
		CustomerSyncVo vo = new CustomerSyncVo();
		long startTime = System.currentTimeMillis();
		long result = personalCommonFuncion(page, size, vo,Flag.CUSTOMERS_SYNC_ALL_PERSONAL);
		logger.info("全量同步企业数据完成，耗时:{}" , (System.currentTimeMillis() - startTime));
		return result;
	}

	private long personalCommonFuncion(int page, int size, CustomerSyncVo vo,Flag flag) {
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		List<ContactsVo> list = customersSyncDao.getAllUserList(vo, rowBounds);
		if (CollectionUtils.isEmpty(list)) {
			return 0;
		}
		PageInfo<ContactsVo> pageInfo = new PageInfo<>(list);
		if (CollectionUtils.isNotEmpty(list)) {
			list.stream().forEach(contactsVo -> {
				CustomersSyncVo voData = new CustomersSyncVo();
				CustomersVo customersVo = new CustomersVo();
				customersVo.setIsEnterprise("N");
				List<CustomersVo> customersVoList = new ArrayList<>();

				List<ContactsVo> contactsVoList = new ArrayList<>();
				contactsVoList.add(contactsVo);
				// 设置个人用户
				customersVo.setContacts(contactsVoList);
				customersVoList.add(customersVo);
				voData.setCustomers(customersVoList);
				voData.setFlag(flag);
				// 发送mq
				msgSender.sendMsg(sendIntegrateCustomers, voData, null);
			});

		}
		if(pageInfo.getPageNum() != pageInfo.getPages()){
			personalCommonFuncion(pageInfo.getPageNum()+1,size,vo,flag);
		}
		return pageInfo.getTotal();
	}

	/**
	 * 查询个人数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	public long getIncrementPersonalList(String selectStart, String selectEnd, int page, int size) {
		CustomerSyncVo vo = new CustomerSyncVo();
		// 如果没有时间则查询昨天的数据或者被修改的数据,有时间则查询选择的时间的数据
		if (!StringUtil.isNullOrEmpty(selectStart) || !StringUtil.isNullOrEmpty(selectEnd)) {
			vo.setCreateStart(selectStart);
			vo.setCreateEnd(selectEnd);
		} else {
			vo.setFlag(INCREMENT_FLAG);
		}
		long startTime = System.currentTimeMillis();
		long result = personalCommonFuncion(page, size, vo,Flag.CUSTOMERS_SYNC_INCREMENT_PERSONAL);
		logger.info("增量同步个人数据完成，耗时:{}" , (System.currentTimeMillis() - startTime));
		return result;
	}
}
