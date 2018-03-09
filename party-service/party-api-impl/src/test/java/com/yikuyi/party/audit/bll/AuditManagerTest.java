package com.yikuyi.party.audit.bll;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.party.acl.bll.ACLManagerTest;
import com.yikuyi.party.audit.model.Audit;
import com.yikuyi.party.audit.vo.AuditVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class AuditManagerTest {

	@Autowired
	private AuditManager auditManager;

	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/party/audit/audits_sample.xml")
	public void testGetAduitListByEntity() {
		AuditVo auditVo = new AuditVo();
		auditVo.setUserId("888888999999");
		PageInfo<AuditVo> pageInfo = auditManager.getAduitListByEntity(auditVo,RowBounds.DEFAULT);
		assertEquals(1,pageInfo.getList().size());
	}
	

	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:com/yikuyi/party/audit/audit_sample.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:com/yikuyi/party/audit/audit_insert_result.xml")
	public void testInsert() {
		Audit audit = new Audit();
		audit.setUserId("9999999901");
		audit.setClientIp("127.0.0.1");
		audit.setServerIp("192.168.1.253");
		audit.setResource("[{\"id\":\"9999999901\",\"partyStatus\":\"PARTY_DISABLED\"}]");
		audit.setAction("User Modify");
		audit.setApplicationCode("PARTY");
		audit.setDate(new Date());
		audit.setActionId("test01");
		audit.setActionDesc("修改9999999901账号状态为PARTY_DISABLED");
		audit.setActionRst("SUCCEEDED");
		auditManager.insertAduit(audit);
	}
	
	@Resource(name = "redisTemplateTransaction")
	private HashOperations<String, String, List<ActivityProductVo>> activitProductOps;

	@Autowired
	private RedissonClient redissonClient;

	private static final Logger logger = LoggerFactory.getLogger(ACLManagerTest.class);
	@Test
	public void test() {
		Map<String,List<ActivityProductVo>> ac = new HashMap<>();
		for(int i=0;i<1000;i++){
			ac.put(String.valueOf(i), Arrays.asList(new ActivityProductVo()));
		}
		long time1 = System.currentTimeMillis();
		redissonClient.getMap("local_test").putAll(ac);
		long time2 = System.currentTimeMillis();
		logger.error("redionsson耗时：",time2-time1);
		activitProductOps.putAll("local_ss", ac);
		long time3 = System.currentTimeMillis();
		logger.error("redisTemplateTransaction耗时：",time3-time2);
	}
}