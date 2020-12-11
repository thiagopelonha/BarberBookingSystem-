
package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Barber;
import model.Customer;
import model.DBException;
import model.DaoBarber;
import model.DaoCustomer;
import view.LoginScreen;

/**
 * Controller class to interact between customer home screen and barber home screen
 * @author Jessica Lopes and Thiago Teixeira
 */
public class LoginController extends MouseAdapter implements ActionListener {
    
    private LoginScreen view;
    private DaoCustomer daoCustomer;
    private DaoBarber daoBarber;

    public LoginController() {
        this.view = new LoginScreen(this);
        this.daoBarber = new DaoBarber();
        this.daoCustomer = new DaoCustomer();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        release();
        RegisterController registerController;
        registerController = new RegisterController();
    }

    // release all the global variables
    public void release() {
        this.view.dispose();
        this.view = null;
        this.daoBarber = null;
        this.daoCustomer = null;          
    }

    // catch method to execute code for event produced by the user
    @Override
    public void actionPerformed(ActionEvent e) {

          // action login
        if (e.getActionCommand().equals("login")) { 
           
           // validation empty fields
           if (!view.getEmail().equals("") && !view.getPassword().equals("")) {
               
                // validate email format
                Pattern VALID_EMAIL_ADDRESS_REGEX = 
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(view.getEmail());
                
                if (!matcher.find()) { // check email formt
                    
                    view.showErrorMsg("Invalid format email"); 
                    return;
                    
                }
                                
                if (view.isCustomer()) { // login customer
                    
                    loginCustomer(view.getEmail(), view.getPassword());
                    
                } else {
                    
                    loginBarber(view.getEmail(), view.getPassword());
                    
                }
                
                
           } else {
               
               view.showErrorMsg("Email and password fields must not be empty");         
               
           }

        }


    }

    // login customer
    private void loginCustomer(String email, String password) {

        Customer customer = null;
        try {
            // check if that customer exist on database
            customer = this.daoCustomer.get(email);
            if (customer == null) {

                view.showErrorMsg("No exist customer with these email and password"); 
                return;

            } 
            // check if password are identical
            if (BCrypt.checkpw(password, customer.getPassword())) {

                view.showSuccessMsg("The user is logged into the system successfully");         
                release();           
                CustomerHomeScreenController customerHomeScreenController;
                customerHomeScreenController = new CustomerHomeScreenController(customer);

            } else {

                view.showErrorMsg("Password is incorrect");         

            } 
            
        } catch (DBException ex) {
            view.showErrorMsg(ex.getMessage());
        }
                        
    }

    // login barber
    private void loginBarber(String email, String password) {

        Barber barber = null;
        try {
            barber = this.daoBarber.get(email);
        } catch (DBException ex) {
            view.showErrorMsg(ex.getMessage());
        }
                
        if (barber == null) {
                    
            view.showErrorMsg("No exist barber with these email and password"); 
            return;
                                
        } 
        // check if password are identical using algorith to encrytp and salt
        if (BCrypt.checkpw(password, barber.getPassword())) {
            
            view.showSuccessMsg("The user is logged into the system successfully");
            release();
            BarberHomeScreenController barberHomeScreenController;
            barberHomeScreenController = new BarberHomeScreenController(barber);

        } else {
            
            view.showErrorMsg("Password is incorrect");
            
        }  

    }
      
}
