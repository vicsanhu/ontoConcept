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
package org.tencompetence.ldauthor.ui.common;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;


/**
 * Singleton FormToolkit for the application
 * 
 * @author Phillip Beauvoir
 * @version $Id: AppFormToolkit.java,v 1.7 2009/06/15 19:40:48 phillipus Exp $
 */
public class AppFormToolkit extends FormToolkit {
    
    private static AppFormToolkit fInstance;
    
    public static final String COLOR_BLUE = "blue"; //$NON-NLS-1$
    
    public static AppFormToolkit getInstance() {
        if(fInstance == null) {
            fInstance = new AppFormToolkit(Display.getDefault());
        }
        return fInstance;
    }
    
    protected AppFormToolkit(Display display) {
        super(Display.getDefault());
        
        // COLORS
        getColors().createColor(COLOR_BLUE, 5, 55, 127);
        
        // FONTS
        Font font = JFaceResources.getDefaultFont();
        FontData[] fontdata = font.getFontData();
        fontdata[0].setHeight(fontdata[0].getHeight() + 2);
        JFaceResources.getFontRegistry().put("headerlabelfont", fontdata); //$NON-NLS-1$
    }

   /**
     * Create a blue label
     * 
     * @param parent
     * @param text
     * @param style
     * @return
     */
    @Override
    public Label createLabel(Composite parent, String text, int style) {
        Label label = super.createLabel(parent, text, style);
        Color color = getColors().getColor("blue"); //$NON-NLS-1$
        label.setForeground(color);
        return label;
    }
    
    /**
     * Create a larger blue label
     * 
     * @param parent
     * @param text
     * @param style
     * @return
     */
    public Label createHeaderLabel(Composite parent, String text, int style) {
        Label label = createLabel(parent, text, style);
        
        Font font = JFaceResources.getFont("headerlabelfont"); //$NON-NLS-1$
        label.setFont(font);
        return label;
    }

}
