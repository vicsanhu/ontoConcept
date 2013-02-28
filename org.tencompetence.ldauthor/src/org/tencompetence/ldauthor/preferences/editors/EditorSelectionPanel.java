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
package org.tencompetence.ldauthor.preferences.editors;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;



/**
 * Preferences Panel to add external editors
 * 
 * @author Phillip Beauvoir
 * @version $Id: EditorSelectionPanel.java,v 1.2 2008/04/24 10:14:53 phillipus Exp $
 */
public class EditorSelectionPanel extends Composite {

    /**
     * The Table Viewer
     */
    private EditorsTableViewer fTableViewer;
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public EditorSelectionPanel(Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        setLayout(layout);
        
        fTableViewer = createTableViewer(this);
        createButtonBar(this);
    }

    /**
     * Create the Table
     * @param parent
     * @return the Table
     */
    private EditorsTableViewer createTableViewer(Composite parent) {
        EditorsTableViewer tableViewer = new EditorsTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        tableViewer.getControl().setLayoutData(gd);
        return tableViewer;
    }
    
    /**
     * Create the Button bar
     * @param parent
     * @return The Bar
     */
    private Composite createButtonBar(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        client.setLayoutData(gd);

        Button buttonNew = new Button(client, SWT.PUSH);
        buttonNew.setText(Messages.EditorSelectionPanel_0);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonNew.setLayoutData(gd);
        buttonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fTableViewer.addNewEntry();
            }
        });
        
        final Button buttonEdit = new Button(client, SWT.PUSH);
        buttonEdit.setText(Messages.EditorSelectionPanel_1);
        buttonEdit.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonEdit.setLayoutData(gd);
        buttonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fTableViewer.editSelectedEntry();
            }
        });
        
        final Button buttonRemove = new Button(client, SWT.PUSH);
        buttonRemove.setText(Messages.EditorSelectionPanel_2);
        buttonRemove.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonRemove.setLayoutData(gd);
        buttonRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fTableViewer.removeSelectedEntries();
            }
        });

        // Listen to table selections to enable buttons
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                buttonEdit.setEnabled(!selection.isEmpty());
                buttonRemove.setEnabled(!selection.isEmpty());
            }
        });

        return client;
    }

    /**
     * Set the data to that from the Preferences Store
     * @param data
     */
    public void setPreferencesData(String data) {
        fTableViewer.setPreferencesData(data);
    }
    
    /**
     * @return The Preferences data string
     */
    public String getPreferencesData() {
        return fTableViewer.getPreferencesData();
    }
}
