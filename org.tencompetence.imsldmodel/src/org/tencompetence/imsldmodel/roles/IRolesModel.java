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
package org.tencompetence.imsldmodel.roles;

import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;



/**
 * Roles Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: IRolesModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public interface IRolesModel extends ILDModelObjectContainer, ILDModelObject {
    
    /**
     * @return A list of all Learners (not a flat list)
     */
    List<ILearnerModel> getLearners();
    
    /**
     * @return A list of all Staff (not a flat list)
     */
    List<IStaffModel> getStaff();
    
    /**
     * @return The first (Default) Learner Role or null if there isn't one
     */
    ILearnerModel getDefaultLearnerRole();
    
    /**
     * @return The first (Default) Staff Role or null if there isn't one
     */
    IStaffModel getDefaultStaffRole();
    
    /**
     * @return A flattened list of All Roles, in order of Learners, then Staff, alphabetically ordered
     */
    List<IRoleModel> getOrderedRoles();

    /**
     * @return A flattened list of Learner Roles, alphabetically ordered
     */
    List<ILearnerModel> getOrderedLearners();

    /**
     * @return A flattened list of Staff Roles, alphabetically ordered
     */
    List<IStaffModel> getOrderedStaff();

    
}