package org.nextime.ion.admin.action.section;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.helper.SiteMap;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public class SiteMapAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        try {
            String map = "";
            Mapping.begin();
            if (request.getQueryString().equals("xml")) {
                map = SiteMap.getXmlMap();
            } else {
                map = SiteMap.getHtmlMap();
            }
            Mapping.rollback();
            request.setAttribute("map", map);
        } catch (MappingException e) {
            Mapping.rollback();
            throw new ServletException(e);
        }

        return mapping.findForward("view");
    }

}
