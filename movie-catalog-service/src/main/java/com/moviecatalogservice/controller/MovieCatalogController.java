package com.moviecatalogservice.controller;

import com.moviecatalogservice.models.Catalog;
import com.moviecatalogservice.models.Movie;
import com.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;


    @RequestMapping("/{userId}")
    public List<Catalog> getCatalog(@PathVariable String userId) {

        UserRating userRatings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId,
                UserRating.class);

        return userRatings.getRatings().stream().map(rating -> {
            System.out.println("https://movie-info-service/movies/" + rating.getMovieId());
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
                    Movie.class);

            return new Catalog(movie.getName(), movie.getDescription(), rating.getRating());

        }).collect(Collectors.toList());
    }
}

/*     Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block(); */
