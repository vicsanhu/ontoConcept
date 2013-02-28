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
package org.tencompetence.ldauthor.ui.wizards.ld;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.tencompetence.ldauthor.templates.ILDTemplate;
import org.tencompetence.ldauthor.templates.ITemplate;
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.templates.CategoriesTableViewer;


/**
 * New LD Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewLDWizardPage.java,v 1.8 2010/01/06 15:34:25 phillipus Exp $
 */
public class NewLDWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewLDWizardPage"; //$NON-NLS-1$
    
    /**
     * The Gallery widget
     */
    private Gallery fGallery;
    
    private GalleryItem fGalleryRoot;
    
    private StyledText fDescriptionText;
    
    private LDTemplateManager fTemplateManager;

    /**
	 * Constructor
     * @param templateManager 
	 */
	public NewLDWizardPage(LDTemplateManager templateManager) {
		super(PAGE_NAME);
		setTitle(Messages.NewLDWizardPage_0);
		setDescription(Messages.NewLDWizardPage_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        
        fTemplateManager = templateManager;
	}

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        
        GridData gd;
        
        SashForm sash1 = new SashForm(container, SWT.HORIZONTAL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 800;
        gd.heightHint = 500;
        sash1.setLayoutData(gd);
        
        Composite categoryComposite = new Composite(sash1, SWT.BORDER);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        categoryComposite.setLayout(layout);
        CLabel label = new CLabel(categoryComposite, SWT.NULL);
        label.setText(Messages.NewLDWizardPage_10);
        label.setImage(ImageFactory.getImage(ImageFactory.IMAGE_APP_32));
        
        Composite tableComp = new Composite(categoryComposite, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        tableComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        CategoriesTableViewer tableViewer = new CategoriesTableViewer(tableComp, SWT.NULL);
        tableViewer.setInput(fTemplateManager);
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                ITemplateGroup group = (ITemplateGroup)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(group != null) {
                    clearGallery();
                    updateGallery(group);
                }
            }
        });
        
        SashForm sash2 = new SashForm(sash1, SWT.VERTICAL);
        
        fGallery = new Gallery(sash2, SWT.V_SCROLL | SWT.BORDER);
        
        // Renderers
        NoGroupRenderer gr = new NoGroupRenderer();
        gr.setMinMargin(24);
        //gr.setItemSize(64, 64);
        gr.setItemWidth(132);
        gr.setAutoMargin(false);
        fGallery.setGroupRenderer(gr);
        
        //fGallery.setItemRenderer(new DefaultGalleryItemRenderer());
        
        DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer() {
            @Override
            protected Point getBestSize(int originalX, int originalY, int maxX, int maxY) {
                /*
                 * Ensure images are not stretched
                 */
                return new Point(originalX, originalY);
            }
        };
        fGallery.setItemRenderer(ir);

        // Root Group
        fGalleryRoot = new GalleryItem(fGallery, SWT.NONE);
        
        // Description
        fDescriptionText = new StyledText(sash2, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP | SWT.BORDER);
        
        fGallery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ILDTemplate template = (ILDTemplate)((GalleryItem)e.item).getData();
                updateWizardPages(template);
             }
        });
        
        // Double-clicks
        fGallery.addListener(SWT.MouseDoubleClick, new Listener() {
            public void handleEvent(Event event) {
                GalleryItem item = fGallery.getItem(new Point(event.x, event.y));
                if(item != null) {
                    getWizard().getContainer().showPage(getNextPage());
                }
            }
        });
        
        tableViewer.setSelection(new StructuredSelection(fTemplateManager.getTemplateGroups().get(0)));
        fGallery.setSelection(new GalleryItem[] { fGalleryRoot.getItem(0) });
        
        sash1.setWeights(new int[] { 30, 70 });
        sash2.setWeights(new int[] { 50, 50 });
        
	    setPageComplete(true); // Yes it's OK
        setControl(container);
	}
    
    public ILDTemplate getSelectedTemplate() {
        if(fGallery.getSelection().length == 0) {
            return null;
        }
        return (ILDTemplate)fGallery.getSelection()[0].getData();
    }
    
    private void updateWizardPages(ILDTemplate template) {
        if(template == null) {
            fDescriptionText.setText(""); //$NON-NLS-1$
            setPageComplete(false);
        }
        else {
            // Update description
            String text = template.getDescription();
            String desc = template.getName() + ":"; //$NON-NLS-1$
            fDescriptionText.setText(desc + "   " + text); //$NON-NLS-1$
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = desc.length();
            style.fontStyle = SWT.BOLD;
            fDescriptionText.setStyleRange(style);
            
            // Tell other pages of our selection
            NewLDWizardPageLocation page1 = (NewLDWizardPageLocation)getWizard().getPage(NewLDWizardPageLocation.PAGE_NAME);
            page1.setTemplate(template);
            NewLDWizardPageOverview page2 = (NewLDWizardPageOverview)getWizard().getPage(NewLDWizardPageOverview.PAGE_NAME);
            page2.setTemplate(template);

            setPageComplete(true);
        }
     }
	
	@Override
	public boolean canFlipToNextPage() {
	    return fGallery.getSelection().length > 0;
	}
	
	/**
     * Clear old root group
     */
    private void clearGallery() {
        if(fGalleryRoot != null && !fGallery.isDisposed() && fGallery.getItemCount() > 0) {
            while(fGalleryRoot.getItemCount() > 0) {
                GalleryItem item = fGalleryRoot.getItem(0);
                fGalleryRoot.remove(item);
            }
        }
    }
    
    private void updateGallery(ITemplateGroup group) {
        for(ITemplate template : group.getTemplates()) {
            GalleryItem thumb = new GalleryItem(fGalleryRoot, SWT.NONE);
            thumb.setText(template.getName());
            thumb.setImage(template.getImage());
            thumb.setData(template);
        }
        
        if(fGalleryRoot.getItem(0) != null) {
            fGallery.setSelection(new GalleryItem[] { fGalleryRoot.getItem(0) });
            updateWizardPages((ILDTemplate)fGalleryRoot.getItem(0).getData());
        }
        else {
            updateWizardPages(null);
        }
    }
	

}
