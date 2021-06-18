package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.DataAccessException;
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

    public Result add(Guest guest) throws DataAccessException {
        Result result = validate(guest);
        if (!result.isSuccess()) {
            return result;
        }

        Guest added = repository.add(guest);
        result.setPayload(added);

        System.out.println("Guest " + added.getId() + " has been added to database.");
        return result;
    }

    // VALIDATION METHODS
    // =========================================================================================================

    public Result validate(Guest guest) {
        Result result = new Result();

        result = validateNulls(guest, result);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateFields(guest, result);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateDuplicate(guest, result);
        if (!result.isSuccess()) {
            return result;
        }
        return result;
    }

    public Result validateNulls(Guest guest, Result result) {
        if (guest == null) {
            result.addErrorMessage("Guest cannot be null.");
        }

        if (guest.getLastName() == null) {
            result.addErrorMessage("Guest last name cannot be null.");
        }

        if (guest.getFirstName() == null) {
            result.addErrorMessage("Guest first name cannot be null.");
        }

        if (guest.getEmail() == null) {
            result.addErrorMessage("Guest email cannot be null.");
        }

        if (guest.getPhone() == null) {
            result.addErrorMessage("Guest phone number cannot be null.");
        }

        if (guest.getState() == null) {
            result.addErrorMessage("Guest state cannot be null.");
        }
        return result;
    }

    public Result validateFields(Guest guest, Result result) {

        if (guest.getLastName() == "") {
            result.addErrorMessage("Guest last name required.");
        }

        if (guest.getFirstName() == "") {
            result.addErrorMessage("Guest first name required.");
        }

        if (guest.getEmail() == "") {
            result.addErrorMessage("Guest email required.");
        } else if (guest.getEmail().indexOf("@") == -1) {
            result.addErrorMessage("Invalid email address.");
        }

        if (guest.getPhone() == "") {
            result.addErrorMessage("Guest phone number required.");
        } else if (guest.getPhone().length() != 13) {
            result.addErrorMessage("Invalid phone number.");
        }

        if (guest.getState() == "") {
            result.addErrorMessage("Guest state required.");
        } else if (guest.getState().length() != 2) {
            result.addErrorMessage("State must be in appropriate abbreviated form.");
        }

        return result;
    }

    private Result validateDuplicate(Guest guest, Result result) {
        List<Guest> guests = findAll();
        for (Guest g : guests) {
            if (g.getFirstName().equals(guest.getFirstName()) && g.getLastName().equals(guest.getLastName())
                    && g.getEmail().equals(guest.getEmail()) && g.getPhone().equals(guest.getPhone())
                    && g.getPhone().equals(guest.getState())) {
                result.addErrorMessage("Guest already exists in database.");
            }
        }
        return result;
    }
}
