package org.nextime.ion.frontoffice.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.nextime.ion.framework.mapping.Mapping;

public class BeginTag extends TagSupport {

    public int doStartTag() throws JspException {
        try {
            Mapping.begin();
        } catch (Exception e) {
            Mapping.rollback();
        }
        return EVAL_PAGE;
    }
}
