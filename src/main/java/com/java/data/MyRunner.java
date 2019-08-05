package com.java.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(String... args) throws Exception {
        getUsers();
    }

    public void getUsers() throws IOException {

        String fileName = "users.dat";
        InputStream in = MyRunner.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::");
            writeUsers(words);
        }

    }

    public void writeUsers(String[] words) throws IOException {
        jdbcTemplate.update("insert into users (user_id, gender, age, occupation, zip) values (?, ?, ?, ?, ?)" ,
            new Object[] {Integer.parseInt(words[0]), words[1], Integer.parseInt(words[2]), 
                Integer.parseInt(words[3]), words[4]},
            new int [] { java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.INTEGER,
                java.sql.Types.INTEGER, java.sql.Types.VARCHAR });
    }
}