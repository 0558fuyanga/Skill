package com.cjl.skill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<String,Object> redisTemplateJDK(RedisConnectionFactory factory){
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		//配置连接工厂
		template.setConnectionFactory(factory);
		return template;
	}
	
	@Bean
	public RedisTemplate<String,Object> redisTemplateJackson(RedisConnectionFactory factory){
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		//配置连接工厂
		template.setConnectionFactory(factory);

		//使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		
		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        
		return template;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory){
		return new StringRedisTemplate(factory);
	}
}
