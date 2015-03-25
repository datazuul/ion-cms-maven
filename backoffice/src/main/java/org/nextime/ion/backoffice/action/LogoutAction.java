package org.nextime.ion.backoffice.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        request.getSession().removeAttribute("userLogin");
        request.getSession().removeAttribute("treeControlNode");
        request.getSession().removeAttribute("pageInfos");

        // Forward to the next page
        return (mapping.findForward("ok"));

    }

}
