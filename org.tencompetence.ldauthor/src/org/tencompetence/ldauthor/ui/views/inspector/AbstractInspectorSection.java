/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * Inspector Section
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractInspectorSection.java,v 1.5 2009/06/10 10:16:48 phillipus Exp $
 */
public abstract class AbstractInspectorSection extends Composite
implements IInspectorSection
{
    
    private IInspectorProvider fProvider;
    private Object fElement;
    
    public AbstractInspectorSection(Composite parent) {
        super(parent, SWT.NULL);
        
        InspectorWidgetFactory.getInstance().adapt(this);
        InspectorWidgetFactory.getInstance().paintBordersFor(this);
    }
    
    public void setInput(IInspectorProvider provider, Object element) {
        fProvider = provider;
        fElement = element;
    }

    public IInspectorProvider getInspectorProvider() {
        return fProvider;
    }
    
    public Object getElement() {
        return fElement;
    }
}
