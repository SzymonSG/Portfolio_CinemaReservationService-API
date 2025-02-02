package com.cinema.booking.validators;
import com.cinema.booking.entities.Movie;
import com.cinema.booking.exceptions.CinemaNotFoundException;
import com.cinema.booking.exceptions.MovieNotFoundException;
import com.cinema.booking.exceptions.PropertyMovieNotFoundException;
import com.cinema.booking.model.BookingModel;
import com.cinema.booking.repository.CinemaRepository;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.PropertiesMovieRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Service
@Data
@Builder
@Slf4j
public class BookingModelValidator {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private PropertiesMovieRepository propertiesMovieRepository;

    private String cinemaName;
    private String movieName;
    private String movieRoom;
    private LocalDateTime dateAndTime;
    private List<Integer> seatsToBooked;

    public BookingModelValidator buildCorrectModel(BookingModel bookingModel) throws MovieNotFoundException, CinemaNotFoundException, PropertyMovieNotFoundException {

        cinemaExits(bookingModel);
        movieExist(bookingModel);
        movieRoomExist(bookingModel);
        dataTimesExist(bookingModel);
        seatsToBookedExist(bookingModel);
        return BookingModelValidator.builder()
                .cinemaName(bookingModel.getCinemaName())
                .movieName(bookingModel.getMovieName())
                .movieRoom(bookingModel.getMovieRoom())
                .dateAndTime(bookingModel.getDateAndTime())
                .seatsToBooked(bookingModel.getSeatsToBooked())
                .build();


    }

    public void seatsToBookedExist(BookingModel bookingModel) throws MovieNotFoundException {
        List<Integer> seatsToBooked = bookingModel.getSeatsToBooked();
        if (CollectionUtils.isEmpty(seatsToBooked)) {
            throw new MovieNotFoundException("No seats were given for bookingg");
        } else {
//            List<Movie> seance = movieRepository.fetchDataToBooking(
//                    bookingModel.getCinemaName(),
//                    bookingModel.getMovieName(),
//                    bookingModel.getMovieRoom(),
//                    bookingModel.getDateAndTime());

            Optional<List<Movie>> seanceOptional = Optional.ofNullable(movieRepository.fetchDataToBookingg(
                    bookingModel.getCinemaName(),
                    bookingModel.getMovieName(),
                    bookingModel.getMovieRoom(),
                    bookingModel.getDateAndTime()
            )).orElseThrow(() -> new MovieNotFoundException("JPQL Query not work!"));

            List<Movie> seance = seanceOptional.stream()
                    .flatMap(x -> x.stream())
                    .collect(Collectors.toList());

            List<Integer> collect = seance
                    .stream()
                    .map(Movie::getSeating)
                    .collect(Collectors.toList());

            boolean seatsExist = collect.containsAll(seatsToBooked); // contain czy zawiera mniejszy w większym
            if (!seatsExist) {
                throw new MovieNotFoundException(seatsToBooked);
            }
        }
    }


    private void dataTimesExist(BookingModel bookingModel) throws PropertyMovieNotFoundException {
        boolean propertyExist = propertiesMovieRepository.existsByStartTimeOfTheMovie(bookingModel.getDateAndTime());
        if (!propertyExist){
            throw new PropertyMovieNotFoundException("Enter Correct date and time movie");
        }
    }

    private void movieRoomExist(BookingModel bookingModel) throws MovieNotFoundException {
        boolean roomExist = movieRepository.existsByMovieRoom(bookingModel.getMovieRoom());
        if (!roomExist){
            throw new MovieNotFoundException("Enter correct movieroom");
        }
    }

    private void movieExist(BookingModel bookingModel) throws MovieNotFoundException {
        boolean movieExist = movieRepository.existsByMovieName(bookingModel.getMovieName());
        if (!movieExist){
            throw  new MovieNotFoundException("Enter correct movie name");
        }
    }

    private void cinemaExits(BookingModel bookingModel) throws CinemaNotFoundException {
        boolean cinemaExist = cinemaRepository.existsByCinemaName(bookingModel.getCinemaName());
        if (!cinemaExist){
            throw new CinemaNotFoundException("Enter correct cinema name");
        }
    }


}
