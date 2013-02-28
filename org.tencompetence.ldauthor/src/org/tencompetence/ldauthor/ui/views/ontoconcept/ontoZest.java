package org.tencompetence.ldauthor.ui.views.ontoconcept;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.internal.ZoomManager;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.views.IiDMethod;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.GenealogyGraph;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.io.GenealogyGraphReader;

@SuppressWarnings("nls")
public class ontoZest extends ViewPart implements IiDMethod
{
	public static String  ID = LDAuthorPlugin.PLUGIN_ID + ".ontoZestView";
	private static GraphViewer viewer;
	private SashForm fSashForm;
	private Composite fStupidHackComposite;
	private IAction actionIn;
	private IAction actionOut;
	private IAction actionPhoto;

	private Control createDiagram(Composite parent) {
		viewer = new GraphViewer(parent, SWT.BORDER);
		viewer.setContentProvider(new GenealogyZestContentProvider());
		Color blue = viewer.getGraphControl().LIGHT_BLUE;
		viewer.setLabelProvider(new OntoZestLabelProvider(blue));
		int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		viewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(style,
			new LayoutAlgorithm[]{
				new DirectedGraphLayoutAlgorithm(style),
				new HorizontalShift(style),
				new MarriageLayoutAlgorithm(style),
				new ShiftDiagramLayoutAlgorithm(style)
			}));
		new CustomFigureHighlightAdapter(viewer);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		viewer.getGraphControl().setLayoutData(gd);
		return viewer.getControl();
	}
	
	private void setModel(GenealogyGraph newGraph) {
		viewer.setInput(newGraph);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fSashForm = new SashForm(parent, SWT.VERTICAL);
        fSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
        AppFormToolkit.getInstance().adapt(fSashForm);
        
        new ExplanationComposite(fSashForm, SWT.NONE);
        
        fStupidHackComposite = new Composite(fSashForm, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 0;
        layout2.verticalSpacing = 0;
        layout2.horizontalSpacing = 0;
        fStupidHackComposite.setLayout(layout2);
        createDiagram(fStupidHackComposite);       
        fSashForm.setWeights(new int[] { 1 , 5 });
		readAndClose(getClass().getResourceAsStream("./genealogy.xml"));
		
		createAction();
		createToolbar();
	}
	
	private void readAndClose(InputStream stream) {
		GenealogyGraph newGraph = new GenealogyGraph();
		try {
			new GenealogyGraphReader(newGraph).read(stream);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		setModel(newGraph);
	}
	
	public static GraphViewer getViewer() {
		return viewer;
	}
	
	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(this.actionIn);
		mgr.add(this.actionOut);
		mgr.add(this.actionPhoto);
	}
	
	private void createAction() {
		actionIn = new Action() {
			@Override
			public void run() {
				ZoomManager zoomManager = new ZoomManager(
						viewer.getGraphControl().getRootLayer(), 
					    viewer.getGraphControl().getViewport() );
				zoomManager.zoomIn();
			}
		};
		actionIn.setText("ActionIn");
		Bundle bundleIn = FrameworkUtil.getBundle(this.getClass());
		URL urlIn = FileLocator.find(bundleIn, new Path("icons/Zoom-In-icon.png"), null);
		ImageDescriptor imageIn = ImageDescriptor.createFromURL(urlIn);
		actionIn.setImageDescriptor(imageIn);
		
		actionOut = new Action() {
			@Override
			public void run() {
				ZoomManager zoomManager = new ZoomManager(
					    viewer.getGraphControl().getRootLayer(), 
					    viewer.getGraphControl().getViewport() );
				zoomManager.zoomOut();
			}
		};
		actionOut.setText("ActionOut");
		Bundle bundleOut = FrameworkUtil.getBundle(this.getClass());
		URL urlOut = FileLocator.find(bundleOut, new Path("icons/Zoom-Out-icon.png"), null);
		ImageDescriptor imageOut = ImageDescriptor.createFromURL(urlOut);
		actionOut.setImageDescriptor(imageOut);
		
		actionPhoto = new Action() {
			@Override
			public void run() {
				GC gc = new GC(fStupidHackComposite);
				Image image = new Image(fStupidHackComposite.getDisplay(),
						fStupidHackComposite.getBounds().width,
						fStupidHackComposite.getBounds().height);
				try {
				    gc.copyArea(image, 0, 0);
				    ImageLoader imageLoader = new ImageLoader();
				    imageLoader.data = new ImageData[] { image.getImageData() };
				    imageLoader.save("c:\\ECC-PNG.png", SWT.IMAGE_PNG);
				} finally {
				    image.dispose();
				    gc.dispose();
				}
			}
		};
		actionPhoto.setText("ActionPhoto");
		Bundle bundlePhoto = FrameworkUtil.getBundle(this.getClass());
		URL urlPhoto = FileLocator.find(bundlePhoto, new Path("icons/camera-icon.png"), null);
		ImageDescriptor imagePhoto = ImageDescriptor.createFromURL(urlPhoto);
		actionPhoto.setImageDescriptor(imagePhoto);
	}
}
