/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ui.views.ViewManager;


/**
 * InspectorManager
 * 
 * @author Phillip Beauvoir
 * @version $Id: InspectorManager.java,v 1.13 2009/05/19 18:21:09 phillipus Exp $
 */
public class InspectorManager implements IPartListener {
    
    private ObjectMapper fObjectMapper = new ObjectMapper();
    private InspectorViewLabelProvider fLabelProvider = new InspectorViewLabelProvider();
    
    private IInspectorProvider fCurrentPart;
    private Object fElement;
    
    private static InspectorManager fInstance = new InspectorManager();
    
    private InspectorWindow fInspectorWindow;
    
    private boolean fEnabled = true;
    
    public static InspectorManager getInstance() {
        return fInstance;
    }
    
    private InspectorManager() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getPartService().addPartListener(this);
    }
    
    public void showInspector() {
        checkUseOneMode();
        
        // Floating Window
        if(ViewManager.useFloatingWindows) {
            getInspectorWindow().open();
            setInput(fCurrentPart, fElement);
            return;
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        InspectorView viewPart = (InspectorView)page.findView(InspectorView.ID);
        
        if(viewPart != null) {
        	page.activate(viewPart);
        }
        else {
            try {
                viewPart = (InspectorView)page.showView(InspectorView.ID);
                // viewPart = (InspectorView)page.showView(InspectorView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
                setInput(fCurrentPart, fElement);
            }
            catch(PartInitException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void toggleInspector() {
        // Floating Window
        if(ViewManager.useFloatingWindows) {
            if(isWindowOpen()) {
                getInspectorWindow().close();
            }
            else {
                showInspector();
            }
            
            return;
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart part = page.findView(InspectorView.ID);
        if(part != null) {
            page.hideView(part);
        }
        else {
            showInspector();
        }
    }

    public void setInput(IInspectorProvider part, Object element) {
        if(!fEnabled) {
            return;
        }
        
        fCurrentPart = part;
        fElement = element;
        
        // Floater
        if(ViewManager.useFloatingWindows) {
            setInputWithWindow(part, element);
        }
        else {
            setInputWithView(part, element);
        }
    }
    
    private void setInputWithView(IInspectorProvider part, Object element) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if(page == null) {
            return;
        }
        
        // Is Inspector showing?
        InspectorView inspectorView = (InspectorView)page.findView(InspectorView.ID);
        if(inspectorView == null) {
            return;
        }
        
        // Set main title
        inspectorView.setMainTitle(fLabelProvider.getText(element));
        
        // Map the element after getting title
        element = fObjectMapper.mapObject(element);

        // Update Page
        inspectorView.setInput(part, element);
    }
    
    private void setInputWithWindow(IInspectorProvider part, Object element) {
        if(!isWindowOpen()) {
            return;
        }
        
        // Set main title
        getInspectorWindow().setMainTitle(fLabelProvider.getText(element));
        
        // Map the element after getting title
        element = fObjectMapper.mapObject(element);
        
        // Update Page
        getInspectorWindow().setInput(part, element);
    }        
    
    public Object getInput() {
        return fElement;
    }

    /**
     * Disable selection handling
     */
    public void disable() {
        fEnabled = false;
    }

    /**
     * Enable selection handling
     */
    public void enable() {
        fEnabled = true;
    }

    public boolean isWindowOpen() {
        if(fInspectorWindow == null) {
            return false;
        }
        return getInspectorWindow().isOpen();
    }
    
    private InspectorWindow getInspectorWindow() {
        if(fInspectorWindow == null) {
            fInspectorWindow = new InspectorWindow();
        }
        return fInspectorWindow;
    }
    
    /**
     * Ensure that we only use one window
     */
    private void checkUseOneMode() {
        if(ViewManager.useFloatingWindows) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            InspectorView viewPart = (InspectorView)page.findView(InspectorView.ID);
            if(viewPart != null) {
                page.hideView(viewPart);
            }
        }
        else if(isWindowOpen()) {
            getInspectorWindow().close();
        }
    }
    
    // =================================================================================
    //                       PART LISTENER
    // =================================================================================

    public void partActivated(IWorkbenchPart part) {
    }

    public void partBroughtToTop(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        if(part == fCurrentPart) {
            setInput(fCurrentPart, null);
        }
        
        // If an Editor is closed that has same LD Model but may not be the same part
        if(part instanceof IInspectorProvider && fCurrentPart != null && part.getAdapter(ILDModel.class) == fCurrentPart.getAdapter(ILDModel.class)) {
            setInput((IInspectorProvider)part, null);
        }
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
    }
}
