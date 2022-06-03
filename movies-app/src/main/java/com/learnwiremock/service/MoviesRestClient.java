package com.learnwiremock.service;

import com.learnwiremock.constants.MoviesAppConstants;
import com.learnwiremock.dto.Movie;
import com.learnwiremock.exception.MovieErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
public class MoviesRestClient {

    private WebClient webClient;

    public MoviesRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Movie> retrieveAllMovies() {
        //http://localhost:8081/movieservice/v1/allMovies
        return webClient.get()
                .uri(MoviesAppConstants.GET_ALL_MOVIES_V1)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }

    public Movie retrieveMovieById(Integer movieId) {
        //http://localhost:8081/movieservice/v1/movie/1
        try {
            return webClient.get()
                    .uri(MoviesAppConstants.MOVIE_BY_ID_PATH_PARAM_V1, movieId)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in retrieveMovieById({}). Status code is {}. Message is {}.", movieId, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in retrieveMovieById(" + movieId + "). Message is " + e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }

    public List<Movie> retrieveMovieByName(String name) {
        //http://localhost:8081/movieservice/v1/movieName?movie_name=Avengers
        String retrieveByNameUri = UriComponentsBuilder.fromUriString(MoviesAppConstants.MOVIE_BY_NAME_QUERY_PARAM_V1).queryParam("movie_name", name).buildAndExpand().toUriString();
        try {
            return webClient.get()
                    .uri(retrieveByNameUri)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in retrieveMovieByName({}). Status code is {}. Message is {}.", name, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in retrieveMovieByName(" + name + "). Message is " + e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }

    public List<Movie> retrieveMovieByYear(Integer movieYear) {
        //http://localhost:8081/movieservice/v1/movieYear?year=2012
        String retrieveByNameYearUri = UriComponentsBuilder.fromUriString(MoviesAppConstants.MOVIE_BY_YEAR_QUERY_PARAM_V1).queryParam("year", movieYear).buildAndExpand().toUriString();

        try {
            return webClient.get()
                    .uri(retrieveByNameYearUri)
                    .retrieve()
                    .bodyToFlux(Movie.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in retrieveMovieByYear({}). Status code is {}. Message is {}.", movieYear, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in retrieveMovieByYear(" + movieYear + "). Message is " + e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }

    public Movie addMovie(Movie newMovie) {
        //http://localhost:8081/movieservice/v1/movie
        try {
            return webClient.post()
                    .uri(MoviesAppConstants.ADD_MOVIE_V1)
                    .syncBody(newMovie)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in addMovie({}). Status code is {}. Message is {}.", newMovie, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in addMovie(" + newMovie + "). Message is " + e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }

    public Movie updateMovie(Integer movieId, Movie movieWithUpdates) {
        //http://localhost:8081/movieservice/v1/movie/1
        try {
            return webClient.put()
                    .uri(MoviesAppConstants.MOVIE_BY_ID_PATH_PARAM_V1, movieId)
                    .syncBody(movieWithUpdates)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in updateMovie({}, {}). Status code is {}. Message is {}.", movieId, movieWithUpdates, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in updateMovie({}, {}). Message is {}", movieId, movieWithUpdates, e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }

    public String deleteMovie(Integer movieId) {
        //http://localhost:8081/movieservice/v1/movie/1
        try {
            return webClient.delete().uri(MoviesAppConstants.MOVIE_BY_ID_PATH_PARAM_V1, movieId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException in deleteMovie({}). Status code is {}. Message is {}.", movieId, e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            throw new MovieErrorResponse(e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Exception in deleteMovie({}). Message is {}", movieId, e.getMessage(), e);
            throw new MovieErrorResponse(e);
        }
    }
}
