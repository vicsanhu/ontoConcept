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
package org.tencompetence.ldpublisher.model.impl;

import org.tencompetence.ldpublisher.IPublishHandler;
import org.tencompetence.ldpublisher.model.ICCObject;

/**
 * Base object for a CopperCore object
 * 
 * @author Paul Sharples
 * @version $Id: AbstractCCObject.java,v 1.3 2008/10/16 11:58:02 ps3com Exp $
 *
 */
public class AbstractCCObject implements ICCObject {
	
	private IPublishHandler fPublishHandler;
	
	private int fCopperCoreKey;
	
	private String fTitle;

	public AbstractCCObject(int copperCoreKey, String title, IPublishHandler publishHandler) {
		super();
		fCopperCoreKey = copperCoreKey;
		fTitle = title;
		fPublishHandler = publishHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.ICCObject#getTitle()
	 */
	public String getTitle() {
		return fTitle;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.ICCObject#getCopperCoreKey()
	 */
	public int getCopperCoreKey() {
		return fCopperCoreKey;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.ICCObject#getPublishHandler()
	 */
	public IPublishHandler getPublishHandler() {
		return fPublishHandler;
	}

}
