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
package org.tencompetence.ldvisualiser.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldvisualiser.ui.view.ObjectAnalysis.RolePartMapping;

/**
 * LDGraph Label Provider
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphLabelProvider.java,v 1.15 2009/10/14 16:20:10 phillipus Exp $
 */
public class LDGraphLabelProvider extends LabelProvider
implements IEntityStyleProvider, IConnectionStyleProvider {
    
    public Color DARK_RED = new Color(Display.getDefault(), 127, 0, 0);
    public Color LIGHT_BLUE = new Color(Display.getDefault(), 127, 127, 255);
    
    private GraphViewer fGraphViewer;
    
    private List<Object> fSteveInterestingDavies;
    
    private List<EntityConnectionData> fInterestingConnections;
    
    private Object fPinned;
    
    public LDGraphLabelProvider(GraphViewer viewer) {
        fGraphViewer = viewer;
        fSteveInterestingDavies = new ArrayList<Object>();
        fInterestingConnections = new ArrayList<EntityConnectionData>();
    }

    @Override
    public Image getImage(Object element) {
        if(element instanceof ILDModelObjectReference) {
            element = ((ILDModelObjectReference)element).getLDModelObject();
        }
        
        if(element instanceof ILDModelObject) {
            return ImageFactory.getIconLDType((ILDModelObject)element);
        }
        
        // Role Parts
        if(element instanceof RolePartMapping) {
            RolePartMapping mapping = (RolePartMapping)element;
            return ImageFactory.getIconLDType(mapping.role);
        }
        
        return null;
    }

    @Override
    public String getText(Object element) {
        if(element instanceof EntityConnectionData) {
            EntityConnectionData data = (EntityConnectionData)element;

            // Role inheritance
            if(data.dest instanceof IRoleModel) {
                return Messages.LDGraphLabelProvider_0;
            }
            
            return ""; //$NON-NLS-1$
        }

        String text = ""; //$NON-NLS-1$
        
        // Role Parts
        if(element instanceof RolePartMapping) {
            RolePartMapping mapping = (RolePartMapping)element;
            text = mapping.role.getTitle();
        }
        
        if(element instanceof ILDModelObjectReference) {
            element = ((ILDModelObjectReference)element).getLDModelObject();
        }
        
        if(element instanceof ITitle) {
            text = ((ITitle)element).getTitle();
        }
        
        // Split if too long
        if(text.length() > 40 && text.contains(" ")) { //$NON-NLS-1$
            int split = text.indexOf(" ", 40); //$NON-NLS-1$
            if(split != -1) {
                text = text.substring(0, split) + "\n" + text.substring(split + 1); //$NON-NLS-1$
            }
        }
        
        return text;
    }

    public void setCurrentSelection(Object parentObject, Object selectedItem) {
        // Don't show interesting connections if pinned
        if(fPinned != null) {
            return;
        }
        
        fSteveInterestingDavies.clear();
        fGraphViewer.refresh();
        
        for(EntityConnectionData connection : fInterestingConnections) {
            fGraphViewer.unReveal(connection);
        }
        fInterestingConnections.clear();
        
        /*
         * Show highlighted connections...
         */
        
        if(selectedItem instanceof IPlayModel) {
            IPlayModel play = (IPlayModel)selectedItem;
            for(ILDModelObject act : play.getActsModel().getChildren()) {
                EntityConnectionData entityConnectionData = new EntityConnectionData(play, act);
                fGraphViewer.reveal(entityConnectionData);
                fSteveInterestingDavies.add(act);
            }
        }
        
        /*
         * Act and connected RolePart Mappings
         */
        if(selectedItem instanceof IActModel) {
            IActModel act = (IActModel)selectedItem;
            for(RolePartMapping mapping : ObjectAnalysis.getRolePartMappings(act)) {
                EntityConnectionData entityConnectionData = new EntityConnectionData(act, mapping);
                fGraphViewer.reveal(entityConnectionData);
                fSteveInterestingDavies.add(mapping);
            }
        }
        
        /*
         * Role in an Act
         */
        if(selectedItem instanceof IRoleModel && parentObject instanceof IActModel) {
            IRoleModel role = (IRoleModel)selectedItem;
            IActModel act = (IActModel)parentObject;
            for(ILDModelObject child : act.getRolePartsModel().getChildren()) {
                IRolePartModel rolePart = (IRolePartModel)child;
                if(rolePart.getRole() == role) {
                    ILDModelObject component = ((IRolePartModel)rolePart).getComponent();
                    if(component != null) {
                        EntityConnectionData entityConnectionData = new EntityConnectionData(role, component);
                        fGraphViewer.reveal(entityConnectionData);
                        fSteveInterestingDavies.add(component);
                    }
                }
            }
        }
        
        /*
         * Role in a RolePart Mapping
         */
        if(selectedItem instanceof IRoleModel && parentObject instanceof RolePartMapping) {
            IRoleModel role = (IRoleModel)selectedItem;
            RolePartMapping mapping = (RolePartMapping)parentObject;
            for(ILDModelObject component : mapping.componentList) {
                EntityConnectionData entityConnectionData = new EntityConnectionData(role, component);
                fGraphViewer.reveal(entityConnectionData);
                fSteveInterestingDavies.add(component);
            }
        }
        
        /*
         * Activity Ref - highlight Environment refs
         */
        if(selectedItem instanceof IActivityRefModel) {
            IActivityType activity = (IActivityType)((IActivityRefModel)selectedItem).getLDModelObject();
            for(ILDModelObjectReference ref : activity.getEnvironmentRefs()) {
                // Note: this is different to IActivityType = we add selectedItem
                EntityConnectionData entityConnectionData = new EntityConnectionData(selectedItem, ref.getLDModelObject());
                fGraphViewer.reveal(entityConnectionData);
            }
        }
        
        /*
         * Learning/Support Activity - highlight Environments
         */
        if(selectedItem instanceof IActivityType) {
            IActivityType activity = (IActivityType)selectedItem;
            for(ILDModelObjectReference ref : activity.getEnvironmentRefs()) {
                EntityConnectionData entityConnectionData = new EntityConnectionData(activity, ref.getLDModelObject());
                fGraphViewer.reveal(entityConnectionData);
            }
        }
        
        /*
         * Activity Structure Ref
         */
        if(selectedItem instanceof IActivityStructureRefModel) {
            // Don't do this it causes the real AS to be highlighted if on same graph
            //selectedItem = ((IActivityStructureRefModel)selectedItem).getLDModelObject();
        }
        
        /*
         * Activity Structure
         */
        if(selectedItem instanceof IActivityStructureModel) {
            IActivityStructureModel as = (IActivityStructureModel)selectedItem;
            
            /*
             * If it's a sequence type then build up relationships in sequence
             */
            if(as.getStructureType() == IActivityStructureModel.TYPE_SEQUENCE) {
                Object src = as;
                
                for(ILDModelObjectReference ref : as.getActivityRefs()) {
                    EntityConnectionData entityConnectionData = new EntityConnectionData(src, ref);
                    fGraphViewer.reveal(entityConnectionData);
                    fSteveInterestingDavies.add(ref);
                    src = ref;
                }
            }
            // Selection
            else {
                for(ILDModelObjectReference ref : as.getActivityRefs()) {
                    EntityConnectionData entityConnectionData = new EntityConnectionData(as, ref);
                    fGraphViewer.reveal(entityConnectionData);
                    fSteveInterestingDavies.add(ref);
                }

            }
        }
        
        for(Object object : fSteveInterestingDavies) {
            fGraphViewer.update(object, null);
        }
    }
    
    public void setPinnedNode(Object parentObject, Object pinned) {
        if(pinned != null) {
            fPinned = null; // clear this temporarily
            setCurrentSelection(parentObject, pinned);
        }
        fPinned = pinned;
    }

    public Object getPinnedNode() {
        return fPinned;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        DARK_RED.dispose();
        LIGHT_BLUE.dispose();
    }

    // ========================================================================================
    // IEntityStyleProvider
    // ========================================================================================

    public boolean fisheyeNode(Object entity) {
        // Very annoying!
        return false;
    }

    public Color getBackgroundColour(Object entity) {
        if(fSteveInterestingDavies.contains(entity)) {
            return fGraphViewer.getGraphControl().HIGHLIGHT_ADJACENT_COLOR;
        }
        return null;
    }

    public Color getBorderColor(Object entity) {
        return null;
    }

    public Color getBorderHighlightColor(Object entity) {
        return null;
    }

    public int getBorderWidth(Object entity) {
        return 0;
    }

    public Color getForegroundColour(Object entity) {
        return null;
    }

    public Color getNodeHighlightColor(Object entity) {
        return null;
    }

    public IFigure getTooltip(Object entity) {
        return null;
    }

    // ========================================================================================
    // IConnectionStyleProvider
    // ========================================================================================
    
    public Color getColor(Object rel) {
        EntityConnectionData data = (EntityConnectionData)rel;
        
        if(data.source instanceof IActModel && data.dest instanceof IActModel) {
            return LIGHT_BLUE;
        }
        
        if(data.dest instanceof IEnvironmentModel) {
            //return DARK_RED;
        }
        
        if(fInterestingConnections.contains(rel)) {
            //return DARK_RED;
        }
        
        return null;
    }

    public int getConnectionStyle(Object rel) {
        EntityConnectionData data = (EntityConnectionData)rel;
        if(data.dest instanceof IEnvironmentModel) {
            // Role in Role Part doing an Environment
            if(data.source instanceof IRoleModel) {
                return ZestStyles.CONNECTIONS_DIRECTED;
            }
            // Environment from Activity
            return ZestStyles.CONNECTIONS_DOT;
        }
        
        // Role inheritance
        if(data.dest instanceof IRoleModel) {
            return ZestStyles.CONNECTIONS_DOT | ZestStyles.CONNECTIONS_DIRECTED;
        }
        
        return ZestStyles.CONNECTIONS_DIRECTED;
    }

    public Color getHighlightColor(Object rel) {
        return null;
    }

    public int getLineWidth(Object rel) {
        return 0;
    }

}