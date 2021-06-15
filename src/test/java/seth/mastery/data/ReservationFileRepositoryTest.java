package seth.mastery.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

}