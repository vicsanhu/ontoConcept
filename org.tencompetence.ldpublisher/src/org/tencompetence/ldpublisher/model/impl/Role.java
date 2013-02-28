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
package org.tencompetence.ldpublisher.model.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.tencompetence.ldpublisher.IPublishHandler;
import org.tencompetence.ldpublisher.IPublisherConstants;
import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IUser;

/**
 * Bean to model a role found in CopperCore
 * 
 * @author Paul Sharples
 * @version $Id: Role.java,v 1.14 2008/10/24 17:29:40 ps3com Exp $
 *
 */
public class Role extends AbstractCCObject implements IRole {
	
	private int fMaxPersons; // -1 means it wasn't found
		
	private int fMinPersons; // -1 means it wasn't found
	private int fParent;
	
		
	private String fRoleType = null;
	private List<IUser> fUsers;

	public Role(int copperCoreKey, String title, int min, int max, int parent, String roleType, IPublishHandler publishHandler) {
		super(copperCoreKey, title, publishHandler);		
		fMinPersons = min;
		fMaxPersons = max;
		fParent = parent;
		fRoleType = roleType;		
	}
		
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#addUserToRole(org.tencompetence.ldpublisher.model.IUser)
	 */
	public void addUserToRole(IUser user) throws Exception {
		// to add a user to a role from scratch..
		// 1. user must exist
		// 2. user must be added to run
		// 3. user must be added to role
		// 4. user has this role set to active in this run
		// so...
		
		// check if user exists?
		try {			
			getPublishHandler().createUser(user.getTitle());
		} 
		catch (Exception ex) {
			//User already exists
			if(!ex.getMessage().contains(IPublisherConstants.ALREADY_EXISTS_EXCEPTION_STRING)){
				throw new Exception(ex);
			}
		}
		try {
			getPublishHandler().addUserToRun(user.getTitle(), fParent);
		} 
		catch (Exception ex) {
			// user is already on the run
			if(!ex.getMessage().contains(IPublisherConstants.ALREADY_EXISTS_EXCEPTION_STRING)){
				throw new Exception(ex);
			}
		}
		try {
			getPublishHandler().addUserToRole(user.getTitle(), getCopperCoreKey());
		} 
		catch (Exception ex) {
			// user is already added to this role
			if(!ex.getMessage().contains(IPublisherConstants.ALREADY_EXISTS_EXCEPTION_STRING)){
				throw new Exception(ex);
			}
		}
		try {
			getPublishHandler().setActiveRole(user.getTitle(), fParent, getCopperCoreKey());
		} 
		catch (Exception ex) {
			// This is already the active role for this user
			if(!ex.getMessage().contains(IPublisherConstants.ALREADY_EXISTS_EXCEPTION_STRING)){
				throw new Exception(ex);
			}
		}
	}
	
		
	
	public IUser createUser(String userId) throws Exception {
		IUser newUser = null;
		try {
			getPublishHandler().createUser(userId);
			newUser = new User(userId);
		} 
		catch (Exception ex) {
			if(!ex.getMessage().contains(IPublisherConstants.ALREADY_EXISTS_EXCEPTION_STRING)){
				throw new Exception(ex);
			}
		}		
		return newUser;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.impl.IRoleDataModelBean#getFMaxPersons()
	 */
	public int getMaxPersons() {
		return fMaxPersons;
	}	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.impl.IRoleDataModelBean#getFMinPersons()
	 */
	public int getMinPersons() {
		return fMinPersons;
	}

	public String getRoleType() {
		return fRoleType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#getUser(java.lang.String)
	 */
	public IUser getUser(String userId) throws Exception {
		IUser user = null;
				
		try {
			user = new User(getPublishHandler().getUser(userId));
		} 
		catch (Exception ex) {
			if(ex.getMessage().contains(IPublisherConstants.NOT_FOUND_EXCEPTION_STRING)){
				// didn't exist so create the user
				user = createUser(userId);
			}
			else{
				// some other error occured
				throw new Exception(ex);
			}
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#getUsers()
	 */
	public List<IUser> getUsers() throws ServiceException, JDOMException, IOException {
		fUsers = getPublishHandler().getUsersFromRole(fParent, this);
		return fUsers;	
	}


	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#hasMaximumUsers()
	 */
	public boolean hasMaximumUsers(){
		if (fUsers == null) return false;
		
		if(fMinPersons == -1){
			return false;
		}
		else{
			if(fUsers.size()< fMaxPersons){
				return false;
			}
			else{
				return true;
			}			
		}
	}


	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#hasMinimumUsers()
	 */
	public boolean hasMinimumUsers(){
		if (fUsers == null) return true;
		
		if(fMinPersons == -1){
			return true;
		}
		else{
			if(fUsers.size()< fMinPersons){
				return false;
			}
			else{
				return true;
			}			
		}
	}


	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#removeUserFromRole(org.tencompetence.ldpublisher.model.IUser)
	 */
	public void removeUserFromRole(IUser user) throws ServiceException, JDOMException, IOException {
		// remove the user from the role
		getPublishHandler().removeUserFromRole(user.getTitle(), getCopperCoreKey());
		// now update our list of other roles attached to this user
		String userRolesXML = getPublishHandler().getUserRoles(user.getTitle(), fParent);
		Element root = getPublishHandler().getJDomRootFromXMLString(userRolesXML);
		ArrayList<Integer> roleIds = new ArrayList<Integer>();
		getPublishHandler().getAllCCRoleIdsForUser(root, roleIds);

		if(roleIds.size()<1){
		// remove the user from the run because there are no other roles
			getPublishHandler().removeUserFromRun(user.getTitle(), fParent);
		}
		else if(roleIds.size()>0){		
			// reassign the active role
			int roleId = roleIds.get(0);
			getPublishHandler().setActiveRole(user.getTitle(), fParent, roleId);
		}			
		fUsers.clear();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IRole#setRoles(java.util.List)
	 */
	public void setUsers(List<IUser> users) {
		fUsers = users;
	}

}
