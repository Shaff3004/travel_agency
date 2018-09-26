package web.commands;

import exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;


/**
 * Abstract class. Base for inheritance
 * @author Volodin Serhii
 */
public abstract class Command implements Serializable {

    public abstract String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException;
}
