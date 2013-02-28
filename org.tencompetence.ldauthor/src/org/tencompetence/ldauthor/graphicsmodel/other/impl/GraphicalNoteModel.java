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
package org.tencompetence.ldauthor.graphicsmodel.other.impl;

import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.EnvironmentEditorGraphicalModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.impl.AbstractGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.other.IGraphicalNoteModel;



/**
 * Editor Model Note class
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalNoteModel.java,v 1.14 2009/05/19 18:21:05 phillipus Exp $
 */
public class GraphicalNoteModel
extends AbstractGraphicalModelObject
implements IGraphicalNoteModel {

    private String fContent = ""; //$NON-NLS-1$
    
    public GraphicalNoteModel(ILDModel ldModel) {
        super(ldModel);
    }

    @Override
    protected void setPropertyDescriptors() {
        super.setPropertyDescriptors();
        
        PropertyDescriptor descriptor = new TextPropertyDescriptor(PROPERTY_CONTENT, Messages.GraphicalNoteModel_0);
        fPropertyDescriptors.add(descriptor);
    }
    
    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.model.other.impl.INoteModel#setContent(java.lang.String)
     */
    public void setContent(String content) {
        String old = fContent;
        fContent = content;
        firePropertyChange(this, PROPERTY_CONTENT, old, content);
    }
    
    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.model.other.impl.INoteModel#getContent()
     */
    public String getContent() {
        return fContent;
    }
    
    @Override
    public void setPropertyValue(Object propertyId, Object value) {
        if(propertyId == PROPERTY_CONTENT) {
            setContent((String)value);
        }
        else {
            super.setPropertyValue(propertyId, value);
        }
    }
    
    @Override
    public Object getPropertyValue(Object propertyId) {
        if(PROPERTY_CONTENT.equals(propertyId)) {
            return getContent();
        }
        return super.getPropertyValue(propertyId);
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public String getTagName() {
        return EnvironmentEditorGraphicalModelFactory.NOTE;
    }

    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        
        Element content = new Element("content", LDAUTHOR_NAMESPACE_EMBEDDED); //$NON-NLS-1$
        content.setText(fContent);
        element.addContent(content);
                
        return element;
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        Element content = element.getChild("content", LDAUTHOR_NAMESPACE_EMBEDDED); //$NON-NLS-1$
        fContent = content.getText();
    }

    public String getName() {
        return Messages.GraphicalNoteModel_1;
    }

    public void setName(String name) {
    }
}
