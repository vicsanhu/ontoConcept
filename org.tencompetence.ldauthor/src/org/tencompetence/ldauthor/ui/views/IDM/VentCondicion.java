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

import javax.swing.*;					
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorTitleComposite;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import java.util.Collection;
import java.util.Iterator;

/**
 * Clase que realiza la vinculacion entre Learning Activity e ItemCondition - Implementa GUI
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class VentCondicion extends JFrame {

public static File fileManifest; // borrar
public static String nombreManifest; // borrar
public static String actividadSeleccionada, condicionSeleccionada; // indices en las listas
public List listParejas;
public  String uri_idtheory;
public  String uri_ld;
public  String uri_uol;
public  String uri_lom;
public  String uri_actual;
private InspectorTitleComposite fTitleBar;

    public VentCondicion() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Genera ventana que permite vincular Actividades de Aprendizaje con Condiciones
     * 
     * @param instancesActivity
     * @param instancesCondicion 
     * @param jenaModel 
     */
    public void generaVentanaCondicion(final Collection instancesActivity, final Collection instancesCondicion, final JenaOWLModel jenaModel)
	{
	asignaUri();	
		
	Display display =PlatformUI.createDisplay();
	//Display display = new Display();
	final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
	shell.addListener(SWT.Close, new Listener() {
	      public void handleEvent(Event event) {
	        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
	        MessageBox messageBox = new MessageBox(shell, style);
	        messageBox.setText("Information");
	        messageBox.setMessage("Close the window?");
	        event.doit = messageBox.open() == SWT.YES;
	      }
	    });
	
	shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	shell.setSize(300, 200);
	shell.setText("Linking Learning Activities and Conditions");
	GridLayout gridLayout = new GridLayout();
	gridLayout.verticalSpacing = 8;
	gridLayout.horizontalSpacing = 3;
	gridLayout.numColumns = 2;
	shell.setLayout(gridLayout);
		
	fTitleBar = new InspectorTitleComposite(shell);
    //fTitleBar.setTitle(Messages.ConditionsView_0, null);
    GridData gd4 = new GridData(GridData.FILL, SWT.NULL, true, false);
    gd4.horizontalSpan=2;
    fTitleBar.setLayoutData(gd4);
    fTitleBar.setTitle("The Elaboration Theory requires Learning Activities to be linked with Conditions that simplify the learning of tasks.\nSelect a Learning Activity and a Condition, then next press Link button.\nYou can select the pairs of Activities and Contitions that are required.", null);//$NON-NLS-1$
	 
    // espacio  despues de titulo
    Text espTitulos = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData10 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	gridData10.horizontalSpan = 2;
	gridData10.horizontalAlignment = SWT.FILL;
	gridData10.grabExcessHorizontalSpace = true;
	gridData10.verticalAlignment = SWT.FILL;
	gridData10.grabExcessVerticalSpace = true;
	espTitulos.setLayoutData(gridData10);
	espTitulos.setText(""); 
		
	// 2. titulo Actividades  
	Text tituloActivity = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData2 = new GridData();
	gridData2.horizontalAlignment = SWT.FILL;
	gridData2.grabExcessHorizontalSpace = true;
	gridData2.verticalAlignment = SWT.FILL;
	gridData2.grabExcessVerticalSpace = true;
	tituloActivity.setLayoutData(gridData2);
	tituloActivity.setText("Learning Activities");
	
	// 2. titulo Conceptos  
	Text tituloCondicion = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData6 = new GridData();
	gridData6.horizontalAlignment = SWT.FILL;
	gridData6.grabExcessHorizontalSpace = true;
	gridData6.verticalAlignment = SWT.FILL;
	gridData6.grabExcessVerticalSpace = true;
	tituloCondicion.setLayoutData(gridData6);
	tituloCondicion.setText("Conditions");
	
	/* 3. Lista de Actividades */
	final List listActividades = new List(shell, SWT.V_SCROLL | SWT.BORDER);
	GridData gridData7 = new GridData();
	gridData7.horizontalAlignment = SWT.FILL;
	gridData7.grabExcessHorizontalSpace = true;
	gridData7.verticalAlignment = SWT.FILL;
	gridData7.grabExcessVerticalSpace = true;
	listActividades.setLayoutData(gridData7);
	
	listActividades.setBounds(0, 0,0,0);
    	
	Iterator it=instancesActivity.iterator();
	while (it.hasNext())
			{
			OWLIndividual actividadIN= (OWLIndividual)it.next();
			RDFProperty titleActiviProp = jenaModel.getRDFProperty(uri_actual+"title");
			listActividades.add((String) actividadIN.getPropertyValue(titleActiviProp));
			}
	 
	listActividades.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//System.err.println(listActividades.getSelectionIndex());
		    	int[] indices = listActividades.getSelectionIndices();
		    	String[] items = listActividades.getSelection();
		    	StringBuffer buffer = new StringBuffer("");
		    	for(int i=0; i < indices.length; i++) {
		    	  buffer.append(items[i]);
		    	  }
		    	//System.out.println(buffer.toString());
		    	actividadSeleccionada=reemplazaBlancos(buffer.toString());
		    	System.out.println(actividadSeleccionada);
		    	//text.setText(buffer.toString());
		    	}
		    public void widgetDefaultSelected(SelectionEvent e) {
		        int[] indices = listActividades.getSelectionIndices();
		        String[] items = listActividades.getSelection();
		        StringBuffer buffer = new StringBuffer("");
		        for(int i=0; i < indices.length; i++) {
		          buffer.append(items[i]);
		          }
		        //System.out.println(buffer.toString());
		        actividadSeleccionada=buffer.toString();
		    	//text.setText(buffer.toString());
		    	}
		   });   
	    
	    
	/* 4. Lista de Condiciones */
	final List listCondiciones = new List(shell, SWT.V_SCROLL | SWT.BORDER);
	GridData gridData8 = new GridData();
	gridData8.horizontalAlignment = SWT.FILL;
	gridData8.grabExcessHorizontalSpace = true;
	gridData8.verticalAlignment = SWT.FILL;
	gridData8.grabExcessVerticalSpace = true;
	listCondiciones.setLayoutData(gridData8);
	//listConceptos.setBounds(0, 0,0,0);
		
	Iterator it3=instancesCondicion.iterator();
	while (it3.hasNext())
			{
			OWLIndividual condicionIN= (OWLIndividual)it3.next();
			RDFProperty titleCondicionProp = jenaModel.getRDFProperty(uri_actual+"title");
			String pasoConcepto=(String) condicionIN.getPropertyValue(titleCondicionProp);
			System.out.println("condicion :"+pasoConcepto);
			listCondiciones.add(pasoConcepto);
			}
	 
	listCondiciones.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			System.err.println(listCondiciones.getSelectionIndex());
	    	int[] indices = listCondiciones.getSelectionIndices();
	    	String[] items = listCondiciones.getSelection();
	    	StringBuffer buffer = new StringBuffer("");
	    	for(int i=0; i < indices.length; i++) {
	    	  buffer.append(items[i]);
	    	  }
	    	//System.out.println(buffer.toString());
	    	condicionSeleccionada=reemplazaBlancos(buffer.toString());
	    	System.out.println(condicionSeleccionada);
	    	//text.setText(buffer.toString());
	    	}
	    public void widgetDefaultSelected(SelectionEvent e) {
	        int[] indices = listCondiciones.getSelectionIndices();
	        String[] items = listCondiciones.getSelection();
	        StringBuffer buffer = new StringBuffer("");
	        for(int i=0; i < indices.length; i++) {
	          buffer.append(items[i]);
	          }
	        //System.out.println(buffer.toString());
	    	condicionSeleccionada=buffer.toString();
	    	//text.setText(buffer.toString());
	    	}
	   });
	    
	/* 5. espacio  podria ir un boton UNDO */
	    Text esp2Text = new Text(shell, SWT.WRAP | SWT.MULTI);
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = SWT.FILL;
		gridData3.grabExcessVerticalSpace = true;
		esp2Text.setLayoutData(gridData3);
		esp2Text.setText("");   
	 
	/* 6 Boton DO */
	
	Button button1 = new Button(shell, SWT.PUSH);
    button1.setText("Link");
    button1.setToolTipText("Link a Learning Activity with a Condition");
    GridData gridData9 = new GridData();
	gridData9.horizontalAlignment = SWT.END;
	//gridData9.grabExcessHorizontalSpace = true;
	//gridData9.verticalAlignment = SWT.FILL;
	//gridData9.grabExcessVerticalSpace = true;
	button1.setLayoutData(gridData9);
    button1.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			System.out.println("Grabar enlace en la ontologia");
			 //  llama a validador 
	        // retorna instancia de actividad seleccionada
			OWLIndividual instanciaActividad=buscaInstancia(instancesActivity,actividadSeleccionada,uri_ld,jenaModel);
	        System.out.println("instancia encontrada :"+instanciaActividad);
	        // retorna instancia de consepto sleccionado
	        OWLIndividual instanciaCondicion=buscaInstancia(instancesCondicion,condicionSeleccionada,uri_actual,jenaModel);
	        System.out.println("concepto encontrado :"+instanciaCondicion);
	        // propiedad relacion actividad-knowledgeItem
	        RDFProperty activity_condicion_refProp = jenaModel.getRDFProperty(uri_actual+"condition-ref");
	        instanciaActividad.addPropertyValue(activity_condicion_refProp,instanciaCondicion);
			// muestra en la lista la pareja
	        listParejas.add(actividadSeleccionada+" - "+condicionSeleccionada);
            //shell.layout();
		}} );
	
    
    /* 5. espacio  podria ir un boton UNDO */
    Text esp2Sugerencias = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData14 = new GridData();
	gridData14.horizontalAlignment = SWT.FILL;
	gridData14.grabExcessHorizontalSpace = true;
	gridData14.verticalAlignment = SWT.FILL;
	gridData14.grabExcessVerticalSpace = true;
	esp2Sugerencias.setLayoutData(gridData14);
	esp2Sugerencias.setText("");   
	
	/* 7 Texto que presenta las parejas Actividades-Conceptos  */
    Text parejasTitulo = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData15 = new GridData();
	gridData15.horizontalAlignment = SWT.BEGINNING;
	gridData15.grabExcessHorizontalSpace = true;
	gridData15.verticalAlignment = SWT.FILL;
	gridData15.grabExcessVerticalSpace = true;
	parejasTitulo.setLayoutData(gridData15);
	parejasTitulo.setText("Activity - Condition");
  
   
    
    /* 7 Texto que presenta las parejas Actividades-Conceptos  */
    Text parejasText = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData4 = new GridData();
	gridData4.horizontalAlignment = SWT.END;
	gridData4.grabExcessHorizontalSpace = true;
	gridData4.verticalAlignment = SWT.FILL;
	gridData4.grabExcessVerticalSpace = true;
	parejasText.setLayoutData(gridData4);
	parejasText.setText("Activity-Condition selected pairs :");

    
	/* 8. Lista de parejas  */
	listParejas = new List(shell, SWT.V_SCROLL | SWT.BORDER);
	listParejas.setBounds(100, 100,50,30);
	GridData gridData5 = new GridData();
	gridData5.horizontalAlignment = SWT.FILL;
	gridData5.grabExcessHorizontalSpace = true;
	gridData5.verticalAlignment = SWT.FILL;
	gridData5.grabExcessVerticalSpace = true;
	listParejas.setLayoutData(gridData5);
	//listParejas.add("actividad 1"+"-"+"celula");
    
	// 9. espacio  podria ir un boton UNDO 
    Text esp3Text = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData12 = new GridData();
	gridData12.horizontalAlignment = SWT.FILL;
	gridData12.grabExcessHorizontalSpace = true;
	gridData12.verticalAlignment = SWT.FILL;
	gridData12.grabExcessVerticalSpace = true;
	esp3Text.setLayoutData(gridData12);
	esp3Text.setText("");   
    
	// 6 Boton DO 
	Button buttonSalir = new Button(shell, SWT.PUSH);
	buttonSalir.setText("Continue the analysis");
	GridData gridData13 = new GridData();
	gridData13.horizontalAlignment = SWT.END;
	//gridData9.grabExcessHorizontalSpace = true;
	//gridData9.verticalAlignment = SWT.FILL;
	//gridData9.grabExcessVerticalSpace = true;
	buttonSalir.setLayoutData(gridData13);
	buttonSalir.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		shell.dispose();
		//display.dispose();
       
	}} );
		
	shell.pack();
	shell.open();
    
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}	
	}

    /**
     * Reemplaza espacios blancos por "_"
     * 
     * @param cadena
     * @return String
     */
    public String reemplazaBlancos(String cadena)
	   {
	   String cadena2=cadena.replace(' ','_');
	  return(cadena2);
	   }
	
	/**
	 * Define URI de la ontología IMS-LD
	 */
	public void asignaUri()
	{
	uri_idtheory="http://www.cc.uah.es/ie/ont/idtheories.owl#";
	uri_ld="http://www.eume.net/ontology/ld.owl#";
	uri_uol="http://www.eume.net/ontology/uol.owl#";
	uri_lom="http://www.eume.net/ontology/lom.owl#";
	uri_actual="http://www.cc.uah.es/ie/ont/elabtheory.owl#"; // para Elaboration theory
	}
	
	/**
	 * Busca una instancia por su title en una coleccion de instancias
	 * 
	 * @param colecInstancia, coleccion de instancias
	 * @param title, titulo de la instancia
	 * @param uri, string uri de OWL
	 * @param jenaModel, OWL model
	 * @return Instancia buscada 
	 */
	public OWLIndividual buscaInstancia(Collection colecInstancia, String title, String uri, JenaOWLModel jenaModel)
	{
	OWLIndividual instanciaEncontrada=null; 
	Iterator it=colecInstancia.iterator();
		while (it.hasNext())
		{
		OWLIndividual instanciaIN= (OWLIndividual)it.next();
		RDFProperty titleActiviProp = jenaModel.getRDFProperty(uri_actual+"title");
		String titleInstancia = (String) instanciaIN.getPropertyValue(titleActiviProp);
		if (titleInstancia.equals(title))
			instanciaEncontrada=instanciaIN;
		}
	return(instanciaEncontrada);
	}		
	
}
