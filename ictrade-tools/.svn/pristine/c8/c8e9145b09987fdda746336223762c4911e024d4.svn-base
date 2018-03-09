package com.ictrade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.FileUtils;
import com.ictrade.tools.JedisUtils;

import junit.framework.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis测试
 * @author tongkun
 */
public class JedisUtilsTest implements Serializable{  
	private static final long serialVersionUID = -2102953897986290485L;

	private static final Logger logger = LoggerFactory.getLogger(JedisUtilsTest.class);
	
	public String testString = "1";
	
	//连接dev环境的redis进行测试。正式环境暂且引入jedisFactory进行初始化
	public static Jedis getJedis(){
		JedisPoolConfig config = new JedisPoolConfig();
		JedisPool pool = new JedisPool(config,"192.168.1.105",6379,0,"Yky@123456",1);
		Jedis jedis = new Jedis();
		jedis = pool.getResource();
		return jedis;
	} 
	
	@Test
	public void testJedis() {
		//测试用样例数据
		List<Map<String,JedisUtilsTest>> testList = new ArrayList<>();
		Map<String,JedisUtilsTest> testMap = new HashMap<>();
		testMap.put("123", new JedisUtilsTest());
		testList.add(testMap);
		try(Jedis jedis = getJedis();//测试用样例连接对象
				//测试用序列化对象
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)){
			//设置样例缓存
			JedisUtils.batchDel(jedis, "testJedis:*");
			jedis.set("testJedis:type-string", "abc");
			oos.writeObject(testList);
			byte[] bytes = baos.toByteArray();
			jedis.set("testJedis:type-Object".getBytes(), bytes);
			
			//验证开始
			List<String> keys = JedisUtils.getByPattern(jedis, "testJedis:*");
			Assert.assertEquals(2, keys.size());
			for(String key:keys){
				//验证获取字符串
				if("testJedis:type-string".equals(key)){
					Assert.assertEquals("abc", JedisUtils.getCacheDetailString(jedis, key, JedisUtils.CacheType.AUTO));
				}
				//验证获取对象json
				else if("testJedis:type-Object".equals(key)){
					Assert.assertEquals("[{\"123\":{\"testString\":\"1\"}}]", JedisUtils.getCacheDetailString(jedis, key, JedisUtils.CacheType.AUTO));
				}
			}
			
			//验证结束删除样例缓存
			JedisUtils.batchDel(jedis, "testJedis:*");
			keys = JedisUtils.getByPattern(jedis, "testJedis:*");
			Assert.assertEquals(0, keys.size());
		} catch (IOException e) {
			logger.error("IOException",e);
		}
	}
}  