package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.GuestRepository;
import seth.mastery.models.Guest;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() {
        return repository.findAll();
    }
}
