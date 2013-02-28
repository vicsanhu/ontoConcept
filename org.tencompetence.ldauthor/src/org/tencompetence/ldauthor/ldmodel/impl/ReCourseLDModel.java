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
package org.tencompetence.ldauthor.ldmodel.impl;

import java.io.File;
import java.util.Hashtable;

import org.jdom.Element;
import org.tencompetence.imsldmodel.LDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.ldmodel.IReCourseInfoModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;


/**
 * Top Model representing the LD Model and containing sub-models
 * 
 * @author Phillip Beauvoir
 * @version $Id: ReCourseLDModel.java,v 1.7 2009/06/18 18:02:29 phillipus Exp $
 */
public class ReCourseLDModel extends LDModel
implements IReCourseLDModel {
    
    private IReCourseInfoModel fReCourseInfoModel;

    private static Hashtable<String, String> fLevelMap = new Hashtable<String, String>();
    
    static {
        fLevelMap.put(LDModelFactory.PROPERTIES, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.WHEN_PROPERTY_VALUE_IS_SET, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.CHANGE_PROPERTY_VALUE, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.MONITOR, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.CONDITIONS, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.WHEN_CONDITION_TRUE, "B"); //$NON-NLS-1$
        fLevelMap.put(LDModelFactory.NOTIFICATION, "C"); //$NON-NLS-1$
    }
    
    public ReCourseLDModel(File manifestFile) {
        super(manifestFile);
        fReCourseInfoModel = new ReCourseInfoModel(this);
    }
    
    @Override
    public String getTitle() {
        String title = super.getTitle();
        return title == null ? Messages.ReCourseLDModel_0 : title;
    }

    public IReCourseInfoModel getReCourseInfoModel() {
        return fReCourseInfoModel;
    }

    @Override
    public String getObjectName(String objectTag) {
        return LDModelUtils.getUserObjectName(objectTag);
    }
    
    public void resetIDs() {
        // Set to null to regenerate
        setManifestIdentifier(null);
        setIdentifier(null);
        setURI(null);
    }

    public void ensureIsCorrectLevel() {
        /*
            Level B adds:
            1) The model of components is extended with the element properties, this is the place where the properties are declared.
            2) The model of complete-activity, complete-act, complete-play and complete-unit-of-learning are extended to include the element when-property-value-is-set. 
            3) The model of on-completion is extended to include the element change-property-value.
            4) The model of service is extended to include the element monitor.
            5) The model of email-data is extended with two attributes (email-property-ref and username-property-ref) referring to global properties with data.
            6) The model of time-limit is extended with one attribute (property-ref) referring to a property with data.
            7) The element method is extended to include the element conditions.
            8) The model of complete-act is extended to include the element when-condition-true.

            Level C adds:
            1) The on-completion model is extended with a notification element.
            2) The then (and else!) model is extended with a notification element.
         */
        
        Element ldRoot = super.toJDOM();
        String level = _ensureLevel(ldRoot, "A"); //$NON-NLS-1$
        setLevel(level);
    }
    
    private String _ensureLevel(Element element, String level) {
        // If we got this, don't carry on
        if(level.equals("C")) { //$NON-NLS-1$
            return level;
        }
        
        if(fLevelMap.containsKey(element.getName())) {
            level = fLevelMap.get(element.getName());
        }

        for(Object child : element.getChildren()) {
            level = _ensureLevel((Element)child, level);
        }
        
        return level;
    }
    
    // ================================== XML JDOM PERSISTENCE =================================

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        // Stop notifications
        boolean oldNotify = isNotifications();
        setNotifications(false);
        
        // ReCourse extra model (if it exists) is in the Metadata element
        Element metadata = element.getChild(LDModelFactory.METADATA, IMSLD_NAMESPACE_100_EMBEDDED);
        if(metadata != null) {
            Element recourseInfo = metadata.getChild(IReCourseInfoModel.ROOT_ELEMENT, LDAUTHOR_NAMESPACE_EMBEDDED);
            if(recourseInfo != null) {
                fReCourseInfoModel.fromJDOM(recourseInfo);
            }
        }
        
        // Reconcile the ReCourse model with the LD model
        fReCourseInfoModel.reconcile();
        
        setNotifications(oldNotify);
    }

    @Override
    public Element toJDOM() {
        Element learningdesign = super.toJDOM();
        
        // Metadata
        Element metadata = new Element(LDModelFactory.METADATA, IMSLD_NAMESPACE_100_EMBEDDED);
        learningdesign.addContent(metadata);
        
        // ReCourse Info can only go in Metadata element
        Element recourse = fReCourseInfoModel.toJDOM();
        metadata.addContent(recourse);
        
        return learningdesign;
    }
}
