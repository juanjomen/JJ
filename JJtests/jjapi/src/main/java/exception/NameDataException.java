package exception;


/**
 * Basic exception that will be thrown if there is a data issue, such as missing
 * fields, names/description too long, etc.
 * 
 * @author wjohnson000
 *
 */
public class NameDataException extends NameSystemException {

    private static final long serialVersionUID = 1L;

    public NameDataException(String message) {
        super(message);
    }
}
