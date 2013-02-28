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
package org.tencompetence.ldauthor.graphicsmodel.environment.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jdom.Element;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.ldauthor.graphicsmodel.EnvironmentEditorGraphicalModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.impl.AbstractGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.other.impl.GraphicalNoteModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.editors.environment.figures.IEnvironmentChildFigure;


/**
 * Environment Model class
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalEnvironmentModel.java,v 1.35 2009/05/22 16:35:05 phillipus Exp $
 */
public class GraphicalEnvironmentModel
extends AbstractGraphicalConnectedModelObject
implements IGraphicalEnvironmentModel {
	
    /**
     * Children
     */
    private List<IGraphicalModelObject> fChildren = new ArrayList<IGraphicalModelObject>();

    /**
     * EnvironmentModel
     */
    private IEnvironmentModel fEnvironmentModel;
    
    public GraphicalEnvironmentModel(ILDModel ldModel) {
        super(ldModel);
    }
    
    public void createLDModelObject() {
        setLDModelObject(LDModelFactory.createModelObject(LDModelFactory.ENVIRONMENT, getLDModel()));
    }

    public ILDModelObject getLDModelObject() {
        return fEnvironmentModel;
    }

    public void setLDModelObject(ILDModelObject object) {
        fEnvironmentModel = (IEnvironmentModel)object;
    }
    
    public void setName(String name) {
        if(fEnvironmentModel != null) {
            fEnvironmentModel.setTitle(name);
        }
    }

    public String getName() {
        if(fEnvironmentModel == null) {
            return LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT);
        }
        return fEnvironmentModel.getTitle();
    }
	
    public String getIdentifier() {
        if(fEnvironmentModel != null) {
            return fEnvironmentModel.getIdentifier();
        }
        return "no-id"; //$NON-NLS-1$
    }

    public boolean addChild(IGraphicalModelObject child, boolean notifyLDModel) {
        if(child != null && child != this && !fChildren.contains(child) && fChildren.add(child)) {
            
            /*
             * Add Component to LD Model
             */
            if(notifyLDModel && child instanceof ILDModelObjectOwner) {
                ILDModelObject component = ((ILDModelObjectOwner)child).getLDModelObject();
                fEnvironmentModel.addChild(component);  
            }

            firePropertyChange(this, PROPERTY_CHILD_ADDED, null, child);
            return true;
        }

        return false;
    }

    public void addChildAt(IGraphicalModelObject child, int index, boolean notifyLDModel) {
        if(child != null) {
            fChildren.add(index, child);
            
            /*
             * Add Component to LD Model but *not* using addChildAt()
             */
            if(notifyLDModel && child instanceof ILDModelObjectOwner) {
                ILDModelObject component = ((ILDModelObjectOwner)child).getLDModelObject();
                fEnvironmentModel.addChild(component);  
            }
            
            firePropertyChange(this, PROPERTY_CHILD_ADDED, null, child);
        }
    }

    public boolean removeChild(IGraphicalModelObject child, boolean notifyLDModel) {
        if(child != null && fChildren.remove(child)) {
            firePropertyChange(this, PROPERTY_CHILD_REMOVED, null, child);

            /*
             * Remove Component from LD Model
             */
            if(notifyLDModel && child instanceof ILDModelObjectOwner) {
                ILDModelObject component = ((ILDModelObjectOwner)child).getLDModelObject();
                fEnvironmentModel.removeChild(component);
            }

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

    // ================================== XML JDOM PERSISTENCE =================================

    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        element.setAttribute(LDModelFactory.REF, getIdentifier());
        
        // Children
        for(IGraphicalModelObject child : fChildren) {
            Element e = child.toJDOM();
            element.addContent(e);
        }
        
        return element;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        // Add referenced Environment object
        String refID = element.getAttributeValue(LDModelFactory.REF);
        ILDModelObject ldModelObject = getLDModel().getModelObject(refID);
        setLDModelObject(ldModelObject);
        
        // Children
        for(Object o : element.getChildren()) {
            Element childElement = (Element)o;
            
            // Create an object
            IGraphicalModelObject gfxChild = createChildObject(childElement.getName());

            // Child object
            if(gfxChild != null) {
                gfxChild.fromJDOM(childElement);
                addChild(gfxChild, false);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ENVIRONMENT;
    }
    
    // ============================= CREATE GRAPHICAL COUNTERPARTS =============================
    

    public void reconcile() {
        int x = 10, y = 10;
        
        for(ILDModelObject child : fEnvironmentModel.getChildren()) {
            if(child instanceof IEnvironmentRefModel) {  
                continue;  // Environment-Refs are created in parent class
            }

            IGraphicalModelObject gfxChild = getGraphicalLDChild(child);
            
            if(gfxChild == null) {
                gfxChild = createChildObject(child.getTagName());
                ((ILDModelObjectOwner)gfxChild).setLDModelObject(child);
                gfxChild.setBounds(new Rectangle(x, y, IEnvironmentChildFigure.DEFAULT_WIDTH + 20 , -1));
                addChild(gfxChild, false);
        
                x += 80;
                if(x > 500) {
                    x = 10;
                    y += 100;
                }
            }
        }
    }
    
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

    private IGraphicalModelObject createChildObject(String name) {
        
        if(name.equals(LDModelFactory.LEARNING_OBJECT)) {
            return new GraphicalLearningObjectModel(getLDModel());
        }
        else if(name.equals(LDModelFactory.SEND_MAIL)) {
            return new GraphicalSendMailModel(getLDModel());
        }
        else if(name.equals(LDModelFactory.CONFERENCE)) {
            return new GraphicalConferenceModel(getLDModel());
        }
        else if(name.equals(LDModelFactory.INDEX_SEARCH)) {
            return new GraphicalIndexSearchModel(getLDModel());
        }
        else if(name.equals(LDModelFactory.MONITOR)) {
            return new GraphicalMonitorModel(getLDModel());
        }
        else if(name.equals(EnvironmentEditorGraphicalModelFactory.NOTE)) {
            return new GraphicalNoteModel(getLDModel());
        }
        
        return null;
    }


}
