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
package org.tencompetence.ldauthor.ui.editors.method;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.method.IRolePartsModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;


/**
 * Adapts the RolePart objects and their children (if any) in an Act to those seen in the RolePartsTreeTableViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartsTreeModelAdapter.java,v 1.28 2009/05/19 18:21:01 phillipus Exp $
 */
public class RolePartsTreeModelAdapter {
    
    private IActModel fAct;
    
    private List<FoldedRolePart> fFoldedRoleParts;
    
    private ILDEditorPart fEditor;
    
    private RolePartsTreeTableViewer fViewer;

    /**
     * @param editor
     * @param viewer
     */
    public RolePartsTreeModelAdapter(ILDEditorPart editor, RolePartsTreeTableViewer viewer) {
        fEditor = editor;
        fViewer = viewer;

        fFoldedRoleParts = new ArrayList<FoldedRolePart>();
        
        addListeners();
    }
    
    /**
     * Add various listeners
     */
    private void addListeners() {
        // Pass on table selections to the Inspector
        fViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // We need to get at the actual component...
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                ILDModelObject component = null;
                
                if(o instanceof FoldedRolePart) {
                    component = ((FoldedRolePart)o).getComponent();
                }
                else if(o instanceof ASChildTreeItem) {
                    component = ((ASChildTreeItem)o).getComponentRef();
                }
                
                if(component != null) {
                    InspectorManager.getInstance().setInput(fEditor, component);
                    fEditor.getEditorSite().getSelectionProvider().setSelection(new StructuredSelection(component));
                }
            }
        });
        
        // Listen to LD model changes to update the table...
        ILDModel ldModel = (ILDModel)fEditor.getAdapter(ILDModel.class);
        ldModel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Role Part added somewhere
                if(evt.getPropertyName() == ILDModelObjectContainer.PROPERTY_CHILD_ADDED){
                    if(evt.getSource() instanceof IRolePartsModel) {
                        IRolePartModel rolePart = (IRolePartModel)evt.getNewValue();
                        addRolePart(rolePart);
                        fViewer.refresh();
                    }
                }
                
                // Activity Changed in some way
                if(evt.getSource() instanceof IActivityType || evt.getSource() instanceof IEnvironmentModel 
                        || evt.getNewValue() instanceof IActivityType) {
                    fViewer.refresh();
                }
                
                // Rename Role - update column
                if(evt.getSource() instanceof IRoleModel && evt.getPropertyName() == ILDModelObject.PROPERTY_NAME) {
                    fViewer.updateColumnName((IRoleModel)evt.getSource());
                }
                
                // Role Added or deleted - update columns
                if(evt.getPropertyName().startsWith(ILDModelObjectContainer.PROPERTY_CHILD) && evt.getNewValue() instanceof IRoleModel) {
                    fViewer.addColumns();
                    fViewer.refresh();
                    fViewer.getTree().getParent().layout();
                }
            }
        });
    }

    /**
     * Set The Act
     * @param act
     */
    public void setAct(IActModel act) {
        fAct = act;
        if(act != null) {
            createFoldedRoleParts();
        }
        else {
            fFoldedRoleParts.clear();
        }
    }
    
    /**
     * @return The Act
     */
    public IActModel getAct() {
        return fAct;
    }
    
    /**
     * @return The Role Parts, folded
     */
    public List<FoldedRolePart> getFoldedRoleParts() {
        return fFoldedRoleParts;
    }
    
    /**
     * Delete a Folded Role Part - actually removes LD RoleParts
     * @param foldedRolePart
     */
    public void deleteFoldedRolePart(FoldedRolePart foldedRolePart) {
        List<IRoleModel> copy = new ArrayList<IRoleModel>(); // Use copy to stop concurrent modification
        copy.addAll(foldedRolePart.getRoles());
        for(IRoleModel role : copy) {
            foldedRolePart.removeRole(role);
        }
        fFoldedRoleParts.remove(foldedRolePart);
    }
    
    /**
     * Move a Folded Role Part - actually removes and adds LD RoleParts at index
     * @param foldedRolePart
     */
    public void moveFoldedRolePart(FoldedRolePart foldedRolePart, int index) {
        if(index >= fFoldedRoleParts.size()) {
            index--;
        }
        
        ILDModelObject component = foldedRolePart.getComponent();
        
        // Find all Role Parts that have this component and move them all above or below
        // the first Role Part that has the target component
        
        FoldedRolePart f = fFoldedRoleParts.get(index);
        ILDModelObject targetComponent = f.getComponent();

        // Sort the RoleParts first
        sortRoleParts();
        
        int movetoPosition = -1;
        
        // Collect the underlying RoleParts that need moving and also find the new position to move to
        List<ILDModelObject> rolePartsToMove = new ArrayList<ILDModelObject>();
        for(int i = 0; i < fAct.getRolePartsModel().getChildren().size(); i++) {
            IRolePartModel rolePart = (IRolePartModel)fAct.getRolePartsModel().getChildren().get(i);
            if(rolePart.getComponent() == component) {
                rolePartsToMove.add(rolePart);
            }
            if(movetoPosition == -1 && rolePart.getComponent() == targetComponent) {
                movetoPosition = i;
            }
        }
        
        // Move them
        for(ILDModelObject modelObject : rolePartsToMove) {
            fAct.getRolePartsModel().getChildren().remove(modelObject);
            fAct.getRolePartsModel().getChildren().add(movetoPosition, modelObject);
        }
        
        // Recreate the folded role parts
        createFoldedRoleParts();
    }
    
    /**
     * Sort the Role Parts by Component
     */
    private void sortRoleParts() {
        List<ILDModelObject> roleParts = fAct.getRolePartsModel().getChildren();
        
        // No point
        if(roleParts.size() < 3) {
            return;
        }
        
        // Make a copy
        List<ILDModelObject> rolePartsCopy = new ArrayList<ILDModelObject>();
        rolePartsCopy.addAll(roleParts);
        
        // Clear originals
        roleParts.clear();
        
        for(ILDModelObject object : rolePartsCopy) {
            IRolePartModel rolePart = (IRolePartModel)object;
            if(!roleParts.contains(rolePart)) {
                roleParts.addAll(getRolePartsWithComponent(rolePart.getComponent(), rolePartsCopy));
            }
        }
    }
    
    /**
     * @param component
     * @return A list of RoleParts that have component as member
     */
    private List<ILDModelObject> getRolePartsWithComponent(ILDModelObject component, List<ILDModelObject> roleParts) {
        List<ILDModelObject> list = new ArrayList<ILDModelObject>();
        
        for(ILDModelObject object : roleParts) {
            IRolePartModel rolePart = (IRolePartModel)object;
            if(rolePart.getComponent() == component) {
                list.add(rolePart);
            }
        }
        
        return list;
    }
    
    /**
     * Create the folded Role Parts
     */
    private void createFoldedRoleParts() {
        fFoldedRoleParts.clear();
        
        for(ILDModelObject objRolePart : fAct.getRolePartsModel().getChildren()) {
            addRolePart((IRolePartModel)objRolePart);
        }
    }
    
    private void addRolePart(IRolePartModel rolePart) {
        FoldedRolePart foldedRolePart = findFoldedRolePart(rolePart.getComponent());
        if(foldedRolePart == null) {
            createFoldedRolePart(rolePart);
        }
        else {
            foldedRolePart.addRole(rolePart.getRole());
        }
    }
    
    /**
     * Create a folded Role Part
     * @param rolePartModel
     * @return
     */
    private FoldedRolePart createFoldedRolePart(IRolePartModel rolePartModel) {
        FoldedRolePart foldedRolePart = new FoldedRolePart(fAct.getRolePartsModel());
        foldedRolePart.setComponent(rolePartModel.getComponent());
        foldedRolePart.addRole(rolePartModel.getRole());
        fFoldedRoleParts.add(foldedRolePart);
        return foldedRolePart;
    }
    
    /**
     * Find a FoldedRolePart by role part's component member
     * @param component
     * @return
     */
    private FoldedRolePart findFoldedRolePart(ILDModelObject component) {
        for(FoldedRolePart foldedRolePart : fFoldedRoleParts) {
            if(foldedRolePart.getComponent() == component) {
                return foldedRolePart;
            }
        }
        return null;
    }
    
    // =============================================================================================================
    // =====================================         HELPER CLASSES         ========================================
    // =============================================================================================================
    
    
    /**
     * A representation of one or more IRolePartModel objects where the component is shared by one or more Roles.
     * For example it turns the following:
     * 
     * Learner --> LA1
     * Learner --> LA2
     * Teacher --> LA1
     * Teacher --> SA1
     * 
     * into:
     * 
     * LA1 --> Learner, Teacher
     * LA2 --> Learner
     * SA1 --> Teacher
     * 
     * It consists of:
     * - One and only one component of either Learning Activity, Support Activity, Environment or UoL HREF type
     * - One or more Roles
     * 
     */
    class FoldedRolePart {
        
        private List<IRoleModel> fRoles = new ArrayList<IRoleModel>();
        
        private IRolePartsModel fRolePartsModel;
        private ILDModelObject fComponent;
        
        // Undo cache so that if user deletes Role Part by unticking it we can restore the same one
        private HashMap<IRoleModel, IRolePartModel> fUndoCache = new HashMap<IRoleModel, IRolePartModel>();
        
        public FoldedRolePart(IRolePartsModel rolePartsModel) {
            fRolePartsModel = rolePartsModel;
        }
        
        /**
         * Add an additional LD Role to the flat list.
         * The Role's RolePart's Component should be the same as the existing one
         * This is an internal call
         * @return True if added
         */
        private boolean addRole(IRoleModel role) {
            if(role != null && !fRoles.contains(role)) {
                return fRoles.add(role);
            }
            return false;
        }
        
        /**
         * Add a Role to the Role Part.  This will create a new LD Role Part based on role
         * @param role
         * @return
         */
        public boolean addNewRole(IRoleModel role) {
            if(!hasRole(role)) {
                IRolePartModel rolePart = fUndoCache.get(role); // See if we have an old one from before
                if(rolePart == null) {
                    rolePart = (IRolePartModel)LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, role.getLDModel());
                }
                else {
                    fUndoCache.remove(role); // remove from cache
                }
                rolePart.setRole(role);
                rolePart.setComponent(fComponent);
                fRolePartsModel.addChild(rolePart);
                //addRole(role); // Not actually called since the ILDModel listener does it, above
                return true;
            }
            return false;
        }
        
        /**
         * Remove a Role from the Role Part.  This will delete the LD Role Part
         * @param role
         * @return
         */
        public boolean removeRole(IRoleModel role) {
            IRolePartModel rolePart = findRolePartModel(role);
            if(rolePart != null) {
                fRolePartsModel.removeChild(rolePart);
                fRoles.remove(rolePart.getRole());
                fUndoCache.put(role, rolePart); // Store the Role Part in case user ticks it again
                return true;
            }
            return false;
        }
        
        /**
         * Find a RolePart in this FoldedRolePart given a role
         * Matches on Role and Component
         * @param role
         * @return
         */
        private IRolePartModel findRolePartModel(IRoleModel role) {
            for(ILDModelObject obj : fRolePartsModel.getChildren()) {
                IRolePartModel rolePart = (IRolePartModel)obj;
                if(role == rolePart.getRole() && fComponent == rolePart.getComponent()) {
                    return (IRolePartModel)rolePart;
                }
            }
            return null;
        }
        
        /**
         * @return The component shared by all Roles
         */
        public ILDModelObject getComponent() {
            return fComponent;
        }
        
        /**
         * Set the LD component for this set of Role Parts
         * @param component
         */
        public void setComponent(ILDModelObject component) {
            fComponent = component;
        }
        
        /**
         * @param role
         * @return True if this contains role
         */
        public boolean hasRole(IRoleModel role) {
            return role != null && fRoles.contains(role);
        }

        public List<IRoleModel> getRoles() {
            return fRoles;
        }
        
        public List<ASChildTreeItem> getChildItems() {
            List<ASChildTreeItem> list = new ArrayList<ASChildTreeItem>();
            
            if(fComponent instanceof IActivityStructureModel) {
                for(ILDModelObjectReference child : ((IActivityStructureModel)fComponent).getActivityRefs()) {
                    ASChildTreeItem item = new ASChildTreeItem(this, child);
                    list.add(item);
                }
            }
            
            return list;
        }
        
        @Override
        public String toString() {
            // Need this for TreeTable comparator
            return (fComponent instanceof ITitle) ? ((ITitle)fComponent).getTitle() : ""; //$NON-NLS-1$
        }
     }
    
    /*
     * Class to keep together the child component ref and parent FoldedRolePart.
     * We have to use this to support the "ghost" ticking in the tree.
     */
    class ASChildTreeItem {
        private FoldedRolePart foldedRolePart;
        private ILDModelObjectReference component;
        
        public ASChildTreeItem(FoldedRolePart foldedRolePart, ILDModelObjectReference component) {
            this.foldedRolePart = foldedRolePart;
            this.component = component;
        }

        public FoldedRolePart getFoldedRolePart() {
            return foldedRolePart;
        }
        
        public ILDModelObjectReference getComponentRef() {
            return component;
        }
        
        /**
         * @return Child objects
         */
        public List<ASChildTreeItem> getChildItems() {
            List<ASChildTreeItem> list = new ArrayList<ASChildTreeItem>();
            
            if(component.getLDModelObject() instanceof IActivityStructureModel) {
                for(ILDModelObjectReference child : ((IActivityStructureModel)component.getLDModelObject()).getActivityRefs()) {
                    ASChildTreeItem item = new ASChildTreeItem(foldedRolePart, child);
                    list.add(item);
                }
            }
            
            return list;
        }
        
        @Override
        public boolean equals(Object obj) {
            // Ensures that the tree gets at the actual underlying object
            if(obj instanceof ASChildTreeItem) {
                return ((ASChildTreeItem)obj).getComponentRef() == getComponentRef();
            }
            return super.equals(obj);
        }
        
        @Override
        public int hashCode() {
            // Ensures that the tree gets at the actual underlying object
            return getComponentRef().hashCode();
        }
    }
}
