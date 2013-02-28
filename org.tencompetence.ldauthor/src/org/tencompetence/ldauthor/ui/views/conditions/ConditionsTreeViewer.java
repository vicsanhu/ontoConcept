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
package org.tencompetence.ldauthor.ui.views.conditions;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.expressions.IConditionsType;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Conditions Tree Viewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionsTreeViewer.java,v 1.6 2009/06/16 19:56:43 phillipus Exp $
 */
public class ConditionsTreeViewer extends TreeViewer {

    public ConditionsTreeViewer(Composite parent) {
        super(parent, SWT.MULTI);
        
        setContentProvider(new ConditionsTreeContentProvider());
        setLabelProvider(new ConditionsTreeLabelProvider());
    }
    

    private class ConditionsTreeContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ILDModel) {
                return ((ILDModel)parent).getMethodModel().getConditionsModel().getChildren().toArray();
            }
            else {
                return new Object[0];
            }
        }
        
        public Object getParent(Object child) {
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            // Conditions
            if(parent instanceof IConditionsType) {
                IConditionsType conditions = (IConditionsType)parent;
                return conditions.getConditions().toArray();
            }
            
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            // Conditions
            if(parent instanceof IConditionsType) {
                return ((IConditionsType)parent).getConditions().size() > 0;
            }
            return false;
        }
    }

    /**
     * View Label Provider
     */
    private class ConditionsTreeLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            if(element instanceof IConditionsType) {
                String title = ((IConditionsType)element).getTitle();
                return StringUtils.isSet(title) ? title : Messages.ConditionsTreeViewer_0;
            }
            if(element instanceof IConditionType) {
                IConditionType type = (IConditionType)element;
                IConditionsType conditions = (IConditionsType)type.getParent();
                int num = conditions.getConditions().indexOf(type) + 1;
                return Messages.ConditionsTreeViewer_1 + 
                       " " + num; //$NON-NLS-1$
            }

            return element.toString();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof IConditionsType) {
                return ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER);
            }
            if(obj instanceof IConditionType) {
                return ImageFactory.getImage(ImageFactory.ICON_CONDITION);
            }

            return null;
        }
    }
}
