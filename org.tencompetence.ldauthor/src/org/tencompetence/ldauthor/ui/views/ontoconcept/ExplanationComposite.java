package org.tencompetence.ldauthor.ui.views.ontoconcept;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;

@SuppressWarnings("nls")
public class ExplanationComposite extends ScrolledComposite{
	
	private Composite fClient;
	private Label fMainLabel;
	private Text fSearch;
	private Button fButton;
	private Combo comboAlgorithms;
	
	private String[] algorithms = {"GridLayoutAlgorithm",
			"HorizontalShift",
			"DirectedGraphLayoutAlgorithm",  
			"HorizontalLayoutAlgorithm", 
			"HorizontalTreeLayoutAlgorithm",
			"RadialLayoutAlgorithm",
			"SpringLayoutAlgorithm",
			"TreeLayoutAlgorithm",
			"VerticalLayoutAlgorithm"};

	public ExplanationComposite(Composite parent, int style) {
		super(parent, style);
		setExpandHorizontal(true);
		
		fClient = new Composite(this, SWT.NULL);
        GridLayout layout = new GridLayout();
        fClient.setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        setLayoutData(gd);
        
        setContent(fClient);
        AppFormToolkit.getInstance().adapt(this);
        AppFormToolkit.getInstance().adapt(fClient);
        
        AppFormToolkit.getInstance().createScrolledForm(this);
        fClient.setLayout(new GridLayout(3, false));
        
        fMainLabel = AppFormToolkit.getInstance().createHeaderLabel(fClient, "Filter:",
        		SWT.NONE);
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        fMainLabel.setLayoutData(gd);
        
        fSearch = AppFormToolkit.getInstance().createText(fClient, "", SWT.BORDER);
        gd  = new GridData(184,16);
        fSearch.setLayoutData(gd);
        
        fButton = AppFormToolkit.getInstance().createButton(fClient, "Search", SWT.PUSH);
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        fButton.setLayoutData(gd);
        
        fMainLabel = AppFormToolkit.getInstance().createHeaderLabel(fClient, "Algorithm:",
        		SWT.NONE);
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        fMainLabel.setLayoutData(gd);
        
        comboAlgorithms = new Combo(fClient,SWT.READ_ONLY);
        comboAlgorithms.setItems(algorithms);
        
        comboAlgorithms.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(SelectionEvent e) {
        		if (comboAlgorithms.getText().
        				equals("HorizontalShift")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("TreeLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("HorizontalTreeLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("RadialLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("GridLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("SpringLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("DirectedGraphLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("HorizontalLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new HorizontalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        		if (comboAlgorithms.getText().
        				equals("VerticalLayoutAlgorithm")) {
        			ontoZest.getViewer().setLayoutAlgorithm(
        					new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        		}
        	}
        	@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
        });
        
        fButton.addSelectionListener(new SelectionAdapter(){
        	@Override
			public void widgetSelected(SelectionEvent e) {
        		ViewerFilter[] filters;
        		filters = !fSearch.getText().isEmpty() ? new ViewerFilter[]{
					new GenealogyZestFilter(fSearch.getText().toLowerCase())
				} : new ViewerFilter[]{};
        		ontoZest.getViewer().setFilters(filters);
        		ontoZest.getViewer().applyLayout();
        	}
        	@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
        });
        
        fClient.pack();
        fClient.layout();
	}

}
