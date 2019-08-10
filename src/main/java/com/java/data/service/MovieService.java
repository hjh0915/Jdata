package com.java.data.service;

import com.java.data.entity.*;
import java.util.*;

public interface MovieService {
    void getUsers();
    void getMovies();
    void getRatings();

    void calcDeviation();
    void calcOneMovieDeviation(MovieScore movieScore, Map<Integer, List<Rate>> rates);
}