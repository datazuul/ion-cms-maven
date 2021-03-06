package org.nextime.ion.frontoffice.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class SelectObjectsParamTag extends BodyTagSupport {

    private String _value;
    private String _name;

    public SelectObjectsParamTag() {
        super();
        init();
    }

    public int doStartTag() throws JspException {
        evaluateExpressions();
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, SelectObjectsTag.class);
        if (t == null) {
            throw new JspTagException("Le tag param doit etre dans un tag selectObjects");
        }

        if (_name == null && _name.equals("")) {
            return EVAL_PAGE;
        }

        SelectObjectsTag parent = (SelectObjectsTag) t;
        String value = _value;
        if (value == null) {
            if (bodyContent == null || bodyContent.getString() == null) {
                value = "";
            } else {
                value = bodyContent.getString().trim();
            }
        }
        parent.addParameter(_name, value);
        return EVAL_PAGE;
    }

    public void release() {
        super.release();
        init();
    }

    public void setValue(String value) {
        _value = value;
    }

    public void setName(String name) {
        _name = name;
    }

    private void init() {
        _value = null;
        _name = null;
    }

    private void evaluateExpressions() throws JspException {
        if (_value != null && !"".equals(_value)) {
            _value
                    = ExpressionEvaluatorManager.evaluate(
                            "value",
                            _value,
                            Object.class,
                            this,
                            pageContext)
                    + "";
        }
    }

}
