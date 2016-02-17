package exception;


/**
 * Basic exception that will be thrown if an error occurs.
 * 
 * @author wjohnson000
 *
 */
public class NameSystemException extends Exception {

    private static final long serialVersionUID = 1L;

    public NameSystemException(String message) {
        super(message);
    }
}
