package seth.mastery.data;

import seth.mastery.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble {

    public final static Host HOST = makeHost();

    private final List<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

    private static Host makeHost() {
        // id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
        Host host = new Host();
        host.setId("498604db-b6d6-4599-a503-3d8190fda823");
        host.setLastName("Testerson");
        host.setEmail("test@mail.com");
        host.setPhone("(555) 5555555");
        host.setAddress("123 Test Ave");
        host.setCity("Testville");
        host.setState("FL");
        host.setPostalCode(90210);
        host.setStandardRate(new BigDecimal(60.0));
        host.setWeekendRate(new BigDecimal(80.0));

        return host;
    }
}
