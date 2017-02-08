package com.spider.redis;

import java.io.Serializable;
import java.util.Set;

/**
 * redis connection interface
 * @author hxuhao
 *
 */
public interface RedisConnection {
	
	void close();
	
	Boolean isConnected();
	
	Long set(String key,Serializable object);
	
	Object get(String key);
	
	Long hset(String key, String field, Serializable object);

    Object hget(String key, String field);

    Long del(String... keys);

    Long hdel(String key, String... fields);

    Long expire(String key, int seconds);

    Set<String> hkeys(String key);

    Boolean exists(String key);
}
