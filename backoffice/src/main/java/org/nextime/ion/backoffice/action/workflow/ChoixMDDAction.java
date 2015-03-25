package org.nextime.ion.backoffice.action.workflow;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.mapping.Mapping;

public class ChoixMDDAction extends BaseAction {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // check if user is correctly logged
        checkUser(request);

        // si on a tous les elements
        if (request.getParameter("membreDD") != null) {
            Hashtable inputs = new Hashtable();
            inputs.put("membreDDLogin", request.getParameter("membreDD"));
            request.setAttribute("inputs", inputs);
            request.setAttribute("id", request.getParameter("id"));
            request.setAttribute("version", request.getParameter("version"));
            request.setAttribute("action", request.getParameter("action"));
            return (mapping.findForward("actions"));
        }

        // liste des membres du directoire
        try {
            Mapping.begin();
            request.setAttribute(
                    "users",
                    Group.getInstance("MembresDD").listUsers());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Mapping.rollback();
        }

        // Forward to the next page
        return (mapping.findForward("view"));

    }

}
