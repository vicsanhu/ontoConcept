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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ExtensionContentHandler;
import org.tencompetence.ldauthor.ILDContentHandler;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.ui.editors.ld.LDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.richtext.RichTextEditorPart;
import org.tencompetence.ldauthor.ui.editors.text.SimpleTextEditor;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Editor Manager
 * 
 * @author Phillip Beauvoir
 * @version $Id: EditorManager.java,v 1.27 2009/10/16 10:59:18 phillipus Exp $
 */
public class EditorManager {
    
    /**
     * Convenience method to Open the LD Editor
     * @param name
     * @param file
     */
    public static void openLDEditor(String name, File file) {
        if(file == null) {
            throw new NullPointerException("File was null in Editor."); //$NON-NLS-1$
        }

        openEditor(new LDEditorInput(name, file), LDMultiPageEditor.ID);
    }
    
    /**
     * Open an Editor
     * 
     * @param input
     * @param editorID
     */
    public static void openEditor(IEditorInput input, String editorID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            page.openEditor(input, editorID);
        }
        catch(PartInitException ex) {
            Logger.logError("Could not open Editor " + editorID); //$NON-NLS-1$
            ex.printStackTrace();
        }
    }
    
    /**
     * Edit a File based up on its file extension or content type
     * @param file
     */
    public static void editFile(File file, ILDModel ldModel) {
        if(file == null || !file.exists()) {
            return;
        }
        
        // First - Check for an external Editor
        if(editInExternalEditor(file)) {
            return;
        }
        
        IEditorDescriptor descriptor = null;

        // Second - Is there a Content Extension Handler for this file?
        ILDContentHandler handler = ExtensionContentHandler.getInstance().getHandler(file);
        if(handler != null) {
            handler.editFile(file, ldModel);
            return;
        }
        
        // Third - Find Local Default Editor if there is one
        try {
            descriptor = getDefaultEditor(file);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        /*
         * No editor specified, so use the external editor call
         */
        if(descriptor == null) {
            openEditor(new FileEditorInput(file.getName(), file), IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
            //Program.launch(file.toString());
            return;
        }
        
        String editorID = descriptor.getId();
        
        if(!StringUtils.isSet(editorID)) {
            return;
        }
        
        // If it's a Rich Text Editor check... 
        if(RichTextEditorPart.ID.equals(editorID)) {
            // ...for Mac font types and sizes the RTE does not work on Mac OSX
            if(Platform.getOS().equals(Platform.OS_MACOSX)) {
                //editorID = SimpleTextEditor.ID;
            }
            // ...User Prefs set not to use RTE
            if(!LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(ILDAuthorPreferenceConstants.PREFS_USE_RTE)) {
                editorID = SimpleTextEditor.ID;
            }
        }
        
        // Default
        openEditor(new FileEditorInput(file.getName(), file), editorID);
    }
    
    /**
     * Edit a file in an external editor
     * @param file
     * @return True if successful 
     */
    public static boolean editInExternalEditor(File file) {
        String programPath = getExternalEditorPath(file);
        
        // No external editor
        if(programPath == null) {
            return false;
        }
        
        String[] cmds = null;
        String cmd = null;
        
        if(Platform.OS_WIN32.equalsIgnoreCase(Platform.getOS())) {
            // This will ensure any user command line options are used in programPath
            cmd = programPath + " " + "\"" + file.getAbsolutePath() + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
//            cmd = new String[] {
//                    programPath,
//                    file.getAbsolutePath()
//            };
        }
        else if(Platform.OS_MACOSX.equalsIgnoreCase(Platform.getOS())) {
            cmds = new String[] {
                    "/usr/bin/open", //$NON-NLS-1$
                    "-a", //$NON-NLS-1$
                    programPath,
                    file.getAbsolutePath()
            };
        }
        else if(Platform.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
            // No good for scripts
            //cmd = programPath + " " + file.getAbsolutePath(); //$NON-NLS-1$
            
            try {
                launchLinux(programPath, file.getAbsolutePath());
                return true;
            }
            catch(Exception ex) {
                System.err.println("Could not launch external editor: " + programPath);  //$NON-NLS-1$
                ex.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
        
        try {
            if(cmds != null) {
                Runtime.getRuntime().exec(cmds);
            }
            else {
                Runtime.getRuntime().exec(cmd);
            }
            return true;
        }
        catch(Exception ex) {
            System.err.println("Could not launch external editor: " + programPath);  //$NON-NLS-1$
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * @param file The file to be edited
     * @return The path of the external editor to use (if any) for a given editor name
     * as set in the Preferences, or null if not set
     */
    public static String getExternalEditorPath(File file) {
        String editors = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_EDITORS);
        
        if(editors.length() == 0) {
            return null;
        }
        
        String ext = FileUtils.getFileExtension(file);
        if(ext != null) {
            StringTokenizer t = new StringTokenizer(editors, ";"); //$NON-NLS-1$
            try {
                while(t.hasMoreElements()) {
                    String extension = t.nextToken();
                    String programPath = t.nextToken();
                    if(extension.equalsIgnoreCase(ext)) {
                        return programPath;
                    }
                }
            }
            catch(NoSuchElementException ex) {
            }
        }
        
        return null;
    }
    
    /**
     * @param file
     * @return The default Editor for a file or null. The file's contents is used as the basis.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static IEditorDescriptor getDefaultEditor(File file) throws IOException {
        IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
        FileInputStream fi = new FileInputStream(file);
        IContentType contentType = contentTypeManager.findContentTypeFor(fi, file.getName());
        fi.close(); // Must close it!!!
        IEditorRegistry editor_registry = PlatformUI.getWorkbench().getEditorRegistry();
        return editor_registry.getDefaultEditor(file.getName(), contentType);
    }
    
    
    // ==================================================================================
    // EXPERIMENTAL STUFF TO TRY TO SOLVE LINUX PROBLEMS
    // ==================================================================================
    
    private static void launchLinux(String cmd, String filePath) throws IOException {
        //String[] cmds = new String[] {
        //        "/bin/sh", //$NON-NLS-1$
        //        cmd
        //};
        
        String[] cmds = new String[] {
                cmd,
                filePath
        };
        
        ProcessBuilder pb = new ProcessBuilder(cmds);
        
        //for(String key : pb.environment().keySet()) {
        //    System.out.println(key + " = " + pb.environment().get(key));
        //}

        // Hack for Kompozer Shell Script
        // Something (Eclipse maybe?) is setting MOZILLA_FIVE_HOME to "/usr/lib/xulrunner-addons/"
        // And the Kompozer script uses that but "run-mozilla.sh" is not there!
        String MOZILLA_FIVE_HOME = pb.environment().get("MOZILLA_FIVE_HOME"); //$NON-NLS-1$
        if(MOZILLA_FIVE_HOME != null) {
            pb.environment().remove("MOZILLA_FIVE_HOME"); //$NON-NLS-1$
        }
        
        Process proc = pb.start();

        // any error message?
        StreamGobbler errorGobbler = new StreamGobbler(proc
                .getErrorStream(), "ERROR"); //$NON-NLS-1$

        // any output?
        StreamGobbler outputGobbler = new StreamGobbler(proc
                .getInputStream(), "OUTPUT"); //$NON-NLS-1$

        // kick them off
        errorGobbler.start();
        outputGobbler.start();

        // This blocks so don't use it
        //int exitVal = proc.waitFor();
        //System.out.println("ExitValue: " + exitVal); //$NON-NLS-1$
    }
    
    static class StreamGobbler extends Thread {
        InputStream is;

        String type;

        StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    System.out.println(type + "> " + line); //$NON-NLS-1$
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
}
