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
package org.tencompetence.widgetadvert;

import java.io.IOException;

import org.jdom.JDOMException;
import org.tencompetence.widgetadvert.impl.WidgetAdvertHandlerImpl;
import org.tencompetence.widgetadvert.model.IWidgetAdvert;

/**
 * A test client for accessing the widget advert
 * 
 * @author Paul Sharples
 * @version $Id: TestClient.java,v 1.4 2008/04/24 10:25:08 phillipus Exp $
 *
 */
public class TestClient {

	//widget server location
	private String fServerLocation = "http://localhost:8080"; //$NON-NLS-1$
	// handler for getting the widget objects
	private IWidgetAdvertHandler fWidgetModel = new WidgetAdvertHandlerImpl();	
		
	/**
	 * 
	 * @throws WidgetAdvertException 
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws Exception
	 */
	public void buildModelTest() throws JDOMException, IOException, WidgetAdvertException {	
		fWidgetModel.setServerLocation(fServerLocation);	
		for(IWidgetAdvert advert : fWidgetModel.getwidgets(true)){
			System.out.println("\n\n=====Widget======\n"); //$NON-NLS-1$
			System.out.println("id:"+advert.getWidgetIdentifier()); //$NON-NLS-1$
			System.out.println("title:"+advert.getWidgetTitle()); //$NON-NLS-1$
			System.out.println("description:"+advert.getWidgetDescription()); //$NON-NLS-1$
			System.out.println("icon location:"+advert.getWidgetIconPath()); //$NON-NLS-1$
			System.out.println("parameter:"+advert.getWidgetParameter()); //$NON-NLS-1$
		}							
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		TestClient testClient = new TestClient();
		try {
			testClient.buildModelTest();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
