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
package org.tencompetence.ldauthor.ui.intro;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.part.IntroPart;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;



/**
 * Welcome screen
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorIntroPart.java,v 1.10 2008/09/10 22:39:04 phillipus Exp $
 */
public class LDAuthorIntroPart
extends IntroPart {
    /**
     * Form
     */
    private Form fForm;
    
    /**
     * Constructor
     */
    public LDAuthorIntroPart() {
        super();        
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.IIntroPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
    public void createPartControl(Composite parent) {
        ImageHyperlink link = null;
        
		fForm = AppFormToolkit.getInstance().createForm(parent);
        
        Font mainFont = fForm.getFont();
        FontData fd = mainFont.getFontData()[0];
        fd.setHeight(14);
        fForm.setFont(new Font(parent.getDisplay(), fd));
        
        fForm.setText(Messages.LDAuthorIntroPart_0);
        
        fForm.getBody().setLayout(new GridLayout());
        
        /*
         * Sub-panel centred
         */
        Composite client = AppFormToolkit.getInstance().createComposite(fForm.getBody());
        client.setLayout(new GridLayout(1, false));
        client.setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, true, false));
        
        createSpacer(client);
        createSpacer(client);
        
        fd.setHeight(10);
        Font textFont = new Font(client.getDisplay(), fd);
        
        /*
         * Start
         */
        link = AppFormToolkit.getInstance().createImageHyperlink(client, SWT.NULL);
        link.setImage(ImageFactory.getImage(ImageFactory.IMAGE_APP_48));
        //link.setHoverImage(ImageFactory.getImage(ImageFactory.ICON_APP48_BORDER));
        link.setText(Messages.LDAuthorIntroPart_1);
        link.setFont(textFont);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                // Threaded to stop SWT dispose error
                Display.getCurrent().asyncExec(new Runnable() {
                    public void run() {
                        IIntroManager manager = PlatformUI.getWorkbench().getIntroManager();
                        manager.closeIntro(LDAuthorIntroPart.this);
                    }
                });
            }
        });
        
        /*
         * Help
         */
        createSpacer(client);
        link = AppFormToolkit.getInstance().createImageHyperlink(client, SWT.NULL);
        link.setImage(ImageFactory.getImage(ImageFactory.IMAGE_APP_48));
        //link.setHoverImage(StrakerPlugin.getDefault().getImage(ImageFactory.ICON_HELP48_BORDER));
        link.setText(Messages.LDAuthorIntroPart_2);
        link.setFont(textFont);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                PlatformUI.getWorkbench().getHelpSystem().displayHelp();
            }
        });
	}
    
    /**
     * @param parent
     */
    private void createSpacer(Composite parent) {
        AppFormToolkit.getInstance().createLabel(parent, ""); //$NON-NLS-1$
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.intro.IIntroPart#standbyStateChanged(boolean)
	 */
	public void standbyStateChanged(boolean standby) {
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.intro.IIntroPart#setFocus()
     */
    @Override
    public void setFocus() {
    }

}
