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

import org.eclipse.draw2d.geometry.Rectangle;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.ldauthor.graphicsmodel.EnvironmentEditorGraphicalModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicsObject;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentsModel;
import org.tencompetence.ldauthor.graphicsmodel.impl.AbstractGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.other.impl.GraphicalNoteModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;


/**
 * Environments Model class
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalEnvironmentsModel.java,v 1.25 2009/05/22 16:35:05 phillipus Exp $
 */
public class GraphicalEnvironmentsModel
extends AbstractGraphicalModelObjectContainer
implements IGraphicalEnvironmentsModel
{

    public GraphicalEnvironmentsModel(ILDModel ldModel) {
        super(ldModel);
    }

    @Override
    public boolean addChild(IGraphicalModelObject child, boolean notifyLDModel) {
        /*
         * Add Environment to LD Model
         */
        if(notifyLDModel && child instanceof IGraphicalEnvironmentModel) {
            ILDModelObject env = ((IGraphicalEnvironmentModel)child).getLDModelObject();
            getLDModel().getEnvironmentsModel().addChild(env);  
        }
        
        return super.addChild(child, notifyLDModel);
    }

    @Override
    public void addChildAt(IGraphicalModelObject child, int index, boolean notifyLDModel) {
        /*
         * Add Environment to LD Model
         */
        if(notifyLDModel && child instanceof IGraphicalEnvironmentModel) {
            ILDModelObject env = ((IGraphicalEnvironmentModel)child).getLDModelObject();
            getLDModel().getEnvironmentsModel().addChild(env);  
        }

        super.addChildAt(child, index, notifyLDModel);
    }

    @Override
    public boolean removeChild(IGraphicalModelObject child, boolean notifyLDModel) {
        /*
         * Remove Environment from LD Model
         */
        if(notifyLDModel && child instanceof IGraphicalEnvironmentModel) {
            ILDModelObject env = ((IGraphicalEnvironmentModel)child).getLDModelObject();
            getLDModel().getEnvironmentsModel().removeChild(env);
        }
        
        return super.removeChild(child, notifyLDModel);
    }


    public String getName() {
        return LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENTS);
    }

    public void setName(String name) {
    }

    @Override
    protected void setPropertyDescriptors() {
        // none
    }
    
    // ============================= JDOM SUPPORT =============================

    public String getTagName() {
        return LDModelFactory.ENVIRONMENTS;
    }

    // ============================= CREATE GRAPHICAL COUNTERPARTS =============================

    public void reconcile() {
        int x = 10, y = 10;
        
        boolean connections_need_creating = false;
        
        // Environments first
        for(ILDModelObject child : getLDModel().getEnvironmentsModel().getChildren()) {
            if(child instanceof IEnvironmentModel) {
                IGraphicalEnvironmentModel gfxEnvironment = (IGraphicalEnvironmentModel)getGraphicalLDChild(child);
                
                if(gfxEnvironment == null) {
                    gfxEnvironment = new GraphicalEnvironmentModel(getLDModel());
                    gfxEnvironment.setLDModelObject(child);
                    gfxEnvironment.setBounds(new Rectangle(x, y, 200, -1));
                    addChild(gfxEnvironment, false);
                    connections_need_creating = true;
                }

                gfxEnvironment.reconcile();
                
                x += 220;
                if(x > 660) {
                    x = 10;
                    y += 110;
                }
            }
        }
        
        // Then do the Environment connections if needed
        if(connections_need_creating) {
            for(IGraphicalModelObject child : getChildren()) {
                if(child instanceof IGraphicalEnvironmentModel) {
                    IGraphicalEnvironmentModel gfxParentEnvironment = (IGraphicalEnvironmentModel)child;
                    for(ILDModelObject obj : ((IEnvironmentModel)gfxParentEnvironment.getLDModelObject()).getChildren()) {
                        if(obj instanceof IEnvironmentRefModel) {
                            String ref = ((IEnvironmentRefModel)obj).getReferenceIdentifier();
                            IGraphicalConnectedModelObject gfxChildEnvironment = getGraphicalLDChild(ref);
                            if(gfxChildEnvironment != null) {
                                IGraphicalModelConnection connection = new GraphicalEnvironmentConnection(getLDModel());
                                connection.connect(gfxParentEnvironment, gfxChildEnvironment, false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected IGraphicsObject createChildObject(String name) {
        if(name.equals(EnvironmentEditorGraphicalModelFactory.NOTE)) {
            return new GraphicalNoteModel(getLDModel());
        }
        else if(name.equals(LDModelFactory.ENVIRONMENT)) {
            return new GraphicalEnvironmentModel(getLDModel());
        }
        else if(name.equals(EnvironmentEditorGraphicalModelFactory.ENVIRONMENT_CONNECTION)) {
            return new GraphicalEnvironmentConnection(getLDModel());
        }
        
        return null;
    }

}

