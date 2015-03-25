package org.nextime.ion.framework.business.impl;

import org.jdom.Document;

public class Style {

    static final public int XSL = 0;
    static final public int XSL_FO = 1;

    private int _type;
    private Document _doc;

    public Style(int type, Document doc) {
        _type = type;
        _doc = doc;
    }

    public int getType() {
        return _type;
    }

    public Document getDocument() {
        return _doc;
    }

}
