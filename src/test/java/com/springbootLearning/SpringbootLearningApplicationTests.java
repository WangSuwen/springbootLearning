package com.springbootLearning;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class SpringbootLearningApplicationTests {


	@Test
	void contextLoads() throws UnsupportedEncodingException {
		String jwtKey = "asdfALDKF;SAasdf123";
		Algorithm algorithm = Algorithm.HMAC256(jwtKey);
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.SECOND, 3600 * 24 * 7);
		String jwtToken = JWT.create()
				.withClaim("id", 1)
				.withClaim("name", "张三")
				.withExpiresAt(calendar.getTime())
				.withIssuedAt(now)
				.sign(algorithm);
		System.out.println("jwtToken----------- " + jwtToken);
	}

}
