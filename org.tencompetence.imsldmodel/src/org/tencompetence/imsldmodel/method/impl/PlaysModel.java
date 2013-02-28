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
package org.tencompetence.imsldmodel.method.impl;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IPlaysModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;

/**
 * Plays Container
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlaysModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class PlaysModel
extends LDModelObjectContainer
implements IPlaysModel {
    
    public PlaysModel(ILDModel model) {
        super(model);
    }
    
    /**
     * Returns true if obj is referenced at all by any Role Part in the LD
     * 
     * @param obj
     * @return
     */
    public boolean isComponentReferencedByRolePart(ILDModelObject obj) {
        if(obj == null) {
            return false;
        }
        
        for(ILDModelObject play : getChildren()) {
            for(ILDModelObject act : ((IPlayModel)play).getActsModel().getChildren()) {
                for(ILDModelObject rolePart : ((IActModel)act).getRolePartsModel().getChildren()) {
                    // Component
                    ILDModelObject ldObject = ((IRolePartModel)rolePart).getComponent();
                    if(ldObject == obj) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return getLDModel().getObjectName(LDModelFactory.PLAYS);
    }
}
