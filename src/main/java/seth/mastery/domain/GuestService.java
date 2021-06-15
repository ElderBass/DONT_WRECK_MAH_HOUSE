package seth.mastery.domain;

import seth.mastery.data.GuestRepository;
import seth.mastery.models.Guest;

import java.util.List;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() {
        return repository.findAll();
    }
}
