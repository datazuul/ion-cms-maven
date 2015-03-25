package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

/**
 * Renvoi une collection d'objets Section racines ( dont le parent vaut null )
 */
public class FindParentSection implements ObjectSelector {

    public Collection selectObjects(Hashtable params, HttpServletRequest request, HttpServletResponse response) throws SelectException {
        try {
            Vector v = new Vector();
            int level = Integer.parseInt(params.get("level") + "");
            Section s = Section.getInstance(params.get("section") + "");
            if (level > s.getLevel()) {
                return v;
            }
            Section parent = s;
            while (parent.getLevel() > level) {
                parent = parent.getParent();
            }
            v.add(parent);
            return v;
        } catch (Exception e) {
            Logger.getInstance().error("Erreur du SelectObject : " + e.getMessage(), this, e);
            throw new SelectException(e.getMessage());
        }
    }

}
