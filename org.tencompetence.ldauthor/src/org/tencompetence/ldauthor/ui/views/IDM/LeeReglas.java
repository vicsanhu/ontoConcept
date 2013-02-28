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

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.util.*;
import java.io.*;
import java.lang.String;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import java.io.FileOutputStream;
import java.util.Collection;


/**
 * Clase que realiza la lectura de los resultados de las reglas en la ontologia y genera
 * el archivo "resultValidacion.xml" de salida
 * 
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class LeeReglas {

//public  FileOutputStream fout;
public  OutputStreamWriter out;
public  JenaOWLModel jenaModel=null;
public  String ontologia="C://ontologies//retOnto//ret-inf.owl";  /* ontologia de entrada */  
public  String directorioRep="C://ontologies//retOnto//";   /* Repository manager    */
public  String uri_idtheory;
public  String uri_ld;
public  String uri_uol;
public  String uri_lom;
public  String uri_actual;
public  String varResultadoRegla, varIdRule,varPropertyRecomm,varClassOnto,varMethodName, varPropertySourceTheory;
public  String nombreXMLSalida;
public static String[] arrSourceTheory;

	public  void asignaUri()
	{
	uri_idtheory="http://www.cc.uah.es/ie/ont/idtheories.owl#";
	uri_ld="http://www.eume.net/ontology/ld.owl#";
	uri_uol="http://www.eume.net/ontology/uol.owl#";
	uri_lom="http://www.eume.net/ontology/lom.owl#";
	uri_actual="http://www.cc.uah.es/ie/ont/elabtheory.owl#"; // para Elaboration theory
	}
   
   /**
 * 
 * Retorna el valor de una propieadad que almacena el resultado de una regla 
 * 
 * @param classOnto
 * @param propertyOnto
 * @return String
 */
public  String leeValorRule(String classOnto, String propertyOnto)
   {
   
	String varValor="false";
	
    RDFProperty propertyProp = jenaModel.getRDFProperty(propertyOnto);
	OWLNamedClass BuscadaCL=jenaModel.getOWLNamedClass(varClassOnto);
	Collection instances = BuscadaCL.getInstances(false);
    
	boolean varAcum=false;
	boolean varBoolean;
	Iterator it=instances.iterator();
	while (it.hasNext())
			{
			OWLIndividual buscarIN= (OWLIndividual)it.next();
			varValor = (String) buscarIN.getPropertyValue(propertyProp);
			if (varValor== null)  varValor="false";
			//System.out.println(" lectura directa valor regla: "+varValor);
			varBoolean=Boolean.parseBoolean(varValor);
			varAcum=(varBoolean || varAcum);
			//System.out.println(" isntancia: "+varValor);
			}
	String varStrValue = new Boolean(varAcum).toString();
	//System.out.println(" valor devuelto :"+varStrValue);
	return(varStrValue);			
  }

   
  /**
  * Lee informacion de un nodo de reglas del archivo "resultValidacion.xml"
  *
  * @param nodoRule
  */
  public  void leeNodoRule(Element nodoRule)
   {
   //System.out.println("hasta aqui 3");
   System.out.println("---------------------------------------------------------------------");//$NON-NLS-1$
	Element idRule=nodoRule.getChild("id-rule");
	varIdRule=reemplazaBlancos(idRule.getText());
	System.out.println("Id Rule:"+varIdRule);	//$NON-NLS-1$
	
	Element methodNam=nodoRule.getChild("methodName");
	varMethodName=methodNam.getText();
	System.out.println("Nombre Metodo:"+varMethodName);	//$NON-NLS-1$
	
	
	Element classOntoRule=nodoRule.getChild("class-onto");
	varClassOnto=reemplazaBlancos(classOntoRule.getText());
	System.out.println("Class: "+varClassOnto);//$NON-NLS-1$
								
	Element propertyOntoRule=nodoRule.getChild("property-onto");
	String varPropertyOnto=reemplazaBlancos(propertyOntoRule.getText());
	
	//System.out.println("Property: "+varPropertyOnto);
    
	varResultadoRegla=leeValorRule(varClassOnto,varPropertyOnto);
	//System.out.println(" llamada leeValorRule (valor regla): "+varResultadoRegla);
	
	Element propertyRecomm=nodoRule.getChild("recomm");
	varPropertyRecomm=propertyRecomm.getText();
	
	Element propertySourceTheo=nodoRule.getChild("sourceReigeluth");
	varPropertySourceTheory=propertySourceTheo.getText();
  }

   // reemplaza los espacios en blancos de los nombres de componentes IMSLD    
   public  String reemplazaBlancos(String cadena)
   {
   String cadena2="";
   StringTokenizer stTexto = new StringTokenizer(cadena);
   while (stTexto.hasMoreElements())
   cadena2 += stTexto.nextElement();
   return(cadena2);
  }
	
   // CARGA LA ONTOLOGIA Y DEVUELVE UN JenaOWLModel          
   private  JenaOWLModel cargaOntologias(String ontologia, String directorioRep)
   {
    try {
			
		BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(ontologia));
        } catch(java.io.IOException e) {
				System.out.println(e);//$NON-NLS-1$
										}
        //jenaModel = null;
        try {
            jenaModel = ProtegeOWL.createJenaOWLModel();
			} catch (Exception ex) {
				ex.printStackTrace();
								   }
        LocalFolderRepository rep = new LocalFolderRepository(new File(directorioRep)); //$NON-NLS-1$
        jenaModel.getRepositoryManager().addGlobalRepository(rep); //$NON-NLS-1$         
            try {
                jenaModel.load(reader, null);
            } catch (Exception ex) {
									ex.printStackTrace();
								   }
	} catch (Exception e) { System.out.println("Exception al cargar la ontologia: " + e);	}
    return(jenaModel);
    }

    /**
     * Obtiene el nombre del LD
     * 
     * @param claseLD
     * @return String
     */
    public  String obtieneNombreLD(String claseLD)
	{
	RDFProperty propertyProp = jenaModel.getRDFProperty(uri_ld+"identifier");
	OWLNamedClass BuscadaCL=jenaModel.getOWLNamedClass(claseLD);
	Collection instances = BuscadaCL.getInstances(false);
    
	//boolean varAcum=false;
	//boolean varBoolean;
	String varValor=null;
	Iterator it=instances.iterator();
	while (it.hasNext())
			{
			OWLIndividual buscarIN= (OWLIndividual)it.next();
			varValor = (String) buscarIN.getPropertyValue(propertyProp);
			}
	return(varValor);	
	}
	
	
	/**
	 * Realiza la lectura del archivo de entrada "reglasMetodo-IN-all.xml" 
	 */
	public void lee_XML()
	{
	String[] recomendaciones;
	recomendaciones = new String[30];
	Integer index=0;
	boolean varValueMethod;
	Element method=null;
	Element Rule=null;	
		
	    // comianza a leer xml 
		SAXBuilder builder=new SAXBuilder(false); 
		try
		{
		// raiz de XMl de salida
		Element rootOut=new Element("methods");
		Document docOut=new Document(rootOut);
				
		Document docIn=builder.build("C:/ontologies/XML-validation/reglasMetodo-IN-all.xml");
		if (docIn==null) System.out.println("doc es nulo");
		Element raizIn=docIn.getRootElement();
		if (raizIn==null) System.out.println("NO raiz es nulo");
		List listaMethodIn=raizIn.getChildren();
		Iterator i_IN = listaMethodIn.iterator();
		if (i_IN==null) System.out.println("i es nulo");
		/**********************************************************************/
		
		while (i_IN.hasNext())
			{
			Element nodoMethodIN= (Element)i_IN.next();
			String varIdentMethod=nodoMethodIN.getAttributeValue("id-method");
			System.out.println("=======================================================================");//$NON-NLS-1$
			System.out.println("Metodo: "+varIdentMethod); //$NON-NLS-1$			
			// xml salida
			method=new Element("method").setAttribute("id-method",varIdentMethod);
			List listaruleIN=nodoMethodIN.getChildren("rule");
			Iterator j_IN = listaruleIN.iterator();
			if (j_IN==null) System.out.println("j_IN es nulo");
			/**********************************************************************/
			varValueMethod=false;
			while (j_IN.hasNext())
				{
				// XML salida
				Rule=new Element("rule");
				
				Element nodoRuleIN= (Element)j_IN.next();
				leeNodoRule(nodoRuleIN);
				System.out.println("== resultado de regla: "+varResultadoRegla);//$NON-NLS-1$
				// 
				varValueMethod=(varValueMethod || Boolean.parseBoolean(varResultadoRegla));
				// xml salida
				Element idRule=new Element("id-rule").setText(varIdRule);
				Element valueRule=new Element("value").setText(varResultadoRegla);
				Element recomm=new Element("recomm").setText(varPropertyRecomm);
				Element metName=new Element("methodName").setText(varMethodName);
				Element sourceTheo=new Element("sourceReigeluth").setText(varPropertySourceTheory);
				Rule.addContent(idRule);
				Rule.addContent(valueRule);
				Rule.addContent(recomm);
				Rule.addContent(metName);
				Rule.addContent(sourceTheo);
				method.addContent(Rule);
				
				}
			System.out.println("=======================================================================");//$NON-NLS-1$
			// XML salida
			String varStrValueMethod = new Boolean(varValueMethod).toString();
			method.setAttribute("value-method",varStrValueMethod);
			rootOut.addContent(method);
			} 
		
						
		// lee id de method para nombre de archivo XML de salida
		nombreXMLSalida=obtieneNombreLD(uri_ld+"Learning-Design");
		
		XMLOutputter out=new XMLOutputter(Format.getPrettyFormat()); 	
		FileOutputStream file=new FileOutputStream("C:/ontologies/XML-validation/resultValidacion.xml");
		out.output(docOut,file);
		file.flush();
		file.close();
		out.output(docOut,System.out);
		docOut=null;
		docIn=null;		
		
		//recomendaciones=leeResultadosReglas();
		System.out.println("== termina metodo lee_XML");
	} catch (Exception nodoRule){ nodoRule.printStackTrace();  }
	
	}
	
	public  String[] ordenMetodosLeeReglas(String metodoSeleccionado)
	{
	System.out.println(" ** En leeReglas5.java  **"); //$NON-NLS-1$
	asignaUri();
	jenaModel=cargaOntologias(ontologia,directorioRep);
	lee_XML();
	String[] recomString2=leeResultadosReglas(metodoSeleccionado);
	/*
	for (int x=0;x<recomString.length;x++) {
		System.out.println (recomString[x]);
		}
	*/
	return (recomString2);
	}
	
	
	/**
	 * Retorna el arreglo recomendacionesXML con las sugerencias
	 * 
	 * @param metodoSeleccionado
	 * @return String[]
	 */
	public  String[] leeResultadosReglas(String metodoSeleccionado)
	{
	arrSourceTheory = new String[30];
	System.out.println("= comienza mretodo leeResultadosReaglas ==");	
	System.out.println("**** Metodo Seleccionado en leeReglas:"+metodoSeleccionado);
	// variable que recoge las recomendaciones
	String[] recomendacionesXML;
	recomendacionesXML = new String[30];
	Integer index=0;
	boolean varValueMethodXML;
	Integer indexXML=0;
	
	// comianza a leer xml 
	SAXBuilder builderXML=new SAXBuilder(false); 
	try
	{
	Document docXML=builderXML.build("C:/ontologies/XML-validation/resultValidacion.xml"); 
	if (docXML==null) System.out.println("docXML es nulo");
	Element raizXML=docXML.getRootElement();
	if (raizXML==null) System.out.println("NOdo raiz es nulo");
	List listaMethodXML=raizXML.getChildren();
	Iterator i_XML = listaMethodXML.iterator();
	if (i_XML==null) System.out.println("i_XML es nulo");
	
	while (i_XML.hasNext())
		{
		Element nodoMethodXML= (Element)i_XML.next();
		String varIdentMethod=nodoMethodXML.getAttributeValue("id-method");
		String varValueMethod=nodoMethodXML.getAttributeValue("value-method");
		Boolean varBooleanValueMethod= Boolean.parseBoolean(varValueMethod);
		System.out.println("XML====================================================================");//$NON-NLS-1$
		System.out.println("Metodo: "+varIdentMethod); //$NON-NLS-1$			
		List listaruleXML=nodoMethodXML.getChildren("rule");
		Iterator j_XML = listaruleXML.iterator();
		if (j_XML==null) System.out.println("j_XML es nulo");
				
		while (j_XML.hasNext())
			{
			Element nodoRuleXML= (Element)j_XML.next();
			
			Element idRule=nodoRuleXML.getChild("id-rule");
			String varIdRule=reemplazaBlancos(idRule.getText());
			System.out.println("XML== Id Rule:"+varIdRule);	//$NON-NLS-1$
			
			Element valueRuleXML=nodoRuleXML.getChild("value");
			String varValueRuleXML=reemplazaBlancos(valueRuleXML.getText());
			System.out.println("XML== value: "+varValueRuleXML);//$NON-NLS-1$
										
			Element recommXML=nodoRuleXML.getChild("recomm");
			String varRecommXML=recommXML.getText();
			System.out.println("XML== recomm: "+varRecommXML);//$NON-NLS-1$
			
			Element nombreMetodoXML=nodoRuleXML.getChild("methodName");
			String varNombreMetodoXML=nombreMetodoXML.getText();
			System.out.println("XML== nombMetodo: "+varNombreMetodoXML);//$NON-NLS-1$
			
			Element sourceTheoryXML=nodoRuleXML.getChild("sourceReigeluth");
			String varSourceTheoryXML=sourceTheoryXML.getText();
			System.out.println("XML== Reigeluth: "+varSourceTheoryXML);//$NON-NLS-1$
			
			
			Boolean varBooleanValueRule= Boolean.parseBoolean(varValueRuleXML);
			
			if ((varValueMethod.equals("false"))&&(varValueRuleXML.equals("false")))
				{
				if (varNombreMetodoXML.equals(metodoSeleccionado)) 
				{ 
				recomendacionesXML[indexXML]=varRecommXML;
				System.out.println("+++++++++++++++Entre en condicion medtdo falso y seleccionado:"+metodoSeleccionado);
				
				arrSourceTheory[indexXML]=varSourceTheoryXML;
				indexXML++;
				
				}
				}
			} 
		 }
		}catch (Exception e) { System.out.println("Exception al leer XML: " + e);	}
	System.out.println("= termina metodo leeResultadosReaglas ==");
	
	System.out.println("arreglo recoemnda en IDMethod final Lee reglas: "+recomendacionesXML[0]);
	System.out.println("arreglo recoemnda en IDMethod final Lee reglas: "+recomendacionesXML[1]);
	System.out.println("arreglo recoemnda en IDMethod final Lee reglas: "+recomendacionesXML[3]);
	return(recomendacionesXML);	
	}
	
	
	/**
	 * Retorna un arregla de String con los fundamentos teoricos de las sugerencias
	 * 
	 * @return String[]
	 */
	public String[] getSourceTheory()
	{
	return(arrSourceTheory);	
	}
	
}