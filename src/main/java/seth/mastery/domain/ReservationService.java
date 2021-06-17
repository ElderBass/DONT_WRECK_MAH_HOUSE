package seth.mastery.domain;

import org.springframework.stereotype.Service;
import seth.mastery.data.DataAccessException;
import seth.mastery.data.GuestRepository;
import seth.mastery.data.HostRepository;
import seth.mastery.data.ReservationRepository;
import seth.mastery.models.Guest;
import seth.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final HostRepository hostRepo;
    private final GuestRepository guestRepo;

    public ReservationService(ReservationRepository reservationRepo, HostRepository hostRepo, GuestRepository guestRepo) {
        this.reservationRepo = reservationRepo;
        this.hostRepo = hostRepo;
        this.guestRepo = guestRepo;
    }

    public List<Reservation> findAll(String hostId) {
        return reservationRepo.findAll(hostId);
    }

    public Result add(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        Reservation added = reservationRepo.add(reservation);
        result.setPayload(added);

        return result;
    }

    public Result update(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);
        if(!result.isSuccess()) {
            return result;
        }

        if (reservationRepo.update(reservation)) {
            result.setPayload(reservation);
        }
        return result;
    }

    public Result delete(Reservation reservation) throws DataAccessException {
        Result result = new Result();
        result = validateDates(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        boolean isDeleted = reservationRepo.delete(reservation);
        if (!isDeleted) {
            result.addErrorMessage("Reservation " + reservation.getId() + " not in database.");
        }
        System.out.println();
        System.out.println("Reservation " + reservation.getId() + " has been deleted.");
        return result;
    }

    // VALIDATION METHODS
    // ================================================================================================

    private Result validate(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        result = validateNulls(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        // TODO may need a different one for update? Or to tweak the one I already have...?
        result = validateDates(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        return result;
    }

    private Result validateNulls(Reservation reservation, Result<Reservation> result) {
        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null.");
            return result;
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Reservation needs a Guest.");
            return result;
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Reservation must have a start date.");
            return result;
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Reservation must have an end date.");
            return result;
        }

        if (reservation.getTotal() == null) {
            result.addErrorMessage("Total amount missing for reservation.");
            return result;
        }
        return result;
    }

    private Result validateFields(Reservation reservation, Result<Reservation> result) {
        Guest guest = reservation.getGuest();
        if (guest.getFirstName() == null) {
            result.addErrorMessage("Guest must have a first name.");
            return result;
        } else if (guest.getFirstName().equals("")) {
            result.addErrorMessage("Guest must have a first name.");
        }

        if (guest.getLastName() == null) {
            result.addErrorMessage("Guest must have a last name.");
            return result;
        } else if (guest.getLastName().equals("")) {
            result.addErrorMessage("Guest must have a last name.");
        }

        if (guest.getEmail() == null) {
            result.addErrorMessage("Guest must have a valid email address.");
            return result;
        } else if (guest.getEmail().equals("")) {
            result.addErrorMessage("Guest must have a valid email address.");
        }

        if (reservation.getId() <= 0) {
            result.addErrorMessage("Reservation ID must be greater than 0.");
        }

        if (reservation.getGuestId() <= 0) {
            result.addErrorMessage("Guest ID must be greater than 0.");
        }

        if (reservation.getTotal().compareTo(new BigDecimal(0)) <= 0) {
            result.addErrorMessage("Total cost of stay must be greater than zero.");
        }

        return result;
    }

    // TODO probably need to make sure you can't update a PAST reservation!
    private Result validateDates(Reservation reservation, Result<Reservation> result) {

        if (reservation.getStartDate().compareTo(LocalDate.now()) < 0) {
            result.addErrorMessage("Start date is in the past. Stop living in the past and move on with your life.");
        }

        if (reservation.getStartDate().compareTo(reservation.getEndDate()) >= 0) {
            result.addErrorMessage("Start date must come before end date. Entropy dictates time must move forward, not backward.");
        }

        List<Reservation> all = reservationRepo.findAll(reservation.getHost().getId());
        for (Reservation r : all) {
            // Checking to see if incoming reservation dates are within an existing set of dates
            if ((r.getStartDate().compareTo(reservation.getStartDate()) >= 0 && r.getEndDate().compareTo(reservation.getEndDate()) <= 0)
                    || (r.getStartDate().compareTo(reservation.getStartDate()) <= 0 && r.getEndDate().compareTo(reservation.getEndDate()) >= 0)
                    && r.getId() != reservation.getId()) {
                result.addErrorMessage("Time slot already filled. Please select a different date.");
            }
            // Checking to see if start date of incoming reservation is within an existing reservation's range
            if (reservation.getStartDate().compareTo(r.getStartDate()) < 0 && reservation.getEndDate().compareTo(r.getStartDate()) > 0 && r.getId() != reservation.getId()) {
                result.addErrorMessage("Dates overlap with existing Reservation. Please select a different date.");
            }
            // Checking to see if end date of incoming reservation is within an existing reservation's range
            if (reservation.getStartDate().compareTo(r.getEndDate()) < 0 && reservation.getEndDate().compareTo(r.getEndDate()) > 0 && r.getId() != reservation.getId()) {
                result.addErrorMessage("Dates overlap with existing Reservation. Please select a different date.");
            }
        }
        return result;
    }

}
