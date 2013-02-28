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
package org.tencompetence.ldauthor.ui.views;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.ui.common.AbstractToolWindow;
import org.tencompetence.ldauthor.ui.common.BrowserWidgetNotSupportedException;
import org.tencompetence.ldauthor.ui.views.browser.BrowserView;
import org.tencompetence.ldauthor.ui.views.preview.PreviewWindow;


/**
 * Manager for creating, showing and selecting View parts
 * 
 * @author Phillip Beauvoir
 * @version $Id: ViewManager.java,v 1.10 2008/11/22 01:47:39 phillipus Exp $
 */
public class ViewManager {
    /*
     * Should we use Floaters?
     */
    public static boolean useFloatingWindows = false;
    
    
    /**
     * Attempt to show the given View if hidden, or bring it to focus
     * @param viewID The ID of the View to show
     * @return The IViewPart or null
     */
    public static IViewPart showViewPart(String viewID, boolean activate) {
        if(useFloatingWindows) {
            AbstractToolWindow window = findFloatingWindow(viewID);
            if(window != null) {
                window.open();
                return null;
            }
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart viewPart = null;
        try {
            // viewPart = page.showView(viewID, null, activate ? IWorkbenchPage.VIEW_ACTIVATE : IWorkbenchPage.VIEW_VISIBLE);
            viewPart = page.showView(viewID);
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
        
        return viewPart;
    }
    
    /**
     * Hide a View Part
     * @param viewID The ID of the View to hide
     */
    public static void hideViewPart(String viewID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference ref = page.findViewReference(viewID);
        if(ref != null) {
            page.hideView(ref);
        }
    }
    
    /**
     * Hide or show a view part in toggle manner
     * 
     * @param viewID
     */
    public static void toggleViewPart(String viewID, boolean activate) {
        if(useFloatingWindows) {
            AbstractToolWindow window = findFloatingWindow(viewID);
            if(window != null) {
                window.toggleView();
                return;
            }
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference ref = page.findViewReference(viewID);
        if(ref != null) {
            page.hideView(ref);
        }
        else {
            showViewPart(viewID, activate);
        }
    }
    
    /**
     * Attempt to find the given View
     * @param viewID The ID of the VIew to find
     * @return The IViewPart or null if not found
     */
    public static IViewPart findViewPart(String viewID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return page.findView(viewID);
    }
    
    /**
     * Attempt to show the given additional View if hidden as another instance, or bring it to focus
     * @param viewID
     * @param secondaryID
     * @return The IViewPart or null
     */
    public static IViewPart showAdditionalViewPart(String viewID, String secondaryID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart viewPart = null;
        try {
            viewPart = page.showView(viewID, secondaryID, IWorkbenchPage.VIEW_ACTIVATE);
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
        return viewPart;
    }
    
    /**
     * Find an alternate FloatingWindow
     * @param viewID
     * @return
     */
    public static AbstractToolWindow findFloatingWindow(String viewID) {
        if(PreviewWindow.ID.equals(viewID)) {
            return PreviewWindow.getInstance();
        }
        
        return null;
    }

    /**
     * Show the location in the Default Browser window as determined by Prefs.
     * This will either be the internal Browser or external
     * @param location
     */
    public static void showInDefaultBrowser(String location) {
        boolean useExternalBrowser = LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(ILDAuthorPreferenceConstants.PREFS_BROWSER_USE_EXTERNAL);
        if(useExternalBrowser) {
            showInExternalBrowser(location);
        }
        else {
            showInInternalBrowser(location);
        }
    }

    /**
     * Show the location in the Internal Browser window
     * @param location
     */
    public static void showInInternalBrowser(String location) {
        // Try the first time to see if we can do it...
        if(BrowserView.BrowserSupported) {  // This is true at first, so make an attempt...
            IViewPart viewPart = showViewPart(BrowserView.ID, false);
            if(viewPart instanceof BrowserView) { // now set URL
                BrowserView view = (BrowserView)viewPart;
                try {
                    view.setURL(location);
                }
                catch(BrowserWidgetNotSupportedException ex) {
                    // Nope, so launch External Browser
                    showInExternalBrowser(location);
                }
            }
        }
        
        // No, so use External Browser
        else {
            showInExternalBrowser(location);
        }
    }

    /**
     * Show the location in the External Browser window
     * @param location
     */
    public static void showInExternalBrowser(String location) {
        URL url = null;
        
        /*
         * Create URL
         */
        try {
            url = new URL(location);
        }
        catch(MalformedURLException ex) {
            /*
             * Try a http protocol
             */
            try {
                url = new URL("http://" + location);  //$NON-NLS-1$
            }
            catch(MalformedURLException ex1) {
                ex1.printStackTrace();
            }
        }
        
        if(url == null) {
            return;
        }
        
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            browser.openURL(url);
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
    }
}
