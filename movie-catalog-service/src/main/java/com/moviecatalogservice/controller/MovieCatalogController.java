package com.moviecatalogservice.controller;

import com.moviecatalogservice.models.Catalog;
import com.moviecatalogservice.models.Movie;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.models.UserRating;
import com.moviecatalogservice.service.MovieInfo;
import com.moviecatalogservice.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieinfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @HystrixCommand
    @RequestMapping("/{userId}")
    public List<Catalog> getCatalogItem(@PathVariable String userId) {
        UserRating userRatings = userRatingInfo.getUserRating(userId);
        return userRatings.getRatings().stream()
                .map(rating -> movieinfo.getCatalog(rating))
                .collect(Collectors.toList());
    }

}

/*
Alternative Webclient way
Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
 */
