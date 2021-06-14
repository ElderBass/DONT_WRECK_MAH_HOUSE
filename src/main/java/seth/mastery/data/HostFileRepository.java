package seth.mastery.data;

import seth.mastery.models.Host;

import java.util.List;

public class HostFileRepository implements HostRepository {

    private final String filePath = "./data/hosts.csv";

    @Override
    public List<Host> findAll() {

    }

    @Override
    public Host findByEmail(String email) {
        return null;
    }
}
