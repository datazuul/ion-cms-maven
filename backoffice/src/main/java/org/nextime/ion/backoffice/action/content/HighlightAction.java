package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;
import org.nextime.ion.commons.PublicationSorter;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.mapping.Mapping;

public class HighlightAction extends BaseAction {

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

            Publication p = Publication.getInstance(id);
            Section section = (Section) p.listSections().firstElement();

            // faire un bel arbre
            TreeControl control
                    = (TreeControl) request.getSession().getAttribute(
                            "treeControlTest");
            collapse(control.getRoot());

            control.selectNode(section.getId());

            // expand all
            while (section != null) {
                TreeControlNode node = control.findNode(section.getId());
                node.setExpanded(true);
                section = section.getParent();
            }

            // trouve la page
            section = (Section) p.listSections().firstElement();
            Vector ps = PublicationSorter.sortPublications(section);
            int pos = ps.indexOf(p);
            int page = pos / ViewSectionAction.pageSize;
            ((Hashtable) (request.getSession().getAttribute("pageInfos"))).put(
                    section.getId(),
                    new Integer(page * ViewSectionAction.pageSize));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Mapping.rollback();
        }

        request.getSession().setAttribute("highlightId", id);

        // Forward to the next page
        return (mapping.findForward("ok"));

    }

    private void collapse(TreeControlNode n) {
        for (int i = 0; i < n.findChildren().length; i++) {
            n.findChildren()[i].setExpanded(false);
            collapse(n.findChildren()[i]);
        }
    }

}
