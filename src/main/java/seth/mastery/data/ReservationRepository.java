package seth.mastery.data;

import seth.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll(String hostId);

    Reservation add(Reservation reservation);

    boolean update(Reservation reservation);

    boolean delete(Reservation reservation);

}
