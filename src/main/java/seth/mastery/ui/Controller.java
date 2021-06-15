package seth.mastery.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seth.mastery.data.DataAccessException;
import seth.mastery.domain.GuestService;
import seth.mastery.domain.HostService;
import seth.mastery.domain.ReservationService;
import seth.mastery.domain.Result;
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
                    // makeReservation();
                    break;
                case EDIT_RESERVATION:
                    // editReservation();
                    break;
                case CANCEL_RESERVATION:
                   // cancelReservation();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    private void viewReservations() {
        String email = view.getEmail();
        Result result = hostService.findByEmail(email);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        } else {
            Host host = (Host) result.getPayload();
            String hostId = host.getId();
            List<Reservation> reservations = reservationService.findAll(hostId);
            view.displayReservations(reservations, host);
            view.enterToContinue();
        }
    }
}
