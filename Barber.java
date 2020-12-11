package model;

/**
 * This class represents a Barber
 * @author Jessica Lopes and Thiago Teixeira
 */
public class Barber extends Customer {
    
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Barber(String email, String password, String fName, String lName, String phoneNumber, String location) {
        super(email, password, fName, lName, phoneNumber);
        this.location = location;
    }

    public Barber(String email) {
        super(email);
    }
 
    @Override
    public String toString() {
        return "Customer{" + "email=" + email + ", fName=" + fName + ", lName=" + lName + ", phoneNumber=" + phoneNumber + "location=" + location + '}';

    }
    
}
