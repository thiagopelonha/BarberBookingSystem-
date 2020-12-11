
package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
import model.Barber;
import model.Customer;
import model.DBException;
import model.DaoAppointment;
import model.DaoBarber;
import model.DaoSlot;
import model.Slot;
import view.CustomerHomeScreen;

/**
 * This class was created to interact with Dao classes and the screen for the customer
 * @author Jessica Lopes and Thiago Teixeira
 */
public class CustomerHomeScreenController  extends MouseAdapter implements ActionListener {
    
    // attributes
    private CustomerHomeScreen view;
    private Customer customer;
    private DaoBarber daoBarber;
    private DaoAppointment daoAppointment;
    private DaoSlot daoSlot;

    // constructor
    public CustomerHomeScreenController(Customer customer) {
        
        this.customer = customer;
        this.daoBarber = new DaoBarber();
        this.daoAppointment = new DaoAppointment();
        this.daoSlot = new DaoSlot();
        Object[][] data = null;
        String message = null;
        // i got appointment to show on the screen
        try {
            data = this.getApp();
        } catch (DBException ex) {
            message = ex.getMessage();
        }
        // create view and i sent data and this object
        this.view = new CustomerHomeScreen(this, data);
        if (message != null)
            this.view.showErrorMsg(message);
    }
    
    // release all the global variables
    public void release() {
        this.view.dispose();
        this.view = null;
        this.daoBarber = null;
        this.customer = null;
        this.daoAppointment = null;
        this.daoSlot = null;
    }

    // click on the close windows
    @Override
    public void mouseClicked(MouseEvent e) {
        release();
    }
    
    // catch method for the screen
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "search":
                this.search(); // search by name or location barbers
                break;
            case "makeAppointment":
                this.makeAppointment(); // make an appointment
                break;
            case "cancelar":
                this.cancelar(); // cancelar
                break;
            case "sendComplain":
                view.sendComplaintScreen(); // send complaint to screen
                break;
            case "logout":
                view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING)); // log out
                this.release(); // release all the parameters
                break;
            case "refresh":
                try {
                    this.refreshStatus(); // refresh table 
                } catch (DBException ex) {
                    this.view.showErrorMsg(ex.getMessage());
                }
                break;
            default:
                break;
        }
        
    }

    // seach by name or location
    private void search() {
                
        if (!view.getfName().equals("")) {
            
            List<Barber> barbers;
            
            try {// call dao to search by name
                barbers = daoBarber.getByName(view.getfName());
                view.setDataBarbers(buildMatrixBarber(barbers));
            } catch (DBException ex) {
                view.showErrorMsg(ex.getMessage());
            }
          
        } else if (!view.getLocation().equals("")) {
            
            List<Barber> barbers;
            
            try { // call dao to search by location
                barbers = daoBarber.getByLocation(view.getLocationText());
                view.setDataBarbers(buildMatrixBarber(barbers));
            } catch (DBException ex) {
                view.showErrorMsg(ex.getMessage());
            }
          
            
        }

    }
    
    // build matrix to send it to the screen
    public Object[][] buildMatrixBarber(List<Barber> barbers) {
        
        Object[][] data = new Object[barbers.size()][4];;
        
        int index = 0;
        for (Barber b: barbers) {
            
            data[index][0] = b.getEmail(); // email
            data[index][1] = b.getfName() + " " + b.getlName(); // name
            data[index][2] = b.getPhoneNumber(); // phone number
            data[index][3] = b.getLocation(); // location

            index++;

        }
        return data;
        
        
    }

    // cancelar appointment set status to cancelled, very similar to cancelar in barber class
    private void cancelar() {

        String[] app = view.getAppSelected();
        // get appointment selected from screen
        if (app == null)
            view.showInfoMsg("A row must be selected to cancel");
        else {
            // validation because we can not set cancelled status if this one is already
            // in cancelled or completed
            if (app[4].equals("cancelled"))
                    view.showInfoMsg("This appointment was already cancelled previously");
            else if (app[4].equals("completed"))
                    view.showInfoMsg("This appointment is completed, this one can not be cancelled");
            else {
                 
                try {
                    Appointment appointment = new Appointment(Integer.parseInt(app[0]));
                    appointment.setStatus("cancelled");
                    try {
                        daoAppointment.updateStatusCancelled(appointment);
                    } catch (DBException ex) {
                        view.showErrorMsg(ex.getMessage());
                    }
                    
                    Appointment appointmentResult = daoAppointment.get(appointment);
                    if (appointment.getStatus().equals(appointmentResult.getStatus())) { 
                        view.showSuccessMsg("The appointment was cancelled successfully");
                        view.setStatus("cancelled");
                    } else {
                        view.showSuccessMsg("The appointment was not cancelled, , its current status prevent from changing");
                        view.setStatus(appointmentResult.getStatus());                                                
                    }
                } catch (DBException ex) {
                    
                    view.showErrorMsg("Error: " + ex.getMessage());
                }
                
            }
                
        }
        
    }
  
    // send complaint to the database
    public void sendComplaint() {
        
        String[] app = view.getAppSelected();
        // get appointment selected on the screen
        if (app == null)
            view.showInfoMsg("A row must be selected to send a complaint");
        else {
            // validations
            if (view.getComplaint().equals(""))
                view.showInfoMsg("Empty message. No message was sent");
            
            if (view.getComplaint().length() > 255) {
                view.showErrorMsg("The message has more than 255 characters");
            }
            try {
                    Appointment appointment = new Appointment(Integer.parseInt(app[0]));
                    String complaint = view.getComplaint();
                    // escape complaint
                    // just in case i has an ' character
                    complaint = complaint.replaceAll("'", "\\\\\\'");
                    appointment.setComplaint(complaint);
                    daoAppointment.updateComplaint(appointment);
                    view.showSuccessMsg("The complaint was sent successfully");
                } catch (DBException ex) { 
                   view.showErrorMsg("The appoinment was not sent" + " - Error: " + ex.getMessage());
                }
                
        }
               
    }

    // make an appointment
    private void makeAppointment() {
        
        if (!view.getDate().equals("") && view.getSlot() != null && !view.getSlot().equals("")) {
                
                // get date
                // format date to localdate
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("d-MM-yyyy");
                LocalDate date = LocalDate.parse(view.getDate(), formatterDate);

                // get slot or time
                // format time
                LocalTime time = LocalTime.parse(view.getSlot());
                
                // get barber
                // Get barber
                Barber barber = getBarber();
                
                // get appointments for a date and the barber 
                List<Appointment> apps;
                try {
                    // get appointments for today
                    apps = this.daoAppointment.getByBarberAndDates(barber, date, null);
                } catch (DBException ex) {
                    view.showErrorMsg(ex.getMessage());
                    return;
                }
                // check if the slot was not modified by the 
                // barber or taken by another user
                if (!isAvailableSlot(time, apps)) {
                
                    // create appointment
                    Appointment app = new Appointment(customer, barber, date, time);
                    int id;
                    try {
                        // result from insertion, i got ID from insertion
                        id = daoAppointment.save(app);
                    } catch (DBException ex) {
                        view.showErrorMsg("The appointment was not created" + " - Error: " + ex.getMessage());
                        return;
                    }
                    view.showSuccessMsg("The apppoinment was created successfully - id " + id);
                                            
                } else {
                    
                   view.showInfoMsg("The slot was taken, please select another one or another date");
   
                }
                        
            } else {
                
                view.showInfoMsg("Date and Slots fields are empty");
                
            }
    }

    // get barber selected from screen
    private Barber getBarber() {

        String[] barber = view.getBarberSelected();
        if (barber != null)
            return new Barber(barber[0]);
        else return null;
    }

    
    // is slot available
    private boolean isAvailableSlot(LocalTime time, List<Appointment> apps) {

        // check if that appointment was not taken
        for(Appointment a: apps) {
            
           if (time.compareTo(a.getTime()) == 0) {
               
               return true;
               
           } 
            
        }
        
        return false;
        
    }
 
    // get all the available slots for a date checking appointments and slots
    public String[] getAvailableSlots() {
        
        List<String> availableSlotAux = new ArrayList<>();        
        
        if (!view.getDate().equals("")) { 
            
            List<Appointment> apps;
            List<Slot> slots;
            // date
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("d-MM-yyyy");
            LocalDate date = LocalDate.parse(view.getDate(), formatterDate);
            // get appointments for that barber and date
            Barber barber = getBarber();
            try {
                apps = daoAppointment.getByBarberAndDates(barber, date, null);
            } catch (DBException ex) {
                view.showErrorMsg(ex.getMessage());
                return null;
            }
            try {
                // get available slots for that barber
                slots = daoSlot.getAllAvailable(barber);
            } catch (DBException ex) {
                view.showErrorMsg(ex.getMessage());
                return null;
            }
            // loop to get available slot for a specific date
            for (Slot slot: slots) {
                
              if (!isAvailableSlot(slot.getTime(), apps)) {
                    
                  availableSlotAux.add(slot.getTime().toString());
                    
               }
                
            }
            
        }                
 
        return availableSlotAux.toArray(new String[0]);           
    }

    // get appointments for a customer to show on the screen
    private Object[][] getApp() throws DBException {
        
        Object[][] data =  null;
        String message = "";
        List<Appointment> apps;
        

        apps = daoAppointment.getByCustomer(this.customer);
        data = new Object[apps.size()][6];
        Barber barber;
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
            
        int index = 0;
            
        for (Appointment app: apps) {
            // only get appointments in a month
            if (app.getDate().isBefore(oneMonthAgo)) {
                    continue;
            }
            
            barber = daoBarber.get(app.getBarber().getEmail());
                    
            
            data[index][0] = app.getId();
            data[index][1] = barber.getfName() + " " + barber.getlName();
            data[index][2] = barber.getPhoneNumber();
            data[index][3] = app.getDate().toString() + " - " + app.getTime().toString();
            data[index][4] = app.getStatus();
            data[index][5] = barber.getLocation();

            index++;
            
        }
            
        return data;

    }

    // refresh status, or update only column status
    // or get new appointments
   private void refreshStatus() throws DBException {
        

        List<Appointment> apps = daoAppointment.getByCustomer(this.customer);
        Barber barber;
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        int countRow = this.view.getLengthRows();
        int index = 0;
        Object[] data = new Object[6];
            
        for (Appointment app: apps) {
                
            if (app.getDate().isBefore(oneMonthAgo)) {
                    continue;
            }
            
            if (countRow <= index) { // it means the user creares a new appointment, it has to be added to the table
           
                barber = daoBarber.get(app.getBarber().getEmail());
           
                data[0] = app.getId();
                data[1] = barber.getfName() + " " + barber.getlName();
                data[2] = barber.getPhoneNumber();
                data[3] = app.getDate().toString() + " - " + app.getTime().toString();
                data[4] = app.getStatus();
                data[5] = barber.getLocation();
                
                this.view.addAppRow(data);
     
            } else {    // only update status    
                this.view.setStatusByRow(index, app.getStatus());
            }    
            index++;
            
        }
           
    }    
    
}
