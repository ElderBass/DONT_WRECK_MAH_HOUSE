package seth.mastery.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
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

// TODO come back to this - it should be working though

    public BigDecimal calculateTotal() {

        BigDecimal weekdays = new BigDecimal(0);
        BigDecimal weekends = new BigDecimal(0);

        for (LocalDate start = startDate; start.isBefore(endDate); start = start.plusDays(1)) {
            if (start.getDayOfWeek().equals(DayOfWeek.MONDAY) || start.getDayOfWeek().equals(DayOfWeek.TUESDAY)
            || start.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) || start.getDayOfWeek().equals(DayOfWeek.THURSDAY)
            || start.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                weekdays = weekdays.add(new BigDecimal(1));
            } else {
                weekends = weekends.add(new BigDecimal(1));
            }
        }

        BigDecimal weekdayCost = host.getStandardRate().multiply(weekdays);
        BigDecimal weekendCost = host.getStandardRate().multiply(weekends);

        return weekdayCost.add(weekendCost);
    }
}
