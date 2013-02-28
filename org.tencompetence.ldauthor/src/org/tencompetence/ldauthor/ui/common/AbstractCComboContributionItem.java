// ------------------------------------------------------------------------------
// Copyright (c) 2005, 2007 IBM Corporation and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
// ------------------------------------------------------------------------------
package org.tencompetence.ldauthor.ui.common;

import java.util.Collection;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Wraps a CCombo in a ContributionItem for use in a toolbar
 * 
 * Does not use a ComboViewer because of tabbing issues - see bug 78885
 * 
 * @author Jeff Hardy
 * 
 */
public abstract class AbstractCComboContributionItem extends ContributionItem {

    protected CCombo fCombo;

    protected ToolItem toolItem;

    protected CoolItem coolItem;

    protected int style;

    protected Collection<String> fInput;

    /**
     * Creates a new instance.
     */
    public AbstractCComboContributionItem(int style) {
        super();
        this.style = style;
    }

    @Override
    public void fill(ToolBar parent, int index) {
        toolItem = new ToolItem(parent, SWT.SEPARATOR);
        Control box = createControl(parent);
        toolItem.setControl(box);
        Point preferredSize = fCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        toolItem.setWidth(preferredSize.x);
    }

    @Override
    public void fill(CoolBar coolBar, int index) {
        Control box = createControl(coolBar);

        if(index >= 0) {
            coolItem = new CoolItem(coolBar, SWT.DROP_DOWN, index);
        }
        else {
            coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
        }

        // Set the back reference.
        coolItem.setData(this);

        // Add the toolbar to the CoolItem widget.
        coolItem.setControl(box);

        // If the toolbar item exists then adjust the size of the cool item.
        Point toolBarSize = box.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        // Set the preferred size to the size of the toolbar plus trim.
        coolItem.setMinimumSize(toolBarSize);
        coolItem.setPreferredSize(toolBarSize);
        coolItem.setSize(toolBarSize);
    }

    @Override
    public void fill(Composite parent) {
        createControl(parent);
    }

    protected Control createControl(final Composite parent) {
        fCombo = new CCombo(parent, style);
        fCombo.setVisibleItemCount(10);
        fCombo.setEnabled(true);
        fCombo.setItems(fInput.toArray(new String[0]));
        fCombo.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                dispose();
            }
        });

        fCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                performSelectionModified(fCombo.getText());
            }

            public void widgetSelected(SelectionEvent e) {
                performSelectionChanged(fCombo.getText());
            }
        });

        return fCombo;
    }

    /**
     * Returns the currently selected method configuration
     */
    protected int getSelectionIndex() {
        return fCombo.getSelectionIndex();
    }

    public void setInput(Collection<String> input) {
        fInput = input;
        if(fCombo != null) {
            fCombo.setItems(fInput.toArray(new String[0]));
        }
    }

    public Collection<String> getInput() {
        return fInput;
    }
    
    /**
     * The combo selection changed
     */
    protected abstract void performSelectionChanged(String text);
    
    /**
     * User typed in new selection
     */
    protected abstract void performSelectionModified(String text);

    public CCombo getCCombo() {
        return fCombo;
    }

    public ToolItem getToolItem() {
        return toolItem;
    }

    public CoolItem getCoolItem() {
        return coolItem;
    }
}
