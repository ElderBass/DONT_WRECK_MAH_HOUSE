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

    @Test
    void shouldNotAddEmptyName() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("(999) 9999999");
        guest.setState("TM");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());

        guest.setFirstName("Triss");
        guest.setLastName("");

        result = service.add(guest);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddEmptyEmail() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("");
        guest.setPhone("(999) 9999999");
        guest.setState("TM");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddInvalidEmail() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold.com");
        guest.setPhone("(999) 9999999");
        guest.setState("TM");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyPhone() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("");
        guest.setState("TM");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddInvalidPhone() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("(999 9999999");
        guest.setState("TM");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldNotAddEmptyState() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("(999) 9999999");
        guest.setState("");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddInvalidState() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(3);
        guest.setFirstName("Triss");
        guest.setLastName("Merigold");
        guest.setEmail("merrygold@gmail.com");
        guest.setPhone("(999) 9999999");
        guest.setState("Temeria");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddDuplicateGuest() throws DataAccessException {
        Guest guest = new Guest();
        guest.setId(1002);
        guest.setFirstName("Sir Charles");
        guest.setLastName("Tendieman");
        guest.setEmail("tendies@mail.com");
        guest.setPhone("(420) 6996");
        guest.setState("FL");

        Result result = service.add(guest);
        assertFalse(result.isSuccess());
    }

}