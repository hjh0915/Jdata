package com.java.data.entity;

import lombok.Data;

@Data 
public class MovieScore {
    private int movieId;
    private float avgScore;
    private int cnt;
}