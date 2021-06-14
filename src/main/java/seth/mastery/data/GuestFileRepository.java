package seth.mastery.data;

import seth.mastery.models.Guest;
import seth.mastery.models.Host;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuestFileRepository implements GuestRepository {

    private String filePath;

    public GuestFileRepository(String filePath) { this.filePath = filePath; }

    @Override
    public List<Guest> findAll() {
        List<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
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
        return null;
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
