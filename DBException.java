
package model;

/**
 * This class is used to send errors from database to the controller
 * Database throws an error, this one is caught. Then an instance of
 * this class is created with error message. Then DAO methods throws
 * this exception
 * @author Jessica Lopes and Thiago Teixeira
 */
public class DBException extends Exception {

    /**
     * Creates a new instance of code DBException without detail
     * message.
     */
    public DBException() {
    }

    /**
     * Constructs an instance of code DBException with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DBException(String msg) {
        super(msg);
    }
}
