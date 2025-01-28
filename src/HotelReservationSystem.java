import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "isdb62";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try(Connection connection = DriverManager.getConnection(URL,USER, PASSWORD);
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(System.in)){

            while (true){
                System.out.println();
                System.out.println("==========HOTEL MANAGEMENT SYSTEM==========");
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Exit");
                System.out.print("Choose your option: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1 -> reservationRoom(connection, scanner);
                    case 2 -> viewReservation(connection, statement);
                    case 3 -> getRoomNumber(connection, statement, scanner);
                    case 4 -> updateReservation(connection, scanner);
                    case 5 -> deleteReservation(connection, scanner);
                    case 6 -> {
                            exit();
                            return;}
                    default -> System.out.println("Invalid choice! Please choose correct option!");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reservationRoom(Connection connection, Scanner scanner){

        try(Statement statement = connection.createStatement()){
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();
            scanner.nextLine();
            System.out.print("Enter Room Number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.nextLine();

            String sqlQuery = "insert into reservations(guest_name, room_number, contact_number)" +
                    "values('"+guestName+"', "+roomNumber+", '"+contactNumber+"')";



                int affectedRow = statement.executeUpdate(sqlQuery);
                if (affectedRow>0){
                    System.out.println("Reservation successfully");
                }else {
                    System.out.println("Reservation failed!!");
                }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    private static void viewReservation(Connection connection, Statement statement){

        String sql = "select reservation_id, guest_name, room_number ,contact_number, reservation_date from reservations";
        try(ResultSet resultSet = statement.executeQuery(sql)){

            System.out.println("=======================Reservation Information=================");
            System.out.println("Reservation ID \tGuest Name\t\tRoom Number\tContact Number\tReservation Date");
            System.out.println("----------------------------------------------------------------");
            while (resultSet.next()){
                int reservation_id = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("%-19d | %-18s | %-15d | %-18s | %-19s", reservation_id, guestName, roomNumber, contactNumber, reservationDate);
                System.out.println("------------------------------------------------------");
            }


        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void getRoomNumber(Connection connection,Statement statement, Scanner scanner){

        try {
            System.out.print("Enter Reservation ID: ");
            int reservationID = scanner.nextInt();
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            String query = "select room_number from reservations " +
                    "where reservation_id = "+reservationID+"and guest_name = '"+guestName+"'";

            try (ResultSet resultSet = statement.executeQuery(query) ){

                if (resultSet.next()){
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number of the reservation id "+reservationID+" and Guest "+guestName+" is: "+roomNumber);
                }else {
                    System.out.println("Reservation not found for the given id and guest name!!");
                }

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        } catch (Exception ignore) {

        }
    }


    private static void updateReservation(Connection connection, Scanner scanner){

        try {
            System.out.println("Enter reservation id to update: ");
            int reservation_id = scanner.nextInt();

            scanner.nextLine();

            if (!reservationExist(connection, reservation_id)){
                System.out.println("Reservaton not found for the given ID");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String contactNumber = scanner.nextLine();

            String sqlQueryUpdate = "update reservations set guest_name = '"+newGuestName+"', room_number = " +
                    roomNumber+","+"contact_number = '"+contactNumber+"', where reservation_id" ;

            try(Statement statement = connection.createStatement()){

                int affectedRow = statement.executeUpdate(sqlQueryUpdate);

                if (affectedRow>0){
                    System.out.println("Updated successfully");
                }else {
                    System.out.println("Update failed!!!");
                }

            }catch (SQLException e){
                e.getMessage();
            }
        } catch (Exception ignore) {

        }
    }


    private static void deleteReservation(Connection connection, Scanner scanner){

        try {
            System.out.println("Enter your reservation ID to delete");
            int reservationID = scanner.nextInt();

            if (!reservationExist(connection, reservationID)){
                System.out.print("Reservation not found for the given ID: ");
                return;
            }

            String sql = "Delete from reservations where reservation_id = "+reservationID;


            try (Statement statement = connection.createStatement()){

                int rowAffected = statement.executeUpdate(sql);

                if (rowAffected>0){
                    System.out.println("Successfully delete");
                }else {
                    System.out.println("Deleting failed.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }catch (Exception ignore){

        }

    }


    private static boolean reservationExist(Connection connection, int reservation_id){

        String query = "select reservation_id from reservations where reservation_id = "+reservation_id;

        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)){

            return resultSet.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i = 5;
        while (i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }

        System.out.println();
        System.out.println("Thank you for using Hotel Management System!!");
    }
}