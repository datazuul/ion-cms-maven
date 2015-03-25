package org.nextime.ion.frontoffice.taglib;

import java.util.Collection;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.nextime.ion.frontoffice.objectSelector.ObjectSelector;

public class SelectObjectsTag extends BodyTagSupport {

    private Hashtable _params;
    private String _type;

    public void addParameter(String name, String value) {
        _params.put(name, value);
    }

    public void setType(String type) {
        _type = type;
    }

    public int doStartTag() throws JspException {
        _params = new Hashtable();
        return (EVAL_BODY_INCLUDE);
    }

    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        init();
    }

    private void init() {
        _params = new Hashtable();
    }

    public Collection getCollection() throws JspException {
        try {
            Object os = Class.forName(_type).newInstance();
            return ((ObjectSelector) os).selectObjects(
                    _params,
                    (HttpServletRequest) pageContext.getRequest(),
                    (HttpServletResponse) pageContext.getResponse());
        } catch (Exception e) {
            throw new JspException(
                    "Impossible d'invoquer le ObjectSelector : " + e.getMessage());
        }
    }
}
