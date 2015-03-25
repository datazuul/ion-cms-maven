package org.nextime.ion.admin.action.category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.business.Category;
import org.nextime.ion.framework.mapping.Mapping;

public class DelCategoryAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String id = request.getParameter("id");

        try {
            Mapping.begin();
            Category.getInstance(id).remove();
            Mapping.commit();
        } catch (Exception e) {
            Mapping.rollback();
            throw new ServletException(e);
        }

        return mapping.findForward("success");
    }
}
