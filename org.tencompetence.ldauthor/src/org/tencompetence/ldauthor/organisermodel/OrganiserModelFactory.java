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
package org.tencompetence.ldauthor.organisermodel;

import java.io.File;

import org.tencompetence.ldauthor.organisermodel.impl.OrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserLD;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserResource;


/**
 * LDModelFactory
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserModelFactory.java,v 1.4 2008/04/24 10:15:27 phillipus Exp $
 */
public class OrganiserModelFactory {
    
    public static final String FOLDER = "folder"; //$NON-NLS-1$
    public static final String INDEX = "index"; //$NON-NLS-1$
    public static final String LD = "ld"; //$NON-NLS-1$
    public static final String RESOURCE = "resource"; //$NON-NLS-1$
    
    
    
    public static IOrganiserObject createModelObject(String name) {
        if(name.equals(LD)) {
            return new OrganiserLD();
        }
        else if(name.equals(RESOURCE)) {
            return new OrganiserResource();
        }
        else if(name.equals(FOLDER)) {
            return new OrganiserFolder();
        }
        
        return null;
    }
    
    /**
     * @param name
     * @param file
     * @return A new OrganiserLD
     */
    public static IOrganiserLD createOrganiserLD(String name, File file) {
        return new OrganiserLD(name, file);
    }
    
    /**
     * @return The single OrganiserIndex
     */
    public static IOrganiserIndex getOrganiserIndex() {
        return OrganiserIndex.getInstance();
    }
}
