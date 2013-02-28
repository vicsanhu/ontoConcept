/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.checker;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * CheckerXMLErrorComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: CheckerXMLErrorComposite.java,v 1.2 2009/06/29 19:17:53 phillipus Exp $
 */
public class CheckerXMLErrorComposite extends ScrolledComposite {
    
    private Composite fClient;

    private Color RED = new Color(Display.getCurrent(), 255, 0, 0);
    private Color GREEN = new Color(Display.getCurrent(), 0, 160, 0);
    
    public CheckerXMLErrorComposite(Composite parent, int style) {
        super(parent, style | SWT.V_SCROLL);
        
        fClient = AppFormToolkit.getInstance().createComposite(this, SWT.NONE);
        setExpandHorizontal(true);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        fClient.setLayoutData(gd);
        setLayoutData(gd);
        
        setContent(fClient);

        GridLayout layout = new GridLayout();
        fClient.setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                RED.dispose();
                GREEN.dispose();
            }
        });
    }

    public void setInput(List<XMLException> errorList) {
        clearContents();
        
        AppFormToolkit.getInstance().createLabel(fClient, Messages.CheckerXMLErrorComposite_0);
        
        if(errorList == null) {
            AppFormToolkit.getInstance().createLabel(fClient, Messages.CheckerXMLErrorComposite_1);
            doLayout();
            return;
        }
        
        if(errorList.isEmpty()) {
            Label label = AppFormToolkit.getInstance().createLabel(fClient, null);
            label.setImage(ImageFactory.getImage(ImageFactory.ICON_GREENTICK));
        }
        
        for(XMLException xmlException : errorList) {
            Composite box = AppFormToolkit.getInstance().createComposite(fClient, SWT.BORDER);
            GridLayout layout = new GridLayout(2, false);
            box.setLayout(layout);
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            gd.heightHint = 60;
            box.setLayoutData(gd);
            
            Label label = AppFormToolkit.getInstance().createLabel(box, xmlException.getType());
            gd = new GridData(SWT.NULL, SWT.CENTER, false, false);
            gd.widthHint = 20;
            label.setLayoutData(gd);
            
            Text text = new Text(box, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
            AppFormToolkit.getInstance().adapt(text, true, true); // Don't want a border
            gd = new GridData(SWT.FILL, SWT.CENTER, true, true);
            text.setLayoutData(gd);
            text.setText(xmlException.getSAXException().getLocalizedMessage());
            
            switch(xmlException.getLevel()) {
                case XMLException.ERROR:
                case XMLException.FATAL_ERROR:
                    label.setImage(ImageFactory.getImage(ImageFactory.ICON_REDCROSS));
                    break;

                default:
                    break;
            }
        }
        
        doLayout();
    }

    private void clearContents() {
        for(Control control : fClient.getChildren()) {
            control.dispose();
        }
    }

    public void doLayout() {
        fClient.pack();
        fClient.layout();
    }
}
