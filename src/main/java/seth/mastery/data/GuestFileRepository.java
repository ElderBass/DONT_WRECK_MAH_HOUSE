package seth.mastery.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import seth.mastery.models.Guest;
import seth.mastery.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GuestFileRepository implements GuestRepository {

    private String filePath;
    private final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    @Autowired
    public GuestFileRepository(@Value("${guestFilePath}")String filePath) { this.filePath = filePath; }

    @Override
    public List<Guest> findAll() {
        List<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }

        result = result.stream()
                .sorted(Comparator.comparing(Guest::getLastName).thenComparing(Guest::getFirstName))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Guest findById(int id) {
        List<Guest> all = findAll();
        for(Guest g : all) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) {
        List<Guest> all = findAll();
        for (Guest g : all) {
            if (g.getEmail().equals(email)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Guest add(Guest guest) throws DataAccessException {
        List<Guest> guests = findAll();
        guest.setId(getNextId(guests));
        guests.add(guest);
        writeAll(guests);
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataAccessException {
        if (guest == null) {
            return false;
        }
        List<Guest> guests = findAll();
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getId() == guest.getId()) {
                guests.set(i, guest);
                writeAll(guests);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataAccessException {
        if (guest == null) {
            return false;
        }

        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == guest.getId()) {
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    // HELPER METHODS
    // =============================================================================================================

    private void writeAll(List<Guest> guests) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Guest g : guests) {
                writer.println(serialize(g));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private int getNextId(List<Guest> all) {
        int nextId = 0;
        for (Guest g : all) {
            nextId = Math.max(nextId, g.getId());
        }
        return nextId + 1;
    }

    private String serialize(Guest g) {

        return String.format("%s,%s,%s,%s,%s,%s",
                g.getId(),
                g.getFirstName(),
                g.getLastName(),
                g.getEmail(),
                g.getPhone(),
                g.getState());
    }

    private Guest deserialize(String[] fields) {

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);

        return guest;
    }
}
