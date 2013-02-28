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
package org.tencompetence.ldpublisher.connection.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import org.coppercore.ccsi.DispatcherServiceLocator;
import org.coppercore.ccsi.DispatcherWS;
import org.coppercore.ccsi.adapters.CopperCoreAdapterServiceLocator;
import org.coppercore.ccsi.adapters.CopperCoreAdapterWS;
import org.coppercore.ccsi.adapters.CopperCoreAdminAdapterServiceLocator;
import org.coppercore.ccsi.adapters.CopperCoreAdminAdapterWS;
import org.coppercore.common.Message;
import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.UolDto;
import org.tencompetence.ldpublisher.connection.ILDConnection;

/**
 * WebService connection class to CCSI
 * (based on the Sled version)
 * 
 * @author Paul Sharples
 * @author Alex Little
 *
 * @version $Id: LDConnectionWebService.java,v 1.5 2009/10/14 16:20:13 phillipus Exp $
 * 
 */
public class LDConnectionWebService implements ILDConnection {

	private String fDispatcherURL;
	private String fLdAdapterURL;
	private String fLdAdminAdapterURL;
	private String fPassword = null;
	private String fUsername = null;
	
	public LDConnectionWebService(String username, String password){
		fUsername = username;
		fPassword = password;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#addUserToRole(java.lang.String, int)
	 */
	public void addUserToRole(String userId, int roleId) throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.addUserToRole(userId,roleId);	
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#addUserToRun(java.lang.String, int)
	 */
	public void addUserToRun(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.addUserToRun(userId,runId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#changeRun(org.coppercore.dto.RunDto)
	 */
	public void changeRun(RunDto runDto) throws MalformedURLException,
			ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.changeRun(runDto);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#clearCache(java.lang.String, int)
	 */
    public void clearCache(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));		
		doAuth(port);
		port.clearCache(userId, runId);
	}

    /*
     * (non-Javadoc)
     * @see org.tencompetence.ldpublisher.connection.ILDConnection#completeActivity(java.lang.String, int, java.lang.String, java.lang.String)
     */
	public void completeActivity(String userId, int runId, String id,
			String type) throws MalformedURLException, ServiceException,
			RemoteException {
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));		
		doAuth(port);
		port.completeActivity(userId, runId, id, type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#createRole(int, java.lang.String)
	 */
	public int createRole(int runId, String roleId)
			throws MalformedURLException, ServiceException, RemoteException {
		int mRoleId = -1;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		mRoleId = port.createRole(runId, roleId);
		return mRoleId;
	}

	/*
     * (non-Javadoc)
     * @see org.tencompetence.ldpublisher.connection.ILDConnection#createRun(int, java.lang.String)
     */
	public int createRun(int uolId, String runTitle) throws MalformedURLException, ServiceException, RemoteException {
		int response = -1;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		RunDto newRun = new RunDto();
		newRun.setUolId(uolId);
		newRun.setTitle(runTitle);
		response = port.createRun(newRun);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#createRun(org.coppercore.dto.RunDto)
	 */
	public int createRun(RunDto runDto) throws MalformedURLException,
			ServiceException, RemoteException {
		int response = -1;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = port.createRun(runDto);
		return response;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#createUser(java.lang.String)
	 */
	public void createUser(String userId) throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.createUser(userId);
	}

	private void doAuth(Object object){
		if(fUsername != null && fPassword != null){
			Stub stub = (Stub) object;
			stub._setProperty(Stub.USERNAME_PROPERTY, fUsername);
			stub._setProperty(Stub.PASSWORD_PROPERTY, fPassword);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getActiveLDRole(java.lang.String, int)
	 */
	public String getActiveLDRole(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = port.getActiveLDRole(userId, runId);
		return response;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getActiveRole(java.lang.String, int)
	 */
	public int getActiveRole(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException {
		int response = 0;
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = (int) port.getActiveRole(userId, runId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getActivityTree(java.lang.String, int)
	 */
	public String getActivityTree(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getActivityTree(userId, runId);
		return response;	
	}
	

		
	

	/**
	 * Method to get the specific CopperCore Adapter endpoint
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	private String getCCAdapter() throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		DispatcherServiceLocator service = new DispatcherServiceLocator();
		DispatcherWS port = service.getDispatcher(new URL(fDispatcherURL));				
		doAuth(port);
		response = (String) port.getAdapterWebServiceURI("org.coppercore.ccsi.adapter.ld"); //$NON-NLS-1$
		return response;
	}

    /**
	 * Method to get the specific CopperCore Administration Adapter endpoint
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 */
    private String getCCAdminAdapter() throws MalformedURLException, ServiceException, RemoteException {
    	String response = ""; //$NON-NLS-1$
    	DispatcherServiceLocator service = new DispatcherServiceLocator();
    	DispatcherWS port = service.getDispatcher(new URL(fDispatcherURL));
    	doAuth(port);
    	response = (String) port.getAdapterWebServiceURI("org.coppercore.ccsi.adapter.ld.admin"); //$NON-NLS-1$
    	return response;
    }

	/*
     * (non-Javadoc)
     * @see org.tencompetence.ldpublisher.connection.ILDConnection#getContent(java.lang.String, int, java.lang.String, java.lang.String)
     */
	public String getContent(String userId, int runId, String id, String type)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getContent(userId, runId, id, type);
		return response;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getContentUri(int)
	 */
	public String getContentUri(int uolId) throws MalformedURLException,
			ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));		
		doAuth(port);
		response = port.getContentUri(uolId);
		return response;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getContentUri(java.lang.String, int)
	 */
	public String getContentUri(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getContentUri(userId, runId);
		return response;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getEnvironmentTree(java.lang.String, int, java.lang.String[])
	 */
	public String getEnvironmentTree(String userId, int runId,
			String[] environmentId) throws MalformedURLException,
			ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getEnvironmentTree(userId, runId, environmentId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getGlobalContent(java.lang.String, int, java.lang.String)
	 */
	public String getGlobalContent(String userId, int runId, String url)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = ""; //$NON-NLS-1$
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getGlobalContent(userId, runId, url);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getGlobalContentForSupportedPerson(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public String getGlobalContentForSupportedPerson(String userId, int runId,
			String url, String supportedPerson) throws MalformedURLException,
			ServiceException, RemoteException {
		String response = null;
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		response = port.getGlobalContentForSupportedPerson(userId, runId, url, supportedPerson);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getProperty(int, java.lang.String, java.lang.String, int)
	 */
	public String getProperty(int uolId, String propId, String ownerId,
			int runId) throws MalformedURLException, ServiceException,
			RemoteException {
		String response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));		
		doAuth(port);
		response = port.getProperty(uolId, propId, ownerId, runId);
		return response;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getRolesTree(int)
	 */
	public String getRolesTree(int runId) throws MalformedURLException,
			ServiceException, RemoteException {
		String response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (String) port.getRolesTree(runId);	
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getRun(int)
	 */
	public RunDto getRun(int runId) throws MalformedURLException,
			ServiceException, RemoteException {
		RunDto response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunDto) port.getRun(runId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getUol(int)
	 */
	public UolDto getUol(int uolId) throws MalformedURLException,
			ServiceException, RemoteException {
		UolDto response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (UolDto) port.getUol(uolId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getUser(java.lang.String)
	 */
	public String getUser(String userId) throws MalformedURLException,
			ServiceException, RemoteException {
		String response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (String) port.getUser(userId);	
		return response;
	}


	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getUserRoles(java.lang.String, int)
	 */
	public String getUserRoles(String userId, int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		String response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (String) port.getUserRoles(userId, runId);	
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#getVersion()
	 */
	public String getVersion() throws MalformedURLException, ServiceException,
			RemoteException {
		String response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (String) port.getVersion();	
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listAllRunsForUser(java.lang.String)
	 */
	public RunParticipationDTO[] listAllRunsForUser(String userId)
			throws MalformedURLException, ServiceException, RemoteException {
		RunParticipationDTO[] response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunParticipationDTO[]) port.listAllRunsForUser(userId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listRuns(int)
	 */
	public RunDto[] listRuns(int uolID) throws MalformedURLException, ServiceException, RemoteException {
		RunDto[] response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunDto[]) port.listRuns(uolID);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listRunsForUser(java.lang.String, int)
	 */
	public RunParticipationDTO[] listRunsForUser(String userId, int uolId)
			throws MalformedURLException, ServiceException, RemoteException {
		RunParticipationDTO[] response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunParticipationDTO[]) port.listRunsForUser(userId, uolId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listUols()
	 */
	public UolDto[] listUols() throws MalformedURLException, ServiceException, RemoteException {
		UolDto[] response = null;		
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (UolDto[]) port.listUols(); 	   	
 	   	return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listUolsForUser(java.lang.String)
	 */
	public UolDto[] listUolsForUser(String userId)
			throws MalformedURLException, ServiceException, RemoteException {
		UolDto[] response = null;		
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (UolDto[]) port.listUolsForUser(userId); 	   	
 	   	return response;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listUsers()
	 */
	public String[] listUsers() throws MalformedURLException, ServiceException,
			RemoteException {
		String[] response = null;		
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (String[]) port.listUsers(); 	   	
 	   	return response;
	}

	public RunParticipationDTO[] listUsersInRole(int runId, int roleInstanceId)
			throws MalformedURLException, ServiceException, RemoteException {
		RunParticipationDTO[] response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunParticipationDTO[]) port.listUsersInRole(runId, roleInstanceId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#listUsersInRun(int)
	 */
	public RunParticipationDTO[] listUsersInRun(int runId)
			throws MalformedURLException, ServiceException, RemoteException {
		RunParticipationDTO[] response = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		response = (RunParticipationDTO[]) port.listUsersInRun(runId);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#notify(java.lang.String, int, java.lang.String)
	 */
	public void notify(String userId, int runId, String notificationXml)
			throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.notify();
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#publishUol(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Message[] publishUol(String imsPackageFilename,
			String schemaLocation, String contentUriOffset,
			String contentFolderOffset) throws MalformedURLException,
			ServiceException, RemoteException {
		Message[] messages = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		messages = (Message[])port.publishUol(imsPackageFilename, schemaLocation, contentUriOffset, contentFolderOffset);
		return messages;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#removeRun(int)
	 */
	public void removeRun(int runId) throws MalformedURLException,
			ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.removeRun(runId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#removeUol(int, java.lang.String)
	 */
	public void removeUol(int uolId, String contentFolderOffset)
			throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.removeUol(uolId, contentFolderOffset);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#removeUser(java.lang.String)
	 */
	public void removeUser(String userId) throws MalformedURLException,
			ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.removeUser(userId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#removeUserFromRole(java.lang.String, int)
	 */
	public void removeUserFromRole(String userId, int roleId) throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.removeUserFromRole(userId, roleId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#removeUserFromRun(java.lang.String, int)
	 */
	public void removeUserFromRun(String userId, int runId) throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.removeUserFromRun(userId,runId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#setActiveRole(java.lang.String, int, int)
	 */
	public void setActiveRole(String userId, int runId, int roleId) throws MalformedURLException, ServiceException, RemoteException{
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		port.setActiveRole(userId,runId,roleId);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#setContentUri(int, java.lang.String)
	 */
	public void setContentUri(int uolId, String contentUri)
			throws MalformedURLException, ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.setContentUri(uolId, contentUri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#setDispatcherURL(java.lang.String)
	 */
	public void setDispatcherURL(String dispatcherURL) throws MalformedURLException, RemoteException, ServiceException {
		fDispatcherURL = dispatcherURL;
		fLdAdminAdapterURL = getCCAdminAdapter();
		fLdAdapterURL = getCCAdapter();
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#setProperty(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setProperty(String uerId, int runId, String propId,
			String ownerId, String value) throws MalformedURLException,
			ServiceException, RemoteException {
		CopperCoreAdapterServiceLocator service = new CopperCoreAdapterServiceLocator();
		CopperCoreAdapterWS port = service.getCopperCoreAdapter(new URL(fLdAdapterURL));
		doAuth(port);
		port.setProperty(uerId, runId, propId, ownerId, value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#setPropertyAdmin(int, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public void setPropertyAdmin(int uolId, String propId, String ownerId,
			int runId, String value) throws MalformedURLException,
			ServiceException, RemoteException {
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		port.setPropertyAdmin(uolId, propId, ownerId, runId, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#validate(java.lang.String, java.lang.String)
	 */
	public Message[] validate(String imsPackageFilename, String schemaLocation)
			throws MalformedURLException, ServiceException, RemoteException {
		Message[] messages = null;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		messages = (Message[])port.validate(imsPackageFilename, schemaLocation);
		return messages;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.connection.ILDConnection#validateRoles(int)
	 */
	public boolean validateRoles(int runId) throws MalformedURLException,
			ServiceException, RemoteException {
		boolean result = false;
		CopperCoreAdminAdapterServiceLocator service = new CopperCoreAdminAdapterServiceLocator();
		CopperCoreAdminAdapterWS port = service.getCopperCoreAdminAdapter(new URL(fLdAdminAdapterURL));
		doAuth(port);
		result = (boolean)port.validateRoles(runId);
		return result;
	}
			
}
