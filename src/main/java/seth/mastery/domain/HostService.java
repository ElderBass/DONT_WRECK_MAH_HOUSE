package seth.mastery.domain;

import seth.mastery.data.GuestRepository;
import seth.mastery.data.HostRepository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;

import java.util.List;

public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() {
        return repository.findAll();
    }


}
