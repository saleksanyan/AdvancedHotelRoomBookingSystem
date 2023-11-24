import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * This class creates objects that keeps information about bookings
 */
public class Booking implements Serializable {
    private LocalDate start;
    private LocalDate end;
    private Room room;

    private Customer customer;

    public Booking(LocalDate start, LocalDate end, Room room) {
        this.start = start;
        this.end = end;
        this.room = room;

    }

    public Booking(LocalDate start, LocalDate end, Customer customer) {
        this.start = start;
        this.end = end;
        this.customer = customer;
    }

    public LocalDate getStart() {
        return start;
    }

    public Customer getCustomer() {
        if(customer != null)
            return customer;
        return null;
    }

    public LocalDate getEnd() {
        return end;
    }

    public Room getRoom() {
        if(room!= null)
            return room;
        return null;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    @Override
    public String toString() {
        if(room != null)
            return "\n=========================================================="+ "\n"+
                    "Booking details" +"\n"+
                    "Start Date: " + start +"\n"+
                    "End Date: " + end +"\n"+
                    "Room: " + room+ "\n"+
                    "=========================================================="+ "\n";
        else
            return "\n=========================================================="+ "\n"+
                    "Booking:" +"\n"+
                    "Start Date: " + start +"\n"+
                    "End Date: " + end +"\n"+
                    "Customer: " + customer+ "\n"+
                    "=========================================================="+ "\n";

    }
}
