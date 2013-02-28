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
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import java.util.*; 
import java.io.FileWriter;
import java.lang.String;
import java.util.Collection;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;
import java.net.URI;
import org.jdom.*;
import org.jdom.input.*;

/**
 * Clase que lee el archivo imsmanifest.xml y realiza las instancias de esta informacion
 * en la ontologia IMS-LD
 *  
 * @author Christian Vidal
 * @version Terpsicore 1.3
 */
public class RecorreManifest {
	public  JenaOWLModel jenaModel=null;
	//public  String ontologia="file:/c:/ontologies/retOnto/ret.owl";
	//public  String ontologia="file:///C:/ontologies/retOnto/ret.owl";  
	public  String ontologia="C://ontologies//retOnto//ret.owl";  /* ontologia Base de entrada */    
    public  String directorioRep="C://ontologies//retOnto//";   /* Repository manager    */
    //public  String directorioRep="file:/c:/ontologies/retOnto/";   
    public  String ontologiaSalida="C:/ontologies/retOnto/ret-out.owl";
	public  String uri_idtheory;
	public  String uri_ld;
	public  String uri_uol;
	public  String uri_lom;
	public  String uri_actual;
	public  Namespace nsimsld=null, ns;
	public  Element raiz;
	public  Iterator i;	
	public  Integer ident=1;
	public  OWLAllDifferent allDifferent;
	//public  leerManifest leer;  
	
	
	/**
	 * Graba la ontologia con las instancias del LD
	 */
	public  void guardaOntologias()
	{
	try {
	    // archivo owl de salida  
		FileWriter writer = new FileWriter(ontologiaSalida);
		OWLModelWriter omw = new OWLModelWriter(jenaModel,jenaModel.getTripleStoreModel().getActiveTripleStore(), writer);
        omw.write();
        writer.close();
	} catch (Exception e) { System.out.println("Exception: " + e);	}
	}
 
    /**********************************************************************************/
	public  void asignaUri()
	{
	/* asigna manuelmente las URIs de las sub ontologias */
	uri_idtheory="http://www.cc.uah.es/ie/ont/idtheories.owl#";
	uri_ld="http://www.eume.net/ontology/ld.owl#";
	uri_uol="http://www.eume.net/ontology/uol.owl#";
	uri_lom="http://www.eume.net/ontology/lom.owl#";
	//uri_actual="http://www.cc.uah.es/ie/ont/MIntelligences.owl#"; // para Multiple Intelligences theory
	uri_actual="http://www.cc.uah.es/ie/ont/elabtheory.owl#"; // para Elaboration theory
	}

 /* reemplaza los espacios en blancos de los nombres de componentes IMSLD   */ 
   public  String reemplazaBlancos(String cadena)
   {
   String cadena2=cadena.replace(' ','_');
   //for (int x=0; x < cadena.length(); x++) {
    //if (cadena[x] == ' ')
    //cadena[x]='_';  }
  return(cadena2);
  }
 
 /* CARGA LA ONTOLOGIA Y DEVUELVE UN JenaOWLModel              */
 /**
 * Carga la ontologia rectornando un OWLModel
 * 
 * @param ontologia
 * @param directorioRep
 * @return JenaOWLModel
 */
private  JenaOWLModel cargaOntologias(String ontologia, String directorioRep)
   {
    try {
			
		BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(ontologia));
        }catch(java.io.IOException e){
            System.out.println(e);//$NON-NLS-1$
        }
        /* estaba en comentario*/
        jenaModel = null;
        
        try {
            /* asi estaba */
        	//jenaModel = ProtegeOWL.createJenaOWLModelFromURI("file:/c:/ontologies/retOnto/ret.owl"); 
        	jenaModel = ProtegeOWL.createJenaOWLModel(); 

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // en norton
        LocalFolderRepository rep = new LocalFolderRepository(new File(directorioRep));//$NON-NLS-1$
        jenaModel.getRepositoryManager().addGlobalRepository(rep);  //$NON-NLS-1$
                            
        /* esto estaba en comentario */       
        try {
             jenaModel.load(reader, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
  	            
	allDifferent = jenaModel.createOWLAllDifferent();
      	
	} catch (Exception e) { System.out.println("Exception al cargar la ontologia: " + e);	}
    return(jenaModel);
    }

	
	/**
	 * Borra instancias de una Clase
	 * 
	 * @param classOnto
	 */
	public  void borraInstancias(String classOnto)
   {
   	
	OWLNamedClass BuscadaCL=jenaModel.getOWLNamedClass(classOnto);
	Collection instances = BuscadaCL.getInstances(false);
    
	boolean varAcum=false;
	boolean varBoolean;
	Iterator it=instances.iterator();
	while (it.hasNext())
			{
			OWLIndividual buscarIN= (OWLIndividual)it.next();
			//System.out.println(" isntancia: "+buscarIN);
			buscarIN.delete();
			// varValor = (String) buscarIN.getPropertyValue(propertyProp);
			// if (varValor== null)  varValor="false";
			// //System.out.println(" lectura directa valor regla: "+varValor);
			// varBoolean=Boolean.parseBoolean(varValor);
			// varAcum=(varBoolean || varAcum);
			// //System.out.println(" isntancia: "+varValor);
			}
	}
	
	 /**
	 * Crea un role-part
	 * 
	 * @param elemento
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaRolePart(Element elemento)
	{
	OWLNamedClass rolePartCL=jenaModel.getOWLNamedClass(uri_ld+"Role-Part");
	/* propiedades de role-part */
	RDFProperty titleRolePartProp = jenaModel.getRDFProperty(uri_actual+"title");
	RDFProperty identifierRolePartProp = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty roleRefProp = jenaModel.getRDFProperty(uri_ld+"role-ref");
	RDFProperty roleExecutionRefProp = jenaModel.getRDFProperty(uri_ld+"execution-entity-ref");
	
	String varIdenRolePart=elemento.getAttributeValue("identifier");
	
	Element titleRolPart=elemento.getChild("title",nsimsld);
	String varTitleRolePart="title";
	if (titleRolPart!=null) {
	 varTitleRolePart=reemplazaBlancos(titleRolPart.getText());
	}
	Element roleRef=elemento.getChild("role-ref",nsimsld);
	if (roleRef==null) System.out.println("role rf es nulo !");
	String varRoleRef=roleRef.getAttributeValue("ref");
	
	String varExecutionRef=null;
	Element executionACRef=elemento.getChild("learning-activity-ref",nsimsld);
	if (executionACRef!=null)
          	{
			varExecutionRef=executionACRef.getAttributeValue("ref");
			
			//OWLIndividual executBuscar=jenaModel.getOWLIndividual(uri_ld+varExecutionRef);
			}
	Element executionSARef=elemento.getChild("support-activity-ref",nsimsld);
	if (executionSARef!=null)
          	{
			varExecutionRef=executionSARef.getAttributeValue("ref");
			}
	// activity-structure-ref
	Element executionStrucRef=elemento.getChild("activity-structure-ref",nsimsld);
	if (executionStrucRef!=null)
          	{
			varExecutionRef=executionStrucRef.getAttributeValue("ref");
			
			}
	OWLIndividual executBuscar=jenaModel.getOWLIndividual(uri_ld+varExecutionRef);
		
	OWLIndividual rolePartIN=rolePartCL.createOWLIndividual(uri_ld+varIdenRolePart);
	rolePartIN.setPropertyValue(identifierRolePartProp,varIdenRolePart);
	rolePartIN.setPropertyValue(titleRolePartProp,varTitleRolePart);
	//
	OWLIndividual roleBuscar=jenaModel.getOWLIndividual(uri_ld+varRoleRef);
	//inserta role-ref
	rolePartIN.setPropertyValue(roleRefProp,roleBuscar);
	//inserta execution-entity-ref
	rolePartIN.setPropertyValue(roleExecutionRefProp,executBuscar);
	return(rolePartIN);
	}
		
	/**
	 * Busca un Recurso mediente su id y retorna el Element xml que lo describe 
	 * @param id
	 * @return Element
	 */
	public Element buscaUOLRecurso(String id)
	{
	Element elemento=null;
	Element resources=raiz.getChild("resources", ns);	
	List listResource =resources.getChildren("resource", ns); 
	if (listResource != null) {
	Iterator i = listResource.iterator();
     while (i.hasNext()){
            //System.out.println(" hasta aqui");
			Element nodoResource= (Element)i.next();
    	
			String idResource=nodoResource.getAttributeValue("identifier");
			//System.out.println("id-resource:"+idResource); 
			if (id.equals(idResource))
					{ // hay learning-activities
					elemento = nodoResource;
					}
						}
							}
	return(elemento);
	}
	
	
	/**
	 * Crea una instancia de UOLResource considerando diferentes tipos
	 * 
	 * @param idRef
	 * @param tipo
	 * @return OWLIndividual
	 */
	public OWLIndividual creaUOLReosurce(String idRef, String tipo)
	{
	OWLIndividual individuo=null;
	/* propiedad des comunes a UOL resource  */
	RDFProperty idResourcePROP = jenaModel.getRDFProperty(uri_uol+"identifier");
	RDFProperty typeResourcePROP = jenaModel.getRDFProperty(uri_uol+"type");
	RDFProperty metadataRefResourcePROP = jenaModel.getRDFProperty(uri_uol+"metadata-ref");
	RDFProperty hrefResourcePROP = jenaModel.getRDFProperty(uri_uol+"href");
	
	String idResource, hrefResource, typeResource;
	
	// para recursos Feedback
	if (tipo.equals("feedback"))
	{
			// al crear la clase debe hacerse con el uri_actual      en MI.owl con uri_uol
			OWLNamedClass uolFeedbackCL=jenaModel.getOWLNamedClass(uri_actual+"Feedback-Description-Resource");
			RDFProperty conditionPROP = jenaModel.getRDFProperty(uri_uol+"condition");
			RDFProperty criterionPROP = jenaModel.getRDFProperty(uri_ld+"criterion");
			RDFProperty actionReosurcePROP = jenaModel.getRDFProperty(uri_ld+"action-word");
			
			//System.out.println("id-ref:"+idRef); 
			Element elementoRecurso=buscaUOLRecurso(idRef);
			
			idResource=elementoRecurso.getAttributeValue("identifier");
			hrefResource=elementoRecurso.getAttributeValue("href");
			typeResource=elementoRecurso.getAttributeValue("type");
			
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_uol+idResource);
			if (recursoBuscar==null) {
			OWLIndividual resourceIN=uolFeedbackCL.createOWLIndividual(uri_uol+idResource);
			resourceIN.setPropertyValue(idResourcePROP,idResource);
			resourceIN.setPropertyValue(hrefResourcePROP,hrefResource);
			resourceIN.setPropertyValue(typeResourcePROP,typeResource);
			individuo =resourceIN;	
			}
	} // fin IF objective
		
	// para recursos del tipo Objective	
	if (tipo.equals("objective"))
	{
			// al crear la clase debe hacerse con el uri_actual      en MI.owl con uri_uol
			OWLNamedClass uolObjectiveReosurceCL=jenaModel.getOWLNamedClass(uri_actual+"Learning-Objective-Resource");
			RDFProperty conditionPROP = jenaModel.getRDFProperty(uri_uol+"condition");
			RDFProperty criterionPROP = jenaModel.getRDFProperty(uri_ld+"criterion");
			RDFProperty actionReosurcePROP = jenaModel.getRDFProperty(uri_ld+"action-word");
			
			//System.out.println("id-ref:"+idRef); 
			Element elementoRecurso=buscaUOLRecurso(idRef);
			
			idResource=elementoRecurso.getAttributeValue("identifier");
			hrefResource=elementoRecurso.getAttributeValue("href");
			typeResource=elementoRecurso.getAttributeValue("type");
			
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_uol+idResource);
			if (recursoBuscar==null) {
			OWLIndividual resourceIN=uolObjectiveReosurceCL.createOWLIndividual(uri_uol+idResource);
			resourceIN.setPropertyValue(idResourcePROP,idResource);
			resourceIN.setPropertyValue(hrefResourcePROP,hrefResource);
			resourceIN.setPropertyValue(typeResourcePROP,typeResource);
			individuo =resourceIN;	
			}
	} // fin IF objective
	
	// para tipo de recurso UOL prequisite 
	if (tipo.equals("prerequisite"))
	{
			// al crear la clase debe hacerse con el uri_actual      en MI.owl con uri_uol
			OWLNamedClass uolPrerequisteCL=jenaModel.getOWLNamedClass(uri_actual+"Prerequisite-Resource");
			RDFProperty conditionPROP = jenaModel.getRDFProperty(uri_uol+"condition");
			RDFProperty criterionPROP = jenaModel.getRDFProperty(uri_ld+"criterion");
						
			Element elementoRecurso=buscaUOLRecurso(idRef);
			idResource=elementoRecurso.getAttributeValue("identifier");
			hrefResource=elementoRecurso.getAttributeValue("href");
			typeResource=elementoRecurso.getAttributeValue("type");
			
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_uol+idResource);
			if (recursoBuscar==null) {
			OWLIndividual resourceIN=uolPrerequisteCL.createOWLIndividual(uri_uol+idResource);
			resourceIN.setPropertyValue(idResourcePROP,idResource);
			resourceIN.setPropertyValue(hrefResourcePROP,hrefResource);
			resourceIN.setPropertyValue(typeResourcePROP,typeResource);
			individuo =resourceIN;	
			}
	} // fin IF objective
	
		// para tipo de recurso UOL: activity description 
	if (tipo.equals("activity-description"))
	{
			// al crear la clase debe hacerse con el uri_actual      en MI.owl con uri_uol
			OWLNamedClass uolActDescCL=jenaModel.getOWLNamedClass(uri_actual+"Activity-Description-Resource");
			if (uolActDescCL==null) System.out.println("individuo es nulo"); 
			//RDFProperty conditionPROP = jenaModel.getRDFProperty(uri_uol+"condition");
			//RDFProperty criterionPROP = jenaModel.getRDFProperty(uri_ld+"criterion");
						
			Element elementoRecurso=buscaUOLRecurso(idRef);
			idResource=elementoRecurso.getAttributeValue("identifier");
			hrefResource=elementoRecurso.getAttributeValue("href");
			typeResource=elementoRecurso.getAttributeValue("type");
			
			System.out.println(" &&& identifier :"+idResource);
			
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_uol+idResource);
			if (recursoBuscar==null) {
			OWLIndividual resourceIN=uolActDescCL.createOWLIndividual(uri_uol+idResource);
			resourceIN.setPropertyValue(idResourcePROP,idResource);
			resourceIN.setPropertyValue(hrefResourcePROP,hrefResource);
			resourceIN.setPropertyValue(typeResourcePROP,typeResource);
			individuo =resourceIN;	
			}
	} // fin IF objective
	
		// para tipo de recurso UOL: activity description 
	if (tipo.equals("learning-object"))
	{
			// al crear la clase debe hacerse con el uri_actual      en MI.owl con uri_uol
			OWLNamedClass uolActDescCL=jenaModel.getOWLNamedClass(uri_actual+"Learning-Object-Resource");

			Element elementoRecurso=buscaUOLRecurso(idRef);
			idResource=elementoRecurso.getAttributeValue("identifier");
			hrefResource=elementoRecurso.getAttributeValue("href");
			typeResource=elementoRecurso.getAttributeValue("type");
			
			// verfica que el recurso UOL no exista
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_uol+idResource);
			if (recursoBuscar==null) {
			OWLIndividual resourceIN=uolActDescCL.createOWLIndividual(uri_uol+idResource);
			resourceIN.setPropertyValue(idResourcePROP,idResource);
			resourceIN.setPropertyValue(hrefResourcePROP,hrefResource);
			resourceIN.setPropertyValue(typeResourcePROP,typeResource);
			individuo =resourceIN;	
					}
	} // fin IF objective
	
	// falta el resto recursos UOL ************************************
	
	return(individuo);
	}
	
	/**
	 * Crea instancias de Prerequisitos
	 * 
	 * @param nodoRequi
	 * @param individuoIN
	 * @param propiedadPROP
	 */
	public  void insertaPrerequi(List nodoRequi,OWLIndividual individuoIN,RDFProperty propiedadPROP)
	{
	OWLNamedClass prerequisitesCL=jenaModel.getOWLNamedClass(uri_ld+"Prerequisite");
	RDFProperty idPrerequisitePROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titlePrerequiPROP = jenaModel.getRDFProperty(uri_ld+"title");
	RDFProperty isvisiblePrerequiPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty ref_PrequiPROP = jenaModel.getRDFProperty(uri_ld+"prerequisite-ref");
	RDFProperty ref_metadataPrequiPROP = jenaModel.getRDFProperty(uri_ld+"metadata-ref");
	
				
	Iterator i = nodoRequi.iterator();
	while (i.hasNext()){
		//System.out.println(" hasta aqui");
		Element Prerequi= (Element)i.next();
		//primer hijo que tenga como nombre club
		Element nodoItemPrerequi=Prerequi.getChild("item", nsimsld); 
		String varIdentPrequi=nodoItemPrerequi.getAttributeValue("identifier");
		//System.out.println(" **** ident requisito : "+varIdentPrequi);
					
		String varVisPrequi=nodoItemPrerequi.getAttributeValue("isvisible");
		String identiref=nodoItemPrerequi.getAttributeValue("identifierref");
		boolean varVisible = Boolean.parseBoolean(varVisPrequi);
		Element titlePrerequi=nodoItemPrerequi.getChild("title",nsimsld);
		String varTitlePrerequi="title";
		if (titlePrerequi!=null) {
		varTitlePrerequi=reemplazaBlancos(titlePrerequi.getText());
								}
		OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentPrequi);
		if (recursoBuscar==null) {
		OWLIndividual prerequisiteIN=prerequisitesCL.createOWLIndividual(uri_ld+varIdentPrequi);
		prerequisiteIN.setPropertyValue(idPrerequisitePROP,varIdentPrequi);
		prerequisiteIN.setPropertyValue(isvisiblePrerequiPROP, varVisible);
		prerequisiteIN.setPropertyValue(titlePrerequiPROP,varTitlePrerequi);
		prerequisiteIN.setPropertyValue(ref_PrequiPROP,creaUOLReosurce(identiref,"prerequisite"));
		// desde Learning Design
		individuoIN.addPropertyValue(propiedadPROP,prerequisiteIN); 	
				}
		} // fin while
				//}// fin if
	}
	 
	/**
	 * Crea una instancia de Learning Object
	 * 
	 * @param nodoLO
	 * @param padre
	 */
	public  void insertaLO(List nodoLO,OWLIndividual padre)
	{
	
	OWLNamedClass learningObjectCL=jenaModel.getOWLNamedClass(uri_ld+"Learning-Object");
	RDFProperty idLOPROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleLOPROP = jenaModel.getRDFProperty(uri_actual+"title");
	RDFProperty isvisibleLOPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty refLOPROP = jenaModel.getRDFProperty(uri_ld+"learning-object-ref");
	RDFProperty LOmetadataPROP = jenaModel.getRDFProperty(uri_ld+"metadata-ref");
	
	RDFProperty learningObjectRefProp = jenaModel.getRDFProperty(uri_ld+"learning-object");
	
	Iterator k=nodoLO.iterator();
	while (k.hasNext()){
		Element nodoLearningObject = (Element)k.next();
		List listaLO=nodoLearningObject.getChildren("item", nsimsld); 

		if (listaLO!= null)  // hay itemes de prequisite 
		{
		Iterator j = listaLO.iterator();
		while (j.hasNext()){
					//System.out.println(" hasta aqui");
			Element nodoItemLO= (Element)j.next();
					//primer hijo que tenga como nombre club
			String varIdentLO=nodoItemLO.getAttributeValue("identifier");
			//System.out.println(" **** ident item LO : "+varIdentLO);
			String varVisLO=nodoItemLO.getAttributeValue("isvisible");
			String identirefLO=nodoItemLO.getAttributeValue("identifierref");
			boolean varVisible = Boolean.parseBoolean(varVisLO);
			String varTitleLO="";
			Element titleLO=nodoItemLO.getChild("title",nsimsld);
			if (titleLO==null) 
			      varTitleLO="LO";
			else varTitleLO=reemplazaBlancos(titleLO.getText());
					
			OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentLO);
			if (recursoBuscar==null) {
			OWLIndividual LOIN=learningObjectCL.createOWLIndividual(uri_ld+varIdentLO);
			LOIN.setPropertyValue(idLOPROP,varIdentLO);
			LOIN.setPropertyValue(isvisibleLOPROP, varVisible);
			LOIN.setPropertyValue(titleLOPROP,varTitleLO);
			LOIN.setPropertyValue(refLOPROP,creaUOLReosurce(identirefLO,"learning-object"));
			padre.addPropertyValue(learningObjectRefProp, LOIN);
								}
			} // fin while j
	}
	} // while k
    }
		
	/**
	 * Crea instancia de Descripcion de Actividad 
	 * 
	 * @param nodoDescActivity
	 * @param padre
	 */
	public  void insertaActDescrip(Element nodoDescActivity,OWLIndividual padre)
	{
	/* recibe el nodo en el xml y el individuo padre (learning activity )  */
	
	OWLNamedClass activityDescriptionCL=jenaModel.getOWLNamedClass(uri_ld+"Activity-Description");
	RDFProperty idActDescPROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleActDescPROP = jenaModel.getRDFProperty(uri_actual+"title");
	RDFProperty isvisibleActDescPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty ref_ActDescPROP = jenaModel.getRDFProperty(uri_ld+"activity-description-ref");
	RDFProperty ref_metadataPrequiPROP = jenaModel.getRDFProperty(uri_ld+"metadata-ref");
	
	RDFProperty actDescRefProp = jenaModel.getRDFProperty(uri_ld+"activity-description");
			
	List listaItemActDesc=null;
	listaItemActDesc=nodoDescActivity.getChildren("item", nsimsld); 
			if (listaItemActDesc!= null)  // hay itemes de prequisite 
				{
				Iterator j = listaItemActDesc.iterator();
				while (j.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoItemActDesc= (Element)j.next();
					//primer hijo que tenga como nombre club
					String varIdentActDesc=nodoItemActDesc.getAttributeValue("identifier");
					//System.out.println(" **** ident activity-decription : "+varIdentActDesc);
					
					String varVisActDesc=nodoItemActDesc.getAttributeValue("isvisible");
					String identiref=nodoItemActDesc.getAttributeValue("identifierref");
					boolean varVisible = Boolean.parseBoolean(varVisActDesc);
					String varTitleActDesc="";
					Element titleActDesc=nodoItemActDesc.getChild("title",nsimsld);
					if (titleActDesc==null)
					          varTitleActDesc="description";
					else varTitleActDesc=reemplazaBlancos(titleActDesc.getText());
					//System.out.println(" **** title activity-decription : "+varTitleActDesc);
					
					OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentActDesc);
					if (recursoBuscar==null) {
					OWLIndividual actDescIN=activityDescriptionCL.createOWLIndividual(uri_ld+varIdentActDesc);
					actDescIN.setPropertyValue(idActDescPROP,varIdentActDesc);
					actDescIN.setPropertyValue(isvisibleActDescPROP, varVisible);
					actDescIN.setPropertyValue(titleActDescPROP,varTitleActDesc);
					actDescIN.setPropertyValue(ref_ActDescPROP,creaUOLReosurce(identiref,"activity-description"));
					padre.setPropertyValue(actDescRefProp, actDescIN);
											}
					} // fin while
				}// fin if
	}
	
	

	/**
	 * Crea instancia de Environment
	 * 
	 * @param nodoEnvironments
	 * @param learningDesignIN
	 * @param learningDesignPROP
	 */
	public  void creaEnvironments(Element nodoEnvironments,OWLIndividual learningDesignIN,RDFProperty learningDesignPROP)
	{
	OWLNamedClass environmentCL=jenaModel.getOWLNamedClass(uri_ld+"Environment");
	//OWLNamedClass supportActivityCL=jenaModel.getOWLNamedClass(uri_ld+"Support-Activity");
	RDFProperty idEnvironmentProp = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleEnvironmentProp = jenaModel.getRDFProperty(uri_actual+"title");
	RDFProperty metadataRefProp = jenaModel.getRDFProperty(uri_ld+"metada-ref");
	//RDFProperty learningObjectRefProp = jenaModel.getRDFProperty(uri_ld+"learning-object");
	RDFProperty serviceRefProp = jenaModel.getRDFProperty(uri_ld+"service-ref");
						
	List listaEnviroments=nodoEnvironments.getChildren("environment", nsimsld); 
	if (listaEnviroments!= null)  // hay environments
		{
		 i = listaEnviroments.iterator();
		while (i.hasNext()){
			//System.out.println(" hasta aqui");
			Element nodoEnvironment= (Element)i.next();
				
			String varIdentEnvironment=nodoEnvironment.getAttributeValue("identifier");
			// String varVisLearActivity=nodoLearActivity.getAttributeValue("isvisible");
			// boolean varVisible = Boolean.parseBoolean(varVisLearActivity);
			Element titleEnvironment=nodoEnvironment.getChild("title",nsimsld);
			String varTitleEnvironment=reemplazaBlancos(titleEnvironment.getText());
			
			OWLIndividual environmentIN=environmentCL.createOWLIndividual(uri_ld+varIdentEnvironment);
			environmentIN.setPropertyValue(idEnvironmentProp,varIdentEnvironment);
			environmentIN.setPropertyValue(titleEnvironmentProp,varTitleEnvironment);
			
			// desde Learning Design
			learningDesignIN.addPropertyValue(learningDesignPROP,environmentIN);
			//learActivityIN.setPropertyValue(titleLearActivityProp,varTitleLearActivity);
			//System.out.println(" environment  : "+varIdentEnvironment);
			 
			// extrae  learning objects
			List listaLO=nodoEnvironment.getChildren("learning-object", nsimsld); 
			if (listaLO== null) System.out.println(" lista de learning object es nula ");
			if (listaLO!= null)  // hay environments
				{
				insertaLO(listaLO,environmentIN);
				}// if
			
			}// while
		}// if
	}
	
	
	
	/**
	 * Asocia un acto con "when-last-act-completed"
	 * 
	 * @param nodoWhenLastAct
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaWhenActCompleted(Element nodoWhenLastAct)
	/* para implementar WHEN-LAST-ACT-COMPLETED. La herramienta Recourse no 
	permite indicar referencias a ACTs */
	{
	ident++;
	String varIdent="itemPlay"+ident;
	OWLIndividual onCompletionUnitIN=null;
	if (nodoWhenLastAct!=null) {
	OWLNamedClass onCompletionUnitCL=jenaModel.getOWLNamedClass(uri_ld+"On-Completion-Unit");
	onCompletionUnitIN=onCompletionUnitCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenCompletionUnitProp = jenaModel.getRDFProperty(uri_actual+"complete-unit-of-learning");
	onCompletionUnitIN.setPropertyValue(whenCompletionUnitProp,"when-last-act-completed");
	}
	return(onCompletionUnitIN);
	}
	
	
	/**
	 * Agrega "user-choice" al completar un acto
	 * 
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaOnCompletionUnitAct()
	// agrega "user-choice" cuando no esta el nodo "complete-act"
	{
	ident++;
	String varIdent="itemAct"+ident;
	OWLIndividual onCompletionActIN=null;
	OWLNamedClass onCompletionUnitCL=jenaModel.getOWLNamedClass(uri_ld+"On-Completion-Unit");
	onCompletionActIN=onCompletionUnitCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenCompletionUnitProp = jenaModel.getRDFProperty(uri_actual+"complete-unit-of-learning");
	onCompletionActIN.setPropertyValue(whenCompletionUnitProp,"user-choice");
	return(onCompletionActIN);
	}
	
	/**
	 * Agrega "user-choice" al completar un Play
	 * 
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaOnCompletionUnitPLAY()
	// 
	{
	ident++;
	String varIdent="itemPlay"+ident;
	OWLIndividual onCompletionPlayIN=null;
	OWLNamedClass onCompletionUnitCL=jenaModel.getOWLNamedClass(uri_ld+"On-Completion-Unit");
	onCompletionPlayIN=onCompletionUnitCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenCompletionUnitProp = jenaModel.getRDFProperty(uri_actual+"complete-unit-of-learning");
	onCompletionPlayIN.setPropertyValue(whenCompletionUnitProp,"user-choice");
	return(onCompletionPlayIN);
	}
	
	
	
	/**
	 * Agrega "user-choice" al completar el UoL
	 * 
	 * @param nodoWhenLastAct
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaOnCompletionUnit(Element nodoWhenLastAct)
	{
	ident++;
	String varIdent="itemActivy"+ident;
	OWLIndividual onCompletionWhenLastActIN=null;
	if (nodoWhenLastAct!=null) {
	OWLNamedClass onCompletionUnitCL=jenaModel.getOWLNamedClass(uri_ld+"On-Completion-Unit");
	onCompletionWhenLastActIN=onCompletionUnitCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenCompletionUnitProp = jenaModel.getRDFProperty(uri_actual+"complete-unit-of-learning");
	onCompletionWhenLastActIN.setPropertyValue(whenCompletionUnitProp,"user-choice");
	}
	return(onCompletionWhenLastActIN);
	}
	
	/**
	 * Agrega "On-Completion-Unit" a la descripcion de Feedback
	 * 
	 * @param nodoFeedback
	 * @param individuoIN
	 * @param propiedadPROP
	 */
	public  void creaOnCompletionFeedback(Element nodoFeedback,OWLIndividual individuoIN, RDFProperty propiedadPROP)
	{
	ident++;
	String varIdent="itemFeedback"+ident;
	OWLIndividual onCompletionFeedbIN=null;
	OWLNamedClass onCompletionFeedbCL=jenaModel.getOWLNamedClass(uri_ld+"On-Completion-Unit");
	onCompletionFeedbIN=onCompletionFeedbCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty feedbackDescriptionProp = jenaModel.getRDFProperty(uri_ld+"feedback-description");
	// desde el padre
	individuoIN.addPropertyValue(propiedadPROP,onCompletionFeedbIN);
	//System.out.println(" crea on completion");
	creaFeedBackDescription(nodoFeedback, onCompletionFeedbIN, feedbackDescriptionProp);
	}
		
	
	
	/**
	 * Crea instancia de "when-role-part-completed" al completar un Act
	 * 
	 * @param listaWhen
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaWhenRolePartCompleted(List listaWhen)
	{
	ident++;
	String varIdent="item"+ident;
	OWLNamedClass completeActCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Act");
	OWLIndividual completeActIN=completeActCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenRoleRefProp = jenaModel.getRDFProperty(uri_ld+"when-role-part-completed");
	
	Iterator j = listaWhen.iterator();
		while (j.hasNext()){
					//System.out.println(" hasta aqui");
			Element nodoWhen= (Element)j.next();
					//primer hijo que tenga como nombre club
			String varRolePartRef=nodoWhen.getAttributeValue("ref");
			//System.out.println(" ref role part: "+varRolePartRef);
			OWLIndividual rolePartBuscarIN=jenaModel.getOWLIndividual(uri_ld+varRolePartRef);			
			completeActIN.addPropertyValue(whenRoleRefProp,rolePartBuscarIN);
			}
	OWLIndividual individuoIN=completeActIN;
	return(individuoIN);
	}
	
	/**
	 * Relaciona un Item con instancia "WhenPlayCompleted"
	 * 
	 * @param listaWhen
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaWhenPlayCompleted(List listaWhen)
	{
	ident++;
	String varIdent="itemPlay"+ident;
	OWLNamedClass whenCompletePlayCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Method");
	OWLIndividual whenCompletePlayIN=whenCompletePlayCL.createOWLIndividual(uri_ld+varIdent);
	RDFProperty whenPlayRefProp = jenaModel.getRDFProperty(uri_ld+"when-play-completed");
	
	Iterator j = listaWhen.iterator();
		while (j.hasNext()){
					//System.out.println(" hasta aqui");
			Element nodoWhen= (Element)j.next();
					//primer hijo que tenga como nombre club
			String varPlayRef=nodoWhen.getAttributeValue("ref");
			//System.out.println(" ref play: "+varPlayRef);
			OWLIndividual playBuscarIN=jenaModel.getOWLIndividual(uri_ld+varPlayRef);			
			whenCompletePlayIN.addPropertyValue(whenPlayRefProp,playBuscarIN);
			}
	OWLIndividual individuoIN=whenCompletePlayIN;
	return(individuoIN);
	}
	
	
	
	/**
	 * Crea instancia de tiempo limite para Method, Play, Act y  Activity
	 * 
	 * @param nodoTimelimit
	 * @param unidad
	 * @return OWLIndividual
	 */
	public  OWLIndividual creaTiempoLimite(Element nodoTimelimit, String unidad)
	{
	// crea el time limit segun tipo de unidad
	
	RDFProperty timeLimitProp = jenaModel.getRDFProperty(uri_ld+"time-limit");
	OWLIndividual tiempoLimiteIN=null;
	ident++; // aumenta el identificador
	String variableIdent="element"+ident;
	String varTimeLimit=reemplazaBlancos(nodoTimelimit.getText());
	
	if (unidad.equals("activity"))
	{
	OWLNamedClass completeActivityCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Activity");
	OWLIndividual completeActivityIN=completeActivityCL.createOWLIndividual(uri_ld+variableIdent);
	completeActivityIN.setPropertyValue(timeLimitProp,varTimeLimit);
	tiempoLimiteIN=completeActivityIN;
	} // fin activity
	
	if (unidad.equals("act"))
	{
	OWLNamedClass completeActCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Act");
	OWLIndividual completeActIN=completeActCL.createOWLIndividual(uri_ld+variableIdent);
	completeActIN.setPropertyValue(timeLimitProp,varTimeLimit);
	tiempoLimiteIN=completeActIN;
	} // fin act
		
	if (unidad.equals("play"))
	{
	OWLNamedClass completePlayCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Play");
	OWLIndividual completePlayIN=completePlayCL.createOWLIndividual(uri_ld+variableIdent);
	completePlayIN.setPropertyValue(timeLimitProp,varTimeLimit);
	tiempoLimiteIN=completePlayIN;
	} // fin act
	
	if (unidad.equals("method"))
	{
	OWLNamedClass completeMethodCL=jenaModel.getOWLNamedClass(uri_ld+"Complete-Method");
	OWLIndividual completeMethodIN=completeMethodCL.createOWLIndividual(uri_ld+variableIdent);
	completeMethodIN.setPropertyValue(timeLimitProp,varTimeLimit);
	tiempoLimiteIN=completeMethodIN;
	} // fin act
		
	return(tiempoLimiteIN);		
	}
		
	/**
	 * Inserta relacion a Environment
	 * 
	 * @param listaEnv
	 * @param individuoIN
	 * @param propiedadProp
	 */
	public  void insertaEnvironmentRef(List listaEnv,OWLIndividual individuoIN,RDFProperty propiedadProp)
	{
	Iterator j = listaEnv.iterator();
		while (j.hasNext()){
			Element nodoEnv= (Element)j.next();
			String varEnvRef=nodoEnv.getAttributeValue("ref");
			//System.out.println(" ref play: "+varPlayRef);
			OWLIndividual refBuscarIN=jenaModel.getOWLIndividual(uri_ld+varEnvRef);
			individuoIN.addPropertyValue(propiedadProp,refBuscarIN);
			}
	}
		
	/**
	 * Crea instancia de Feedback Description
	 * 
	 * @param nodoFeedback
	 * @param individuoIN
	 * @param propiedadProp
	 */
	public  void creaFeedBackDescription(Element nodoFeedback,OWLIndividual individuoIN,RDFProperty propiedadProp)
	{
	OWLNamedClass feedbackCL=jenaModel.getOWLNamedClass(uri_ld+"Feedback-Description");
	RDFProperty idFeedbackPROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleFeedbackPROP = jenaModel.getRDFProperty(uri_ld+"title");
	RDFProperty isvisibleFeedbackPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty ref_FeedbackPROP = jenaModel.getRDFProperty(uri_ld+"feedback-description-ref");
	
	List listaItemFeedb=null;
	listaItemFeedb=nodoFeedback.getChildren("item", nsimsld); 
	if (listaItemFeedb!= null)  // hay learning-activities
			{
			Iterator i = listaItemFeedb.iterator();
			while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoItemFeedb= (Element)i.next();
					//primer hijo que tenga como nombre club
					String varIdentFeedb=nodoItemFeedb.getAttributeValue("identifier");
					String varVisFeedb=nodoItemFeedb.getAttributeValue("isvisible");
					String identiref=nodoItemFeedb.getAttributeValue("identifierref");
					boolean varVisible = Boolean.parseBoolean(varVisFeedb);
					Element titleItemFeedb=nodoItemFeedb.getChild("title",nsimsld);
					String varTitleFeedb="";
					if (titleItemFeedb==null)
					    varTitleFeedb="title";
					else
						varTitleFeedb=reemplazaBlancos(titleItemFeedb.getText());
					
					OWLIndividual feedBackIN=feedbackCL.createOWLIndividual(uri_ld+varIdentFeedb);
					feedBackIN.setPropertyValue(idFeedbackPROP,varIdentFeedb);
					feedBackIN.setPropertyValue(isvisibleFeedbackPROP, varVisible);
					feedBackIN.setPropertyValue(titleFeedbackPROP,varTitleFeedb);
					feedBackIN.setPropertyValue(ref_FeedbackPROP,creaUOLReosurce(identiref,"feedback"));
					//desde padre
					individuoIN.addPropertyValue(propiedadProp,feedBackIN);	
					} // fin while
				
				}// fin if
	}
			
	/**
	 * Crea instancia de Learning Objective de Actividades
	 * 
	 * @param nodoLearningObjectives
	 * @param individuoIN
	 * @param propiedadProp
	 * @param objectiveActKnowledgeProp
	 */
	public  void creaLearningObjectiveLActivity(Element nodoLearningObjectives,OWLIndividual individuoIN,RDFProperty propiedadProp,RDFProperty objectiveActKnowledgeProp)
	{
	OWLNamedClass learningObjectivesCL=jenaModel.getOWLNamedClass(uri_ld+"Learning-Objective");
	RDFProperty idLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"title");
	RDFProperty isvisibleLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty ref_LearningObjectivePROP = jenaModel.getRDFProperty(uri_ld+"learning-objective-ref");
	
	List listaItemLObjectives=null;
	listaItemLObjectives=nodoLearningObjectives.getChildren("item", nsimsld); 
	if (listaItemLObjectives!= null)  // hay learning-activities
			{
			Iterator i = listaItemLObjectives.iterator();
			while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoItemLObjectives= (Element)i.next();
					//primer hijo que tenga como nombre club
					String varIdentLearObjectives=nodoItemLObjectives.getAttributeValue("identifier");
					String varVisLearObjectives=nodoItemLObjectives.getAttributeValue("isvisible");
					String identiref=nodoItemLObjectives.getAttributeValue("identifierref");
					boolean varVisible = Boolean.parseBoolean(varVisLearObjectives);
					Element titleLearningObjectives=nodoItemLObjectives.getChild("title",nsimsld);
					String varTitleLearObjectives="";
					if (titleLearningObjectives==null)
					    varTitleLearObjectives="title";
					else
						varTitleLearObjectives=reemplazaBlancos(titleLearningObjectives.getText());
					
					OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentLearObjectives);
					
					/* busca KnowledgeItem */
					OWLIndividual knowledgeBuscar=jenaModel.getOWLIndividual(uri_actual+varTitleLearObjectives); 
										
					if (recursoBuscar==null) {
					OWLIndividual learObjectivesIN=learningObjectivesCL.createOWLIndividual(uri_ld+varIdentLearObjectives);
					learObjectivesIN.setPropertyValue(idLearningObjectivesPROP,varIdentLearObjectives);
					learObjectivesIN.setPropertyValue(isvisibleLearningObjectivesPROP, varVisible);
					learObjectivesIN.setPropertyValue(titleLearningObjectivesPROP,varTitleLearObjectives);
					learObjectivesIN.setPropertyValue(ref_LearningObjectivePROP,creaUOLReosurce(identiref,"objective"));
					//desde Learning Design
					individuoIN.addPropertyValue(propiedadProp,learObjectivesIN);	
									}
					/* relaciona learningActivity con  concept-learning-objectives  */ 
					if (knowledgeBuscar != null) {
					individuoIN.setPropertyValue(objectiveActKnowledgeProp,knowledgeBuscar);	
					}
			        
					} // fin while
									
				}// fin if
	}
	
	/**
	 * Crea instancia de Learning Objective
	 * 
	 * @param nodoLearningObjectives
	 * @param individuoIN
	 * @param propiedadProp
	 */
	public  void creaLearningObjective(Element nodoLearningObjectives,OWLIndividual individuoIN,RDFProperty propiedadProp)
	{
	OWLNamedClass learningObjectivesCL=jenaModel.getOWLNamedClass(uri_ld+"Learning-Objective");
	RDFProperty idLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"identifier");
	RDFProperty titleLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"title");
	RDFProperty isvisibleLearningObjectivesPROP = jenaModel.getRDFProperty(uri_ld+"isvisible");
	RDFProperty ref_LearningObjectivePROP = jenaModel.getRDFProperty(uri_ld+"learning-objective-ref");
	
	List listaItemLObjectives=null;
	listaItemLObjectives=nodoLearningObjectives.getChildren("item", nsimsld); 
	if (listaItemLObjectives!= null)  // hay learning-activities
			{
			Iterator i = listaItemLObjectives.iterator();
			while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoItemLObjectives= (Element)i.next();
					//primer hijo que tenga como nombre club
					String varIdentLearObjectives=nodoItemLObjectives.getAttributeValue("identifier");
					String varVisLearObjectives=nodoItemLObjectives.getAttributeValue("isvisible");
					String identiref=nodoItemLObjectives.getAttributeValue("identifierref");
					boolean varVisible = Boolean.parseBoolean(varVisLearObjectives);
					Element titleLearningObjectives=nodoItemLObjectives.getChild("title",nsimsld);
					String varTitleLearObjectives="";
					if (titleLearningObjectives==null)
					    varTitleLearObjectives="title";
					else
						varTitleLearObjectives=reemplazaBlancos(titleLearningObjectives.getText());
					
					OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentLearObjectives);
					
					/* busca KnowledgeItem */
					//OWLIndividual knowledgeBuscar=jenaModel.getOWLIndividual(uri_actual+varTitleLearObjectives); 
										
					if (recursoBuscar==null) {
					OWLIndividual learObjectivesIN=learningObjectivesCL.createOWLIndividual(uri_ld+varIdentLearObjectives);
					learObjectivesIN.setPropertyValue(idLearningObjectivesPROP,varIdentLearObjectives);
					learObjectivesIN.setPropertyValue(isvisibleLearningObjectivesPROP, varVisible);
					learObjectivesIN.setPropertyValue(titleLearningObjectivesPROP,varTitleLearObjectives);
					learObjectivesIN.setPropertyValue(ref_LearningObjectivePROP,creaUOLReosurce(identiref,"objective"));
					//desde Learning Design
					individuoIN.addPropertyValue(propiedadProp,learObjectivesIN);	
									}
					/* relaciona learningActivity con  concept-learning-objectives  */ 
					//if (knowledgeBuscar != null) {
					//individuoIN.setPropertyValue(objectiveActKnowledgeProp,knowledgeBuscar);	
					//}
			        
					} // fin while
									
				}// fin if
	}	
  
   
   /**
 * Lee archivo "imsmanifest.xml" y genera las intancias en la ontologia para el metodo y 
 * ontologia de dominio seleccionada
 * 
 * @param archivo
 * @param metodoSeleccionado
 * @param ontologiaSeleccionada
 */
public  void leeManifest(String archivo, String metodoSeleccionado, String ontologiaSeleccionada)
   {
     try {
        
		//String archivo="imsmanifest.xml"; /* archivo manifest de ims-ld */
		 asignaUri();  /* asigna las uRIs de las ontologias */
		
		/* carga la ontologia en un JenaOWLModel  */
		jenaModel=cargaOntologias(ontologia,directorioRep);//$NON-NLS-1$
	     if (jenaModel ==null) 
	        { System.out.println("jena Model es nulo!"); }
				
		// borra instancias anteriores de Method
		borraInstancias(uri_ld+"Method");
		borraInstancias(uri_ld+"Learning-Design");
		borraInstancias(uri_ld+"Play");
		borraInstancias(uri_ld+"Act");
		borraInstancias(uri_ld+"Role");
		borraInstancias(uri_ld+"Role-Part");
		borraInstancias(uri_ld+"Activity");
		borraInstancias(uri_ld+"Activity-Structure");
		borraInstancias(uri_ld+"Environment");
				
		// inicia JDOM
		SAXBuilder builder=new SAXBuilder(false); 
        Document doc=builder.build(archivo);       
        raiz=doc.getRootElement();
        System.out.println("ID Manifiesto:"+ raiz.getAttributeValue("identifier"));//$NON-NLS-1$
		Namespace ns= raiz.getNamespace(); //nombre de espacio principal
		Namespace ns2=null;
		Namespace nsimsld=ns2.getNamespace("http://www.imsglobal.org/xsd/imsld_v1p0");//nombre de espacio imsld
				
		Element organizations=raiz.getChild("organizations", ns);
		Element resources=raiz.getChild("resources",ns);
		
		/* organizations   */
	   if (organizations == null)
		            System.out.println("organizations es nulo");
		Element learningDesign=organizations.getChild("learning-design", nsimsld);         
        //System.out.println("hola2");
		if (learningDesign == null)
		            System.out.println("learningDesign es nulo");
				
		Element nombreLD=learningDesign.getChild("title",nsimsld);
					
		/* variables */
		String varIdent=learningDesign.getAttributeValue("identifier");
		String varSeq=learningDesign.getAttributeValue("sequence-used");
		boolean varSequence = Boolean.parseBoolean(varSeq);
		String varLevel=learningDesign.getAttributeValue("level");
		String varTitle=reemplazaBlancos(nombreLD.getText());
		String varVersion=learningDesign.getAttributeValue("version");
	 	String var= uri_ld + varIdent;
		URI varUri=null;
		//varUri= new URI(var);
		//if (varUri != null) System.out.println(" Uri NO es nulo ");
		
			     
	   /* instancia Learning-Design  */
	   OWLNamedClass LearningDesignCL=jenaModel.getOWLNamedClass(uri_ld+"Learning-Design");
	   if (LearningDesignCL==null) 
	        { System.out.println("clase ld es nulo!"); }
	   OWLIndividual LearningDesignIN=LearningDesignCL.createOWLIndividual(uri_ld+varIdent);
	   /* propiedades de Learning-Design */
	   RDFProperty seqProp = jenaModel.getRDFProperty(uri_ld+"sequence-used");
	   RDFProperty idProp = jenaModel.getRDFProperty(uri_ld+"identifier");
	   RDFProperty levelProp = jenaModel.getRDFProperty(uri_ld+"level");
	   RDFProperty titleProp = jenaModel.getRDFProperty(uri_actual+"title");
	   RDFProperty uriProp = jenaModel.getRDFProperty(uri_ld+"uri");
	   RDFProperty versionProp = jenaModel.getRDFProperty(uri_ld+"version");
	   RDFProperty prerequisiteRefProp = jenaModel.getRDFProperty(uri_ld+"prerequisite");
	   RDFProperty componentRefProp = jenaModel.getRDFProperty(uri_ld+"component-ref");
	   RDFProperty learningObjectiveRefProp = jenaModel.getRDFProperty(uri_ld+"learning-objective");
	   
	   /* agrega los valores a propiedades de Learning_design */
	   LearningDesignIN.setPropertyValue(idProp,varIdent);
	   LearningDesignIN.setPropertyValue(levelProp,varLevel);
	   LearningDesignIN.setPropertyValue(titleProp,varTitle);
	   LearningDesignIN.setPropertyValue(seqProp,varSequence);
	   //LearningDesignIN.setPropertyValue(uriProp,(URI)varUri);
	   LearningDesignIN.setPropertyValue(versionProp,varVersion);		   
	   
	   
	   Element nodoComponents=learningDesign.getChild("components", nsimsld); 
	   
		
	// inserta environment	
		Element nodoEnvironments=nodoComponents.getChild("environments", nsimsld); 
		//if (nodoEnvironments==null) { System.out.println("no hay environments"); }
		if (nodoEnvironments!=null)
				creaEnvironments(nodoEnvironments,LearningDesignIN,componentRefProp);


	 // Inserta Learning Activities
			
			//OWLNamedClass learningActivityCL=jenaModel.getOWLNamedClass(uri_ld+"Learning-Activity");
			OWLNamedClass learningActivityCL=jenaModel.getOWLNamedClass(uri_actual+"ConceptLearningActivity");
			
			OWLNamedClass supportActivityCL=jenaModel.getOWLNamedClass(uri_ld+"Support-Activity");
			OWLNamedClass activityStructureCL=jenaModel.getOWLNamedClass(uri_ld+"Activity-Structure");
			
			RDFProperty idLearActivityProp = jenaModel.getRDFProperty(uri_ld+"identifier");
			RDFProperty titleLearActivityProp = jenaModel.getRDFProperty(uri_actual+"title");
			RDFProperty isvisibleLearActivityProp = jenaModel.getRDFProperty(uri_ld+"isvisible");
			RDFProperty onCompletionRefProp = jenaModel.getRDFProperty(uri_ld+"on-completion-ref");
			RDFProperty completeActivityRefProp = jenaModel.getRDFProperty(uri_ld+"complete-activity-ref");
			RDFProperty objectiveActRefProp = jenaModel.getRDFProperty(uri_ld+"learning-objective");
			RDFProperty objectiveActKnowledgeProp = jenaModel.getRDFProperty(uri_actual+"concept-learning-objective");
			
			
			// propiedades de Activity Strcuture
			RDFProperty executionOrderProp = jenaModel.getRDFProperty(uri_ld+"execution-order");
			RDFProperty idStuctureProp = jenaModel.getRDFProperty(uri_ld+"identifier");
			RDFProperty numberToSelectProp = jenaModel.getRDFProperty(uri_ld+"number-to-select");
			RDFProperty sortStrucProp = jenaModel.getRDFProperty(uri_ld+"sort");
			RDFProperty structureTypeProp = jenaModel.getRDFProperty(uri_ld+"structure-type");
			RDFProperty informationProp = jenaModel.getRDFProperty(uri_ld+"information");	
			RDFProperty structureMetadataRefProp = jenaModel.getRDFProperty(uri_ld+"metadata-ref");
			RDFProperty structEnvironmentRefProp = jenaModel.getRDFProperty(uri_ld+"environment-ref");
			RDFProperty executionEntityRefProp = jenaModel.getRDFProperty(uri_ld+"execution-entity-ref");
			RDFProperty prerequisiteProp = jenaModel.getRDFProperty(uri_ld+"prerequisite");			
			//RDFProperty actDescRefProp = jenaModel.getRDFProperty(uri_ld+"activity-description");
						
			Element listaActivities=nodoComponents.getChild("activities", nsimsld); 
			List listaLearActivities=listaActivities.getChildren("learning-activity", nsimsld); 
			if (listaLearActivities!= null)  // hay learning-activities
				{
				 i = listaLearActivities.iterator();
				while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoLearActivity= (Element)i.next();
					//primer hijo que tenga como nombre club
					
					String varIdentLearActivity=nodoLearActivity.getAttributeValue("identifier");
					String varVisLearActivity=nodoLearActivity.getAttributeValue("isvisible");
					boolean varVisible = Boolean.parseBoolean(varVisLearActivity);
					Element titleLearActivity=nodoLearActivity.getChild("title",nsimsld);
					String varTitleLearActivity=reemplazaBlancos(titleLearActivity.getText());
					OWLIndividual learActivityIN=learningActivityCL.createOWLIndividual(uri_ld+varIdentLearActivity);
					allDifferent.addDistinctMember(learActivityIN);
                    learActivityIN.setPropertyValue(idLearActivityProp,varIdentLearActivity);
					learActivityIN.setPropertyValue(isvisibleLearActivityProp,varVisible);
					learActivityIN.setPropertyValue(titleLearActivityProp,varTitleLearActivity);
					// desde Learning Design a Activities
					LearningDesignIN.addPropertyValue(componentRefProp,learActivityIN); 
					Element nodoActivityDesc=nodoLearActivity.getChild("activity-description",nsimsld);
					if (nodoActivityDesc!=null)
							{
							//System.out.println(" entro a idescriocion ");
							insertaActDescrip(nodoActivityDesc, learActivityIN );
							}
					
					// complete-activity   user choice
					Element nodoCompleteActivity=nodoLearActivity.getChild("complete-activity",nsimsld);
					if (nodoCompleteActivity!=null)
						{
						Element nodoUserChoice=nodoCompleteActivity.getChild("user-choice",nsimsld);
						if (nodoUserChoice!=null)
							{
							learActivityIN.setPropertyValue(onCompletionRefProp,creaOnCompletionUnit(nodoUserChoice));
							}
						Element nodoTimeLimit=nodoCompleteActivity.getChild("time-limit",nsimsld);
						if (nodoTimeLimit!=null)
							{
							learActivityIN.setPropertyValue(completeActivityRefProp,creaTiempoLimite(nodoTimeLimit,"activity"));
							}
						}
					// inserta environment
					List nodoActEnv=nodoLearActivity.getChildren("environment-ref",nsimsld);
					if (nodoActEnv!=null)
							{
							insertaEnvironmentRef(nodoActEnv,learActivityIN,structEnvironmentRefProp); 
							}									
					// inserta prerequisite
					List nodoPrerequi=nodoLearActivity.getChildren("prerequisites",nsimsld);
					if (nodoPrerequi!=null)
						{
						//System.out.println(" si tiene prerequisito ");
						insertaPrerequi(nodoPrerequi,learActivityIN,prerequisiteProp);
						}
					// inserta LEARNING OBJECTIVE
					Element nodoObjective=nodoLearActivity.getChild("learning-objectives",nsimsld);
					if (nodoObjective!=null)
							{
							/* crea enlace a item de objetivos-recursos UOL y KnowledgeItem*/
						    creaLearningObjectiveLActivity(nodoObjective,learActivityIN,objectiveActRefProp,objectiveActKnowledgeProp); 												
							
							}									
					// inserta FEEDBACK DESCRIPTION
					Element nodoOnCompletion=nodoLearActivity.getChild("on-completion",nsimsld);
					if (nodoOnCompletion!=null)
							{
							Element nodoFeedBack=nodoOnCompletion.getChild("feedback-description",nsimsld);
							if (nodoFeedBack!=null) 
								{
								//System.out.println(" si hay nodo feedback");
								creaOnCompletionFeedback(nodoFeedBack,learActivityIN,onCompletionRefProp);
								}
							}					
				}
			}
			
			// Inserta Support Activities
			List listaSupActivities=listaActivities.getChildren("support-activity", nsimsld); 
			if (listaSupActivities!= null)  // hay learning-support
				{
				i = listaSupActivities.iterator();
				while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoLearActivity= (Element)i.next();
					//primer hijo que tenga como nombre club
					
					String varIdentLearActivity=nodoLearActivity.getAttributeValue("identifier");
					String varVisLearActivity=nodoLearActivity.getAttributeValue("isvisible");
					boolean varVisible = Boolean.parseBoolean(varVisLearActivity);
					Element titleLearActivity=nodoLearActivity.getChild("title",nsimsld);
					String varTitleLearActivity=reemplazaBlancos(titleLearActivity.getText());
					OWLIndividual learActivityIN=supportActivityCL.createOWLIndividual(uri_ld+varIdentLearActivity);
					allDifferent.addDistinctMember(learActivityIN);
					learActivityIN.setPropertyValue(idLearActivityProp,varIdentLearActivity);
					learActivityIN.setPropertyValue(isvisibleLearActivityProp,varVisible);
					learActivityIN.setPropertyValue(titleLearActivityProp,varTitleLearActivity);
					// desde Learning Design
					LearningDesignIN.addPropertyValue(componentRefProp,learActivityIN); 
					Element nodoActivityDesc=nodoLearActivity.getChild("activity-description",nsimsld);
					if (nodoActivityDesc!=null)
							{
							//System.out.println(" entro a desccripcion de Support activity");
							insertaActDescrip(nodoActivityDesc, learActivityIN );
							}
					
					// complete-activity   user choice
					Element nodoCompleteActivity=nodoLearActivity.getChild("complete-activity",nsimsld);
					if (nodoCompleteActivity!=null)
						{
						Element nodoUserChoice=nodoCompleteActivity.getChild("user-choice",nsimsld);
						if (nodoUserChoice!=null)
							{
							learActivityIN.setPropertyValue(onCompletionRefProp,creaOnCompletionUnit(nodoUserChoice));
							}
						Element nodoTimeLimit=nodoCompleteActivity.getChild("time-limit",nsimsld);
						if (nodoTimeLimit!=null)
							{
							learActivityIN.setPropertyValue(completeActivityRefProp,creaTiempoLimite(nodoTimeLimit,"activity"));
							}
						}
					// inserta environment
					List nodoActEnv=nodoLearActivity.getChildren("environment-ref",nsimsld);
					if (nodoActEnv!=null)
							{
							insertaEnvironmentRef(nodoActEnv,learActivityIN,structEnvironmentRefProp); 
							}
					
					}
				}
		// activity structure
		List listaActivityStructure=listaActivities.getChildren("activity-structure", nsimsld); 
			if (listaActivityStructure!= null)  // hay activity structure
				{
				int cont_act=0;
				i = listaActivityStructure.iterator();
				while (i.hasNext()){
					//System.out.println(" hasta aqui");
					Element nodoActivityStruct= (Element)i.next();
					//primer hijo que tenga como nombre club
					cont_act=0;
					String varStrucActivity=nodoActivityStruct.getAttributeValue("identifier");
					String varSortActivity=nodoActivityStruct.getAttributeValue("sort");
					String varStrucType=nodoActivityStruct.getAttributeValue("structure-type");
					Element titleStrucActivity=nodoActivityStruct.getChild("title",nsimsld);
					String varTitleLearActivity=reemplazaBlancos(titleStrucActivity.getText());
					String varStringNumberToSelect=nodoActivityStruct.getAttributeValue("number-to-select)");
					int varNumberToSelect=0;
					if (varStringNumberToSelect!=null) {
						varNumberToSelect=Integer.parseInt(varStringNumberToSelect);
										}
					
					OWLIndividual activityStructureIN=activityStructureCL.createOWLIndividual(uri_ld+varStrucActivity);
					activityStructureIN.setPropertyValue(idStuctureProp,varStrucActivity);
					activityStructureIN.setPropertyValue(sortStrucProp,varSortActivity);
					activityStructureIN.setPropertyValue(structureTypeProp,varStrucType);
					activityStructureIN.setPropertyValue(titleLearActivityProp,varTitleLearActivity);
					activityStructureIN.setPropertyValue(numberToSelectProp,varNumberToSelect);		
					
					// desde Learning Design
					LearningDesignIN.addPropertyValue(componentRefProp,activityStructureIN); 
										
					List activityStrucRef=nodoActivityStruct.getChildren("learning-activity-ref",nsimsld);
					//int cont_act=0;
					if (activityStrucRef!= null)  // hay activity structure
						{
						Iterator j = activityStrucRef.iterator();
						while (j.hasNext()){
						//System.out.println(" hasta aqui");
						Element nodoActivityRef= (Element)j.next();
						//primer hijo 
						String varStrucActivityRef=nodoActivityRef.getAttributeValue("ref");
						OWLIndividual activityBuscarIN=jenaModel.getOWLIndividual(uri_ld+varStrucActivityRef);
						
						/* agrega orden de secuencia "execution-order" a cada actividad de la estrcutura */
						activityBuscarIN.setPropertyValue(executionOrderProp,cont_act);
						cont_act++;
						/* conecta estructura de actividades con Actividades */
						activityStructureIN.addPropertyValue(executionEntityRefProp,activityBuscarIN);
						                }
						}
					// inserta environment
					List nodoActEnv=nodoActivityStruct.getChildren("environment-ref",nsimsld);
					if (nodoActEnv!=null)
							{
							insertaEnvironmentRef(nodoActEnv,activityStructureIN,structEnvironmentRefProp); 
							}
					// INSERTA SUPPORT ACIVITY
					List nodoSuppActivity=nodoActivityStruct.getChildren("support-activity-ref",nsimsld);
					if (nodoSuppActivity!=null)
						{
						Iterator j = nodoSuppActivity.iterator();
						while (j.hasNext()){
						//System.out.println(" hasta aqui");
						Element nodoSuppActivityRef= (Element)j.next();
						//primer hijo que tenga como nombre club
						String varStrucActivityRef=nodoSuppActivityRef.getAttributeValue("ref");
						OWLIndividual activityBuscarIN=jenaModel.getOWLIndividual(uri_ld+varStrucActivityRef);
						/* agrega orden de secuencia "execution-order" a cada actividad de la estrcutura */
						activityBuscarIN.setPropertyValue(executionOrderProp,cont_act);
						cont_act++;
						/* conecta support activity a la estructura de actividades */
						activityStructureIN.addPropertyValue(executionEntityRefProp,activityBuscarIN);
							}
						}
					// INSERTA ACTIVITY STRUCTURE
					List nodoActivStruc=nodoActivityStruct.getChildren("activity-structure-ref",nsimsld);
					if (nodoActivStruc!=null)
						{
						Iterator j = nodoActivStruc.iterator();
						while (j.hasNext()){
						//System.out.println(" hasta aqui");
						Element nodoStrctureActivRef= (Element)j.next();
						//primer hijo que tenga como nombre club
						String varStrucActivityRef=nodoStrctureActivRef.getAttributeValue("ref");
						OWLIndividual activityBuscarIN=jenaModel.getOWLIndividual(uri_ld+varStrucActivityRef);
						
						activityStructureIN.addPropertyValue(executionEntityRefProp,activityBuscarIN);
							}
						}
					
					}
				}
	   	   
	   // inserta roles
	   OWLNamedClass roleLearnerCL=jenaModel.getOWLNamedClass(uri_ld+"Learner");
	   OWLNamedClass roleStaffCL=jenaModel.getOWLNamedClass(uri_ld+"Staff");
	   OWLNamedClass roleParticipantCL=jenaModel.getOWLNamedClass(uri_ld+"Participant");
	   OWLNamedClass roleModeratorCL=jenaModel.getOWLNamedClass(uri_ld+"Moderator");
	   OWLNamedClass roleConfMngCL=jenaModel.getOWLNamedClass(uri_ld+"Conference-Manager");
	   RDFProperty titleRoleProp = jenaModel.getRDFProperty(uri_actual+"title");
	   RDFProperty idenRoleProp = jenaModel.getRDFProperty(uri_ld+"identifier");

	   List listaRoles=nodoComponents.getChildren("roles", nsimsld); 
	    i = listaRoles.iterator();
        while (i.hasNext()){
            //System.out.println(" hasta aqui");
			Element nodoRol= (Element)i.next();
                        
			List nodoLearner=nodoRol.getChildren("learner",nsimsld);
			if (nodoLearner != null) {
				Iterator j=nodoLearner.iterator();
				while (j.hasNext()){
					Element nodoListLearner= (Element)j.next();
					String varIdentRole=nodoListLearner.getAttributeValue("identifier");
					Element titleRol=nodoListLearner.getChild("title",nsimsld);
					String varTitleRole="title";
					if (titleRol!=null) 
						varTitleRole=reemplazaBlancos(titleRol.getText());
					OWLIndividual roleIN=roleLearnerCL.createOWLIndividual(uri_ld+varIdentRole);
				
					roleIN.setPropertyValue(idenRoleProp,varIdentRole);
					roleIN.setPropertyValue(titleRoleProp,varTitleRole);
					LearningDesignIN.addPropertyValue(componentRefProp,roleIN); 
					}
				}
			List nodoTeacher=nodoRol.getChildren("staff",nsimsld);	
			if (nodoTeacher != null) {
				Iterator j=nodoTeacher.iterator();
				while (j.hasNext()){
					Element nodoListTeacher= (Element)j.next();
					String varIdentRole=nodoListTeacher.getAttributeValue("identifier");
					Element titleRol=nodoListTeacher.getChild("title",nsimsld);
					String varTitleRole="title";
					if (titleRol!=null) 
						varTitleRole=reemplazaBlancos(titleRol.getText());
								
					OWLIndividual roleIN=roleStaffCL.createOWLIndividual(uri_ld+varIdentRole);
					roleIN.setPropertyValue(idenRoleProp,varIdentRole);
					roleIN.setPropertyValue(titleRoleProp,varTitleRole);
					LearningDesignIN.addPropertyValue(componentRefProp,roleIN);
					}
				}
			
			List nodoParticipant=nodoRol.getChildren("participant",nsimsld);	
			if (nodoParticipant != null) {
				Iterator j=nodoParticipant.iterator();
				while (j.hasNext()){
					Element nodoListParticipant= (Element)j.next();
					String varIdentRole=nodoListParticipant.getAttributeValue("identifier");
					Element titleRol=nodoListParticipant.getChild("title",nsimsld);
					String varTitleRole="title";
					if (titleRol!=null) 
						varTitleRole=reemplazaBlancos(titleRol.getText());
					OWLIndividual roleIN=roleParticipantCL.createOWLIndividual(uri_ld+varIdentRole);
					roleIN.setPropertyValue(idenRoleProp,varIdentRole);
					roleIN.setPropertyValue(titleRoleProp,varTitleRole);
					LearningDesignIN.addPropertyValue(componentRefProp,roleIN);
					}
				}
			
			Element nodoModerator=nodoRol.getChild("moderator",nsimsld);	
			if (nodoModerator != null) {
				String varIdentRole=nodoModerator.getAttributeValue("identifier");
				Element titleRol=nodoModerator.getChild("title",nsimsld);
				String varTitleRole=reemplazaBlancos(titleRol.getText());
				OWLIndividual roleIN=roleModeratorCL.createOWLIndividual(uri_ld+varIdentRole);
				roleIN.setPropertyValue(idenRoleProp,varIdentRole);
				roleIN.setPropertyValue(titleRoleProp,varTitleRole);
				LearningDesignIN.addPropertyValue(componentRefProp,roleIN);
				}
			
			Element nodoConfMng=nodoRol.getChild("conference-manager",nsimsld);	
			if (nodoConfMng != null) {
				String varIdentRole=nodoConfMng.getAttributeValue("identifier");
				Element titleRol=nodoConfMng.getChild("title",nsimsld);
				String varTitleRole=reemplazaBlancos(titleRol.getText());
				OWLIndividual roleIN=roleConfMngCL.createOWLIndividual(uri_ld+varIdentRole);
				roleIN.setPropertyValue(idenRoleProp,varIdentRole);
				roleIN.setPropertyValue(titleRoleProp,varTitleRole);
				LearningDesignIN.addPropertyValue(componentRefProp,roleIN);
				}

			}
	   
	   // inserta ld:Method
		Element nodoMethod=learningDesign.getChild("method", nsimsld);  
		if (nodoMethod == null)
		            System.out.println("method es nulo");
		
		OWLNamedClass methodCL=jenaModel.getOWLNamedClass(uri_ld+"Method");
		OWLIndividual methodIN=methodCL.createOWLIndividual(uri_ld+"Mehtod"+varIdent);
		RDFProperty method_refProp = jenaModel.getRDFProperty(uri_ld+"method-ref");
		RDFProperty completeUOLRefProp = jenaModel.getRDFProperty(uri_ld+"complete-unit-of-learning-ref");
				
		LearningDesignIN.setPropertyValue(method_refProp,methodIN);
						
		// inserta ld:play
		OWLNamedClass playCL=jenaModel.getOWLNamedClass(uri_ld+"Play");
		RDFProperty idPlayProp = jenaModel.getRDFProperty(uri_ld+"identifier");
		RDFProperty visibleProp = jenaModel.getRDFProperty(uri_ld+"isvisible");
		RDFProperty titlePlayProp = jenaModel.getRDFProperty(uri_actual+"title");
		RDFProperty play_refProp = jenaModel.getRDFProperty(uri_ld+"play-ref"); // de metodo
		RDFProperty completePlayRefProp = jenaModel.getRDFProperty(uri_ld+"complete-play-ref"); // 
		//RDFProperty onCompletionRefProp = jenaModel.getRDFProperty(uri_ld+"on-completion-ref"); // 
		
		List listaPlay=nodoMethod.getChildren("play",nsimsld);
		if (listaPlay == null)
		            System.out.println(" Play es nulo");
		
		Iterator j = listaPlay.iterator();
        while (j.hasNext()){
            //System.out.println(" hasta aqui");
			Element nodoPlay= (Element)j.next();
            //primer hijo que tenga como nombre club
            String varIdentPlay=nodoPlay.getAttributeValue("identifier");
			String varVis=nodoPlay.getAttributeValue("isvisible");
			boolean varVisible = Boolean.parseBoolean(varVis);
			Element playTitle=nodoPlay.getChild("title", nsimsld);
			if (playTitle == null)
		            System.out.println(" titulo de play es nulo");
			String varTitlePlay=reemplazaBlancos(playTitle.getText());
			OWLIndividual playIN=playCL.createOWLIndividual(uri_ld+varIdentPlay);
			// System.out.println(" hasta aqui2");
			playIN.setPropertyValue(idPlayProp,varIdentPlay);
			playIN.setPropertyValue(visibleProp,varVisible);
			playIN.setPropertyValue(titlePlayProp,varTitlePlay);
			methodIN.addPropertyValue(play_refProp,playIN);
			
			// inserta COMPLETE PLAY
			Element nodoCompletePlay=nodoPlay.getChild("complete-play",nsimsld);
					
					if (nodoCompletePlay==null) playIN.addPropertyValue(onCompletionRefProp,creaOnCompletionUnitPLAY());
					if (nodoCompletePlay!=null)
						{		
						Element nodoWhenCompleted=nodoCompletePlay.getChild("when-last-act-completed",nsimsld);
						if (nodoWhenCompleted!=null) 
							{
							playIN.setPropertyValue(onCompletionRefProp,creaWhenActCompleted(nodoWhenCompleted));
							}
						
						Element nodoTimeLimitPlay=nodoCompletePlay.getChild("time-limit",nsimsld);
						if (nodoTimeLimitPlay!=null)
							{
							playIN.setPropertyValue(completePlayRefProp,creaTiempoLimite(nodoTimeLimitPlay,"play"));
							}
						}
			// inserta FEEDBACK DESCRIPTION
					Element nodoOnCompletion=nodoPlay.getChild("on-completion",nsimsld);
					if (nodoOnCompletion!=null)
							{
							Element nodoFeedBack=nodoOnCompletion.getChild("feedback-description",nsimsld);
							if (nodoFeedBack!=null) 
								{
								//System.out.println(" si hay nodo feedback");
								creaOnCompletionFeedback(nodoFeedBack,playIN,onCompletionRefProp);
								}

							}		
			playIN=null;
			}
			
		// inserta ACT
		OWLNamedClass actCL=jenaModel.getOWLNamedClass(uri_ld+"Act");
		RDFProperty idActProp = jenaModel.getRDFProperty(uri_ld+"identifier");
		RDFProperty titleActProp = jenaModel.getRDFProperty(uri_actual+"title");
		RDFProperty execOrderActProp = jenaModel.getRDFProperty(uri_ld+"execution-order");
		RDFProperty rolePartRefProp = jenaModel.getRDFProperty(uri_ld+"role-part-ref");
		RDFProperty completeActRefProp = jenaModel.getRDFProperty(uri_ld+"complete-act-ref");
		RDFProperty onCompleteActRefProp = jenaModel.getRDFProperty(uri_ld+"on-completion-ref");
		
		RDFProperty act_refProp = jenaModel.getRDFProperty(uri_ld+"act-ref"); // de Play
		
		listaPlay=null;
		listaPlay=nodoMethod.getChildren("play",nsimsld);
		if (listaPlay == null)
		            System.out.println(" Play es nulo");
		
		i = listaPlay.iterator();
        //System.out.println(" hasta aqui1");
		if (i == null) System.out.println(" lista de play nulos");
		while (i.hasNext())
			{
			Element nodoPlay= (Element)i.next();
            String varIdentPlay=nodoPlay.getAttributeValue("identifier");
						

			OWLIndividual playBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentPlay);
			if (playBuscar == null) 
		            System.out.println(" playBuscar es nulo");
			
			List listaAct=nodoPlay.getChildren("act",nsimsld);
			if (listaAct == null)
		            System.out.println(" lsitAct es nulo");
			j = listaAct.iterator();
			while (j.hasNext()) 
				{
				Element nodoAct= (Element)j.next();

				String varIdentAct=nodoAct.getAttributeValue("identifier");
				Element actTitle=nodoAct.getChild("title", nsimsld);
				String varTitleAct="title";
				if (actTitle!=null) varTitleAct=reemplazaBlancos(actTitle.getText());
				OWLIndividual actIN=null;
				OWLIndividual recursoBuscar=jenaModel.getOWLIndividual(uri_ld+varIdentAct);
			    if (recursoBuscar==null) {
				actIN=actCL.createOWLIndividual(uri_ld+varIdentAct);
				allDifferent.addDistinctMember(actIN);
				if (actIN==null) System.out.println(" individuo acto es nulo");
				actIN.setPropertyValue(idActProp,varIdentAct);
				actIN.setPropertyValue(titleActProp,varTitleAct);
				playBuscar.addPropertyValue(act_refProp,actIN);  // permite inserta varias property
 											
				// crear ROLE Part
				List listRolePart=nodoAct.getChildren("role-part", nsimsld);
				if (listRolePart != null)
					{		            
					Iterator w = listRolePart.iterator();
					while (w.hasNext()) {
						Element nodoRolePart= (Element)w.next();
						OWLIndividual rolePartIND=creaRolePart(nodoRolePart);
						allDifferent.addDistinctMember(rolePartIND);
						if (rolePartIND==null) System.out.println(" indivduo role part es nulo");
						actIN.addPropertyValue(rolePartRefProp,rolePartIND);	
						}
					}
				
				// crea tiempo limite para ACT
				Element nodoCompleteAct=nodoAct.getChild("complete-act",nsimsld);
				if (nodoCompleteAct==null) actIN.addPropertyValue(onCompleteActRefProp,creaOnCompletionUnitAct());	
					if (nodoCompleteAct!=null)
						{
						List nodoWhenCompleted=nodoCompleteAct.getChildren("when-role-part-completed",nsimsld);
						if (nodoWhenCompleted!=null) 
							{
							actIN.setPropertyValue(completeActRefProp,creaWhenRolePartCompleted(nodoWhenCompleted));
							}
						Element nodoTimeLimit=nodoCompleteAct.getChild("time-limit",nsimsld);
						if (nodoTimeLimit!=null)
							{
							actIN.setPropertyValue(completeActRefProp,creaTiempoLimite(nodoTimeLimit,"act"));
							}
						}
									
				// inserta FEEDBACK DESCRIPTION
					Element nodoOnCompletion=nodoAct.getChild("on-completion",nsimsld);
					if (nodoOnCompletion!=null)
							{
							Element nodoFeedBack=nodoOnCompletion.getChild("feedback-description",nsimsld);
							if (nodoFeedBack!=null) 
								{
								//System.out.println(" si hay nodo feedback");
								creaOnCompletionFeedback(nodoFeedBack,actIN,onCompleteActRefProp);
								}
							}	
				
				}
				actIN=null;
				}
			playBuscar=null;
			}
			
			// inserta Prequisitos 
			List nodoPrerequi=learningDesign.getChildren("prerequisites",nsimsld);
			if (nodoPrerequi!=null)
			 {
			 //System.out.println(" si tiene prerequisito ");
			 insertaPrerequi(nodoPrerequi,LearningDesignIN,prerequisiteRefProp);
			 }
			
			// inserta LEARNING OBJECTIVES
			Element nodoLearningObjectives=learningDesign.getChild("learning-objectives",nsimsld);
			if (nodoLearningObjectives!=null)
			{
			creaLearningObjective(nodoLearningObjectives,LearningDesignIN,learningObjectiveRefProp);
			}
			
			// complete method
			Element nodoCompleteUOL=nodoMethod.getChild("complete-unit-of-learning",nsimsld);
			
			//if (nodoCompleteUOL==null) methodIN.addPropertyValue(onCompletionRefProp,creaOnCompletionUnitMethod());
			
			if (nodoCompleteUOL!=null)
				{
				List nodoWhenPlayCompleted=nodoCompleteUOL.getChildren("when-play-completed",nsimsld);
				if (nodoWhenPlayCompleted!=null) 
					{
					methodIN.setPropertyValue(completeUOLRefProp,creaWhenPlayCompleted(nodoWhenPlayCompleted));
					}
				Element nodoTimeLimit=nodoCompleteUOL.getChild("time-limit",nsimsld);
				if (nodoTimeLimit!=null)
					{
					methodIN.setPropertyValue(completeUOLRefProp,creaTiempoLimite(nodoTimeLimit,"method"));
					}
				}
			// inserta FEEDBACK DESCRIPTION
			Element nodoOnCompletion=nodoMethod.getChild("on-completion",nsimsld);
			if (nodoOnCompletion!=null)
					{
					Element nodoFeedBack=nodoOnCompletion.getChild("feedback-description",nsimsld);
					if (nodoFeedBack!=null) 
						{
						//System.out.println(" si hay nodo feedback");
						creaOnCompletionFeedback(nodoFeedBack,methodIN,onCompletionRefProp);
						}
					}	
	   
		// relaciona conceptos y condiciones con actividades
		// IF theoretical or conceptual 
		if ((metodoSeleccionado.equals("theoretical")) || (metodoSeleccionado.equals("conceptual")))
			{
			OWLNamedClass conceptCL=null;
			System.out.println(" entra a manifest:"+metodoSeleccionado);
			System.out.println(" entra a manifest Ontology:"+ontologiaSeleccionada);
			OWLNamedClass activityCL=jenaModel.getOWLNamedClass(uri_actual+"ConceptLearningActivity");
			Collection instancesActivity = activityCL.getInstances(false);	
			if (ontologiaSeleccionada.equals("EnvO"))
			    {
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-EnvO");
			    }
			if (ontologiaSeleccionada.equals("GO"))
				{
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-GO");
				}
			if (ontologiaSeleccionada.equals("AAT"))
				{
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-AAT");
				}
			if (ontologiaSeleccionada.equals("Programming"))
			    {
			    conceptCL=jenaModel.getOWLNamedClass(uri_actual+"Knowledge-Programming");
			    }
			Collection instancesConcept = conceptCL.getInstances(false);	
			// llama a ventRelacion.java para 
			//if (instancesConcept==null) System.out.println(" isntancesConcept es NULO !!!");
			VentRelacion ventanaConceptos = new VentRelacion();
			ventanaConceptos.generaVentana(instancesActivity, instancesConcept,jenaModel);
			  
			}
		
		//IF Simplifying conditions sequence
		if ((metodoSeleccionado.equals("simplifying")))
			{
			OWLNamedClass conceptCL=null;
			//System.out.println(" entra a manifest:"+metodoSeleccionado);
			OWLNamedClass activityCL=jenaModel.getOWLNamedClass(uri_actual+"ConceptLearningActivity");
			Collection instancesActivity = activityCL.getInstances(false);	
			if (ontologiaSeleccionada.equals("EnvO"))
			    {
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-EnvO");
			    }
			if (ontologiaSeleccionada.equals("GO"))
				{
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-GO");
				}
			if (ontologiaSeleccionada.equals("AAT"))
				{
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"KnowledgeItem-AAT");
				}
			if (ontologiaSeleccionada.equals("Programming"))
		    	{
				conceptCL=jenaModel.getOWLNamedClass(uri_actual+"Knowledge-Programming");
		    	}
			Collection instancesConcept = conceptCL.getInstances(false);	
			// llama a ventRelacion.java para 
			//if (instancesConcept==null) System.out.println(" isntancesConcept es NULO !!!");
			VentRelacion ventanaConceptos = new VentRelacion();
			ventanaConceptos.generaVentana(instancesActivity, instancesConcept,jenaModel);
				
			
			// llamada para relacionar Condiciones
			//OWLNamedClass activityCL=jenaModel.getOWLNamedClass(uri_actual+"ConceptLearningActivity");
			//Collection instancesActivity = activityCL.getInstances(false);
			OWLNamedClass conditionCL=jenaModel.getOWLNamedClass(uri_actual+"ConditionItem");
			Collection instancesCondicion = conditionCL.getInstances(false);		
			VentCondicion ventanaCondicion = new VentCondicion();
			ventanaCondicion.generaVentanaCondicion(instancesActivity, instancesCondicion,jenaModel);
		   	}
	   	   
	   		
	   /* Graba las ontologias en archivo de salida */
	   guardaOntologias();
		System.out.println(" =====================================================================");//$NON-NLS-1$
		System.out.println(" archivo imsmanifest.xml instanciado en "+ontologiaSalida);//$NON-NLS-1$
		System.out.println(" =====================================================================");//$NON-NLS-1$
		}catch (Exception e){
        e.printStackTrace();
     }
	}

}
