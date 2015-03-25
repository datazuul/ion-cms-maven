package org.nextime.ion.framework.business.impl;

public class DataField {

    private String _name;
    private String _value;

    public DataField(String name, String value) {
        _name = name;
        _value = value;
    }

    public DataField() {
    }

    public String getName() {
        return _name;
    }

    public String getValue() {
        return _value;
    }

    public void setName(String value) {
        _name = value;
    }

    public void setValue(String value) {
        _value = value;
    }
}
