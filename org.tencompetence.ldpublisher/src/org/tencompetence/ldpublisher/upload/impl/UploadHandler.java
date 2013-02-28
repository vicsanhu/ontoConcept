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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.tencompetence.ldpublisher.upload.ICancelMonitor;
import org.tencompetence.ldpublisher.upload.ICancelable;
import org.tencompetence.ldpublisher.upload.IUploadHandler;

/**
 * Concrete upload class
 * 
 * @author Paul Sharples
 * @version $Id: UploadHandler.java,v 1.5 2007/11/26 15:17:33 ps3com Exp $
 *
 */
public class UploadHandler implements IUploadHandler {
	
	public static String fPublishWebPath = "/Publisher/ldcontroller?requestId=1000";//$NON-NLS-1$
	public static String fFullUrlPublishPath = null;

	/**
	 * Set the url
	 * @param serverUrl
	 */
	public UploadHandler(String serverUrl) {						
		StringBuffer httpPath = new StringBuffer();
		httpPath.append(serverUrl);
		httpPath.append(fPublishWebPath);		
		fFullUrlPublishPath = httpPath.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.upload.IUploadHandler#sendData(org.apache.commons.httpclient.methods.multipart.Part[])
	 */
	public String sendData(Part[] parts, ICancelMonitor cancelMonitor) throws HttpException, IOException {
	    PostMethod filePost = new PostMethod(fFullUrlPublishPath);

	    try {
            // hook to the ICancelMonitor if one was passed
            if(cancelMonitor != null) {
                ICancelable cancelable = new CancelableOperation(filePost);
                cancelMonitor.setOperation(cancelable);
            }

            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            HttpClient client = new HttpClient();
            client.executeMethod(filePost);
            int statusCode = client.executeMethod(filePost);

            if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
                return readFully(new InputStreamReader(filePost.getResponseBodyAsStream(), filePost.getResponseCharSet()));
            }
            else {
                throw new IOException("Method failed: " + filePost.getStatusLine() + ' ' + filePost.getURI()); //$NON-NLS-1$
            }
        } 
		finally {
			// Release the connection.
			filePost.releaseConnection();
			if (cancelMonitor != null)
				// release the associated operation
				cancelMonitor.releaseOperation();
		}
	}
	
	/**
	 * This is supposed to be the correct way to read the response instead of using getResponseBody() - which gives a runtime warning;
	 * 
	 * See - http://mail-archives.apache.org/mod_mbox/jakarta-httpclient-user/200411.mbox/%3c1101558111.4070.22.camel@localhost.localdomain%3e
	 * @param input
	 * @return
	 * @throws IOException
	 */
	 private String readFully(Reader input) throws IOException {
		BufferedReader bufferedReader = input instanceof BufferedReader ? (BufferedReader) input
				: new BufferedReader(input);
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4 * 1024];
		int charsRead;
		while ((charsRead = bufferedReader.read(buffer)) != -1) {
			result.append(buffer, 0, charsRead);
		}
		return result.toString();
	}
	 
		/**
		 * This class implements the ICancelable interface. The cancel operation is
		 * implemented by aborting the passed HTTP method.
		 * 
		 * @author Hubert Vogten
		 * 
		 */
		private class CancelableOperation implements ICancelable {
			
			HttpMethod fMethod;

			/**
			 * Constructs a new CancelableOperation. This implements the ICanceable
			 * interface.
			 * 
			 * @param method
			 */
			public CancelableOperation(HttpMethod method) {
				fMethod = method;
			}

			/**
			 * Aborts the HTTP method when process is canceled.
			 */
			public void cancel() {
				fMethod.abort();
			}
		}

}