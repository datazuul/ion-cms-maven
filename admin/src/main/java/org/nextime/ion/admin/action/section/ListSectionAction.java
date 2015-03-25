package org.nextime.ion.admin.action.section;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public class ListSectionAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // pour situer la vue
        request.setAttribute("view", "section");

        try {
            Mapping.begin();
            Vector v = Section.listAll();
            Mapping.rollback();
            request.setAttribute("sections", v);
        } catch (MappingException e) {
            Mapping.rollback();
            throw new ServletException(e);
        }

        return mapping.findForward("view");
    }

}
