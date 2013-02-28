package org.tencompetence.ldauthor.ui.views.ontoconcept;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.views.IiDMethod;


@SuppressWarnings("nls")
public class ontoConcept extends ViewPart implements IiDMethod
{
	private Section section;
	public static String  ID = LDAuthorPlugin.PLUGIN_ID + ".ontoConceptView";
	private ScrolledForm form;
	private String fileSelected;
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	Shell shell = PlatformUI.getWorkbench().
            getActiveWorkbenchWindow().getShell();
	private Composite sectionFooter;
	List<String> results;
	private runOntology run;
	private Table table;
	
	public ontoConcept() {

	}
	
	@Override
	public void createPartControl(Composite parent) {        
		form = toolkit.createScrolledForm(parent);
		form.setFont(new Font(null,"Arial",12,SWT.BOLD)); 
		form.setText("Welcome to OntoConcept"); 
		toolkit.decorateFormHeading(form.getForm());
		
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 15;
		layout.topMargin = 15;
		layout.horizontalSpacing = 10;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);
		
 		section = createSection(Messages.getString("ontoConcept.3"),
 				Messages.getString("ontoConcept.4"),1);
		final Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		createComboOntology(sectionClient);
		section.setClient(sectionClient);
		section = createSection(Messages.getString("ontoConcept.5"),
				Messages.getString("ontoConcept.11"),1);  
		Composite sectionFind = toolkit.createComposite(section);
		toolkit.paintBordersFor(sectionFind);
		sectionFind.setLayout(new GridLayout());
		toolkit.createLabel(sectionFind, Messages.getString("ontoConcept.12"), 
				SWT.SEARCH);
		final Text topic = toolkit.createText(sectionFind, Messages.getString("ontoConcept.13"), 
				SWT.BORDER);
		topic.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setClient(sectionFind);
		final Button buttonSearch = toolkit.createButton(sectionFind,Messages.getString("ontoConcept.6"),SWT.NONE); //$NON-NLS-1$
		section = createSection(Messages.getString("ontoConcept.18"),Messages.getString("ontoConcept.19"),2); //$NON-NLS-1$ //$NON-NLS-2$
		sectionFooter = toolkit.createComposite(section);
		sectionFooter.setLayout(new GridLayout());
		section.setClient(sectionFooter);
		sectionFooter.setVisible(false);
		table =  new Table(sectionFooter,SWT.MULTI | SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION);
		final String[] titles = {"Id", "Classes"};
		for(int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		buttonSearch.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(fileSelected == null || topic.getText().length() == 0) {
					setFormMessage(Messages.getString("ontoConcept.7"),IMessageProvider.WARNING); //$NON-NLS-1$
				}else {
					 setFormMessage(Messages.getString("ontoConcept.9"),IMessageProvider.INFORMATION); //$NON-NLS-1$
					 run = new runOntology("file:///" + fileSelected,topic.getText()); //$NON-NLS-1$
					 results = new ArrayList<String>();
					 results = run.getResultSearch();	
					 sectionFooter.setVisible(true);
					 table.removeAll();
					 for(int i=0; i<results.size(); i++) {
						 TableItem item = new TableItem(table, SWT.NULL);
						 item.setText(0,""+i);
						 item.setText(1,results.get(i));
						 int backgroundColor = (i % 2 == 0 ? 
								 SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW: SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
						 item.setBackground(Display.getDefault().
								 getSystemColor(backgroundColor));
					 }
					 for(int i=0; i<titles.length; i++) {
						 table.getColumn(i).pack();
					 }
					 table.addSelectionListener(new SelectionAdapter() {
						 @Override
						public void widgetSelected(SelectionEvent e) {
							 // TODO Auto-generated method stub
							 int id = 	table.getSelectionIndex();
							 if(id != -1) {
								 boolean result = MessageDialog.openQuestion(shell, "Confirm",
										 "Sure to generate an ECC for "+results.get(id).toString()+"?");
								 if(result) {
									 run.searchDefinitiveOntology(results.get(id).toString());
								 }
								 table.deselectAll();
							}
						 }
					 });
					 form.reflow(true);
				}
				GridData gridData = new GridData();
			    gridData.horizontalSpan = 2;
			    table.setLayoutData(gridData);
			}
		});
	}
	
	
	private void createComboOntology(Composite composite) {
		String[] dataOnto = {Messages.getString("ontoConcept.10"),
				Messages.getString("ontoConcept.17"),"" + 
				Messages.getString("ontoConcept.0"),  
				Messages.getString("ontoConcept.1"), 
				Messages.getString("ontoConcept.2")}; 
		
		final Combo comboOntology = new Combo(composite,SWT.READ_ONLY);
		comboOntology.setItems(dataOnto);
		comboOntology.setLayout(new GridLayout());
		comboOntology.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(comboOntology.getText().equals("<Choose Other Ontology>")) { 
					final FileDialog dialog = new FileDialog(shell);
					dialog.setFilterExtensions(new String[] {"*.owl"}); 
					dialog.setFilterNames(new String[] {"OWL Files"}); 
					fileSelected = dialog.open(); 
					if(fileSelected != null) {
						String[] arrayFileSelected = fileSelected.split("\\\\"); 
						setFormMessage(Messages.getString("ontoConcept.8") +  
								arrayFileSelected[arrayFileSelected.length-1],IMessageProvider.INFORMATION);
						fileSelected = fileSelected.replaceAll("\\\\", "/").replaceAll(" ", "%20"); 
					}
				}
			}
		});
	}
	
	private Section createSection(String title, String description, int colSpan) {
		Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | 
				Section.TWISTIE | Section.EXPANDED);
				
		if(title != null) {
			section.setText(title);
		}
		
		if(description !=  null) {
			section.setDescription(description);
		}
		
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = colSpan;
		section.setLayoutData(td);
		return section;
	}
	
	private void setFormMessage(String message, int type) {
		form.getForm().getMenuManager().update(true);
		form.getForm().addMessageHyperlinkListener(new HyperlinkAdapter());
		form.getForm().setMessage(message,type);
	}

	@Override
	public void setFocus() {
	}

}
