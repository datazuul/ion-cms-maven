package org.nextime.ion.frontoffice.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.nextime.ion.framework.mapping.Mapping;

public class EndTag extends TagSupport {

    public int doStartTag() throws JspException {
        try {
            Mapping.rollback();
        } catch (Exception e) {
        }
        return EVAL_PAGE;
    }
}
