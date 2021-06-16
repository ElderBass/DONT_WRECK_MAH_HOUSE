package seth.mastery.ui;

import org.springframework.stereotype.Component;
import seth.mastery.domain.Result;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.List;

@Component
public class View {

    private ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MenuOption selectMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MenuOption option : MenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max - 1);
        return MenuOption.fromValue(io.readInt(message, min, max));
    }

    public void displayReservations(List<Reservation> reservations, Host host) {
        displayHeader(MenuOption.VIEW_HOST_RESERVATIONS.getMessage());
        System.out.println();
        displayHeader(host.getLastName() + ", " + host.getEmail() + " - " + host.getCity() + ", " + host.getState());
        for (Reservation r : reservations) {
            // ID: 8, 08/12/2020 - 08/18/2020, Guest: Carncross, Tremain, Email: tcarncross2@japanpost.jp
            System.out.printf("ID: %s | Dates: %s - %s | Guest: %s, %s - %s",
                    r.getId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getGuest().getLastName(),
                    r.getGuest().getFirstName(),
                    r.getGuest().getEmail());
            System.out.println();
        }
    }

    // TODO need to figure out how to calculate value and set it.
    public Reservation createReservation(Guest guest, Host host) {
        Reservation reservation = new Reservation();
        LocalDate start = io.readLocalDate("Start Date [MM/dd/yyyy]: ");
        LocalDate end = io.readLocalDate("End Date [MM/dd/yyyy]: ");

        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setGuestId(guest.getId());
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setTotal(reservation.calculateTotal());

        return reservation;
    }

    public String getEmail(String type) {
        String email = io.readRequiredString("Please enter " + type + "'s email: ");
        return email;
    }



    // HELPER METHODS
    // =====================================================================================

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayResult(Result result) {
        if (result.isSuccess()) {
            System.out.println("Operation Successful.");
        } else {
            displayHeader("Errors");
            for (String msg : result.getErrorMessages()) {
                System.out.printf("- %s%n", msg);
                System.out.println("Could not perform operation. Please try again.");
            }
        }
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }
}
