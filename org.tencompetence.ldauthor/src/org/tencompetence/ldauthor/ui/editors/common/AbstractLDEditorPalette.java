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
package org.tencompetence.ldauthor.ui.editors.common;


import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;


/**
 * Abstract Palette for Editors 
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDEditorPalette.java,v 1.9 2009/05/19 18:21:05 phillipus Exp $
 */
public abstract class AbstractLDEditorPalette
extends PaletteRoot
implements ILDAuthorPreferenceConstants, IPropertyChangeListener
{
    /**
     * Whether the connection tools stay down
     */
    private boolean isSticky;
    
    private ILDModel fLDModel;
    
    /**
     * Default Constructor
     */
    public AbstractLDEditorPalette(ILDModel ldModel) {
        fLDModel = ldModel;
        
        // Sticky Connection tools
        isSticky = LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(PREFS_CONNECTION_TOOL_STICKY);
        
        /*
         * Listen to Preference changes
         */
        LDAuthorPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }
    
    protected ILDModel getLDModel() {
        return fLDModel;
    }
    
    /**
     * Create a Group of Controls
     */
    protected PaletteGroup createControlGroup() {
        PaletteGroup controls = new PaletteGroup(Messages.AbstractLDEditorPalette_0);
        add(controls);
        
        // The selection tool
        ToolEntry tool = new SelectionToolEntry();
        controls.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        // The marquee selection tool
        controls.add(new MarqueeToolEntry());
        
        return controls;
    }
    
    /**
     * Create a Drawer of Controls
     */
    protected PaletteDrawer createControlDrawer() {
        PaletteDrawer controls = new PaletteDrawer(Messages.AbstractLDEditorPalette_0);
        add(controls);
        
        // The selection tool
        ToolEntry tool = new SelectionToolEntry();
        controls.add(tool);

        // Use selection tool as default entry
        setDefaultEntry(tool);

        // The marquee selection tool
        controls.add(new MarqueeToolEntry());
        
        return controls;
    }

    

    /* (non-Javadoc)
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
        if(PREFS_CONNECTION_TOOL_STICKY.equals(event.getProperty())) {
            /*
             * If Preferences are "OK"-d we get a Boolean value.
             * If Preferences are "Restore Default"-ed then it is a String value!!!
             * See IPreferenceStore.addPropertyChangeListener(IPropertyChangeListener listener)
             * So best to re-query the Preferences.
             */ 
            isSticky = LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(PREFS_CONNECTION_TOOL_STICKY);
        }
    }
    
    /**
     * Clean up
     */
    public void dispose() {
        LDAuthorPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
    }

    /**
     * Over-ride class ConnectionCreationToolEntry so we can set Sticky property
     */
    protected class StickyConnectionCreationToolEntry extends ConnectionCreationToolEntry {

        public StickyConnectionCreationToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
            super(label, shortDesc, factory, iconSmall, iconLarge);
        }
        
        @Override
        public Tool createTool() {
            // Set whether tool stays selected
            ConnectionCreationTool connectionTool = (ConnectionCreationTool) super.createTool();
            connectionTool.setUnloadWhenFinished(!isSticky);
            return connectionTool;
        }

    }
}
