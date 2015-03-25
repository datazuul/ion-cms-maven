package org.nextime.ion.frontoffice.bean;

import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.business.Section;

public class IonStatus {

    private Section currentSection;
    private Publication currentPublication;
    private int currentVersion;
    private boolean isStatic;

    public IonStatus() {
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Section value) {
        currentSection = value;
    }

    /**
     * Returns the currentPublication.
     *
     * @return Publication
     */
    public Publication getCurrentPublication() {
        return currentPublication;
    }

    /**
     * Sets the currentPublication.
     *
     * @param currentPublication The currentPublication to set
     */
    public void setCurrentPublication(Publication currentPublication) {
        this.currentPublication = currentPublication;
    }

    /**
     * Returns the isStatic.
     *
     * @return boolean
     */
    public boolean getIsStatic() {
        return isStatic;
    }

    /**
     * Sets the isStatic.
     *
     * @param isStatic The isStatic to set
     */
    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * Returns the currentVersion.
     *
     * @return int
     */
    public int getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Sets the currentVersion.
     *
     * @param currentVersion The currentVersion to set
     */
    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

}
