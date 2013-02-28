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
package org.tencompetence.widgetadvert.ui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.jdom.JDOMException;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.widgetadvert.IWidgetAdvertHandler;
import org.tencompetence.widgetadvert.impl.WidgetAdvertHandlerImpl;
import org.tencompetence.widgetadvert.model.IWidgetAdvert;


/**
 * Activate Widget Services
 * 
 * @author Phillip Beauvoir
 * @version $Id: WidgetServiceAdapter.java,v 1.7 2009/05/19 18:21:35 phillipus Exp $
 */
public class WidgetServiceAdapter implements IPartListener {

    private static WidgetServiceAdapter fInstance;
    
    private boolean fStarted;
    
    private List<IWidgetAdvert> fWidgets;
    
    /**
     * Keep a list of entries
     */
    private HashMap<ILDMultiPageEditor, EditorPaletteInfo> fDoneList = new HashMap<ILDMultiPageEditor, EditorPaletteInfo>();
    
    public static WidgetServiceAdapter getInstance() {
        if(fInstance == null) {
            fInstance = new WidgetServiceAdapter();
        }
        return fInstance;
    }
    
    private WidgetServiceAdapter() {
    }
    
    public void start() {
        if(fStarted) {
            return;
        }
        
        fStarted = true;

        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        
        // Listen to part selections
        window.getPartService().addPartListener(this);
    }
    
    public void stop() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getPartService().removePartListener(this);
        fStarted = false;
    }
    
    /**
     * Query the Widget Server for its widgets
     * 
     * @throws JDOMException
     * @throws IOException
     * @throws Exception
     */
    public void queryServer() {
        QueryWidgetsDialog dlg = new QueryWidgetsDialog(Display.getCurrent().getActiveShell());
        if(dlg.open() == IDialogConstants.CANCEL_ID) {
            return;
        }
        
        String fServerLocation = dlg.getServerURLPath();
        
        IWidgetAdvertHandler fWidgetModel = new WidgetAdvertHandlerImpl();
        fWidgetModel.setServerLocation(fServerLocation);
        
        try {
            fWidgets = fWidgetModel.getwidgets(true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.WidgetServiceAdapter_0, ex.getMessage());
            return;
        }
        
        start();

        // If there are already active pages...
        checkActiveWindows();

        MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                Messages.WidgetServiceAdapter_0,
                fWidgets.size() + Messages.WidgetServiceAdapter_1);
    }
    
    /**
     * If there are editor windows open, then update them
     */
    private void checkActiveWindows() {
        for(IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
            for(IWorkbenchPage page : window.getPages()) {
                for(IEditorReference editorRef : page.getEditorReferences()) {
                    IEditorPart part = editorRef.getEditor(false);
                    if(part instanceof ILDMultiPageEditor) {
                        addToEditorPalette((ILDMultiPageEditor)part);
                    }
                }
            }
        }
    }
    
    /**
     * Add the Widgets to the Environment Editor Palette
     * 
     * @param editor
     */
    private void addToEditorPalette(ILDMultiPageEditor editor) {
        if(fWidgets == null) {
            return;
        }
        
        PaletteRoot root = editor.getEnvironmentEditorPage().getPaletteRoot();
        
        // Remove old palette entry if we have one and add a fresh one
        EditorPaletteInfo info = fDoneList.get(editor);
        if(info != null) {
            root.remove(info.fDrawerComponents);
        }
        
        // Add new Entries
        PaletteDrawer drawerComponents = new PaletteDrawer(Messages.WidgetServiceAdapter_2);
        root.add(drawerComponents);
        
        for(IWidgetAdvert widget : fWidgets) {
            ImageDescriptor desc = null;
            
            try {
                URL url = new URL(widget.getWidgetIconPath());
                desc = ImageDescriptor.createFromURL(url);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            
            String title = widget.getWidgetTitle();
            
            PaletteEntry entry = new CombinedTemplateCreationEntry(title,
                    Messages.WidgetServiceAdapter_3,
                    new GraphicalModelFactoryExtension(LDModelFactory.CONFERENCE, (ILDModel)editor.getAdapter(ILDModel.class), widget),
                    desc,
                    desc);
            drawerComponents.add(entry);
        }
        
        fDoneList.put(editor, new EditorPaletteInfo(editor, drawerComponents));
    }

    public void partActivated(IWorkbenchPart part) {
    }

    public void partBroughtToTop(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        if(part instanceof ILDMultiPageEditor) {
            fDoneList.remove(part);
        }
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
        if(part instanceof ILDMultiPageEditor) {
            addToEditorPalette((ILDMultiPageEditor)part);
        }
    }
    
    
    class EditorPaletteInfo {
        ILDMultiPageEditor fEditor;
        PaletteDrawer fDrawerComponents;
        
        public EditorPaletteInfo(ILDMultiPageEditor editor, PaletteDrawer drawerComponents) {
            fEditor = editor;
            fDrawerComponents = drawerComponents;
        }

    }
}
