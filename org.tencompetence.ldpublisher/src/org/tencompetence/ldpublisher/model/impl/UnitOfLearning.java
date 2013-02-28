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

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.jdom.JDOMException;
import org.tencompetence.ldpublisher.IPublishHandler;
import org.tencompetence.ldpublisher.model.IRun;
import org.tencompetence.ldpublisher.model.IUnitOfLearning;

/**
 * A CopperCore Unit Of Learning Object
 * 
 * @author Paul Sharples
 * @version $Id: UnitOfLearning.java,v 1.6 2009/10/14 16:20:13 phillipus Exp $
 *
 */
public class UnitOfLearning extends AbstractCCObject implements IUnitOfLearning {
		
	public UnitOfLearning(int copperCoreKey, String title, IPublishHandler publishHandler){
		super(copperCoreKey, title, publishHandler);				
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IUnitOfLearning#getRuns()
	 */
	public List<IRun> getRuns() throws MalformedURLException, RemoteException, ServiceException {					
		return getPublishHandler().getRunsForUol(getCopperCoreKey());				
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IUnitOfLearning#removeRun(org.tencompetence.ldpublisher.model.IRun)
	 */
	public void removeRun(IRun run) throws ServiceException, JDOMException, IOException{
		getPublishHandler().removeRun(run.getCopperCoreKey());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.ldpublisher.model.IUnitOfLearning#createRun(org.tencompetence.ldpublisher.model.IUnitOfLearning, java.lang.String)
	 */
	public void createRun(String runTitle) throws ServiceException, JDOMException, IOException {
		
		int copperCoreDBKey = getPublishHandler().createRunForUol(getCopperCoreKey(), runTitle);
		if(copperCoreDBKey == -1){
			throw new ServiceException("Unable to create run"); //$NON-NLS-1$
		}
	}

}
