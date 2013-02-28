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
package org.tencompetence.ldauthor.graphicsmodel.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;

/**
 * Base Graphical Model Object
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractGraphicalModelObject.java,v 1.16 2009/05/19 18:21:05 phillipus Exp $
 */
public abstract class AbstractGraphicalModelObject
implements IGraphicalModelObject {
    
    private HashMap<Object, Object> fDataTable = new HashMap<Object, Object>();
    
    /**
     * Property Descriptors
     */
    protected List<PropertyDescriptor> fPropertyDescriptors;
    
    /**
     * Property Change Listeners
     */
    protected PropertyChangeSupport fChangelisteners;
    
    private ILDModel fLDModel;

    private Rectangle fBounds = new Rectangle(0, 0, -1, -1);
    
    
    protected AbstractGraphicalModelObject(ILDModel ldModel) {
        fChangelisteners = new PropertyChangeSupport(this);
        fPropertyDescriptors = new ArrayList<PropertyDescriptor>();
        setPropertyDescriptors();

        fLDModel = ldModel;
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    /**
     * Setup Property Descriptors
     */
    protected void setPropertyDescriptors() {
        PropertyDescriptor descriptor = new TextPropertyDescriptor(PROPERTY_XPOS, "X"); //$NON-NLS-1$
        descriptor.setValidator(new NumberPropertyValidator());
        fPropertyDescriptors.add(descriptor);
        
        descriptor = new TextPropertyDescriptor(PROPERTY_YPOS, "Y"); //$NON-NLS-1$
        descriptor.setValidator(new NumberPropertyValidator());
        fPropertyDescriptors.add(descriptor);
        
        descriptor = new TextPropertyDescriptor(PROPERTY_WIDTH, Messages.AbstractGraphicalModelObject_0);
        descriptor.setValidator(new NumberPropertyValidator());
        fPropertyDescriptors.add(descriptor);
        
        descriptor = new TextPropertyDescriptor(PROPERTY_HEIGHT, Messages.AbstractGraphicalModelObject_1);
        descriptor.setValidator(new NumberPropertyValidator());
        fPropertyDescriptors.add(descriptor);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] array = new IPropertyDescriptor[fPropertyDescriptors.size()];
        return fPropertyDescriptors.toArray(array);
    }

    public Object getPropertyValue(Object propertyId) {
        if(PROPERTY_XPOS.equals(propertyId)) {
            return Integer.toString(getBounds().x);
        }
        if(PROPERTY_YPOS.equals(propertyId)) {
            return Integer.toString(getBounds().y);
        }
        if(PROPERTY_HEIGHT.equals(propertyId)) {
            return Integer.toString(getBounds().height);
        }
        if(PROPERTY_WIDTH.equals(propertyId)) {
            return Integer.toString(getBounds().width);
        }
        return null;
    }
 
    public void setPropertyValue(Object propertyId, Object value) {
        if(PROPERTY_XPOS.equals(propertyId)) {
            int x = Integer.parseInt((String)value);
            Rectangle bounds = getBounds().getCopy();
            bounds.x = x;
            setBounds(bounds);
        }
        else if(PROPERTY_YPOS.equals(propertyId)) {
            int y = Integer.parseInt((String)value);
            Rectangle bounds = getBounds().getCopy();
            bounds.y = y;
            setBounds(bounds);
        }
        else if(PROPERTY_HEIGHT.equals(propertyId)) {
            int height = Integer.parseInt((String)value);
            Rectangle bounds = getBounds().getCopy();
            bounds.height = height;
            setBounds(bounds);
        }
        else if(PROPERTY_WIDTH.equals(propertyId)) {
            int width = Integer.parseInt((String)value);
            Rectangle bounds = getBounds().getCopy();
            bounds.width = width;
            setBounds(bounds);
        }
    }
    
    /**
     * Children should override this. The default implementation returns false.
     */
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
     * Children should override this. The default implementation does nothing.
     */
    public void resetPropertyValue(Object id) {
        // do nothing
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        fChangelisteners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        fChangelisteners.removePropertyChangeListener(l);
    }

    public void firePropertyChange(Object source, String propName, Object oldValue, Object newValue) {
        PropertyChangeEvent evt = new PropertyChangeEvent(source, propName, oldValue, newValue);
        fChangelisteners.firePropertyChange(evt);
    }

    public void setBounds(Rectangle bounds) {
        Rectangle old = fBounds;
        fBounds = bounds;
        firePropertyChange(this, PROPERTY_BOUNDS, old, fBounds);
    }
    
    public Rectangle getBounds() {
        return fBounds;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    public Object getEditableValue() {
        return this;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public void setData(Object key, Object value) {
        fDataTable.put(key, value);
    }
    
    public Object getData(Object key) {
        return fDataTable.get(key);
    }

    /**
     * Class to validate number input
     */
    public static class NumberPropertyValidator implements ICellEditorValidator {
        public String isValid(Object value) {
            int intValue = -1;
            try {
                intValue = Integer.parseInt((String) value);
            } catch (NumberFormatException exc) {
                return "Not a number"; //$NON-NLS-1$
            }
            return (intValue >= 0) ? null : "Value must be >=  0"; //$NON-NLS-1$
        }
    }
    
    // ================================== XML JDOM PERSISTENCE =================================
    
    public Element toJDOM() {
        Element element = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        Element bounds = new Element("bounds", LDAUTHOR_NAMESPACE_EMBEDDED); //$NON-NLS-1$
        bounds.setAttribute("x", "" + fBounds.x); //$NON-NLS-1$ //$NON-NLS-2$
        bounds.setAttribute("y", "" + fBounds.y); //$NON-NLS-1$ //$NON-NLS-2$
        bounds.setAttribute("width", "" + fBounds.width); //$NON-NLS-1$ //$NON-NLS-2$
        bounds.setAttribute("height", "" + fBounds.height); //$NON-NLS-1$ //$NON-NLS-2$
        element.addContent(bounds);
        
        return element;
    }
    
    public void fromJDOM(Element element) {
        Element bounds = element.getChild("bounds", LDAUTHOR_NAMESPACE_EMBEDDED); //$NON-NLS-1$
        
        try {
            fBounds.x = bounds.getAttribute("x").getIntValue(); //$NON-NLS-1$
            fBounds.y = bounds.getAttribute("y").getIntValue(); //$NON-NLS-1$
            fBounds.width = bounds.getAttribute("width").getIntValue(); //$NON-NLS-1$
            fBounds.height = bounds.getAttribute("height").getIntValue(); //$NON-NLS-1$
        }
        catch(DataConversionException ex) {
            ex.printStackTrace();
        }
    }
}
