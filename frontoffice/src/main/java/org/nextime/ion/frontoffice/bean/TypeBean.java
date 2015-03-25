package org.nextime.ion.frontoffice.bean;

import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.struts.digester.Digester;

public class TypeBean {

    protected String _name;
    protected String _template;
    protected String _cache;
    protected String _jsp;
    protected String _description;
    protected Vector _properties = new Vector();
    protected Vector _publicationsTypes = new Vector();

    protected static Vector _types = new Vector();

    public String getName() {
        return _name;
    }

    public String getJsp() {
        return _jsp;
    }

    public String getTemplate() {
        return _template;
    }

    public String getCache() {
        return _cache;
    }

    public void setCache(String value) {
        _cache = value;
    }

    public void setJsp(String value) {
        _jsp = value;
    }

    protected static TypeBean getBean(String template) {
        for (int i = 0; i < _types.size(); i++) {
            if (((TypeBean) _types.get(i)).getTemplate().equals(template)) {
                return (TypeBean) _types.get(i);
            }
        }
        return null;
    }

    public String getDescription() {
        return _description;
    }

    public Vector getPublicationTypes() {
        return _publicationsTypes;
    }

    public void setPublicationTypes(String types) {
        StringTokenizer tokenizer = new StringTokenizer(types, ", ");
        while (tokenizer.hasMoreTokens()) {
            _publicationsTypes.add(tokenizer.nextToken());
        }
    }

    public void setDescription(String description) {
        _description = description;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setTemplate(String template) {
        _template = template;
    }

    public Vector getProperties() {
        return _properties;
    }

    public PropertyBean getProperty(String name) {
        for (int i = 0; i < _properties.size(); i++) {
            PropertyBean p = (PropertyBean) _properties.get(i);
            if (name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    public int getPropertiesNumber() {
        return _properties.size();
    }

    public void addProperty(PropertyBean p) {
        _properties.add(p);
    }

    public Vector getItems() {
        return _types;
    }

    public static void addBean(TypeBean bean) {
        _types.add(bean);
    }

    protected static TypeBean parse(InputStream in) throws Exception {
        TypeBean bean = new TypeBean();
        Digester digester = new Digester();
        digester.push(bean);
        digester.setValidating(false);
        digester.addObjectCreate("templates-description/template", "org.nextime.ion.frontoffice.bean.TypeBean");
        digester.addSetProperties("templates-description/template");
        digester.addSetNext("templates-description/template", "addBean");
        digester.addCallMethod("templates-description/template/description", "setDescription", 0);
        digester.addCallMethod("templates-description/template/publication-types", "setPublicationTypes", 0);
        digester.addObjectCreate("templates-description/template/property", "org.nextime.ion.frontoffice.bean.PropertyBean");
        digester.addSetProperties("templates-description/template/property");
        digester.addSetTop("templates-description/template/property", "setSectionType");
        digester.parse(in);
        return bean;
    }
}
