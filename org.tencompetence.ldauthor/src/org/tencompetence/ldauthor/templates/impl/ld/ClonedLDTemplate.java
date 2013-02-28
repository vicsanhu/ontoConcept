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
package org.tencompetence.ldauthor.templates.impl.ld;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.tencompetence.ldauthor.LDAuthorException;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Cloned LD Template
 * 
 * @author Phillip Beauvoir
 * @version $Id: ClonedLDTemplate.java,v 1.4 2009/05/19 18:21:04 phillipus Exp $
 */
public class ClonedLDTemplate extends AbstractLDTemplate {
    
    private File fCopiedFolder;

    public ClonedLDTemplate() {
        super();
        setDescription(Messages.ClonedLDTemplate_0);
        setName(Messages.ClonedLDTemplate_1);
    }

    public Image getImage() {
        return ImageFactory.getImage(ImageFactory.IMAGE_APP_48);
    }

    @Override
    public void create(File manifestFile) throws LDAuthorException {
        super.create(manifestFile);
        
        LDModelSerializer serialiser = new LDModelSerializer(fLDModel);

        try {
            if(fCopiedFolder != null) {
                // Copy Folder
                FileUtils.copyFolder(fCopiedFolder, fLDModel.getRootFolder());
                // Open Manifest and change the IDs, save it again
                serialiser.loadModel();
                fLDModel.resetIDs();
            }

            serialiser.saveModel();
        }
        catch(Exception ex) {
            throw new LDAuthorException(ex);
        }

    }

    public void setCopiedFolder(File copiedFolder) {
        fCopiedFolder = copiedFolder;
    }
}
