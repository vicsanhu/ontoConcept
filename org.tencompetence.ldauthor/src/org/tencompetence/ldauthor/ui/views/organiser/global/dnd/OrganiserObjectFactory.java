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
package org.tencompetence.ldauthor.ui.views.organiser.global.dnd;

import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;


/**
 * Factory to create new Organiser entries after a drag and drop operation
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserObjectFactory.java,v 1.2 2008/04/24 10:15:05 phillipus Exp $
 */
public class OrganiserObjectFactory {
    
    private static OrganiserObjectFactory fInstance = new OrganiserObjectFactory();
    
    public static OrganiserObjectFactory getInstance() {
        return fInstance;
    }
    
    /**
     * Return true if this object is supported for Drop support
     * 
     * @param object
     * @return
     */
    public boolean isSupportedType(Object object) {
        if(object instanceof IOrganiserObject) {
            return true;
        }
        
        return false;
    }
    
    /**
     * @param object
     * @return The correct Organiser object depending on given source object
     */
    public IOrganiserObject createOrganiserObject(Object object) {
        
        // If the object is an IOrganiserObject just return it
        if(object instanceof IOrganiserObject) {
            return (IOrganiserObject)object;
        }
        
        System.out.println("OrganiserObjectFactory: Requested null object for " + object); //$NON-NLS-1$
        
        return null;
    }
}
