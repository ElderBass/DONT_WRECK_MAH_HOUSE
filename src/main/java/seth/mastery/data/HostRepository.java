package seth.mastery.data;

import seth.mastery.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll();
    Host findById(String id);
    Host findByEmail(String email);
    Host add(Host host) throws DataAccessException;
    boolean update(Host host) throws DataAccessException;
    boolean delete(Host host) throws DataAccessException;
}
