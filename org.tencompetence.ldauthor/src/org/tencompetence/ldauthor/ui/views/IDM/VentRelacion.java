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

import javax.imageio.ImageIO;
import javax.swing.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorTitleComposite;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.eclipse.swt.widgets.Event;

/**
 * Clase que realiza la vinculacion entre Learning Activity e ItemKnowledge - Implementa GUI
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class VentRelacion extends JFrame {

public static String actividadSeleccionada, conceptoSeleccionado; // indices en las listas
public List listParejas;
public String uri_idtheory;
public String uri_ld;
public String uri_uol;
public String uri_lom;
public String uri_actual;
private InspectorTitleComposite fTitleBar;
        
    public VentRelacion() {
	// TODO Auto-generated constructor stub
    }
	
    /**
     * Genera ventana que permite vincular Actividades de Aprendizaje con Topicos
     * 
     * @param instancesActivity 
     * @param instancesConcept  
     * @param jenaModel
     */
    public void generaVentana(final Collection instancesActivity, final Collection instancesConcept, final JenaOWLModel jenaModel)
	{
	asignaUri();	
	final Display display =PlatformUI.createDisplay();
	//Display display = new Display();
	
	final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
	//Image iconoTerpsicore = Toolkit.getDefaultToolkit().getImage("icons/flagIcon_chile.png");
	//this.setIconImage(ImageIO.read(getClass().getResource("icons/flagIcon_chile.png")));
	//this.setIconImage (iconoTerpsicorenew ImageIcon("icons/flagIcon_chile.png").getImage());
	//this.setIconImage (new ImageIcon("icons/flagIcon_chile.png").getImage());

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
	shell.setText("Linking Learning Activities and Topics");
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
    fTitleBar.setTitle("The Elaboration Theory requires Learning Activities to be linked with Topics of a domain ontology.\nSelect a Learning Activity and a Topic, then press Link button.\nYou can select the pairs of Activities and Topics that are required.", null);//$NON-NLS-1$
	
		
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
	Text tituloConcept = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData6 = new GridData();
	gridData6.horizontalAlignment = SWT.FILL;
	gridData6.grabExcessHorizontalSpace = true;
	gridData6.verticalAlignment = SWT.FILL;
	gridData6.grabExcessVerticalSpace = true;
	tituloConcept.setLayoutData(gridData6);
	tituloConcept.setText("Topics");
	
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
	    
	    
	/* 4. Lista de Conceptos */
	final List listConceptos = new List(shell, SWT.V_SCROLL | SWT.BORDER);
	GridData gridData8 = new GridData();
	gridData8.horizontalAlignment = SWT.FILL;
	gridData8.grabExcessHorizontalSpace = true;
	gridData8.verticalAlignment = SWT.FILL;
	gridData8.grabExcessVerticalSpace = true;
	listConceptos.setLayoutData(gridData8);
	//listConceptos.setBounds(0, 0,0,0);
	
	// creacion de un arreglo para ordenar Topics
	ArrayList arrayListConceptos = new ArrayList();
	
	Iterator it2=instancesConcept.iterator();
	while (it2.hasNext())
			{
			OWLIndividual conceptoIN= (OWLIndividual)it2.next();
			RDFProperty titleConcepProp = jenaModel.getRDFProperty(uri_actual+"title");
			String pasoConcepto=(String) conceptoIN.getPropertyValue(titleConcepProp);
			System.out.println("concepto :"+pasoConcepto);
			arrayListConceptos.add(pasoConcepto); 
			}
	// ordena Lista de Topicos
	Collections.sort(arrayListConceptos,String.CASE_INSENSITIVE_ORDER);
	
	// Agrega las topicos ordenados en SWT List
	Iterator itArrayList=arrayListConceptos.iterator();
	while (itArrayList.hasNext())
			{
			listConceptos.add((String)itArrayList.next());
			}
		
	listConceptos.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			System.err.println(listConceptos.getSelectionIndex());
	    	int[] indices = listConceptos.getSelectionIndices();
	    	String[] items = listConceptos.getSelection();
	    	StringBuffer buffer = new StringBuffer("");
	    	for(int i=0; i < indices.length; i++) {
	    	  buffer.append(items[i]);
	    	  }
	    	//System.out.println(buffer.toString());
	    	conceptoSeleccionado=reemplazaBlancos(buffer.toString());
	    	System.out.println(conceptoSeleccionado);
	    	//text.setText(buffer.toString());
	    	}
	    public void widgetDefaultSelected(SelectionEvent e) {
	        int[] indices = listConceptos.getSelectionIndices();
	        String[] items = listConceptos.getSelection();
	        StringBuffer buffer = new StringBuffer("");
	        for(int i=0; i < indices.length; i++) {
	          buffer.append(items[i]);
	          }
	        //System.out.println(buffer.toString());
	    	conceptoSeleccionado=buffer.toString();
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
    button1.setToolTipText("Link a Learning Activity with a learning Topic");
    GridData gridData9 = new GridData();
	gridData9.horizontalAlignment = SWT.END;
	button1.setLayoutData(gridData9);
    button1.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			System.out.println("Grabar enlace en la ontologia");
			 //  llama a validador 
	        // retorna instancia de actividad seleccionada
			OWLIndividual instanciaActividad=buscaInstancia(instancesActivity,actividadSeleccionada,uri_ld,jenaModel);
	        System.out.println("instancia encontrada :"+instanciaActividad);
	        // retorna instancia de consepto sleccionado
	        OWLIndividual instanciaConcep=buscaInstancia(instancesConcept,conceptoSeleccionado,uri_actual,jenaModel);
	        System.out.println("concepto encontrado :"+instanciaConcep);
	        // propiedad relacion actividad-knowledgeItem
	        RDFProperty activity_concept_refProp = jenaModel.getRDFProperty(uri_actual+"concept-learning-objective");
	        instanciaActividad.addPropertyValue(activity_concept_refProp,instanciaConcep);
			// muestra en la lista la pareja
	        listParejas.add(actividadSeleccionada+" - "+conceptoSeleccionado);
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
	parejasTitulo.setText("Activity - Topic");

    
    /* 7 Texto que presenta las parejas Actividades-Conceptos  */
    Text parejasText = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData4 = new GridData();
	gridData4.horizontalAlignment = SWT.END;
	gridData4.grabExcessHorizontalSpace = true;
	gridData4.verticalAlignment = SWT.FILL;
	gridData4.grabExcessVerticalSpace = true;
	parejasText.setLayoutData(gridData4);
	parejasText.setText("Activity-Topic selected pairs :");

    
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
    
	
	// 9. espacio 
    Text esp3Text = new Text(shell, SWT.WRAP | SWT.MULTI);
	GridData gridData12 = new GridData();
	gridData12.horizontalAlignment = SWT.FILL;
	gridData12.grabExcessHorizontalSpace = true;
	gridData12.verticalAlignment = SWT.FILL;
	gridData12.grabExcessVerticalSpace = true;
	esp3Text.setLayoutData(gridData12);
	esp3Text.setText("");   
    
	// 6 Boton Continue the analysis 
	Button buttonSalir = new Button(shell, SWT.PUSH);
	buttonSalir.setText("Continue the analysis");
	GridData gridData13 = new GridData();
	gridData13.horizontalAlignment = SWT.END;
	buttonSalir.setLayoutData(gridData13);
	buttonSalir.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		shell.dispose();
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
	public  String reemplazaBlancos(String cadena)
	   {
	   String cadena2=cadena.replace(' ','_');
	  return(cadena2);
	   }

	
	/**
	 * Define URI de la ontología IMS-LD
	 */
	public  void asignaUri()
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
	 * @param colecInstancia
	 * @param title
	 * @param uri
	 * @param jenaModel
	 * @return OWLIndividual
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
