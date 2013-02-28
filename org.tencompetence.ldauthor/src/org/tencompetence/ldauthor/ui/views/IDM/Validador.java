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
import java.lang.String;

/**
 * Clase que organiza la instnciacion de las clases recorreManifest.java, bidge5Inferencia.java
 * y leeReglas5.java
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class Validador
	{
		
	public  String[] recomenda;
	
	/**
	 * Realiza la invocacion a la sclases para realizar la validacion del LD
	 * para el metodo ID seleccionado y la ontologia de dominio seleccionada
	 * 
	 * @param archivo
	 * @param metodoSeleccionado
	 * @param ontologiaSeleccionada
	 * @return String[]
	 */
	public  String[] valida(String archivo, String metodoSeleccionado, String ontologiaSeleccionada)
	{
		try {		
			recomenda = new String[30];  
			
			// nombre de archivo que llega 
			System.out.println("metodo seleccionado (Validador) :" + metodoSeleccionado);
			
			
			RecorreManifest recorreManifestIMS= new RecorreManifest();
			recorreManifestIMS.leeManifest(archivo,metodoSeleccionado, ontologiaSeleccionada);
			
			BridgeInferencia inferencia = new BridgeInferencia();
			inferencia.bridgeSWRL(metodoSeleccionado);
			
			LeeReglas leeReglas = new LeeReglas();
			recomenda= leeReglas.ordenMetodosLeeReglas(metodoSeleccionado);
			
			//recomenda[0]="hola";
			
		} catch (Exception e) { System.out.println("Exception : " + e);	}  	
		
	return(recomenda);
	}
	
		
	}