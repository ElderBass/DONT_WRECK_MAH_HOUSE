package seth.mastery.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HostFileRepository implements HostRepository {

    private String filePath;
    private String directory;
    private final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    @Autowired
    public HostFileRepository(@Value("${reservationDirectory}") String directory, @Value("${hostFilePath}")String filePath) {
        this.filePath = filePath;
        this.directory = directory;
    }

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

    @Override
    public Host add(Host host) throws DataAccessException {
        List<Host> hosts = findAll();
        host.setId(java.util.UUID.randomUUID().toString());
        hosts.add(host);
        writeAll(hosts);
        return host;
    }

    @Override
    public boolean update(Host host) throws DataAccessException {
        if (host == null) {
            return false;
        }
        List<Host> hosts = findAll();
        for (int i = 0; i < hosts.size(); i++) {
            if (hosts.get(i).getId() == host.getId()) {
                hosts.set(i, host);
                writeAll(hosts);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataAccessException, IOException {
        if (host == null) {
            return false;
        }

        List<Host> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(host.getId())) {
                all.remove(i);
                writeAll(all);
                return deleteHostFile(host.getId());
            }
        }
        return false;
    }

    // HELPER METHODS
    // =========================================================================================================

    private void writeAll(List<Host> hosts) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Host h : hosts) {
                writer.println(serialize(h));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private String serialize(Host h) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                h.getId(),
                h.getLastName(),
                h.getEmail(),
                h.getPhone(),
                h.getAddress(),
                h.getCity(),
                h.getState(),
                h.getPostalCode(),
                h.getStandardRate(),
                h.getWeekendRate());
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
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));

        return host;
    }

    private boolean deleteHostFile(String id) throws IOException {
        boolean didDelete = false;
        try {
            Files.deleteIfExists(Paths.get(directory, id + ".csv"));
        } catch (NoSuchFileException e) {
            System.out.println("No such file/directory exists" + e);
            didDelete = false;
        } catch (IOException e) {
            System.out.println("Invalid permissions.");
            didDelete = false;
            throw new IOException(e);
        }

        System.out.println("Deletion successful.");
        didDelete = true;
        return didDelete;
    }
}
