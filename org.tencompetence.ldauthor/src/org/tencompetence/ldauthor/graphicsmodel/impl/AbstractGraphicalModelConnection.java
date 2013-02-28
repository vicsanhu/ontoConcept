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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.utils.StringUtils;

/**
 * Base Model Connection with Properties support
 * 
 * @author Paul Sharples
 * @author Phillip Beauvoir
 * @version $Id: AbstractGraphicalModelConnection.java,v 1.14 2009/05/19 18:21:05 phillipus Exp $
 */
public abstract class AbstractGraphicalModelConnection
implements IGraphicalModelConnection {
    
	/** True, if the connection is attached to its endpoints. */ 
    protected boolean fIsConnected;
			
	/** Connection's source endpoint. */
	private IGraphicalConnectedModelObject fSource;
	
	/** Connection's target endpoint. */
	private IGraphicalConnectedModelObject fTarget;
	
    private String fRelationship = ""; //$NON-NLS-1$
    
	/**
     * Property Descriptors
     */
	private List<PropertyDescriptor> fPropertyDescriptors;

    /**
     * Listeners
     */
	private PropertyChangeSupport fChangeListeners;

    private ILDModel fLDModel;
    
    private HashMap<Object, Object> fDataTable = new HashMap<Object, Object>();
    

    
    protected AbstractGraphicalModelConnection(ILDModel ldModel) {
        fLDModel = ldModel;
        fChangeListeners = new PropertyChangeSupport(this);
        setPropertyDescriptors();
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    /**
     * Setup Property Descriptors
     */
    protected void setPropertyDescriptors() {
        fPropertyDescriptors = new ArrayList<PropertyDescriptor>();
        
        PropertyDescriptor descriptor = new TextPropertyDescriptor(PROPERTY_RELATIONSHIP, Messages.AbstractGraphicalModelConnection_0);
        fPropertyDescriptors.add(descriptor);
        
        descriptor = new TextPropertyDescriptor(PROPERTY_SOURCE_NAME, Messages.AbstractGraphicalModelConnection_1);
        fPropertyDescriptors.add(descriptor);
        
        descriptor = new TextPropertyDescriptor(PROPERTY_TARGET_NAME, Messages.AbstractGraphicalModelConnection_2);
        fPropertyDescriptors.add(descriptor);        
    }   
     
    public String getRelationshipText() {
        return fRelationship;
    }
    
    public void setRelationshipText(String text) {
        String old = fRelationship;
        fRelationship = text;
        firePropertyChange(this, PROPERTY_RELATIONSHIP, old, text);
    }
    
    @Override
    public String toString() {
        return Messages.AbstractGraphicalModelConnection_3;
    }
    
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		 fChangeListeners.addPropertyChangeListener(listener);		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	    fChangeListeners.removePropertyChangeListener(listener);		
	}
	
    public void firePropertyChange(Object source, String propName, Object oldValue, Object newValue) {
        PropertyChangeEvent evt = new PropertyChangeEvent(source, propName, oldValue, newValue);
        fChangeListeners.firePropertyChange(evt);
    }
    
    /**
     * Fire an overall structure change
     * @param prop
     * @param child
     */
    protected void fireStructureChange(String prop, Object child) {
        fChangeListeners.firePropertyChange(prop, null, child);
    }

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] array = new IPropertyDescriptor[fPropertyDescriptors.size()];
        return fPropertyDescriptors.toArray(array);
	}

	/**
	 * Children should override this. 
	 */
	public Object getPropertyValue(Object propertyId) {
		if(PROPERTY_SOURCE_NAME.equals(propertyId)) {			
			return fSource.getName();
		}
		else if(PROPERTY_TARGET_NAME.equals(propertyId)) {			
			return fTarget.getName();
		}
		else if(PROPERTY_RELATIONSHIP.equals(propertyId)) {
		    return getRelationshipText();
		}
		else {
			return null;
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
	}

	public void setPropertyValue(Object propertyId, Object value) {
		if(PROPERTY_TARGET_NAME.equals(propertyId)){			
			fTarget.setName((String)value);
		}
		else if(PROPERTY_SOURCE_NAME.equals(propertyId)){
			fSource.setName((String)value);
		}
		else if(PROPERTY_RELATIONSHIP.equals(propertyId)){
		    setRelationshipText((String)value);
        }
	}
	
	/** 
     * Create a (solid) connection between two distinct shapes.
     * @param source a source endpoint for this connection (non null)
     * @param target a target endpoint for this connection (non null)
     * @throws IllegalArgumentException if any of the parameters are null or source == target
     * @see #setLineStyle(int) 
     */
    public void connect(IGraphicalConnectedModelObject source, IGraphicalConnectedModelObject target, boolean notifyLDModel){
    	reconnect(source, target, notifyLDModel);
    }
    
    /** 
     * Disconnect this connection from the shapes it is attached to.
     */
    public void disconnect(boolean notifyLDModel) {
    	if (fIsConnected) {
    		fSource.removeConnection(this, notifyLDModel);
    		fTarget.removeConnection(this, notifyLDModel);
    		fIsConnected = false;
    	}
    }
	/**
	 * Reconnect to a different source and/or target shape.
	 * The connection will disconnect from its current attachments and reconnect to 
	 * the new source and target. 
	 * @param newSource a new source endpoint for this connection (non null)
	 * @param newTarget a new target endpoint for this connection (non null)
	 * @throws IllegalArgumentException if any of the paramers are null or newSource == newTarget
	 */
	public void reconnect(IGraphicalConnectedModelObject newSource, IGraphicalConnectedModelObject newTarget, boolean notifyLDModel) {
		if (newSource == null || newTarget == null || newSource == newTarget) {
			throw new IllegalArgumentException();
		}
		disconnect(notifyLDModel);
		fSource = newSource;
		fTarget = newTarget;		
		reconnect(notifyLDModel);
	}
	
	/** 
	 * Reconnect this connection. 
	 * The connection will reconnect with the shapes it was previously attached to.
	 */  
	public void reconnect(boolean notifyLDModel) {
		if (!fIsConnected) {
			fSource.addConnection(this, notifyLDModel);
			fTarget.addConnection(this, notifyLDModel);
			fIsConnected = true;
		}
	}
	
	/**
	 * Returns the source endpoint of this connection.
	 * @return a non-null Shape instance
	 */
	public IGraphicalConnectedModelObject getSource() {
		return fSource;
	}

	/**
	 * Returns the target endpoint of this connection.
	 * @return a non-null Shape instance
	 */
	public IGraphicalConnectedModelObject getTarget() {
		return fTarget;
	}
	
    public void setData(Object key, Object value) {
        fDataTable.put(key, value);
    }
    
    public Object getData(Object key) {
        return fDataTable.get(key);
    }

	
    // ============================= JDOM SUPPORT =============================

    public void fromJDOM(Element element) {
        setRelationshipText(element.getAttributeValue("relationship")); //$NON-NLS-1$
    }

    public Element toJDOM() {
        Element connectionElement = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        if(StringUtils.isSet(getRelationshipText())) {
            connectionElement.setAttribute("relationship", getRelationshipText()); //$NON-NLS-1$
        }

        // Add referenced ids of the connected objects
        String id = getSource().getIdentifier();
        connectionElement.setAttribute("src", id); //$NON-NLS-1$
        
        id = getTarget().getIdentifier();
        connectionElement.setAttribute("tgt", id); //$NON-NLS-1$
        
        return connectionElement;
    }

}
