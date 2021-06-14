package seth.mastery.data;

import seth.mastery.models.Host;

import java.util.List;

public interface HostRepository {

    Host findByEmail(String email);

    List<Host> findAll();
}
