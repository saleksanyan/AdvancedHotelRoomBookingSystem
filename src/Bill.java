import java.io.Serializable;
import java.time.LocalDate;

/**
 * This class creates objects that keeps information about bills from reservations
 */
public class Bill implements Serializable {

    private Customer customer;
    private Room room;
    private LocalDate start;
    private LocalDate end;
    private double taxes;

    private double serviceFee;
    private double total;

    public Bill(Customer customer, Room room, LocalDate start, LocalDate end) {
        this.customer = customer;
        this.room = room;
        this.start = start;
        this.end = end;
        this.taxes = room.getPrice()*0.2;
        this.serviceFee = room.getPrice()*0.1;
        this.total = room.getPrice()+taxes+serviceFee;
    }

    @Override
    public String toString() {
        return "\n=========================================================="+
                "\n"+"Bill: " +"\n"+
                "Customer: " + customer +"\n"+
                "Room: " + room +"\n"+
                "Start Date:" + start +"\n"+
                "End Date: " + end +"\n"+
                "Taxes: " + taxes +"\n"+
                "Service Fee: " + serviceFee+"\n"+
                "Total: "+ total+"\n"+
                "==========================================================\n";
    }
}
