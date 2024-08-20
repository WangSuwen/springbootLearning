package com.springbootLearning;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.springbootLearning.mapper")
public class SpringbootLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootLearningApplication.class, args);
	}

}
