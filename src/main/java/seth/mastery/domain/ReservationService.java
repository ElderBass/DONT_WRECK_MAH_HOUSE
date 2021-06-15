package seth.mastery.domain;

import seth.mastery.data.GuestRepository;
import seth.mastery.data.HostRepository;
import seth.mastery.data.ReservationRepository;
import seth.mastery.models.Reservation;

import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final HostRepository hostRepo;
    private final GuestRepository guestRepo;

    public ReservationService(ReservationRepository reservationRepo, HostRepository hostRepo, GuestRepository guestRepo) {
        this.reservationRepo = reservationRepo;
        this.hostRepo = hostRepo;
        this.guestRepo = guestRepo;
    }

    public List<Reservation> findAll(String hostId) {
        return reservationRepo.findAll(hostId);
    }
}
