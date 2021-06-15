package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.GuestRepository;
import seth.mastery.data.HostRepository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;

import java.util.List;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() {
        return repository.findAll();
    }

    public Result findByEmail(String email) {
        // TODO may need some validation here, e.g. if the repo returns null or something
        Result<Host> result = new Result();
        Host host = repository.findByEmail(email);
        if (host == null) {
            result.addErrorMessage("No Host found with email address \"" + email + "\". Please try again.");
        } else {
            result.setPayload(host);
        }

        return result;
    }

}
