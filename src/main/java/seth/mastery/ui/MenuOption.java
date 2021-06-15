package seth.mastery.ui;

public enum MenuOption {

    EXIT(0, "Exit Program"),
    VIEW_HOST_RESERVATIONS(1, "View Reservations for a Host"),
    MAKE_RESERVATION(2, "Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_RESERVATION(4, "Cancel a Reservation");

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
