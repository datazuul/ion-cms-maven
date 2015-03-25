package org.nextime.ion.framework.event;

public interface WcmListener {

    public void objectCreated(WcmEvent event);

    public void objectModified(WcmEvent event);

    public void objectDeleted(WcmEvent event);
}
