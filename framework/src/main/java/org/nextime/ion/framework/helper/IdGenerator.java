package org.nextime.ion.framework.helper;

import java.util.Vector;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.logger.Logger;

public class IdGenerator {

    /**
     * Prochain id libre pour une section
     */
    public static String nextSectionId() {
        try {
            String id = "";
            Vector v = Section.listAll();
            int max = 0;
            for (int i = 0; i < v.size(); i++) {
                Section s = (Section) v.get(i);
                String tid = s.getId();
                if (tid.startsWith("s")) {
                    try {
                        String ss = tid.substring(1);
                        int n = Integer.parseInt(ss);
                        if (n >= max) {
                            max = n + 1;
                        }
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }
                }
            }
            id = "s" + max;
            return id;
        } catch (Exception e) {
            Logger.getInstance().error(
                    "Erreur lors de la generation de l'id : " + e.getMessage(),
                    IdGenerator.class,
                    e);
            return null;
        }
    }

    /**
     * Prochain id libre pour une publication
     */
    public static String nextPublicationId() {
        try {
            String id = "";
            Vector v = Publication.listAll();
            int max = 0;
            for (int i = 0; i < v.size(); i++) {
                Publication s = (Publication) v.get(i);
                String tid = s.getId();
                if (tid.startsWith("p")) {
                    try {
                        String ss = tid.substring(1);
                        int n = Integer.parseInt(ss);
                        if (n >= max) {
                            max = n + 1;
                        }
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }
                }
            }
            id = "p" + max;
            return id;
        } catch (Exception e) {
            return null;
        }
    }

}
