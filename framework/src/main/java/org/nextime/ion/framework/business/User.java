package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.UserImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public abstract class User {

    public static User getInstance(String login) throws MappingException {
        try {
            User u
                    = (User) Mapping.getInstance().getDb().load(
                            UserImpl.class,
                            login);
            Logger.getInstance().log(
                    "Une instance de l'objet User pour l'id "
                    + login
                    + " xxx.",
                    User.class);
            return u;
        } catch (Exception e) {
            String message
                    = "Impossible de charger une instance de l'objet User pour l'id "
                    + login
                    + ".";
            Logger.getInstance().error(message, User.class, e);
            throw new MappingException(message);
        }
    }

    public static User create(String login) throws MappingException {
        try {
            UserImpl u = new UserImpl();
            u.setLogin(login);
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet User pour l'id " + login + " xxx.",
                    User.class);
            return u;
        } catch (Exception e) {
            String message
                    = "Impossible de crxer l'objet User pour l'id " + login + ".";
            Logger.getInstance().error(message, User.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String login) throws MappingException {
        try {
            User u = getInstance(login);
            u.remove();
        } catch (Exception e) {
            String message
                    = "Etes vous certain qu'un objet User pour l'id "
                    + login
                    + " existe ?";
            Logger.getInstance().error(message, User.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log(
                    "L'objet User pour l'id " + getLogin() + " xxx detruit.",
                    User.class);
        } catch (Exception e) {
            String message
                    = "Impossible de detruire l'objet User pour l'id "
                    + getLogin()
                    + ".";
            Logger.getInstance().error(message, User.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getLogin();

    /**
     * Retourne le password de l'utilisateur.
     *
     * @return password de User
     */
    public abstract String getPassword();

    public abstract void setPassword(String value);

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void removeMetaData(String key);

    public abstract void addGroup(Group group);

    public abstract void removeGroup(Group group);

    public abstract boolean isInGroup(Group group);

    public abstract Vector listGroups();

    /**
     * Retire tous les groupes de cet utilisateur.
     */
    public abstract void resetGroups();

    public abstract String[] getGroupsIds();

    /**
     * Liste les publications de l'utilisateur ( dont il est l'auteur )
     *
     * @return un Vector d'objet Publication
     */
    public abstract Vector listPublications();

    /**
     * renvoi une liste de tous les utilisateurs
     */
    public static Vector listAll() {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.UserImpl p");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                v.add(results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du listAll de User",
                    User.class,
                    e);
            return v;
        }
        return v;
    }

    public static void addListener(WcmListener listener) {
        UserImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        UserImpl.removeListener(listener);
    }

}
