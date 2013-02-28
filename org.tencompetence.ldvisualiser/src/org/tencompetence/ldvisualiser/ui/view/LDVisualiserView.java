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
package org.tencompetence.ldvisualiser.ui.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldvisualiser.LDVisualiserPlugin;
import org.tencompetence.ldvisualiser.preferences.ILDVisualiserPreferenceConstants;
import org.tencompetence.ldvisualiser.ui.IImages;


public class LDVisualiserView extends ViewPart
implements IContextProvider, IZoomableWorkbenchPart, PropertyChangeListener, IInspectorProvider {

    public static String ID = LDVisualiserPlugin.PLUGIN_ID + ".ldVisualiserView"; //$NON-NLS-1$
    public static String HELP_ID = LDVisualiserPlugin.PLUGIN_ID + ".ldVisualiserViewHelp"; //$NON-NLS-1$
    
    private HashMap<ILDModel, LDState> fLDStates = new HashMap<ILDModel, LDState>();
    private LDState fCurrentLDState;
    
    private LDEditorContextDelegate fLDEditorContextDelegate;
    
    private ExplanationComposite fExplanationComposite;
    
    private boolean fShowExplanation;
    
    private LDGraphViewer fGraphViewer;
    
    private Composite fStupidHackComposite;
    
    private SashForm fSashForm;
    
    // Actions
    private Action fActionBack;
    private Action fActionForward;
    private Action fActionLayout;
    private Action fActionShowInspector;
    private Action fActionPinSelection;
    private Action fActionUnpinSelection;
    
    private Action fActionHorizontalTreeLayout;
    private Action fActionVerticalTreeLayout;
    
    private Action fActionShowExplanation;
    
    private ZoomContributionViewItem fContextZoomContributionViewItem;
    private ZoomContributionViewItem fToolbarZoomContributionViewItem;

    private LayoutAlgorithm fHorizontalTreeLayoutAlgorithm;
    private LayoutAlgorithm fVerticalTreeLayoutAlgorithm;
    //private LayoutAlgorithm fCompositeLayoutAlgorithm;

    private LDGraphLabelProvider fLabelProvider;

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);

        fSashForm = new SashForm(parent, SWT.VERTICAL);
        fSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        AppFormToolkit.getInstance().adapt(fSashForm);
        
        fExplanationComposite = new ExplanationComposite(fSashForm, SWT.NONE);
        
        // Because GraphViewer assigns a Graph child in its constructor that we can't get rid of
        fStupidHackComposite = new Composite(fSashForm, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 0;
        layout2.verticalSpacing = 0;
        layout2.horizontalSpacing = 0;
        fStupidHackComposite.setLayout(layout2);
        
        fGraphViewer = new LDGraphViewer(fStupidHackComposite, SWT.NONE);
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        fGraphViewer.getGraphControl().setLayoutData(gd);
        
        fSashForm.setWeights(new int[] { 1 , 5 });
        
        // Set up Graph Viewer
        
        fHorizontalTreeLayoutAlgorithm = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
        fVerticalTreeLayoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
        
        // Comparators
        //fHorizontalTreeLayoutAlgorithm.setComparator(new LDComparator());
        //fVerticalTreeLayoutAlgorithm.setComparator(new LDComparator());
        
        fGraphViewer.setContentProvider(new LDGraphContentProvider());
        
        fLabelProvider = new LDGraphLabelProvider(fGraphViewer);
        fGraphViewer.setLabelProvider(fLabelProvider);
        
        fGraphViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object selectedElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
                handleSelectionChanged(selectedElement);
            }
        });
        
        fGraphViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                Object selectedElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
                focusOn(selectedElement, true);
                // When a new object is drilled into, disable the forward action
                // The forward action only stores history when the back button was used
                fCurrentLDState.getForwardStack().clear();
                fActionForward.setEnabled(false);
            }
        });
        
        // Make Actions
        makeActions();
        
        // Set up ToolBar
        makeLocalToolBar();
        
        // Set up local menu actions
        makeMenuActions();
        
        // Right-click menu
        hookContextMenu();
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(getSite().getWorkbenchWindow()) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if(fCurrentLDState != null) {
                    fCurrentLDState.getLDModel().removePropertyChangeListener(LDVisualiserView.this);
                }
                
                if(editor != null) {
                    ILDModel ldModel = (ILDModel)editor.getAdapter(ILDModel.class);
                    ldModel.addPropertyChangeListener(LDVisualiserView.this);
                    
                    setPartName(Messages.LDVisualiserView_0 + " - " + editor.getTitle()); //$NON-NLS-1$
                    
                    // Store previous state
                    if(fCurrentLDState != null) {
                        fCurrentLDState.storeCurrentState();
                    }
                    
                    // Update model and view
                    fCurrentLDState = fLDStates.get(ldModel);
                    if(fCurrentLDState == null) {
                        fCurrentLDState = new LDState(ldModel, fGraphViewer);
                        fLDStates.put(ldModel, fCurrentLDState);
                    }
                    
                    focusOn(fCurrentLDState.getCurrentObject(), false);
                    
                    fActionBack.setEnabled(!fCurrentLDState.getBackStack().isEmpty());
                    fActionForward.setEnabled(!fCurrentLDState.getForwardStack().isEmpty());
                }
                else {
                    fCurrentLDState = null;
                    setPartName(Messages.LDVisualiserView_0);
                    
                    // Clear model and view
                    fGraphViewer.setInput(null);
                    
                    fActionBack.setEnabled(false);
                    fActionForward.setEnabled(false);
                }
            }
        };
        
        // Preference of layout type
        IPreferenceStore store = LDVisualiserPlugin.getDefault().getPreferenceStore();
        String layoutType = store.getString(ILDVisualiserPreferenceConstants.PREFS_LDVISUALISER_LAYOUT_ALGORITHM);
        
        if(ILDVisualiserPreferenceConstants.LAYOUT_ALGORITHM_HORIZONTAL_TREE.equals(layoutType)) {
            fGraphViewer.setLayoutAlgorithm(fHorizontalTreeLayoutAlgorithm);
            fActionHorizontalTreeLayout.setChecked(true);
        }
        else {
            fGraphViewer.setLayoutAlgorithm(fVerticalTreeLayoutAlgorithm);
            fActionVerticalTreeLayout.setChecked(true);
        }
        
//        LayoutAlgorithm[] algos = {
//                new SpringLayoutAlgorithm(LayoutStyles.NONE),
//                new TreeLayoutAlgorithm(LayoutStyles.NONE),
//        };
//        fCompositeLayoutAlgorithm = new CompositeLayoutAlgorithm(LayoutStyles.NONE, algos);

//        fGraphViewer.setLayoutAlgorithm(fCompositeLayoutAlgorithm);
        
        // Hide/Show Explanation
        fShowExplanation = store.getBoolean(ILDVisualiserPreferenceConstants.PREFS_SHOW_EXPLANATION);
        if(fShowExplanation) {
            fSashForm.setMaximizedControl(null);
        }
        else {
            fSashForm.setMaximizedControl(fStupidHackComposite);
        }
        fActionShowExplanation.setChecked(fShowExplanation);
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // If there is already an active page then process it
        fLDEditorContextDelegate.checkEditorOpen();
    }
    
    /**
     * Focus on a selected object by drilling into it
     * @param selectedElement
     * @param recordHistory
     */
    private void focusOn(Object selectedElement, boolean recordHistory) {
        if(fCurrentLDState == null || selectedElement == null || selectedElement instanceof EntityConnectionData) {
            return;
        }
        
        // If it's a leaf node, show the Inspector
        if(ObjectAnalysis.isLeaf(fCurrentLDState.getCurrentObject(), selectedElement)) {
            InspectorManager.getInstance().showInspector();
            InspectorManager.getInstance().setInput(LDVisualiserView.this, selectedElement);
            return;
        }
        
        // Store current positions first
        if(fCurrentLDState.getCurrentObject() != selectedElement) {
            fCurrentLDState.storeCurrentState();
        }
        
        // Display graph
        fGraphViewer.setInput(selectedElement);
        
        Object currentObject = fCurrentLDState.getCurrentObject();
        
        if(recordHistory) {
            fCurrentLDState.getBackStack().push(currentObject);
            fActionBack.setEnabled(true);
        }
        
        // Update this
        fCurrentLDState.setCurrentObject(selectedElement);

        // And restore previous positions if any
        fCurrentLDState.restoreLastState();
        
        // Explanation box
        fExplanationComposite.setContext(selectedElement);
    }

    /**
     * Handle the select changed. This will update the view whenever a selection
     * occurs.
     * 
     * @param selectedItem
     */
    private void handleSelectionChanged(Object selectedItem) {
        if(selectedItem instanceof ILDModelObject) {
            InspectorManager.getInstance().setInput(LDVisualiserView.this, selectedItem);
        }
        
        // Highlight the selection's dependents
        fLabelProvider.setCurrentSelection(fCurrentLDState.getCurrentObject(), selectedItem);
    }


    @Override
    public void setFocus() {
        if(fGraphViewer != null) {
            fGraphViewer.getControl().setFocus();
        }
    }
    
    /**
     * Make the Actions
     */
    private void makeActions() {
        fActionBack = new Action(Messages.LDVisualiserView_3) {
            @Override
            public void run() {
                if(fCurrentLDState == null) {
                    return;
                }
                if(fCurrentLDState.getBackStack().size() > 0) {
                    Object o = fCurrentLDState.getBackStack().pop();
                    fCurrentLDState.getForwardStack().push(fCurrentLDState.getCurrentObject());
                    fActionForward.setEnabled(true);
                    focusOn(o, false);
                    if(fCurrentLDState.getBackStack().size() <= 0) {
                        setEnabled(false);
                    }
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(LDVisualiserPlugin.PLUGIN_ID, IImages.ICON_BACKWARD);
            }
        };
        fActionBack.setEnabled(false);
        
        fActionForward = new Action(Messages.LDVisualiserView_4) {
            @Override
            public void run() {
                if(fCurrentLDState == null) {
                    return;
                }
                if(fCurrentLDState.getForwardStack().size() > 0) {
                    Object o = fCurrentLDState.getForwardStack().pop();
                    focusOn(o, true);
                    if(fCurrentLDState.getForwardStack().size() <= 0) {
                        setEnabled(false);
                    }
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(LDVisualiserPlugin.PLUGIN_ID, IImages.ICON_FORWARD);
            }
        };
        fActionForward.setEnabled(false);
        
        fActionLayout = new Action(Messages.LDVisualiserView_5) {
            @Override
            public void run() {
                fGraphViewer.doApplyLayout();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(LDVisualiserPlugin.PLUGIN_ID,
                        IImages.ICON_LAYOUT);
            }
        };
        
        fActionHorizontalTreeLayout = new Action(Messages.LDVisualiserView_6, IAction.AS_RADIO_BUTTON) {
            @Override
            public void run() {
                if(fGraphViewer.getGraphControl().getLayoutAlgorithm() != fHorizontalTreeLayoutAlgorithm) {
                    fGraphViewer.setLayoutAlgorithm(fHorizontalTreeLayoutAlgorithm, true);
                    fGraphViewer.doApplyLayout();
                    IPreferenceStore store = LDVisualiserPlugin.getDefault().getPreferenceStore();
                    store.setValue(ILDVisualiserPreferenceConstants.PREFS_LDVISUALISER_LAYOUT_ALGORITHM,
                            ILDVisualiserPreferenceConstants.LAYOUT_ALGORITHM_HORIZONTAL_TREE);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionVerticalTreeLayout = new Action(Messages.LDVisualiserView_7, IAction.AS_RADIO_BUTTON) {
            @Override
            public void run() {
                if(fGraphViewer.getGraphControl().getLayoutAlgorithm() != fVerticalTreeLayoutAlgorithm) {
                    fGraphViewer.setLayoutAlgorithm(fVerticalTreeLayoutAlgorithm, true);
                    fGraphViewer.doApplyLayout();
                    IPreferenceStore store = LDVisualiserPlugin.getDefault().getPreferenceStore();
                    store.setValue(ILDVisualiserPreferenceConstants.PREFS_LDVISUALISER_LAYOUT_ALGORITHM,
                            ILDVisualiserPreferenceConstants.LAYOUT_ALGORITHM_VERTICAL_TREE);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionShowInspector = new Action(Messages.LDVisualiserView_8) {
            @Override
            public void run() {
                Object selectedElement = ((IStructuredSelection)fGraphViewer.getSelection()).getFirstElement();
                if(selectedElement instanceof ILDModelObject) {
                    InspectorManager.getInstance().showInspector();
                    InspectorManager.getInstance().setInput(LDVisualiserView.this, selectedElement);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionPinSelection = new Action(Messages.LDVisualiserView_9) {
            @Override
            public void run() {
                Object selectedElement = ((IStructuredSelection)fGraphViewer.getSelection()).getFirstElement();
                if(selectedElement instanceof ILDModelObject) {
                    fLabelProvider.setPinnedNode(fCurrentLDState.getCurrentObject(), selectedElement);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionUnpinSelection = new Action(Messages.LDVisualiserView_10) {
            @Override
            public void run() {
                fLabelProvider.setPinnedNode(null, null);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionShowExplanation = new Action("Explanation window", IAction.AS_CHECK_BOX) { //$NON-NLS-1$
            @Override
            public void run() {
                fShowExplanation = !fShowExplanation;

                if(fShowExplanation) {
                    fSashForm.setMaximizedControl(null);
                }
                else {
                    fSashForm.setMaximizedControl(fStupidHackComposite);
                }
                
                IPreferenceStore store = LDVisualiserPlugin.getDefault().getPreferenceStore();
                store.setValue(ILDVisualiserPreferenceConstants.PREFS_SHOW_EXPLANATION, fShowExplanation);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(LDVisualiserPlugin.PLUGIN_ID, IImages.ICON_FOCUS);
            }
        };
        
        fToolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
        fContextZoomContributionViewItem = new ZoomContributionViewItem(this);
    }
    
    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBar() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();

        manager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
        manager.add(fActionLayout);
        manager.add(new Separator());
        manager.add(fActionBack);
        manager.add(fActionForward);
        manager.add(new Separator());
        manager.add(fActionShowExplanation);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Create the local menu Actions
     */
    private void makeMenuActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Local menu items
        IMenuManager manager = actionBars.getMenuManager();
        
        MenuManager prefs = new MenuManager(Messages.LDVisualiserView_11);
        manager.add(prefs);
        prefs.add(fActionShowExplanation);
        
        MenuManager layoutTypeMenu = new MenuManager(Messages.LDVisualiserView_12);
        manager.add(layoutTypeMenu);
        
        layoutTypeMenu.add(fActionHorizontalTreeLayout);
        layoutTypeMenu.add(fActionVerticalTreeLayout);
        
        manager.add(new Separator());
        manager.add(fToolbarZoomContributionViewItem);
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#LDPropertiesViewPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(fGraphViewer.getControl());
        fGraphViewer.getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuMgr, fGraphViewer);
    }

    /**
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        if(fCurrentLDState == null) {
            return;
        }
        
        Object selectedElement = ((IStructuredSelection)fGraphViewer.getSelection()).getFirstElement();
        
        manager.add(fActionLayout);
        manager.add(new Separator());
        if(selectedElement instanceof ILDModelObject) {
            manager.add(fActionShowInspector);
            manager.add(new Separator());
            if(ObjectAnalysis.hasChildNodes(fCurrentLDState.getCurrentObject(), selectedElement)) {
                manager.add(fActionPinSelection);
            }
        }
        if(fLabelProvider.getPinnedNode() != null) {
            manager.add(fActionUnpinSelection);
        }
        manager.add(new Separator());

        manager.add(fActionBack);
        manager.add(fActionForward);
        manager.add(new Separator());
        manager.add(fContextZoomContributionViewItem);
    }

    public AbstractZoomableViewer getZoomableViewer() {
        return fGraphViewer;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(propertyName.startsWith(ILDModelObjectContainer.PROPERTY_CHILD)) {
            fGraphViewer.refresh();
            fExplanationComposite.refresh();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the focussed editor part
         */
        if(adapter == ILDEditorPart.class) {
            if(fLDEditorContextDelegate.getActiveEditor() != null) {
                return fLDEditorContextDelegate.getActiveEditor().getAdapter(ILDEditorPart.class);
            }
        }
        
        /*
         * Return the focussed multi-page editor if any
         */
        if(adapter == ILDMultiPageEditor.class) {
            return fLDEditorContextDelegate.getActiveEditor();
        }

        return super.getAdapter(adapter);
    }

    
    /**
     * Dispose of all objects
     */
    @Override
    public void dispose() {
        super.dispose();
        
        if(fCurrentLDState != null) {
            fCurrentLDState.getLDModel().removePropertyChangeListener(this);
        }
        
        for(LDState ldState : fLDStates.values()) {
            ldState.dispose();
        }
        
        fLDStates.clear();
        
        fLDEditorContextDelegate.dispose();
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
        return Messages.LDVisualiserView_0;
    }
}
