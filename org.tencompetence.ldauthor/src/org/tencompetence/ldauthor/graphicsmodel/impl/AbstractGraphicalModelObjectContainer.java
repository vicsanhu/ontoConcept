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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicsObject;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;


/**
 * This Container type is used for main diagrams that hold child figures
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractGraphicalModelObjectContainer.java,v 1.5 2009/05/19 18:21:05 phillipus Exp $
 */
public abstract class AbstractGraphicalModelObjectContainer
extends AbstractGraphicalModelObject
implements IGraphicalModelObjectContainer {

    /**
     * Children
     */
    private List<IGraphicalModelObject> fChildren = new ArrayList<IGraphicalModelObject>();
    
    protected AbstractGraphicalModelObjectContainer(ILDModel ldModel) {
        super(ldModel);
    }
    
    public boolean addChild(IGraphicalModelObject child, boolean notifyLDModel) {
        if(child != null && child != this && !fChildren.contains(child) && fChildren.add(child)) {
            firePropertyChange(this, PROPERTY_CHILD_ADDED, null, child);
            return true;
        }
        return false;
    }

    public void addChildAt(IGraphicalModelObject child, int index, boolean notifyLDModel) {
        if(child != null) {
            fChildren.add(index, child);
            firePropertyChange(this, PROPERTY_CHILD_ADDED, null, child);
        }
    }

    public boolean removeChild(IGraphicalModelObject child, boolean notifyLDModel) {
        if(child != null && fChildren.remove(child)) {
            firePropertyChange(this, PROPERTY_CHILD_REMOVED, null, child);
            return true;
        }
        return false;
    }
    
    public boolean moveChild(IGraphicalModelObject child, int index, boolean notifyLDModel) {
        if(child != null && fChildren.remove(child)) {
            fChildren.add(index, child);
            firePropertyChange(this, PROPERTY_CHILD_MOVED, null, child);
            return true;
        }
        return false;
    }

    public List<IGraphicalModelObject> getChildren() {
        return fChildren;
    }

    public boolean canDeleteChild(IGraphicalModelObject child) {
        return true;
    }
    
    public boolean hasLDObjectChild(String id) {
        if(id == null) {
            return false;
        }
        
        for(IGraphicalModelObject child : getChildren()) {
            if(child instanceof ILDModelObjectOwner) {
                ILDModelObject ldObject = ((ILDModelObjectOwner)child).getLDModelObject();
                if((ldObject instanceof IIdentifier) && id.equals(((IIdentifier)ldObject).getIdentifier())) {
                    return true;
                }
                else if((ldObject instanceof ILDModelObjectReference) && id.equals(((ILDModelObjectReference)ldObject).getReferenceIdentifier())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get a graphical child object given the LD object it wraps
     * @param object
     * @return
     */
    public IGraphicalModelObject getGraphicalLDChild(ILDModelObject object) {
        for(IGraphicalModelObject gxfObject : getChildren()) {
            if(gxfObject instanceof ILDModelObjectOwner) {
                if(object == ((ILDModelObjectOwner)gxfObject).getLDModelObject()) {
                    return gxfObject;
                }
            }
        }
        return null;
    }
    
    /**
     * Get a graphical connected child object given its identifier
     * @param identifier
     * @return
     */
    protected IGraphicalConnectedModelObject getGraphicalLDChild(String identifier) {
        for(IGraphicalModelObject gxfObject : getChildren()) {
            if(gxfObject instanceof IGraphicalConnectedModelObject) {
                if(identifier.equals(((IGraphicalConnectedModelObject)gxfObject).getIdentifier())) {
                    return (IGraphicalConnectedModelObject)gxfObject;
                }
            }
        }
        return null;
    }

    protected abstract IGraphicsObject createChildObject(String name);

    // ============================= JDOM SUPPORT =============================
    
    @Override
    public void fromJDOM(Element element) {
        /*
         * Read in graphical child information
         */
        for(Object o : element.getChildren()) {
            Element childElement = (Element)o;
            String tag = childElement.getName();
            
            IGraphicsObject gfxObject = createChildObject(tag);
            if(gfxObject != null) {
                gfxObject.fromJDOM(childElement);
                
                // Model object
                if(gfxObject instanceof IGraphicalModelObject) {
                    addChild((IGraphicalModelObject)gfxObject, false);
                }
                
                // Connection
                if(gfxObject instanceof IGraphicalModelConnection) {
                    IGraphicalModelConnection connection = (IGraphicalModelConnection)gfxObject;
                    String srcID = childElement.getAttributeValue("src"); //$NON-NLS-1$
                    String tgtID = childElement.getAttributeValue("tgt"); //$NON-NLS-1$
                    
                    IGraphicalConnectedModelObject objectSrc = getGraphicalLDChild(srcID);
                    IGraphicalConnectedModelObject objectTgt = getGraphicalLDChild(tgtID);

                    if(objectSrc != null && objectTgt != null) {
                        connection.connect(objectSrc, objectTgt, false);
                    }
                    else {
                        System.err.println("ERROR:  Could not locate objects for connection"); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    @Override
    public Element toJDOM() {
        Element element = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        // Children
        for(IGraphicalModelObject child : getChildren()) {
            Element e = child.toJDOM();
            element.addContent(e);
        }
        
        // Connections
        for(IGraphicalModelObject child : getChildren()) {
            if(child instanceof IGraphicalConnectedModelObject) {
                for(IGraphicalModelConnection connection : ((IGraphicalConnectedModelObject)child).getSourceConnections()) {
                    Element e = connection.toJDOM();
                    element.addContent(e);
                }
            }
        }
        
        return element;
    }
}
