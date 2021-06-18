package seth.mastery.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.models.Guest;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    private GuestService service;

    @BeforeEach
    void setUp() {
        service = new GuestService(new GuestRepositoryDouble());
    }
    // TODO write some tests!

    @Test
    void shouldFindAll() {
        int actual = service.findAll().size();
        assertEquals(2, actual);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("(999) 9999999");
        guest.setState("TM");

        Result result = service.add(guest);
        assertTrue(result.isSuccess());
    }

}