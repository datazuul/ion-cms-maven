package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.mapping.Mapping;

public class MyWorkAction extends BaseAction {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // check if user is correctly logged
        checkUser(request);

        // fill data
        try {
            Mapping.begin();

        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            Mapping.rollback();
        }

        // Forward to the next page
        return (mapping.findForward("view"));

    }

}
