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
package org.tencompetence.ldauthor.ui.editors;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.impl.ReCourseLDModel;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Editor Input for LD Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDEditorInput.java,v 1.5 2010/02/01 10:45:09 phillipus Exp $
 */
public class LDEditorInput 
implements IEditorInput
{
    /**
     * The Model
     */
    private IReCourseLDModel fModel;
    
    /**
     * LD Name
     */
    private String fName;
    private static String fNamePaso;
    
    /**
     * LD Manifest File
     */
    private File fManifestFile;
    
    private LDModelSerializer fSerialiser;

    public LDEditorInput(String name, File manifestFile) {
        if(manifestFile == null) {
            throw new IllegalArgumentException("File cannot be null"); //$NON-NLS-1$
        }
        
        fName = name;
        fNamePaso=fName;
        fManifestFile = manifestFile;
    }
    
    /**
     * @return The Model
     */
    public IReCourseLDModel getModel() {
        if(fModel == null) {
            fModel = new ReCourseLDModel(fManifestFile);
            
            // Load it if it exists
            if(fManifestFile.exists()) {
                try {
                    fSerialiser = new LDModelSerializer(fModel);
                    fSerialiser.loadModel();
                }
                catch(Exception ex) { 
                    ex.printStackTrace();
                }
            }
        }
        
        return fModel;
    }
    
    /**
     * Save the Model
     * @throws IOException 
     */
    public void saveModel() throws IOException {
        if(fSerialiser == null) {
            fSerialiser = new LDModelSerializer(getModel());
        }
        fSerialiser.saveModel();
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageFactory.getImageDescriptor(ImageFactory.ICON_LD);
    }

    public String getName() {
        return fName;
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return fManifestFile.toString();
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if(adapter == ILDModel.class) {
            return getModel();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof LDEditorInput)) {
            return false;
        }
        
        return fManifestFile.equals(((LDEditorInput)obj).fManifestFile);
    }
    public static String retornaNombreFile()
    {
   	return(fNamePaso);
    }

}
