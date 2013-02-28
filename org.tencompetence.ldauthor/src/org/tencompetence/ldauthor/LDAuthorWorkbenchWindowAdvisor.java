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
package org.tencompetence.ldauthor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewWindow;

/**
 * WorkbenchWindowAdvisor
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorWorkbenchWindowAdvisor.java,v 1.7 2008/11/22 01:47:39 phillipus Exp $
 */
public class LDAuthorWorkbenchWindowAdvisor
extends WorkbenchWindowAdvisor
{
    private boolean fDoOpenInspector;
    private boolean fDoOpenPreview;
    
    private static String STATE_INSPECTOR_OPEN = "inspector.open"; //$NON-NLS-1$
    private static String STATE_PREVIEW_OPEN = "preview.open"; //$NON-NLS-1$
    
    /**
     * Constructor
     * @param configurer
     */
    public LDAuthorWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new LDAuthorActionBarAdvisor(configurer);
    }
    
    @Override
    public IStatus saveState(IMemento memento) {
        // Save Floating Window states
        if(ViewManager.useFloatingWindows) {
            memento.putString(STATE_INSPECTOR_OPEN, InspectorManager.getInstance().isWindowOpen() ? "true" : null); //$NON-NLS-1$
            
            if(PreviewWindow.isCreated()) {
                memento.putString(STATE_PREVIEW_OPEN, PreviewWindow.getInstance().isOpen() ? "true" : null); //$NON-NLS-1$
            }
        }
        
        return super.saveState(memento);
    }
    
    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        
        if(ViewManager.useFloatingWindows) {
            // Open Floating Window states
            if(fDoOpenInspector) {
                InspectorManager.getInstance().showInspector();
            }
            
            if(fDoOpenPreview) {
                PreviewWindow.getInstance().open();
            }
        }
    }
    
    @Override
    public IStatus restoreState(IMemento memento) {
        if(ViewManager.useFloatingWindows) {
            fDoOpenInspector = memento.getString(STATE_INSPECTOR_OPEN) != null;
            fDoOpenPreview = memento.getString(STATE_PREVIEW_OPEN) != null;
        }
        
        return super.restoreState(memento);
    }
}
