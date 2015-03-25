package org.nextime.ion.frontoffice.objectSelector;

import org.nextime.ion.framework.business.Publication;

public class PublicationResult {

    protected String view;
    protected Publication publication;
    protected int version;

    /**
     * Returns the publication.
     *
     * @return Publication
     */
    public Publication getPublication() {
        return publication;
    }

    /**
     * Returns the version.
     *
     * @return int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the view.
     *
     * @return String
     */
    public String getView() {
        return view;
    }

    /**
     * Sets the publication.
     *
     * @param publication The publication to set
     */
    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    /**
     * Sets the version.
     *
     * @param version The version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Sets the view.
     *
     * @param view The view to set
     */
    public void setView(String view) {
        this.view = view;
    }

}
