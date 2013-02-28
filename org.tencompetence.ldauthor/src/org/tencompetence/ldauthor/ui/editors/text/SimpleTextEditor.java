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
package org.tencompetence.ldauthor.ui.editors.text;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.tencompetence.ldauthor.ui.editors.FileEditorInput;




/**
 * Simple Text Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: SimpleTextEditor.java,v 1.3 2008/04/24 10:15:28 phillipus Exp $
 */
public class SimpleTextEditor
extends AbstractTextEditor {
    
    public static final String ID = "org.tencompetence.text.SimpleTextEditor"; //$NON-NLS-1$
    
    
    public SimpleTextEditor() {
        super();
        init();
    }
    
    /**
     * Initializes the document provider and source viewer configuration.
     * Called by the constructor. Subclasses may replace this method.
     */
    private void init() {
        configureInsertMode(SMART_INSERT, false);
        setDocumentProvider(new SimpleDocumentProvider());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        // No...
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#performSaveAs(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected void performSaveAs(IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();
        FileEditorInput oldInput = (FileEditorInput)getEditorInput();
        
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        
        dialog.setText(Messages.SimpleTextEditor_0);
        String oldPath = oldInput.getFile().getAbsolutePath();
        dialog.setFileName(oldPath);
        
        /*
         * Ask for new file
         */
        String path = dialog.open();
        if(path != null) {
            File file = new File(path);
            
            IDocumentProvider provider = getDocumentProvider();
            if (provider == null) {
                // editor has programmatically been  closed while the dialog was open
                return;
            }
            
            FileEditorInput newInput = new FileEditorInput(getEditorInput().getName(), file);
            
            boolean success = false;
            try {
                provider.aboutToChange(newInput);
                provider.saveDocument(progressMonitor, newInput, provider.getDocument(oldInput), true);            
                success = true;
            }
            catch (CoreException x) {
                IStatus status = x.getStatus();
                if (status == null || status.getSeverity() != IStatus.CANCEL) {
                    String title = Messages.SimpleTextEditor_0; 
                    String msg = Messages.SimpleTextEditor_1; 
                    
                    if (status != null) {
                        switch (status.getSeverity()) {
                            case IStatus.INFO:
                                MessageDialog.openInformation(shell, title, msg);
                            break;
                            case IStatus.WARNING:
                                MessageDialog.openWarning(shell, title, msg);
                            break;
                            default:
                                MessageDialog.openError(shell, title, msg);
                        }
                    } else {
                        MessageDialog.openError(shell, title, msg);
                    }
                }
            } finally {
                provider.changed(newInput);
                if(success) {
                    setInput(newInput);
                }
            }
            
            if(progressMonitor != null) {
                progressMonitor.setCanceled(!success);
            }
        }
    }
}
