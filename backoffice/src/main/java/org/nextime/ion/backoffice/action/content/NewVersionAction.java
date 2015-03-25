package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.mapping.Mapping;

public class NewVersionAction extends BaseAction {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // check if user is correctly logged
        checkUser(request);

        String id = request.getParameter("id");

        try {
            Mapping.begin();
            Publication.getInstance(id).newVersion(User.getInstance(request.getSession().getAttribute("userLogin") + ""));
            Mapping.commit();
        } catch (Exception e) {
            Mapping.rollback();
            throw new ServletException(e);
        }

        // Forward to the next page
        return (mapping.findForward("ok"));

    }

}
