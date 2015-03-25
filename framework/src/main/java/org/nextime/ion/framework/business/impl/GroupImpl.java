package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.mapping.AccessMode;
import org.nextime.ion.framework.business.Group;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.mapping.MappingException;

/**
 * Une implementation de Group
 *
 * @author gbort
 * @version 1.1
 */
public class GroupImpl extends Group implements Persistent {

    private static Vector _listeners = new Vector();

    public static void addListener(WcmListener listener) {
        _listeners.add(listener);
    }

    public static void removeListener(WcmListener listener) {
        _listeners.remove(listener);
    }
    private String _description;
    private String _id;
    private Hashtable _metaData;
    private Vector _users;

    // pour que la classe soit constructible par castor
    public GroupImpl() {
        _description = "";
        _metaData = new Hashtable();
        _users = new Vector();
    }

    public void addUser(User user) {
        if (!_users.contains(user)) {
            _users.add(user);
            user.addGroup(this);
        }

    }

    public boolean equals(Object o) {
        try {
            return ((GroupImpl) o).getId().equals(getId());
        } catch (Exception e) {
            return false;
        }
    }

    public String getId() {
        return _id;
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

    public Vector getUsers() {
        return _users;
    }

    public String[] getUsersIds() {
        String[] users = new String[_users.size()];
        for (int i = 0; i < _users.size(); i++) {
            users[i] = ((User) _users.get(i)).getLogin();
        }
        return users;
    }

    public boolean isInThisGroup(User user) {
        for (int i = 0; i < _users.size(); i++) {
            if (_users.get(i).equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void jdoAfterCreate() {
        WcmEvent event = new WcmEvent(this, getId());
        for (int i = 0; i < _listeners.size(); i++) {
            ((WcmListener) _listeners.get(i)).objectCreated(event);
        }
    }

    public void jdoAfterRemove() {
        WcmEvent event = new WcmEvent(this, getId());
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
            WcmEvent event = new WcmEvent(this, getId());
            for (int i = 0; i < _listeners.size(); i++) {
                ((WcmListener) _listeners.get(i)).objectModified(event);
            }
        }
    }

    public void jdoTransient() {
    }

    public void jdoUpdate() {
    }

    public Vector listUsers() {
        return (Vector) _users.clone();
    }

    public void removeMetaData(String key) {
        _metaData.remove(key);
    }

    public void removeUser(User user) {
        _users.remove(user);
    }

    public void resetUsers() {
        for (int i = 0; i < _users.size(); i++) {
            User g = (User) _users.get(i);
            g.removeGroup(this);
        }
        _users = new Vector();
    }

    public void setId(String value) {
        _id = value;
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

    public void setUsers(Vector v) {
        _users = v;
    }

    public String toString() {
        return "type[GROUP] properties["
                + _id
                + ";"
                + _description
                + "] metaData"
                + _metaData
                + " members["
                + _users.size()
                + "]";
    }

}
