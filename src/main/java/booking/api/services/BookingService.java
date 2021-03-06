package main.java.booking.api.services;

import booking.api.clients.BookingApi;
import main.java.booking.api.dtos.booking.BookingDTO;
import main.java.booking.api.dtos.booking.BookingIdDTO;
import main.java.booking.api.dtos.booking.BookingExtendedDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import java.util.List;

@Slf4j
public class BookingService {

    private BookingApi bookingApi;

    public BookingService(BookingApi bookingApi) {
        this.bookingApi = bookingApi;
    }

    private void logResponseCode(Response response) {
        if (response.code() == 200 || response.code() == 201)
            log.info("Api request is successful with code of: HTTP {}", response.code());
        else
            log.error("Request have failed with error code of: HTTP {}.", response.code());
    }

    /**
     * This method gets booking id lists from the BE with out any queries.
     * @return List<BookingIdDTO> A list contains BookingIdDTO objects that are returned from endpoint.
     */
    public List<BookingIdDTO> getBookingsIdList() {
        return getBookingsIdList(null, null, null, null);
    }

    /**
     * This method gets booking id lists from the BE with following queries.
     * If you would like to use it without queries please see {@link #getBookingsIdList()}
     * @param firstName @Nullable First name search criteria for booking
     * @param lastName @Nullable Last name search criteria for booking
     * @param checkInDate @Nullable Check-in date search criteria for booking. Format: {YYYY-MM-DD}
     * @param checkOutDate @Nullable Check-out date search criteria for booking. Format: {YYYY-MM-DD}
     * @return List<BookingIdDTO> A list contains BookingIdDTO objects that are returned from endpoint.
     */
    @SneakyThrows
    public List<BookingIdDTO> getBookingsIdList(String firstName, String lastName, String checkInDate, String checkOutDate) {
        log.info("Getting available booking list from service");
        Response<List<BookingIdDTO>> response = bookingApi.getBookingIds(firstName, lastName, checkInDate, checkOutDate).execute();
        logResponseCode(response);
        return response.body();
    }

    /**
     * This method gets booking detail from the BE for specific booking id.
     * @param bookingId int booking id value.
     * @return BookingDTO object contains all booking detail.
     */
    public BookingDTO getBookingDetail(int bookingId) {
        BookingIdDTO bookingIdDTO = new BookingIdDTO(bookingId);
        return getBookingDetail(bookingIdDTO);
    }

    /**
     * This method gets booking detail from the BE for specific BookingIdDTO object.
     * @param bookingIdDTO BookingIdDTO object for specific booking. This method is overloaded with a method accepts int value please see {@link #getBookingDetail(int bookingId)}
     * @return BookingDTO object contains all booking detail.
     */
    @SneakyThrows
    public BookingDTO getBookingDetail(BookingIdDTO bookingIdDTO) {
        log.info("Getting booking detail for the booking with id of {}.", bookingIdDTO.getBookingId());
        Response<BookingDTO> response = bookingApi.getBooking(bookingIdDTO.getBookingId()).execute();
        logResponseCode(response);
        return response.body();
    }

    /**
     * This method creates new booking with specified BookingDTO object.
     * @param bookingDTO BookingDTO object that contains booking detail.
     * @return BookingExtendedDTO an object that contains BookingIdDTO and BookingDTO as response of BE from creation of booking.
     */
    @SneakyThrows
    public BookingExtendedDTO createBooking(BookingDTO bookingDTO) {
        log.info("Setting a booking for the name of {} {}.", bookingDTO.getFirstName(), bookingDTO.getLastName());
        Response<BookingExtendedDTO> response = bookingApi.createBooking(bookingDTO).execute();
        logResponseCode(response);
        if (response.code() == 200)
            log.info("Booking has been created.");
        return response.body();
    }

    /**
     * This method updates existing booking with specified BookingExtendedDTO object.
     * @param bookingExtendedDTO an object that contains BookingIdDTO and BookingDTO.
     * @return BookingDTO object that contains booking detail after update.
     */
    @SneakyThrows
    public BookingDTO updateBooking(BookingExtendedDTO bookingExtendedDTO) {
        BookingDTO bookingDTO = bookingExtendedDTO.getBookingDetails();
        int bookingId = bookingExtendedDTO.getBookingId();
        log.info("Updating booking for booking id of {}", bookingId);
        Response<BookingDTO> response = bookingApi.updateBooking(bookingId, bookingDTO).execute();
        logResponseCode(response);
        return response.body();
    }

    /**
     * This method removes existing booking with specified booking id.
     * @param bookingId int booking id value.
     * @return Boolean Method will return true if the request successfully made.
     */
    @SneakyThrows
    public boolean deleteBooking(int bookingId) {
        log.info("Removing the booking with booking id of {}", bookingId);
        return bookingApi.deleteBooking(bookingId).execute().code() == 201;
    }

    /**
     * This method returns the response code of get Booking. For request body please see {@link #getBookingDetail(int bookingId)}
     * @param bookingId int booking id value.
     * @return HTTP status code.
     */
    @SneakyThrows
    public int getResponseCodeOfBookingGetRequest(int bookingId) {
        return bookingApi.getBooking(bookingId).execute().code();
    }

}
