package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

public abstract class Section {

    public static Section getInstance(String id) throws MappingException {
        try {
            Section u
                    = (Section) Mapping.getInstance().getDb().load(
                            SectionImpl.class,
                            id);
            Logger.getInstance().log(
                    "Une instance de l'objet Section pour l'id "
                    + id
                    + " xxx.",
                    Section.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de charger une instance de l'objet Section pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }

    public static Section create(Section parent, String id)
            throws MappingException {
        try {
            SectionImpl u = new SectionImpl();
            u.setId(id);
            u.setParent(parent);
            u.initIndex();
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet Section pour l'id " + u.getId() + " xxx.",
                    Section.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de crxer l'objet Section pour l'id " + id + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }

    public Section createSubSection(String id) throws MappingException {
        try {
            SectionImpl u = new SectionImpl();
            u.setId(id);
            u.setParent(this);
            u.initIndex();
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet Section pour l'id " + u.getId() + " xxx.",
                    Section.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de crxer l'objet Section pour l'id " + id + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String id) throws MappingException {
        try {
            Section u = getInstance(id);
            u.remove();
        } catch (MappingException e) {
            String message
                    = "Etes vous certain qu'un objet Section pour l'id "
                    + id
                    + " existe ?";
            Logger.getInstance().error(message, Section.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Vector v;
            if (getParent() == null) {
                v = listRootSections();
            } else {
                v = getParent().listSubSections();
            }
            for (int i = 0; i < v.size(); i++) {
                ((SectionImpl) v.get(i)).setIndex(0);
            }
            for (int i = 0; i < v.size(); i++) {
                ((SectionImpl) v.get(i)).initIndex();
            }
			//			Vector v2 = listSubSections();
            //			for (int i = 0; i < v2.size(); i++) {
            //				((SectionImpl) v2.get(i)).setParent(getParent());
            //			}
            Logger.getInstance().log(
                    "L'objet Section pour l'id " + getId() + " xxx detruit.",
                    Section.class);
        } catch (PersistenceException e) {
            String message
                    = "Impossible de detruire l'objet Section pour l'id "
                    + getId()
                    + ".";
            Logger.getInstance().error(message, Section.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getId();

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void removeMetaData(String key);

    public abstract Section getParent();

    public abstract void changeParent(Section parent);

    public abstract Vector listSubSections();

    public abstract Vector listPublications();

    /**
     * Ajoute un objet publication a cette Section
     */
    public abstract void addPublication(Publication p);

    /**
     * Retire un objet publication de cette Section
     */
    public abstract void removePublication(Publication p);

    /**
     * Verifie si une publication appartient a la Section
     */
    public abstract boolean isInThisSection(Publication p);

    public abstract String[] getPublicationsIds();

    /**
     * Liste les Section racine.
     *
     * @return un Vector de Section
     */
    public static Vector listRootSections() throws MappingException {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT s FROM org.nextime.ion.framework.business.impl.SectionImpl s WHERE is_undefined( parent ) order by s.index");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                Section s = (Section) results.next();
                v.add(s);
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du list des sections root",
                    Section.class,
                    e);
            throw new MappingException(e.getMessage());
        }
        return v;
    }

    /**
     * Liste tous les objets Section
     *
     * @return un Vector de Section
     */
    public static Vector listAll() throws MappingException {
        Vector v = new Vector();
        Vector v2 = listRootSections();
        for (int i = 0; i < v2.size(); i++) {
            Section s = (Section) v2.get(i);
            v.add(s);
            SectionImpl.listAllInternal(v, s);
        }
        return v;
    }

    public abstract String getPath();

    public abstract void up();

    public abstract void down();

    /**
     * Retourne l'index de la section
     */
    public abstract int getIndex();

    public abstract int getLevel();

    public static void addListener(WcmListener listener) {
        SectionImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        SectionImpl.removeListener(listener);
    }

}
