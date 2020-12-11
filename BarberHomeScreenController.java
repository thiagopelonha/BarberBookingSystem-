
package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Appointment;
import model.Barber;
import model.Customer;
import model.DBException;
import model.DaoAppointment;
import model.DaoBarber;
import model.DaoCustomer;
import model.DaoSlot;
import model.Slot;
import view.BarberHomeScreen;

/**
 * This controller was created to interact with DAOs and customer home screen
 * @author Jessica Lopes and Thiago Teixeira
 */
public class BarberHomeScreenController extends MouseAdapter implements ActionListener {
     
    // attributes for the class
    private BarberHomeScreen view;
    private DaoBarber daoBarber;
    private DaoAppointment daoAppointment;
    private DaoSlot daoSlot;
    private Barber barber;
    private List<Slot> slots;
    private List<Slot> slotsFromDB;
    private boolean noHasBeenModifiedRecently;
    private boolean firstTime;
    private DaoCustomer daoCustomer;

    public BarberHomeScreenController(Barber barber) {
        
        this.barber = barber;
        this.daoAppointment = new DaoAppointment();
        this.daoBarber = new DaoBarber();
        this.daoSlot = new DaoSlot();
        this.daoCustomer = new DaoCustomer();
        // there were no modifications to the table
        this.noHasBeenModifiedRecently = true;
        this.slots = new ArrayList<>();
        this.slotsFromDB = new ArrayList<>();
        Object[][] data = null;
        String message = null;
        
        // get the slots for the barber logged into the system
        try {
            data = this.getSlots();
        } catch (DBException ex) { // exception thrown from database
            message = ex.getMessage();
        }
        // create a view screen, the controller and slots are sent to this object
        this.view = new BarberHomeScreen(this, data);  
        // if message was not null, error from database
        if (message != null) {
            view.showErrorMsg(message);
        }
        // if the barber has slots (he/she already load some), barber can not
        // add more slots
        if (data.length > 0) {
            firstTime = false;
            view.disableAddandDeleteSlot();
        } else
            firstTime = true;            
        
    }
    
    // clear all the attributes
    public void release() {
        this.view.dispose();
        this.view = null;    
        this.daoBarber = null;
        this.barber = null;
        this.daoAppointment = null;
        this.daoSlot = null;
        this.slots = null;
        this.slotsFromDB = null;
        this.daoCustomer = null;
    }

    // click on close button on the windows
    @Override
    public void mouseClicked(MouseEvent e) {
        release();
    }

    // catch events produced by the user
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "add":
                this.add(); // add slots
                break;
            case "delete":
                this.delete(); // delete slots
                break;
            case "setAvailable":
                this.setAvailable(); // set available or not
                break;
            case "save":
                this.save(); // save slots to DB
                break;
            case "logout":
                view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING)); // close application
                this.release(); // release attributes
                break;
            case "search":
                search(); // search by dates
                break;
            case "change":
                change(); // update status
                break; 
            case "refresh":
                refresh(); // refresh table
                break;
            default:
                break;
        } 
        
    }
      
    // add new slot
    private void add() {

        LocalTime time = LocalTime.parse(this.view.getTime());
        
        int compare;
        Slot slot;
        
        Iterator<Slot> iterator = slots.iterator();
        
        // we compare slots to add the slot in the correct position
        // slot are represented by time
        if (!slots.isEmpty()) { // barber already loaded at least one slot
        
            int index = 1;
            // loop throught slot list (global variable) to compare
            // slots and add the new one in the correct position
            while (iterator.hasNext()) {

                compare = iterator.next().getTime().compareTo(time);

                if (compare == 0) { // slos was already added, it is not net

                    this.view.showInfoMsg("The slot has already been added");
                    break;

                } if (compare > 0)  { // the new slot, must be added because
                                      // next one is bigger than this one

                    slot = new Slot(this.barber, time, true); // new slot
                    slots.add(index-1,slot); // insert the slot in the local list
                    Object[] row = { this.view.getTime(), true}; // create a row
                    this.view.addSlot(index-1, row); // the row is inserted in the table to show to the user
                    this.noHasBeenModifiedRecently = false; // list slots was modified with a new slot
                    this.view.showSuccessMsg("The new slot has been added"); // show to the user
                    break;

                } if (!iterator.hasNext()) { // add the user to the end of the table
                                            // the new one is bigger than the others

                    slot = new Slot(this.barber, time, true); 
                    slots.add(slot);
                    Object[] row = { this.view.getTime(), true};
                    this.view.addSlot(index, row);
                    this.noHasBeenModifiedRecently = false;
                    this.view.showSuccessMsg("The new slot has been added");
                    break;

                }

                index++;
            }
        } else { // first slot, no slots were added
            
            slot = new Slot(this.barber, time, true);
            slots.add(0,slot);
            Object[] row = { this.view.getTime(), true};
            this.view.addSlot(0, row);
            this.noHasBeenModifiedRecently = false;
            this.view.showSuccessMsg("The new slot has been added");

        }
    }

    // delete slot
    private void delete() {

        String[] slot = null;
        slot = this.view.getSlotSelected();
        
        
        if (slot != null) {
            
            int index = 0;
            LocalTime time;
            
            // we search for the slots to remove it
            for (Slot s: slots) {
                
                time = LocalTime.parse(slot[0]); 
                
                if (time.compareTo(s.getTime()) == 0) {
                    this.noHasBeenModifiedRecently = false;
                    slots.remove(index); // slots is removed from the global variable
                    this.view.removeSlot(); // slots is removed from screen
                    break;
                }
                
                index++;

                
            }

            
        } else { // select a slot from the table to delete
            
            this.view.showInfoMsg("You must select a slot to delete");
            
        }
        

    }

    // set slots available
    private void setAvailable() {

        String[] slot = null;
        slot = this.view.getSlotSelected();
        
        if (slot != null) {
            
            int index = 0;
            LocalTime time;
            // loop through the list to set it as available or not
            for (Slot s: slots) {
                
                time = LocalTime.parse(slot[0]); // convert slot from the screen to localtime
                                                // to compare values
                
                if (time.compareTo(s.getTime()) == 0) {
                    
                    if (s.isAvailable())
                        s.setAvailable(false);
                    else
                        s.setAvailable(true);
                    
                    this.noHasBeenModifiedRecently = false;
                    this.view.setAvailability(s.isAvailable()); // modify screen
                    break;
                }
                index++;
                
            }
            
        } else {
            
            this.view.showInfoMsg("You must select a slot to delete");
            
        }
        
    }

    // save table slots
    private void save() {

        if (!this.noHasBeenModifiedRecently) { // first there must be at leat one modification in the table
            // at the beggining barber adds all the slots
            // then user can set available or not
            
            if (this.firstTime) { // first time user enters slots
                
                try {
                    daoSlot.saveAll(slots); // save all the slots to DB
                    view.showSuccessMsg("The slots were created successfully"); // successfully
                    copyList(); // i copy the current list, I have two list to compare
                                // when the user click on save again
                                // the two tables are compared
                                // one of them is equal to the database
                                // if the are equal, i wont insert in the database
                    this.view.disableAddandDeleteSlot(); // disable on the screen
                                                         // buttons to add and delete
                    firstTime = false; // user already loaded the slots in the DB
                    this.noHasBeenModifiedRecently = true; // after save, this flag
                    // is used to indicate there were not modification after insertion
                } catch (DBException ex) {
                    this.view.showErrorMsg("Error: " + ex.getMessage());
                }
            } else { // after user only can set available or not
                 
               if (!this.areEqual()) { // i compare if the two tables are equal
                   
                   try {
                            // this list contains all the slots to insert in the database
                            List<Slot> slotsAux = new ArrayList<>(); 

                            int index = 0;

                            for (Slot s: slots) {
                                // if the slot in the table which contains the modifications
                                // is different from the slot in the table equal to database
                                // so i have to update that slot in DB
                                if (s.isAvailable() != slotsFromDB.get(index).isAvailable())
                                    slotsAux.add(s);
                                
                                index++;

                            }
                       
                            daoSlot.updateAvailabilitySlots(slotsAux); // update slots calling daoslot
                            view.showSuccessMsg("The slots were modified xuccessfully"); // show message on the screen
                            copyList(); // copy list again because database was modified
                            this.noHasBeenModifiedRecently = true; // again no changes recently
                       } catch (DBException ex) {
                           this.view.showErrorMsg(ex.getMessage());
                       }
                   
               } else {
                   
                   this.view.showInfoMsg("Slots were not modified");
                   
               }
                
            }
           
        } else {
            
            this.view.showInfoMsg("The table has not been modified");
            
        }
    }

    // get slots from database, all the slots for the barber
    private Object[][] getSlots() throws DBException {


        // get Slots
        slots = daoSlot.getAll(barber);
        Object[][] data = new Object[slots.size()][2];
        
        if (slots.isEmpty()) { // barber didnt load slots yet
            firstTime = true;
            return data;
        } else { // get slots 
            copyList(); // copy tables to have same version
            int index = 0;
            for (Slot s: slots) {

                data[index][0] = s.getTime();
                data[index][1] = s.isAvailable();
                
                index++;

            }
            firstTime = false;
            return data;
        }
        
    }
    
    // copy List : copy the table which got from DB in a new one
    // i used the first one to register the changes on the fly
    private void copyList() {
        
        slotsFromDB.clear();
        for (Slot s: slots) {                       
            slotsFromDB.add(new Slot(s.getId(), s.getBarber(), s.getTime(), s.isAvailable()));
        }
        
    }
    
    // compare these two tables are equal or not
    // if these two table are equal there were no modifications
    private boolean areEqual() {
        
        int index = 0;
        Slot slot;
        for (Slot s: slots) {         
            slot = slotsFromDB.get(index);
            if (s.isAvailable() != slot.isAvailable())
                return false;
            index++;
        }        
        return true;
    }

    // search on database by dates
    private void search() {
        // first date from the range is empty
        if (this.view.getDateFrom().equals("")) {
            
            this.view.showErrorMsg("The first date is empty");
            return;
        }
            
        // format date to localdate
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("d-MM-yyyy");
        LocalDate dateFrom = LocalDate.parse(this.view.getDateFrom(), formatterDate);
        // if the second one is empty, only search by the first one
        if (this.view.getDateTo().equals("")) {

            try {
                List<Appointment> apps = daoAppointment.getByBarberAndDates(barber, dateFrom, null);
                buildTable(apps);
            } catch (DBException ex) {
                this.view.showErrorMsg(ex.getMessage());
            }
        // if the second one is not empty search by the second one as well                    
        } else if (!this.view.getDateTo().equals("")) {
                
            LocalDate dateTo = LocalDate.parse(this.view.getDateTo(), formatterDate);
            if (!dateFrom.isBefore(dateTo)) {
            
                view.showErrorMsg("The first is after the second one"); 
                return;
            }
            
            if (Period.between(dateFrom, dateTo).getMonths() >= 1) {
                
                view.showErrorMsg("The difference between the first date and the second one is greater than one month"); 
                return;                
                
            }
            
           try {
                List<Appointment> apps = daoAppointment.getByBarberAndDates(barber, dateFrom, dateTo);
                buildTable(apps);
            } catch (DBException ex) {
                this.view.showErrorMsg(ex.getMessage());
            }
       
        }
    }    
    
    // search appointments for today
    public void searchUpcomingApps() {
        
        refresh();
        
    }

    // build the table to sent to the screen
    // the screen is waiting for a matrix
    // to show on the screen
    private void buildTable(List<Appointment> apps) {

        Object[][] data = new Object[apps.size()][5];
        Customer customer;
            
        int index = 0;
            
        for (Appointment app: apps) {
                            
            try {
                customer = daoCustomer.get(app.getCustomer().getEmail());
            } catch (DBException ex) {
                this.view.showErrorMsg(ex.getMessage());
                return;
            }
                    
            data[index][0] = app.getId();
            data[index][1] = customer.getfName() + " " + customer.getlName();
            data[index][2] = customer.getPhoneNumber();
            data[index][3] = app.getDate().toString() + " - " + app.getTime().toString();
            data[index][4] = app.getStatus();

            index++;
            
        }
            
        this.view.setDataApps(data);
        
    }

    // change status
    private void change() {
        
        switch (this.view.getStatus()) {

            case "cancelled":
                cancelar();
                break;
            case "confirmed":
                confirmed();
                break;
            case "completed":
                completed();
                break;
            default:
                break;
            
        }
    }

    // cancelled by the user
    private void cancelar() {

        String[] app = this.view.getAppSelected();
        // get appointment selected from screen
        if (app == null)
            view.showInfoMsg("A row must be selected to cancel");
        else {  // first validation an appointment cant be cancelled, if it is in the same status
                // or completed
                if (app[4].equals("cancelled"))
                        view.showInfoMsg("This appointment was already cancelled previously");
                else if (app[4].equals("completed"))
                        view.showInfoMsg("This appointment is completed, this one can not be cancelled");
                else {

                    try {
                        // create an appointment
                        Appointment appointment = new Appointment(Integer.parseInt(app[0]));
                        // set status to cancelled
                        appointment.setStatus("cancelled");
                        try {
                            // update status
                            daoAppointment.updateStatusCancelled(appointment);
                        } catch (DBException ex) {
                            view.showErrorMsg(ex.getMessage());
                        }
                        // get status from database to know if the status was modified successfully
                        // because how the screen is not refreshed autmatically
                        // we must check it on the database before update
                        // so then how we can get if the update was modified or not
                        // we get again the value from database
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

    // set status to confirmation, very similar to cancelar
    private void confirmed() {

        String[] app = this.view.getAppSelected();

        if (app == null)
            view.showInfoMsg("A row must be selected to cancel");
        else {            
                if (app[4].equals("confirmed"))
                        view.showInfoMsg("This appointment was already cancelled previously");
                else if (app[4].equals("completed"))
                        view.showInfoMsg("This appointment is completed, this one can not be confirmed");
                else if (app[4].equals("cancelled"))
                        view.showInfoMsg("This appointment was cancelled, this one can not be confirmed");
                else {

                    try {
                        Appointment appointment = new Appointment(Integer.parseInt(app[0]));
                        appointment.setStatus("confirmed");
                        try {
                            daoAppointment.updateStatusConfirmed(appointment);
                        } catch (DBException ex) {
                            view.showErrorMsg(ex.getMessage());
                        }
                        Appointment appointmentResult = daoAppointment.get(appointment);
                        if (appointment.getStatus().equals(appointmentResult.getStatus())) { 
                            view.showSuccessMsg("The appointment was confirmed successfully");
                            view.setStatus("confirmed");
                        } else {
                            view.showSuccessMsg("The appointment was not caconfirmed, its current status prevent from changing");
                            view.setStatus(appointmentResult.getStatus());                                                
                        }
                    } catch (DBException ex) {

                        view.showErrorMsg("Error: " + ex.getMessage());
                    }

                }
        }        
    }

    // set status to completed, very similar to cancelar
    private void completed() {

        String[] app = this.view.getAppSelected();

        if (app == null)
            view.showInfoMsg("A row must be selected to cancel");
        else {            
                if (app[4].equals(""))
                        view.showInfoMsg("This appointment must be confirmed first");
                else if (app[4].equals("completed"))
                        view.showInfoMsg("This appointment was already completed previously");
                else if (app[4].equals("cancelled"))
                        view.showInfoMsg("This appointment was cancelled, this one can not be confirmed");
                else {

                    try {
                        Appointment appointment = new Appointment(Integer.parseInt(app[0]));
                        appointment.setStatus("completed");
                        try {
                            daoAppointment.updateStatusCompleted(appointment);
                        } catch (DBException ex) {
                            view.showErrorMsg(ex.getMessage());
                        }
                        Appointment appointmentResult = daoAppointment.get(appointment);
                        if (appointment.getStatus().equals(appointmentResult.getStatus())) { 
                            view.showSuccessMsg("The appointment was completed successfully");
                            view.setStatus("completed");
                        } else {
                            view.showSuccessMsg("The appointment was not completed, its current status prevent from changing");
                            view.setStatus(appointmentResult.getStatus());                                                
                        }
                    } catch (DBException ex) {

                        view.showErrorMsg("Error: " + ex.getMessage());
                    }

                }
        } 

    }

    // refresh table on the screen, I got appointments for today 
    private void refresh() {
        try {
            List<Appointment> apps = daoAppointment.getByBarberAndDates(barber, LocalDate.now(), null);
            buildTable(apps);
        } catch (DBException ex) {
                this.view.showErrorMsg(ex.getMessage());
        }   
    }

}
