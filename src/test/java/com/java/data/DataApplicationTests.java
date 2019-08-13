package com.java.data;

import java.lang.annotation.Target;

import com.java.data.entity.MovieScore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.beans.factory.annotation.Autowired;

import com.java.data.dao.*;

import java.util.*;

import com.java.data.entity.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTests {

    @Autowired
	MovieDao movieDao;

	@Test
	public void contextLoads() {
	}

	@Test 
	public void testMovieDeviation() {
		MovieScore m = new MovieScore();
		m.setMovieId(231);
		m.setTitle("Dumb & Dumber (1994)");
		m.setAvgScore(3.1924242424242424);
		m.setCnt(660);

		Map<Integer, List<Rate>> rates = movieDao.getRates();

		double x = m.calcDeviation(rates);

		assertThat(x, closeTo(1.321333, 1.321334)); //closeTo的用法为取到区间数

		// MovieScore m = new MovieScore();
		// m.setMovieId(2675);
		// m.setTitle("Twice Upon a Yesterday (1998)");
		// m.setAvgScore(3.400000);
		// m.setCnt(10);

		// Map<Integer, List<Rate>> rates = movieDao.getRates();

		// double x = m.calcDeviation(rates);

		// assertThat(x, equalTo(1.280624));
	}

}
