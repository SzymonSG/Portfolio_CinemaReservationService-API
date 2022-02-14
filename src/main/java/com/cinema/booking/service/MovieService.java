package com.cinema.booking.service;

import com.cinema.booking.entities.Movie;
import com.cinema.booking.exceptions.MovieNotFoundException;
import com.cinema.booking.mapstructDTO.reservationDTO.FreeSeatDto;
import com.cinema.booking.mapstructDTO.reservationDTO.BasicInfoAboutMovie;


import java.time.LocalDateTime;
import java.util.List;

public interface MovieService {
    //TODO
    public Movie movieSave(Movie movie);
    //TODO
    public List<Movie> fetchMoviesList();
    //TODO przemyśl sens , po co filmy zajęte pokazwyac, I czy nie lepiej pokazać same nazwy filmów w danym dniu.
    //List<Movie> checkMoviesAfterDate(LocalDateTime localDate);

    //List<Movie> multiBookedPlaceWithDate(String cinemaName, String movieName, List<Integer> wantedPlaces, LocalDateTime localDateTime) throws MovieNotFoundException;

    //List<Movie> findFreePlacesOnMovie(String cinemaName, String movieName, LocalDateTime localDateTime) throws MovieNotFoundException;

    public List<BasicInfoAboutMovie> checkBasicInfoAboutMovies(LocalDateTime localDateTime, String cinemaName) throws MovieNotFoundException;

    public Movie fetchMovieById(Long movieId) throws MovieNotFoundException;

    public void deleteMovieById(Long movieId);

    //List<Movie> showAllPlayingMoviesInCinema(String cinemaName) throws MovieNotFoundException;

    //List<Movie> showDateChosenMovie(String moviesName, LocalDateTime localDateTime);
}
