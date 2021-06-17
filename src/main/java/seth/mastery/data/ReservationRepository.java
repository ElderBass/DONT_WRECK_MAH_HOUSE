package seth.mastery.data;

import seth.mastery.models.Guest;
import seth.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll(String hostId);

    List<Reservation> findAllReservationsByGuest(Guest guest);

    Reservation add(Reservation reservation) throws DataAccessException;

    boolean update(Reservation reservation) throws DataAccessException;

    boolean delete(Reservation reservation) throws DataAccessException;

}
