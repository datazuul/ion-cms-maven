package org.nextime.ion.common;

import java.util.Vector;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.User;

public class IsOnline {

    public static String frontUserLogin = "visiteurAnonyme";

    public static boolean getStatus(Publication p) {
        try {
            Vector v = p.getVersions();
            for (int i = 1; i <= v.size(); i++) {
                if (p
                        .getVersion(i)
                        .getWorkflow(User.getInstance(frontUserLogin))
                        .getPermissions()
                        .contains("canDisplay")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getStatus(Object p) {
        try {
            return getStatus((Publication) p);
        } catch (Exception e) {
            return false;
        }
    }

    public static int getMostRecentVersion(Publication p) {
        try {
            Vector v = p.getVersions();
            for (int i = 0; i < v.size(); i++) {
                PublicationVersion ver = (PublicationVersion) v.get(i);
                if (ver
                        .getWorkflow(User.getInstance(frontUserLogin))
                        .getPermissions()
                        .contains("canDisplay")) {
                    return ver.getVersion();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
