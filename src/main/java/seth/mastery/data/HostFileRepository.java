package seth.mastery.data;

import seth.mastery.models.Host;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HostFileRepository implements HostRepository {

    private String filePath;

    public HostFileRepository(String filePath) { this.filePath = filePath; }

    @Override
    public List<Host> findAll() {
        List<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }

        result = result.stream()
                .sorted(Comparator.comparing(Host::getLastName))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Host findById(String id) {
        List<Host> hosts = findAll();
        for(Host h : hosts) {
            if (h.getId().equals(id)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public Host findByEmail(String email) {
        List<Host> all = findAll();
        for (Host h : all) {
            if (h.getEmail().equals(email)) {
                return h;
            }
        }
        return null;
    }

    private Host deserialize(String[] fields) {

        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1]);
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostalCode(Integer.parseInt(fields[7]));
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));

        return host;
    }
}
