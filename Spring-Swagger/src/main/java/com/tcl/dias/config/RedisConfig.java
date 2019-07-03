package com.tcl.dias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration class
 * 
 *
 */
@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHostName;

	@Value("${spring.redis.port}")
	private int redisPort;

	/**
	 * Getting the redis connection instance through jedisconnection factory
	 * 
	 * @return
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHostName,
				redisPort);
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	/**
	 * Configuring the redis template
	 * 
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		//redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		//redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
		//redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		//redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		//redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

}
