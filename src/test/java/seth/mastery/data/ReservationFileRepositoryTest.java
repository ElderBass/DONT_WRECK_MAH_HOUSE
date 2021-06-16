package seth.mastery.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    // TODO make a copy of data files..

    private String RES_TEST_PATH = "./data/reservations-test-data";
    private String RES_SEED_PATH = "./data/reservations-seed-3edda6bc-ab95-49a8-8962-d50b53f84b15.csv";

    private String HOST_TEST_PATH = "./data/hosts-test.csv";
    private String HOST_SEED_PATH = "./data/hosts-seed.csv";

    private String GUEST_TEST_PATH = "./data/guests-test.csv";
    private String GUEST_SEED_PATH = "./data/guests-seed.csv";

    private String hostId = "3edda6bc-ab95-49a8-8962-d50b53f84b15";

    HostFileRepository hostRepo = new HostFileRepository(HOST_TEST_PATH);
    GuestFileRepository guestRepo = new GuestFileRepository(GUEST_TEST_PATH);

    ReservationRepository resRepo = new ReservationFileRepository(RES_TEST_PATH, guestRepo, hostRepo);


    @BeforeEach
    void setup() throws DataAccessException, IOException {
        Files.copy(Paths.get(RES_SEED_PATH), Paths.get(RES_TEST_PATH+"/"+hostId+".csv"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(HOST_SEED_PATH), Paths.get(HOST_TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(GUEST_SEED_PATH), Paths.get(GUEST_TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException, IOException {
        int actual = resRepo.findAll(hostId).size();
        assertEquals(13, actual);
    }

    @Test
    void shouldAddReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        Host host = hostRepo.findById(hostId);
        Guest guest = guestRepo.findAll().get(0);

        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2021, 6, 12));
        reservation.setEndDate(LocalDate.of(2021, 6, 15));
        reservation.setTotal(new BigDecimal(300.00));

        Reservation actual = resRepo.add(reservation);

        assertNotNull(actual);
        assertEquals(14, actual.getId());
    }

    @Test
    void shouldUpdateExistingReservation() throws DataAccessException {
        List<Reservation> reservations = resRepo.findAll(hostId);

        Reservation reservation = reservations.get(0); // 1,2021-07-31,2021-08-07,640,2550
        reservation.setStartDate(LocalDate.of(2021, 7, 30));
        reservation.setEndDate(LocalDate.of(2021, 8, 6));

        boolean actual = resRepo.update(reservation);
        assertEquals(true, actual);
    }

    @Test
    void shouldNotUpdateNonexistentReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setId(20);

        assertFalse(resRepo.update(reservation));
    }

    @Test
    void shouldDeleteExistingReservation() throws DataAccessException {
        List<Reservation> reservations = resRepo.findAll(hostId);
        Reservation reservation = reservations.get(0);
        boolean result = resRepo.delete(reservation);

        assertTrue(result);
    }

    @Test
    void shouldNotDeleteNonexistentReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);
        reservation.setId(30);
        boolean result = resRepo.delete(reservation);

        assertFalse(result);
    }
}