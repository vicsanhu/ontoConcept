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
package org.tencompetence.ldauthor.ui.editors.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Helper class that updates the hrefs of Resources and Resource Files
 * when physical files are renamed or moved
 * 
 * @author Phillip Beauvoir
 * @version $Id: FileChangeHelper.java,v 1.6 2009/05/19 18:21:04 phillipus Exp $
 */
public class FileChangeHelper {
    
    /**
     * Changed files
     */
    private HashMap<File, File> fChangedFiles = new HashMap<File, File>();
    
    private ILDModel fLDModel;

    public FileChangeHelper(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public void renameFile(File oldFile, File newFile) {
        if(oldFile.isDirectory()) {
            fChangedFiles.clear();
            saveChangedFiles(oldFile.listFiles(), newFile);
            boolean result = oldFile.renameTo(newFile);
            if(result) {
                checkFilesChanged();
            }
        }
        else {
            boolean result = oldFile.renameTo(newFile);
            if(result) {
                checkFileChanged(oldFile, newFile);
            }
        }
    }
    
    /**
     * Move files to the the destination folder
     * 
     * @param files
     * @param destFolder
     * @param monitor
     * @throws IOException
     */
    public void moveFiles(File[] files, File destFolder, IProgressMonitor monitor) throws IOException {
        fChangedFiles.clear();
        saveChangedFiles(files, destFolder);
        FileUtils.moveFiles(files, destFolder, monitor);
        checkFilesChanged();
    }
    
    /**
     * Save a flat list of all about-to-be-changed files
     * 
     * @param files
     * @param destFolder
     */
    private void saveChangedFiles(File[] files, File destFolder) {
        for(File file : files) {
            File newFile = new File(destFolder, file.getName());
            if(file.isDirectory()) {
                saveChangedFiles(file.listFiles(), new File(destFolder, file.getName()));
            }
            else {
                fChangedFiles.put(file, newFile);
            }
        }
    }
    
    private void checkFilesChanged() {
        for(File file : fChangedFiles.keySet()) {
            checkFileChanged(file, fChangedFiles.get(file));
        }
    }
    
    private void checkFileChanged(File oldFile, File newFile) {
        IResourcesModel resources = fLDModel.getResourcesModel();
        File rootFolder = fLDModel.getRootFolder();

        for(IResourceModel resource : resources.getResources()) {
            String oldHref = FileUtils.getRelativePath(oldFile, rootFolder);
            String newHref = FileUtils.getRelativePath(newFile, rootFolder);
            if(oldHref.equalsIgnoreCase(resource.getHref())) {
                resource.setHref(newHref);
            }
            for(IResourceFileModel resourceFile : resource.getFiles()) {
                if(oldHref.equalsIgnoreCase(resourceFile.getHref())) {
                    resourceFile.setHref(newHref);
                }
            }
        }
    }
    
}
