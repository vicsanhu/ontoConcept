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
package org.tencompetence.imsldmodel.environments;

import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;


/**
 * Learning Object Interface
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILearningObjectModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public interface ILearningObjectModel
extends ILDModelObject, ITitle, IIdentifier
{
    String PROPERTY_TYPE = "Property.Type"; //$NON-NLS-1$
    
    String[] TYPE_STRINGS = {
            null,
            "knowledge-object", //$NON-NLS-1$
            "tool-object", //$NON-NLS-1$
            "test-object" //$NON-NLS-1$
    };
    
    int TYPE_NONE = 0;
    int TYPE_KNOWLEDGE_OBJECT = 1;
    int TYPE_TOOL_OBJECT = 2;
    int TYPE_TEST_OBJECT = 3;
    
    boolean isVisible();
    void setIsVisible(boolean set);
    
    int getType();
    void setType(int type);
    
    String getClassType();
    void setClassType(String s);
    
    String getParameters();
    void setParameters(String s);

    IItemTypeContainer getItems();
}