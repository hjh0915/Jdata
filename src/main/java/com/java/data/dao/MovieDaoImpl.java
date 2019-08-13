package com.java.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.io.IOException;

import com.java.data.entity.*;


@Repository
public class MovieDaoImpl implements MovieDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //写入数据库Users表中的数据，从文件中写入
    @Override
    public void writeUsers(List<String[]> rows) {
        String sql = "insert into users (user_id, gender, age, occupation, zip) values (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return rows.size();
            }
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                String[] words = rows.get(i);
                ps.setInt(1, Integer.parseInt(words[0]));
                ps.setString(2, words[1]);
                ps.setInt(3, Integer.parseInt(words[2]));
                ps.setInt(4, Integer.parseInt(words[3]));
                ps.setString(5, words[4]);
            }
        }); 
    }


    //写入数据库Movies表中的数据，从文件中写入
    @Override
    public void writeMovies(List<String[]> rows) {
        String sql = "insert into movies (movie_id, title, genres) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return rows.size();
            }
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                String[] words = rows.get(i);
                ps.setInt(1, Integer.parseInt(words[0]));
                ps.setString(2, words[1]);
                ps.setString(3, words[2]);
            }
        }); 
    }


    //写入数据库Ratings表中的数据，从文件中写入
    @Override
    public void writeRatings(List<String[]> rows) {
        String sql = "insert into rates (user_id, movie_id, rating, timestamp) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return rows.size();
            }
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                String[] words = rows.get(i);
                ps.setInt(1, Integer.parseInt(words[0]));
                ps.setInt(2, Integer.parseInt(words[1]));
                ps.setInt(3, Integer.parseInt(words[2]));
                ps.setString(4, words[3]);
            }
        }); 
    }


    //三张表的关联后，从表中取出按照性别F, M的电影
    @Override
    public void getMoviesBySex() {
        String sql = "select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M " + 
                    "from (select m.movie_id, m.title, " + 
                    "avg(case when (u.gender = 'F') then  rating else 0 end) as female, " + 
                    "avg(case when(u.gender = 'M') then rating else 0 end) as male " + 
                    "from rates r, users u, movies m " +
                    "where r.movie_id=m.movie_id and r.user_id=u.user_id " + 
                    "group by m.movie_id, m.title, u.gender) t " + 
                    "group by t.movie_id, t.title";

        // List<Object> row = new ArrayList<>();
        // List<List<object>> rows = new ArrayList<>();

        jdbcTemplate.query(sql, result -> {
            while (result.next()) {
                System.out.print(String.format("%-60s", result.getString("title")));
                System.out.print(String.format("%10.4f", result.getFloat("F")));
                System.out.println(String.format("%10.4f", result.getFloat("M")));
            }
        });
    }


    //通过ratings表与movies表关联，取出并计算出每部电影的平均分，以及每部电影的评分数量
    @Override
    public List<MovieScore> getMovieScore() {
        String sql = "select r.movie_id as movieId, m.title, avg(r.rating) as avgScore, count(*) as cnt from rates r, movies m where r.movie_id=m.movie_id group by r.movie_id, m.title order by r.movie_id asc";

        List<MovieScore> movieScores = new ArrayList<>();

        jdbcTemplate.query(sql, result -> {
            while (result.next()) {
                MovieScore movieScore = new MovieScore();
                movieScore.setMovieId(result.getInt("movieId"));
                movieScore.setTitle(result.getString("title"));
                movieScore.setAvgScore(result.getFloat("avgScore"));
                movieScore.setCnt(result.getInt("cnt"));

                movieScores.add(movieScore);
            }
        });

        return movieScores;
    }


    //取到每部电影的每条评分
    @Override
    public Map<Integer, List<Rate>> getRates() {

        Map<Integer, List<Rate>> rates = new HashMap<>();

        jdbcTemplate.query("select movie_id as movieId, rating from rates order by movie_id asc",  
            result -> {
            while (result.next()) {
                int id = result.getInt("movieId");
                Integer nid = new Integer(id);

                Rate rate = new Rate();
                rate.setMovieId(result.getInt("movieId"));
                rate.setRating(result.getInt("rating"));

                // 在rates这个map中去找这个id     
                if (rates.containsKey(nid)) {
                    // 如果找到了，就在moviedId中的value里新增
                    List<Rate> x = rates.get(nid);
                    x.add(rate);
                } else {
                    // 如果没找到，就新增查询结果放到这个rates里作为一个新元素
                    List<Rate> v = new ArrayList<>();
                    v.add(rate);
                    rates.put(nid, v);
                }
            }
        });

        return rates;

    }
}