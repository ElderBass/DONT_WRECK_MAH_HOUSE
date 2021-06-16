package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.GuestRepository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;

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

    public Result findByEmail(String email) {
        Result<Guest> result = new Result();
        Guest guest = repository.findByEmail(email);
        if (guest == null) {
            result.addErrorMessage("No Guest found with email address \"" + email + "\".");
        } else {
            result.setPayload(guest);
        }

        return result;
    }
}
