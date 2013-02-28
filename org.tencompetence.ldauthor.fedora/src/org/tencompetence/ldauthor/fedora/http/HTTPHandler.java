/*
 * Copyright (c) 2007, Consortium Board TENCompetence All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the TENCompetence nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.ldauthor.fedora.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.tencompetence.ldauthor.preferences.ProxyConnectionPrefsManager;

/**
 * A class using HttpClient to handle HTTP requests & manipulate the
 * responses
 * 
 * @author Paul Sharples
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Id: HTTPHandler.java,v 1.4 2008/10/15 17:05:04 phillipus Exp $
 */
public class HTTPHandler {

    private static final String ERROR_PROXY = Messages.HTTPHandler_0;
    private static final String ERROR_INVALID_PASSWORD = Messages.HTTPHandler_1;
    private static final String ERROR_CONNECT = Messages.HTTPHandler_2;

    public String ctype;
    
    private String fUserName, fPassword;

    public HTTPHandler(String userName, String password) {
        fUserName = userName;
        fPassword = password;
    }

    public String put(String url, String xmlData) throws RestException {
        // Prepare HTTP put
        PutMethod put = new PutMethod(url);
        System.out.println("PUT to " + url); //$NON-NLS-1$
        System.out.println(xmlData);
        return sendXmlData(xmlData, put, null, null);

    }

    public String post(String uri, String xmlData) throws RestException {
        // Prepare HTTP post
        PostMethod post = new PostMethod(uri);
        System.out.println("POST to " + uri); //$NON-NLS-1$
        System.out.println(xmlData);

        return sendXmlData(xmlData, post, null, null);
    }

    public String post(String uri, String xmlData, File file, ICancelMonitor cancelMonitor) throws RestException {
        PostMethod post = new PostMethod(uri);
        System.out.println("POST to " + uri); //$NON-NLS-1$
        System.out.println(xmlData);
        return sendXmlData(xmlData, post, file, cancelMonitor);
    }
    
    public String get(String url) throws RestException {
        System.out.println("GET from " + url); //$NON-NLS-1$
        return fetchData2(new GetMethod(url));
    }

    public String delete(String url) throws RestException {
        return fetchData2(new DeleteMethod(url));
    }

   
    
    private String sendXmlData(String xmlData, EntityEnclosingMethod method, File localResourceFile,
            ICancelMonitor cancelMonitor) throws RestException {

        // Tell the method to automatically handle authentication.
        method.setDoAuthentication(true);

        if (localResourceFile == null) {
            try {
                method.setRequestEntity(new StringRequestEntity(xmlData, "text/xml", "UTF8"));//$NON-NLS-1$  //$NON-NLS-2$
            } catch (UnsupportedEncodingException e) {
                throw new RestException(e);
            }

            // Specify content type and encoding
            // If content encoding is not explicitly specified
            // ISO-8859-1 is assumed
            method.setRequestHeader("Content-type", "text/xml; charset=UTF8"); //$NON-NLS-1$  //$NON-NLS-2$         
        } else {
            Part[] parts = createFileResourceParts(xmlData, localResourceFile);
            if (parts != null) {
                method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            }
        }

        return executeMethod(method, cancelMonitor);
    }
    
    /**
     * @param method The <code>HttpMethod</code> to execute
     * @param cancelMonitor optional monitor to enable canceling of the method execution whilest running
     * @return the response from the server
     * @throws RestException whenever there is a connection error or when the server returns an error.
     */
    private String executeMethod(HttpMethodBase method, ICancelMonitor cancelMonitor) throws RestException {

        // Execute the method.
        try {

            // hook to the ICancelMonitor if one was passed
            if (cancelMonitor != null) {
                ICancelable cancelable = new CancelableOperation(method);
                cancelMonitor.setOperation(cancelable);
            }
            HttpClient client = new HttpClient();

            // set the client's proxy values if needed
            ProxyConnectionPrefsManager.setProxySettings(client);

            // Pass our credentials to HttpClient
            // TODO fetch server, port and preferably realm from conduit
            client.getState().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
                    new UsernamePasswordCredentials(fUserName, fPassword));
            
            // send the basic authentication response even before the server gives an unauthorized response
            client.getParams().setAuthenticationPreemptive(true);
            
            // Add user language to http request in order to notify server of user's language
            Locale locale = Locale.getDefault();
            method.setRequestHeader("Accept-Language", locale.getLanguage()); //$NON-NLS-1$ 
            
            int statusCode = client.executeMethod(method);

            if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
                return readFully(new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));
                // for now we are only expecting Strings
                // return method.getResponseBodyAsString();
            }
            else if(statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new AuthenticationException(ERROR_INVALID_PASSWORD);
            }
            else if(statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED) {
                throw new ProxyAuthenticationException(ERROR_PROXY);
            }
            else {
                throw new RestException("Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText()); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new HttpIOException(ERROR_CONNECT, e);
        } finally {
            // Release the connection.
            method.releaseConnection();
            if (cancelMonitor != null)
                // release the associated operation
                cancelMonitor.releaseOperation();
        }
    }
    
    private Part[] createFileResourceParts(String xml, File file) throws RestException {
        if (xml == null || file == null)
            throw new RestException("Null values not allowed when creating file parts"); //$NON-NLS-1$
        StringPart xmlPart = new StringPart("rest-message", xml); //$NON-NLS-1$
        xmlPart.setContentType("text/xml"); //$NON-NLS-1$
        try {
            FilePart localFile = new FilePart(file.getName(), file);

            Part[] resourceParts = new Part[] { xmlPart, localFile };

            return resourceParts;
        } catch (FileNotFoundException e) {
            throw new RestException("Failed to attach file " + file.getAbsolutePath(), e);//$NON-NLS-1$
        }

    }

    // Alternative method
    @SuppressWarnings("unused")
    private String fetchData(HttpMethodBase method) throws IOException {
        try {
            // Create an instance of HttpClient.
            HttpClient client = new HttpClient();
            
            // Provide custom retry handler is necessary
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
            
            // Tell the method to automatically handle authentication.
            method.setDoAuthentication(true);

            // Execute the method.
            int statusCode = client.executeMethod(method);
            if(statusCode == HttpStatus.SC_OK) {
                Header hType = method.getResponseHeader("Content-type"); //$NON-NLS-1$
                ctype = hType.getValue();
                return readFully(new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));
            }
            else if(statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new HttpException("Passed credentials are not correct"); //$NON-NLS-1$
            }
            else if(statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED) {
                throw new HttpException("Proxy authentication required."); //$NON-NLS-1$
            }
            else {
                throw new HttpException("Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText()); //$NON-NLS-1$
            }
        }
        finally {
            // Release the connection.
            method.releaseConnection();
        }
    }

    private String fetchData2(HttpMethodBase method) throws RestException {

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));

        // Tell the method to automatically handle authentication.
        method.setDoAuthentication(true);

        return executeMethod(method, null);
    }

    /**
     * This is supposed to be the correct way to read the response instead of
     * using getResponseBody() - which gives a runtime warning;
     * 
     * See - http://mail-archives.apache.org/mod_mbox/jakarta-httpclient-user/200411.mbox/%3c1101558111.4070.22.camel@localhost.localdomain%3e
     * 
     * @param input
     * @return
     * @throws IOException
     */
    private String readFully(Reader input) throws IOException {
        BufferedReader bufferedReader = input instanceof BufferedReader ? (BufferedReader)input : new BufferedReader(input);
        StringBuffer result = new StringBuffer();
        char[] buffer = new char[4 * 1024];
        int charsRead;
        while((charsRead = bufferedReader.read(buffer)) != -1) {
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
