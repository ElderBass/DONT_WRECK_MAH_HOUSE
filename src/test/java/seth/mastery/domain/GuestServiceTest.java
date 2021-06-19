package seth.mastery.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.models.Guest;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    private GuestService service;
    private Guest GUEST1;

    @BeforeEach
    void setUp() {
        GUEST1 = new Guest(GuestRepositoryDouble.GUEST1);
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

    @Test
    void shouldUpdate() throws DataAccessException {
        GUEST1.setLastName("Tendieman Esquire");
        Result result = service.update(GUEST1);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNonExistentGuest() throws DataAccessException {
        Guest triss = new Guest(1003, "Triss", "Merigold", "marrygold@gmail.com", "(666) 6578905", "TM");

        Result result = service.update(triss);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDelete() throws DataAccessException {
        Result result = service.delete(GUEST1);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteNonexistentGuest() throws DataAccessException {
        Guest triss = new Guest(1003, "Triss", "Merigold", "marrygold@gmail.com", "(666) 6578905", "TM");

        Result result = service.delete(triss);
        assertFalse(result.isSuccess());
    }

}