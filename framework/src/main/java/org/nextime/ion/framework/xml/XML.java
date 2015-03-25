package org.nextime.ion.framework.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;
import org.nextime.ion.framework.config.Config;

/**
 * Fourni les services XML de base pour le framework WCM
 *
 * @author gbort
 * @version 1.0
 */
public class XML {

    private SAXBuilder builder;
    static private XML instance;
    private boolean validation = true;

    public static XML getInstance() {
        if (instance == null) {
            instance = new XML();
        }
        return instance;
    }

    private XML() {
        String saxDriver = Config.getInstance().getSaxDriver();
        validation = Config.getInstance().getXmlValidationState();
        if (saxDriver.equals("_AUTO_")) {
            builder = new SAXBuilder();
        } else {
            builder = new SAXBuilder(saxDriver);
        }
        builder.setValidation(validation);
    }

    public org.jdom.Document read(File file) throws XMLException {
        try {
            return builder.build(file);
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }
    }

    public org.jdom.Document read(InputStream is) throws XMLException {
        try {
            return builder.build(is);
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }
    }

    public org.jdom.Document getDocument(String xml) throws XMLException {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
            return readWithoutValidation(is);
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }
    }

    public org.jdom.Document readWithoutValidation(File file)
            throws XMLException {
        try {
            builder.setValidation(false);
            org.jdom.Document doc = builder.build(file);
            builder.setValidation(validation);
            return doc;
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }
    }

    public org.jdom.Document readWithoutValidation(InputStream is)
            throws XMLException {
        try {
            builder.setValidation(false);
            org.jdom.Document doc = builder.build(is);
            builder.setValidation(validation);
            return doc;
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }
    }

    public String getString(org.jdom.Document doc) {
        XMLOutputter xmlout = new XMLOutputter("\t", true, "ISO-8859-1");
        StringWriter sw = new StringWriter();
        try {
            xmlout.output(doc, sw);
        } catch (IOException e) {
            sw.write("Error : " + e.getMessage());
        }
        return sw.toString();
    }

    public String getStringWithoutFormat(org.jdom.Document doc) {
        XMLOutputter xmlout = new XMLOutputter("", false, "ISO-8859-1");
        StringWriter sw = new StringWriter();
        try {
            xmlout.output(doc, sw);
        } catch (IOException e) {
            sw.write("Error : " + e.getMessage());
        }
        return sw.toString();
    }

    public String getString(org.jdom.Element doc) {
        XMLOutputter xmlout = new XMLOutputter("\t", true, "ISO-8859-1");
        StringWriter sw = new StringWriter();
        try {
            xmlout.output(doc, sw);
        } catch (IOException e) {
            sw.write("Error : " + e.getMessage());
        }
        return sw.toString();
    }

    public String transform(org.jdom.Document xml, org.jdom.Document xsl) {
        try {
            Transformer transformer
                    = TransformerFactory.newInstance().newTransformer(
                            new JDOMSource(xsl));
            StringWriter sW = new java.io.StringWriter();
            transformer.transform(new JDOMSource(xml), new StreamResult(sW));
            return sW.toString();
        } catch (TransformerException e) {
            return "Erreur de transformation : " + e.getMessageAndLocation();
        }
    }

    public String transform(org.jdom.Document xml, org.jdom.Document xsl, Hashtable params) {
        try {
            Transformer transformer
                    = TransformerFactory.newInstance().newTransformer(
                            new JDOMSource(xsl));
            Enumeration keys = params.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement() + "";
                transformer.setParameter(key, params.get(key));
            }
            StringWriter sW = new java.io.StringWriter();
            transformer.transform(new JDOMSource(xml), new StreamResult(sW));
            return sW.toString();
        } catch (TransformerException e) {
            return "Erreur de transformation : " + e.getMessageAndLocation();
        }
    }

}
