package com.moviecatalogservice.service;

import com.moviecatalogservice.models.Catalog;
import com.moviecatalogservice.models.Movie;
import com.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalog",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
            })
    public Catalog getCatalog(Rating rating) {
        System.out.println("https://movie-info-service/movies/" + rating.getMovieId());
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
                Movie.class);
        return new Catalog(movie.getName(), movie.getDescription(), rating.getRating());
    }

    public Catalog getFallbackCatalog(Rating rating) {
        return new Catalog("Movie name not found", "", rating.getRating());
    }
}
