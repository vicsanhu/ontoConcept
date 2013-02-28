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
package org.tencompetence.ldpublisher.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.jdom.JDOMException;

/**
 * Exposed role methods
 * 
 * @author Paul Sharples
 * @version $Id: IRole.java,v 1.12 2008/10/24 17:30:21 ps3com Exp $
 *
 */
public interface IRole extends ICCObject {
		
	/**
	 * get the min persons
	 * @return
	 */
	int getMinPersons();

	/**
	 * get the max persons
	 * @return
	 */
	int getMaxPersons();
	
	/**
	 * set the roles
	 * @param userss
	 */
	void setUsers(List<IUser> userss);
	
	/**
	 * Get available roles
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	List<IUser> getUsers() throws MalformedURLException, RemoteException, ServiceException, JDOMException, IOException;
	
	/**
	 * checks if there are the mimimum amount of users on a given role
	 * @return
	 */
	public boolean hasMinimumUsers();
	
	/**
	 * checks if the maximum amount of users on a given role has been reached
	 * @return
	 */
	public boolean hasMaximumUsers();
	
	/**
	 * 
	 * @param user
	 * @param role
	 * @throws Exception 
	 */
	void addUserToRole(IUser user) throws Exception ;
	
	/**
	 * 
	 * @param user
	 * @param role
	 * @throws IOException 
	 */
	void removeUserFromRole(IUser user) throws MalformedURLException, RemoteException, ServiceException, JDOMException, IOException;
	

	/**
	 * 
	 * @param userId
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 * @throws Exception 
	 */
	IUser createUser(String userId) throws Exception;
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 * @throws Exception
	 */
	IUser getUser(String userId) throws MalformedURLException, RemoteException, ServiceException, Exception;
	
	/**
	 * 
	 * @return
	 */
	/*
	IRun getParent();
	*/

}