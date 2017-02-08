package com.spider.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.spider.utils.JsonUtils;

/**
 * redis config
 * @author hxuhao
 *
 */
public class RedisConfig {
	
	
	private RedisConnectionConfig connectionConfig;
	private List<RedisServerConfig> redisServers;	
	



	public RedisConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public void setConnectionConfig(RedisConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public List<RedisServerConfig> getRedisServers() {
		return redisServers;
	}

	public void setRedisServers(List<RedisServerConfig> redisServers) {
		this.redisServers = redisServers;
	}


	private static final String CONFIG_FILE_NAME = "redis.json";
	
	public static RedisConfig create(){
		String config = readConfig();
		RedisConfig redisConfig = JsonUtils.decode(config, RedisConfig.class);
		return redisConfig;
	}
	
	// read config from the file
	public static String readConfig(){


		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE_NAME);
			if(inputStream == null){
				throw new NoSuchFieldError("config file not found!");
			}
			StringBuilder sb = new StringBuilder();
			// decorator and adapter
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			//System.out.println("go");
			String line = bufferedReader.readLine();
			while(line != null){
				sb.append(line);
				line = bufferedReader.readLine();
			}
			//System.out.println(sb.toString());
			return sb.toString();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static class RedisServerConfig{
		
		private String ip;
		private int port;
		private String password;
		
		public RedisServerConfig() {
			
		}
		
		public String getIp() {
			return ip;
		}
		
		public void setIp(String ip) {
			this.ip = ip;
		}
		
		public int getPort() {
			return port;
		}
		
		public void setPort(int port) {
			this.port = port;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	
	
	public static class RedisConnectionConfig{
		private int maxTotal;
		private int maxIdle;
		private int maxWait;
		private int timeout;
		
		public RedisConnectionConfig(){
			
		}
		
		public int getMaxTotal() {
			return maxTotal;
		}
		
		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}
		
		public int getMaxIdle() {
			return maxIdle;
		}
		
		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}
		
		public int getMaxWait() {
			return maxWait;
		}
		
		public void setMaxWait(int maxWait) {
			this.maxWait = maxWait;
		}
		
		public int getTimeout() {
			return timeout;
		}
		
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
		
	}
	
}
