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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Editor Input for File Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: FileEditorInput.java,v 1.4 2010/02/01 10:45:09 phillipus Exp $
 */
public class FileEditorInput 
implements IPathEditorInput
{
    
    /**
     * Name
     */
    private String fName;
    
    /**
     * File
     */
    private File fFile;

    public FileEditorInput(String name, File file) {
        if(file == null) {
            throw new IllegalArgumentException("File cannot be null"); //$NON-NLS-1$
        }

        fName = name;
        fFile = file;
    }
    
    public File getFile() {
        return fFile;
    }
    
    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return ImageFactory.getImageDescriptor(ImageFactory.ICON_EDIT);
    }

    public String getName() {
        return fName == null || "".equals(fName) ? fFile.getName() : fName; //$NON-NLS-1$
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return getFile().getPath();
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof FileEditorInput)) {
            return false;
        }
        
        return fFile.equals(((FileEditorInput)obj).fFile);
    }

    public IPath getPath() {
        return new Path(fFile.getAbsolutePath());
    }
}
