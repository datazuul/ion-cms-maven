package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.GroupImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public abstract class Group {

    public static Group getInstance(String id) throws MappingException {
        try {
            Group u
                    = (Group) Mapping.getInstance().getDb().load(GroupImpl.class, id);
            Logger.getInstance().log(
                    "Une instance de l'objet Group pour l'id "
                    + id
                    + " xxx.",
                    Group.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de charger une instance de l'objet Group pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, Group.class, e);
            throw new MappingException(message);
        }
    }

    public static Group create(String id) throws MappingException {
        try {
            GroupImpl u = new GroupImpl();
            u.setId(id);
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet Group pour l'id " + id + " xxx.",
                    Group.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de crxer l'objet Group pour l'id " + id + ".";
            Logger.getInstance().error(message, Group.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String id) throws MappingException {
        try {
            Group u = getInstance(id);
            u.remove();
        } catch (MappingException e) {
            String message
                    = "Etes vous certain qu'un objet Group pour l'id "
                    + id
                    + " existe ?";
            Logger.getInstance().error(message, Group.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log(
                    "L'objet Group pour l'id " + getId() + " xxx detruit.",
                    Group.class);
        } catch (PersistenceException e) {
            String message
                    = "Impossible de detruire l'objet Group pour l'id "
                    + getId()
                    + ".";
            Logger.getInstance().error(message, Group.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getId();

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract void removeMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void addUser(User user);

    public abstract void removeUser(User user);

    public abstract boolean isInThisGroup(User user);

    public abstract Vector listUsers();

    public abstract String[] getUsersIds();

    /**
     * Retire tous les utilisateurs de ce groupe.
     */
    public abstract void resetUsers();

    public static Vector listAll() throws MappingException {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.GroupImpl p");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                v.add(results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du listAll de Group",
                    Group.class,
                    e);
            throw new MappingException(e.getMessage());
        }
        return v;
    }

    public static void addListener(WcmListener listener) {
        GroupImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        GroupImpl.removeListener(listener);
    }
}
