package seth.mastery.data;

import seth.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    public final static Guest GUEST1 = makeGuest1();

    public final static Guest GUEST2 = makeGuest2();

    private final List<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {

        guests.add(GUEST1);
        guests.add(GUEST2);
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findById(int id) {
        return null;
    }

    @Override
    public Guest findByEmail(String email) {
        return null;
    }

    private static Guest makeGuest1() {
        Guest guest = new Guest();
        guest.setId(1001);
        guest.setFirstName("Charlton");
        guest.setLastName("Teston");
        guest.setEmail("chuckT@mail.com");
        guest.setPhone("(420) 6969");
        guest.setState("TX");

        return guest;
    }

    private static Guest makeGuest2() {
        Guest guest = new Guest();
        guest.setId(1002);
        guest.setFirstName("Sir Charles");
        guest.setLastName("Tendieman");
        guest.setEmail("tendies@mail.com");
        guest.setPhone("(420) 6996");
        guest.setState("FL");

        return guest;
    }
}
