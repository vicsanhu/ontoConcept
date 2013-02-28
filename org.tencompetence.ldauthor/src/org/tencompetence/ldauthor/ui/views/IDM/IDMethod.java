///////////////////////////////////////////////////////////////////////
//                       Terpsicore V1.3                             //
// Conjunto de clases que implementan el analisis de la conformidad  // 
// de un Learning Design en relacion a Teorias de Diseño             // 
// Instruccional. Extiende la funcionalidad del software ReCourse de //
// TenCompetenece (LGPL).                                            //
//                     Copyright (C) 2011                            //
//                                                                   //
// Author: Christian Vidal Castro <cvidal@ubiobio.cl>                //
//                                                                   //
// This program is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU General Public               //
// License as published by the Free Software Foundation; either      //
// version 3 of the License, or (at your option) any later version.  //
//                                                                   //
// This program is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// General Public License for more details.                          //
///////////////////////////////////////////////////////////////////////

package org.tencompetence.ldauthor.ui.views.IDM;

import java.awt.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.views.IiDMethod;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorPage;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorPageFactory;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorTitleComposite;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;


/**
 * Clase implementa una vista de Recourse, integra la GUI de Recourse con Terpsicore
 *  
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class IDMethod extends ViewPart implements IiDMethod{

	public static String ID = LDAuthorPlugin.PLUGIN_ID + ".viewIDM"; //$NON-NLS-1$
    //public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".ldConditionsViewHelp"; //$NON-NLS-1$
    
    public String[] recomenda;
    public String recomendacionesJuntas;
    //private ILDModel fCurrentLDModel;
    public Label textSugerencias;
    public List listSugerencias;
    //private LDEditorContextDelegate fLDEditorContextDelegate;
    
    private InspectorTitleComposite fTitleBar,fTitleBar_RET;
    private InspectorPageFactory fPageMapper = new InspectorPageFactory();
    private String metodoSeleccionado; // metdodo seleccionado por usuario mediante combo
    private String ontologiaSeleccionada; // metdodo seleccionado por usuario mediante combo
    
	
	public IDMethod() {
		// TODO Auto-generated constructor stub
		//System.out.println("constructor IDMethod");
	}

	/** 
	* Crea los controles de la vista principal de Terpsicore
	* @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	*/
	@Override
	public void createPartControl(final Composite parent) {
		
		
		// TODO Auto-generated method stub
		recomendacionesJuntas=" ";
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 4;
    	        
       // Platfom.getInstallLocation(); 
        
        parent.setLayout(layout);
             
        //
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
                factory.adapt(parent);
        factory.paintBordersFor(parent);
        
        
        fTitleBar = new InspectorTitleComposite(parent);
        GridData gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        gd.horizontalSpan=4;
        fTitleBar.setLayoutData(gd);
        
                
        setMainTitle("Checking the Learning Design conformance with a particular representation of a Instructional Design Theory."); //$NON-NLS-1$
        
        // texto Choose teoria
        Text chooseTheo = new Text(parent, SWT.WRAP | SWT.MULTI);
    	chooseTheo.setText("Choose a Theory:");

    	// List de Teorias   
        Font Letra4=new Font("Arial",Font.PLAIN,12);	
    	String[] dataTheo = {"Select a Theory","Elaboration Theory","Multiple Intelligences","Learning by Doing"};
    	final Combo comboTheo = new Combo(parent,SWT.READ_ONLY);
    	comboTheo.setItems(dataTheo);
    	    	
    	//Text text = new Text (parent, SWT.SINGLE | SWT.BORDER);
    	//text.setText ("some text");
    	comboTheo.select (0); // por defecto muestra primer metodo
    
    	
    	// espacio  despues de titulo
        Text espTheo = new Text(parent, SWT.WRAP | SWT.MULTI);
    	GridData gridDataTheo = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    	gridDataTheo.horizontalSpan = 2;
    	espTheo.setLayoutData(gridDataTheo);
    	espTheo.setText("");
    	
    	
    	
    	// ayuda para la teoria de elaboracion
    	fTitleBar_RET = new InspectorTitleComposite(parent);
        //fTitleBar.setTitle(Messages.ConditionsView_0, null);
        GridData gd2 = new GridData(GridData.FILL, SWT.NULL, true, false);
        gd2.horizontalSpan=4;
        fTitleBar_RET.setLayoutData(gd2);
        fTitleBar_RET.setVisible(false);
        fTitleBar_RET.setTitle("Must choose a method of Elaboration theory and a domain ontology that relates to the learning topics of LD.", null);//$NON-NLS-1$
    	
    	
    	// texto Choose method:
        final Text chooseMet = new Text(parent, SWT.WRAP | SWT.MULTI);
    	chooseMet.setText("Choose a Method:");
    	chooseMet.setVisible(false);
    	
        // List de metodos  
        Letra4=new Font("Arial",Font.PLAIN,12);	
    	String[] data2 = {"Conceptual elaboration sequence","Theoretical elaboration sequence","Simplifying conditions sequence"};
    	final Combo comboMetodos = new Combo(parent,SWT.READ_ONLY);
    	comboMetodos.setItems(data2);
    	comboMetodos.setVisible(false);
    	//Text text = new Text (parent, SWT.SINGLE | SWT.BORDER);
    	//text.setText ("some text");
    	comboMetodos.select (0); // por defecto muestra primer metodo
    	comboMetodos.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected(SelectionEvent e) {
    		if (comboMetodos.getText().equals("Conceptual elaboration sequence")) {	
    		System.out.println("Conceptual elaboration sequence");
    		metodoSeleccionado="conceptual";
    		// para elaboracion teorica
    		}
    		if (comboMetodos.getText().equals("Theoretical elaboration sequence")) {	
        	System.out.println("Theoretical elaboration sequence");
        	// se asume que theoretical y conceptual tienen el mismo tratamiento
        	metodoSeleccionado="conceptual";
        	// elaboracion conceptual
    		}
    		if (comboMetodos.getText().equals("Simplifying conditions sequence")) {	
        	System.out.println("Simplifying conditions sequence");
        	metodoSeleccionado="simplifying";
        	// condiciones simplificadas
    		}
    	   	}
    	});
    	comboMetodos.addListener (SWT.DefaultSelection, new Listener () {
    		public void handleEvent (Event e) {
    			metodoSeleccionado="conceptual";
    			System.out.println (e.widget + " - Default Selection");
    			

    		}
    	});
    	
    	// elección de ontologia
    	final Text chooseOntologia = new Text(parent, SWT.WRAP | SWT.MULTI);
    	chooseOntologia.setText("Choose a domain Ontology:");
    	chooseOntologia.setVisible(false);
    	
        // List Ontologias   
        Letra4=new Font("Arial",Font.PLAIN,12);	
    	String[] dataOnto = {"Evironment Ontology","Gene Ontology","Art and Architecture Thesaurus","Programming"};
    	final Combo comboOntologia = new Combo(parent,SWT.READ_ONLY);
    	comboOntologia.setItems(dataOnto);
    	comboOntologia.setVisible(false);
    	//Text text = new Text (parent, SWT.SINGLE | SWT.BORDER);
    	//text.setText ("some text");
    	comboOntologia.select (0); // por defecto muestra primer metodo
    	comboOntologia.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected(SelectionEvent e) {
    		if (comboOntologia.getText().equals("Evironment Ontology")) {	
    		System.out.println("EnvO");
    		ontologiaSeleccionada="EnvO";
    		// para elaboracion teorica
    		}
    		if (comboOntologia.getText().equals("Gene Ontology")) {	
        	System.out.println("GO");
        	// se asume que theoretical y conceptual tienen el mismo tratamiento
        	ontologiaSeleccionada="GO";
        	// elaboracion conceptual
    		}
    		if (comboOntologia.getText().equals("Art and Architecture Thesaurus")) {	
        	System.out.println("AAT");
        	ontologiaSeleccionada="AAT";
        	// condiciones simplificadas
    		}
    		if (comboOntologia.getText().equals("Programming")) {	
            	System.out.println("Programming");
            	ontologiaSeleccionada="Programming";
            	// condiciones simplificadas
        		}    	    		
    	}
    	});
    	comboOntologia.addListener (SWT.DefaultSelection, new Listener () {
    		public void handleEvent (Event e) {
    			ontologiaSeleccionada="EnvO";
    			System.out.println (e.widget + " - Default Selection");
    			

    		}
    	});
    		    	    	
        // boton Validar 
        final Button button1 = new Button(parent, SWT.PUSH);
        button1.setVisible(false);
        button1.setText("Analyze");
        button1.setToolTipText("Run the analysis of LD using the selected Instructional Design Theory");
        GridData gridDataButtonValidation = new GridData();
        gridDataButtonValidation.horizontalAlignment = SWT.BEGINNING;
        gridDataButtonValidation.horizontalSpan=2;
        //gridDataButtonValidation.horizontalSpan=2;
    	//gridData9.grabExcessHorizontalSpace = true;
    	//gridData9.verticalAlignment = SWT.FILL;
    	//gridData9.grabExcessVerticalSpace = true;
    	button1.setLayoutData(gridDataButtonValidation);
        button1.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent e) {
    			
    			String archivoManifest=null;
    			archivoManifest=LDEditorInput.retornaNombreFile();
    			System.out.println("El nombre del archivo manifest capturado:"+ LDEditorInput.retornaNombreFile());
    			if (archivoManifest!=null)
    			{
    				
    				if (metodoSeleccionado==null)
    				{
                    MessageBox messageBoxMetodoSel = new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxMetodoSel.setMessage("Must select a method."); messageBoxMetodoSel.open();					
    				}
    				if (ontologiaSeleccionada==null)
    				{
                    MessageBox messageBoxMetodoSel = new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxMetodoSel.setMessage("Must select a domain Ontology."); messageBoxMetodoSel.open();					
        			}
    				
    				if ((metodoSeleccionado!=null) && (ontologiaSeleccionada!=null))
    				{
    				listSugerencias.removeAll();
    				listSugerencias.add("Processign...");
    				//System.out.println(" Llamaria a validador");
    				//  llama a validador 
    				Validador validation = new Validador(); 
    				//recomenda=validation.valida("c:/imsmanifest.xml",metodoSeleccionado);
    				recomenda=validation.valida(archivoManifest,metodoSeleccionado, ontologiaSeleccionada);
    				/* pasa de String[] a String  */
    				listSugerencias.removeAll();
    				//parent.setRedraw();
    				parent.layout();
    				int x=0;
    				while (recomenda[x]!=null) 
    					{
    					recomendacionesJuntas=recomendacionesJuntas+recomenda[x]+"\n";
    					//textSugerencias.setText(recomendacionesJuntas);
    					listSugerencias.add(recomenda[x]);
    					//listSugerencias.add(recomenda[x]).setToolTipText("prueba hhhhhh  ");
    					x++;
    					}
    				if (recomenda[0]==null)
    					listSugerencias.add("no suggestions");
    				System.out.println("recomendaciones juntas :"+recomendacionesJuntas);
    				parent.layout();
    								
    				
    				
    				metodoSeleccionado=null;
    				ontologiaSeleccionada=null;
    				}
    				
    				}
    			else {
    				
    				MessageBox messageBoxLDOpen =   new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxLDOpen.setMessage("Must open a Learning Design to analyze."); messageBoxLDOpen.open();
    				//System.out.println ("no hay archivo LD abierto");
    			     }
    			}
    	});
        
        // evento de List Toerias
        comboTheo.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		if (comboTheo.getText().equals("Elaboration Theory")) {	
        		
        		// vuelve visible list de metodos y ontologia
        		button1.setVisible(true);
        		fTitleBar_RET.setVisible(true);
        		//explicacionTheo.setVisible(true);
        		chooseMet.setVisible(true);
        		comboOntologia.setVisible(true);
        		chooseOntologia.setVisible(true);
        		comboMetodos.setVisible(true);
            	System.out.println("Elaboration Theory");
        		//combo.setVisible(true);
        		//metodoSeleccionado="conceptual";
        		// para elaboracion teorica
        		}
        		if (comboTheo.getText().equals("Multiple Intelligences")) {	
        		
        		// vuelve NOvisible list de metodos y ontologia
        		button1.setVisible(false);
        		fTitleBar_RET.setVisible(false);
        		//explicacionTheo.setVisible(false);
        		chooseMet.setVisible(false);
            	comboOntologia.setVisible(false);
            	chooseOntologia.setVisible(false);
            	comboMetodos.setVisible(false);	
        		MessageBox messageBoxMetodoSel = new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxMetodoSel.setMessage("Analysis with this theory is not enabled."); messageBoxMetodoSel.open();
        		System.out.println("Multiple Intelligences");
            	// se asume que theoretical y conceptual tienen el mismo tratamiento
            	//metodoSeleccionado="conceptual";
            	// elaboracion conceptual
        		}
        		if (comboTheo.getText().equals("Learning by Doing")) {	
        		button1.setVisible(false);
        		// vuelve NOvisible list de metodos y ontologia
        		fTitleBar_RET.setVisible(false);
        		//explicacionTheo.setVisible(false);
        		chooseMet.setVisible(false);
                comboOntologia.setVisible(false);
                chooseOntologia.setVisible(false);
                comboMetodos.setVisible(false);	
            	MessageBox messageBoxMetodoSel = new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxMetodoSel.setMessage("Analysis with this theory is not enabled."); messageBoxMetodoSel.open();
        		System.out.println("Learning by Doing");
            	//metodoSeleccionado="simplifying";
            	// condiciones simplificadas
        		}
        		if (comboTheo.getText().equals("Select a Theory")) {	
            		// vuelve NOvisible list de metodos y ontologia
            		button1.setVisible(false);
            		fTitleBar_RET.setVisible(false);
            		//explicacionTheo.setVisible(false);
            		chooseMet.setVisible(false);
                	comboOntologia.setVisible(false);
                	chooseOntologia.setVisible(false);
                	comboMetodos.setVisible(false);	
            		//MessageBox messageBoxMetodoSel = new MessageBox(new Shell(),SWT.OK| SWT.ICON_WARNING); messageBoxMetodoSel.setMessage("Analysis with this theory is not enabled."); messageBoxMetodoSel.open();
            		System.out.println("aviso de selccion de teoria");
                	// se asume que theoretical y conceptual tienen el mismo tratamiento
                	//metodoSeleccionado="conceptual";
                	// elaboracion conceptual
            		}
        	    }
        	});
        	comboTheo.addListener (SWT.DefaultSelection, new Listener () {
        		public void handleEvent (Event e) {
        			System.out.println (e.widget + " - Default Selection");
        			

        		}
        	});
        	
        
        
        // espacio  despues de titulo
        Text espTitulos = new Text(parent, SWT.WRAP | SWT.MULTI);
    	GridData gridData10 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    	espTitulos.setLayoutData(gridData10);
    	espTitulos.setText(""); 
        
        fTitleBar = new InspectorTitleComposite(parent);
        //fTitleBar.setTitle(Messages.ConditionsView_0, null);
        GridData gd4 = new GridData(GridData.FILL, SWT.NULL, true, false);
        gd4.horizontalSpan=4;
        fTitleBar.setLayoutData(gd4);
        fTitleBar.setTitle("Results of analysis. Suggestions:", null);//$NON-NLS-1$
        
                
        listSugerencias = new List(parent, SWT.V_SCROLL);
        listSugerencias.setBounds(0, 0,0,0);
        listSugerencias.setToolTipText("Click on a suggestion to know its theoretical foundation");
        GridData gridSugerencias = new GridData();
		gridSugerencias.horizontalSpan = 4;
		gridSugerencias.horizontalAlignment = SWT.FILL;
		gridSugerencias.grabExcessHorizontalSpace = true;
		gridSugerencias.verticalAlignment = SWT.FILL;
		gridSugerencias.grabExcessVerticalSpace = true;
		listSugerencias.setLayoutData(gridSugerencias);

        listSugerencias.addSelectionListener(new SelectionListener() {
    		public void widgetSelected(SelectionEvent e) {
    			
    			SourceTheory ventanaTheory = new SourceTheory();
				String descReigeluth=LeeReglas.arrSourceTheory[(int)listSugerencias.getSelectionIndex()];
				ventanaTheory.generaVentanaTheory(descReigeluth,recomenda[(int)listSugerencias.getSelectionIndex()]);
    			 	}
    		public void widgetDefaultSelected(SelectionEvent e) {
    	        /*
    			*/
    	    	}
         });	
	
	}

	public void setMainTitle(String title) {
        fTitleBar.setTitle(title, null);
    }
    
    /*
	public void setInput(IInspectorProvider client, Object element) {
        AbstractInspectorPage inspectorPage = fPageMapper.getPage(fStackComposite, element);
        if(inspectorPage != null) {
            inspectorPage.setInput(client, element);
            fStackComposite.setControl(inspectorPage);
        }
    }
    */
	
    @Override
    public void setFocus() {
        fTitleBar.setFocus();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fPageMapper != null) {
            fPageMapper.dispose();
        }
    }
}
