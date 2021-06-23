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

import java.io.IOException;
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
        } catch (DataAccessException | IOException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye, Admin.");
    }

    public void runMenuLoop() throws DataAccessException, IOException {
        MenuOption option;
        do {
            option = view.selectMenuOption();
            switch (option) {
                case VIEW_HOST_RESERVATIONS:
                    viewReservations();
                    break;
                case VIEW_GUEST_RESERVATIONS:
                    viewReservationsByGuest();
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
                case ADD_GUEST:
                    addGuest();
                    break;
                case EDIT_GUEST:
                    editGuest();
                    break;
                case DELETE_GUEST:
                    deleteGuest();
                    break;
                case ADD_HOST:
                    addHost();
                    break;
                case EDIT_HOST:
                    editHost();
                    break;
                case DELETE_HOST:
                    deleteHost();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    // MENU SWITCH STATEMENT METHODS
    // =================================================================================================

    //TODO some of these may be a bit long-winded...considering trimming or creating other helpers

    // RESERVATION METHODS ----------------------------------------------------------------------

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
            view.displayReservations(reservations, host, true);
            view.enterToContinue();
        }
    }

    private void viewReservationsByGuest() {
        view.displayHeader(MenuOption.VIEW_GUEST_RESERVATIONS.getMessage());
        String email = view.getEmail("Guest");
        Result result = guestService.findByEmail(email);
        if (!result.isSuccess()) {
            view.displayResult(result);
            return;
        } else {
            Guest guest = (Guest) result.getPayload();
            Result findAllResult = reservationService.findAllReservationsByGuest(guest);
            if (!result.isSuccess()) {
                System.out.println("Proceeding to Main Menu...");
                System.out.println();
                return;
            }
            List<Reservation> reservations = (List<Reservation>) findAllResult.getPayload();
            view.displayReservations(reservations, guest, true);
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
        view.displayReservations(reservations, host, false);

        Reservation reservation = view.createReservation(guest, host);
        if (reservation == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
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
        if (reservation == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
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
        if (reservation == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
        Result<Reservation> resResult = reservationService.delete(reservation);
        view.displayResult(resResult);
    }

    // GUEST METHODS ---------------------------------------------------------------------------------

    private void addGuest() throws DataAccessException {
        view.displayHeader(MenuOption.ADD_GUEST.getMessage());
        Guest guest = view.createGuest();
        Result result = guestService.add(guest);
        view.displayResult(result);
    }

    private void editGuest() throws DataAccessException {
        view.displayHeader(MenuOption.EDIT_GUEST.getMessage());

        Result hostResult = getGuestOrHostFromEmail("Guest");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }

        Guest guest = (Guest) hostResult.getPayload();
                Guest edited = view.editGuest(guest);
        if (edited == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
        Result<Guest> resResult = guestService.update(edited);
        view.displayResult(resResult);
    }

    private void deleteGuest() throws DataAccessException {
        view.displayHeader(MenuOption.DELETE_GUEST.getMessage());
        Result guestResult = getGuestOrHostFromEmail("Guest");
        if (!guestResult.isSuccess()) {
            view.displayResult(guestResult);
            return;
        }

        Guest guest = (Guest) guestResult.getPayload();
        Guest deleted = view.deleteGuest(guest);
        if (deleted == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
        Result<Guest> result = guestService.delete(guest);
        view.displayResult(result);
    }

    // HOST METHODS ------------------------------------------------------------------------------------

    private void addHost() throws DataAccessException {
        view.displayHeader(MenuOption.ADD_HOST.getMessage());
        Host host = view.createHost();
        Result result = hostService.add(host);
        view.displayResult(result);
    }
    
    private void editHost() throws DataAccessException {
        view.displayHeader(MenuOption.EDIT_HOST.getMessage());

        Result hostResult = getGuestOrHostFromEmail("Host");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }

        Host host = (Host) hostResult.getPayload();
        Host edited = view.editHost(host);
        if (edited == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
        Result<Host> resResult = hostService.update(edited);
        view.displayResult(resResult);
    }

    private void deleteHost() throws IOException, DataAccessException {
        view.displayHeader(MenuOption.DELETE_HOST.getMessage());
        Result hostResult = getGuestOrHostFromEmail("Host");
        if (!hostResult.isSuccess()) {
            view.displayResult(hostResult);
            return;
        }

        Host host = (Host) hostResult.getPayload();
        Host deleted = view.deleteHost(host);
        if (deleted == null) {
            System.out.println("Proceeding to Main Menu...");
            System.out.println();
            return;
        }
        Result<Host> result = hostService.delete(host);
        view.displayResult(result);
    }

    // HELPER METHODS
    // ====================================================================================
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
