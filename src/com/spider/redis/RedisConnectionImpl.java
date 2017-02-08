package com.spider.redis;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;

import redis.clients.jedis.Jedis;

/**
 * redis connection 
 * @author hxuhao
 *
 */


public class RedisConnectionImpl implements RedisConnection{
	private Jedis jedis;

	public  RedisConnectionImpl(Jedis jedis) {
		this.jedis = jedis;
	}
	
	@Override
	public void close() {
		jedis.close();
	}

	@Override
	public Boolean isConnected() {
		return jedis.isConnected();
	}

	
	
	@Override
	public Long hset(String key, String field, Serializable object) {
		byte[] value = SerializationUtils.serialize(object);
		return jedis.hset(key.getBytes(), field.getBytes(), value);
	}

	@Override
	public Object hget(String key, String field) {
		// TODO Auto-generated method stub
		return SerializationUtils.deserialize(jedis.hget(key.getBytes(),field.getBytes()));
	}

	@Override
	public Long del(String... keys) {
		// TODO Auto-generated method stub
		return jedis.del(keys);
	}

	@Override
	public Long hdel(String key, String... fields) {
		// TODO Auto-generated method stub
		return jedis.hdel(key, fields);
	}

	@Override
	public Long expire(String key, int seconds) {
		// TODO Auto-generated method stub
		return jedis.expire(key, seconds);
	}

	@Override
	public Set<String> hkeys(String key) {
		// TODO Auto-generated method stub
		return jedis.hkeys(key);
	}

	@Override
	public Boolean exists(String key) {
		// TODO Auto-generated method stub
		return jedis.exists(key);
	}

	@Override
	public Long set(String key, Serializable object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
