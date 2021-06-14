package seth.mastery.data;

import seth.mastery.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findById(int id);

    Guest findByEmail(String email);
}
