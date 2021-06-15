package seth.mastery.ui;

import org.springframework.stereotype.Component;

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
}
