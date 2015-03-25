package org.nextime.ion.framework.event;

public class WcmEvent {

    private Object _source;
    private Class _objectType;
    private String _id;

    public WcmEvent(Object source, String id) {
        _source = source;
        _objectType = source.getClass();
        _id = id;
    }

    public Object getSource() {
        return _source;
    }

    public Class getObjectType() {
        return _objectType;
    }

    public String getId() {
        return _id;
    }
}
