/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.preferences.templates;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.ldauthor.templates.ITemplate;
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * TreeViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: TemplatesTreeViewer.java,v 1.3 2008/12/15 15:00:35 phillipus Exp $
 */
public class TemplatesTreeViewer extends TreeViewer {
    
    private LDTemplateManager fLDTemplateManager;
    
    public TemplatesTreeViewer(Composite parent, int style, LDTemplateManager templateManager) {
        super(parent, style);
        
        setLabelProvider(new TemplatesTreeViewerLabelProvider());
        setContentProvider(new TemplatesTreeViewerContentProvider());
        
        setAutoExpandLevel(2);
        
        fLDTemplateManager = templateManager;
        setInput(fLDTemplateManager);
    }
    

    // ====================================================================================

    /**
     * View Content Provider
     */
    private class TemplatesTreeViewerContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof LDTemplateManager) {
                return ((LDTemplateManager)parentElement).getTemplateGroups().toArray();
            }
                
            if(parentElement instanceof ITemplateGroup) {
                return ((ITemplateGroup)parentElement).getTemplates().toArray();
            }
            
            return new Object[0];
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object element) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object element) {
            if(element instanceof ITemplateGroup) {
                return ((ITemplateGroup)element).getTemplates().size() > 0;
            }
            
            return false;
        }
    }

    /**
     * View Label Provider
     */
    private class TemplatesTreeViewerLabelProvider extends LabelProvider {

        public TemplatesTreeViewerLabelProvider() {
        }
        
        @Override
        public String getText(Object element) {
            if(element instanceof ITemplateGroup) {
                return ((ITemplateGroup)element).getName();
            }
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getName();
            }
            return element.toString();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof ITemplateGroup) {
                return ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER);
            }
            if(obj instanceof ITemplate) {
                return ((ITemplate)obj).getImage();
            }

            return null;
        }
    }
}
