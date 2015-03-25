package org.nextime.ion.frontoffice.taglib;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class IterateOverTag extends BodyTagSupport {

    private String _var;
    private Collection _collection;
    private Iterator _iterator;
    private IterateStatus _status;

    public IterateOverTag() {
        super();
        init();
    }

    public void setVar(String var) {
        _var = var;
    }

    public int doStartTag() throws JspException {

        Tag t = findAncestorWithClass(this, SelectObjectsTag.class);
        if (t == null) {
            throw new JspTagException("Le tag iterateOver doit etre dans un tag selectObjects");
        }

        _collection = ((SelectObjectsTag) t).getCollection();

        if (_collection == null) {
            return (SKIP_BODY);
        }

        _status = new IterateStatus();
        _status._size = _collection.size();
        _status._index = 0;

        _iterator = _collection.iterator();

        if (_iterator.hasNext()) {
            Object element = _iterator.next();
            if (element == null) {
                pageContext.removeAttribute(_var);
            } else {
                pageContext.setAttribute(_var, element);
            }
            _status._index++;
            pageContext.setAttribute("iterateStatus", _status);
            return (EVAL_BODY_TAG);
        } else {
            return (SKIP_BODY);
        }
    }

    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            JspWriter writer = pageContext.getOut();
            if (writer instanceof BodyContent) {
                writer = ((BodyContent) writer).getEnclosingWriter();
            }
            try {
                writer.print(bodyContent.getString());
            } catch (IOException e) {
                throw new JspException(e);
            }
            bodyContent.clearBody();
        }

        if (_iterator.hasNext()) {
            Object element = _iterator.next();
            if (element == null) {
                pageContext.removeAttribute(_var);
            } else {
                pageContext.setAttribute(_var, element);
            }
            _status._index++;
            pageContext.setAttribute("iterateStatus", _status);
            return (EVAL_BODY_TAG);
        } else {
            return (SKIP_BODY);
        }

    }

    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        init();
    }

    private void init() {
        _var = null;
        _status = null;
    }

}
