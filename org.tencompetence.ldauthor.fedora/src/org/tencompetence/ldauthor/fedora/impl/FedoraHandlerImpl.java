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
package org.tencompetence.ldauthor.fedora.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.ldauthor.fedora.IFedoraHandler;
import org.tencompetence.ldauthor.fedora.http.HTTPHandler;
import org.tencompetence.ldauthor.fedora.http.ICancelMonitor;
import org.tencompetence.ldauthor.fedora.http.RestException;
import org.tencompetence.ldauthor.fedora.model.IDC;
import org.tencompetence.ldauthor.fedora.model.IGETResource;
import org.tencompetence.ldauthor.fedora.model.IPOSTResource;
import org.tencompetence.ldauthor.fedora.model.IResourceDescriptor;
import org.tencompetence.ldauthor.fedora.model.IUser;
import org.tencompetence.ldauthor.fedora.model.impl.GETResource;
import org.tencompetence.ldauthor.fedora.model.impl.POSTFileResource;
import org.tencompetence.ldauthor.fedora.model.impl.ResourceDescriptor;
import org.tencompetence.ldauthor.fedora.model.impl.User;
import org.tencompetence.ldauthor.ldmodel.IOverviewModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;

/**
 * Fedora Handler
 * 
 * @author Phil Beauvoir
 * @version $Id: FedoraHandlerImpl.java,v 1.12 2009/05/19 18:21:31 phillipus Exp $
 *
 */
public class FedoraHandlerImpl implements IFedoraHandler {
    
	private String fServerURL;
	private String fUserName;
	private String fPassword;
	
	public FedoraHandlerImpl(String serverURL, String username, String password) {
	    setServerLocation(serverURL);
	    fUserName = username;
	    fPassword = password;
	}
	
	private void setServerLocation(String url){
	    if(url != null && !url.endsWith("/")) { //$NON-NLS-1$
	        url += "/"; //$NON-NLS-1$
	    }
	    fServerURL = url;
	}

    public String getServerLocation() {
        return fServerURL;
    }
	
	public List<IResourceDescriptor> listResources() throws JDOMException, RestException, IOException {
	    List<IResourceDescriptor> list = new ArrayList<IResourceDescriptor>();
	    
	    String url = fServerURL + RESOURCES;
	    
	    HTTPHandler handler = new HTTPHandler(fUserName, fPassword);
	    String result = handler.get(url);
	    if(result != null) {
	        SAXBuilder builder = new SAXBuilder();
	        Element root = builder.build(new StringReader(result)).getRootElement();
	        for(Object o : root.getChildren(RESOURCE)) {
                IResourceDescriptor descriptor = new ResourceDescriptor();
                descriptor.fromJDOM((Element)o);
                list.add(descriptor);
            }
	    }
	    
	    return list;
	}
	
	public IGETResource getResource(String guid) throws IOException, JDOMException, RestException {
	    String url = fServerURL + guid;
	    
	    IGETResource resource = new GETResource();

        HTTPHandler handler = new HTTPHandler(fUserName, fPassword);
        String result = handler.get(url);
        if(result != null) {
            //System.out.println("RESULT FROM getResource(): \n" + result + "\n");
            SAXBuilder builder = new SAXBuilder();
            Element root = builder.build(new StringReader(result)).getRootElement();
            resource.fromJDOM(root);
        }
	    
	    return resource;
	}
	
	public void getResourceContent(String guid) throws IOException, RestException {
        String url = fServerURL + guid + "/content"; //$NON-NLS-1$
        
        HTTPHandler handler = new HTTPHandler(fUserName, fPassword);
        String result = handler.get(url);
        System.out.println(result);
        
        // TODO - More
    }
	
	public void postLearningDesign(IReCourseLDModel ldModel, ICancelMonitor monitor) throws RestException, IOException, JDOMException {
	    // New HTTP Handler
        HTTPHandler handler = new HTTPHandler(fUserName, fPassword);

        // Get the next Resource GUID
	    String resource_guid = getNextResourceGUID(handler);
        
	    // Get User ID from server
        String user_guid = getUserID(fUserName);
        System.out.println("USER is: " + user_guid); //$NON-NLS-1$
        if(user_guid == null) {
            throw new RestException(Messages.FedoraHandlerImpl_0);
        }
        
        // New POST Resource with user ID set
        IPOSTResource resource = new POSTFileResource(resource_guid);
        resource.getOwner().setHref(user_guid);
        
        // Add DC Metadata
        IDC dc = resource.getDC();
        
        IOverviewModel overviewModel = ldModel.getReCourseInfoModel().getOverviewModel();
        
        dc.setTitle(ldModel.getTitle());
        dc.setIdentifier(resource_guid);
        dc.setDescription(overviewModel.getDescription());
        dc.setSubject(overviewModel.getSubject());
        dc.setType("IMS Learning Design"); //$NON-NLS-1$
        //dc.setSource("Source");
        //dc.setCoverage("Coverage");
        dc.setCreator(overviewModel.getAuthor());
        //dc.setPublisher("Publisher");
        //dc.setContributor("Contributor");
        //dc.setRights("Rights");
        //dc.setRelation("Relation");
        dc.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); //$NON-NLS-1$
        dc.setFormat("application/zip"); //$NON-NLS-1$
        //dc.setLanguage("Language");

        // POST the Resource Metadata
        String url1 = fServerURL + resource_guid; 
	    handler.post(url1, resource.toString());
	    
	    // Make a temporary zip file of the LD
        LDModelSerializer serialiser = new LDModelSerializer(ldModel);
        File tmpFile = File.createTempFile("lds", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
        tmpFile.deleteOnExit();
        serialiser.createZipFile(tmpFile, true);
        
	    // Now POST the content file
	    String url2 = fServerURL + resource_guid + "/content"; //$NON-NLS-1$
        handler.post(url2, resource.toString(), tmpFile, monitor);

        tmpFile.delete();
	}
	
	public List<IUser> listUsers() throws RestException, JDOMException, IOException {
        List<IUser> list = new ArrayList<IUser>();
        
        String url = fServerURL + USERS;
        
        HTTPHandler handler = new HTTPHandler(fUserName, fPassword);
        String result = handler.get(url);
        if(result != null) {
            SAXBuilder builder = new SAXBuilder();
            Element root = builder.build(new StringReader(result)).getRootElement();
            for(Object o : root.getChildren(USER)) {
                IUser user = new User();
                user.fromJDOM((Element)o);
                list.add(user);
            }
        }
        
        return list;
    }
	
	private String getNextResourceGUID(HTTPHandler handler) throws RestException {
	    // Get the next GUID for a Resource
        String url = fServerURL + "getNextGuid?namespace=resource"; //$NON-NLS-1$
        
        String resource_guid = handler.get(url);
        if(resource_guid == null) {
            throw new RestException("Could not retrieve new Resource GUID"); //$NON-NLS-1$
        }
        
        // Get the middle bit (hacky)
        resource_guid = resource_guid.replaceAll("<guid>", ""); //$NON-NLS-1$ //$NON-NLS-2$
        resource_guid = resource_guid.replaceAll("</guid>", ""); //$NON-NLS-1$ //$NON-NLS-2$
        
        //System.out.println("Got Resource GUID: " + resource_guid);

        return resource_guid;
	}
	
	   /**
     * Return a user GUID given their name
     * 
     * @param userName
     * @return
     * @throws RestException
     * @throws JDOMException
     * @throws IOException
     */
    private String getUserID(String userName) throws RestException, JDOMException, IOException {
        userName = userName.toLowerCase();
        
        List<IUser> list = listUsers();
        for(IUser user : list) {
            String title = user.getTitle();
            if(title != null && title.toLowerCase().equals(userName)) {
                return user.getHref();
            }
        }
        
        return null;
    }
    

}
