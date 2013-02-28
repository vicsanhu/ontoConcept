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
package org.tencompetence.ldauthor.ui.views.preview;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.ui.common.AbstractToolWindow;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.ILDEditPart;


/**
 * Floating Window version of Preview - not used!
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreviewWindow.java,v 1.4 2009/05/06 17:53:58 phillipus Exp $
 */
public class PreviewWindow extends AbstractToolWindow 
implements ISelectionListener, IPartListener, IContextProvider {
    
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".preView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".preViewHelp"; //$NON-NLS-1$
    
    private static PreviewWindow fInstance;
    
    public static boolean isCreated() {
        return fInstance != null;
    }

    public static PreviewWindow getInstance() {
        if(fInstance == null) {
            fInstance = new PreviewWindow();
        }
        return fInstance;
    }
    
    private static final String DIALOG_SETTINGS_SECTION = "PreviewWindowSettings"; //$NON-NLS-1$

    private PreviewFactory fPreviewFactory;
    
    private StackComposite fStackComposite;
    
    
    PreviewWindow() {
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.PreviewWindow_0);
    }
    
    @Override
    protected Control createContents(Composite parent) {
        fPreviewFactory = new PreviewFactory();
        
        Composite client = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Stack Composite
        fStackComposite = new StackComposite(client, SWT.NONE);
        fStackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Listen to workbench selections
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getSelectionService().addSelectionListener(this);
        
        // Listen to part selections
        window.getPartService().addPartListener(this);
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // If there is already an active page then process it
        if(window.getActivePage() != null) {
            selectionChanged(window.getActivePage().getActivePart(), window.getSelectionService().getSelection());
        }

        return client;
    }
    
    @Override
    protected Point getDefaultSize() {
        return new Point(700, 650);
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = LDAuthorPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if(section == null) {
            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        } 
        return section;
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(part == null) {
            return;
        }
        
        ILDEditorPart editorPart = (ILDEditorPart)part.getAdapter(ILDEditorPart.class);
        
        if(editorPart == null) {
            // Blank out?
            return;
        }

        if(!(selection instanceof IStructuredSelection)) {
            // Blank out?
            return;
        }
        
        Object object = ((IStructuredSelection)selection).getFirstElement();
        
        if(object == null) {
            // Blank out?
            return;
        }
        
        // GEF Edit part, so get its model object
        if(object instanceof ILDEditPart) {
            object = ((ILDEditPart)object).getModel();
        }
        
        // Object owner
        if(object instanceof ILDModelObjectOwner) {
            object = ((ILDModelObjectOwner)object).getLDModelObject();
        }
        
        // Get appropriate preview
        ILDPreview preview = fPreviewFactory.getPreview(object, fStackComposite);
        
        // Update it
        if(preview != null) {
            fStackComposite.setControl(preview.getClientArea());
            preview.update(editorPart, object);
        }
        else {
            // Blank out?
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getSelectionService().removeSelectionListener(this);
        window.getPartService().removePartListener(this);
    }
    
    // =================================================================================
    //                       PART LISTENER
    // =================================================================================

    public void partActivated(IWorkbenchPart part) {
    }

    public void partBroughtToTop(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        // If there are no more LD editors open then set the context to null
        if(part instanceof ILDMultiPageEditor) {
            for(IEditorReference ref : part.getSite().getPage().getEditorReferences()) {
                IEditorPart editorPart = ref.getEditor(false);
                if(editorPart instanceof ILDMultiPageEditor) {
                    return;
                }
            }
            
            fStackComposite.setControl(null);
        }
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
    }


    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContextChangeMask()
     */
    public int getContextChangeMask() {
        return NONE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
     */
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
     */
    public String getSearchExpression(Object target) {
        return Messages.PreView_0;
    }
}
