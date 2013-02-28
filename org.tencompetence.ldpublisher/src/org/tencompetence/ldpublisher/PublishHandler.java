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
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.UolDto;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.ldpublisher.connection.ILDConnection;
import org.tencompetence.ldpublisher.connection.impl.LDConnectionWebService;
import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IRun;
import org.tencompetence.ldpublisher.model.IUnitOfLearning;
import org.tencompetence.ldpublisher.model.IUser;
import org.tencompetence.ldpublisher.model.impl.Role;
import org.tencompetence.ldpublisher.model.impl.Run;
import org.tencompetence.ldpublisher.model.impl.UnitOfLearning;
import org.tencompetence.ldpublisher.model.impl.User;
import org.tencompetence.ldpublisher.upload.ICancelMonitor;
import org.tencompetence.ldpublisher.upload.IResourcePart;
import org.tencompetence.ldpublisher.upload.IUploadHandler;
import org.tencompetence.ldpublisher.upload.impl.ResourcePart;
import org.tencompetence.ldpublisher.upload.impl.UploadHandler;


/**
 * Concrete class for CopperCore publishing methods
 * 
 * @author Paul Sharples
 * @version $Id: PublishHandler.java,v 1.19 2009/10/14 16:20:13 phillipus Exp $
 *
 */
public class PublishHandler implements IPublishHandler {

	/*
	 * The path on the server to the ccsi dispatcher service
	 */
	public static final String fDispatcherPath = "/ccsi/services/Dispatcher"; //$NON-NLS-1$

	public String fContentUriOffset = null;
	
	/*
	 * This value is the address of the server - i.e. http://localhost:8080
	 */
	public String fCoppercoreServerUrl = null;
	
	/*
	 * Our handle to CopperCore
	 */
	public ILDConnection fEngine = null;

	private String fPublicationMessage = null;
	
	public PublishHandler(String coppercoreServerurl, String username, String password) throws MalformedURLException, RemoteException, ServiceException{
		fCoppercoreServerUrl = coppercoreServerurl;		
		fEngine = new LDConnectionWebService(username,password);
		fEngine.setDispatcherURL(fCoppercoreServerUrl + fDispatcherPath);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#addUserToRole(java.lang.String, int)
	 */
	public void addUserToRole(String userId, int roleId) throws MalformedURLException, RemoteException, ServiceException{
		fEngine.addUserToRole(userId, roleId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#addUserToRun(java.lang.String, int)
	 */
	public void addUserToRun(String userId, int runId) throws MalformedURLException, RemoteException, ServiceException{
		fEngine.addUserToRun(userId, runId);
	}
	
	/**
	 * Private method to - used to recursively search for roles within an xml fragment retrieved from CopperCore
	 * @param current - jdom element
	 * @param rolesTable - flat list of roles
	 */
	private void collectRolesFromJDom(Element current, List<IRole> roleList, int runId) {							
		if(current.getName().equalsIgnoreCase(IPublisherConstants.RoleType.learner.name()) || current.getName().equalsIgnoreCase(IPublisherConstants.RoleType.staff.name())){
			String roleType = current.getName();
			String id = current.getAttributeValue(IPublisherConstants.IDENTIFIER_ATTRIBUTE);			
			String min = current.getAttributeValue(IPublisherConstants.MIN_PERSONS_ATTRIBUTE);
			String max = current.getAttributeValue(IPublisherConstants.MAX_PERSONS_ATTRIBUTE);
			if(min==null) min="-1"; //$NON-NLS-1$
			if(max==null) max="-1"; //$NON-NLS-1$
			Element titleElement = current.getChild(IPublisherConstants.TITLE_ELEMENT);
			String title = IPublisherConstants.UNTITLED_ROLE;
			if (titleElement != null){
				title = titleElement.getText();
			}
			roleList.add(new Role(Integer.parseInt(id), title, Integer.parseInt(min), Integer.parseInt(max), runId, roleType ,this));
		}
		
		List<?> children = current.getChildren();
		Iterator<?> iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			collectRolesFromJDom(child, roleList, runId);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#createRunForUol(int, java.lang.String)
	 */
	public int createRunForUol(int uolId, String runTitle) throws MalformedURLException, RemoteException, ServiceException {
		return fEngine.createRun(uolId, runTitle);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#createUser(java.lang.String)
	 */
	public void createUser(String userId) throws MalformedURLException, RemoteException, ServiceException{
		fEngine.createUser(userId);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#findRunFromTitle(int, java.lang.String)
	 */
	public int findRunFromTitle(int uolId, String title) throws MalformedURLException, RemoteException, ServiceException {
		int runId = -1;
		RunDto[] dto = fEngine.listRuns(uolId);
		if (dto == null) return runId;
		for (int i=0;i<dto.length;i++) {
			if (dto[i].getTitle().equals(title)) {
				runId = dto[i].getRunId();
				break;
			}
		}
		return runId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#findUolInCC(java.lang.String)
	 */
	public int findUolInCC(String uriOfUol) throws MalformedURLException, RemoteException, ServiceException {
		int uolId = -1;
		// get the correct UOL
		UolDto[] dto = fEngine.listUols();
		if (dto == null) return uolId;
		for (int i=0;i<dto.length;i++) {
			if (dto[i].getUri().equals(uriOfUol)) {
				uolId = dto[i].getId();
				break;
			}
		}
		return uolId;
	}

	public String getActivityTree(String userId, int runId) throws MalformedURLException, RemoteException, ServiceException{
		return fEngine.getActivityTree(userId, runId);
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#getAllCCRoleIdsForUser(org.jdom.Element, java.util.ArrayList)
	 */
	public void getAllCCRoleIdsForUser(Element current, ArrayList<Integer> ccRolesFound){		
		if(current.getName().equalsIgnoreCase(IPublisherConstants.RoleType.learner.name()) || current.getName().equalsIgnoreCase(IPublisherConstants.RoleType.staff.name())){			
			String id = current.getAttributeValue(IPublisherConstants.IDENTIFIER_ATTRIBUTE);						
			if (id != null){
				ccRolesFound.add(Integer.parseInt(id));
			}			
		}		
		List<?> children = current.getChildren();
		Iterator<?> iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			getAllCCRoleIdsForUser(child, ccRolesFound);					
		}
	}
	
	public String getContent(String userId, int runId, String id, String type) throws MalformedURLException, ServiceException, RemoteException{
		 return fEngine.getContent(userId, runId, id, type);
	 }

	
	public String getEnvironmentTree(String userId, int runId,
			String[] environmentId) throws MalformedURLException,
			ServiceException, RemoteException {
		return fEngine.getEnvironmentTree(userId, runId, environmentId);
	}
	
	
	public Element getJDomRootFromXMLString(String xml) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xml)).getRootElement();
		return root;
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#getLastPublicationMessage()
	 */
	public String getLastPublicationMessage() {
		return fPublicationMessage;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#parseRoles(int)
	 */
	public List<IRole> getRoles(int runId) throws MalformedURLException, RemoteException, JDOMException, IOException, ServiceException{
		Element root = getJDomRootFromXMLString(getRolesTree(runId));		
		List<IRole> roles = new ArrayList<IRole>();					
		collectRolesFromJDom(root, roles, runId);				
		return roles;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#getRolesTree(int)
	 */
	public String getRolesTree(int runId) throws MalformedURLException, RemoteException, ServiceException {
		return fEngine.getRolesTree(runId);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#getRunsForUol(int)
	 */
	public List<IRun> getRunsForUol(int uolId) throws MalformedURLException, RemoteException, ServiceException {
		List<IRun> runList = new ArrayList<IRun>();	
		RunDto[] runDtos = fEngine.listRuns(uolId);
		if(runDtos == null) {
		    return runList;
		}
		for (RunDto runDto : runDtos) {
			IRun aRun = new Run(runDto.getRunId(), runDto.getTitle(), this);
			runList.add(aRun);			
		}
		return runList;
	}
	  
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#getUnitsOfLearning()
	 */
	public List<IUnitOfLearning> getUnitsOfLearning() throws MalformedURLException, RemoteException, ServiceException {
		List<IUnitOfLearning> uolList = new ArrayList<IUnitOfLearning>();
		UolDto[] uolDtos = fEngine.listUols();
		if(uolDtos == null) {
		    return uolList;
		}
		for(UolDto uol : uolDtos){
		    IUnitOfLearning aUol = new UnitOfLearning(uol.getId(), uol.getTitle(), this);
		    uolList.add(aUol);
		}
		return uolList;
	}
	
	
	
	public String getUser(String userId) throws MalformedURLException,
			RemoteException, ServiceException {
		return fEngine.getUser(userId);
	}
	
	public String getUserRoles(String userId, int runId)
			throws MalformedURLException, RemoteException, ServiceException {
		return fEngine.getUserRoles(userId, runId);
	}	

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#addActiveUsersToRoles(int, org.tencompetence.ldpublisher.model.IRole)
	 */
	public List<IUser> getUsersFromRole(int runId, IRole role) throws ServiceException, JDOMException, IOException {		
		//String userRolesXML;
		List<IUser> usersFound = new ArrayList<IUser>();
		
		// TODO !!!!!!!!!!!!!!!!!!!!!!!!
		// Test this more
		RunParticipationDTO[] participants = fEngine.listUsersInRole(runId, role.getCopperCoreKey());
		if (participants == null) return usersFound;
		for(RunParticipationDTO participant : participants){
			usersFound.add(new User(participant.getUserId()));
		}
		
		
		/*
		 // THIS WAS THE OLD SLOW VERSION MAKING TOO MANY WEB REQUESTS
		RunParticipationDTO[] participants = fEngine.listUsersInRun(runId);
		if (participants == null) return usersFound;
		
		for(RunParticipationDTO participant : participants){
				userRolesXML = getUserRoles(participant.getUserId(), runId);
				Element root = getJDomRootFromXMLString(userRolesXML);
				populateUsersInRole(participant.getUserId(), role.getCopperCoreKey(), root, usersFound);															
		}
		*/
		return usersFound;
	}
	
	@SuppressWarnings("unused")
    private void populateUsersInRole (String userId, int ccRoleId, Element current, List<IUser> usersFound){
		ArrayList<Integer> roleIds = new ArrayList<Integer>();
		getAllCCRoleIdsForUser(current, roleIds);
		for(Integer roleId : roleIds){
			if(roleId == ccRoleId){
				usersFound.add(new User(userId));
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#publishUol(java.io.File, org.tencompetence.ldpublisher.ICancelMonitor)
	 */
	public boolean publishUol(File file, ICancelMonitor cancelMonitor) throws HttpException, IOException {
		boolean upLoadSuccess = false;
		// upload & publish
		IUploadHandler uploadHandler = new UploadHandler(fCoppercoreServerUrl);

		if (file.exists()) {
			IResourcePart resPart = new ResourcePart(file);
			fPublicationMessage = uploadHandler.sendData(resPart.getResourceParts(), cancelMonitor);
			if (!fPublicationMessage.contains("Validation failed")) {//$NON-NLS-1$
				upLoadSuccess = true;
			}
		}
		return upLoadSuccess;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#removeRun(int)
	 */
	public void removeRun(int runId) throws MalformedURLException, RemoteException, ServiceException {
		fEngine.removeRun(runId);
	}
	
	public void removeUnitOfLearning(IUnitOfLearning uol) throws MalformedURLException, RemoteException, ServiceException {
		if (fContentUriOffset == null){
			fContentUriOffset = IPublisherConstants.DEFAULT_CONTENT_URI_OFFSET;
		}		
		fEngine.removeUol(uol.getCopperCoreKey(), fContentUriOffset);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#removeUserFromRole(java.lang.String, int)
	 */
	public void removeUserFromRole(String userId, int roleId) throws MalformedURLException, ServiceException, RemoteException{
		fEngine.removeUserFromRole(userId, roleId);
	}
	
	 /*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#removeUserFromRun(java.lang.String, int)
	 */
	public void removeUserFromRun(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException{
		fEngine.removeUserFromRun(userId, runId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#setActiveRole(java.lang.String, int, int)
	 */
	public void setActiveRole(String userId, int runId, int roleId) throws MalformedURLException, ServiceException, RemoteException{
		fEngine.setActiveRole(userId, runId, roleId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.IPublishHandler#setContentFolderOffset(java.lang.String)
	 */
	public void setContentFolderOffset(String path) {
		fContentUriOffset = path;		
	}
	
}
