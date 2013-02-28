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
package org.tencompetence.ldpublisher;

/**
 * Some constants
 * 
 * @author Paul Sharples
 * @version $Id: IPublisherConstants.java,v 1.6 2009/10/14 16:20:13 phillipus Exp $
 *
 */
public interface IPublisherConstants {
	
	enum RoleType { learner, staff };
	String TITLE_ELEMENT = "title"; //$NON-NLS-1$
	String IDENTIFIER_ATTRIBUTE = "identifier"; //$NON-NLS-1$
	String MAX_PERSONS_ATTRIBUTE = "max-persons"; //$NON-NLS-1$
	String MIN_PERSONS_ATTRIBUTE = "min-persons"; //$NON-NLS-1$
	String UNTITLED_ROLE = "Untitled role"; //$NON-NLS-1$
	String DEFAULT_CONTENT_URI_OFFSET = "jboss-4.0.4.GA/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war"; //$NON-NLS-1$
	String ALREADY_EXISTS_EXCEPTION_STRING = "AlreadyExistsException"; //$NON-NLS-1$
	String NOT_FOUND_EXCEPTION_STRING = "NotFoundException"; //$NON-NLS-1$
}
