package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao for barber, all the data from DB for barbers is got from this class
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class DaoBarber {

    // link to the database
    private String dbServer = "jdbc:mysql://52.50.23.197/Jessica_2019197?use_SSL=false";
    // user to query DB and perorm inserts, deletes and updaes
    private String user = "Jessica_2019197";
    // password
    private String password = "2019197";

    // get barber
    public Barber get(String email) throws DBException {

        String query = "SELECT * FROM Barber WHERE email = '" + email + "';";
        String fName, lName, pNumber, pass, location;
        Barber barber = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            if (rs.next()) {

                fName = rs.getString("first_name");
                lName = rs.getString("last_name");
                pNumber = rs.getString("phone_number");
                pass = rs.getString("password");
                location = rs.getString("location");

                barber = new Barber(email, pass, fName, lName, pNumber, location);

            }

            // Close the result set, statement and the connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            String msg = null;
            // Loop through the SQL Exceptions
            while (se != null) {
                msg = se.getSQLState() + " " + "Message: " + se.getMessage() + " " + "Error  : " + se.getErrorCode();
                se = se.getNextException();
                if (se != null) {
                    msg = msg + System.lineSeparator();
                }
            }
            throw new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());

        }

        return barber;

    }

    // save a barber
    public void save(Barber barber) throws DBException {

        String sql = "INSERT INTO Barber (email, password, first_name, last_name, phone_number, location) "
                + "VALUES ('" + barber.getEmail() + "','" + barber.getPassword() + "','" + barber.getfName()
                + "','" + barber.getlName() + "','" + barber.getPhoneNumber() + "','" + barber.getLocation() + "');";

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            // Close the result set, statement and the connection
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            String msg = null;
            // Loop through the SQL Exceptions
            while (se != null) {
                msg = se.getSQLState() + " " + "Message: " + se.getMessage() + " " + "Error  : " + se.getErrorCode();
                se = se.getNextException();
                if (se != null) {
                    msg = msg + System.lineSeparator();
                }
            }
            throw new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }

    }

    // get barber by name it return a list collection
    public List<Barber> getByName(String fName) throws DBException {

        // the query is created by filtering by name and barber type
        String query = "SELECT * FROM Barber WHERE first_name = '" + fName + "';";
        String email, lName, phoneNumber, pass, location, fNameDB;
        List<Barber> barbers = new ArrayList<>();
        Barber barber = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            while (rs.next()) {

                email = rs.getString("email");
                lName = rs.getString("last_name");
                phoneNumber = rs.getString("phone_number");
                pass = rs.getString("password");
                location = rs.getString("location");
                fNameDB = rs.getString("first_name");

                barber = new Barber(email, pass, fNameDB, lName, phoneNumber, location);

                barbers.add(barber);

            }

            // Close the result set, statement and the connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            String msg = null;
            // Loop through the SQL Exceptions
            while (se != null) {
                msg = se.getSQLState() + " " + "Message: " + se.getMessage() + " " + "Error  : " + se.getErrorCode();
                se = se.getNextException();
                if (se != null) {
                    msg = msg + System.lineSeparator();
                }
            }
            throw new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
        return barbers;
    }

    // get barbers by location, it returns a list collection
    public List<Barber> getByLocation(String location) throws DBException {

        // the query is created by filtering by name and barber type
        String query = "SELECT * FROM Barber WHERE location = '" + location + "';";
        String email, lName, phoneNumber, pass, fName, locationDB;
        List<Barber> barbers = new ArrayList<>();
        Barber barber = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            while (rs.next()) {

                email = rs.getString("email");
                lName = rs.getString("last_name");
                phoneNumber = rs.getString("phone_number");
                pass = rs.getString("password");
                fName = rs.getString("first_name");
                locationDB = rs.getString("location");

                barber = new Barber(email, pass, fName, lName, phoneNumber, locationDB);

                barbers.add(barber);

            }

            // Close the result set, statement and the connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            String msg = null;
            // Loop through the SQL Exceptions
            while (se != null) {
                msg = se.getSQLState() + " " + "Message: " + se.getMessage() + " " + "Error  : " + se.getErrorCode();
                se = se.getNextException();
                if (se != null) {
                    msg = msg + System.lineSeparator();
                }
            }
            throw new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
        return barbers;
    }

}
