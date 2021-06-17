package seth.mastery.ui;

public enum MenuOption {

    EXIT(0, "Exit Program"),
    VIEW_HOST_RESERVATIONS(1, "View Reservations for a Host"),
    VIEW_GUEST_RESERVATIONS(2, "View Reservations for a Guest"),
    MAKE_RESERVATION(3, "Make a Reservation"),
    EDIT_RESERVATION(4, "Edit a Reservation"),
    CANCEL_RESERVATION(5, "Cancel a Reservation");

    private int value;
    private String message;

    MenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static MenuOption fromValue(int value) {
        for (MenuOption option : MenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
