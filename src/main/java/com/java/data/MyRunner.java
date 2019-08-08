package com.java.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.sql.*;
import java.util.*;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(String... args) throws Exception {
        getUsers();
        getMovies();
        getRatings();
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
}