package exception;


/**
 * class exception of DB work
 * @author Serhii Volodin
 */
public class DBException extends ApplicationException {
    public DBException(){
        super();
    }

    public DBException(String message, Throwable cause){
        super(message, cause);
    }

    public DBException(String message){
        super(message);
    }
}
