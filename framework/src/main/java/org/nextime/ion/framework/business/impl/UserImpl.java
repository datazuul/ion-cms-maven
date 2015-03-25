package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * Une implementation de User
 *
 * @author gbort
 * @version 1.0
 */
public class UserImpl extends User implements Persistent, Comparable {

    private static Vector _listeners = new Vector();

    public static void addListener(WcmListener listener) {
        _listeners.add(listener);
    }

    public static void removeListener(WcmListener listener) {
        _listeners.remove(listener);
    }
    private Vector _groups;

    private String _login;
    private Hashtable _metaData;
    private String _password;

    // pour que la classe soit constructible par castor
    public UserImpl() {
        _password = "";
        _metaData = new Hashtable();
        _groups = new Vector();
    }

    public void addGroup(Group group) {
        if (!_groups.contains(group)) {
            _groups.add(group);
            group.addUser(this);
        }
    }

    // comparer : par ordre alphabetique du login
    public int compareTo(Object o) {
        try {
            UserImpl u = (UserImpl) o;
            return u.getLogin().compareTo(getLogin());
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean equals(Object o) {
        try {
            return ((UserImpl) o).getLogin().equals(getLogin());
        } catch (Exception e) {
            return false;
        }
    }

    public Vector getGroups() {
        return _groups;
    }

    public String[] getGroupsIds() {
        String[] groups = new String[_groups.size()];
        for (int i = 0; i < _groups.size(); i++) {
            groups[i] = ((Group) _groups.get(i)).getId();
        }
        return groups;
    }

    public String getLogin() {
        return _login;
    }

    public Object getMetaData(String key) {
        return _metaData.get(key);
    }

    public Hashtable getMetaData() {
        return _metaData;
    }

    public byte[] getMetaDataBytes() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(_metaData);
        byte[] result = os.toByteArray();
        os.close();
        return result;
    }

    public Vector getMetaDataFields() {
        Enumeration keys = _metaData.keys();
        Vector v = new Vector();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            v.add(new DataField(key + "", _metaData.get(key) + ""));
        }
        return v;
    }

    public String getPassword() {
        return _password;
    }

    public boolean isInGroup(Group group) {
        for (int i = 0; i < _groups.size(); i++) {
            if (_groups.get(i).equals(group)) {
                return true;
            }
        }
        return false;
    }

    public void jdoAfterCreate() {
        WcmEvent event = new WcmEvent(this, getLogin());
        for (int i = 0; i < _listeners.size(); i++) {
            ((WcmListener) _listeners.get(i)).objectCreated(event);
        }
    }

    public void jdoAfterRemove() {
        WcmEvent event = new WcmEvent(this, getLogin());
        for (int i = 0; i < _listeners.size(); i++) {
            ((WcmListener) _listeners.get(i)).objectDeleted(event);
        }
    }

    public void jdoBeforeCreate(Database db) {
    }

    public void jdoBeforeRemove() {
    }

    @Override
    public Class jdoLoad(AccessMode am) throws Exception {
        return null;
    }

    public Class jdoLoad(short accessMode) {
        return null;
    }

    public void jdoPersistent(Database db) {
    }

    public void jdoStore(boolean modified) {
        if (modified) {
            WcmEvent event = new WcmEvent(this, getLogin());
            for (int i = 0; i < _listeners.size(); i++) {
                ((WcmListener) _listeners.get(i)).objectModified(event);
            }
        }
    }

    public void jdoTransient() {
    }

    public void jdoUpdate() {
    }

    public Vector listGroups() {
        return (Vector) _groups.clone();
    }

    public Vector listPublications() {
        Vector v = new Vector();
        try {
            OQLQuery oql
                    = Mapping.getInstance().getDb().getOQLQuery(
                            "SELECT p FROM org.nextime.ion.framework.business.impl.PublicationVersionImpl p WHERE author = $1");
            oql.bind(this);
            QueryResults results = oql.execute();
            while (results.hasMore()) {
                PublicationVersion s = (PublicationVersion) results.next();
                v.add(s);
            }
        } catch (Exception e) {
            Logger.getInstance().error(
                    "erreur lors du list des publication de l'auteur",
                    this,
                    e);
            return v;
        }
        return v;
    }

    public void removeGroup(Group group) {
        _groups.remove(group);
    }

    public void removeMetaData(String key) {
        _metaData.remove(key);
    }

    public void resetGroups() {
        for (int i = 0; i < _groups.size(); i++) {
            Group g = (Group) _groups.get(i);
            g.removeUser(this);
        }
        _groups = new Vector();
    }

    public void setGroups(Vector v) {
        _groups = v;
    }

    public void setLogin(String value) {
        _login = value;
    }

    public void setMetaData(String key, Object value) throws MappingException {
        if (!(value instanceof java.io.Serializable)) {
            throw new MappingException(
                    value.getClass()
                    + " n'implemente pas l'interface java.io.Serializable");
        }
        _metaData.put(key, value);
    }

    public void setMetaData(Hashtable ht) {
        _metaData = ht;
    }

    public void setMetaDataBytes(byte[] value) throws Exception {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(value);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object o = ois.readObject();
            is.close();
            _metaData = (Hashtable) o;
        } catch (NullPointerException e) {
            _metaData = new Hashtable();
        }
    }

    public void setPassword(String value) {
        _password = value;
    }

    public String toString() {
        return "type[USER] properties["
                + _login
                + ";"
                + _password
                + "] metaData"
                + _metaData
                + " groups"
                + _groups;
    }

}
