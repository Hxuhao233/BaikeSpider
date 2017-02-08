package com.spider.redis;

import com.spider.redis.RedisConfig.RedisConnectionConfig;
import com.spider.redis.RedisConfig.RedisServerConfig;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
	private JedisPool jedisPool;
	
	private RedisManager(){
		// get redis config
		RedisConfig config = RedisConfig.create();
		if(config == null)
			throw new IllegalStateException("Read redis config failed!");
	
		RedisServerConfig serverConfig = config.getRedisServers().get(0);
		JedisPoolConfig poolConfig = getPoolConfig(config);
		// check password
		if(serverConfig.getPassword() != null){
			jedisPool = new JedisPool(poolConfig, serverConfig.getIp(), serverConfig.getPort(), config.getConnectionConfig().getTimeout(), serverConfig.getPassword());
		}else{
			jedisPool = new JedisPool(poolConfig, serverConfig.getIp(), serverConfig.getPort(), config.getConnectionConfig().getTimeout());
		}
	}
	
	public RedisConnection getConnection(){
		return new RedisConnectionImpl(jedisPool.getResource());
	}
	
	public static class RedisManagerHolder{
		private  final static RedisManager instance = new RedisManager(); 
	}
	
	public static RedisManager getInstance(){
		return RedisManagerHolder.instance;
	}
	
	private JedisPoolConfig getPoolConfig(RedisConfig redisConfig){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(redisConfig.getConnectionConfig().getMaxIdle());
		jedisPoolConfig.setMaxTotal(redisConfig.getConnectionConfig().getMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(redisConfig.getConnectionConfig().getMaxWait());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
		return jedisPoolConfig;
	}
}
