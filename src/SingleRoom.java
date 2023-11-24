/**
 * this class lets to create types of rooms such as Single Rooms
 */
public class SingleRoom extends Room{

    private final int PRICE = 20;
    public SingleRoom() {
        super();
    }

    @Override
    public String roomHas() {
        return " a single bed, bathroom, TV, and closet";
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override

    public Type getType(){
        return Type.SINGLE;
    }
}
