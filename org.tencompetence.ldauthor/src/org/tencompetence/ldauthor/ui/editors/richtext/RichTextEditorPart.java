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
package org.tencompetence.ldauthor.ui.editors.richtext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epf.richtext.IRichTextEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.FileEditorInput;


/**
 * Rich Text Editor Part showing RTE in the full Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: RichTextEditorPart.java,v 1.6 2009/05/07 11:29:38 phillipus Exp $
 */
public class RichTextEditorPart extends EditorPart {
    
    public static final String ID = "org.tencompetence.rte.RichTextEditorPart"; //$NON-NLS-1$

    /**
     * Form
     */
    private Form fForm;
    
    /**
     * The actual Rich Text Editor component
     */
    private IRichTextEditor fEditor;
    
    /**
     * Dirty flag
     */
    private boolean isDirty;

    
    
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public void createPartControl(Composite parent) {
        fForm = AppFormToolkit.getInstance().createForm(parent);
        AppFormToolkit.getInstance().paintBordersFor(fForm.getBody());
        
        /*
         * Form Body is main composite
         */
        Composite body = fForm.getBody();
        body.setLayout(new GridLayout());
        
        // File and Base Path
        File file = ((FileEditorInput)getEditorInput()).getFile();
        String basePath = file.getParent();
        
        fEditor = new RTE(body, SWT.NULL, getEditorSite(), basePath);
        
        // Border
        fEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        
        // Listen to changes
        fEditor.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });
        
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fEditor.setLayoutData(gd);
        
        // Part name
        setPartName(getEditorInput().getName());
        
        try {
            openFile(file);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        // Hack for Linux + Eclipse 3.4 not registering initial key events
        if(Platform.getOS().equals(Platform.OS_LINUX)) {
            fEditor.setModified(true);
        }
    }
    
    /**
     * Open the file for editing
     * @param file
     * @throws IOException
     */
    private void openFile(File file) throws IOException {
        if(!file.exists() || !file.canRead()) {
            return;
        }

        int BUFFER_SIZE = 4096;
        
        FileInputStream fis = new FileInputStream(file);
        // TODO Should we do this using UTF-8?
        //InputStreamReader isr = new InputStreamReader(fis, "UTF-8"); //$NON-NLS-1$
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        StringBuffer textBuffer = new StringBuffer(BUFFER_SIZE);
        char[] buffer = new char[BUFFER_SIZE];
        int charsRead;
        while ((charsRead = br.read(buffer, 0, BUFFER_SIZE)) > 0) {
            textBuffer.append(buffer, 0, charsRead);
        }

        fEditor.setText(textBuffer.toString());

        if(br != null) {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
        String text = fEditor.getText();
        File file = ((FileEditorInput)getEditorInput()).getFile();
        
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            // TODO Should we do this using UTF-8? It's saved as ANSI by default without specifying
            OutputStreamWriter isw = new OutputStreamWriter(fos); 
            //OutputStreamWriter isw = new OutputStreamWriter(fos, "UTF-8"); //$NON-NLS-1$
            bw = new BufferedWriter(isw);
            bw.write(text);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        if(bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
            }
        }
        
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * Sets the dirty state of this editor.
     * 
     * <p>
     * An event will be fired immediately if the new state is different than the
     * current one.
     * 
     * @param dirty the new dirty state to set
     */
    protected void setDirty(boolean dirty) {
        if(isDirty != dirty) {
            isDirty = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }
    
    @Override
    public boolean isDirty() {
        return isDirty;
    }
    
    @Override
    public void setFocus() {
        fEditor.setFocus();
    }


}
