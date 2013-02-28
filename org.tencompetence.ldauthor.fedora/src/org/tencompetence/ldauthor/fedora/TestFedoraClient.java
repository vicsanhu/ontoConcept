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
package org.tencompetence.ldauthor.fedora;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;
import org.tencompetence.ldauthor.fedora.http.RestException;
import org.tencompetence.ldauthor.fedora.impl.FedoraHandlerImpl;
import org.tencompetence.ldauthor.fedora.model.IGETResource;
import org.tencompetence.ldauthor.fedora.model.IResourceDescriptor;
import org.tencompetence.ldauthor.fedora.model.IUser;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.impl.ReCourseLDModel;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;

/**
 * A test client for accessing the widget advert
 * 
 * @author Paul Sharples
 * @version $Id: TestFedoraClient.java,v 1.10 2009/05/19 18:21:31 phillipus Exp $
 *
 */
public class TestFedoraClient {

	// Server location
	//private static String fServerLocation = "http://fedora.it.fmi.uni-sofia.bg:8080/fedora/"; //$NON-NLS-1$
	private static String fServerLocation = "http://gaco.fmi.uni-sofia.bg:8080/FedoraKRSM/fedora/"; //$NON-NLS-1$
	
	
	// Handler
	private static IFedoraHandler fHandler = new FedoraHandlerImpl(fServerLocation, "fred", "fred");	  //$NON-NLS-1$//$NON-NLS-2$
		
	public void resourcesTest() throws IOException, JDOMException, RestException {	
	    List<IResourceDescriptor> resources = fHandler.listResources();
	    for(IResourceDescriptor resource : resources) {
	        System.out.println(resource.toString());
            //getResourceTest(resource.getGUID());
            System.out.println();
        }
	}
	
	public void getResourceTest(String resourceGUID) throws IOException, JDOMException, RestException {
	    IGETResource resource = fHandler.getResource(resourceGUID);
	    
	    System.out.println(resource.toString());
	    
        System.out.println("---------------------------------------------"); //$NON-NLS-1$
        System.out.println();
	}
	
	public void getResourceContentTest(String resourceGUID) throws IOException, RestException {
	    fHandler.getResourceContent(resourceGUID);
	}
	
	public void postNewResourceTest() throws RestException, IOException, JDOMException {
	    IReCourseLDModel ldModel = new ReCourseLDModel(new File("d:\\12Steps")); //$NON-NLS-1$
	    LDModelSerializer serialiser = new LDModelSerializer(ldModel);
	    serialiser.loadModel();
        fHandler.postLearningDesign(ldModel, null);
    }
	
    public void usersTest() throws RestException, JDOMException, IOException {
        List<IUser> users = fHandler.listUsers();
        for(IUser user : users) {
            System.out.println(user.toString());
            //getResourceTest(resource.getGUID());
            System.out.println();
        }
    }

    
    /**
	 * @param args
	 */
	public static void main(String[] args) {		
		TestFedoraClient testClient = new TestFedoraClient();

        try {
			//testClient.resourcesTest();
			//testClient.getResourceTest("resource:1");  //$NON-NLS-1$
			//testClient.getResourceContentTest("resource:423");  //$NON-NLS-1$
            //testClient.postNewResourceTest();
            testClient.usersTest();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

}
