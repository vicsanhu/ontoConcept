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
package org.tencompetence.imsldmodel.activities.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentRefModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectReferenceArrayList;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.impl.ItemModelType;


/**
 * Activity Structure Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityStructureModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class ActivityStructureModel implements IActivityStructureModel, PropertyChangeListener {
    
    private ILDModel fLDModel;
    
    private String fID;
    private String fTitle;
    
    private int fSort;
    private int fStructureType = TYPE_SELECTION; // Default
    private int fNumberToSelect;
    
    private List<ILDModelObjectReference> fActivityRefs = new LDModelObjectReferenceArrayList();
    private List<ILDModelObjectReference> fEnvironmentRefs = new LDModelObjectReferenceArrayList();
    
    private List<IEnvironmentModel> fEnvironmentRefsUndoCache = new ArrayList<IEnvironmentModel>();

    private IItemModelType fInformationModel;

    public ActivityStructureModel(ILDModel ldModel) {
        fLDModel = ldModel;
        fInformationModel = new ItemModelType(ldModel, LDModelFactory.INFORMATION);
        ldModel.addPropertyChangeListener(this);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "as-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        fLDModel.firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public List<ILDModelObjectReference> getActivityRefs() {
        return fActivityRefs;
    }

    public IItemModelType getInformationModel() {
        return fInformationModel;
    }

    public int getNumberToSelect() {
        return fNumberToSelect;
    }

    public int getSort() {
        return fSort;
    }

    public int getStructureType() {
        return fStructureType;
    }

    public void setNumberToSelect(int num) {
        int old = fNumberToSelect;
        fNumberToSelect = num;
        fLDModel.firePropertyChange(this, PROPERTY_NUMBER_TO_SELECT, old, num);
    }

    public void setSort(int type) {
        int old = fSort;
        fSort = type;
        fLDModel.firePropertyChange(this, PROPERTY_SORT_TYPE, old, type);
    }

    public void setStructureType(int type) {
        int old = fStructureType;
        fStructureType = type;
        fLDModel.firePropertyChange(this, PROPERTY_STRUCTURE_TYPE, old, type);
    }

    // ====================================== ENVIRONMENT REF ACCESS SUPPORT ===========================================

    public List<ILDModelObjectReference> getEnvironmentRefs() {
        return fEnvironmentRefs;
    }

    public void addEnvironmentRef(IEnvironmentModel env) {
        // Convert LD object to ref object...
        if(!fEnvironmentRefs.contains(env)) {
            ILDModelObjectReference ref = new EnvironmentRefModel(env.getLDModel());
            ref.setReferenceIdentifer(env.getIdentifier());
            fEnvironmentRefs.add(ref);
            
            // Fire property change
            fLDModel.firePropertyChange(this, PROPERTY_ENVIRONMENT_REFS_CHANGED, null, fEnvironmentRefs);
        }
    }

    public void removeEnvironmentRef(IEnvironmentModel env) {
        fEnvironmentRefs.remove(env);
        
        // Fire property change
        fLDModel.firePropertyChange(this, PROPERTY_ENVIRONMENT_REFS_CHANGED, null, fEnvironmentRefs);
    }

    // ====================================== ACTIVITY REF ACCESS SUPPORT ===========================================

    public boolean containsActivity(ILDModelObject child) {
        return fActivityRefs.contains(child);
    }

    public IActivityRefModel addActivity(ILDModelObject child, int index) {
        if(containsActivity(child)) {
            System.err.println("Attempt to add duplicate Activity in ActivityStructureModel"); //$NON-NLS-1$
            return null;
        }
        
        // Convert LD object to ref object...
        
        IActivityRefModel ref = null;
        
        // If this is a ref, make a new ref (don't re-use the same ref)
        if(child instanceof IActivityRefModel) {
            child = ((IActivityRefModel)child).getLDModelObject();
        }
        
        // Create a new ref
        if(child instanceof ILearningActivityModel) {
            ref = new LearningActivityRefModel(getLDModel());
            ref.setReferenceIdentifer(((ILearningActivityModel)child).getIdentifier());
        }
        else if(child instanceof ISupportActivityModel) {
            ref = new SupportActivityRefModel(getLDModel());
            ref.setReferenceIdentifer(((ISupportActivityModel)child).getIdentifier());
        }
        else if(child instanceof IActivityStructureModel) {
            ref = new ActivityStructureRefModel(getLDModel());
            ref.setReferenceIdentifer(((IActivityStructureModel)child).getIdentifier());
        }
        
        if(ref != null) {
            ref.setParent(this);
            
            if(index == -1) {
                fActivityRefs.add(ref);
            }
            else {
                if(index >= fActivityRefs.size()) {
                    fActivityRefs.add(ref);
                }
                else {
                    fActivityRefs.add(index, ref);
                }
            }
            
            // Fire property change
            fLDModel.firePropertyChange(this, PROPERTY_ACTIVITY_REFS_CHANGED, null, fActivityRefs);
        }
        
        return ref;
    }
    
    public void removeActivity(ILDModelObject child) {
        fActivityRefs.remove(child);
        
        // Fire property change
        fLDModel.firePropertyChange(this, PROPERTY_ACTIVITY_REFS_CHANGED, null, fActivityRefs);
    }

    
    // ====================================== PROPERTY CHANGE SUPPORT ===========================================

    public void propertyChange(PropertyChangeEvent evt) {
        
        // An object was removed - check if it was an activity or environment that we reference...
        if(evt.getPropertyName().equals(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED)) {
            ILDModelObject object =  (ILDModelObject)evt.getNewValue();
            
            // Activity removed
            if(fActivityRefs.contains(object)) {
                removeActivity(object);
            }
            
            // Environment removed
            else if(object instanceof IEnvironmentModel) {
                IEnvironmentModel env = (IEnvironmentModel)evt.getNewValue();
                if(fEnvironmentRefs.contains(env)) {
                    removeEnvironmentRef(env);
                    fEnvironmentRefsUndoCache.add(env);
                }
            }
        }
        
        // An object was added - check if it was an environment we once referenced...
        // (Deleting Environments is undoable)
        else if(evt.getPropertyName().equals(ILDModelObjectContainer.PROPERTY_CHILD_ADDED)) {
            ILDModelObject object =  (ILDModelObject)evt.getNewValue();
            
            // Environment added
            if(object instanceof IEnvironmentModel) {
                IEnvironmentModel env = (IEnvironmentModel)evt.getNewValue();
                if(fEnvironmentRefsUndoCache.contains(env)) {
                    addEnvironmentRef(env);
                    fEnvironmentRefsUndoCache.remove(env);
                }
            }
        }
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        try {
            Attribute att = element.getAttribute(LDModelFactory.NUMBER_TO_SELECT);
            if(att != null) {
                fNumberToSelect = att.getIntValue();
            }
        }
        catch(DataConversionException ex) {
            ex.printStackTrace();
        }
        
        String s = element.getAttributeValue(LDModelFactory.SORT);
        if(s != null) {
            for(int i = 0; i < SORT_STRINGS.length; i++) {
                if(s.equals(SORT_STRINGS[i])) {
                    fSort = i;
                }
            }
        }
        
        s = element.getAttributeValue(LDModelFactory.STRUCTURE_TYPE);
        if(s != null) {
            for(int i = 0; i < TYPE_STRINGS.length; i++) {
                if(s.equals(TYPE_STRINGS[i])) {
                    fStructureType = i;
                }
            }
        }
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            else if(tag.equals(LDModelFactory.INFORMATION)) {
                fInformationModel.fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.ENVIRONMENT_REF)) {
                ILDModelObjectReference ref = new EnvironmentRefModel(fLDModel);
                ref.fromJDOM(child);
                fEnvironmentRefs.add(ref);
            }

            else if(tag.equals(LDModelFactory.LEARNING_ACTIVITY_REF)) {
                IActivityRefModel ref = new LearningActivityRefModel(fLDModel);
                ref.fromJDOM(child);
                ref.setParent(this);
                fActivityRefs.add(ref);
            }
            
            else if(tag.equals(LDModelFactory.SUPPORT_ACTIVITY_REF)) {
                IActivityRefModel ref = new SupportActivityRefModel(fLDModel);
                ref.fromJDOM(child);
                ref.setParent(this);
                fActivityRefs.add(ref);
            }
            
            else if(tag.equals(LDModelFactory.ACTIVITY_STRUCTURE_REF)) {
                IActivityRefModel ref = new ActivityStructureRefModel(fLDModel);
                ref.fromJDOM(child);
                ref.setParent(this);
                fActivityRefs.add(ref);
            }
        }

    }

    public Element toJDOM() {
        Element activityStructure = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        activityStructure.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        
        if(fNumberToSelect != 0) {
            activityStructure.setAttribute(LDModelFactory.NUMBER_TO_SELECT, "" + fNumberToSelect); //$NON-NLS-1$
        }
        
        if(fSort != 0) {
            activityStructure.setAttribute(LDModelFactory.SORT, SORT_STRINGS[fSort]);
        }
        
        activityStructure.setAttribute(LDModelFactory.STRUCTURE_TYPE, TYPE_STRINGS[fStructureType]);
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        activityStructure.addContent(title);

        // Information
        Element information = fInformationModel.toJDOM();
        if(information != null) {
            activityStructure.addContent(information);
        }
        
        // Environment Refs
        for(ILDModelObjectReference ref : fEnvironmentRefs) {
            Element e = ref.toJDOM();
            activityStructure.addContent(e);
        }
        
        // Activity Refs
        for(ILDModelObjectReference ref : fActivityRefs) {
            Element e = ref.toJDOM();
            activityStructure.addContent(e);
        }
        
        return activityStructure;
    }

    public String getTagName() {
        return LDModelFactory.ACTIVITY_STRUCTURE;
    }
}
