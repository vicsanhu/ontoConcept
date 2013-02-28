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
package org.tencompetence.widgetadvert.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.widgetadvert.IWidgetAdvertHandler;
import org.tencompetence.widgetadvert.IWidgetConstants;
import org.tencompetence.widgetadvert.WidgetAdvertException;
import org.tencompetence.widgetadvert.model.IWidgetAdvert;
import org.tencompetence.widgetadvert.model.impl.WidgetAdvertImpl;

import org.tencompetence.widgetadvert.util.HTTPHandler;

/**
 * concrete class for acessing the widget objects
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdvertHandlerImpl.java,v 1.3 2008/04/24 10:25:15 phillipus Exp $
 *
 */
public class WidgetAdvertHandlerImpl implements IWidgetAdvertHandler {
	
	public static final String fAdvertiserContextPath = "/wookie/advertise"; //$NON-NLS-1$
	private String fWidgetServerUrl = null;	
	private String xmlStr = null;
	private List<IWidgetAdvert> fAvailableWidgets = null;
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.IWidgetAdvertHandler#getWidgetXMLDoc()
	 */
	public String getWidgetXMLDoc() throws WidgetAdvertException  {
		if(fWidgetServerUrl!=null){
			HTTPHandler handler = new HTTPHandler();
			xmlStr = handler.get(fWidgetServerUrl);
			return xmlStr;		
		}
		else{
			return "<widgets/>"; //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.IWidgetAdvertHandler#setServerLocation(java.lang.String)
	 */
	public void setServerLocation(String widgetServerUrl){
		fWidgetServerUrl = widgetServerUrl + fAdvertiserContextPath;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.IWidgetAdvertHandler#getwidgets()
	 */
	public List<IWidgetAdvert> getwidgets() throws JDOMException, IOException, WidgetAdvertException {
		return getwidgets(false);
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.IWidgetAdvertHandler#getwidgets(boolean)
	 */
	public List<IWidgetAdvert> getwidgets(boolean removeDuplicateWidgetEntries) throws JDOMException, IOException, WidgetAdvertException {
		fAvailableWidgets = new ArrayList<IWidgetAdvert>();	
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(getWidgetXMLDoc())).getRootElement();		
		List<?> children = root.getChildren();
		Iterator<?> iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			if(child.getName().equalsIgnoreCase(IWidgetConstants.WIDGET_ELEMENT)){
				IWidgetAdvert advert = createWidgetEntity(child);
				if(!(removeDuplicateWidgetEntries && fAvailableWidgets.contains(advert))){				
					fAvailableWidgets.add(createWidgetEntity(child));
				}
			}
		}				
		return fAvailableWidgets;
	}
	
	/**
	 * create a widget entity from the xml fragment
	 */
	private IWidgetAdvert createWidgetEntity(Element widgetElement){
		String identifier = widgetElement.getAttributeValue(IWidgetConstants.IDENTIFIER_ATTRIBUTE);		
		Element titleElement = widgetElement.getChild(IWidgetConstants.TITLE_ELEMENT);
		String title = IWidgetConstants.UNTITLED_WIDGET;
		if (titleElement != null){
			title = titleElement.getText();
		}
		Element descElement = widgetElement.getChild(IWidgetConstants.DESCRIPTION_ELEMENT);
		String desc = IWidgetConstants.UNTITLED_WIDGET;
		if (descElement != null){
			desc = descElement.getText();
		}
		Element iconElement = widgetElement.getChild(IWidgetConstants.ICON_ELEMENT);
		String iconPath = IWidgetConstants.NO_ICON_DEFINED;
		if (iconElement != null){
			iconPath = iconElement.getText();
		}
		Element paramElement = widgetElement.getChild(IWidgetConstants.PARAMETER_ELEMENT);
		String param = IWidgetConstants.NO_PARAM_DEFINED;
		if (paramElement != null){
			param = paramElement.getText();
		}
		return new WidgetAdvertImpl(identifier, title, desc, iconPath, param);		
	}

}
