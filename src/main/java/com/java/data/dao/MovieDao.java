package com.java.data.dao;

import java.util.*;
import com.java.data.entity.*;

public interface MovieDao {
    void writeUsers(List<String[]> rows);
    void writeMovies(List<String[]> rows);
    void writeRatings(List<String[]> rows);
    void getMoviesBySex();
    List<MovieScore> getMovieScore();
    Map<Integer, List<Rate>> getRates();
}