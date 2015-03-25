package org.nextime.ion.framework.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.mapping.AccessMode;
import org.jdom.Document;
import org.nextime.ion.framework.business.TypePublication;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;
import org.nextime.ion.framework.logger.Logger;
import org.nextime.ion.framework.mapping.MappingException;
import org.nextime.ion.framework.xml.XML;
import org.nextime.ion.framework.xml.XMLException;

/**
 * Une implementation de TypePublication
 *
 * @author gbort
 * @version 1.7
 */
public class TypePublicationImpl
        extends TypePublication
        implements Persistent {

    public final static String ERROR = "ERROR";
    public final static String OK = "OK";
    private static boolean _cache = Config.getInstance().getDisableTypeCache();
    private static Vector _listeners = new Vector();
    private static Hashtable _modelInstances = new Hashtable();
    private static Hashtable _styleInstances = new Hashtable();

    public static void addListener(WcmListener listener) {
        _listeners.add(listener);
    }

    public static void removeListener(WcmListener listener) {
        _listeners.remove(listener);
    }
    private String _description;
    private String _id;
    private Hashtable _metaData;
    private Document _model;
    private Hashtable _styles;
    private boolean _stylesState = true;

    // pour que la classe soit constructible par castor
    public TypePublicationImpl() {
        _description = "";
        _metaData = new Hashtable();
        _styles = new Hashtable();
    }

    private void doLoadModel() throws MappingException {
        if (_modelInstances.get(getId()) == null || _cache) {
            // get modeles/(id).xml
            File model
                    = new File(
                            Config.getInstance().getTypePublicationDirectory(),
                            "models" + File.separator + getId() + ".xml");
            if (!model.exists() || !model.isFile()) {
                String m
                        = "Le fichier "
                        + Config.getInstance().getTypePublicationDirectory()
                        + File.separator
                        + "modeles"
                        + File.separator
                        + getId()
                        + ".xml est introuvable.";
                Logger.getInstance().error(m, this, new MappingException(m));

                throw new MappingException(m);
            }
            // get XML doc
            Document doc;
            try {
                doc = XML.getInstance().readWithoutValidation(model);
            } catch (XMLException e) {
                String m
                        = "Le fichier "
                        + getId()
                        + ".xml est mal formattx : "
                        + e.getMessage();
                Logger.getInstance().error(m, this, new MappingException(m));
                throw new MappingException(m);
            }
            // build model
            _model = doc;
            _modelInstances.put(getId(), _model);
        }
        _model = (Document) _modelInstances.get(getId());

    }

    private void doLoadStyles() throws MappingException {
        if (_styleInstances.get(getId()) == null || _cache) {
            File stylesPath
                    = new File(
                            Config.getInstance().getTypePublicationDirectory(),
                            "styles" + File.separator + getId());
            if (!stylesPath.exists() || !stylesPath.isDirectory()) {
                String m
                        = "Le repertoire "
                        + Config.getInstance().getTypePublicationDirectory()
                        + File.separator
                        + "styles"
                        + File.separator
                        + getId()
                        + " devant contenir les styles est introuvable.";
                Logger.getInstance().error(m, this, new MappingException(m));
                _stylesState = false;
                throw new MappingException(m);
            }
            File[] files = stylesPath.listFiles();
            Hashtable ts = new Hashtable();
            for (int i = 0; i < files.length; i++) {
                try {
                    String name
                            = files[i].getName().substring(
                                    0,
                                    files[i].getName().indexOf("."));
                    int type
                            = files[i].getName().substring(
                                    files[i].getName().indexOf(".") + 1).equals(
                                    "fo")
                                    ? Style.XSL_FO
                                    : Style.XSL;
                    Document doc
                            = XML.getInstance().readWithoutValidation(files[i]);
                    Style style = new Style(type, doc);
                    ts.put(name, style);
                } catch (XMLException e) {
                    _stylesState = false;
                    String m
                            = "La feuille de style "
                            + files[i].getName()
                            + " pour le type "
                            + getId()
                            + " est mal formx : "
                            + e.getMessage()
                            + ". Il ne sera pas chargx.";
                    Logger.getInstance().error(
                            m,
                            this,
                            new MappingException(m));
                }
            }
            _styleInstances.put(getId(), ts);
        }
        _styles = (Hashtable) _styleInstances.get(getId());
    }

    public boolean equals(Object o) {
        try {
            return ((TypePublicationImpl) o).getId().equals(getId());
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

    public Document getModel() {
        return _model;
    }

    public String getModelState() {
        if (_model == null) {
            return ERROR;
        }
        return OK;
    }

    public Style getStyleSheet(String name) {
        return (Style) _styles.get(name);
    }

    public String getStylesState() {
        if (!_stylesState) {
            return ERROR;
        }
        return OK;
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
        return jdoLoad(am.getId());
    }

    public Class jdoLoad(short accessMode) {
        try {
            doLoadModel();
        } catch (Exception e) {
            Logger.getInstance().log(
                    "Le type de publication "
                    + getId()
                    + " sera probablement incorrect.",
                    this);
        }
        try {
            doLoadStyles();
        } catch (Exception e) {
            Logger.getInstance().log(
                    "Le type de publication "
                    + getId()
                    + " sera probablement incorrect.",
                    this);
        }
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

    public Vector listStyles() {
        Vector v = new Vector();
        Enumeration keys = _styles.keys();
        while (keys.hasMoreElements()) {
            v.add(keys.nextElement());
        }
        return v;
    }

    public void removeMetaData(String key) {
        _metaData.remove(key);
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

    public String toString() {
        return "type[TypePublication] properties["
                + _id
                + ";"
                + _description
                + "] metaData"
                + _metaData;
    }

}
