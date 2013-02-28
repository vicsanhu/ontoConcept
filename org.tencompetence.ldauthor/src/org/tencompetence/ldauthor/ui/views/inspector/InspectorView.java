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

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.StackComposite;


/**
 * The Inspector View
 * 
 * @author Phillip Beauvoir
 * @version $Id: InspectorView.java,v 1.7 2009/06/10 10:16:48 phillipus Exp $
 */
public class InspectorView extends ViewPart
implements IContextProvider {

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".inspectorView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".inspectorViewHelp"; //$NON-NLS-1$
    
    private InspectorTitleComposite fTitleBar;
    
    private StackComposite fStackComposite;
    
    private InspectorPageFactory fPageMapper = new InspectorPageFactory();
    

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        parent.setLayout(layout);
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        factory.adapt(parent);
        factory.paintBordersFor(parent);
        
        fTitleBar = new InspectorTitleComposite(parent);
        GridData gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fTitleBar.setLayoutData(gd);
        
        fStackComposite = new StackComposite(parent, SWT.NULL);
        gd = new GridData(GridData.FILL, SWT.FILL, true, true);
        fStackComposite.setLayoutData(gd);
        factory.adapt(fStackComposite);
        factory.paintBordersFor(fStackComposite);
        
        setMainTitle(" "); //$NON-NLS-1$
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    public void setMainTitle(String title) {
        fTitleBar.setTitle(title, null);
    }
    
    public void setInput(IInspectorProvider client, Object element) {
        AbstractInspectorPage inspectorPage = fPageMapper.getPage(fStackComposite, element);
        if(inspectorPage != null) {
            inspectorPage.setInput(client, element);
            fStackComposite.setControl(inspectorPage);
        }
    }

    @Override
    public void setFocus() {
        fTitleBar.setFocus();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fPageMapper != null) {
            fPageMapper.dispose();
        }
    }
    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContextChangeMask()
     */
    public int getContextChangeMask() {
        return NONE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
     */
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
     */
    public String getSearchExpression(Object target) {
        return Messages.InspectorView_0;
    }

}
