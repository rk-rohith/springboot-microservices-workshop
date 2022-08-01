package com.movieinfoservice.controller;

import com.movieinfoservice.model.Movie;
import com.movieinfoservice.model.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieInfoController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{movieId}")
    public Movie getCatalog(@PathVariable String movieId) {
        System.out.println("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey);
        MovieSummary movieSummary = restTemplate.getForObject(
                "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey, MovieSummary.class);
        Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
        System.out.println(movieSummary);
        return movie;
    }
}
