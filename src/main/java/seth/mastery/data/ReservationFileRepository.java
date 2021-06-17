package seth.mastery.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import seth.mastery.models.Guest;
import seth.mastery.models.Host;
import seth.mastery.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private final String HEADER = "id,start_date,end_date,guest_id,total";
    private String directory;

    private final String GUEST_PATH = "./data/guests.csv";
    private final String HOST_PATH = "./data/hosts.csv";

    private GuestFileRepository guestRepo;
    private HostFileRepository hostRepo;

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final String INVALID_DATE
            = "[INVALID] Enter a date in MM/dd/yyyy format.";

    @Autowired
    public ReservationFileRepository(@Value("${reservationDirectory}")String directory, GuestFileRepository guestFileRepository, HostFileRepository hostFileRepository) {
        this.directory = directory;
        this.guestRepo = guestFileRepository;
        this.hostRepo = hostFileRepository;
    }

    @Override
    public List<Reservation> findAll(String hostId) {
        List<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, hostId));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataAccessException {
        List<Reservation> reservations = findAll(reservation.getHost().getId());
        reservation.setId(getNextId(reservations));
        reservations.add(reservation);
        writeAll(reservations, reservation.getHost().getId());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataAccessException {
        if (reservation == null || reservation.getHost() == null) {
            return false;
        }
        List<Reservation> reservations = findAll(reservation.getHost().getId());
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservation.getId()) {
                reservations.set(i, reservation);
                writeAll(reservations, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataAccessException {
        if (reservation == null || reservation.getHost() == null) {
            return false;
        }
        String hostId = reservation.getHost().getId();
        List<Reservation> all = findAll(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.remove(i);
                writeAll(all, hostId);
                return true;
            }
        }
        return false;
    }


    // HELPER METHODS
    // ===========================================================================================

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private int getNextId(List<Reservation> all) {
        int nextId = 0;
        for (Reservation r : all) {
            nextId = Math.max(nextId, r.getId());
        }
        return nextId + 1;
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);
            for (Reservation r : reservations) {
                writer.println(serialize(r));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private String serialize(Reservation reservation) {

        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields, String hostId) {

        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));

        result.setStartDate(convertDateFormat(fields[1]));
        result.setEndDate(convertDateFormat(fields[2]));

        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));

        Guest guest = guestRepo.findById(Integer.parseInt(fields[3]));;
        result.setGuest(guest);

        Host host = hostRepo.findById(hostId);
        result.setHost(host);
        return result;
    }

    // TODO figure out why this isn't working...
    private LocalDate convertDateFormat(String date) {
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldDate = LocalDate.parse(date, originalFormat);
        String newDate = dtf.format(oldDate);
        // this string newDate is indeed in the correct format of MM/dd/yyyy
        // but this LocalDate.parse does not convert the string into the dtf format and reverts to yyyy-MM-dd
        return LocalDate.parse(newDate, dtf);

    }
}
