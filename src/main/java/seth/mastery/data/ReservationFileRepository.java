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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private final String HEADER = "id,start_date,end_date,guest_id,total";
    private String directory;

    private GuestFileRepository guestRepo;
    private HostFileRepository hostRepo;


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
        List<Reservation> sorted = result.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
        return sorted;
    }

    @Override
    public List<Reservation> findAllReservationsByGuest(Guest guest) {
        List<Host> hosts = hostRepo.findAll();
        List<Reservation> guestReservations = new ArrayList<>();

        for (int i = 0; i < hosts.size(); i++) {
            List<Reservation> hostReservations = findAll(hosts.get(i).getId());
            for (int j = 0; j < hostReservations.size(); j++) {
                Guest g = hostReservations.get(j).getGuest();
                if (g.getFirstName().equals(guest.getFirstName()) && g.getLastName().equals(guest.getLastName())
                    && g.getEmail().equals(guest.getEmail()) && g.getId() == guest.getId()) {
                    guestReservations.add(hostReservations.get(j));
                }
            }
        }
        return guestReservations;
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

        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));

        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));

        Guest guest = guestRepo.findById(Integer.parseInt(fields[3]));;
        result.setGuest(guest);

        Host host = hostRepo.findById(hostId);
        result.setHost(host);
        return result;
    }

}
