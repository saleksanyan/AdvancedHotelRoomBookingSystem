
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class gets together all the functionalities and creates the system
 */
public class ConsoleInterface {

    public ConsoleInterface() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String actionNum = getAction(scanner);
        OperatorFunctionality operator = new OperatorFunctionality();

        while(!actionNum.equals("8")) {
            if (actionNum.equals("1")) {
                action1(scanner, operator);

            } else if (actionNum.equals("2")) {
                action2(scanner, operator);

            }else if(!operator.getRooms().isEmpty() && !operator.getCustomers().isEmpty()) {
                if (actionNum.equals("3")) {
                    action3(operator, scanner);
                } else if (actionNum.equals("4")) {
                    Customer c = getCustomer(operator, scanner);
                    operator.customerReport(c);
                } else if (actionNum.equals("5")) {
                    Room r = getRoom(operator, scanner);
                    operator.roomReport(r);
                } else if (actionNum.equals("6")) {
                    File file = getFile("Please enter the name/path " +
                            "of the file in which you want to look into", scanner, true);
                    operator.lookingIntoSpecificRoomHistory(file, getRoom(operator,scanner).getID());
                } else if (actionNum.equals("7")) {
                    Room r = getRoom(operator, scanner);
                    File file = getFile("Please enter the name/path of " +
                            "the file in which you want to save the room info", scanner, false);
                    operator.savingSpecificRoomHistory(r, file);
                }
            }else{
                System.out.println("The datasets of customers or rooms is empty!");
            }
            actionNum = getAction(scanner);
        }
        System.out.println("Bye!!");
    }


    /**
     * asks user what he wants to do in the system
     * @param scanner reads the user answers
     * @return user answer
     */
    private static String getAction(Scanner scanner) {
        String actionNum;
        System.out.println("Please type ONLY the numbers that you can see in " +
                "front of the given action"+"\n"+
                "1 - Add room"+"\n"+"2 - Add customer"+"\n"+"3 - Book a room" +"\n"+
                "4 - Make a report for a specific customer"+"\n"+ "5 - Make a report " +
                "for a specific room"+"\n"+
                "6 - Look into specific room's history via file path"+"\n"+
                "7 - Saving specific room's history via file path"+"\n"+"8 - Exit");
        actionNum = scanner.nextLine();

        while(!(actionNum.equals("1") || actionNum.equals("2") || actionNum.equals("3") ||
                actionNum.equals("4") || actionNum.equals("5") || actionNum.equals("6")
                || actionNum.equals("7") || actionNum.equals("8"))){
            System.out.println("Please choose the answer from 1,2,3,4,5,6,7,8");
            actionNum = scanner.nextLine().trim();
        }
        return actionNum;
    }

    /**
     *
     * @param question waht question we want to ask the user
     * @param scanner reads the user answers
     * @param read - true if we want to read the file, false otherwise
     * @return file with given path
     */
    private static File getFile(String question, Scanner scanner, boolean read) throws IOException {
        System.out.println(question);
        String path = scanner.nextLine();
        File file = new File(path);
        if(!read)
            file.createNewFile();
        return file;
    }

    /**
     * does 3rd action from the list of actions
     * @param operator OperatorFunctionality class object
     */
    private static void action3(OperatorFunctionality operator, Scanner scanner) throws IOException {
        Customer c = getCustomer(operator, scanner);
        Room r = getRoom(operator, scanner);
        System.out.println(r.getRoomBookings());
        System.out.println(c.getRoomBookingDates());
        LocalDate s = dateGetter(scanner, "starting");
        LocalDate e = dateGetter(scanner, "ending");
        while(s.isAfter(e)) {
            System.out.println("Invalid date! Please enter them again!");
            s = dateGetter(scanner, "starting");
            e = dateGetter(scanner, "ending");
        }
        Bill b = operator.addRoomBooking(s, e, c, r);
        if(b!=null){
            r.addRoomBooking(new Booking(s,e,c));
            c.addRoomRegistrationDate(new Booking(s,e,r));
            System.out.println("Do you want to save the bill in a file?(y/n)");
            String answer = scanner.nextLine().trim();
            while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))) {
                System.out.println("Please answer y or n");
                answer = scanner.nextLine().trim();
            }
            if (answer.equalsIgnoreCase("y")) {
                System.out.println(
                        "\nPlease enter the name/path of the file in which you want to save the bill");
                String path = scanner.nextLine().trim();
                operator.savingBill(b, path);
            }
        }
        else System.out.println("That room is not available for that period of time");
    }

    /**
     * creates a room object and returns it
     * @return room object
     */

    private static Room getRoom(OperatorFunctionality operator, Scanner scanner) {
        printRooms(operator);
        System.out.print("Please enter room ID: ");
        String roomID = scanner.nextLine().trim();
        while(!(roomID.matches("\\d+") && Integer.parseInt(roomID)<=Room.getIdRep()
                && Integer.parseInt(roomID) > 0)){
            System.out.print("Please enter valid room ID: ");
            roomID = scanner.nextLine().trim();
        }
        return operator.findRoomByID(Integer.parseInt(roomID));
    }

    /**
     * creates a customer object and returns it
     * @return customer object
     */

    private static Customer getCustomer(OperatorFunctionality operator, Scanner scanner) {
        printCustomers(operator);
        System.out.print("Please enter customer ID: ");
        String customerID = scanner.nextLine().trim();
        while(!(customerID.matches("\\d+") && Integer.parseInt(customerID)<=Room.getIdRep()
                && Integer.parseInt(customerID) > 0)){
            System.out.print("Please enter valid customer ID: ");
            customerID = scanner.nextLine().trim();
        }
        return operator.findCustomerByID(Integer.parseInt(customerID));
    }

    /**
     * does 2nd action from the list of actions
     */

    private static void action2(Scanner scanner, OperatorFunctionality operator) throws IOException, ClassNotFoundException {
        Customer customer;
        String regexMail = "^(.+)@(.+)$";
        String regexPhone = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
        Pattern mailPattern = Pattern.compile(regexMail);
        Pattern phonePattern = Pattern.compile(regexPhone);
        System.out.print("Please enter customer's name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Please enter customer's email(if you do not want to share this info just enter '0')");
        String mail = scanner.nextLine().trim();
        Matcher matcherMail = mailPattern.matcher(mail);
        if (mail.equals("0"))
            System.out.println("Please enter customer's phone number(this is mandatory since you did not enter the mail)");
        else {
            while (!matcherMail.matches()) {
                System.out.println("Please enter valid email address");
                mail = scanner.nextLine().trim();
                matcherMail = mailPattern.matcher(mail);
            }
            System.out.println("Please enter customer's phone number(if you do not want to enter the phone number enter '0')");
        }
        String phone = scanner.nextLine().trim();
        Matcher matcherPhone = phonePattern.matcher(phone);
        if (!mail.equals("0") && phone.equals("0"))
            customer = new Customer(name, mail, null);
        else {
            while (!matcherPhone.matches()) {
                System.out.println("Please enter valid phone number");
                phone = scanner.nextLine().trim();
                matcherPhone = phonePattern.matcher(phone);
            }
            if (!mail.equals("0"))
                customer = new Customer(name, mail, phone);
            else
                customer = new Customer(name, null, phone);

        }
        operator.addCustomer(customer);
    }

    /**
     * print room objects
     */
    private static void printRooms(OperatorFunctionality operator) {
        System.out.println("\n" + "==========================================================");
        for (Room r:
                operator.getRooms()) {
            System.out.println(r);
        }
        System.out.println("==========================================================" + "\n");
    }

    /**
     * prints customer objects
     */
    private static void printCustomers(OperatorFunctionality operator) {
        System.out.println("\n" + "==========================================================");
        for (Customer customer:
                operator.getCustomers()) {
            System.out.println(customer);
        }
        System.out.println("==========================================================" + "\n");
    }

    /**
     * does 1st action from the list of actions
     */
    private static void action1(Scanner scanner, OperatorFunctionality operator) throws IOException {
        System.out.println("What type of room do you want to add?" + "\n" +
                "(Please type ONLY the numbers that you can see in front of the given action)" + "\n" +
                "1 - Single" + "\n" + "2 - Double" + "\n" + "3 - Deluxe");
        String roomType = scanner.nextLine().trim();
        while (!(roomType.equals("1") || roomType.equals("2") || roomType.equals("3"))) {
            System.out.println("Please choose the answer from 1,2,3");
            roomType = scanner.nextLine().trim();
        }
        switch (roomType) {
            case "1":
                operator.addRoom(new SingleRoom());
                break;
            case "2":
                operator.addRoom(new DoubleRoom());
                break;
            case "3":
                operator.addRoom(new DeluxeRoom());
                break;
        }
    }

    /**
     * checks and returns date for booking
     * @param period - specifies if the date is the end or the start
     * @return created date
     */
    private static LocalDate dateGetter(Scanner scanner, String period) {
        System.out.println("This data is for "+period +" date of booking period");
        System.out.print("Please enter the year of your booking: ");
        String year = scanner.nextLine().trim();
        while(year.isEmpty()|| !year.matches("\\d+") || Calendar.getInstance().get(Calendar.YEAR) > Integer.parseInt(year)){
            System.out.print("Please enter valid year of your booking: ");
            year = scanner.nextLine().trim();
        }
        System.out.print("Please enter the month of your booking: ");
        String month = scanner.nextLine().trim();
        while(month.isEmpty()|| !month.matches("\\d+") || (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(year)
                && Integer.parseInt(month) < Calendar.getInstance().get(Calendar.MONTH))
                || !(month.matches("\\d+") && Integer.parseInt(month)<13 &&Integer.parseInt(month)>0)) {
            System.out.print("Please enter a valid month of your booking(1-12): ");
            month = scanner.nextLine().trim();
        }
        System.out.print("Please enter the day of your booking: ");
        String day = scanner.nextLine().trim();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        while(day.isEmpty() || !day.matches("\\d+") || (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(year)
                && Integer.parseInt(month) == (Calendar.getInstance().get(Calendar.MONTH)+1) &&
                Integer.parseInt(day) < Calendar.getInstance().get(Calendar.DATE))
                || !(month.matches("\\d+") && Integer.parseInt(day)<=numDays && Integer.parseInt(day)>0)) {
            System.out.print("Please enter a valid day of your booking: ");
            day = scanner.nextLine().trim();
        }
        return LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
    }
}
