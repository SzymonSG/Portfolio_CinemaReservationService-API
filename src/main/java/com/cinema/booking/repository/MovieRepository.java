package com.cinema.booking.repository;
import com.cinema.booking.entities.Movie;
import com.cinema.booking.entities.PropertiesMovie;
import com.cinema.booking.dtos.showInfoDto.BasicInfoAboutMovieDto;
import com.cinema.booking.payloads.RepertoireDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    boolean existsBySeating(Integer seating);

    @Query(
            "SELECT new com.cinema.booking.dtos.showInfoDto.BasicInfoAboutMovieDto" +
                    "(m.movieName,m.seating,m.movieRoom,p.startTimeOfTheMovie)" +
                    "FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p " +
                    "WHERE p.startTimeOfTheMovie = :localDateTime AND m.booked='free' " +
                    "AND c.cinemaName=:cinemaName"
    )
    List<BasicInfoAboutMovieDto>getFreePlacesForSelected_CinemaAndDataTime(String cinemaName, LocalDateTime localDateTime);


    //Czy to powinno zwracać List<Optional> czy moze Optional <List<Movie>>
    @Query(
            "SELECT m,c,p FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p " +
                    "WHERE c.cinemaName= :cinema AND m.movieName = :movie AND " +
                    "p.startTimeOfTheMovie= :localDateTime"
    )
     List<Movie> getDataCollectionToReservation(String cinema, String movie,
                                                LocalDateTime localDateTime);



    @Query(
            "SELECT m,c,p FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p WHERE c.cinemaName=:cinemaName"
    )
    List<Movie> getAllDataFromCinema(String cinemaName);

    @Query(
            "SELECT m FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p WHERE c.cinemaName=:cinemaName " +
                    "AND m.movieName=:movieName AND p.startTimeOfTheMovie=:localDateTime AND m.booked='free'"

    )
    List<Movie> getAllFreePlacesOnMovie(String cinemaName,String movieName, LocalDateTime localDateTime);


//    @Query(
//            "SELECT m FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p WHERE c.cinemaName=:cinemaName " +
//                    "GROUP BY m.movieName, m.movieId"
//    )
    @Query(
            "SELECT DISTINCT new com.cinema.booking.payloads.RepertoireDTO(m.movieName)" +
                    "FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p WHERE c.cinemaName=:cinemaName"
    )
    List<RepertoireDTO>getAllPlayingMovies(String cinemaName);


    @Query(
            "SELECT p FROM Movie m JOIN m.cinemas c JOIN m.properitiesMovie p WHERE c.cinemaName= :cinemaName " +
                    "AND m.movieName=:movieName GROUP BY p.startTimeOfTheMovie, p.propertyId"
    )
    List<PropertiesMovie>getLocalDateTimeForChosenMovie(String cinemaName, String movieName);
}
