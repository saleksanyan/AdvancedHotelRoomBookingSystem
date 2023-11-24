
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * This class is for creating customer objects with functionalities
 */
public class Customer implements Serializable {
    private static int idRep;
    private int id;
    private String name;
    private String mail;
    private String phone;
    private final ArrayList<Booking> roomBookingDates;


    public Customer(String name, String mail, String phone) {
        roomBookingDates = new ArrayList<>();
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        id = ++idRep;
    }

    public static void setIdRep(int idRep) {
        Customer.idRep = idRep;
    }

    public String getName() {
        return name;
    }
    public int getID() {
        return id;
    }
    public void addRoomRegistrationDate(Booking info){
        roomBookingDates.add(info);
    }

    public ArrayList<Booking> getRoomBookingDates() {
        return roomBookingDates;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getID() == customer.getID();
    }

    @Override
    public String toString() {
        if(phone != null && mail != null)
            return "ID: "+ id+
                    ", name: " + name +
                    ", mail: " + mail +
                    ", phone: " + phone;
        else if(phone != null)
            return "ID: "+ id+", name: " + name +
                    ", phone: " + phone;
        else
            return "ID: "+ id+", name: " + name+
                    ", mail: " + mail;
    }
}
