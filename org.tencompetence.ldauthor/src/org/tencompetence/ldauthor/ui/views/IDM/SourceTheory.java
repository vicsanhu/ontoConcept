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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;


/**
 * Clase que muestra los fundamentos teoricos de cada sugerencia
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class SourceTheory extends JFrame {

    public SourceTheory() {
	// TODO Auto-generated constructor stub
    }

	/**
	 * Genera la pantalla que muestra la fundamentacion teorica para un sugerencia
	 * 
	 * @param sourceRecomendacion
	 * @param sugerencia
	 */
	public void generaVentanaTheory(String sourceRecomendacion, String sugerencia)
	{
	System.out.println("dentro de sourceTheory :"+sourceRecomendacion);	
	Display display =PlatformUI.createDisplay();
	final Shell shell = new Shell(display, SWT.CLOSE);
	shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	shell.setSize(250, 200);
	shell.setText("Reigeluth's Elaboration Theory");
	GridLayout gridLayout = new GridLayout();
	gridLayout.verticalSpacing = 8;
	gridLayout.horizontalSpacing = 3;
	gridLayout.numColumns = 1;
	shell.setLayout(gridLayout);
		
		
	final StyledText widget = new StyledText(shell,0);
	String pasoTitulo="The suggestion:";
	widget.setText(pasoTitulo);
	StyleRange style3 = new StyleRange();
	style3.background = display.getSystemColor(SWT.COLOR_WHITE);
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	 gridData.horizontalSpan = 1;
	//gridData.horizontalAlignment = SWT.FILL;
	gridData.grabExcessHorizontalSpace = true;
	gridData.verticalAlignment = SWT.FILL;
	gridData.grabExcessVerticalSpace = true;
	widget.setLayoutData(gridData);
    widget.setStyleRange(style3);
	    
    Label textoSugerencia = new Label(shell, SWT.BORDER | SWT.WRAP);
    textoSugerencia.setBackground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
    String pasoTitulo2="\n"+sugerencia+"\n"+" ";
	textoSugerencia.setText(pasoTitulo2);
	GridData gridDataSug = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	 gridDataSug.horizontalSpan = 1;
	//gridData.horizontalAlignment = SWT.FILL;
	gridDataSug.grabExcessHorizontalSpace = true;
	gridDataSug.verticalAlignment = SWT.FILL;
	gridDataSug.grabExcessVerticalSpace = true;
	textoSugerencia.setLayoutData(gridDataSug);
	//textoSugerencia.setStyleRange(styleSug);
		
	final StyledText widget2 = new StyledText(shell,0);
	String pasoTitulo3="\n is based on the following method of The Elaboration Theory [1]:";
	widget2.setText(pasoTitulo3);
	StyleRange style8 = new StyleRange();
	//style8.fontStyle = SWT.BOLD;
	GridData gridData8 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	 gridData8.horizontalSpan = 1;
	//gridData.horizontalAlignment = SWT.FILL;
	gridData8.grabExcessHorizontalSpace = true;
	gridData8.verticalAlignment = SWT.FILL;
	gridData8.grabExcessVerticalSpace = true;
	widget2.setLayoutData(gridData8);
    widget2.setStyleRange(style8);
	//widget.setText("This is the StyledText widget.");
	    
    Label widgetSugg = new Label(shell, SWT.BORDER | SWT.WRAP);
    widgetSugg.setBackground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
    widgetSugg.setText("\n"+"\""+sourceRecomendacion+"\""+"\n"+"  ");
    GridData gridData1 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	gridData1.horizontalSpan = 1;
	gridData1.grabExcessHorizontalSpace = true;
	gridData1.verticalAlignment = SWT.FILL;
	gridData1.grabExcessVerticalSpace = true;
	widgetSugg.setLayoutData(gridData1);
              
	final StyledText widgetRef = new StyledText(shell,0);
	String referenceStr = "\n Reference:";
	widgetRef.setText(referenceStr);
	StyleRange style5 = new StyleRange();
	//style3.fontStyle = SWT.BOLD;
	style5.background = display.getSystemColor(SWT.COLOR_WHITE);
    GridData gridData3 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	 gridData3.horizontalSpan = 1;
	//gridData.horizontalAlignment = SWT.FILL;
	gridData3.grabExcessHorizontalSpace = true;
	gridData3.verticalAlignment = SWT.FILL;
	gridData3.grabExcessVerticalSpace = true;
	widgetRef.setLayoutData(gridData3);
    widgetRef.setStyleRange(style5);
    	
    final StyledText widgetReigeluth = new StyledText(shell,0);
    String reigeluthStr ="[1] Reigeluth, C.M. 1999. Instructional-Design Theories and Models, \n Volume II: A New Paradigm of Instructional Theory. Lawrence Erlbaum Assoc., Mahwah, NJ.";
	widgetReigeluth.setText(reigeluthStr);
	StyleRange style7 = new StyleRange();
	//style3.fontStyle = SWT.BOLD;
	style7.background = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
    GridData gridData4 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	 gridData4.horizontalSpan = 1;
	//gridData.horizontalAlignment = SWT.FILL;
	gridData4.grabExcessHorizontalSpace = true;
	gridData4.verticalAlignment = SWT.FILL;
	gridData4.grabExcessVerticalSpace = true;
	widgetReigeluth.setLayoutData(gridData4);
    widgetReigeluth.setStyleRange(style7);
            	
	Button cerrar = new Button(shell, SWT.PUSH);
    cerrar.setText("Close");
    GridData gridData9 = new GridData();
	gridData9.horizontalAlignment = SWT.END;
	gridData9.grabExcessHorizontalSpace = true;
	gridData9.verticalAlignment = SWT.FILL;
	gridData9.grabExcessVerticalSpace = true;
	cerrar.setLayoutData(gridData9);
    cerrar.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();			
		}} );
       
    shell.pack();
	shell.open();
	}

}
