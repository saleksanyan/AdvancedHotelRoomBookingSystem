
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * This abstract class is for creating room objects
 */

public abstract class Room implements Serializable {


    enum Type{ SINGLE, DOUBLE, DELUXE}
    private static int idRep;
    private int id;
    private final ArrayList<Booking> roomBookings;


    public Room() {
        roomBookings = new ArrayList<>();
        id = ++idRep;

    }

    public static int getIdRep() {
        return idRep;
    }

    public ArrayList<Booking> getRoomBookings() {
        return roomBookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return room.getID() == this.getID();
    }

    public void addRoomBooking(Booking info){
        roomBookings.add(info);
    }
    public int getID() {
        return id;
    }

    public static void setIdRep(int idRep) {
        Room.idRep = idRep;
    }


    /**
     * gives an information about what the room has
     * @return the information about room
     */
    public abstract String roomHas();

    public abstract int getPrice();

    public abstract Type getType();

    @Override
    public String toString() {
        return "ID: " + id + " Type: "+getType();
    }

}
