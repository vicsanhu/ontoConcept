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
package org.tencompetence.imsldmodel.expressions;

import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;


/**
 * IShowHideType
 * 
 * @author Phillip Beauvoir
 * @version $Id: IShowHideType.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public interface IShowHideType extends ILDModelObject {

    /*
     * Choices
     */
    String[] CHOICES = {
            LDModelFactory.CLASS,
            LDModelFactory.ITEM_REF,
            LDModelFactory.ENVIRONMENT_REF,
            LDModelFactory.LEARNING_ACTIVITY_REF,
            LDModelFactory.SUPPORT_ACTIVITY_REF,
            LDModelFactory.ACTIVITY_STRUCTURE_REF,
            LDModelFactory.PLAY_REF,
            LDModelFactory.UOL_HREF
    };
    
    
    /**
     * @return A list of reference types
     */
    List<ILDModelObject> getMemberReferences();
    
    /**
     * Add a reference to a component member or UOL HREF
     * @param object
     */
    void addMember(ILDModelObject member);
    
    /**
     * Remove a reference to a component member or UOL HREF
     * @param object
     */
    void removeMember(ILDModelObject member);
    
    void setType(String type);
    
    String getType();

}
