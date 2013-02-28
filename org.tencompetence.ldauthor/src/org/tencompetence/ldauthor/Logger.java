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
package org.tencompetence.ldauthor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Logger utility class
 * 
 * @author Phillip Beauvoir
 * @version $Id: Logger.java,v 1.2 2007/09/03 11:48:19 phillipus Exp $
 */
public final class Logger {

    /**
     * Convenience method to log an OK event
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logOK(String message, Throwable t) {
        log(IStatus.OK, message, t);
    }

    /**
     * Convenience method to log an OK event
     * 
     * @param message Message to print
     */
    public static void logOK(String message) {
        log(IStatus.OK, message, null);
    }

    /**
     * Convenience method to log an error
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logError(String message, Throwable t) {
        log(IStatus.ERROR, message, t);
    }

    /**
     * Convenience method to log an error
     * 
     * @param message Message to print
     */
    public static void logError(String message) {
        log(IStatus.ERROR, message, null);
    }

    /**
     * Convenience method to log some info
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logInfo(String message, Throwable t) {
        log(IStatus.INFO, message, t);
    }

    /**
     * Convenience method to log some info
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logInfo(String message) {
        log(IStatus.INFO, message, null);
    }

    /**
     * Convenience method to log a warning
     * 
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void logWarning(String message, Throwable t) {
        log(IStatus.WARNING, message, t);
    }

    /**
     * Convenience method to log a warning
     * 
     * @param message Message to print
     */
    public static void logWarning(String message) {
        log(IStatus.WARNING, message, null);
    }

    /**
     * Helper logger to log events for this bundle plug-in
     * 
     * @param severity can be
     *          IStatus.WARNING
     *          IStatus.INFO
     *          IStatus.ERROR
     *          IStatus.OK
     *          IStatus.CANCEL
     * @param message Message to print
     * @param t Exception that is thrown
     */
    public static void log(int severity, String message, Throwable t) {
        LDAuthorPlugin.getDefault().getLog().log(
                new Status(severity, LDAuthorPlugin.getDefault().getId(), IStatus.OK, message, t));
        System.out.println(message);
    }
    
    
    // ==============================================================================
    // DIALOGS FOR ERRORS AND WARNINGS
    // ==============================================================================
    
    /**
     * Convenience method to open a standard error dialog
     * 
     * @param message
     *            the message
     */
    public static void showErrorDialog(String message) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", message); //$NON-NLS-1$
    }
    
    
    /**
     * Convenience method to open a standard warning dialog
     * 
     * @param message
     *            the message
     */
    public static void showWarningDialog(String message) {
        MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Warning", message); //$NON-NLS-1$
    }

    /**
     * Convenience method to open a standard information dialog
     * 
     * @param message
     *            the message
     */
    public static void showInformationDialog(String message) {
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Information", message); //$NON-NLS-1$
    }
}
