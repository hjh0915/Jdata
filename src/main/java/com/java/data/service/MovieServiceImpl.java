package com.java.data.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.java.data.dao.*;
import com.java.data.entity.*;

import java.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;


@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieDao movieDao;

    @Override
    public void getUsers() {

        List<String[]> rows = new ArrayList<>();

        String fileName = "users.dat";
        InputStream in = MovieServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        try {
            while ((line = br.readLine()) != null) {
                String[] words = line.split("::");
                if ( i % 2000 == 0 ) {
                    movieDao.writeUsers(rows);
                    rows = new ArrayList<>();
                }
                rows.add(words);

                i++;
            }

            movieDao.writeUsers(rows);
        } catch (Exception e) {
        }

    }

    @Override
    public void getMovies() {

        List<String[]> rows = new ArrayList<>();

        String fileName = "movies.dat";
        InputStream in = MovieServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        try {
            while ((line = br.readLine()) != null) {
                String[] words = line.split("::");
                if ( i % 2000 == 0 ) {
                    movieDao.writeMovies(rows);
                    rows = new ArrayList<>();
                }
                rows.add(words);
    
                i++;
            }
    
            movieDao.writeMovies(rows);
        } catch (Exception e) {
        }

    }

    @Override
    public void getRatings() {

        List<String[]> rows = new ArrayList<>();

        String fileName = "ratings.dat";
        InputStream in = MovieServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int i = 1;

        try {
            while ((line = br.readLine()) != null) {
                String[] words = line.split("::");
                if ( i % 2000 == 0 ) {
                    movieDao.writeRatings(rows);
                    rows = new ArrayList<>();
                }
                rows.add(words);
    
                i++;
            }
    
            movieDao.writeRatings(rows);
        } catch (Exception e) {
        }
    }

    @Override
    public void calcDeviation() {
        List<MovieScore> movieScores = movieDao.getMovieScore();
        Map<Integer, List<Rate>> rates = movieDao.getRates();

        for (MovieScore movieScore : movieScores) {
            movieScore.calcDeviation(rates);
        }
    }
    
}