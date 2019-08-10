package com.java.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTests {

    @Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void contextLoads() {
	}

}
