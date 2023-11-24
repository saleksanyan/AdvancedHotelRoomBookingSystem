/**
 * this class lets to create types of rooms such as Double Rooms
 */
public class DoubleRoom extends Room{
    private final int PRICE = 35;
    public DoubleRoom() {
        super();
    }

    @Override
    public String roomHas() {
        return " a double bed, bathroom, TV, and closet";
    }
    @Override
    public int getPrice() {
        return  PRICE;
    }

    @Override

    public Type getType(){
        return Type.DOUBLE;
    }
}
