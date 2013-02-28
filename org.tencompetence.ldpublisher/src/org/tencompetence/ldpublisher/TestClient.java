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

import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IRun;
import org.tencompetence.ldpublisher.model.IUnitOfLearning;
import org.tencompetence.ldpublisher.model.IUser;


/**
 * 
 * A test class for publishing an LD and setting up user/run/role
 * 
 * 
 * 
 * General steps for publishing and provisioning from scratch
 * ----------------------------------------------------------
 * 
 * (1) IPublishHandler.setServerLocation(theServerUrl);
 * Set the coppercore server url first. e.g. http://localhost:8080
 * 
 * (2) IPublishHandler.publishUol(thearchivezipFile)
 * Publish(upload) the LD zip file
 * 
 * (3) IPublishHandler.findUolInCC(UolUri);
 * Retrieve the CopperCore DB handle for the published LD (UOL)
 * (obviously must match the URI found in the above zip file imsmanifest.xml
 * 
 * (4) IPublishHandler.createRunForUol(uolId, fNewRunTitle);
 * Create a new run for the UOL, giving the run title
 * 
 * (5) IPublishHandler.createUser(stringUserId);
 * Create a new user
 * 
 * (6) IPublishHandler.addUserToRun(userId, runId);
 * Add user to a run
 * 
 * (7) IPublishHandler.addUserToRole(userId, roleId);
 * Add user to a role
 * 
 * (8 - optional?) IPublishHandler.setActiveRole(userId, runId, roleId);
 * Make a given role active for the user
 * 
 * @author Paul Sharples
 * @version $Id: TestClient.java,v 1.14 2009/10/14 16:20:13 phillipus Exp $
 *
 */
public class TestClient {

	// this value would be obtained from the prefs - address of the server
	//public static final String fCoppercoreServerUrl = "http://galadriel.cetis.ac.uk:8080";
	public static final String fCoppercoreServerUrl = "http://localhost:8080"; //$NON-NLS-1$
	// the next two lines are used to specify the actual file path on disk of the LD zip file
	public static final String fileName = "space_1.zip"; //$NON-NLS-1$
	//public static final String fileName = "Astronomy.zip";
	public static final File fArchiveFile = new File("D:\\Test_Packages\\learning-design\\", fileName); //$NON-NLS-1$
	
	// we need the UOL uri (of the above files manifest) to search for it in the coppercore DB later.
    public static final String fUolUri = "http://www.reload.ac.uk/tickettospace"; //$NON-NLS-1$
	//public static final String fUolUri = "Ast";
	
	// We'll need to add a new run.  A run needs a title - we will need to get this from user eventually.
	public static final String fNewRunTitle = "Cohort B Thursday Mornings"; //$NON-NLS-1$
		
	//publishing interface
	private IPublishHandler fPublishHandler;
	
	
	public void buildModelTest() throws Exception {
		
		fPublishHandler = new PublishHandler(fCoppercoreServerUrl, "tenc", "tenc");				  //$NON-NLS-1$//$NON-NLS-2$
		
		for(IUnitOfLearning uol : fPublishHandler.getUnitsOfLearning()){
			System.out.println("+"+uol.getTitle()); //$NON-NLS-1$
			for(IRun run : uol.getRuns()){
				System.out.println("--+"+run.getTitle()); //$NON-NLS-1$
				for(IRole role : run.getRoles()){
					System.out.println("----+"+role.getTitle()); //$NON-NLS-1$
					for(IUser user: role.getUsers()){
						System.out.println("--------"+user.getTitle()); //$NON-NLS-1$
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		try {
			TestClient rth = new TestClient();
			rth.buildModelTest();
		} 		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
