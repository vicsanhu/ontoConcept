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

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * Preferences for the Flyout Palette
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphicalEditorFlyoutPreferences.java,v 1.2 2008/04/24 10:15:03 phillipus Exp $
 */
public class LDGraphicalEditorFlyoutPreferences
implements FlyoutPreferences
{
    /** Preference ID used to persist the palette location. */
    public static final String PALETTE_DOCK_LOCATION = "ldEditorPalette.Location"; //$NON-NLS-1$
    
    /** Preference ID used to persist the palette size. */
    public static final String PALETTE_SIZE = "ldEditorPalette.Size"; //$NON-NLS-1$
    
    /** Preference ID used to persist the flyout palette's state. */
    public static final String PALETTE_STATE = "ldEditorPalette.State"; //$NON-NLS-1$

    private IPreferenceStore getPreferenceStore() {
        return LDAuthorPlugin.getDefault().getPreferenceStore();
    }
    
    public int getDockLocation() {
        int location = getPreferenceStore().getInt(PALETTE_DOCK_LOCATION);
        if(location == 0) {
            location = PositionConstants.EAST;
        }
        return location;
    }
    
    public int getPaletteState() {
        int state = getPreferenceStore().getInt(PALETTE_STATE);
        if(state == 0) {
            state = FlyoutPaletteComposite.STATE_PINNED_OPEN;
        }
        return state;
    }
    
    public int getPaletteWidth() {
        return getPreferenceStore().getInt(PALETTE_SIZE);
    }
    
    public void setDockLocation(int location) {
        getPreferenceStore().setValue(PALETTE_DOCK_LOCATION, location);
    }
    
    public void setPaletteState(int state) {
        getPreferenceStore().setValue(PALETTE_STATE, state);
    }
    
    public void setPaletteWidth(int width) {
        getPreferenceStore().setValue(PALETTE_SIZE, width);
    }
}
