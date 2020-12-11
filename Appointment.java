
package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents an Appointment
 * @author Jessica Lopes and Thiago Teixeira
 */
public class Appointment {
    // dadas for register
    private int id;
    private Customer customer;
    private Barber barber;
    private String status;
    private String complaint;
    private LocalDate date;
    private LocalTime time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Appointment(int id, Customer customer, Barber barber, String status, String complaint, LocalDate date, LocalTime time) {
        this.id = id;
        this.customer = customer;
        this.barber = barber;
        this.status = status;
        this.complaint = complaint;
        this.date = date;
        this.time = time;
    }

    public Appointment(int id) {
        this.id = id;
    }

    public Appointment(Customer customer, Barber barber, LocalDate date, LocalTime time) {
        this.customer = customer;
        this.barber = barber;
        this.date = date;
        this.time = time;
        this.status = "";
        this.complaint = "";
    }

    

    @Override
    public String toString() {
        return "Appoinment{" + "id=" + id + ", emailCustomer=" + customer + ", emailBarber=" + barber + ", status=" + status + ", complaint=" + complaint + ", date=" + date + ", time=" + time + '}';
    }
    
    
    
    
    
    
}
