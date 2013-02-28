/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.ldauthor.organisermodel.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.jdom.Document;
import org.jdom.Element;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.organisermodel.IOrganiserIndex;
import org.tencompetence.ldauthor.organisermodel.OrganiserModelFactory;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;



/**
 * The Organiser Index for Organiser entries
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserIndex.java,v 1.25 2009/05/19 18:21:09 phillipus Exp $
 */
public class OrganiserIndex
extends AbstractOrganiserContainer
implements IOrganiserIndex {
    
    /**
     * Static instance
     */
    private static OrganiserIndex fInstance;

    /**
     * Listener list
     */
    private PropertyChangeSupport fListeners = new PropertyChangeSupport(this);
    
    /**
     * Flag for needs saving
     */
    private boolean fIsDirty;
    
    /**
     * @return The static instance
     */
    public static OrganiserIndex getInstance() {
        if(fInstance == null) {
            fInstance = new OrganiserIndex();
            fInstance.load();
        }
        return fInstance;
    }
    
    /**
     * Hidden Constructor
     */
    private OrganiserIndex() {
        // Listen to user changing data store and forward on
        LDAuthorPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
                if(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER.equals(event.getProperty())) {
                    getChildren().clear();
                    load();
                    firePropertyChange(this, IOrganiserIndex.PROPERTY_ORGANISER_CHANGED, null, this);
                }
            }
        });
    }

    /**
     * Load the Index
     */
    private void load() {
        File backingFile = getFile();
        if(backingFile.exists()) {
            try {
                Document doc = JDOMXMLUtils.readXMLFile(backingFile);
                Element root = doc.getRootElement();
                fromJDOM(root);
            }
            catch(Exception ex) { // catch all exceptions
                ex.printStackTrace();
                addDefaultEntries();
            }
        }
        else {
            addDefaultEntries();
        }
    }
    
    public synchronized void save() {
        try {
            Document doc = new Document();
            Element root = toJDOM();
            doc.setRootElement(root);
            JDOMXMLUtils.write2XMLFile(doc, getFile());
            fIsDirty = false;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Add some default entries that help the new user
     */
    public void addDefaultEntries() {
        //addChild(new OrganiserFolder("Activities"));
        addChild(new OrganiserFolder(Messages.OrganiserIndex_0));
        addChild(new OrganiserFolder(Messages.OrganiserIndex_1));
        addChild(new OrganiserFolder(Messages.OrganiserIndex_2));
        save();
    }

    public File getFile() {
        return new File(getOrganiserFolder(), "organiser.xml"); //$NON-NLS-1$
    }

    /**
     * The location of the user "Organiser" folder
     */
    public File getOrganiserFolder() {
        return LDAuthorPlugin.getDefault().getUserDataFolder();
    }

    public String getTagName() {
        return OrganiserModelFactory.INDEX;
    }
    
    public void setDirty(boolean dirty) {
        fIsDirty = dirty;
    }
    
    public boolean isDirty() {
        return fIsDirty;
    }

    //========================== Model Listener events  ==========================

    public void addPropertyChangeListener(PropertyChangeListener l) {
        fListeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        fListeners.removePropertyChangeListener(l);
    }
    
    /**
     * Fire a Property Change event
     * @param source The object affected
     * @param prop The property that changed
     * @param oldValue Old Value 
     * @param newValue New Value
     */
    protected void firePropertyChange(Object source, String prop, Object oldValue, Object newValue) {
        fListeners.firePropertyChange(new PropertyChangeEvent(source, prop, oldValue, newValue));
    }

    /**
     * Fire a structural change
     * @param prop The type of change
     * @param child The object affected
     */
    protected void fireStructureChange(String prop, Object child) {
        firePropertyChange(child, prop, null, child);
    }
}
