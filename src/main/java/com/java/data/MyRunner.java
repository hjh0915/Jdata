package com.java.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.data.entity.*;

import java.util.stream.Collectors;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
	private ApplicationArguments nargs;

    public void run(String... args) throws Exception {
        // getUsers();
        // getMovies();
        // getRatings();

        if(nargs.containsOption("insert")) {
            //Get argument values
            List<String> values = nargs.getOptionValues("insert");
             
            log.info("insert :: " + values);
            for (String x: values) {
                log.info(x);
                if (x.equals("users")) {
                    getUsers();
                } else if (x.equals("movies")) {
                    getMovies();
                } else if (x.equals("rates")) {
                    getRatings();
                } else {
                    log.info("没有对应的表名");
                }
            }
        }

        // List<String> nonOptionArgs = nargs.getNonOptionArgs();
         
        // log.info("Non Option Args List ...");
         
        // if (!nonOptionArgs.isEmpty())
        // {
        //     nonOptionArgs.forEach(file -> log.info(file));
        // }

        if(nargs.containsOption("query")) {
            List<String> values = nargs.getOptionValues("query");

            log.info("query :: " + values);
            for (String x: values) {
                log.info(x);
                if (x.equals("sex")) {
                    getMoviesBySex();
                }
            }
        }

        if(nargs.containsOption("calc")) {
            calcDeviation();
        }
    }

    public void getUsers() throws IOException {

        List<String[]> rows = new ArrayList<>();

        String fileName = "users.dat";
        InputStream in = MyRunner.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        while ((line = br.readLine()) != null) {
            String[] words = line.split("::");
            if ( i % 2000 == 0 ) {
                writeUsers(rows);
                rows = new ArrayList<>();
            }
            rows.add(words);

            i++;
        }

        writeUsers(rows);

    }

    public void writeUsers(List<String[]> rows) throws IOException {
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

    public void getMovies() throws IOException {

        List<String[]> rows = new ArrayList<>();

        String fileName = "movies.dat";
        InputStream in = MyRunner.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        while ((line = br.readLine()) != null) {
            String[] words = line.split("::");
            if ( i % 2000 == 0 ) {
                writeMovies(rows);
                rows = new ArrayList<>();
            }
            rows.add(words);

            i++;
        }

        writeMovies(rows);

    }

    public void writeMovies(List<String[]> rows) throws IOException {
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

    public void getRatings() throws IOException {

        List<String[]> rows = new ArrayList<>();

        String fileName = "ratings.dat";
        InputStream in = MyRunner.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        while ((line = br.readLine()) != null) {
            String[] words = line.split("::");
            if ( i % 2000 == 0 ) {
                writeRatings(rows);
                rows = new ArrayList<>();
            }
            rows.add(words);

            i++;
        }

        writeRatings(rows);

    }

    public void writeRatings(List<String[]> rows) throws IOException {
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

    public void getMoviesBySex() throws IOException {
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

    public void calcDeviation() {
        List<MovieScore> movieScores = getMovieScore();
        Map<Integer, List<Rate>> rates = getRates();

        for (MovieScore movieScore : movieScores) {
            int movieId = movieScore.getMovieId();
            Integer nid = new Integer(movieId);

            float avgScore = movieScore.getAvgScore();
            int n = movieScore.getCnt();
            
            List<Rate> movieRates = getMovieRates(rates, nid);

            float s = 0.0F;
            for (Rate rate : movieRates) {
                int rating = rate.getRating();
                float x = ((float)rating - avgScore) ;
                s = s + x*x;
            }
            float movieDeviation = s / n;

            System.out.print(String.format("%-10d", movieId));
            System.out.println(movieDeviation);
        }
    }

    public List<MovieScore> getMovieScore() {
        String sql = "select movie_id as movieId, avg(rating) as avgScore, count(*) as cnt from rates group by movie_id order by movie_id asc";

        List<MovieScore> movieScores = new ArrayList<>();

        jdbcTemplate.query(sql, result -> {
            while (result.next()) {
                MovieScore movieScore = new MovieScore();
                movieScore.setMovieId(result.getInt("movieId"));
                movieScore.setAvgScore(result.getFloat("avgScore"));
                movieScore.setCnt(result.getInt("cnt"));

                movieScores.add(movieScore);
            }
        });

        return movieScores;
    }
    
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

    public List<Rate> getMovieRates(Map<Integer, List<Rate>> rates, Integer movieId) {

        //return rates.stream().filter(x -> (x.getMovieId() == movieId)).collect(Collectors.toList());
        
        // List<Rate> movieRates = new ArrayList<>();
        // for (Rate x: rates) {
        //     if (x.getMovieId() == movieId) {
        //         movieRates.add(x);
        //     }
        // }
        // return movieRates;

        return rates.get(movieId);
    }
}