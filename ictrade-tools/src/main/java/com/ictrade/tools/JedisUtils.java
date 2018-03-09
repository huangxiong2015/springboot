package com.ictrade.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykyframework.exception.SystemException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author tongkun Jedis 相关的操作
 */
public class JedisUtils<T> {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);

	public enum CacheType {
		/**
		 * 字符串
		 */
		STRING,
		/**
		 * 对象
		 */
		OBJECT,
		/**
		 * 自动。先尝试转换为对象，如果失败则转换为字符串
		 */
		AUTO
	}

	/**
	 * 批量删除redis缓存方法
	 * 
	 * @param jedis
	 *            连接
	 * @param pattern
	 *            要删除的匹配正则
	 */
	public static void batchDel(Jedis jedis, String pattern) {
		Set<String> set = jedis.keys(pattern);
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String keyStr = it.next();
			logger.debug(keyStr);
			jedis.del(keyStr);
		}
	}

	/**
	 * 根据正则批量获取缓存
	 * 
	 * @param jedis
	 *            连接
	 * @param pattern
	 *            要匹配的正则
	 * @return 获取结果
	 */
	public static List<String> getByPattern(Jedis jedis, String pattern) {
		return jedis.keys(pattern).stream().collect(Collectors.toList());
	}

	/**
	 * 根据类型获取缓存的值（字符串）
	 * 
	 * @param jedis
	 *            链接对象
	 * @param key
	 *            要获取的key
	 * @param type
	 *            值的类型。如果是对象则转换为json，如果是字符串则直接返回。如果是AUTO则先尝试转换为对象，失败则直接返回字符串
	 * @return 获取到的缓存的字符串。
	 * @since 2017年6月26日
	 * @author tongkun@yikuyi.com
	 */
	public static String getCacheDetailString(Jedis jedis, String key, CacheType type) {
		String result = null;
		// 对象类型的缓存
		if (CacheType.OBJECT.equals(type)) {
			try {
				Object obj = getCacheDetailObject(jedis, key);
				if (obj != null) {
					ObjectMapper om = new ObjectMapper();
					result = om.writeValueAsString(obj);
				}
			} catch (ClassNotFoundException e) {
				result = "没有引入这个类：" + e.getMessage() + " 原始内容：\n" + jedis.get(key);
			} catch (JsonProcessingException e) {
				result = "无法转换为json：" + e.getMessage() + " 原始内容：\n" + jedis.get(key);
			} catch (IOException e) {
				result = "不是对象：" + e.getMessage() + " 原始内容：\n" + jedis.get(key);
			}
		}
		// 字符串类型的缓存
		else if (CacheType.STRING.equals(type)) {
			result = jedis.get(key);
		}
		// 自动识别的缓存
		else if (CacheType.AUTO.equals(type)) {
			try {
				Object obj = getCacheDetailObject(jedis, key);
				if (obj != null) {
					ObjectMapper om = new ObjectMapper();
					result = om.writeValueAsString(obj);
				}
			} catch (ClassNotFoundException e) {
				result = "没有引入这个类：" + e.getMessage() + " 原始内容：\n" + jedis.get(key);
			} catch (JsonProcessingException e) {
				result = "无法转换为json：" + e.getMessage() + " 原始内容：\n" + jedis.get(key);
			} catch (IOException e) {
				result = jedis.get(key);
			}
		}
		return result;
	}

	/**
	 * 根据key获取缓存的值（对象）
	 * 
	 * @param jedis
	 *            链接对象
	 * @param key
	 *            要获取的key
	 * @return 缓存的对象
	 * @since 2017年6月26日
	 * @author tongkun@yikuyi.com
	 * @throws IOException
	 *             不是一个对象
	 * @throws ClassNotFoundException
	 *             当前项目没有引用这个类，无法解析
	 */
	public static Object getCacheDetailObject(Jedis jedis, String key) throws ClassNotFoundException, IOException {
		String type = jedis.type(key.split(":")[0]);
		if ("hash".equals(type)) {
			String[] keys = key.split(":");
			if (keys.length > 1) {
				return JedisSerializeUtil.unserializeSet(jedis.hmget(keys[0].getBytes(), keys[1].getBytes()));
			} else {
				return JedisSerializeUtil.unserializehmbb2moo(jedis.hgetAll(keys[0].getBytes()));
			}
		} else {
			return getCacheObject(jedis, key);
		}
	}

	/**
	 * 根据key获取缓存的值（对象）
	 * 
	 * @param jedis
	 *            链接对象
	 * @param key
	 *            要获取的key
	 * @return 缓存的对象
	 * @since 2017年6月26日
	 * @author tongkun@yikuyi.com
	 * @throws IOException
	 *             不是一个对象
	 * @throws ClassNotFoundException
	 *             当前项目没有引用这个类，无法解析
	 */
	public static Object getCacheObject(Jedis jedis, String key) throws ClassNotFoundException, IOException {
		byte[] bytes = jedis.get(key.getBytes());
		if (bytes == null || bytes.length == 0)
			return null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> batchetCacheDetailObject(Jedis jedis, List<String> keys) {
		Pipeline p = jedis.pipelined();
		Map<String, Response<byte[]>> responses = new HashMap<>(keys.size());
		for (String key : keys) {
			responses.put(key, p.get(key.getBytes()));
		}
		p.sync();
		List<T> returnList = new ArrayList<>(responses.size());
		responses.values().stream().forEach(v -> returnList.add((T) getCacheDetailObject(v.get())));
		return returnList;
	}

	public static Object getCacheDetailObject(byte[] bytes) {
		if (null == bytes || bytes.length == 0) {
			return null;
		}
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		} catch (Exception e) {
			throw new SystemException("批量获取jedis商品转换异常", e);
		}
	}

	/**
	 * 根据泛型的类，模糊搜索缓存并返回
	 * 
	 * @param jedis
	 *            连接对象
	 * @param pattern
	 *            搜索用正则
	 * @return 取得结果
	 * @since 2017年7月3日
	 * @author tongkun@yikuyi.com
	 */
	public List<T> getObjectByPattern(Jedis jedis, String pattern) {
		List<String> keys = getByPattern(jedis, pattern);
		return batchetCacheDetailObject(jedis, keys);
	}
}