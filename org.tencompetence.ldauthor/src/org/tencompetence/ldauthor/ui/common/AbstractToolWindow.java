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
package org.tencompetence.ldauthor.ui.common;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


/**
 * Floating Tool Window that can persist state.
 * A lot of code taken from PopupDilaog class
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractToolWindow.java,v 1.3 2008/11/22 01:47:39 phillipus Exp $
 */
public abstract class AbstractToolWindow extends Window {
    
    /**
     * The dialog settings key name for stored dialog x location.
     */
    private static final String DIALOG_ORIGIN_X = "DIALOG_X_ORIGIN"; //$NON-NLS-1$

    /**
     * The dialog settings key name for stored dialog y location.
     */
    private static final String DIALOG_ORIGIN_Y = "DIALOG_Y_ORIGIN"; //$NON-NLS-1$

    /**
     * The dialog settings key name for stored dialog width.
     */
    private static final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$

    /**
     * The dialog settings key name for stored dialog height.
     */
    private static final String DIALOG_HEIGHT = "DIALOG_HEIGHT"; //$NON-NLS-1$


    protected AbstractToolWindow() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        setShellStyle(SWT.TOOL | SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        
        newShell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                dispose();
            }
        });
    }
    
    public boolean isOpen() {
        return getShell() != null;
    }
    
    public void toggleView() {
        if(isOpen()) {
            close();
        }
        else {
            open();
        }
    }
    
    protected IDialogSettings getDialogSettings() {
        return null;
    }
    
    /**
     * Saves the bounds of the shell in the appropriate dialog settings. The
     * bounds are recorded relative to the parent shell, if there is one, or
     * display coordinates if there is no parent shell. Subclasses typically
     * need not override this method, but may extend it (calling
     * <code>super.saveDialogBounds</code> if additional bounds information
     * should be stored. Clients may also call this method to persist the bounds
     * at times other than closing the dialog.
     * 
     * @param shell
     *            The shell whose bounds are to be stored
     */
    protected void saveDialogBounds(Shell shell) {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            Point shellLocation = shell.getLocation();
            Point shellSize = shell.getSize();
            Shell parent = getParentShell();
            if (parent != null) {
                Point parentLocation = parent.getLocation();
                shellLocation.x -= parentLocation.x;
                shellLocation.y -= parentLocation.y;
            }
            String prefix = getClass().getName();
            settings.put(prefix + DIALOG_WIDTH, shellSize.x);
            settings.put(prefix + DIALOG_HEIGHT, shellSize.y);

            settings.put(prefix + DIALOG_ORIGIN_X, shellLocation.x);
            settings.put(prefix + DIALOG_ORIGIN_Y, shellLocation.y);
        }
    }

    @Override
    protected Point getInitialSize() {
        Point result = getDefaultSize();
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            try {
                int width = settings.getInt(getClass().getName()
                        + DIALOG_WIDTH);
                int height = settings.getInt(getClass().getName()
                        + DIALOG_HEIGHT);
                result = new Point(width, height);

            } catch (NumberFormatException e) {
            }
        }
        // No attempt is made to constrain the bounds. The default
        // constraining behavior in Window will be used.
        return result;
    }
    
    @Override
    protected Point getInitialLocation(Point initialSize) {
        Point result = getDefaultLocation(initialSize);
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            try {
                int x = settings.getInt(getClass().getName()
                        + DIALOG_ORIGIN_X);
                int y = settings.getInt(getClass().getName()
                        + DIALOG_ORIGIN_Y);
                result = new Point(x, y);
                // The coordinates were stored relative to the parent shell.
                // Convert to display coordinates.
                Shell parent = getParentShell();
                if (parent != null) {
                    Point parentLocation = parent.getLocation();
                    result.x += parentLocation.x;
                    result.y += parentLocation.y;
                }
            } catch (NumberFormatException e) {
            }
        }
        // No attempt is made to constrain the bounds. The default
        // constraining behavior in Window will be used.
        return result;
    }

    protected Point getDefaultLocation(Point initialSize) {
        return super.getInitialLocation(initialSize);
    }
    
    protected Point getDefaultSize() {
        return super.getInitialSize();
    }
    
    public void dispose() {
        saveDialogBounds(getShell());
    }
}
