package org.nextime.ion.frontoffice.objectSelector;

import org.exolab.castor.jdo.PersistenceException;

public class SelectException extends PersistenceException {

    public SelectException(String e) {
        super(e);
    }

}
