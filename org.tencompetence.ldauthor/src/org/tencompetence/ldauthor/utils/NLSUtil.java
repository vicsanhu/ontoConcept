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
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Utility to copy properties files to target NLS projects and create diffs.
 * 
 * This is a work in progress...
 * 
 * TODO - More
 * 
 * @author Phillip Beauvoir
 * @version $Id: NLSUtil.java,v 1.3 2008/04/25 11:46:27 phillipus Exp $
 */
public class NLSUtil {
    
    private static File ThisFolder = new File("."); //$NON-NLS-1$
    
    // These folder locations will depend on where you put the projects!
    private static File FolderDE = new File("../nls/org.tencompetence.ldauthor.nls.de"); //$NON-NLS-1$
    private static File FolderNL = new File("../nls/org.tencompetence.ldauthor.nls.nl"); //$NON-NLS-1$
    private static File FolderES = new File("../nls/org.tencompetence.ldauthor.nls.es"); //$NON-NLS-1$
    private static File FolderBG = new File("../nls/org.tencompetence.ldauthor.nls.bg"); //$NON-NLS-1$
    
    private File fSrcFolder;
    private File fTgtFolder;
    private String fLanguage;
    
    private String propertiesExtension = ".properties"; //$NON-NLS-1$
    
    public static final String LANGUAGE_DUTCH = "nl"; //$NON-NLS-1$
    public static final String LANGUAGE_BULGARIAN = "bg"; //$NON-NLS-1$
    public static final String LANGUAGE_FRENCH = "fr"; //$NON-NLS-1$
    public static final String LANGUAGE_GERMAN = "de"; //$NON-NLS-1$
    public static final String LANGUAGE_SPANISH = "es"; //$NON-NLS-1$
    public static final String LANGUAGE_CATALAN = "ca"; //$NON-NLS-1$

    /**
     * Include these file names
     */
    private static String fIncludeList[] = {
            "messages.properties", //$NON-NLS-1$
            "plugin.properties" //$NON-NLS-1$
    };
    
    public static void main(String[] args) {
        // Comment out the ones you don't want
        new NLSUtil(ThisFolder, FolderNL, LANGUAGE_DUTCH);
        new NLSUtil(ThisFolder, FolderES, LANGUAGE_SPANISH);
        new NLSUtil(ThisFolder, FolderBG, LANGUAGE_BULGARIAN);
        new NLSUtil(ThisFolder, FolderDE, LANGUAGE_GERMAN);
    }
    
    public NLSUtil(File srcFolder, File tgtFolder, String language) {
        fSrcFolder = srcFolder;
        fTgtFolder = tgtFolder;
        fLanguage = language;
        
        try {
            scanFolder(fSrcFolder);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void scanFolder(File folder) throws IOException {
        File[] files = folder.listFiles();
        
        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            
            if(file.isDirectory()) {
                scanFolder(file);
            }
            else if(isMessageFile(file)) {
                processFile(file);
            }
        }
    }
    
    private void processFile(File file) throws IOException {
        // Comment out/in the routines you want
        
        File tgtFileNLS = getTargetLanguageFileName(file);
        
        // Copy Original file without language suffix to target project
        // copyFileOriginal(file);

        // Copy NLS file with language suffix to target project
        copyFile(file, tgtFileNLS);
        
        // Create Diff Report between original src file and target translated one
        // createDiffReport(file, tgtFileNLS);
    }
    
    /**
     * Return the file name to be used for a target with _xx two letter langauge code
     * Example, messages.properties becomes "messages_nl.properties"
     * @param file The original file
     * @return
     */
    private File getTargetLanguageFileName(File file) {
        String newPath = file.getPath();
        int index = newPath.lastIndexOf(propertiesExtension);
        newPath = newPath.substring(0, index) + "_" + fLanguage + propertiesExtension; //$NON-NLS-1$
        return new File(fTgtFolder, newPath);
    }

    /**
     * Copy the original file from src to target
     * @param file
     * @return The target file
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private File copyFileOriginal(File file) throws IOException {
        String name = file.getPath();
        File tgtFileOriginal = new File(fTgtFolder, name);
        copyFile(file, tgtFileOriginal);
        return tgtFileOriginal;
    }
    
    /**
     * Create a diff report file between the original props file and the target one
     * 
     * @param srcfile
     * @param tgtFile
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private boolean createDiffReport(File srcfile, File tgtFile) throws IOException {
        if(!srcfile.exists() || !tgtFile.exists()) {
            return false;
        }
        
        // Load Source Properties File
        Properties propsSource = new Properties();
        propsSource.load(new FileInputStream(srcfile));
        
        // Load Target Properties File
        Properties propsTarget = new Properties();
        propsTarget.load(new FileInputStream(tgtFile));
        
        // Keep a list of new/changed Strings
        Properties propsDiff = new Properties();
        
        // Diff file
        File diffFile = new File(tgtFile.getAbsolutePath() + ".diff"); //$NON-NLS-1$
        OutputStream diffOS = new FileOutputStream(diffFile);

        // Find new Strings
        boolean added = false;
        Enumeration<?> e = propsSource.keys();
        while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = (String)propsSource.get(key);
            if(propsTarget.get(key) == null) {
                propsDiff.put(key, value);
                added = true;
            }
        }
        
        if(added) {
            propsDiff.store(diffOS, "The following Strings need to be added:"); //$NON-NLS-1$
        }

        // Find removed Strings
        boolean removed = false;
        propsDiff.clear();
        e = propsTarget.keys();
        while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = (String)propsTarget.get(key);
            if(propsSource.get(key) == null) {
                propsDiff.put(key, value);
                removed = true;
            }
        }

        if(removed) {
            propsDiff.store(diffOS, "The following Strings are no longer used and can be removed:"); //$NON-NLS-1$
        }

        // Close Diff File
        diffOS.close();
        
        // If no changes delete diff file
        if(!added && !removed) {
            diffFile.delete();
        }
        
        return (added || removed);
    }
    
    /**
     * Copy a File.  The Source file must exist.
     */
    private void copyFile(File srcFile, File destFile) throws IOException {
        if(srcFile.equals(destFile)) {
            throw new IOException("Source and Target Files cannot be the same"); //$NON-NLS-1$
        }
        
        destFile.getParentFile().mkdirs();
        
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
     * Check if the file is a properties file
     * 
     * @param file
     * @return 
     */
    private boolean isMessageFile(File file) {
        if(file.isDirectory()) {
            return false;
        }
        
        // Don't include the bin folder
        if(file.getAbsolutePath().indexOf("bin") != -1) { //$NON-NLS-1$
            return false;
        }
        
        for(int i = 0; i < fIncludeList.length; i++) {
            String s = fIncludeList[i];
            if(s.equalsIgnoreCase(file.getName())) {
                return true;
            }
        }
        
        return false;
    }
}
