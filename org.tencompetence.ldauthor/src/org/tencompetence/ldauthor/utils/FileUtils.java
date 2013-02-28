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
package org.tencompetence.ldauthor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Some useful File Utilities
 *
 * @author Phillip Beauvoir
 * @author Paul Sharples
 * @version $Id: FileUtils.java,v 1.15 2008/12/04 20:50:56 phillipus Exp $
 */
public final class FileUtils  {
	
    /**
     * Static file name initial number
     */
    public static int fFileNameCounter = -1;
    
	private FileUtils() {}
	
	/**
	 * Get the extension portion of a filename.
	 * @param file The File in question
	 * @return The extension part of the filename including the "." or "" if no extension
	 */
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
			return fileName.substring(i).toLowerCase();
		}
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * Get the short name portion of a filename not including the extension.
	 * @param file The File in question
	 * @return The name part of a file name excluding the extension
	 */
	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
		    return fileName.substring(0, i);
		}
		else {
		    return fileName;
		}
	}
	
	/**
	 * Calculate the size of files
	 * 
	 * @param files
	 * @return
	 */
	public static long getFileSize(File[] files) {
	    long num = 0L;
	    
	    for(File file : files) {
	        if(file.isDirectory()) {
	            num += getFileSize(file.listFiles());
	        }
	        else {
	            num += file.length();
	        }
        }
	    
	    return num;
	}
	
	public static void deleteFiles(File[] files) throws IOException {
	    for(File file : files) {
            if(file.isDirectory()) {
                deleteFolder(file);
            }
            else {
                file.delete();
            }
        }
	}
	
	/**
	 * Copy Files to target folder
	 * 
	 * @param files
	 * @param destFolder
	 * @throws IOException 
	 */
	public static void copyFiles(File[] files, File destFolder, IProgressMonitor progressMonitor) throws IOException {
	    if(!destFolder.isDirectory()) {
            throw new IOException("Parent folder should be directory"); //$NON-NLS-1$
        }
	    
	    if(progressMonitor != null) {
	        progressMonitor.setTaskName(Messages.FileUtils_0);
	    }
	    
	    for(File file : files) {
            if(file.exists()) {
                File target = new File(destFolder, file.getName());
                if(file.isDirectory()) {
                    copyFolder(file, target, progressMonitor);
                }
                else {
                    copyFile(file, target, true);
                }
            }
        }
	}
	
	/**
     * Move Files to target folder
     * 
     * @param files
     * @param destFolder
     * @throws IOException 
     */
    public static void moveFiles(File[] files, File destFolder, IProgressMonitor progressMonitor) throws IOException {
        if(!destFolder.isDirectory()) {
            throw new IOException("Parent folder should be directory"); //$NON-NLS-1$
        }
        
        if(progressMonitor != null) {
            progressMonitor.setTaskName(Messages.FileUtils_1);
        }
        
        for(File file : files) {
            if(file.exists()) {
                File target = new File(destFolder, file.getName());
                if(file.isDirectory()) {
                    moveFolder(file, target, progressMonitor);
                }
                else {
                    moveFile(file, target);
                }
            }
        }
    }
	
	/**
	 * Copy a Folder and all its files and sub-folder to target Folder which will be created if not there.
	 * @param srcFolder The Source Folder
	 * @param destFolder The Destination Folder
	 * @param progressMonitor An optional IProgressMonitor.  Can be null.
	 * @throws IOException On error or if there is a IProgressMonitor and user pressed Cancel
	 */
	public static void copyFolder(File srcFolder, File destFolder, IProgressMonitor progressMonitor) throws IOException {
	    if(srcFolder.equals(destFolder)) {
	        throw new IOException("Source and target folders cannot be the same."); //$NON-NLS-1$
	    }
	    
	    // Check that destFolder is not a child of srcFolder
	    for(File dest = destFolder.getParentFile(); dest != null; dest = dest.getParentFile()) {
	        if(dest.equals(srcFolder)) {
	            throw new IOException("The destination folder cannot be a subfolder of the source folder."); //$NON-NLS-1$
	        }
	    }
	        
	    destFolder.mkdirs();
	    File[] srcFiles = srcFolder.listFiles();
	    for(int i = 0; i < srcFiles.length; i++) {
	        File srcFile = srcFiles[i];
	        // If we have a Progress Monitor...
	        if(progressMonitor != null) {
	            progressMonitor.subTask(srcFile.getName());
	            if(progressMonitor.isCanceled()) {
                    throw new IOException("User cancelled."); //$NON-NLS-1$
	            }
	        }
	        if(srcFile.isDirectory()) {
	            copyFolder(srcFile, new File(destFolder, srcFile.getName()), progressMonitor);
	        }
	        else {
	            copyFile(srcFile, new File(destFolder, srcFile.getName()), false);
	        }
	    }
	}
	
	/**
	 * Copy a Folder and all its files and sub-folders to target Folder
	 * @throws IOException
	 */
	public static void copyFolder(File srcFolder, File destFolder) throws IOException {
	    copyFolder(srcFolder, destFolder, null);
	}
	
	/**
	 * Copy a File.  The Source file must exist.
	 * @param srcFile
	 * @param destFile
	 * @param createCopy If this is true and the destination file exists, the file is copied such as "myfile(1).txt"
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean createCopy) throws IOException {
	    if(createCopy) {
	        if(srcFile.equals(destFile) || destFile.exists()) {
	            int i = 1;
	            String name = getFileNameWithoutExtension(srcFile);
	            String ext = getFileExtension(srcFile);
	            do {
	                destFile = new File(destFile.getParentFile(), name + "(" + i++ + ")" + ext); //$NON-NLS-1$ //$NON-NLS-2$
	            }
	            while(destFile.exists());
	        }
	    }
	    else {
	        if(srcFile.equals(destFile)) {
	            throw new IOException("Source and Target Files cannot be the same"); //$NON-NLS-1$
	        }
	    }
	    
	    int bufSize = 1024;
	    byte[] buf = new byte[bufSize];
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile), bufSize);
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile), bufSize);
	    int size;
	    while((size = bis.read(buf)) != -1) {
	        bos.write(buf, 0, size);
	    }
	    bos.flush();
	    bos.close();
	    bis.close();
	}
	
	/**
     * Move a Folder
     */
    public static void moveFolder(File srcFolder, File destFolder, IProgressMonitor progressMonitor) throws IOException {
        copyFolder(srcFolder, destFolder, progressMonitor);
        deleteFolder(srcFolder);
    }
	
	/**
	 * Move a File
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile, false);
		srcFile.delete();
	}
	
	/**
	 * Delete a folder and its contents
	 * @param afolder -  a folder
	 */
	public static void deleteFolder(File afolder) throws IOException {
	    if(afolder == null) {
	        return;
	    }
	    
	    // Not root directories
	    // This way does not work where afolder = new File("aFolder");
	    // File parent = afolder.getParentFile();
	    File parent = new File(afolder.getAbsolutePath()).getParentFile();
	    if(parent == null) {
	        throw new IOException("Cannot delete root folder"); //$NON-NLS-1$
	    }
	    
	    if(afolder.exists() && afolder.isDirectory()) {
	        //delete content of directory:
	        File[] files = afolder.listFiles();
	        int count = files.length;
	        for(int i = 0; i < count; i++) {
	            File f = files[i];
	            if(f.isFile()) {
	                f.delete();
	            }
	            else if(f.isDirectory()) {
	                deleteFolder(f);
	            }
	        }
	        afolder.delete();
	    }
	}
	
    /**
	 * Sort a list of files into Folders first, files second
	 * @param files
	 * @return
	 */
	public static File[] sortFiles(File[] files) {
	    if(files == null || files.length == 0) {
	        return files;
	    }
	    
		Vector<File> v = new Vector<File>();
		
		// Folders
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				v.add(file);
			}
		}
		
		// Files
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(!file.isDirectory()){
				v.add(file);
			}
		}
		
		File[] f = new File[v.size()];
		v.copyInto(f);
		v = null;
		return f;
	}
	
	/**
	 * Get a relative path for a file given its relationship to basePath
	 */
    public static String getRelativePath(File path, File basePath) {
        try {
            String dir = path.toURL().toExternalForm();
            String baseDir = appendSeparator(basePath.toURL().toExternalForm(), "/"); //$NON-NLS-1$
            StringBuffer result = new StringBuffer();
            while (dir.indexOf(baseDir) == -1) {
                basePath = basePath.getParentFile();
                if(basePath == null) {
                    return path.getName();
                }
                baseDir = appendSeparator(basePath.toURL().toExternalForm(), "/"); //$NON-NLS-1$
                result.append("../"); //$NON-NLS-1$
            }
            if (dir.indexOf(baseDir) == 0) {
                String delta = dir.substring(baseDir.length());
                result.append(delta);
            }
            return result.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            return path.getName();
        }
    }
	
    /**
     * Appends the platform specific path separator to the end of a path.
     * 
     * @param path
     *            a path name
     * @return the path name appended with the platform specific path separator
     */
    public static String appendSeparator(String path) {
        return appendSeparator(path, File.separator);
    }

    /**
     * Appends the given path separator to the end of a path
     * 
     * @param path
     *            a path name
     * @param separator
     *            a path separator
     * @return the path name appended with the given separator
     */
    public static String appendSeparator(String path, String separator) {
        return path.endsWith(separator) ? path : path + separator;
    }

    
    /**
     * 
     * Generate a unique file name for a file checking in folder for a unique name
     * @param folder the folder to generate the name in 
     * @param prefix the file prefix
     * @param suffix the file suffix without a dot
     * @return
     */
    public static String generateFileName(File folder, String prefix, String suffix) {
        if(fFileNameCounter == -1) {
            fFileNameCounter = new Random().nextInt() & 0xffff;
        }
        
        File tmpFile;
        
        do {
            fFileNameCounter++;
            tmpFile = new File(folder, prefix + Integer.toString(fFileNameCounter) + "." + suffix); //$NON-NLS-1$

        } while(tmpFile.exists());
        
        return tmpFile.getName();
    }

    /**
     * Return true if file is in root folder
     * @param rootFolder
     * @param file
     * @return
     */
    public static boolean checkFileInRootFolder(File rootFolder, File file) {
        if(rootFolder == null || file == null) {
            return false;
        }
        
        if(!rootFolder.isDirectory()) {
            return false;
        }
        
        while((file = file.getParentFile()) != null) {
            if(file.equals(rootFolder)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Ask the user to select a file in the Root folder or a sub-folder of the root folder.
     * If the file is not in or below the root folder, a warning message is shown and null is returned.
     * 
     * @param rootFolder
     * @param shell
     * @return The selected file or null if the file is not in or below the root folder or user cancelled.
     */
    public static File selectFileFromRootFolder(File rootFolder, Shell shell) {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setText(Messages.FileUtils_2);
        dialog.setFilterPath(rootFolder.getPath());  // force to rootFolder (doesn't work on Mac)
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }

        File file = new File(path);

        // If file is not in the root folder warn
        if(!checkFileInRootFolder(rootFolder, file)) {
            MessageDialog.openError(shell,
                    Messages.FileUtils_3,
                    Messages.FileUtils_4 +
                    rootFolder.getPath());
            return null;
        }

        return file;
    }

    /**
     * Ask the user to select files in the Root folder or a sub-folder of the root folder.
     * If any file is not in or below the root folder, a warning message is shown and null is returned.
     * 
     * @param rootFolder
     * @param shell
     * @return The selected files or null if any file is not in or below the root folder or user cancelled.
     */
    public static File[] selectFilesFromRootFolder(File rootFolder, Shell shell) {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        dialog.setText(Messages.FileUtils_5);
        dialog.setFilterPath(rootFolder.getAbsolutePath());  // force to rootFolder (doesn't work on Mac)
        
        dialog.open();

        String[] paths = dialog.getFileNames();
        if(paths.length == 0) {
            return null;
        }
        
        File[] files = new File[paths.length];
        
        for(int i = 0; i < paths.length; i++) {
            files[i] = new File(dialog.getFilterPath(), paths[i]);  // Filter path is changed by dialog
            
            if(!FileUtils.checkFileInRootFolder(rootFolder, files[i])) {
                MessageDialog.openError(shell,
                        Messages.FileUtils_3,
                        Messages.FileUtils_7 +
                        rootFolder.getPath());
                return null;
            }
        }
        
        
        return files;
    }

    
    /**
     * Checks for valid file name, and converts illegal character to "_" character
     * @param name
     * @return The valid name
     */
    public static String getValidFileName(String name) {
        if(!StringUtils.isSet(name)) {
            return "untitled"; //$NON-NLS-1$
        }
        return name.replaceAll("[: !*<>\"/|?\\\\]", "_"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}

