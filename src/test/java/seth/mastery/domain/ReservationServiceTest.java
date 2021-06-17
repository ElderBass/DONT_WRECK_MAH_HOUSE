package seth.mastery.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.data.HostRepositoryDouble;
import seth.mastery.data.ReservationRepositoryDouble;
import seth.mastery.models.Guest;
import seth.mastery.models.Reservation;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private static final String hostId = "498604db-b6d6-4599-a503-3d8190fda823";

    private ReservationService service;
    private Guest guest1Copy = new Guest(GuestRepositoryDouble.GUEST1);

    @BeforeEach
    void setUp() {
        service = new ReservationService(
                new ReservationRepositoryDouble(),
                new HostRepositoryDouble(),
                new GuestRepositoryDouble());
    }


    @Test
    void shouldFindAll() {
        int actual = service.findAll(hostId).size();
        assertEquals(2, actual);
    }

    // "ADD" TESTS
    // =====================================================================================

    @Test
    void shouldAddReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 8, 10));
        reservation.setEndDate(LocalDate.of(2021, 8, 13));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldAddStartDateOnExistingEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 15));
        reservation.setEndDate(LocalDate.of(2021, 9, 18));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldAddEndDateOnExistingStartDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 4));
        reservation.setEndDate(LocalDate.of(2021, 9, 9));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullReservation() throws DataAccessException {
        Reservation reservation = null;
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullGuest() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setId(3);
        reservation.setGuest(null);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuestId(100);
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullHost() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(null);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullDates() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(null);
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        reservation.setStartDate(LocalDate.of(2021, 6, 20));
        reservation.setEndDate(null);

        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyGuestEmail() throws DataAccessException {
        Reservation reservation = new Reservation();

        guest1Copy.setEmail("");

        reservation.setId(3);
        reservation.setGuest(guest1Copy);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuestId(guest1Copy.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddEmptyGuestName() throws DataAccessException {
        Reservation reservation = new Reservation();

        guest1Copy.setFirstName("");

        reservation.setId(3);
        reservation.setGuest(guest1Copy);
        reservation.setGuestId(guest1Copy.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        guest1Copy.setLastName("");
        guest1Copy.setFirstName(GuestRepositoryDouble.GUEST1.getFirstName());
        reservation.setGuest(guest1Copy);

        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddInvalidReservationId() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        reservation.setId(0);
        result = service.add(reservation);
        assertFalse(result.isSuccess());

        reservation.setId(-5);
        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }


    // TODO make sure these tests for add are exhaustive - have shouldAdd tests for startDate = existing endDate and the inverse

    @Test
    void shouldNotAddReservationInPast() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2020, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEndDateLessThanStartDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 20));
        reservation.setEndDate(LocalDate.of(2021, 6, 19));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationDuringExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 2));
        reservation.setEndDate(LocalDate.of(2021, 11, 5));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddStartDateInsideExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 4));
        reservation.setEndDate(LocalDate.of(2021, 11, 9));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEndDateInsideExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 10, 30));
        reservation.setEndDate(LocalDate.of(2021, 11, 5));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationThatContainsExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(3);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 30));
        reservation.setEndDate(LocalDate.of(2021, 11, 9));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    // "UPDATE" TESTS
    // ====================================================================================================

    @Test
    void shouldNotUpdateReservationInPast() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2020, 10, 10));
        reservation.setEndDate(LocalDate.of(2020, 10, 4));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateEndDateLessThanStartDate() throws DataAccessException {

        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 10, 10));
        reservation.setEndDate(LocalDate.of(2021, 10, 4));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateReservationDuringExistingReservation() throws DataAccessException {

        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 10));
        reservation.setEndDate(LocalDate.of(2021, 9, 14));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateStartDateInsideExistingReservation() throws DataAccessException {

        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 12));
        reservation.setEndDate(LocalDate.of(2021, 9, 16));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateEndDateInsideExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 8));
        reservation.setEndDate(LocalDate.of(2021, 9, 14));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateReservationThatContainsExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 8));
        reservation.setEndDate(LocalDate.of(2021, 9, 16));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
        // I might be going crazy but when I run all tests, this one fails, but when I debug it, it succeeds
    void shouldUpdateStartDateOnExistingEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 15));
        reservation.setEndDate(LocalDate.of(2021, 9, 18));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldUpdateEndDateOnExistingStartDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 9, 4));
        reservation.setEndDate(LocalDate.of(2021, 9, 9));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldUpdate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 1));
        reservation.setEndDate(LocalDate.of(2021, 11, 8));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    // "DELETE" TESTS
    // =========================================================================================================

    @Test
    void shouldDelete() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 1));
        reservation.setEndDate(LocalDate.of(2021, 11, 9));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.delete(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeletePastReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2020, 11, 1));
        reservation.setEndDate(LocalDate.of(2020, 11, 9));
        reservation.setTotal(reservation.calculateTotal());

        Result result = service.delete(reservation);
        assertFalse(result.isSuccess());
    }
}