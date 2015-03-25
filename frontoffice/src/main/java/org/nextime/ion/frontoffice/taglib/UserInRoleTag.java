package org.nextime.ion.frontoffice.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class UserInRoleTag extends TagSupport {

    protected String _role = null;

    public String getRole() {
        return _role;
    }

    public void setRole(String role) {
        _role = role;
    }

    public int doStartTag() throws JspException {
        if (checkRole()) {
            return (EVAL_BODY_INCLUDE);
        } else {
            return (SKIP_BODY);
        }
    }

    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    protected boolean checkRole() {
        return ((HttpServletRequest) pageContext.getRequest()).isUserInRole(_role);
    }
}
