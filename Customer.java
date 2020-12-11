package model;

/**
 * This class represents a customer
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class Customer {

    protected String email;
    protected String password;
    protected String fName;
    protected String lName;
    protected String phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Customer(String email, String password, String fName, String lName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
    }

    public Customer(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" + "email=" + email + ", fName=" + fName + ", lName=" + lName + ", phoneNumber=" + phoneNumber + '}';
    }

}
