package org.nextime.ion.admin.action.publi;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.admin.form.PubliForm;
import org.nextime.ion.framework.business.Category;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.workflow.Workflow;

public class AddPubliAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // pour situer la vue
        request.setAttribute("view", "publi");

        String id = "";

        try {

            // pour remplir les listes select
            Mapping.begin();
            request.setAttribute("sectionList", Section.listAll());
            request.setAttribute("categoryList", Category.listAll());
            request.setAttribute("workflowList", Workflow.listTypes());
            request.setAttribute("userList", User.listAll());
            request.setAttribute("typeList", TypePublication.listAll());
            Mapping.rollback();

            // si on a pas soumis le formulaire
            if (request.getParameter("addSubmit") == null) {
                //request.setAttribute( "pid", IdGenerator.nextPublicationId());
                return new ActionForward(mapping.getInput());
            }

            // creation de l'objet
            PubliForm f = (PubliForm) form;
            Mapping.begin();
            id = f.getId();
            Publication p = Publication.create(User.getInstance(f.getAuthor()), f.getId(), TypePublication.getInstance(f.getType()), f.getWorkflow());

            String[] g = f.getSections();
            if (g != null) {
                for (int i = 0; i < g.length; i++) {
                    p.addSection(Section.getInstance(g[i]));
                }
            }
            g = f.getCategories();
            if (g != null) {
                for (int i = 0; i < g.length; i++) {
                    p.addCategory(Category.getInstance(g[i]));
                }
            }
            Mapping.commit();

        } catch (Exception e) {
            Mapping.rollback();
            request.setAttribute("error", e.getMessage());
            return new ActionForward(mapping.getInput());
        }

        request.setAttribute("id", id);

        return mapping.findForward("success");
    }

}
