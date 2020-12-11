
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
import view.RegisterScreen;

/**
 * Controller for the register screen
 * @author Jessica Lopes and Thiago Teixeira
 */
public class RegisterController extends MouseAdapter implements ActionListener {
    
    private RegisterScreen view;
    private DaoCustomer daoCustomer;
    private DaoBarber daoBarber;

    
    
    public RegisterController() {
        this.view = new RegisterScreen(this);
        this.daoBarber = new DaoBarber();
        this.daoCustomer = new DaoCustomer();    
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        release();
        LoginController loginController;
        loginController = new LoginController();
    }
    
    public void release() {
        this.view.dispose();
        this.view = null;
        this.daoBarber = null;
        this.daoCustomer = null;          
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equals("register")) {
            
            // check fields for null
            if (!view.getEmail().equals("")       && 
               !view.getfName().equals("")        && 
               !view.getsName().equals("")        &&
               !view.getPassword().equals("")     &&
               !view.getConfPassword().equals("") &&
               !view.getPhoneNumber().equals("")) {
                
                // if the user is a barber, check field location for null
                if (view.getLocationText().equals("") && (view.isBarber())) {
                    
                        view.showErrorMsg("Every field is mandatory, they must be filled");
                        return;
                }
                    
                // validations on email and passwords
                if (areValid(view.getEmail(), view.getPassword(), view.getConfPassword())) {

                    if (view.isCustomer()) {
                        // register cutomer
                        registerCustomer(new Customer(view.getEmail(), encryptPassword(view.getPassword()), 
                                                       view.getfName(), view.getsName(), view.getPhoneNumber()));

                    } else {

                        registerBarber(new Barber(view.getEmail(), encryptPassword(view.getPassword()), 
                                                       view.getfName(), view.getsName(), view.getPhoneNumber(),
                                                       view.getLocationText()));
                    }

                } 
                
            } else {
                
               view.showErrorMsg("Every field is mandatory, they must be filled");
                
            }
        }        
        
    }
    
    private boolean areValid(String email, String password, String confPassword) {
        
        // validate email format
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(view.getEmail());
                
        if (!matcher.find()) { // invalid format
                    
            view.showErrorMsg("Invalid format email"); 
            return false;
                    
        }
        
        // following validations were got from https://java2blog.com/validate-password-java/
        
        if (password.length() > 15 || password.length() < 8) {
            view.showErrorMsg("Password must be less than 15 and more than 8 characters in length.");
            return false;
        }
        
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars )) {
            view.showErrorMsg("Password must have atleast one uppercase character");
            return false;
        }
        
        String specialChars = "(.*[@,#,$,%].*$)";
        if (!password.matches(specialChars )) {
            view.showErrorMsg("Password must have atleast one special character among @#$%");
            return false;
        }
        
        //////////////////////////////////////////////////////////////////////////////////
        
        if (!password.equals(confPassword)) {
            
            view.showErrorMsg("Passwords do not match");
            return false;            
            
        }
        
        return true;
           
    }
    // encrypt password using BCrypt
    private String encryptPassword(String password) {
        
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
        
    }
    
    private void registerCustomer(Customer customer) {
        
        try {// i use dao to register the user on DB
            daoCustomer.save(customer);
            view.showSuccessMsg("The customer was registered successflly");
            release();
            LoginController loginController;
            loginController = new LoginController();
        } catch (DBException ex) {
            view.showErrorMsg(ex.getMessage());
        }
        
    }
    
    private void registerBarber(Barber barber) {
        
        try { // i use dao to register barber on database
            daoBarber.save(barber);
            view.showSuccessMsg("The barber was registered successflly");
            release();
            LoginController loginController;
            loginController = new LoginController();            
        } catch (DBException ex) {
            view.showErrorMsg(ex.getMessage());
        }
        
    }
    
}
