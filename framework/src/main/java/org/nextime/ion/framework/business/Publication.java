package org.nextime.ion.framework.business;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public abstract class Publication {

    public static Publication getInstance(String id) throws MappingException {
        try {
            Publication u
                    = (Publication) Mapping.getInstance().getDb().load(
                            PublicationImpl.class,
                            id);
            Logger.getInstance().log(
                    "Une instance de l'objet Publication pour l'id "
                    + id
                    + " xxx.",
                    Publication.class);
            ((PublicationImpl) u).postLoad();
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de charger une instance de l'objet Publication pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, Publication.class, e);
            throw new MappingException(message);
        }
    }

    public static Publication create(User user, String id, TypePublication type, String workflowType)
            throws MappingException {
        try {
            PublicationImpl u = new PublicationImpl();
            ((PublicationImpl) u).postLoad();
            u.setId(id);
            u.setType(type);
            u.setDate(new Date());
            u.setWorkflowType(workflowType);
            u.newVersion(user);
            Mapping.getInstance().getDb().create(u);
            u.postLoad();
            Logger.getInstance().log(
                    "Un objet Publication pour l'id " + id + " xxx.",
                    Publication.class);
            return u;
        } catch (Exception e) {
            String message
                    = "Impossible de crxer l'objet Publication pour l'id " + id + "." + e.getMessage();
            Logger.getInstance().error(message, Publication.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String id) throws MappingException {
        try {
            Publication u = getInstance(id);
            u.remove();
        } catch (MappingException e) {
            String message
                    = "Etes vous certain qu'un objet Publication pour l'id "
                    + id
                    + " existe ?";
            Logger.getInstance().error(message, Publication.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log(
                    "L'objet Publication pour l'id " + getId() + " xxx detruit.",
                    Publication.class);
        } catch (PersistenceException e) {
            String message
                    = "Impossible de detruire l'objet Publication pour l'id "
                    + getId()
                    + ".";
            Logger.getInstance().error(message, Publication.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getId();

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract void removeMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void changeType(TypePublication type);

    /**
     * Retourne le type de la publication.
     *
     * @return un objet TypePublication
     */
    public abstract TypePublication getType();

    public abstract void setDate(Date date);

    /**
     * Retourne la date
     */
    public abstract Date getDate();

    public abstract String getFormatedDate();

    /**
     * Liste les sections auquelles appartient la publication.
     *
     * @return Un vector d'objet Section
     */
    public abstract Vector listSections();

    public abstract void addSection(Section s);

    /**
     * Retire la publication d'une section.
     *
     * @param La section a laquelle on souhaite que la publication n'appartienne plus.
     */
    public abstract void removeSection(Section s);

    /**
     * Verifie si la publication appartient a cette section.
     *
     * @return true/false
     */
    public abstract boolean isInSection(Section s);

    public abstract String[] getSectionsIds();

    /**
     * Supprime toutes les Sections pour cette publication.
     */
    public abstract void resetSections();

    public abstract Vector listCategories();

    public abstract void addCategory(Category c);

    public abstract void removeCategory(Category c);

    public abstract boolean isInCategory(Category c);

    public abstract String[] getCategoriesIds();

    public abstract void resetCategories();

    public static Vector listAll() throws MappingException {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.PublicationImpl p");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                v.add(results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du listAll de Publication",
                    Publication.class,
                    e);
            throw new MappingException(e.getMessage());
        }
        return v;
    }

    public abstract PublicationVersion getVersion(int ver);

    public abstract Vector getVersions();

    public abstract PublicationVersion getLastVersion();

    public abstract void newVersion(User user) throws Exception;

    public static void addListener(WcmListener listener) {
        PublicationImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        PublicationImpl.removeListener(listener);
    }

}
