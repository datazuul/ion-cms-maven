package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.jdom.Document;
import org.nextime.ion.framework.business.impl.TypePublicationImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public abstract class TypePublication {

    public static TypePublication getInstance(String id)
            throws MappingException {
        try {
            TypePublication u
                    = (TypePublication) Mapping.getInstance().getDb().load(
                            TypePublicationImpl.class,
                            id);
            Logger.getInstance().log(
                    "Une instance de l'objet TypePublication pour l'id "
                    + id
                    + " xxx.",
                    TypePublication.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de charger une instance de l'objet TypePublication pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, TypePublication.class, e);
            throw new MappingException(message);
        }
    }

    public static TypePublication create(String id) throws MappingException {
        try {
            TypePublicationImpl u = new TypePublicationImpl();
            u.setId(id);
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet TypePublication pour l'id " + id + " xxx.",
                    Group.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de crxer l'objet TypePublication pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, TypePublication.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String id) throws MappingException {
        try {
            TypePublication u = getInstance(id);
            u.remove();
        } catch (MappingException e) {
            String message
                    = "Etes vous certain qu'un objet TypePublication pour l'id "
                    + id
                    + " existe ?";
            Logger.getInstance().error(message, TypePublication.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log(
                    "L'objet TypePublication pour l'id "
                    + getId()
                    + " xxx detruit.",
                    Group.class);
        } catch (PersistenceException e) {
            String message
                    = "Impossible de detruire l'objet TypePublication pour l'id "
                    + getId()
                    + ".";
            Logger.getInstance().error(message, TypePublication.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getId();

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void removeMetaData(String key);

    /**
     * Renvoi la liste des styles disponibles pour ce type
     *
     * @return un Vetor de String
     */
    public abstract Vector listStyles();

    /**
     * renvoi une liste de toutes les objets Publication.
     *
     * @return un Vector d'objet Publication
     * @throws MappingException en cas d'erreur lors du SELECT
     */
    public static Vector listAll() throws MappingException {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.TypePublicationImpl p");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                v.add(results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du listAll de TypePublication",
                    TypePublication.class,
                    e);
            throw new MappingException(e.getMessage());
        }
        return v;
    }

    public abstract Document getModel();

    public static void addListener(WcmListener listener) {
        TypePublicationImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        TypePublicationImpl.removeListener(listener);
    }

}
