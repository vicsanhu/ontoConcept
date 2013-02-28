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
package org.tencompetence.ldauthor.ui.views.inspector;

import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;


/**
 * Inspector View Label Provider
 * 
 * @author Phillip Beauvoir
 * @version $Id: InspectorViewLabelProvider.java,v 1.2 2009/05/19 18:21:09 phillipus Exp $
 */
public class InspectorViewLabelProvider {

    public Image getImage(Object element) {
        return null;
    }

    public String getText(Object element) {
        String s = null;
        
        if(element instanceof EditPart) {
            s = ((EditPart)element).getModel().toString();
        }

        else if(element instanceof ITitle) {
            s = ((ITitle)element).getTitle();
        }

        else if(element instanceof IOrganiserObject) {
            s = ((IOrganiserObject)element).getName();
        }

        else if(element != null) {
            s = element.toString();
        }
        
        if(s == null || "".equals(s)) { //$NON-NLS-1$
            s = " "; //$NON-NLS-1$
        }
        
        return s;
    }
}
