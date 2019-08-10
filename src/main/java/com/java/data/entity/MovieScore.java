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
        int n = cnt - 1 ;                  //方差和标准方差， 样本n 与 n-1 的差别是？
        
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