package org.nextime.ion.backoffice.tree;

/**
 * <p>
 * Interface for Admin Tree Controller to build plugin components into the tree
 *
 * @author Jazmin Jonson
 * @version
 */
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionServlet;

public interface TreeBuilder {

    public void buildTree(TreeControl treeControl,
            ActionServlet servlet,
            HttpServletRequest request);
}
