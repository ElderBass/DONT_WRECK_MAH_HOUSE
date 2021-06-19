package seth.mastery.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepositoryDouble;
import seth.mastery.data.HostRepositoryDouble;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    private HostService service;
    private GuestRepositoryDouble guestRepo;
    private Host HOST;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        service = new HostService(new HostRepositoryDouble());
        HOST = new Host(HostRepositoryDouble.HOST);
        guestRepo = new GuestRepositoryDouble();
        guests = guestRepo.findAll();
    }

    @Test
    void shouldFindAll() {
        int actual = service.findAll().size();
        assertEquals(1, actual);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        // id, lastName,  email,  phone,  address,  city,  state,  postalCode,  standardRate,  weekendRate
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "(333) 3425678",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyLastName() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "", "life_aquatic@gmail.com", "(333) 3425678",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyOrInvalidEmail() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "", "(333) 3425678",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());

        host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquaticgmail.com", "(333) 3425678",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        result = service.add(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyOrInvalidPhone() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());

        host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "() 3425678",
                "123 Submarine Village", "Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        result = service.add(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyAddress() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "(333) 3425678",
                "","Naples", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddEmptyCity() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "(333) 3425678",
                "123 Submarine Village","", "IT", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());    }

    @Test
    void shouldNotAddEmptyOrInvalidState() throws DataAccessException {
        Host host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "(333) 3425678",
                "123 Submarine Village","Naples", "", "80031", new BigDecimal(200), new BigDecimal(300));

        Result result = service.add(host);
        assertFalse(result.isSuccess());

        host = new Host("659604db-b6d6-4599-a503-3d8190fda659", "Zissou", "life_aquatic@gmail.com", "(333) 3425678",
                "123 Submarine Village","Naples", "Italy", "80031", new BigDecimal(200), new BigDecimal(300));

        result = service.add(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdate() throws DataAccessException {
        HOST.setLastName("Heston");

        Result result = service.update(HOST);
        assertTrue(result.isSuccess());

        assertEquals("Heston", HOST.getLastName());
    }

}