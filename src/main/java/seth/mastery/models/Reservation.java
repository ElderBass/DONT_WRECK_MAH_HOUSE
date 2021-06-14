package seth.mastery.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class Reservation {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int guestId;
    private Host host;
    private Guest guest;
    private BigDecimal total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

// TODO come back to this

//    private BigDecimal calculateTotal() {
//        Period period = Period.between(startDate, endDate);
//        BigDecimal daysOfStay = new BigDecimal(period.getDays());
//        return
//
//    }
}
