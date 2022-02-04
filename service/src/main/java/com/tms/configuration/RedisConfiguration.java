package com.tms.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Value("${redis.devendpoint.Primary}")
	public String primary;

	@Value("${redis.devendpoint.reader}")
	public String reader;

	@Value("${redis.devendpoint.port}")
	public int port;

	@Bean(name = "jedisConnectionFactory")
	@Primary
	protected JedisConnectionFactory jedisConnectionFactory() {
		org.springframework.data.redis.connection.RedisConfiguration configuration = null;
		configuration = new RedisStandaloneConfiguration(primary,
				port);
		JedisClientConfiguration jedisClientConfiguration = null;
		jedisClientConfiguration = JedisClientConfiguration.builder().connectTimeout(Duration.ofSeconds(5))
				.readTimeout(Duration.ofSeconds(5)).usePooling().build();

		if (jedisClientConfiguration.getPoolConfig().isPresent()) {
			jedisClientConfiguration.getPoolConfig().get().setMinIdle(10);
			jedisClientConfiguration.getPoolConfig().get().setMaxIdle(50);
			jedisClientConfiguration.getPoolConfig().get().setMaxTotal(1000);
			jedisClientConfiguration.getPoolConfig().get().setMaxWaitMillis(2000);
			jedisClientConfiguration.getPoolConfig().get().setBlockWhenExhausted(true);
		}
		JedisConnectionFactory factory = null;
		factory = new JedisConnectionFactory((RedisStandaloneConfiguration) configuration, jedisClientConfiguration);
		System.out.println("connection cache sucussfully!");
		return factory;
	}

	@Bean(name = "redisWriteTemplate")
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean(name = "jedisReadConnectionFactory")
	protected JedisConnectionFactory jedisReadConnectionFactory() {
		org.springframework.data.redis.connection.RedisConfiguration configuration = null;
		configuration = new RedisStandaloneConfiguration(
				reader, port);
		JedisClientConfiguration jedisClientConfiguration = null;
		jedisClientConfiguration = JedisClientConfiguration.builder().connectTimeout(Duration.ofSeconds(5))
				.readTimeout(Duration.ofSeconds(5)).usePooling().build();
		if (jedisClientConfiguration.getPoolConfig().isPresent()) {
			jedisClientConfiguration.getPoolConfig().get().setMinIdle(10);
			jedisClientConfiguration.getPoolConfig().get().setMaxIdle(50);
			jedisClientConfiguration.getPoolConfig().get().setMaxTotal(1000);
			jedisClientConfiguration.getPoolConfig().get().setMaxWaitMillis(2000);
			jedisClientConfiguration.getPoolConfig().get().setBlockWhenExhausted(true);
		}
		JedisConnectionFactory factory = null;
		factory = new JedisConnectionFactory((RedisStandaloneConfiguration) configuration, jedisClientConfiguration);
		return factory;
	}

	@Bean(name = "redisReadTemplate")
	public RedisTemplate<String, Object> redisReadTemplate() {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(jedisReadConnectionFactory());
		return redisTemplate;
	}
}