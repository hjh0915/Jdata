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
            calcOneMovieDeviation(movieScore, rates);
        }
    }

    @Override
    public void calcOneMovieDeviation(MovieScore movieScore, Map<Integer, List<Rate>> rates) {
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

    private List<Rate> getMovieRates(Map<Integer, List<Rate>> rates, Integer movieId) {

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