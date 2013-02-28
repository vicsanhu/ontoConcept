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
package org.tencompetence.widgetadvert.model.impl;

import org.tencompetence.widgetadvert.model.IWidgetAdvert;

/**
 * concrete class for a  widget object
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdvertImpl.java,v 1.1 2008/02/07 16:20:39 ps3com Exp $
 *
 */
public class WidgetAdvertImpl implements IWidgetAdvert {
	
	private String widgetIdentifier = null;
	private String widgetTitle = null;
	private String widgetDescription = null;
	private String widgetIconPath = null;
	private String widgetParameter = null;
	
	public WidgetAdvertImpl(String widgetIdentifier, String widgetTitle,
			String widgetDescription, String widgetIconPath,
			String widgetParameter) {
		super();
		this.widgetIdentifier = widgetIdentifier;
		this.widgetTitle = widgetTitle;
		this.widgetDescription = widgetDescription;
		this.widgetIconPath = widgetIconPath;
		this.widgetParameter = widgetParameter;
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#getWidgetIdentifier()
	 */
	public String getWidgetIdentifier() {
		return widgetIdentifier;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#setWidgetIdentifier(java.lang.String)
	 */
	public void setWidgetIdentifier(String widgetIdentifier) {
		this.widgetIdentifier = widgetIdentifier;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#getWidgetTitle()
	 */
	public String getWidgetTitle() {
		return widgetTitle;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#setWidgetTitle(java.lang.String)
	 */
	public void setWidgetTitle(String widgetTitle) {
		this.widgetTitle = widgetTitle;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#getWidgetDescription()
	 */
	public String getWidgetDescription() {
		return widgetDescription;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#setWidgetDescription(java.lang.String)
	 */
	public void setWidgetDescription(String widgetDescription) {
		this.widgetDescription = widgetDescription;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#getWidgetIconPath()
	 */
	public String getWidgetIconPath() {
		return widgetIconPath;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#setWidgetIconPath(java.lang.String)
	 */
	public void setWidgetIconPath(String widgetIconPath) {
		this.widgetIconPath = widgetIconPath;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#getWidgetParameter()
	 */
	public String getWidgetParameter() {
		return widgetParameter;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetadvert.model.impl.IWidgetAdvert#setWidgetParameter(java.lang.String)
	 */
	public void setWidgetParameter(String widgetParameter) {
		this.widgetParameter = widgetParameter;
	}
	
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {        
        return (this == obj) || (getWidgetIdentifier() != null && getWidgetIdentifier().equals(((WidgetAdvertImpl)obj).getWidgetIdentifier()));
    }

}
