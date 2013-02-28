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

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * Show Preview in a Popup dialog
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreviewPopup.java,v 1.2 2008/11/20 17:41:02 phillipus Exp $
 */
public class PreviewPopup extends PopupDialog {
    
    private static final String DIALOG_SETTINGS_SECTION = "PreviewPopupDialogSettings"; //$NON-NLS-1$
    
    private Composite fComposite;
    
    private ILDEditorPart fLDEditor;
    
    private Object fObject;
    
    public PreviewPopup(Shell parent, ILDEditorPart ldEditor, Object object) {
        super(parent, SWT.RESIZE, true, true, false, false, false, null, null);
        fLDEditor = ldEditor;
        fObject = object;
    }
    
    @Override
    protected Control createContents(Composite parent) {
        getShell().setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        initializeBounds();
        return createDialogArea(parent);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        fComposite = (Composite) super.createDialogArea(parent);

        PreviewFactory fPreviewFactory = new PreviewFactory();
        ILDPreview preview = fPreviewFactory.getPreview(fObject, fComposite);
        if(preview != null) {
            preview.update(fLDEditor, fObject);
        }

        return fComposite;
    }

    @Override
    protected Point getInitialLocation(Point size) {
        return getShell().getDisplay().getCursorLocation();
    }

    @Override
    protected Point getDefaultSize() {
        return new Point(600, 250);
    }
    
    @Override
    protected Control getFocusControl() {
        return fComposite;
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
}
