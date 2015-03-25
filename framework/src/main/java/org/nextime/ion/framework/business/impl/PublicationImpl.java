package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.mapping.AccessMode;
import org.jdom.Document;
import org.nextime.ion.framework.business.Category;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.PublicationVersion;
import org.nextime.ion.framework.business.Section;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.business.User;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.mapping.Mapping;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.framework.workflow.Workflow;

/**
 * Une implementation de Publication
 *
 * @author gbort
 * @version 1.7
 */
public class PublicationImpl
        extends Publication
        implements Persistent, Comparable {

    private static Vector _listeners = new Vector();

    public static void addListener(WcmListener listener) {
        _listeners.add(listener);
    }

    public static void removeListener(WcmListener listener) {
        _listeners.remove(listener);
    }
    private boolean _canBeModified = false;
    private Vector _categories;
    private Date _date;

    private String _id;
    private Hashtable _metaData;
    private boolean _modified = false;
    private String _name;
    private Vector _sections;
    private TypePublication _type;
    private Vector _versions;
    private String _workflowType;

    // pour que la classe soit constructible par castor
    public PublicationImpl() {
        _name = "";
        _metaData = new Hashtable();
        _sections = new Vector();
        _categories = new Vector();
        _type = null;
        _date = null;
        _versions = new Vector();
    }

    public void addCategory(Category c) {
        if (!_categories.contains(c)) {
            _categories.add(c);
            c.addPublication(this);
        }
        postChange();
    }

    public void addSection(Section s) {
        if (!_sections.contains(s)) {
            _sections.add(s);
            s.addPublication(this);
        }
        postChange();
    }

    public void changeType(TypePublication type) {
        setType(type);
    }

    public int compareTo(Object o) {
        try {
            Publication p = (Publication) o;
            int r = p.getDate().compareTo(getDate());
            if (r == 0) {
                return -1;
            }
            if (r < 0) {
                return -10;
            }
            if (r > 0) {
                return 10;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean equals(Object o) {
        try {
            return ((PublicationImpl) o).getId().equals(getId());
        } catch (Exception e) {
            return false;
        }
    }

    public Vector getCategories() {
        return _categories;
    }

    public String[] getCategoriesIds() {
        String[] categories = new String[_categories.size()];
        for (int i = 0; i < _categories.size(); i++) {
            categories[i] = ((Category) _categories.get(i)).getId();
        }
        return categories;
    }

    public Date getDate() {
        return _date;
    }

    public String getDatePubli() {
        if (getDate() == null) {
            return null;
        }
        return getDate().getTime() + "";
    }

    public String getFormatedDate() {
        if (_date == null) {
            return "";
        }
        String pattern = Config.getInstance().getDateFormatPattern();
        if (pattern == null) {
            return _date.toString();
        }
        return new SimpleDateFormat(pattern).format(_date);
    }

    public String getId() {
        return _id;
    }

    public PublicationVersion getLastVersion() {
        try {
            return (PublicationVersion) getVersions().firstElement();
        } catch (Exception e) {
            return null;
        }
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

    public Vector getSections() {
        return _sections;
    }

    public String[] getSectionsIds() {
        String[] sections = new String[_sections.size()];
        for (int i = 0; i < _sections.size(); i++) {
            sections[i] = ((Section) _sections.get(i)).getId();
        }
        return sections;
    }

    public TypePublication getType() {
        return _type;
    }

    public PublicationVersion getVersion(int ver) {
        try {
            return (PublicationVersion) getVersions().get(getVersions().size() - ver);
        } catch (Exception e) {
            return null;
        }
    }

    public Vector getVersions() {
        Collections.sort(_versions);
        return _versions;
    }

    public String getWorkflowType() {
        return _workflowType;
    }

    public boolean isInCategory(Category c) {
        return _categories.contains(c);
    }

    public boolean isInSection(Section s) {
        return _sections.contains(s);
    }

    public void jdoAfterCreate() {
        WcmEvent event = new WcmEvent(this, getId());
        for (int i = 0; i < _listeners.size(); i++) {
            ((WcmListener) _listeners.get(i)).objectCreated(event);
        }
    }

    public void jdoAfterRemove() {
        // dispatch event
        WcmEvent event = new WcmEvent(this, getId());
        for (int i = 0; i < _listeners.size(); i++) {
            ((WcmListener) _listeners.get(i)).objectDeleted(event);
        }
    }

    public void jdoBeforeCreate(Database db) {
    }

    public void jdoBeforeRemove() {
        // remove all versions
        Enumeration v = ((Vector) getVersions().clone()).elements();
        while (v.hasMoreElements()) {
            try {
                PublicationVersionImpl p = (PublicationVersionImpl) v.nextElement();
                p.remove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        if (_modified) {
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

    public Vector listCategories() {
        return (Vector) _categories.clone();
    }

    public Vector listSections() {
        return (Vector) getSections().clone();
    }

    public void newVersion(User user) throws Exception {

        Document dataToInsert = getType().getModel();
        if (getLastVersion() != null) {
            dataToInsert = getLastVersion().getData();
        }

        PublicationVersionImpl version = new PublicationVersionImpl();
        version.setPublication(this);

        Hashtable ht = new Hashtable();
        ht.put("author", user.getLogin());
        ht.put("id", getId());
        ht.put("version", (getVersions().size() + 1) + "");
        Workflow wf = Workflow.create(user, getWorkflowType(), ht);
        version.setWorkflow(wf);

        version.setAuthor(user);
        version.setData(dataToInsert);
        version.setVersion(getVersions().size() + 1);
        version.setId(getId() + "_v" + version.getVersion());
        Mapping.getInstance().getDb().create(version);
        _versions.add(version);
        postChange();
    }

    public void postChange() {
        if (_canBeModified) {
            _modified = true;
        }
    }

    public void postLoad() {
        _canBeModified = true;
    }

    public void removeCategory(Category c) {
        _categories.remove(c);
        postChange();
    }

    public void removeMetaData(String key) {
        _metaData.remove(key);
        postChange();
    }

    public void removeSection(Section s) {
        _sections.remove(s);
        postChange();
    }

    public void resetCategories() {
        for (int i = 0; i < _categories.size(); i++) {
            Category g = (Category) _categories.get(i);
            g.removePublication(this);
        }
        _categories = new Vector();
        postChange();
    }

    public void resetSections() {
        for (int i = 0; i < _sections.size(); i++) {
            Section g = (Section) _sections.get(i);
            g.removePublication(this);
        }
        _sections = new Vector();
        postChange();
    }

    public void setCategories(Vector v) {
        _categories = v;
        postChange();
    }

    public void setDate(Date date) {
        _date = date;
        postChange();
    }

    public void setDatePubli(String datePubli) {
        setDate(new Date(Long.parseLong(datePubli)));
    }

    public void setId(String id) {
        _id = id;
        postChange();
    }

    public void setMetaData(String key, Object value) throws MappingException {
        if (!(value instanceof java.io.Serializable)) {
            throw new MappingException(
                    value.getClass()
                    + " n'implemente pas l'interface java.io.Serializable");
        }
        _metaData.put(key, value);
        postChange();
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

    public void setSections(Vector v) {
        _sections = v;
        postChange();
    }

    public void setType(TypePublication type) {
        _type = type;
    }

    public void setVersions(Vector versions) {
        _versions = versions;
    }

    // versions
    public void setWorkflowType(String t) {
        _workflowType = t;
    }

    public String toString() {
        return "type[PUBLICATION] properties["
                + _id
                + ";"
                + _name
                + "] metaData"
                + _metaData
                + " sections"
                + _sections;
    }

}
