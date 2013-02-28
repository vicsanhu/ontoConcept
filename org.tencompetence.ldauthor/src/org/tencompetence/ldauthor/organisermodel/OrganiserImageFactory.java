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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Factory to return images for Organiser objects
 * We don't put these in the model classes so as to decouple graphical and model concerns
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserImageFactory.java,v 1.8 2008/04/24 10:15:27 phillipus Exp $
 */
public class OrganiserImageFactory {
    
    private static OrganiserImageFactory fInstance = new OrganiserImageFactory();
    
    public static OrganiserImageFactory getInstance() {
        return fInstance;
    }
    
    /**
     * @param object
     * @return The correct Organiser image depending on given source object
     */
    public Image getImage(IOrganiserObject object) {
        String imageName = getImageName(object);
        if(imageName != null) {
            return ImageFactory.getImage(imageName);
        }
        
        return null;
    }
    
    public ImageDescriptor getImageDescriptor(IOrganiserObject object) {
        String imageName = getImageName(object);
        if(imageName != null) {
            return ImageFactory.getImageDescriptor(imageName);
        }
        
        return null;
    }
    
    private String getImageName(Object object) {
        if(object instanceof IOrganiserLD) {
            return ImageFactory.ICON_LD;
        }
        else if(object instanceof IOrganiserFolder) {
            return "folder"; //$NON-NLS-1$
        }
        else if(object instanceof IOrganiserResource) {
            return ImageFactory.ICON_RESOURCE;
        }
        
        // Add more here...

        Logger.logError("OrganiserImageFactory: Requested null image for " + object); //$NON-NLS-1$
        return null;
    }
}
