package com.java.data.entity;

import lombok.Data;

import java.util.*;
import java.lang.Math;

@Data 
public class MovieScore {
    private int movieId;
    private String title;
    private double avgScore;
    private int cnt;

    public double calcDeviation(Map<Integer, List<Rate>> rates) {
        
        Integer nid = new Integer(movieId);
        int n = cnt;
        
        List<Rate> movieRates = rates.get(movieId);

        double s = 0.0;
        for (Rate rate : movieRates) {
            int rating = rate.getRating();
            double x = ((double)rating - avgScore);
            s = s + x*x;
        }
        return Math.sqrt(s / n);
    }
}