package seth.mastery.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seth.mastery.data.DataAccessException;
import seth.mastery.domain.GuestService;
import seth.mastery.domain.HostService;
import seth.mastery.domain.ReservationService;
import seth.mastery.domain.Result;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

import java.util.List;

@Component
public class Controller {

    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;
    private final View view;

    @Autowired
    public Controller(ReservationService reservationService, HostService hostService, GuestService guestService, View view) {
        this.reservationService = reservationService;
        this.hostService = hostService;
        this.guestService = guestService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Greetings, Admin, and Welcome to your Dashboard.");
        try {
            runMenuLoop();
        } catch (DataAccessException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye, Admin.");
    }

    public void runMenuLoop() throws DataAccessException {
        MenuOption option;
        do {
            option = view.selectMenuOption();
            switch (option) {
                case VIEW_HOST_RESERVATIONS:
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    // MENU METHODS
    // =================================================================================================

    private void viewReservations() {
        String email = view.getEmail("Host");
        Result result = hostService.findByEmail(email);
        if (!result.isSuccess()) {
            view.displayResult(result);
            return;
        } else {
            Host host = (Host) result.getPayload();
            String hostId = host.getId();
            List<Reservation> reservations = reservationService.findAll(hostId);
            view.displayReservations(reservations, host);
            view.enterToContinue();
        }
    }

    private void makeReservation() throws DataAccessException {
        view.displayHeader(MenuOption.MAKE_RESERVATION.getMessage());

        Result guestResult = getGuestOrHostFromEmail("Guest");
        if (!guestResult.isSuccess()) {
            view.displayResult(guestResult);
            return;
        }
        Result hostResult = getGuestOrHostFromEmail("Host");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }
        Guest guest = (Guest) guestResult.getPayload();
        Host host = (Host) hostResult.getPayload();
        List<Reservation> reservations = reservationService.findAll(host.getId());
        view.displayReservations(reservations, host);

        Reservation reservation = view.createReservation(guest, host);
        Result resResult = reservationService.add(reservation);
        view.displayResult(resResult);
    }

    private void editReservation() throws DataAccessException {
        view.displayHeader(MenuOption.EDIT_RESERVATION.getMessage());

        Result hostResult = getGuestOrHostFromEmail("Host");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }

        Host host = (Host) hostResult.getPayload();
        List<Reservation> reservations = reservationService.findAll(host.getId());
        Reservation reservation = view.editReservation(reservations, host);
        Result<Reservation> resResult = reservationService.update(reservation);
        view.displayResult(resResult);
    }

    private void cancelReservation() throws DataAccessException {
        view.displayHeader(MenuOption.CANCEL_RESERVATION.getMessage());
        Result hostResult = getGuestOrHostFromEmail("Host");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }

        Host host = (Host) hostResult.getPayload();
        List<Reservation> reservations = reservationService.findAll(host.getId());
        Reservation reservation = view.cancelReservation(reservations, host);
        Result<Reservation> resResult = reservationService.delete(reservation);
        view.displayResult(resResult);
    }

    private Result getGuestOrHostFromEmail(String type) {
        String email = view.getEmail(type);
        Result result = new Result();
        if (type.equals("Guest")) {
            result = guestService.findByEmail(email);
        }
        if (type.equals("Host")) {
            result = hostService.findByEmail(email);
        }

        return result;
    }

}
