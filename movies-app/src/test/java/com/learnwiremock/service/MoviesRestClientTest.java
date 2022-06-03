package com.learnwiremock.service;

import com.learnwiremock.dto.Movie;
import com.learnwiremock.exception.MovieErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

public class MoviesRestClientTest {

    MoviesRestClient moviesRestClient;
    WebClient webClient;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:8081";
        webClient = WebClient.create(baseUrl);
        moviesRestClient = new MoviesRestClient(webClient);
    }

    @Test
    void retrieveAllMovies() {
        List<Movie> movieList = moviesRestClient.retrieveAllMovies();
        System.out.println(movieList);
        Assertions.assertTrue(movieList.size() > 0);
    }

    @Test
    void retrieveMovieById() {
        //given
        Integer movieId = 1;
        //when
        Movie movie = moviesRestClient.retrieveMovieById(movieId);
        //then
        System.out.println(movie);
        Assertions.assertNotNull(movie);
        Assertions.assertEquals("Batman Begins", movie.getName());
    }

    @Test
    void retrieveMovieByIdNotFound() {
        //given
        Integer movieId = 100;
        //when, then
        Assertions.assertThrows(MovieErrorResponse.class, () -> moviesRestClient.retrieveMovieById(movieId));
    }

    @Test
    void retrieveMovieByName() {
        //given
        String movieName = "Avengers";
        //when
        List<Movie> movies = moviesRestClient.retrieveMovieByName(movieName);
        //then
        System.out.println(movies);
        Assertions.assertNotNull(movies);
        Assertions.assertEquals(4, movies.size());
    }

    @Test
    void retrieveMovieByNameNotFound() {
        //given
        String movieName = "ABC";
        //when, then
        Assertions.assertThrows(MovieErrorResponse.class, () -> moviesRestClient.retrieveMovieByName(movieName));
    }

    @Test
    void retrieveMovieByYear() {
        //given
        Integer movieYear = 2012;
        //when
        List<Movie> movies = moviesRestClient.retrieveMovieByYear(movieYear);
        //then
        System.out.println(movies);
        Assertions.assertNotNull(movies);
        Assertions.assertEquals(2, movies.size());
    }

    @Test
    void retrieveMovieByYearNotFound() {
        //given
        Integer movieYear = 1950;
        //when, then
        Assertions.assertThrows(MovieErrorResponse.class, () -> moviesRestClient.retrieveMovieByYear(movieYear));
    }

    @Test
    void addMovie() {
        //given
        Movie newMovie = new Movie(null, "Toy Story 4", "Tom Hanks", 2019, LocalDate.of(2019, 06, 20));
        //when
        Movie createdMovie = moviesRestClient.addMovie(newMovie);
        //then
        System.out.println(createdMovie);
        Assertions.assertNotNull(createdMovie);
        Assertions.assertNotNull(createdMovie.getMovie_id());
    }

    @Test
    void addMovieBadRequest() {
        //given
        Movie newMovie = new Movie(null, null, "Tom Hanks", 2019, LocalDate.of(2019, 06, 20));
        //when, then
        String expectedErrorMessage = "Message is Please pass all the input fields : [name]";
        Assertions.assertThrows(MovieErrorResponse.class, () -> moviesRestClient.addMovie(newMovie), expectedErrorMessage);
    }

    @Test
    void updateMovie() {
        //given
        Integer movieId = 3;
        String cast = "Tom Hardy";
        Movie movieWithUpdates = new Movie(null, null, cast, 2019, null);
        //when
        Movie updatedMovie = moviesRestClient.updateMovie(movieId, movieWithUpdates);
        //then
        System.out.println(updatedMovie);
        Assertions.assertNotNull(updatedMovie);
        Assertions.assertNotNull(updatedMovie.getCast().contains(cast));
    }

    @Test
    void updateMovieInvalidId() {
        //given
        Integer movieId = 100;
        String cast = "Tom Hardy";
        Movie movieWithUpdates = new Movie(null, null, cast, 2019, null);
        //when, then
        String expectedErrorMessage = "No Movie Available with the given Id - 100";
        Assertions.assertThrows(MovieErrorResponse.class, () -> moviesRestClient.updateMovie(movieId, movieWithUpdates), expectedErrorMessage);
    }

    @Test
    void deleteMovie() {
        //given
        Movie newMovie = new Movie(null, "Toy Story 4", "Tom Hanks", 2030, LocalDate.of(2030, 06, 20));
        Movie createdMovie = moviesRestClient.addMovie(newMovie);
        //when
        String responseMessage = moviesRestClient.deleteMovie(createdMovie.getMovie_id().intValue());
        //then
        String expectedResponseMessage = "Movie Deleted Successfully";
        Assertions.assertEquals(expectedResponseMessage, responseMessage);
    }

    @Test
    void deleteMovieNotFound() {
        //given
        Integer movieId = 100;
        //when, then
        String expectedErrorMessage = "No Movie Available with the given Id - 100";
        Assertions.assertThrows(MovieErrorResponse.class, () ->moviesRestClient.deleteMovie(movieId), expectedErrorMessage);
    }
}
