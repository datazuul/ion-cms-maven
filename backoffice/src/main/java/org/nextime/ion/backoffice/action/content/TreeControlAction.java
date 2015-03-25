package org.nextime.ion.backoffice.action.content;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.backoffice.action.BaseAction;
import org.nextime.ion.backoffice.tree.TreeControl;
import org.nextime.ion.backoffice.tree.TreeControlNode;

/**
 * Test <code>Action</code> that handles events from the tree control test page.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.1 $ $Date: 2003/04/23 18:55:58 $
 */
public class TreeControlAction extends BaseAction {

    // --------------------------------------------------------- Public Methods
    /**
     * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another web
     * component that will create it). Return an <code>ActionForward</code> instance describing where and how control
     * should be forwarded, or <code>null</code> if the response has already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // check if user is correctly logged
        checkUser(request);

        getServlet().log("Entered TreeControlTestAction:perform()");

        String name = null;
        HttpSession session = request.getSession();
        TreeControl control
                = (TreeControl) session.getAttribute("treeControlTest");

        // Handle a tree expand/contract event
        name = request.getParameter("tree");

        if (name != null) {
            getServlet().log("Tree expand/contract on " + name);

            TreeControlNode node = control.findNode(name);

            if (node != null) {
                getServlet().log("Found Node: " + name);
                node.setExpanded(!node.isExpanded());
            }
        } else {
            getServlet().log("tree param is null");
        }

        // Handle a select item event
        name = request.getParameter("select");
        if (name != null) {
            getServlet().log("Select event on " + name);
            control.selectNode(name);
            control.findNode(name).setExpanded(true);
        }

        // Forward back to the test page
        return (mapping.findForward("view"));

    }

}
