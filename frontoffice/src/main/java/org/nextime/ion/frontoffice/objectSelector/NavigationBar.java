package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

/**
 * Renvoi une collection d'objets Section racines ( dont le parent vaut null )
 */
public class NavigationBar implements ObjectSelector {

    public Collection selectObjects(Hashtable params, HttpServletRequest request, HttpServletResponse response) throws SelectException {
        try {
            //Mapping.begin();
            String currentSection = (String) params.get("currentSection");
            Vector v = new Vector();
            Section current = Section.getInstance(currentSection);
            while (current != null) {
                v.add(current);
                current = current.getParent();
            }
            Collections.reverse(v);
            //Mapping.rollback();
            return v;
        } catch (Exception e) {
            Logger.getInstance().error("Erreur du SelectObject", this, e);
            throw new SelectException(e.getMessage());
        }
    }

}
