package com.springbootLearning;

import com.springbootLearning.entity.User;
import com.springbootLearning.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootLearningApplicationTests {

	@Autowired
	UserMapper userMapper;

	@Test
	void contextLoads() {
		User u = new User();
		u.setName("Lisa");
		u.setEmail("100292@qq.com");
		u.setPassword("3456789");
		int userCount = userMapper.insert(u);
		System.out.println("插入用户：" + userCount);
		/*User user = userMapper.selectById(1);
		System.out.println(user);*/
	}

}
