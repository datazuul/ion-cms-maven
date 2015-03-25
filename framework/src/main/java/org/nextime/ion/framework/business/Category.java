/*
 * iON content management system.
 * Copyright (C) 2002  Guillaume Bort(gbort@msn.com). All rights reserved.
 *
 * Copyright (c) 2000 The Apache Software Foundation. All rights reserved.
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
 */
package org.nextime.ion.framework.business;

import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.nextime.ion.framework.business.impl.CategoryImpl;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * @author gbort
 * @version 0.9
 */
public abstract class Category {

    public static Category getInstance(String id) throws MappingException {
        try {
            Category u
                    = (Category) Mapping.getInstance().getDb().load(
                            CategoryImpl.class,
                            id);
            Logger.getInstance().log(
                    "Une instance de l'objet Category pour l'id "
                    + id
                    + " xxx",
                    Category.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de charger une instance de l'objet Category pour l'id "
                    + id
                    + ".";
            Logger.getInstance().error(message, Category.class, e);
            throw new MappingException(message);
        }
    }

    public static Category create(String id) throws MappingException {
        try {
            CategoryImpl u = new CategoryImpl();
            u.setId(id);
            Mapping.getInstance().getDb().create(u);
            Logger.getInstance().log(
                    "Un objet Category pour l'id " + id + " xxx.",
                    Category.class);
            return u;
        } catch (PersistenceException e) {
            String message
                    = "Impossible de crxer l'objet Category pour l'id " + id + ".";
            Logger.getInstance().error(message, Category.class, e);
            throw new MappingException(message);
        }
    }

    public static void remove(String id) throws MappingException {
        try {
            Category u = getInstance(id);
            u.remove();
        } catch (MappingException e) {
            String message
                    = "Etes vous certain qu'un objet Category pour l'id "
                    + id
                    + " existe ?";
            Logger.getInstance().error(message, Category.class, e);
        }
    }

    public void remove() throws MappingException {
        try {
            Mapping.getInstance().getDb().remove(this);
            Logger.getInstance().log(
                    "L'objet Category pour l'id " + getId() + " xxx detruit.",
                    Category.class);
        } catch (PersistenceException e) {
            String message
                    = "Impossible de detruire l'objet Category pour l'id "
                    + getId()
                    + ".";
            Logger.getInstance().error(message, Category.class, e);
            throw new MappingException(message);
        }
    }

    public abstract String getId();

    public abstract void setMetaData(String key, Object value)
            throws MappingException;

    public abstract Object getMetaData(String key);

    public abstract void removeMetaData(String key);

    public abstract Hashtable getMetaData();

    public abstract void addPublication(Publication p);

    public abstract void removePublication(Publication p);

    public abstract boolean isInThisCategory(Publication p);

    public abstract Vector listPublications();

    public abstract String[] getPublicationsIds();

    public abstract void resetPublications();

    public static Vector listAll() throws MappingException {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.CategoryImpl p");
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                v.add(results.next());
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du listAll de Category",
                    Section.class,
                    e);
            throw new MappingException(e.getMessage());
        }
        return v;
    }

    public static void addListener(WcmListener listener) {
        CategoryImpl.addListener(listener);
    }

    /**
     * Retire un listener
     *
     * @see #addListener(WCMListener)
     */
    public static void removeListener(WcmListener listener) {
        CategoryImpl.removeListener(listener);
    }

}
