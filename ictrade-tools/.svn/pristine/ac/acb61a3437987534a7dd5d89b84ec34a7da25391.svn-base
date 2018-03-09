package com.ictrade.tools;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ykyframework.exception.SystemException;

public final class JedisSerializeUtil {

	private JedisSerializeUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * unserialize byte[]
	 * 
	 * @param bytes
	 * @return Object
	 */
	public static Object unserialize(final byte[] bytes) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * unserialize hash Map<byte[], byte[]>
	 * 
	 * @param hash
	 * @return Map<Object, Object>
	 */
	public static List<Object> unserializeSet(final Collection<byte[]> hashs) {
		List<Object> result = new ArrayList<>();
		if (null == hashs || hashs.isEmpty()) {
			return result;
		}
		hashs.stream().forEach(v -> result.add(unserialize(v)));
		return result;
	}

	/**
	 * unserialize hash Map<byte[], byte[]>
	 * 
	 * @param hash
	 * @return Map<Object, Object>
	 */
	public static Map<Object, Object> unserializehmbb2moo(final Map<byte[], byte[]> hash) {
		Map<Object, Object> result = new HashMap<>();
		if (null == hash || hash.isEmpty()) {
			return result;
		}
		try {
			Set<Entry<byte[], byte[]>> keys = hash.entrySet();
			Iterator<Entry<byte[], byte[]>> iterator = keys.iterator();
			while (iterator.hasNext()) {
				Entry<byte[], byte[]> temp = iterator.next();
				result.put(new String(temp.getKey()), unserialize(temp.getValue()));
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
		return result;
	}

}
