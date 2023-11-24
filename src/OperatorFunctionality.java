
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class helps with system functionalities.
 * It provides methods that work with rooms and customers adding and bookings
 */

public class OperatorFunctionality implements Serializable {
    private final String ROOMFILE = "rooms.txt";
    private final String CUSTOMERFILE = "customers.txt";
    private File roomFile;
    private File customerFile;
    private ArrayList<Customer> customers;
    private ArrayList<Room> rooms;


    public OperatorFunctionality() throws IOException, ClassNotFoundException {
        roomFile = new File(ROOMFILE);
        customerFile = new File(CUSTOMERFILE);
        roomFile.createNewFile();
        customerFile.createNewFile();
        customers = getObjects(CUSTOMERFILE);
        rooms = getObjects(ROOMFILE);
        Room.setIdRep(rooms.size());
        Customer.setIdRep(customers.size());

    }

    /**
     * adds rooms to the system/file
     * @param room - room that should be added
     * @throws IOException when file not found
     */

    public void addRoom(Room room) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(roomFile))) {
            rooms.add(room);
            outputStream.writeObject(rooms);
            System.out.println("The room has been added!");
            System.out.println(rooms);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * adds customers to the system/file
     * @param customer -  customer that should be added
     * @throws IOException when file not found
     */
    public void addCustomer(Customer customer) throws IOException, ClassNotFoundException {
        if (!customerIsInTheList(customer)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(customerFile))) {
                customers.add(customer);
                outputStream.writeObject(customers);
                System.out.println(customers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(customer);
            System.out.println("The customer has been added!");
        } else {
            System.out.println("THIS CUSTOMER IS ALREADY IN THE LIST!");
        }
    }

    /**
     * checks if the customer is already in the system
     * @param customer given customer
     * @return true if customer is in the list, no otherwise
     */
    private boolean customerIsInTheList(Customer customer) {
        return customers.contains(customer);
    }


    /**
     * gets rooms/customers from the file
     * @param fileName the file from which we want to get
     * @return object that was in the file
     * @param <T> room or customer
     * @throws IOException when file not found
     * @throws ClassNotFoundException when we cannot cast the object to T
     */

    public <T> ArrayList<T> getObjects(String fileName) throws IOException, ClassNotFoundException {
            ArrayList<T> items = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            items = (ArrayList<T>) inputStream.readObject();
        } catch (EOFException e) {
            System.out.println( );
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * books the room for given customer
     * @param start - start date
     * @param end - end date
     * @param c - customer who wants to book a room
     * @param r - room that should be booked
     * @return the bill if the room is available for that period of time
     */
    public Bill addRoomBooking(LocalDate start, LocalDate end, Customer c, Room r){
        if(!roomIsBookedForThatPeriod(r,start,end)) {
            c.addRoomRegistrationDate(new Booking(start, end, r));
            r.addRoomBooking(new Booking(start, end, c));
            replaceRoom(r);
            replaceCustomer(c);
            Bill b = new Bill(c, r, start, end);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(roomFile))) {
                outputStream.writeObject(rooms);
                System.out.println(rooms);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(customerFile))) {
                outputStream.writeObject(customers);
                System.out.println(customers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(b);
            System.out.println("The room is booked for that period of time!");
            return b;

        }
        return null;
    }

    /**
     * replaced old room with the same room but with more bookings
     * @param r - same room but with more bookings
     */
    private void replaceRoom(Room r){
        Room r1 = findRoomByID(r.getID());
        rooms.remove(r1);
        rooms.add(r);
    }
    /**
     * replaced old customer with the same customer but with more bookings
     * @param c - same customer but with more bookings
     */
    private void replaceCustomer(Customer c){
            Customer c1 = findCustomerByID(c.getID());
            customers.remove(c1);
            customers.add(c);
    }

    /**
     * gets customers
      * @return customers
     */

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * gets rooms
     * @return rooms
     */

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * saves given rooms history to the given file
     * @param r - room which history we want to save
     * @param path - path to the file in which we want to save the history
     * @throws FileNotFoundException when the file cannot be found
     */
    public void savingSpecificRoomHistory(Room r, File path) throws FileNotFoundException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(r.getRoomBookings());
            System.out.println("Room's report is written in the file: "+ path);
        } catch (IOException e) {
            System.out.println("File not found!");
        }

    }

    /**
     * saves the bill from the booking
     * @param b the bill that should be saved
     * @param path the path to the file in which bill should be saved
     * @throws IOException when file not found
     */
    public void savingBill(Bill b, String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(b);
            System.out.println("Bill has been saved in the file: "+ path);
        }catch (IOException e) {
            System.out.println("File not found!");
        }


    }


    /**
     * gets from the given file rooms with the same ID as the given room is
     * @param path path to the file where we search room information
     * @param roomID given room ID
     * @throws FileNotFoundException if the path of the file is wrong or there is no file with that name
     */
    public void lookingIntoSpecificRoomHistory(File path, int roomID) throws FileNotFoundException {
        ArrayList<?> history;
        Boolean hasSpeceficRoomObject = false;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            history = (ArrayList<?>) inputStream.readObject();
            for (Object obj:
                 history) {
                if((obj instanceof Room room)) {
                    if((room).getID() == roomID) {
                        System.out.println(room);
                        hasSpeceficRoomObject = true;
                    }
                }
            }
            if(!hasSpeceficRoomObject)
                System.out.println("There is no room with specified ID in this file\n");
        } catch (EOFException e) {
            System.out.println("End of file");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File not found!");
        }


    }

    /**
     * finds customer with the given id
     * @param id given id
     * @return customer with the given id
     */
    public Customer findCustomerByID(int id){
        for (Customer customer:
             customers) {
            if(customer.getID() == id)
                return customer;
        }
        return null;
    }

    /**
     * finds room with the given id
     * @param id given id
     * @return room with the given id
     */
    public Room findRoomByID(int id){
        for (Room room:
             rooms) {
            if(room.getID() == id)
                return room;
        }
        return null;
    }

    /**
     * prints room bookings on the screen if there are any
     * @param r given room
     */
    public void roomReport(Room r){
        ArrayList<Booking> bookings = r.getRoomBookings();
        if(bookings.isEmpty()) {
            System.out.println("The room does not have a history of bookings\n");
            return;
        }
        for (Booking b:
                bookings) {
            System.out.println(b);
        }
    }

    /**
     * prints customer bookings on the screen if there are any
     * @param c given room
     */

    public void customerReport(Customer c){
        ArrayList<Booking> bookings = c.getRoomBookingDates();
        if(bookings.isEmpty()){
            System.out.println("The customer does not have a history of bookings\n");
            return;
        }
        for (Booking b:
                bookings) {
            System.out.println(b);
        }    }


    /**
     * checks if the room is booked for that period of time
     * @param r room that we should check
     * @param s starting date of reservation
     * @param e ending date of reservation
     * @return true if the room is booked, false otherwise
     */
    private boolean roomIsBookedForThatPeriod(Room r, LocalDate s, LocalDate e){
        ArrayList<Booking> b = r.getRoomBookings();
        if(b==null)
            return false;
        if(rooms.contains(r))
            for (int i = 0; i < b.size(); i++) {
                if(((b.get(i).getStart().isBefore(e) && b.get(i).getStart().isAfter(s)) || b.get(i).getStart().equals(s))
                        || ((b.get(i).getEnd().isBefore(e) && b.get(i).getEnd().isAfter(s)) || b.get(i).getEnd().equals(e)))
                    return true;
            }
        return false;
    }

}
