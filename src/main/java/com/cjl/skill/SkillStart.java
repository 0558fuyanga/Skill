package com.cjl.skill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cjl.skill.mapper")
public class SkillStart {
	public static void main(String[] args) {
		SpringApplication.run(SkillStart.class, args);
	}

	/*
	 * @Bean public RequestContextListener requestContextListener() { return new
	 * RequestContextListener(); }
	 */

}
