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
                    threadPoolKey = "movieInfoPool",
                    threadPoolProperties = {
                            @HystrixProperty(name = "coreSize", value = "20"),
                            @HystrixProperty(name = "maxQueueSize", value = "10")
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
