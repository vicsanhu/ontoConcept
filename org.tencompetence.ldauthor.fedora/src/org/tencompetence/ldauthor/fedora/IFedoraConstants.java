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
package org.tencompetence.ldauthor.fedora;

import org.jdom.Namespace;

/**
 * Some constants used for parsing the xml structure
 * 
 * @author Phil Beauvoir
 * @version $Id: IFedoraConstants.java,v 1.4 2008/10/16 22:05:59 phillipus Exp $
 *
 */
public interface IFedoraConstants {
    
    Namespace DC_NAMESPACE = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/"); //$NON-NLS-1$ //$NON-NLS-2$
    
    String CANONICAL = "canonical"; //$NON-NLS-1$
    String CATEGORY = "category"; //$NON-NLS-1$
    String COMMENTS = "comments"; //$NON-NLS-1$
    String CONTRIBUTOR = "contributor"; //$NON-NLS-1$
    String COUNT = "count"; //$NON-NLS-1$
    String COVERAGE = "coverage"; //$NON-NLS-1$
    String CREATOR = "creator"; //$NON-NLS-1$
    String DATE = "date"; //$NON-NLS-1$
    String DC = "dc"; //$NON-NLS-1$
    String DESCRIPTION = "description"; //$NON-NLS-1$
    String FORMAT = "format"; //$NON-NLS-1$
    String HREF="href"; //$NON-NLS-1$
    String IDENTIFIER = "identifier"; //$NON-NLS-1$
    String LANGUAGE = "language"; //$NON-NLS-1$
    String LINK = "link"; //$NON-NLS-1$
    String NUMBER_OF_VOTES = "numberOfVotes"; //$NON-NLS-1$
    String OWNER = "owner"; //$NON-NLS-1$
    String PUBLISHER = "publisher"; //$NON-NLS-1$
    String RATING = "rating"; //$NON-NLS-1$
    String RELATION = "relation"; //$NON-NLS-1$
	String RESOURCE = "resource"; //$NON-NLS-1$
	String RESOURCES = "resources"; //$NON-NLS-1$
	String RIGHTS = "rights"; //$NON-NLS-1$
    String SCORE = "score"; //$NON-NLS-1$
	String SOURCE = "source"; //$NON-NLS-1$
    String SUBJECT = "subject"; //$NON-NLS-1$
    String TAG = "tag"; //$NON-NLS-1$
    String TAGLIST = "tag-list"; //$NON-NLS-1$
	String TITLE = "title"; //$NON-NLS-1$
	String TYPE = "type"; //$NON-NLS-1$
    String USER = "user"; //$NON-NLS-1$
    String USERS = "users"; //$NON-NLS-1$
    String VALUE = "value"; //$NON-NLS-1$
	
	String RESOURCE_FILE_TYPE = "File resource"; //$NON-NLS-1$
	String RESOURCE_WEB_TYPE = "Web resource"; //$NON-NLS-1$
}
