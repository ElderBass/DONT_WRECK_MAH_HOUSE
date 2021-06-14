package seth.mastery.data;

import seth.mastery.models.Guest;

public interface GuestRepository {

    Guest findById(int id);
}
