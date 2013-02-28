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
package org.tencompetence.ldpublisher.upload.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.tencompetence.ldpublisher.upload.IResourcePart;

/**
 * Concrete class to build a specific multipart request structure which 
 * will be recognised by the CopperCore Upload routine 
 * 
 * @author Paul Sharples
 * @version $Id: ResourcePart.java,v 1.3 2009/10/14 16:20:13 phillipus Exp $
 *
 */
public class ResourcePart implements IResourcePart {

	private static final String fFileFormName = "uploadfile";//$NON-NLS-1$
	private static final String fOperationFormName = "todo";//$NON-NLS-1$
	private static final String fOperationFormValue = "publish";//$NON-NLS-1$
	
	private Part[] fResourceParts;
	private FilePart fLocalFile;
	private StringPart fOperation;
	
	/**
	 * set the file to be uploaded
	 * @param uploadFile
	 * @throws FileNotFoundException 
	 */
	public ResourcePart(File uploadFile) throws FileNotFoundException {
		if(uploadFile == null) {
		    throw new RuntimeException("Upload file can not be null"); //$NON-NLS-1$
		}
		fLocalFile = new FilePart(fFileFormName, uploadFile);
		fOperation = new StringPart(fOperationFormName, fOperationFormValue);		
		fResourceParts = new Part[] {fLocalFile, fOperation};
	}
	
	
	public Part[] getResourceParts() {
		return fResourceParts;
	}
		
}

