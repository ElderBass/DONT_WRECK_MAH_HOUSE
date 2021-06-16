package seth.mastery.domain;

import org.junit.jupiter.api.Test;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.data.HostRepositoryDouble;
import seth.mastery.data.ReservationRepositoryDouble;
import seth.mastery.models.Guest;
import seth.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private static final String hostId = "498604db-b6d6-4599-a503-3d8190fda823";

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new HostRepositoryDouble(),
            new GuestRepositoryDouble());

    @Test
    void shouldFindAll() {
        int actual = service.findAll(hostId).size();
        assertEquals(1, actual);
    }

    @Test
    void shouldAddReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 8, 10));
        reservation.setEndDate(LocalDate.of(2021, 8, 13));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
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
        reservation.setGuest(null);
        reservation.setGuestId(100);
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullDates() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
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
        Guest guest = GuestRepositoryDouble.GUEST;
        guest.setEmail("");

        reservation.setGuest(guest);
        reservation.setGuestId(guest.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddEmptyGuestName() throws DataAccessException {
        Reservation reservation = new Reservation();
        Guest guest = GuestRepositoryDouble.GUEST;
        guest.setFirstName("");

        reservation.setGuest(guest);
        reservation.setGuestId(guest.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        guest.setLastName("");
        guest.setFirstName(GuestRepositoryDouble.GUEST.getFirstName());
        reservation.setGuest(guest);

        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    // TODO make sure these tests for add are exhaustive

    @Test
    void shouldNotAddReservationInPast() throws DataAccessException{
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2020, 6, 18));
        reservation.setEndDate(LocalDate.of(2021, 6, 20));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEndDateLessThanStartDate() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 6, 20));
        reservation.setEndDate(LocalDate.of(2021, 6, 19));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationDuringExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 8, 1));
        reservation.setEndDate(LocalDate.of(2021, 8, 2));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddStartDateInsideExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 8, 1));
        reservation.setEndDate(LocalDate.of(2021, 8, 9));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEndDateInsideExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 7, 30));
        reservation.setEndDate(LocalDate.of(2021, 8, 5));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationThatContainsExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();

        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getId());
        reservation.setStartDate(LocalDate.of(2021, 7, 30));
        reservation.setEndDate(LocalDate.of(2021, 8, 9));
        reservation.setTotal(new BigDecimal(100.00));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    // TODO need to test all the update stuff too!
}