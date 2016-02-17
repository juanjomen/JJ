package exception;

/**
 * Exception that will be thrown if a a delete cannot be performed because there are dependent instances still in
 * existance or a create cannot be performed because the instance with that key already exists
 *
 * Created by dwgriffith on 10/12/2015.
 */
public class NameConflictException extends NameSystemException {

  private static final long serialVersionUID = 1L;

  public NameConflictException(String message) {super(message);}
}
