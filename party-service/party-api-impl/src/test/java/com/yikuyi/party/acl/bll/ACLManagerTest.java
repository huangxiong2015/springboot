package com.yikuyi.party.acl.bll;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.sound.sampled.AudioFormat.Encoding;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.yikuyi.brand.model.ProductBrand;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
public class ACLManagerTest {

	/*
	 * @Autowired private ACLManager aclManager;
	 * 
	 * @Test
	 * 
	 * @DatabaseSetup(type = DatabaseOperation.REFRESH, value =
	 * "party_role_sample.xml") public void testGetUserRoleList() { Set<String>
	 * roles = aclManager.getUserRoleList("123456"); assertTrue(roles.size() >
	 * 0); }
	 * 
	 * @Test
	 * 
	 * @DatabaseSetup(type = DatabaseOperation.REFRESH, value =
	 * "party_permission_sample.xml") public void testGetUserPermissionList() {
	 * Set<String> pers = aclManager.getUserPermissionList("123456789-01");
	 * assertTrue(pers.size() > 0); }
	 * 
	 * @Test
	 * 
	 * @DatabaseSetup(type = DatabaseOperation.REFRESH, value =
	 * "party_permission_sample.xml") public void testGetUserByRole() {
	 * List<Party> list = aclManager.getUserByRole("SUPPLIER");
	 * assertTrue(list.size() > 0); }
	 * 
	 * @Test
	 * 
	 * @DatabaseSetup(type = DatabaseOperation.REFRESH, value =
	 * "party_permission_sample.xml") public void testGetMenuRole() {
	 * List<RoleTypeVo> list = aclManager.getMenuRole("SUPPLIER");
	 * assertTrue(list.size() > 0); }
	 * 
	 * @Test public void testAddRoelMenuRelaction() throws Exception{ RoleVo vo
	 * = new RoleVo(); vo.setMenuType("1"); vo.setMenuPower("1");
	 * vo.setMenuId("1"); List<String> list = new ArrayList<>(); list.add("1");
	 * vo.setRoleIdList(list); aclManager.addRoelMenuRelaction(vo); }
	 */

	// 注入HashOperations对象
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, List<ProductBrand>> activitProductOps;

	@Autowired
	private RedissonClient redissonClient;

	public static void main(String[] args) throws Exception {
		
	}
	
	@Test
	public void test() throws Exception {
		ObjectMapper object = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
		/*object.readValue(new File("e:\\test.txt"), ProductBrand.class);*/
		
		
		List<String> stList  = IOUtils.readLines(new FileInputStream(new File("e:\\test.txt")), Charset.forName("UTF-8"));
		StringBuilder b = new StringBuilder();
		stList.stream().forEach(v->b.append(v.trim()));
		Object j = JSONObject.parse(b.toString());
		
		
		ProductBrand brand = object.readValue(object.writeValueAsString(j), ProductBrand.class);
		
		Map<String,List<ProductBrand>> ac = new HashMap<>();
		for(int i=0;i<10000;i++){
			ac.put(String.valueOf(i), Arrays.asList(brand));
		}
		for(int i=0;i<10;i++){
			long time1 = System.currentTimeMillis();
			activitProductOps.putAll("testss"+i, ac);
			long time3 = System.currentTimeMillis();
			System.out.println("redisTemplateTransaction耗时："+String.valueOf(time3-time1));
		}
		for(int i=0;i<10;i++){
			long time1 = System.currentTimeMillis();
			redissonClient.getMap("local_test"+i).putAll(ac);
			long time2 = System.currentTimeMillis();
			System.out.println("redionsson耗时："+String.valueOf(time2-time1));
		}
		Set<Object> set = new HashSet<>();
		for(int i=0;i<10000;i++){
			set.add(String.valueOf(i));
		}
		
		for(int i=0;i<10;i++){
			long time1 = System.currentTimeMillis();
			redissonClient.getMap("local_test"+i).getAll(set).values();
			long time2 = System.currentTimeMillis();
			System.out.println("redionsson查询耗时："+String.valueOf(time2-time1));
		}
		List<String> s = set.stream().map(v->v.toString()).collect(Collectors.toList());
		for(int i=0;i<10;i++){
			long time1 = System.currentTimeMillis();
			activitProductOps.multiGet("testss"+i, s);
			long time3 = System.currentTimeMillis();
			System.out.println("redisTemplateTransaction查询耗时："+String.valueOf(time3-time1));
		}
		
	}
}
