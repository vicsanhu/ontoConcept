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
package org.tencompetence.ldauthor.ui.views.inspector.item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.StackComposite;

/**
 * ItemModel Editor Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemModelComposite.java,v 1.8 2009/06/15 12:58:41 phillipus Exp $
 */
public class ItemModelComposite
extends StackComposite {

    private ItemModelViewer fItemViewer;
    
    private ItemModelEditor fItemEditor;
    
    private IItemTypeContainer fItemTypeContainer;
    
    public ItemModelComposite(Composite parent) {
        super(parent, SWT.NONE);
        setup();
    }
    
    public void setItemModel(IItemTypeContainer itemTypeContainer) {
        fItemViewer.setModel(itemTypeContainer);
        fItemTypeContainer = itemTypeContainer;
        
        if(fItemEditor != null) {
            fItemEditor.setItemModel(itemTypeContainer);
        }
    }

    private void setup() {
        AppFormToolkit.getInstance().paintBordersFor(this);
        AppFormToolkit.getInstance().adapt(this);

        fItemViewer = new ItemModelViewer(this, SWT.NONE);
        AppFormToolkit.getInstance().adapt(fItemViewer);
        fItemViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        showViewer();
    }

    public void showViewer() {
    	fItemViewer.refresh(); // Reflect any changes in the Viewer
        setControl(fItemViewer);
    }
    
    public void showEditor() {
        setControl(getItemEditor());
    }
    
    public ItemModelViewer getItemViewer() {
        return fItemViewer;
    }
    
    public ItemModelEditor getItemEditor() {
        // Lazy...
        if(fItemEditor == null) {
            fItemEditor = new ItemModelEditor(this, SWT.NONE);
            AppFormToolkit.getInstance().adapt(fItemEditor);
            fItemEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            
            fItemEditor.setItemModel(fItemTypeContainer);
        }
        
        return fItemEditor;
    }
}