package org.tencompetence.qtieditor.ui;

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

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Stack Composite allows child Composites to be flipped like a Card layout
 * 
 * @author Phillip Beauvoir
 * @version $Id: StackComposite.java,v 1.2 2008/11/19 12:37:21 rp_cherian Exp $
 */
//public class StackComposite extends Composite {
public class StackComposite extends Composite {
	/**
	 * Layout for StackPanel
	 */
	private StackLayout fStackLayout;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Parent Composite
	 * @param style
	 */
	public StackComposite(Composite parent, int style) {
		super(parent, style);
		fStackLayout = new StackLayout();
		setLayout(fStackLayout);
	}

	/**
	 * Set the top control on the Stack and Layout
	 * 
	 * @param control
	 */
	public void setControl(Control control) {
		if (fStackLayout.topControl != control) {
			fStackLayout.topControl = control;
			control.setLayoutData(new GridData(GridData.FILL_BOTH));
			layout();
		}
	}

}