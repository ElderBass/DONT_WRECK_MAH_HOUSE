package seth.mastery.domain;

import org.junit.jupiter.api.Test;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.data.HostRepositoryDouble;
import seth.mastery.data.ReservationRepositoryDouble;

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
}