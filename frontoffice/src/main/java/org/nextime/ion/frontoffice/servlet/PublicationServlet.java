package org.nextime.ion.frontoffice.servlet;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.common.IsOnline;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.frontoffice.bean.IonStatus;
import org.nextime.ion.frontoffice.bean.SectionTypes;
import org.nextime.ion.frontoffice.exception.IdNotFoundException;

public class PublicationServlet extends HttpServlet {

    public void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            CommonThings.common(request, response);

            String requestedId
                    = (request.getPathInfo() != null)
                            ? request.getPathInfo().substring(1)
                            : null;
            if (requestedId != null) {
                if (requestedId.indexOf(".") != -1) {
                    requestedId
                            = requestedId.substring(0, requestedId.indexOf("."));
                }
            }

            // rollback dirty transaction ??
            if (Mapping.getInstance().isTransactionActive()) {
                try {
                    Mapping.rollback();
                } catch (Exception e) {
                }
            }

            Mapping.begin();

            Publication publication;
            try {
                publication = Publication.getInstance(requestedId);
            } catch (MappingException e) {
                throw new IdNotFoundException();
            }

            Section section = (Section) publication.listSections().firstElement();

            IonStatus status = new IonStatus();
            status.setCurrentSection(section);
            status.setCurrentPublication(publication);
            status.setCurrentVersion(IsOnline.getMostRecentVersion(publication));
            if (request.getParameter("static") != null) {
                status.setIsStatic(true);
            }
            request.setAttribute("ionStatus", status);

            // utilise la metaData "template" de la section
            // pour decider vers quelle vue rediriger le flux
            String template = (String) section.getMetaData("template");
            if (template == null) {
                template = "default";
            }

            String jsp = SectionTypes.getSectionBean(this, template).getJsp();

            if (request.getParameter("template") != null) {
                jsp = request.getParameter("template") + ".jsp";
            }

            getServletContext()
                    .getRequestDispatcher(
                            "/templates/" + jsp)
                    .forward(request, response);

            Mapping.rollback();

        } catch (Exception e) {
            Mapping.rollback();
            //transmet l'exception a tomcat
            throw new ServletException(e);
        }
    }

    protected String getDefaultSectionId() throws MappingException {
        Vector rootSections = Section.listRootSections();
        return ((Section) rootSections.get(0)).getId();
    }

}
