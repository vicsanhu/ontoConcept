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

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;
import edu.stanford.smi.protegex.owl.swrl.bridge.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.String;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter; 
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
 
   
/**
 * Clase que crea BridgeSWRL, realiza la inferencia y genera archivo OWL de salida
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class BridgeInferencia
	{
	public  String ontologia="C:/ontologies/retOnto/ret-out.owl";
    public  String directorioRep="C://ontologies//retOnto//";   /* Repository manager    */
	public  String ontologiaSalida="C:/ontologies/retOnto/ret-inf.owl";
		
	/**
	 * construye el BridgeSWRL, activa y ejecuta las reglas para el metodo seleccionado
	 * 
	 * @param metodoSeleccionado
	 */
	public  void bridgeSWRL(String metodoSeleccionado)
	{
				
		System.out.println(" ** En bridge5inferencia.java  **"); //$NON-NLS-1$
		try {
			
		BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(ontologia));
        }catch(java.io.IOException e){
            System.out.println(e); //$NON-NLS-1$
        }
        JenaOWLModel jenaModel = null;
        try {
            jenaModel = ProtegeOWL.createJenaOWLModel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LocalFolderRepository rep = new LocalFolderRepository(new File(directorioRep));//$NON-NLS-1$
        jenaModel.getRepositoryManager().addGlobalRepository(rep); //$NON-NLS-1$        
            try {
                jenaModel.load(reader, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
      			
	    		
		SWRLRuleEngineBridge bridge=null;
		bridge = BridgeFactory.createBridge(jenaModel);
		
		/*   habilita reglas   */
		SWRLFactory  factory = new SWRLFactory(jenaModel); 
		
		// si es el metodo Conceptual or theoretical elaboration sequence 
		if ((metodoSeleccionado.equals("theoretical")) || (metodoSeleccionado.equals("conceptual")))
		{	
		SWRLImp swrlRule1 = factory.getImp("showsBefore");
        swrlRule1.enable(); 
        SWRLImp swrlRule2 = factory.getImp("met1-1-1");
        swrlRule2.enable(); 
        SWRLImp swrlRule3 = factory.getImp("met1-1-2");
        swrlRule3.enable(); 
        SWRLImp swrlRule4 = factory.getImp("met1-1-3");
        swrlRule4.enable(); 
        SWRLImp swrlRule5 = factory.getImp("met1-1-4");
        swrlRule5.enable(); 
        SWRLImp swrlRule6 = factory.getImp("met1-2");
        swrlRule6.enable(); 
        SWRLImp swrlRule7 = factory.getImp("met1-4");
        swrlRule7.enable(); 
        SWRLImp swrlRule8 = factory.getImp("met1-5-1");
        swrlRule8.enable(); 
        SWRLImp swrlRule9 = factory.getImp("met1-5-2");
        swrlRule9.enable(); 
        SWRLImp swrlRule10 = factory.getImp("met1-6-1");
        swrlRule10.enable(); 
        SWRLImp swrlRule11 = factory.getImp("met1-6-2");
        swrlRule11.enable(); 
        System.out.println("reglas activadas: theoretical o conceptual");
		}		
		
		//IF Simplifying conditions sequence
		if ((metodoSeleccionado.equals("simplifying")))
		{	
		
		SWRLImp swrlRule12 = factory.getImp("showsBefore");
        swrlRule12.enable(); 
        SWRLImp swrlRule13 = factory.getImp("met3-1-1");
        swrlRule13.enable(); 
        SWRLImp swrlRule14 = factory.getImp("met3-1-2");
        swrlRule14.enable(); 
        SWRLImp swrlRule15 = factory.getImp("met3-2-2");
        swrlRule15.enable(); 
        SWRLImp swrlRule16 = factory.getImp("met3-5-1");
        swrlRule16.enable(); 
        SWRLImp swrlRule17 = factory.getImp("met3-5-2");
        swrlRule17.enable(); 
        SWRLImp swrlRule18 = factory.getImp("met3-5-3");
        swrlRule18.enable(); 
        SWRLImp swrlRule19 = factory.getImp("met3-5-4");
        swrlRule19.enable(); 
        SWRLImp swrlRule20 = factory.getImp("met3-6-1");
        swrlRule20.enable();
        SWRLImp swrlRule21 = factory.getImp("met3-6-2");
        swrlRule21.enable();
        SWRLImp swrlRule22 = factory.getImp("met3-7");
        swrlRule22.enable();
        System.out.println("reglas activadas: simplifyng");
        /* fin habilitar reglas   */	
		}
		
		bridge.infer();
	
		/* archivo owl de salida  */
		FileWriter writer = new FileWriter(ontologiaSalida);
        OWLModelWriter omw = new OWLModelWriter(jenaModel,jenaModel.getTripleStoreModel().getActiveTripleStore(), writer);
        omw.write();
        writer.close();
			
			} catch (Exception e) { System.out.println("Exception: " + e);	}
		}
	
	
	}


