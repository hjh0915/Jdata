package com.java.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.data.service.*;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);

    @Autowired
    private ApplicationArguments nargs;
    
    @Autowired
    MovieService movieService;

    public void run(String... args) throws Exception {

        if(nargs.containsOption("insert")) {
            List<String> values = nargs.getOptionValues("insert");
             
            log.info("insert :: " + values);
            for (String x: values) {
                log.info(x);
                if (x.equals("users")) {
                    movieService.getUsers();
                } else if (x.equals("movies")) {
                    movieService.getMovies();
                } else if (x.equals("rates")) {
                    movieService.getRatings();
                } else {
                    log.info("没有对应的表名");
                }
            }
        }

        // if(nargs.containsOption("query")) {
        //     List<String> values = nargs.getOptionValues("query");

        //     log.info("query :: " + values);
        //     for (String x: values) {
        //         log.info(x);
        //         if (x.equals("sex")) {
        //             getMoviesBySex();
        //         }
        //     }
        // }

        if(nargs.containsOption("calc")) {
            movieService.calcDeviation();
        }
    }
}