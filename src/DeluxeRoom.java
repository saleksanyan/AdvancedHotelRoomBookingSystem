/**
 * this class lets to create types of rooms such as Deluxe Rooms
 */
public class DeluxeRoom extends Room{
    private final int PRICE = 55;
    public DeluxeRoom() {
        super();
    }

    @Override
    public String roomHas() {
        return " a minibar, a bathtub, a king-size bed, and a sitting area";
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override

    public Type getType(){
        return Type.DELUXE;
    }
}
