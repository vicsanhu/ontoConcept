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
package org.tencompetence.ldpublisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IRun;
import org.tencompetence.ldpublisher.model.IUnitOfLearning;
import org.tencompetence.ldpublisher.model.IUser;
import org.tencompetence.ldpublisher.upload.ICancelMonitor;

/**
 * Exposed publishing methods
 * 
 * @author Paul Sharples
 * @version $Id: IPublishHandler.java,v 1.18 2009/05/19 18:18:48 phillipus Exp $
 *
 */
public interface IPublishHandler {
	
	/**
	 * Add a given user to a role
	 * @param userId
	 * @param roleId
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	void addUserToRole(String userId, int roleId)
			throws MalformedURLException, RemoteException, ServiceException;

	/**
	 * Add a given user to a run
	 * @param userId
	 * @param runId
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */	
	void addUserToRun(String userId, int runId)
			throws MalformedURLException, RemoteException, ServiceException;

	/**
	 * Create a new run for the given uol with supplied title
	 * 
	 * @param uolId
	 * @param runTitle
	 * @return - the CC db key for the new run
	 * @throws ServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	int createRunForUol(int uolId, String runTitle)
			throws MalformedURLException, RemoteException, ServiceException;

	/**
	 * Create a user in CopperCore
	 * @param userId
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	void createUser(String userId) throws MalformedURLException,
			RemoteException, ServiceException;

	/**
	 * Find a run in CopperCore from its title
	 * @param uolId
	 * @param title
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	int findRunFromTitle(int uolId, String title)
			throws MalformedURLException, RemoteException, ServiceException;

	/**
	 * Find a Unit of Learning in CopperCpre by its uri attribute (assumed to be
	 * unique)
	 * 
	 * @param uriOfUol
	 * @return - the found uol key or -1 denoting it wasnt found
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	int findUolInCC(String uriOfUol) throws MalformedURLException,
			RemoteException, ServiceException;

	String getActivityTree(String userId, int runId)
			throws MalformedURLException, RemoteException, ServiceException;

	void getAllCCRoleIdsForUser(Element current, ArrayList<Integer> ccRolesFound);

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws IOException
	 * @throws JDOMException
	 */
	/*
	ICopperCoreModel getModel() throws ServiceException, IOException, JDOMException;
	*/

	String getContent(String userId, int runId, String id, String type) throws MalformedURLException, ServiceException, RemoteException;

	public String getEnvironmentTree(String userId, int runId,
			String[] environmentId) throws MalformedURLException,
			ServiceException, RemoteException;

	Element getJDomRootFromXMLString(String xml) throws JDOMException,
			IOException;

	/**
	 * Retrieve the last message from the CopperCore publication routine useful
	 * for when a publish has failed
	 * 
	 * @return - the last message from the server
	 */
	String getLastPublicationMessage();

	/**
	 * 
	 * @param run
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws ServiceException
	 */
	/*
	void populateRolesFromRun(IRun run) throws MalformedURLException, RemoteException, JDOMException, IOException, ServiceException;
	 */
	
	/**
	 * 
	 * @param uol
	 * @throws ServiceException
	 * @throws JDOMException
	 * @throws IOException
	 */
	/*
	void populateRunsFromUol(IUnitOfLearning uol) throws ServiceException, JDOMException, IOException;
	 */
	
	/**
	 * Returns a list of IRoleDataModelBeans populated from what was found in getRolesTree(int runId)
	 * @param runId
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws ServiceException
	 */
	List<IRole> getRoles(int run)
			throws MalformedURLException, RemoteException, JDOMException,
			IOException, ServiceException;
	
	
	/**
	 * Get the xml snippet from CopperCore containing the role information on this run
	 * @param runId
	 * @return - the xml snippet
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	String getRolesTree(int runId) throws MalformedURLException,
			RemoteException, ServiceException;
	
	/**
	 * get the runs for a particular unit of learning
	 * 
	 * @param uolId -the CC id of this uol
	 * @return - Hashtable keyed with the CC DB key and value being the title of the run
	 * @throws ServiceException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	List<IRun> getRunsForUol(int uolId)
			throws MalformedURLException, RemoteException, ServiceException;
	
	/**
	 * 
	 * @return A list of units of learning
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	List<IUnitOfLearning> getUnitsOfLearning() 
			throws MalformedURLException, RemoteException, ServiceException;
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	String getUser(String userId) throws MalformedURLException, RemoteException, ServiceException;
	
	String getUserRoles(String userId, int runId) throws MalformedURLException, RemoteException, ServiceException;
	
	/**
	 * Populate the local model for a given Coppercore instance.
	 * @param coppercoreServerurl
	 * @return a list of uols with populated runs, roles & users found in CopperCore
	 * @throws ServiceException
	 * @throws JDOMException
	 * @throws IOException
	 */
	/*
	List<IUnitOfLearning> populateUols() throws ServiceException, JDOMException, IOException;
	*/
	/**
	 * Is passed an IRoles object and runId.  It will populate the IRole with a list of IUsers found under
	 * the specific role id in CopperCore
	 * @param runId
	 * @param role
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	List<IUser> getUsersFromRole(int runId, IRole role) throws MalformedURLException, RemoteException, ServiceException, JDOMException, IOException;
	
	/**
	 * Publish a url to the CopperCore server
	 * 
	 * @param file
	 * @return - true or false whether it was validated
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	boolean publishUol(File file, ICancelMonitor cancelMonitor) throws FileNotFoundException, HttpException, IOException;
	
	/**
	 * 
	 * @param runId
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	void removeRun(int runId) throws MalformedURLException, RemoteException, ServiceException;	

	/**
	 * 
	 * @param uol
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	void removeUnitOfLearning(IUnitOfLearning uol) throws MalformedURLException, RemoteException, ServiceException;

	/**
	 * Removes the specified user from a role in CopperCore
	 * @param userId
	 * @param roleId
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void removeUserFromRole(String userId, int roleId)
			throws MalformedURLException, ServiceException, RemoteException;
	
	
	
	/**
	 * Removes the specified user from a run in CopperCore
	 * @param userId
	 * @param runId
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteExceptione
	 */
	void removeUserFromRun(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException;

	/**
	 * Sets a users active role
	 * @param userId
	 * @param runId
	 * @param roleId
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	void setActiveRole(String userId, int runId, int roleId)
			throws MalformedURLException, ServiceException, RemoteException;

	/**
	 * 
	 * @param path
	 */
	void setContentFolderOffset(String path);
}
