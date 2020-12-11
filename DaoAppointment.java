package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao for Appointment. Data from appointments is got in this class
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class DaoAppointment {

    // link to the database
    private String dbServer = "jdbc:mysql://52.50.23.197/Jessica_2019197?use_SSL=false";
    // user to query DB and perorm inserts, deletes and updaes
    private String user = "Jessica_2019197";
    // password
    private String password = "2019197";

    //save an appointment
    public int save(Appointment app) throws DBException {

        java.sql.Date date = java.sql.Date.valueOf(app.getDate());
        java.sql.Time time = java.sql.Time.valueOf(app.getTime());

        String sql = "INSERT INTO Appointment (email_customer, email_barber, status, complaint, date_appointment, time_appointment) "
                + "VALUES ('" + app.getCustomer().getEmail() + "','" + app.getBarber().getEmail()
                + "','" + app.getStatus() + "','" + app.getComplaint() + "','" + date
                + "','" + time + "')";

        int id = 0;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();

            rs.next();

            id = rs.getInt(1);

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
            throw new DBException(msg); // custom exception is created with the message
        } catch (Exception e) {
            throw new DBException(e.getMessage()); // custom exception is created with the message
        }

        return id;

    }

    // get an appointment
    public Appointment get(Appointment app) throws DBException {

        String query = "SELECT * FROM Appointment WHERE id_appointment = '" + app.getId() + "';";
        String emailBarber, emailCustomer, status, complaint;
        int id;
        LocalTime time;
        LocalDate date;
        Barber barber;
        Customer customer;
        Appointment appResult = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            if (rs.next()) {

                emailBarber = rs.getString("email_barber");
                emailCustomer = rs.getString("email_customer");
                status = rs.getString("status");
                complaint = rs.getString("complaint");
                time = LocalTime.parse(rs.getString("time_appointment"));
                date = rs.getDate("date_appointment").toLocalDate();
                id = rs.getInt("id_appointment");

                barber = new Barber(emailBarber);
                customer = new Customer(emailCustomer);

                appResult = new Appointment(id, customer, barber, status, complaint, date, time);

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

        return appResult;

    }

    // update status to cancelled
    public void updateStatusCancelled(Appointment app) throws DBException {

        // this sql is created because the system double check is the status was not 
        // updated by the user who created this appoitment
        String sql = "UPDATE  Appointment SET status = IF(status <> 'cancelled' AND status <> 'completed', '"
                + app.getStatus() + "', status) WHERE id_appointment =" + app.getId() + ";";

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

    // update status to confirmed
    public void updateStatusConfirmed(Appointment app) throws DBException {

        // this sql is created because the system double check is the status was not 
        // updated by the user who created this appoitment        
        String sql = "UPDATE  Appointment SET status = IF(status = '', '"
                + app.getStatus() + "', status) WHERE id_appointment =" + app.getId() + ";";

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

    // update status to completed
    public void updateStatusCompleted(Appointment app) throws DBException {

        // this sql is created because the system double check is the status was not 
        // updated by the user who created this appoitment        
        String sql = "UPDATE  Appointment SET status = IF(status = 'confirmed', '"
                + app.getStatus() + "', status) WHERE id_appointment =" + app.getId() + ";";

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

    //update complaint in an appointment
    public void updateComplaint(Appointment app) throws DBException {

        // this sql is created because the system double check is the status was not 
        // updated by the user who created this appoitment        
        String sql = "UPDATE Appointment SET complaint = '" + app.getComplaint() + "' WHERE id_appointment = "
                + app.getId();

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

    // get appoinments from a customer
    public List<Appointment> getByCustomer(Customer customer) throws DBException {

        String query = "SELECT * FROM Appointment WHERE email_customer = '" + customer.getEmail() + "';";
        String emailBarber, status, complaint;
        int id;
        LocalTime time;
        LocalDate date;
        Barber barber;
        List<Appointment> apps = new ArrayList<>();
        Appointment app = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            while (rs.next()) {

                emailBarber = rs.getString("email_barber");
                status = rs.getString("status");
                complaint = rs.getString("complaint");
                time = LocalTime.parse(rs.getString("time_appointment"));
                date = rs.getDate("date_appointment").toLocalDate();
                id = rs.getInt("id_appointment");

                barber = new Barber(emailBarber);

                app = new Appointment(id, customer, barber, status, complaint, date, time);
                apps.add(app);

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

        return apps;

    }

    // get appointment by Barber and a range of date, the second date also can be empty. 
    // so if the second date is empty, only searcb by the first one
    public List<Appointment> getByBarberAndDates(Barber barber, LocalDate dateFrom, LocalDate dateTo) throws DBException {

        java.sql.Date dateAux1 = java.sql.Date.valueOf(dateFrom);
        java.sql.Date dateAux2 = null;
        if (dateTo != null) {
            dateAux2 = java.sql.Date.valueOf(dateTo);
        }
        String query = null;
        if (dateTo != null) {
            query = "SELECT * FROM Appointment WHERE email_barber = '" + barber.getEmail()
                    + "' AND date_appointment BETWEEN '" + dateAux1 + "' AND '" + dateAux2 + "';"; // ORDER BY date_appointment ASC, time_appointment ASC;";
        } else {
            query = "SELECT * FROM Appointment WHERE email_barber = '" + barber.getEmail()
                    + "' AND date_appointment = '" + dateAux1 + "';"; //ORDER BY date_appointment ASC, time_appointment ASC;";
        }
        String emailCustomer, status, complaint;
        int id;
        LocalTime time;
        LocalDate date;
        Customer customer;
        List<Appointment> apps = new ArrayList<>();
        Appointment app = null;

        try {

            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);

            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set
            while (rs.next()) {

                emailCustomer = rs.getString("email_customer");
                status = rs.getString("status");
                complaint = rs.getString("complaint");
                time = LocalTime.parse(rs.getString("time_appointment"));
                date = rs.getDate("date_appointment").toLocalDate();
                id = rs.getInt("id_appointment");

                customer = new Customer(emailCustomer);

                app = new Appointment(id, customer, barber, status, complaint, date, time);
                apps.add(app);

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

        return apps;

    }

}
