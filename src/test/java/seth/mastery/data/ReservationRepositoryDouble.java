package seth.mastery.data;

import seth.mastery.models.Reservation;

import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private String hostId = "498604db-b6d6-4599-a503-3d8190fda823"; // may want to change this

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setId(0);
        reservation.
    }

    @Override
    public List<Reservation> findAll(String hostId) {
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) {
        return null;
    }

    @Override
    public boolean update(Reservation reservation) {
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) {
        return false;
    }
}
