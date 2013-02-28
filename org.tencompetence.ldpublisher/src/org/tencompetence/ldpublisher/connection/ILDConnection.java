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
package org.tencompetence.ldpublisher.connection;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import org.coppercore.common.Message;
import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.UolDto;

/**
 * Exposed Connection methods - specific to the axis web service with CCSI
 * 
 * @author Paul Sharples
 * @version $Id: ILDConnection.java,v 1.4 2009/01/15 16:08:49 ps3com Exp $
 *
 */
public interface ILDConnection {

	/**
	 * Adds the specified user to the role.
	 * <p>
	 * The user will also be added to all parent roles of this role.
	 * <p>
	 * Adding a user to a role may violate restrictions placed on the role using the following
	 * attributes in IMS LD: imsld:match-persons, imsld:max-persons. <br>
	 * The method will add the user without checking these constraints. It is up to the client to
	 * check the role-assigments by calling validateRoles.
	 * 
	 * @param userId
	 *        is the id of the user
	 * @param roleId
	 *        is the id of the role to add the user to
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void addUserToRole(String userId, int roleId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Adds an existing user to an existing run.
	 * 
	 * <p>
	 * The user is also assigned to the root role of the rolestree of this run but the activeRole of
	 * the RunParticipation is not set by this method.
	 * 
	 * <p>
	 * All conditions of this run/unit-of-learning are evaluated once to initialize the run for this
	 * user.
	 * 
	 * @param userId
	 *        the id of the user to assign to a run
	 * @param runId
	 *        the id of the run the user is assigned to
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void addUserToRun(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Modifies the run identified by runDto.runId.
	 * 
	 * <p>
	 * Only the title and/or start time of the run can be changed, the other fields of runDto are
	 * ignored.
	 * 
	 * @param runDto
	 *        contains the runId of the run to change and the new title, starttime of the run
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public void changeRun(RunDto runDto) throws MalformedURLException, ServiceException, RemoteException;
	
    /**
     * Clears the local cache for the given session. A cache may become dirty,
     * due to an re-publication.
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void clearCache(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
	
	 /**
	 * Sets the state of the activity to completed.
	 *
	 * <p>The method also implements all actions implied by the learning design like completing a
	 * rolepart, completing an act setting properties etc.
	 *
	 * @param userId - the user
	 * @param runId - given run id
	 * @param id is the ld identifier of the activity to complete
	 * @param type String defines if it is a learning-activity or a support-activity.	 
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void completeActivity(String userId, int runId, String id, String type) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Creates a new role instance for the specified role.
	 * 
	 * <p>
	 * The new role instance will be created in the context of the specified run. <br>
	 * This operation is only allowed if the role has the attribute imsld:create-new allowed. If the
	 * role has the attribute imsld:create-new not-allowed the method will throw an exception.
	 * 
	 * @param runId
	 *        is the run where the new role will be created
	 * @param roleId
	 *        is the id of the role to create the instance from
	 * @return the id of the new role instance
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public int createRole(int runId, String roleId) throws MalformedURLException, ServiceException, RemoteException;

    /** Creates a new run using the parameters specified in runDto.
     * 
     * <p>
     * The following fields of runDto are used:
     * <ul>
     * <li>RunDto.uolId is the id of the Uol the new run belongs to</li>
     * <li>RunDto.title is the title of the new run</li>
     * <li>runDto.starttime is the starttime of the run, if this parameter is null, the current
     * date/time is used as the starttime</li>
     * </ul>
     * 
     * <p>
     * After the run is created a new instance of the roletree is created.
     * 
     * @param runDto
     *        specifies the parameters for the new run.
     * @return the id of the newly created run.
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public int createRun(int uolId, String runTitle) throws MalformedURLException, ServiceException, RemoteException;

	/**
     * Creates a new run using the parameters specified in runDto.
     * 
     * <p>
     * The following fields of runDto are used:
     * <ul>
     * <li>RunDto.uolId is the id of the Uol the new run belongs to</li>
     * <li>RunDto.title is the title of the new run</li>
     * <li>runDto.starttime is the starttime of the run, if this parameter is null, the current
     * date/time is used as the starttime</li>
     * </ul>
     * 
     * <p>
     * After the run is created a new instance of the roletree is created.
     * 
     * @param runDto
     *        specifies the parameters for the new run.
     * @return the id of the newly created run.
     * @throws CopperCoreException 
     *         if the unit of learning cannot be found.
     * @throws RemoteException
     */
    public int createRun(RunDto runDto) throws MalformedURLException, ServiceException, RemoteException;
	
    /**
     * Adds a new user to CopperCore.
     *  <p>
     * 
     * @param userId
     *        the id of the new user
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void createUser(String userId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
     * Returns the Learning Design identifier of the active role of the specified user in the
     * specified run.
     * 
     * <p>
     * If there is no active role assigned, the method returns an empty string.
     * 
     * @param userId
     *        is the id of the user
     * @param runId
     *        is the id of the run
     * @return a String specifying the LD id of the active role.
     * @throws CopperCoreException 
     *         if either the run or the user cannot be found.
     * @throws RemoteException 
     */    
    public String getActiveLDRole(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
		
    /**
     * Returns the id of the active role of the specified user in the specified run.
     * 
     * <p>
     * If there is no active role assigned, the method returns -1.
     * 
     * @param userId
     *        is the id of the user
     * @param runId
     *        is the id of the run
     * @return an int specifying the id of the active role.
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public int getActiveRole(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Gets the activity tree for the user in the specified run.
	 *
	 * <p>The tree is expressed in xml. The tree is personalized, meaning that all properties are filled
	 * in with their actual values for that user.
	 *
	 * @return the xml representation for the personalized activity-tree
	 * @param userId
	 * @param runId
	 * @return - xml representation
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public String getActivityTree(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
    
	/**
	 * Returns the content for the specified ld element.
	 *
	 * <p>The content is expressed in xml and is personalized for the specified user in the active role within a run.
	 *
	 * @param id is the identifier of the ld element to retrieve
	 * @param type String defines the the LD type of the element to retrieve
	 * @param userId - the user id
	 * @param runId - the run id
	 * @return xml representation
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
    public String getContent(String userId, int runId, String id, String type) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the content uri of the specified unit of learning.
     * 
     * @param uolId
     *        is the id of the unit of learning
     * @return the content uri, if the uri is not set, it returns null
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getContentUri(int uolId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the content uri of the specified unit of learning.
     * 
     * @param uolId
     *        is the id of the unit of learning
     * @param runId - the run identifier        
     * @return the content uri, if the uri is not set, it returns null
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getContentUri(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Gets the specified environment tree for the specified user in the specified run. The tree is
     * expressed in xml. The tree is personalized
     *
     * @param userId - the user
     * @param runId - the run
     * @param environmentId [] String array containing the identifiers of the environment trees to
     *   retrieve
     * @return the xml representation of the requested environment tree
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getEnvironmentTree(String userId, int runId, String[] environmentId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the personalized IMS LD global content in the context of the specified user.
     *
     * <p> Global content is xml content referenced from the ims learning design and included in the
     * ims ld package. The content is specifically tagged as being global content. This content may
     * include ims ld global elements like set-property and view-property. The content is personalized
     * by adding actual user data to the global elements that are present in the content.
     *
     * @param userId - the user id
     * @param runId - the run id
     * @param url String a reference to the resource containing the global content
     * @return String the personalized version of the global content
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getGlobalContent(String userId, int runId, String url) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the personalized IMS LD global content in the context of the specified
     * supported-person.
     *
     * <p> Global content is xml content referenced from the ims learning design and included in the
     * ims ld package. The content is specifically tagged as being global content. This content may
     * include ims ld global elements like set-property and view-property. The content is personalized
     * by adding actual user data to the global elements that are present in the content.
     * 
     * @param userId - the user id
     * @param runId - the run id
     * @param url String a reference to the resource containing the global content
     * @param supportedPerson String the identifier of the supported-person
     * @return String the personalized version of the global content
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getGlobalContentForSupportedPerson(String userId, int runId, String url, String supportedPerson) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
	 * Returns the value of the specified property.
	 * 
	 * <p>
	 * The returned value is the primitive value of the property, for instance if the property is an
	 * integer property the integer value is returned as a String representing this value.
	 * 
	 * <p>
	 * Depending on the scope of the property, not all parameters are required. For a global personal
	 * property for example the uolId and runId parameters are not required and can be left null.
	 * 
	 * @param uolId
	 *        is the id of the unit-of-learning the property belongs to
	 * @param propId
	 *        is the id of the property
	 * @param ownerId
	 *        is the id of the owner the property belongs to
	 * @param runId
	 *        is the id of the run the property belongs to
	 * @return a String containing the value of the specified property
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public String getProperty(int uolId, String propId, String ownerId, int runId) throws MalformedURLException, ServiceException, RemoteException;
    
	/**
	 * Returns the xml tree for the specified run which contains all roles and role instances for that
	 * run.
	 * 
	 * @param runId
	 *        is the id of the run to get the role tree for
	 * @return a String containing an xml representation of all roles for that run
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public String getRolesTree(int runId) throws MalformedURLException, ServiceException, RemoteException;
    
	/**
	 * Returns the data of the run.
	 * 
	 * @param runId
	 *        the id of the run to find
	 * @return a RunDto object describing the run
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public RunDto getRun(int runId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the data of the uol.
     * 
     * @param uolId
     *        the id of the uol to find
     * @return a UolDto object describing the uol
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public UolDto getUol(int uolId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the specified user.
     * 
     * <p>
     * Because the user only contains the userid, this method in effect only checks if the specified
     * user exists.
     * 
     * @param userId
     *        the id of the user to find
     * @return the userid of the found user
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getUser(java.lang.String userId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Retrieves an xml representation of the hierarchy with all the roles where the user is assigned
     * to within the context of the specified run.
     * 
     * @param userId
     *        is the id of the user to retrieve the role tree for
     * @param runId
     *        is the id of the run
     * @return an xml representation of the personalized role tree for the specified user for the
     *         given run context
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getUserRoles(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
	 * Get the version of CopperCore
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
    public String getVersion() throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns the version of the deployed CopperCore engine.
     * 
     * <p>
     * If no version number was defined or the version number could not be read, null will be returned
     * instead.
     * </p>
     * 
     * @return String the version of the deployed CopperCore engine or null if no version number could
     *         be determined
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public RunParticipationDTO[] listAllRunsForUser(String userId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
	 * Returns an <code>Array</code> of <code>RunDto</code>'s for all runs of the specified
	 * unit of learning.
	 * 
	 * <p>
	 * If there are no runs, the method returns an empty collection
	 * 
	 * @param uolId
	 *        the id of the unit of learning to get the runs for.
	 * @return a Collection containing all RunDto's.
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public RunDto[] listRuns(int uolID) throws MalformedURLException, ServiceException, RemoteException;
    
	/**
	 * Returns an aaray of all runs in the given unit of learning for a given user.
	 * 
	 * <p>
	 * Each run is represented by a RunParticipationDto. If there are no runs for the given user, the
	 * method returns an empty collection.
	 * 
	 * @param userId
	 *        the user to search the runs for
	 * @param uolId
	 *        the unit of learning where the runs belong to
	 * @return an array containing all RunParticipationDto's for the given user and
	 *         unit-of-learning.     
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public RunParticipationDTO[] listRunsForUser(String userId, int uolId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
	 * Returns an Array containing <code>UolDto</code>'s for all unit of learnings that are
	 * present in CopperCore.
	 * 
	 * <p>
	 * If no Uol's are found, the method returns an empty collection.
	 * 
	 * @return an array of all UolDto's
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public UolDto[] listUols() throws MalformedURLException, ServiceException, RemoteException;
    
	/**
	 * Returns an array containing <code>UolDto</code>'s for all unit-of-learning's for the
	 * given user.
	 * 
	 * <p>
	 * A user is assigned to a uol if he has assigned to a run for this uol. <br>
	 * If the user is not assigned to any uol, the method returns an empty collection.
	 * 
	 * @param userId
	 *        the id of the user to look up
	 * @return an array containing all uols for the user
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public UolDto[] listUolsForUser(String userId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * get a list of  current users in database
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public String[] listUsers() throws MalformedURLException, ServiceException, RemoteException;
    
    public RunParticipationDTO[] listUsersInRole(int runId, int roleInstanceId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Returns an array of all users in the given run.
     * 
     * <p>
     * Each individual user is represented with a RunParticipationDTO. If there are no users found,
     * the method returns an empty collection.
     * 
     * @param runId
     *        the id of the run to retrieve the users for.
     * @return an array containing all RunParticipationDTO's.      
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public RunParticipationDTO[] listUsersInRun(int runId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Send Notifications as defined in the notificationXml.
     * 
     * @param userId - the user id
     * @param runId - the run id
     * @param notificationXml String the Xml repesentation of the notifications
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public void notify(String userId, int runId, String notificationXml) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Publishes the ims package to CopperCore.
     * 
     * <p>
     * Before the package is imported in CopperCore the method first validates the package. If there
     * are errors the package is not published.
     * 
     * @param imsPackageFilename
     *        String specifies the file containing the IMS LD package
     * @param schemaLocation
     *        String filelocation where the xml schemas can be found. These schemas are needed for the
     *        xml schema validation of the package
     * @param contentUriOffset
     *        String href prefix to point to the included content
     * @param contentFolderOffset
     *        String prefix pointing to the file location where the content is stored
     * @return PublicationResult containing the outcome of the publication process.
     * @throws CopperCoreException 
     */
    public Message[] publishUol(String imsPackageFilename, String schemaLocation, String contentUriOffset, String contentFolderOffset) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
     * Removes a run from the system.
     * 
     * <p>
     * Removing a run involves removing all users from the run and removing all properies from the
     * system that are either directly related to the run or to the users in the run.
     * </p>
     * 
     * @param runId
     *        the id of the run to be removed
     * @throws CopperCoreException 
     *         if the run cannot be found
     * @throws RemoteException 
     */
    public void removeRun(int runId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
     * Removes an uol from the system.
     * 
     * <p>
     * Removing a uol involves removing all runs from the uol and removing all properies from the
     * system that are related to the uol.
     * </p>
     * 
     * @param uolId
     *        the id of the uol to be removed
     * @param contentFolderOffset
     *        String offset to the location that contains the resource to be removed
     * @throws CopperCoreException 
     *         if the uol cannot be found
     * @throws RemoteException 
     */
    public void removeUol(int uolId, String contentFolderOffset) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Removes a user from the system.
     * 
     * <p>
     * Removing a user will remove all properties belonging to the user from the system as well.
     * </p>
     * 
     * @param userId
     *        the userid of the user to be removed
     * @throws CopperCoreException 
     * @throws RemoteException 
     */
    public void removeUser(String userId) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Removes the specified user to the role.
     * 
     * <p>
     * Removing a user from a role may violate restrictions placed on the role using the following
     * attribute in IMS LD: imsld:min-persons. <br>
     * The method will remove the user without checking this constraints. It is up to the client to
     * check the role-assigments by calling validateRoles.
     * 
     * @param userId
     *        is the id of the user
     * @param roleId
     *        is the id of the role to remove the user from	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void removeUserFromRole(String userId, int roleId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Removes the user from the specified run by deleting the RunParticipation.
	 * 
	 * @param userId
	 *        the id of the user to delete from the run
	 * @param runId
	 *        the run from which the user has to be deleted
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public void removeUserFromRun(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException;
	
	/**
	 * Sets the active role for the user in the specified run.
	 * 
	 * @param userId
	 *        is the id of the user
	 * @param runId
	 *        is the id of the run to set the active role for
	 * @param roleId
	 *        is the id of the active role instance.
	 * @throws RemoteException 
	 *         if the user, run or activeRole could not be found or if the user is not assigned to the
	 *         activeRole, or as the activeRole is not assigned to the run.
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public void setActiveRole(String userId, int runId, int roleId) throws MalformedURLException, ServiceException, RemoteException;
	
    /**
     * Sets the content uri of the specified unit of learning.
     * <p>
     * This uri is the offset that wil be used to locate all content present in the ims ld content
     * package. The engine does not check the uri, it just adds the uri in front of all references to
     * the local resources.
     * 
     * @param uolId
     *        is the id of the unit-of-learning
     * @param contentUri
     *        contains the uri offset of the local resources
     * @throws CopperCoreException 
     * @throws RemoteException 
     * @see #getContentUri
     */
    public void setContentUri(int uolId, String contentUri) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
	 * Sets the URL of where the dispatcher service can be found
	 * @param dispatcherURL
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public void setDispatcherURL(String dispatcherURL) throws MalformedURLException, RemoteException, ServiceException;
	
	/**
	 * Sets the property of the specified owner to the given value.
	 *
	 * <p>The new value of the property is checked to see if the datatypes match and if the value does
	 * not violate any of the optional restrictions that are specified in learning design. If the
	 * value is not valid a RemoteException is raised describing the error encountered. The value of
	 * the property is not changed.
	 *
	 * <p> After setting the new value all depended processing is performed like evaluating conditions.
	 *
	 * <p> Depending on the type of the property, the ownerId either is the userid, the
	 * supported-person id or the role-id.
	 *
	 * @param userId
	 * @param runId
	 * @param propId String the ld identifier of the property
	 * @param ownerId String the identifier of the owner of the property
	 * @param value String the new value for the property
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
    public void setProperty(String uerId, int runId, String propId, String ownerId, String value) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Sets the value of the specified property.
     * 
     * <p>
     * The value is a String representing the actual value of the property. When storing, the property
     * validates if the value of of the correct type for the specific property.
     * 
     * <p>
     * Depending on the scope of the property, not all parameters are required. For a global personal
     * property for example the uolId and runId parameters are not required and can be left null.
     * 
     * @param uolId
     *        is the id of the unit-of-learning the property belongs to
     * @param propId
     *        is the id of the property
     * @param ownerId
     *        is the id of the owner the property belongs to
     * @param runId
     *        is the id of the run the property belongs to
     * @param value
     *        is the String represention of the value to be stored.
     * @throws CopperCoreException 
     * @throws RemoteException 
     * @see #getProperty
     */
    public void setPropertyAdmin(int uolId, String propId, String ownerId, int runId, java.lang.String value) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Validates the package.      (Note* Validates an already *UPLOADED IMS LD package)
     * 
     * <p>
     * During validation the structure of the package is checked, the manifest is checked against the
     * xml schemas and semantic validation is performed. The validator tries to find as many errors as
     * possible by not stopping after a single error. All errors found are reported back in the
     * messageLiast parameter.
     * 
     * @param imsPackageFilename
     *        String contains the filename of the IMS LD package
     * @param schemaLocation
     *        String filelocation where the xml schemas can be found. These schemas are needed for the
     *        xml schema validation of the package
     * @return ValidationResult the result of the validation.
     * 
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public Message[] validate(String imsPackageFilename, String schemaLocation) throws MalformedURLException, ServiceException, RemoteException;
    
    /**
     * Validates all constraints that are imposed uppon the roles by the author.
     * <p>
     * The following attributes are checked for all roles in the specified run:
     * <ul>
     * <li>imsld:match-persons</li>
     * <li>imsld:max-persons></li>
     * <li>imsld:min-persons</li>
     * </ul>
     * 
     * @param runId
     *        is the id of the run to validate the roles for
     * @return true if no constrains are validated, otherwise return false
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws RemoteException
     */
    public boolean validateRoles(int runId) throws MalformedURLException, ServiceException, RemoteException;   	
}
