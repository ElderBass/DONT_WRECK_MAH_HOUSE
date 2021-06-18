package seth.mastery.ui;

import org.springframework.stereotype.Component;
import seth.mastery.domain.Result;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class View {

    private ConsoleIO io;
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

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

        String message = String.format("Select [%s-%s]: ", min, max);
        return MenuOption.fromValue(io.readInt(message, min, max));
    }

    // RESERVATION CRUD METHODS
    // ==============================================================================================

    public void displayReservations(List<Reservation> reservations, Host host, boolean showAll) {
        displayHeader(MenuOption.VIEW_HOST_RESERVATIONS.getMessage());
        System.out.println();
        displayHeader(host.getLastName() + ", " + host.getEmail() + " - " + host.getCity() + ", " + host.getState());
        for (Reservation r : reservations) {
            // Do this if statement to only retrieve reservations in the future
            if (showAll) {
                displayFormattedReservation(r);
            } else {
                if (r.getStartDate().compareTo(LocalDate.now()) > 0) {
                    displayFormattedReservation(r);
                }
            }
        }
        System.out.println();
    }

    // Overload the above method here for displaying Reservations by Guest
    public void displayReservations(List<Reservation> reservations, Guest guest, boolean showAll) {
        displayHeader("Viewing Reservations for " + guest.getFirstName() + " " + guest.getLastName());

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found for " + guest.getFirstName() + " " + guest.getLastName());
            System.out.println();
            return;
        }

        for (Reservation r : reservations) {
            if (showAll) {
                displayFormattedReservation(r);
            } else {
                if (r.getStartDate().compareTo(LocalDate.now()) > 0) {
                    displayFormattedReservation(r);
                }
            }
        }
        System.out.println();
    }

    public Reservation createReservation(Guest guest, Host host) {
        Reservation reservation = new Reservation();
        LocalDate start = io.readLocalDate("Start Date [MM/dd/yyyy]: ", true);
        LocalDate end = io.readLocalDate("End Date [MM/dd/yyyy]: ", true);

        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setGuestId(guest.getId());
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setTotal(reservation.calculateTotal());

        if (!confirmReservationCreationAndEdits(reservation, "Confirm Reservation", "Book this Reservation?")) {
            System.out.println("No problem. Come back any time.");
            return null;
        }
        System.out.println("Reservation " + reservation.getId() + " confirmed.");
        return reservation;
    }

    public Reservation editReservation(List<Reservation> reservations, Host host) {
        Reservation reservation = getReservationSelection(reservations, host);

        LocalDate start = io.readLocalDate("Enter Start Date [" + convertDateFormat(reservation.getStartDate()) + "]: ", false);
        if (start != null) {
            reservation.setStartDate(start);
        }
        LocalDate end = io.readLocalDate("Enter End Date [" + convertDateFormat(reservation.getEndDate()) + "]: ", false);
        if (end != null) {
            reservation.setEndDate(end);
        }
        // Need to recalculate total after new dates have been set
        reservation.setTotal(reservation.calculateTotal());
        if (!confirmReservationCreationAndEdits(reservation, "Confirm Edits", "Save Changes?")) {
            System.out.println("No problem. Come back any time.");
            return null;
        }
        return reservation;
    }

    public Reservation cancelReservation(List<Reservation> reservations, Host host) {
        Reservation reservation = getReservationSelection(reservations, host);
        if (!confirmReservationCancellation(reservation)) {
            System.out.println();
            System.out.println("Reservation still booked.");
            return null;
        }
        return reservation;
    }

    // GUEST CRUD METHODS
    // =======================================================================================

    public Guest createGuest() {
        System.out.println("~Please fill out the following fields~");
        System.out.println();
        String firstName = io.readRequiredString("Enter Guest's first name: ");
        String lastName = io.readRequiredString("Enter Guest's last name: ");
        String email = io.readRequiredString("Enter Guest's email: ");
        String phone = io.readRequiredString("Enter Guest's phone number [e.g. (555) 5555555]: ");
        String state = io.readRequiredString("Enter Guest's state abbreviation [e.g. NY]: ");
        System.out.println();

        Guest guest = new Guest(firstName, lastName, email, phone, state);
        return guest;
    }

    public Guest editGuest(Guest guest) {
        System.out.println("Fill out fields OR press [enter] to keep previous value.");
        System.out.println();
        String firstName = io.readString("First Name [" + guest.getFirstName() + "]: ");
        if (firstName.trim().length() > 0) {
            guest.setFirstName(firstName);
        }
        String lastName = io.readString("Last Name [" + guest.getLastName() + "]: ");
        if (lastName.trim().length() > 0) {
            guest.setLastName(firstName);
        }
        String email = io.readString("Email [" + guest.getEmail() + "]: ");
        if (email.trim().length() > 0) {
            guest.setEmail(email);
        }
        String phone = io.readString("Phone number [" + guest.getPhone() + "]: ");
        if (phone.trim().length() > 0) {
            guest.setPhone(phone);
        }
        String state = io.readString("State [" + guest.getState() + "]: ");
        if (state.trim().length() > 0) {
            guest.setState(state);
        }

        return guest;
    }

    // CONFIRMATION METHODS
    // ==============================================================================================

    private boolean confirmReservationCancellation(Reservation reservation) {
        displayHeader("Confirm Cancellation");
        System.out.println();

        displayHeader(reservation.getHost().getLastName() + ", " + reservation.getHost().getEmail() +
                " - " + reservation.getHost().getCity() + ", " + reservation.getHost().getState());

        displayFormattedReservation(reservation);
        System.out.println();
        return io.readBoolean("Cancel this reservation? [y/n]: ");
    }

    private boolean confirmReservationCreationAndEdits(Reservation reservation, String header, String prompt) {
        displayHeader(header);
        System.out.println("Start Date: " + convertDateFormat(reservation.getStartDate()));
        System.out.println("End Date: " + convertDateFormat(reservation.getEndDate()));
        System.out.println("Total Cost: $" + reservation.calculateTotal());

        return io.readBoolean(prompt + " [y/n]: ");
    }


    // HELPER METHODS
    // =====================================================================================

    public String getEmail(String type) {
        String email = io.readRequiredString("Please enter " + type + "'s email: ");
        return email;
    }

    private Reservation getReservationSelection(List<Reservation> reservations, Host host) {
        displayReservations(reservations, host, false);
        int reservationId = io.readInt("Enter Reservation ID Number: ", 1, reservations.size());

        Reservation reservation = null;
        for (Reservation r : reservations) {
            if (r.getId() == reservationId) {
                reservation = r;
            }
        }
        return reservation;
    }

    private void displayFormattedReservation(Reservation r) {
        System.out.printf("ID: %s | Dates: %s - %s | Guest: %s, %s - %s",
                r.getId(),
                convertDateFormat(r.getStartDate()),
                convertDateFormat(r.getEndDate()),
                r.getGuest().getLastName(),
                r.getGuest().getFirstName(),
                r.getGuest().getEmail());
        System.out.println();
    }

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
            System.out.println();
            System.out.println("Operation Successful :)");
        } else {
            displayHeader("Errors");
            for (String msg : result.getErrorMessages()) {
                System.out.printf("- %s%n", msg);
                System.out.println();
            }
            System.out.println("Could not perform operation. Please try again :(");
        }
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    private String convertDateFormat(LocalDate date) { return dtf.format(date); }

}
