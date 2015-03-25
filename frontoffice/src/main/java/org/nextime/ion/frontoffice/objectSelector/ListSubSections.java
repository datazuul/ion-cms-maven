package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

public class ListSubSections implements ObjectSelector {

    public Collection selectObjects(Hashtable params, HttpServletRequest request, HttpServletResponse response) throws SelectException {
        try {
            String rootSection = (String) params.get("rootSection");
            //Mapping.begin();
            Section root = Section.getInstance(rootSection);
            Vector v = root.listSubSections();
            //Mapping.rollback();
            return v;
        } catch (Exception e) {
            Logger.getInstance().error("Erreur du SelectObject", this, e);
            throw new SelectException(e.getMessage());
        }
    }

}
