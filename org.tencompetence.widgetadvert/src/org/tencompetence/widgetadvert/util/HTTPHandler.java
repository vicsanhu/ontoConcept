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
package org.tencompetence.widgetadvert.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.tencompetence.widgetadvert.WidgetAdvertException;

/**
 * A class using HttpClient to handle HTTP get requests & manipulate the responses
 *
 * @author Paul Sharples
 * @version $Id: HTTPHandler.java,v 1.4 2008/04/24 10:25:14 phillipus Exp $
 */
public class HTTPHandler {
	
	//static Logger _logger = Logger.getLogger(ProxyClient.class.getName());
	
	public String ctype;

	public HTTPHandler() {}	
	
		public String get(String url) throws WidgetAdvertException {
			// System.out.println("GET from " + url); //$NON-NLS-1$
			return fetchData(new GetMethod(url));
		}
	      
		private String fetchData(HttpMethodBase method) throws WidgetAdvertException {
			try {
				// Create an instance of HttpClient.
				HttpClient client = new HttpClient();
				// Provide custom retry handler is necessary
				// TODO: the line below was causing an error under jboss (not tomcat)
				// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
				// Tell the method to automatically handle authentication.
				method.setDoAuthentication(true);
				// Execute the method.
				int statusCode = client.executeMethod(method);
				if (statusCode == HttpStatus.SC_OK) {
					Header hType = method.getResponseHeader("Content-type"); //$NON-NLS-1$
					ctype = hType.getValue();
					return readFully(new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));
				} 
				else if (statusCode == HttpStatus.SC_UNAUTHORIZED){	
					throw new WidgetAdvertException("Passed credentials are not correct"); //$NON-NLS-1$
				}
				else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED){
					throw new WidgetAdvertException("Proxy authentication required."); //$NON-NLS-1$
				}
				else{
					throw new WidgetAdvertException(							
							"Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText()); //$NON-NLS-1$
				}
			} 
			catch (HttpException e) {
				throw new WidgetAdvertException(e);
			} 
			catch (IOException e) {
				throw new WidgetAdvertException(e);
			} 
			finally {				
				// Release the connection.
				method.releaseConnection();
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
		 
}
