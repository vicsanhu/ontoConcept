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

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.ICompleteActivityType;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentRefModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectReferenceArrayList;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.types.ICompleteType;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.imsldmodel.types.impl.ItemModelType;
import org.tencompetence.imsldmodel.types.impl.OnCompletionType;


/**
 * Abstract Activity Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractActivityModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public abstract class AbstractActivityModel implements IActivityModel, PropertyChangeListener {

    private ILDModel fLDModel;
    
    protected String fID;
    protected String fTitle;
    
    private boolean fIsVisible = true;
    private String fParameters;
    
    protected IItemModelType fDescriptionModel;
    
    protected ICompleteActivityType fCompleteActivityType;
    
    protected IOnCompletionType fCompletionType;
    
    protected List<ILDModelObjectReference> fEnvironmentRefs = new LDModelObjectReferenceArrayList();
    
    private List<IEnvironmentModel> fUndoCache = new ArrayList<IEnvironmentModel>();

    protected AbstractActivityModel(ILDModel ldModel) {
        fLDModel = ldModel;
        
        fDescriptionModel = new ItemModelType(ldModel, LDModelFactory.ACTIVITY_DESCRIPTION);
        
        // This is the preferred default. But see fromJDOM(Element element), below...
        getCompleteActivityType().setChoice(ICompleteActivityType.COMPLETE_USER_CHOICE);
        
        fLDModel.addPropertyChangeListener(this);
    }

    public ICompleteActivityType getCompleteActivityType() {
        if(fCompleteActivityType == null) {
            fCompleteActivityType = new CompleteActivityType(this);
        }
        return fCompleteActivityType;
    }

    public IItemModelType getDescriptionModel() {
        return fDescriptionModel;
    }

    public IOnCompletionType getOnCompletionType() {
        if(fCompletionType == null) {
            fCompletionType = new OnCompletionType(this);
        }
        return fCompletionType;
    }

    public String getParameters() {
        return fParameters;
    }

    public boolean isVisible() {
        return fIsVisible;
    }

    public void setIsVisible(boolean set) {
        fIsVisible = set;
    }

    public void setParameters(String s) {
        fParameters = s;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        fLDModel.firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Environments being removed
        if(evt.getNewValue() instanceof IEnvironmentModel) {
            IEnvironmentModel env = (IEnvironmentModel)evt.getNewValue();
            if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                if(fEnvironmentRefs.contains(env)) {
                    removeEnvironmentRef(env);
                    fUndoCache.add(env);
                }
            }
            else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName())) {
                if(fUndoCache.contains(env)) {
                    addEnvironmentRef(env);
                    fUndoCache.remove(env);
                }
            }
        }
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
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fIsVisible = !"false".equals(element.getAttributeValue(LDModelFactory.ISVISIBLE)); //$NON-NLS-1$
        fParameters = element.getAttributeValue(LDModelFactory.PARAMETERS);
        
        /*
         * Treat Activity Completion as a special case because if the element is not present
         * then we have to counteract the default set for it, above.
         */
        Element completion = element.getChild(LDModelFactory.COMPLETE_ACTIVITY, IMSLD_NAMESPACE_100_EMBEDDED);
        if(completion == null) {
            getCompleteActivityType().setChoice(ICompleteType.COMPLETE_NONE);
        }
        else {
            getCompleteActivityType().fromJDOM(completion);
        }
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            else if(tag.equals(LDModelFactory.ACTIVITY_DESCRIPTION)) {
                fDescriptionModel.fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.ON_COMPLETION)) {
                getOnCompletionType().fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.ENVIRONMENT_REF)) {
                ILDModelObjectReference ref = new EnvironmentRefModel(fLDModel);
                ref.fromJDOM(child);
                fEnvironmentRefs.add(ref);
            }
        }

    }

    public Element toJDOM() {
        Element activity = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        activity.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        activity.setAttribute(LDModelFactory.ISVISIBLE, "" + isVisible()); //$NON-NLS-1$
        
        if(StringUtils.isSet(fParameters)) {
            activity.setAttribute(LDModelFactory.PARAMETERS, fParameters);
        }
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        activity.addContent(title);
        
        return activity;
    }
}
