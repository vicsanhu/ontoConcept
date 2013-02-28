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
package org.tencompetence.imsldmodel.activities;

import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.types.IItemModelType;


/**
 * IActivityStructureModel
 * 
 * @author Phillip Beauvoir
 * @version $Id: IActivityStructureModel.java,v 1.4 2009/11/26 09:16:03 phillipus Exp $
 */
public interface IActivityStructureModel
extends IActivityType {
    
    String PROPERTY_STRUCTURE_TYPE = "Property.ActivityStructureType"; //$NON-NLS-1$
    String PROPERTY_NUMBER_TO_SELECT = "Property.NumberToSelect"; //$NON-NLS-1$
    String PROPERTY_SORT_TYPE = "Property.SortType"; //$NON-NLS-1$
    String PROPERTY_ACTIVITY_REFS_CHANGED = "Property.ActivityRefsChanged"; //$NON-NLS-1$
    
    int TYPE_SEQUENCE = 0;
    int TYPE_SELECTION = 1;
    
    String[] TYPE_STRINGS = {
            "sequence", //$NON-NLS-1$
            "selection" //$NON-NLS-1$
    };
    
    String[] SORT_STRINGS = {
            null,
            "as-is", //$NON-NLS-1$
            "visibility-order" //$NON-NLS-1$
    };
        
    int getStructureType();
    void setStructureType(int type);
    
    int getSort();
    void setSort(int type);
    
    int getNumberToSelect();
    void setNumberToSelect(int num);
    
    IItemModelType getInformationModel();
    
    List<ILDModelObjectReference> getActivityRefs();
    
    /**
     * @param child
     * @return True if this Activity Structure contains a reference to an Activity child
     */
    boolean containsActivity(ILDModelObject child);
    
    /**
     * @param component
     * @param index If this is -1, add at the end
     */
    IActivityRefModel addActivity(ILDModelObject component, int index);

    /**
     * Remove an Activity
     * @param child
     */
    void removeActivity(ILDModelObject child);
}