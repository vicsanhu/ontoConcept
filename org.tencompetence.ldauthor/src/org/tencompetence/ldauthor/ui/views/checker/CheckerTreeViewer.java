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
package org.tencompetence.ldauthor.ui.views.checker;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.CheckerCategory;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.ErrorCheckItem;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.ILDCheckerItem;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.WarningCheckItem;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Tree for Checker View
 * 
 * @author Phillip Beauvoir
 * @version $Id: CheckerTreeViewer.java,v 1.7 2009/07/03 11:13:16 phillipus Exp $
 */
public class CheckerTreeViewer
extends TreeViewer {
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public CheckerTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        setContentProvider(new CheckerViewContentProvider());
        setLabelProvider(new CheckerViewLabelProvider());
    }
    
    public void setLDModel(IReCourseLDModel ldModel) {
        setInput(ldModel);
        expandAll();
    }
    
    /**
     * View Content Provider
     */
    private class CheckerViewContentProvider implements ITreeContentProvider {
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof IReCourseLDModel) {
                LDChecker checker = new LDChecker((IReCourseLDModel)parentElement);
                List<ILDCheckerItem> list = checker.check();
                if(!list.isEmpty()) {
                    return list.toArray();
                }
            }
            if(parentElement instanceof CheckerCategory) {
                return ((CheckerCategory)parentElement).getList().toArray();
            }
            return new String[] { Messages.CheckerTreeViewer_0 } ;
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
            if(element instanceof CheckerCategory) {
                return ((CheckerCategory)element).hasErrors() || ((CheckerCategory)element).hasWarnings();
            }
            return false;
        }
    }
    
    /**
     * View Label Provider
     */
    private class CheckerViewLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            return element.toString();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof CheckerCategory) {
                CheckerCategory category = (CheckerCategory)obj;
                
                if(category.hasErrors() || category.hasWarnings()) {
                    Object o = category.getComponent();
                    
                    if(o instanceof ILDModelObject) {
                        return ImageFactory.getIconLDType((ILDModelObject)o);
                    }
                    
                    if(o instanceof ILDModel) {
                        return ImageFactory.getImage(ImageFactory.ICON_LD);
                    }
                    
                    return null;
                }
                
                return ImageFactory.getImage(ImageFactory.ICON_GREENTICK);
            }
            
            if(obj instanceof ErrorCheckItem) {
                return ImageFactory.getImage(ImageFactory.ICON_REDCROSS);
            }
            
            if(obj instanceof WarningCheckItem) {
                return ImageFactory.getImage(ImageFactory.ICON_ORANGE_EXCLAMATION);
            }
            
            return ImageFactory.getImage(ImageFactory.ICON_GREENTICK);
        }
    }
}
