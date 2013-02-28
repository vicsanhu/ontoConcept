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

import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;
import org.tencompetence.ldauthor.fedora.http.ICancelMonitor;
import org.tencompetence.ldauthor.fedora.http.RestException;
import org.tencompetence.ldauthor.fedora.model.IGETResource;
import org.tencompetence.ldauthor.fedora.model.IResourceDescriptor;
import org.tencompetence.ldauthor.fedora.model.IUser;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;


/**
 * Interface for acessing the Fedora Handler
 * 
 * @author Phil Beauvoir
 * @version $Id: IFedoraHandler.java,v 1.9 2009/05/19 18:21:31 phillipus Exp $
 *
 */
public interface IFedoraHandler extends IFedoraConstants {

    /**
     * @return The server location
     */
    String getServerLocation();

    /**
     * List the Resources on the server
     * @return
     * @throws JDOMException
     * @throws RestException
     * @throws IOException
     */
    List<IResourceDescriptor> listResources() throws JDOMException, RestException, IOException;
    
    /**
     * Get The Resource Metadata for a Resource
     * 
     * @param guid
     * @return
     * @throws RestException
     * @throws JDOMException
     * @throws IOException
     */
    IGETResource getResource(String guid) throws RestException, JDOMException, IOException;
    
    /**
     * GET The Content of a Resource from the Server
     * @param guid
     * @throws RestException
     * @throws IOException
     */
    void getResourceContent(String guid) throws RestException, IOException;
    
    /**
     * POST a Learning Design to the Server.
     * It will be zipped up and sent as a zip file with covering DC Metadata.
     * @param ldModel
     * @param monitor
     * @throws RestException
     * @throws IOException
     * @throws JDOMException
     */
    void postLearningDesign(IReCourseLDModel ldModel, ICancelMonitor monitor) throws RestException, IOException, JDOMException;
    
    /**
     * List all users on the server
     * @return
     * @throws RestException
     * @throws JDOMException
     * @throws IOException
     */
    List<IUser> listUsers() throws RestException, JDOMException, IOException;
}