package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.HostRepository;
import seth.mastery.data.HostRepository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Host;
import seth.mastery.models.Host;

import java.math.BigDecimal;
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

        Result<Host> result = new Result();
        Host host = repository.findByEmail(email);
        if (host == null) {
            result.addErrorMessage("No Host found with email address \"" + email + "\".");
        } else {
            result.setPayload(host);
        }

        return result;
    }


    public Result add(Host host) throws DataAccessException {
        Result result = validate(host);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateDuplicate(host, result);
        if (!result.isSuccess()) {
            return result;
        }

        Host added = repository.add(host);
        result.setPayload(added);

        System.out.println("Host " + added.getId() + " has been added to database.");
        return result;
    }

    public Result update(Host host) throws DataAccessException {
        Result<Host> result = validate(host);
        if(!result.isSuccess()) {
            return result;
        }

        if (repository.update(host)) {
            result.setPayload(host);
        }
        System.out.println();
        System.out.println("Host " + host.getId() + " has been updated.");
        return result;
    }


    public Result delete(Host host) throws DataAccessException {
        Result result = new Result();

        boolean isDeleted = repository.delete(host);
        if (!isDeleted) {
            result.addErrorMessage("Host " + host.getId() + " not in database.");
        }
        System.out.println();
        System.out.println("Host " + host.getId() + " has been deleted.");

        return result;
    }

    // VALIDATION METHODS
    // ==========================================================================================================
    
    private Result validate(Host host) {
        Result result = new Result();

        result = validateNulls(host, result);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateFields(host, result);
        if (!result.isSuccess()) {
            return result;
        }

        return result;
    }

    private Result validateNulls(Host host, Result result) {
        if (host == null) {
            result.addErrorMessage("Host cannot be null.");
        }

        if (host.getLastName() == null) {
            result.addErrorMessage("Host last name cannot be null.");
        }

        if (host.getEmail() == null) {
            result.addErrorMessage("Host email cannot be null.");
        }

        if (host.getPhone() == null) {
            result.addErrorMessage("Host phone number cannot be null.");
        }
        
        if (host.getAddress() == null) {
            result.addErrorMessage("Host address cannot be null.");
        }
        
        if (host.getCity() == null) {
            result.addErrorMessage("Host city cannot be null.");
        }

        if (host.getState() == null) {
            result.addErrorMessage("Host state cannot be null.");
        }
        
        return result;
    }

    private Result validateFields(Host host, Result result) {

        if (host.getLastName().equals("")) {
            result.addErrorMessage("Host last name required.");
        }

        if (host.getEmail().equals("")) {
            result.addErrorMessage("Host email required.");
        } else if (host.getEmail().indexOf("@") == -1) {
            result.addErrorMessage("Invalid email address.");
        }

        if (host.getPhone().equals("")) {
            result.addErrorMessage("Host phone number required.");
        } else if (host.getPhone().length() != 13) {
            result.addErrorMessage("Invalid phone number.");
        }
        
        if (host.getAddress().equals("")) {
            result.addErrorMessage("Host address is required.");
        }
        
        if (host.getCity().equals("")) {
            result.addErrorMessage("Host city is required.");
        }

        if (host.getState().equals("")) {
            result.addErrorMessage("Host state required.");
        } else if (host.getState().length() != 2) {
            result.addErrorMessage("State must be in appropriate abbreviated form.");
        }

        if (host.getPostalCode().equals("")) {
            result.addErrorMessage("Postal code required.");
        } else if (host.getPostalCode().length() != 5) {
            result.addErrorMessage("Invalid postal code.");
        }

        if (host.getStandardRate().compareTo(new BigDecimal(0)) < 0) {
            result.addErrorMessage("Standard rate must be greater than 0.");
        }

        if (host.getWeekendRate().compareTo(new BigDecimal(0)) < 0) {
            result.addErrorMessage("Weekend rate must be greater than 0.");
        }

        return result;
    }

    private Result validateDuplicate(Host host, Result result) {
        List<Host> hosts = findAll();
        for (Host h : hosts) {
            if (h.getLastName().equals(host.getLastName())
                    && h.getEmail().equals(host.getEmail()) && h.getPhone().equals(host.getPhone())
                    && h.getState().equals(host.getState()) && h.getAddress().equals(host.getAddress())) {
                result.addErrorMessage("Host already exists in database.");
            }
        }
        return result;
    }
}
