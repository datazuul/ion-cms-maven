package org.nextime.ion.backoffice.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import org.nextime.ion.backoffice.exception.UserNotLoggedException;

public class BaseAction extends Action {

    protected void checkUser(HttpServletRequest request)
            throws UserNotLoggedException {
        if (request.getSession().getAttribute("userLogin") == null) {
            throw new UserNotLoggedException();
        }
    }

}
