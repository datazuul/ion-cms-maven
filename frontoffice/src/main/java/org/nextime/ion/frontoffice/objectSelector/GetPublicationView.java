package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.common.IsOnline;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;

public class GetPublicationView implements ObjectSelector {

    public Collection selectObjects(
            Hashtable params,
            HttpServletRequest request,
            HttpServletResponse response)
            throws SelectException {
        try {
            String publication = (String) params.get("publication");
            String view = (String) params.get("view");

            String currentLocale
                    = request.getSession().getAttribute("currentLocale") + "";

            Publication p = Publication.getInstance(publication);

            int version = 1;
            if (params.get("version") == null) {
                version = IsOnline.getMostRecentVersion(p);
            } else {
                version = Integer.parseInt(params.get("version") + "");
            }

            PublicationResult r = new PublicationResult();
            r.setVersion(version);
            r.setPublication(p);
            if (view != null) {
                r.setView(
                        new String(
                                Viewer.getView(p, version, view, currentLocale)));
            }
            Vector v2 = new Vector();
            v2.add(r);
            return v2;
        } catch (Exception e) {
            Logger.getInstance().error("Erreur du SelectObject", this, e);
            throw new SelectException(e.getMessage());
        }
    }

}
