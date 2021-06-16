package seth.mastery.data;

import seth.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private String hostId = "498604db-b6d6-4599-a503-3d8190fda823"; // may want to change this

    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST1);
        reservation.setGuestId(GuestRepositoryDouble.GUEST1.getId());
        reservation.setStartDate(LocalDate.of(2021, 11, 1));
        reservation.setEndDate(LocalDate.of(2021, 11, 8));
        reservation.setTotal(reservation.calculateTotal());
        reservations.add(reservation);

        Reservation newReservation = new Reservation();
        newReservation.setId(2);
        newReservation.setHost(HostRepositoryDouble.HOST);
        newReservation.setGuest(GuestRepositoryDouble.GUEST2);
        newReservation.setGuestId(GuestRepositoryDouble.GUEST2.getId());
        newReservation.setStartDate(LocalDate.of(2021, 9, 9));
        newReservation.setEndDate(LocalDate.of(2021, 9, 15));
        reservations.add(newReservation);

    }

    @Override
    public List<Reservation> findAll(String hostId) {
        return reservations;
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
