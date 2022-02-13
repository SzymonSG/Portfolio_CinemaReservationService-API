package com.cinema.booking.controllers;
import com.cinema.booking.entities.Movie;
import com.cinema.booking.entities.PropertiesMovie;
import com.cinema.booking.exceptions.MovieNotFoundException;
import com.cinema.booking.mapper.CinemaMapStruct;
import com.cinema.booking.mapstructDTO.*;
import com.cinema.booking.service.MovieService;
import com.cinema.booking.service.ReservationService;
import com.cinema.booking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final MovieService movieService;
    private final ReservationService reservationService;
    private final CinemaMapStruct cinemaMapStruct;
    private final ShowService showService;


    @PutMapping("/cinemaName/{cinemaName}/movieName/{movieName}/date")
    public List<Movie> multiBookedPlaceWithDate( @PathVariable("cinemaName")String cinemaName,
                                                 @PathVariable("movieName")String movieName,
                                                 @RequestBody List<Integer> wantedPlaces,
                                                 @RequestParam("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,
                                                 pattern = "yyyy-MM-dd; HH:mm:ss") LocalDateTime localDateTime) throws MovieNotFoundException {
        return reservationService.multiBookedPlaceWithDate(cinemaName,movieName,wantedPlaces,localDateTime);
    }

    //1 Lista Filmów+

    @GetMapping("/cinemas/{cinemaName}/movies")
    public List<MovieNameDto>showAllPlayingMovies(@PathVariable("cinemaName")String cinemaName) throws MovieNotFoundException {
        List<Movie> movies = showService.showAllPlayingMoviesInCinema(cinemaName);
        return cinemaMapStruct.toMovieNameListDto(movies);
    }
    //2 Godzina i data filmu not work ale w innej wersji work+ (-)
    @GetMapping("/dates/cinemas/{cinemaName}/movies/{movieName}")
    public List<DataDto>showDateChosenMovie(@PathVariable("cinemaName")String cinemaName,
                                                       @PathVariable("movieName")String moviesName) throws MovieNotFoundException {
        List<PropertiesMovie> dataTimeMovie = showService.showDateChosenMovie(cinemaName, moviesName);

        return cinemaMapStruct.toDataDtoListMovie(dataTimeMovie);
//        return cinemaMapStruct.toPropertiesMovieListDto(dataTimeMovie);
    }


    //3.
    //wolne miejsca na konkretny film
    @GetMapping("/findFreePlaces/cinemaName/{cinemaName}/movieName/{movieName}/date")
    public List<FreePlaceDto> findFreeMoviesInCinema(@PathVariable ("cinemaName")String cinemaName,
                                                     @PathVariable ("movieName") String movieName,
                                                     @RequestParam("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,
                                                     pattern = "yyyy-MM-dd; HH:mm:ss") LocalDateTime localDateTime) throws MovieNotFoundException {
        List<Movie> freePlacesOnMovie = showService.findFreePlacesOnMovie(cinemaName, movieName, localDateTime);
        return cinemaMapStruct.toFreePlaceListDto(freePlacesOnMovie);
    }


   //TODO gdzie mamy wolne miejsca dzisiaj w Bonarce na package DTO
//    @GetMapping("/findByDateFree/{cinemaName}/date")
//    public List<BasicInfoAboutMovie> findAllFreePlacesTodayInCinema(@PathVariable("cinemaName") String cinemaName,
//                                                                    @RequestParam("localDate")
//                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,
//                                                                    pattern = "yyyy-MM-dd; HH:mm:ss") LocalDateTime localDateTime) throws MovieNotFoundException {
//        List<BasicInfoAboutMovie> movies = movieService.checkBasicInfoAboutMovies(localDateTime,cinemaName);
//        return movies;
//    }

    //to samo co 2 założenie mamy ale z innym dto (chyba powinno jeszcze wziąć pod uwagę film) ale czy też kino
    @GetMapping("/dates")
    public List<ComplexMovieDto> findShowMovieByDateTime(@RequestParam("localDate")
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,
                                                         pattern = "yyyy-MM-dd; HH:mm:ss") LocalDateTime localDateTime){
        List<Movie> movies = showService.checkMoviesAfterDate(localDateTime);
        return cinemaMapStruct.toComplexMovieListDto(movies);
    }



    ///DTO VERSION RESERVATION
//    @PutMapping("/cinemaName/{cinemaName}/movieName/{movieName}/datee")
//    public List<Movie> multiBookedPlacewithDateeee( @PathVariable("cinemaName")String cinemaName,
//                                                  @PathVariable("movieName")String movieName,
//                                                  @RequestBody List<Integer> wantedPlaces,
//                                                  @RequestParam("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,
//                                                          pattern = "yyyy-MM-dd; HH:mm:ss") LocalDateTime localDateTime) throws MovieNotFoundException {
//        List<Movie> movies = movieService.multiBookedPlaceWithDate(cinemaName, movieName, wantedPlaces, localDateTime);
//        List<ComplexMovieDTO> complexMovieDTOS = cinemaMapper.toComplexListMovieDto(movies);
//        List<Movie> moviesAgain = cinemaMapper.dtoToComplexMovieList(complexMovieDTOS);
//        return moviesAgain;
//    }
//

    // do dorobienia
//    @GetMapping("/cinemaPlayingMovies/{cinemaName}")
//    public List<String> showAllPlayingMovies(@PathVariable ("cinemaName")String cinemaName){
//        return movieService.showAllPlayingMovies(cinemaName);
//    }
//


    //gropuingby for filter add only, + doczytanie o Desrailizer and Serializer Date

}