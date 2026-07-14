package com.springbootLearning;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;


@SpringBootApplication
@MapperScan("com.springbootLearning.mapper")
public class SpringbootLearningApplication {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${ymlfile.name}")
    private String ymlfileName;

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringbootLearningApplication.class, args);
        // TODO:  方案一：从context中获取activeProfile，最简单的获取方式
        System.out.println("【方案一】当前环境-----------------" + context.getEnvironment().getProperty("spring.profiles.active"));
	}

    // TODO: 方案二：利用 Spring 的 @PostConstruct 或者是监听器（适合更复杂的启动初始化）
    @PostConstruct
    public void init() {
        System.out.println("【方案二】🔥当前环境-----------------" + activeProfile);
        System.out.println("【方案二】🔥当前yml文件名-----------------" + ymlfileName);
    }
}
