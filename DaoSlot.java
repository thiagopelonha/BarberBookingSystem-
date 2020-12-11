
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DaoSlot gets information from DB for slots
 * @author Jessica Lopes and Thiago Teixeira
 */
public class DaoSlot {
    
 
    // link to the database
    private String dbServer = "jdbc:mysql://52.50.23.197/Jessica_2019197?use_SSL=false";
    // user to query DB and perorm inserts, deletes and updaes
    private String user = "Jessica_2019197";
    // password
    private String password = "2019197";
    
    // get only available slots 
    public List<Slot> getAllAvailable(Barber barber) throws DBException {
        
        LocalTime time;  
        int id;
        boolean available;
        Slot slot;
        List<Slot> slotsResult = null;
       
        
         String query = "SELECT * FROM Slot WHERE email_barber = '" + barber.getEmail() + "';";
        
               
        try {
                    
            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);
            
            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            //
            slotsResult = new ArrayList<>();
            
            // Loop through the result set
            while (rs.next()) {
                
                id = rs.getInt("id_slot");
                time = LocalTime.parse(rs.getString("time_slot"));
                available = rs.getBoolean("available_slot");
                
                if (available) // if available free is true, this record will be returned
                    slotsResult.add(new Slot(id, barber,time, available));
                
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
                if (se != null)
                    msg =  msg + System.lineSeparator();
            }
            throw  new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
        
        return slotsResult;
        
    }
    // get all the slots
    public List<Slot> getAll(Barber barber) throws DBException {
        
        LocalTime time;  
        int id;
        boolean available;
        Slot slot;
        List<Slot> slotsResult = null;
       
        
         String query = "SELECT * FROM Slot WHERE email_barber = '" + barber.getEmail() + "';";
        
               
        try {
                    
            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);
            
            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            //
            slotsResult = new ArrayList<>();
            
            // Loop through the result set
            while (rs.next()) {
                
                id = rs.getInt("id_slot");
                time = LocalTime.parse(rs.getString("time_slot"));
                available = rs.getBoolean("available_slot");
                slotsResult.add(new Slot(id, barber,time, available));
                
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
                if (se != null)
                    msg =  msg + System.lineSeparator();
            }
            throw  new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
        
        return slotsResult;
        
    }
    
    // save a list of slots
    public void saveAll(List<Slot> slots) throws DBException {
        
        java.sql.Time time;   
        String sql = "INSERT INTO Slot (email_barber, time_slot, available_slot) "
                         + "VALUES ('";
        
        int index = 1;
        // create query
        for (Slot slot: slots) {
        
            time = java.sql.Time.valueOf(slot.getTime());
            
            if (slot.isAvailable()) // if slot is available, set 1 (true) to DB
                sql = sql  + slot.getBarber().getEmail() + "','" + time + "','" + 1;
            else // if slots is not available set 0 (false) to database
                sql = sql  + slot.getBarber().getEmail() + "','" + time + "','" + 0;
                            
            if (index == slots.size()) // when it finishes, it gets closed
                sql = sql + "');";
            else // or there are more slots to add
                sql = sql + "'),('";
            
        
            index += 1;
            
        }
                
        try {
                    
            // Get a connection to the database
            Connection conn = DriverManager.getConnection(dbServer, user, password);
            
            // Get a statement from the connection
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            ResultSet rs = stmt.getGeneratedKeys();
            
            index = 0;
            
            while (rs.next()) {
                
                slots.get(index).setId(rs.getInt(1));
                index++;
                
            } 

            // Close the result set, statement and the connection
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            String msg = null;
            // Loop through the SQL Exceptions
            while (se != null) {
                msg = se.getSQLState() + " " + "Message: " + se.getMessage() + " " + "Error  : " + se.getErrorCode();
                se = se.getNextException();
                if (se != null)
                    msg =  msg + System.lineSeparator();
            }
            throw  new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }      
        
    }
    // update available_slot field
    public void updateAvailabilitySlots(List<Slot> slots) throws DBException {
        
        // updates a list collections of slots
        // with this update operation we can update many slots at the same time
        // only one operation        
        String sql = "UPDATE Slot SET available_slot = CASE WHEN available_slot = 1 THEN 0 "
                    + "WHEN available_slot = 0 THEN 1 END WHERE ";
                
        int index = 1;
        // create query
        for (Slot slot: slots) {
                                                
            if (index < slots.size())
                sql = sql + "id_slot = " + slot.getId() + " OR ";
            else
                sql = sql + "id_slot = " + slot.getId() + ";";
            
            index += 1;
            
        }
                
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
                if (se != null)
                    msg =  msg + System.lineSeparator();
            }
            throw  new DBException(msg);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }  
    }
    
}
