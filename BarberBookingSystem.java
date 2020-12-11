
package barberbookingsystem;

import Controller.LoginController;
import model.DBException;

/**
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class BarberBookingSystem {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DBException {
        // TODO code application logic here
       new LoginController();        
    }
    

    
}