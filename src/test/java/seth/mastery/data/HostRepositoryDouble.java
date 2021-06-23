package seth.mastery.data;

import seth.mastery.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    public final static Host HOST = makeHost();

    private final List<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

    private static Host makeHost() {

        Host host = new Host();
        host.setId("498604db-b6d6-4599-a503-3d8190fda823");
        host.setLastName("Testerson");
        host.setEmail("test@mail.com");
        host.setPhone("(555) 5555555");
        host.setAddress("123 Test Ave");
        host.setCity("Testville");
        host.setState("FL");
        host.setPostalCode("90210");
        host.setStandardRate(new BigDecimal(60.0));
        host.setWeekendRate(new BigDecimal(80.0));

        return host;
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findById(String id) {
        return null;
    }

    @Override
    public Host findByEmail(String email) {
        return null;
    }

    @Override
    public Host add(Host host) throws DataAccessException {
        return host;
    }

    @Override
    public boolean update(Host host) throws DataAccessException {
        for (int i = 0; i < hosts.size(); i++) {
            if (hosts.get(i).getId().equals(host.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataAccessException {
        for (int i = 0; i < hosts.size(); i++) {
            if (hosts.get(i).getId().equals(host.getId())) {
                return true;
            }
        }
        return false;
    }
}
