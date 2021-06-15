package seth.mastery.ui;

import org.springframework.stereotype.Component;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

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

    public String getEmail() { return io.readRequiredString("Please enter Host's email: "); }

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

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }
}
