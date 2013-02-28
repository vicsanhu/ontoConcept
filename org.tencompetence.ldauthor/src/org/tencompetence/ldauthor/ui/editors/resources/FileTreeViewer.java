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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * FileTreeViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: FileTreeViewer.java,v 1.13 2010/01/06 15:34:25 phillipus Exp $
 */
public class FileTreeViewer extends TreeViewer {
    
    private int delay = 10000; // 10 secs
    
    private Runnable refreshTimer;

    public FileTreeViewer(Composite parent, int style, ILDEditorPart editor) {
        super(parent, style | SWT.MULTI);
        
        setContentProvider(new FileTreeContentProvider());
        setLabelProvider(new FileTreeLabelProvider());
        
        // Sort folders first, files second, alphabetical
        setSorter(new ViewerSorter() {
            @Override
            public int category(Object element) {
                if(element instanceof File) {
                    File f = (File)element;
                    return f.isDirectory() ? 0 : 1;
                }
                return 0;
            }
        });

        setupRefreshTimer();
    }

    /**
     * Set the Root Folder
     * @param rootFolder
     */
    public void setRootFolder(File rootFolder) {
        if(rootFolder.exists()) {
            setInput(rootFolder);
        }
    }
    
    public void startRefreshTimer() {
        Display.getDefault().timerExec(0, refreshTimer);
    }
    
    public void stopRefreshTimer() {
        Display.getDefault().timerExec(-1, refreshTimer);
    }
    
    /**
     * Set up the Refresh timer
     */
    private void setupRefreshTimer() {
        refreshTimer = new Runnable() {
            public void run() { 
                if(!getTree().isDisposed()) { // this is important!
                    refresh();
                    Display.getDefault().timerExec(delay, this);  // run again
                }
            }
        };

        /*
         * Dispose of timer
         */
        getTree().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(refreshTimer != null) {
                    Display.getDefault().timerExec(-1, refreshTimer);
                }
            }
        });
    }



    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================
    private class FileTreeContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            if(child instanceof File) {
                return ((File)child).getParentFile();
            }
            return null;
        }
        
        public Object[] getChildren(Object parent) {
            if(parent instanceof File && ((File)parent).isDirectory()) {
                return ((File)parent).listFiles();
            }
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            if(parent instanceof File && ((File)parent).isDirectory()) {
                File[] files = ((File)parent).listFiles();
                return files != null && files.length > 0;
            }
            return false;
        }
    }
    
    
    // =============================================================================================
    //
    //                                   LABEL PROVIDER
    //
    // =============================================================================================
    private class FileTreeLabelProvider extends LabelProvider {
        
        @Override
        public String getText(Object obj) {
            if(obj instanceof File) {
                File f = (File)obj;
                return f.getName();
            }
            else {
                return ""; //$NON-NLS-1$
            }
        }
        
        @Override
        public Image getImage(Object obj) {
            Image image = null;
            
            if(obj instanceof File) {
                File f = (File)obj;
                if(f.isDirectory()) {
                    image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
                }
                else {
                    image = ImageFactory.getProgramFileIcon(f);
                    // On Ubuntu the image can be too big!
                    if(image.getImageData().height > 16) {
                        System.out.println((image.getImageData().height));
                    }
                }
            }
            
            if(image == null) {
                image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
            }
            
            return image;
        }
    }
}
