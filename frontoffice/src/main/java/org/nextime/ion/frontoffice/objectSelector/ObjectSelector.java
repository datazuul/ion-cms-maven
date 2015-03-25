package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ObjectSelector {

    public Collection selectObjects(Hashtable params, HttpServletRequest request, HttpServletResponse response) throws SelectException;

}
