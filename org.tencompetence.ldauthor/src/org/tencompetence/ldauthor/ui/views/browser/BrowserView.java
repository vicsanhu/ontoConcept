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
package org.tencompetence.ldauthor.ui.views.browser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.ui.common.BrowserWidgetNotSupportedException;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.views.IBrowserView;


/**
 * The Browser view.  This consists of a <code>Browser</code> control, and an
 * address bar consisting of a <code>Label</code> and a <code>Text</code> 
 * control.  This registers handling actions for the retargetable actions added 
 * by <code>BrowserActionBuilder</code> (Back, Forward, Stop, Refresh).  
 * This also hooks listeners on the Browser control for status and progress
 * messages, and redirects these to the status line.
 * 
 * @author Phillip Beauvoir
 * @version $Id: BrowserView.java,v 1.8 2009/10/30 14:18:10 phillipus Exp $
 */
public class BrowserView
extends ViewPart
implements IBrowserView
{
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".browserView"; //$NON-NLS-1$
    public static String HELPID_BROWSER_VIEW = LDAuthorPlugin.PLUGIN_ID + ".browserViewHelp"; //$NON-NLS-1$
	
	/**
	 * The Browser component
	 */
    protected Browser fBrowser;
	
	/**
	 * Location textField
	 */
    protected Text fLocationTextField;
	
	/**
	 * The last viewed URL
	 */
    protected static String fStoredUrl = "about:blank"; //$NON-NLS-1$
	
	/**
	 * Whether to restore the last viewed URL
	 */
    protected static boolean fRestoreLastUrl;
	
	
    /*
     * The Browser doesn't always work on all platforms!
     */
    public static boolean BrowserSupported = true;
    
	
	// Actions
	private Action fActionBack;
	private Action fActionForward;
	private Action fActionStop;
	private Action fActionRefresh;
    private Action fActionToggleRestoreLastUrl;

    
	/**
	 * Default Constructor
	 */
	public BrowserView() {
	}
	
	/**
	 * Set the URL in the browser
	 * @param url
	 * @throws BrowserWidgetNotSupportedException 
	 */
	public void setURL(String url) throws BrowserWidgetNotSupportedException {
        if(!BrowserSupported) {
            throw new BrowserWidgetNotSupportedException();
        }
        
	    if(fBrowser != null && url != null && !url.equals(fBrowser.getUrl())) {
            fBrowser.setUrl(url);
	        fStoredUrl = url;
	    }
	}
	
    /* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
    public void createPartControl(Composite parent) {
        loadPreferences();
        
		try {
            fBrowser = createBrowser(parent, getViewSite().getActionBars());
        }
        catch(BrowserWidgetNotSupportedException ex) {
            ex.printStackTrace();
            return;
        }
        
		if(fBrowser != null && fRestoreLastUrl) {
            fBrowser.setUrl(fStoredUrl);
		}
        
        // Register Help Context
        if(fBrowser != null) {
            PlatformUI.getWorkbench().getHelpSystem().setHelp(fBrowser, HELPID_BROWSER_VIEW);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID_BROWSER_VIEW);
        }
	}

    /**
     * Load local view preferences
     */
    protected void loadPreferences() {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        fRestoreLastUrl = store.getBoolean(ILDAuthorPreferenceConstants.PREFS_BROWSER_VIEW_RESTORE_URL);
        fStoredUrl = store.getString(ILDAuthorPreferenceConstants.PREFS_BROWSER_VIEW_URL);
    }

    /**
     * Save local view preferences
     */
    protected void savePreferences() {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        store.setValue(ILDAuthorPreferenceConstants.PREFS_BROWSER_VIEW_RESTORE_URL, fRestoreLastUrl);
        store.setValue(ILDAuthorPreferenceConstants.PREFS_BROWSER_VIEW_URL, fStoredUrl);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
    public void setFocus() {
		if(fBrowser != null && !fBrowser.isDisposed()) {
            fBrowser.setFocus();
		}
	}
	
    /**
	 * Create the Browser component and actions.
	 * 
	 * @param parent
	 * @param actionBars
	 * @return
	 */
	protected Browser createBrowser(Composite parent, final IActionBars actionBars) throws BrowserWidgetNotSupportedException {
        GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);
        
        /**
         * These controls have to be put on in the right order...
         */
        
		Label labelAddress = new Label(parent, SWT.NONE);
		labelAddress.setText(Messages.BrowserView_0);
		
		fLocationTextField = new Text(parent, SWT.BORDER);
		GridData data = new GridData();
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		fLocationTextField.setLayoutData(data);

        try {
            fBrowser = new ExtendedBrowser(parent, SWT.NONE);
            BrowserSupported = true;
        }
        catch(SWTError ex) {
            /*
             * Browser Widget not supported
             */
            BrowserSupported = false;
            labelAddress.setVisible(false);
            fLocationTextField.setVisible(false);
            Label label = new Label(parent, SWT.CENTER);
            label.setText(Messages.BrowserView_1);
            label.moveAbove(labelAddress);
            throw new BrowserWidgetNotSupportedException(ex);
        }

		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
        fBrowser.setLayoutData(data);

        fBrowser.addProgressListener(new ProgressAdapter() {
			IProgressMonitor monitor = actionBars.getStatusLineManager().getProgressMonitor();
			boolean working = false;
			int workedSoFar;
			@Override
            public void changed(ProgressEvent event) {
				if(event.total == 0) {
				    monitor.done();
				    return;
				}
				if(!working) {
					if(event.current == event.total) {
					    return;
					}
					monitor.beginTask("", event.total); //$NON-NLS-1$
					workedSoFar = 0;
					working = true;
				}
				monitor.worked(event.current - workedSoFar);
				workedSoFar = event.current;
			}
			
			@Override
            public void completed(ProgressEvent event) {
				monitor.done();
				working = false;
			}
		});
		
        fBrowser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
			    // To open up another mini-Browser:
				
			    //Shell shell = new Shell(Display.getDefault());
				//shell.setText("New Window");
				//shell.setLayout(new FillLayout());
				//Browser browser = new Browser(shell, SWT.NONE);
				//shell.open();
				//browser.setUrl("http://www.eclipse.org");
				//event.browser = browser;
				
			    // These open and close listeners now need to be added to this new Browser
			    // And destroy the shell on a close listener
			    // See http://www.jdocs.com/eclipse/3.0/api/org/eclipse/swt/browser/WindowEvent.html
			    
			    // Launches external native Browser
                if(event.browser != null) { // this can be null
                    fBrowser.setUrl(event.browser.getUrl());
                }
			}
		});
		
        fBrowser.addStatusTextListener(new StatusTextListener() {
			IStatusLineManager status = actionBars.getStatusLineManager(); 
			public void changed(StatusTextEvent event) {
				status.setMessage(event.text);
			}
		});
		
        fBrowser.addLocationListener(new LocationAdapter() {
            @Override
            public void changing(LocationEvent event) {
                fLocationTextField.setText(event.location);
            }
            
			@Override
            public void changed(LocationEvent event) {
				fLocationTextField.setText(event.location);
				refreshActions();
				fStoredUrl = fBrowser.getUrl();
			}
		});
		
        fBrowser.addTitleListener(new TitleListener() {
            public void changed(TitleEvent event) {
                setPartName(event.title);
            }
        });
		
		fLocationTextField.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetDefaultSelected(SelectionEvent e) {
                fBrowser.setUrl(fLocationTextField.getText());
			}
		});
		
		// Make Actions
		makeActions();
		
		// Set up ToolBar
		makeLocalToolBar();
		
		// Set up local menu actions
		makeMenuActions();
		
		return fBrowser;
	}
	
	/**
	 * Make the Actions
	 */
	private void makeActions() {
	    fActionBack = new LDAuthorActionFactory.BackAction() {
			@Override
            public void run() {
				fBrowser.back();
			}
		};
        fActionBack.setEnabled(false);
		
		fActionForward = new LDAuthorActionFactory.ForwardAction() {
			@Override
            public void run() {
                fBrowser.forward();
			}
		};
        fActionForward.setEnabled(false);
		
		fActionStop = new LDAuthorActionFactory.StopAction() {
			@Override
            public void run() {
                fBrowser.stop();
				// cancel any partial progress.
				getViewSite().getActionBars().getStatusLineManager().getProgressMonitor().done();
			}
		};
		
		fActionRefresh = new LDAuthorActionFactory.RefreshAction() {
			@Override
            public void run() {
                fBrowser.refresh();
			}
		};
		
		fActionToggleRestoreLastUrl = new Action(Messages.BrowserView_2, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fRestoreLastUrl = !fRestoreLastUrl;
            }
        };
        fActionToggleRestoreLastUrl.setChecked(fRestoreLastUrl);
        
        // Register the Keybinding for actions
        IHandlerService service = (IHandlerService)getViewSite().getService(IHandlerService.class);
        service.activateHandler(fActionRefresh.getActionDefinitionId(), new ActionHandler(fActionRefresh));

	}
	
	/**
	 * Populate the ToolBar
	 */
	private void makeLocalToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		
		manager.add(fActionBack);
		manager.add(fActionForward);
		manager.add(fActionStop);
		manager.add(fActionRefresh);

		manager.add(new Separator());
		// Add your actions here...
	}
	
	/**
	 * Create the local menu Actions
	 */
	private void makeMenuActions() {
	    IActionBars actionBars = getViewSite().getActionBars();
	    
	    // Local menu items
	    IMenuManager manager = actionBars.getMenuManager();
		manager.add(fActionToggleRestoreLastUrl);
	}
	
	/**
	 * Refresh the Action states
	 */
	private void refreshActions() {
	    fActionForward.setEnabled(fBrowser.isForwardEnabled());
	    fActionBack.setEnabled(fBrowser.isBackEnabled());
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        IProgressMonitor monitor = getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
        monitor.done();
        
        savePreferences();
        super.dispose();
    }
}