package org.nextime.ion.backoffice.tree;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 * An individual node of a tree control represented by an instance of <code>TreeControl</code>, and rendered by an
 * instance of <code>TreeControlTag</code>.</p>
 *
 * @author Jazmin Jonson
 * @author Craig R. McClanahan
 * @version $Revision: 1.1 $ $Date: 2003/04/23 18:55:01 $
 */
public class TreeControlNode implements Serializable {

    // ----------------------------------------------------------- Constructors
    /**
     * Construct a new TreeControlNode with the specified parameters.
     *
     * @param name Internal name of this node (must be unique within the entire tree)
     * @param icon Pathname of the image file for the icon to be displayed when this node is visible, relative to the
     * image directory for our images
     * @param label The label that will be displayed to the user if this node is visible
     * @param action The hyperlink to be selected if the user selects this node, or <code>null</code> if this node's
     * label should not be a hyperlink
     * @param target The window target in which the <code>action</code> hyperlink's results will be displayed, or
     * <code>null</code> for the current window
     * @param expanded Should this node be expanded?
     */
    public TreeControlNode(
            String name,
            String icon,
            String label,
            String action,
            String target,
            boolean expanded) {

        super();
        this.name = name;
        this.icon = icon;
        this.label = label;
        this.action = action;
        this.target = target;
        this.expanded = expanded;

    }

    // ----------------------------------------------------- Instance Variables
    /**
     * The set of child <code>TreeControlNodes</code> for this node, in the order that they should be displayed.
     */
    protected ArrayList children = new ArrayList();

    // ------------------------------------------------------------- Properties
    /**
     * The hyperlink to which control will be directed if this node is selected by the user.
     */
    protected String action = null;

    public String getAction() {
        return (this.action);
    }

    /**
     * Is this node currently expanded?
     */
    protected boolean expanded = false;

    public boolean isExpanded() {
        return (this.expanded);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * The pathname to the icon file displayed when this node is visible, relative to the image directory for our
     * images.
     */
    protected String icon = null;

    public String getIcon() {
        return (this.icon);
    }

    /**
     * The label that will be displayed when this node is visible.
     */
    protected String label = null;

    public String getLabel() {
        return (this.label);
    }

    /**
     * Is this the last node in the set of children for our parent node?
     */
    protected boolean last = false;

    public boolean isLast() {
        return (this.last);
    }

    void setLast(boolean last) {
        this.last = last;
    }

    /**
     * Is this a "leaf" node (i.e. one with no children)?
     */
    public boolean isLeaf() {
        synchronized (children) {
            return (children.size() < 1);
        }
    }

    public void up() {
        TreeControlNode parent = getParent();
        ArrayList brothers = parent.children;
        int myIndex = brothers.indexOf(this);
        if (myIndex < brothers.size() - 1) {
            TreeControlNode nextOne
                    = (TreeControlNode) brothers.get(myIndex + 1);
            brothers.set(myIndex, nextOne);
            brothers.set(myIndex + 1, this);
        }
        rebuildLast(brothers);
    }

    protected void rebuildLast(ArrayList nodes) {
        for (int i = 1; i <= nodes.size(); i++) {
            TreeControlNode node = (TreeControlNode) nodes.get(i - 1);
            if (i == nodes.size()) {
                node.setLast(true);
            } else {
                node.setLast(false);
            }
        }
    }

    public void rebuildLast() {
        ArrayList brothers = parent.children;
        rebuildLast(brothers);
    }

    public void rebuildLastChildren() {
        ArrayList brothers = children;
        rebuildLast(brothers);
    }

    public void down() {
        TreeControlNode parent = getParent();
        ArrayList brothers = parent.children;
        int myIndex = brothers.indexOf(this);
        if (myIndex > 0) {
            TreeControlNode nextOne
                    = (TreeControlNode) brothers.get(myIndex - 1);
            brothers.set(myIndex, nextOne);
            brothers.set(myIndex - 1, this);
        }
        rebuildLast(brothers);
    }

    /**
     * The unique (within the entire tree) name of this node.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    /**
     * The parent node of this node, or <code>null</code> if this is the root node.
     */
    protected TreeControlNode parent = null;

    public TreeControlNode getParent() {
        return (this.parent);
    }

    void setParent(TreeControlNode parent) {
        this.parent = parent;
        if (parent == null) {
            width = 1;
        } else {
            width = parent.getWidth() + 1;
        }
    }

    /**
     * Is this node currently selected?
     */
    protected boolean selected = false;

    public boolean isSelected() {
        return (this.selected);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * The window target for the hyperlink identified by the <code>action</code> property, if this node is selected by
     * the user.
     */
    protected String target = null;

    public String getTarget() {
        return (this.target);
    }

    /**
     * The <code>TreeControl</code> instance representing the entire tree.
     */
    protected TreeControl tree = null;

    public TreeControl getTree() {
        return (this.tree);
    }

    void setTree(TreeControl tree) {
        this.tree = tree;
    }

    /**
     * The display width necessary to display this item (if it is visible). If this item is not visible, the calculated
     * width will be that of our most immediately visible parent.
     */
    protected int width = 0;

    public int getWidth() {
        return (this.width);
    }

    // --------------------------------------------------------- Public Methods
    /**
     * Add a new child node to the end of the list.
     *
     * @param child The new child node
     *
     * @exception IllegalArgumentException if the name of the new child node is not unique
     */
    public void addChild(TreeControlNode child)
            throws IllegalArgumentException {

        tree.addNode(child);
        child.setParent(this);
        synchronized (children) {
            int n = children.size();
            if (n > 0) {
                TreeControlNode node = (TreeControlNode) children.get(n - 1);
                node.setLast(false);
            }
            child.setLast(true);
            children.add(child);
        }

    }

    /**
     * Add a new child node at the specified position in the child list.
     *
     * @param offset Zero-relative offset at which the new node should be inserted
     * @param child The new child node
     *
     * @exception IllegalArgumentException if the name of the new child node is not unique
     */
    public void addChild(int offset, TreeControlNode child)
            throws IllegalArgumentException {

        tree.addNode(child);
        child.setParent(this);
        synchronized (children) {
            children.add(offset, child);
        }

    }

    /**
     * Return the set of child nodes for this node.
     */
    public TreeControlNode[] findChildren() {

        synchronized (children) {
            TreeControlNode results[] = new TreeControlNode[children.size()];
            return ((TreeControlNode[]) children.toArray(results));
        }

    }

    /**
     * Remove this node from the tree.
     */
    public void remove() {

        if (tree != null) {
            tree.removeNode(this);
        }

    }

    /**
     * Remove the child node (and all children of that child) at the specified position in the child list.
     *
     * @param offset Zero-relative offset at which the existing node should be removed
     */
    public void removeChild(int offset) {

        synchronized (children) {
            TreeControlNode child = (TreeControlNode) children.get(offset);
            tree.removeNode(child);
            child.setParent(null);
            children.remove(offset);
        }

    }

    // -------------------------------------------------------- Package Methods
    /**
     * Remove the specified child node. It is assumed that all of the children of this child node have already been
     * removed.
     *
     * @param child Child node to be removed
     */
    void removeChild(TreeControlNode child) {

        if (child == null) {
            return;
        }
        synchronized (children) {
            int n = children.size();
            for (int i = 0; i < n; i++) {
                if (child == (TreeControlNode) children.get(i)) {
                    children.remove(i);
                    return;
                }
            }
        }

    }

    /**
     * Sets the label.
     *
     * @param label The label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the icon.
     *
     * @param icon The icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
