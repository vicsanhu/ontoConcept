package org.tencompetence.ldauthor.ui.views.ontoconcept;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.tencompetence.ldauthor.ui.views.ontoconcept.jaxb.GenerateXML;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

@SuppressWarnings("nls")
public class runOntology {
	public JenaOWLModel jenaModel;
	public JenaOWLModel eccModel;
	public JenaOWLModel retModel;
	private String ontology;
	public  OWLAllDifferent allDifferent;
	private List<String> resultSearch= new ArrayList<String>();
	private List<String> errors= new ArrayList<String>();
	private HashMap<RDFSClass,List<OWLNamedClass>> hasMapArray = new HashMap<RDFSClass, 
			List<OWLNamedClass>>();
	
	public runOntology(String fileSelected, String topic) {
		this.ontology = fileSelected;
		this.searchPreviewOntology(topic);
	}
	
	public void loadOntology() {
		try {
			jenaModel = ProtegeOWL.createJenaOWLModelFromURI(ontology);
			allDifferent = jenaModel.createOWLAllDifferent();
		} catch (OntologyLoadException e) {
			 e.printStackTrace();
			 errors.add(e.toString() + e.getLocalizedMessage());
		}
	}
	
	public void loadRetOntology() {
		String retOntology= "file:/c:/ontologies/retOnto/ret.owl";
		try {
			retModel = ProtegeOWL.createJenaOWLModelFromURI(retOntology);
			allDifferent = retModel.createOWLAllDifferent();
		} catch (OntologyLoadException e) {
			 e.printStackTrace();
		}
	}
	
	public void incorporateTerpsicore(String searchedTopic) {
		loadRetOntology();
		OWLNamedClass  knowledgeItemInstance;
		OWLNamedClass knowledgeItem = retModel.getOWLNamedClass("KnowledgeItem");
		if(knowledgeItem != null) {
			searchedTopic = "KnowledgeItem-"+searchedTopic;
			if(retModel.getOWLNamedClass(searchedTopic) != null) {
				retModel.getOWLNamedClass(searchedTopic).delete();
			}
			knowledgeItemInstance = retModel.createOWLNamedSubclass(
					searchedTopic, knowledgeItem);
			OWLProperty hasKind = retModel.getOWLProperty("concept-hasKind");
			OWLProperty hasPart = retModel.getOWLProperty("concept-hasPart");
			for(Iterator<?> it = eccModel.getUserDefinedOWLNamedClasses().
					iterator();it.hasNext();) {
				OWLNamedClass searchClass = (OWLNamedClass)it.next();
				if(retModel.getOWLIndividual(searchClass.getLocalName()) != null) {
					retModel.getOWLIndividual(searchClass.getLocalName()).delete();
				}
				knowledgeItemInstance.createOWLIndividual(searchClass.getLocalName());
			}
			for(Iterator<?> it = eccModel.getUserDefinedOWLNamedClasses().
					iterator();it.hasNext();) {
				OWLNamedClass searchClass = (OWLNamedClass)it.next();
				OWLIndividual superClass = retModel.getOWLIndividual(searchClass.getLocalName());
				for(Iterator<?> jt = searchClass.getSubclasses(false).iterator();
						jt.hasNext();) {
					OWLNamedClass searchSubClass = (OWLNamedClass)jt.next();
					OWLIndividual subClass = retModel.getOWLIndividual(searchSubClass.getLocalName());
					superClass.addPropertyValue(hasKind, subClass);
				}
				for(Iterator<?> kt = ((OWLNamedClass)searchClass).getRestrictions(false).
						iterator();kt.hasNext();) {
					OWLRestriction searchProperty = (OWLRestriction)kt.next();
					if(searchProperty instanceof OWLSomeValuesFrom) {
						RDFResource someValuesFrom = (RDFResource) ((OWLSomeValuesFrom) searchProperty).
								getSomeValuesFrom();
						OWLIndividual subClass = retModel.getOWLIndividual(someValuesFrom.getLocalName());
						superClass.addPropertyValue(hasPart, subClass);
					}
					if(searchProperty instanceof OWLAllValuesFrom) {
						RDFResource allValuesFrom = (RDFResource) ((OWLAllValuesFrom) searchProperty).
								getAllValuesFrom();
						OWLIndividual subClass = retModel.getOWLIndividual(allValuesFrom.getLocalName());
						superClass.addPropertyValue(hasPart, subClass);
					}
					if(searchProperty instanceof OWLHasValue) {
						RDFResource hasValue = (RDFResource) ((OWLHasValue) searchProperty).
								getHasValue();
						OWLIndividual subClass = retModel.getOWLIndividual(hasValue.getLocalName());
						superClass.addPropertyValue(hasPart, subClass);
					}
				}
			}
		}
		try {
			FileWriter writer = new FileWriter("c:/ontologies/retOnto/ret.owl");
			OWLModelWriter omw = new OWLModelWriter(retModel,retModel.
					getTripleStoreModel().getActiveTripleStore(), writer);
			 omw.write();
		     writer.close();
		} catch(Exception e) {
			 System.out.println("Exception: " + e);
		}
	}
	
	public void write() {
		try {
			Element genealogy = new Element("genealogy");
			Document doc = new Document(genealogy);
			doc.setRootElement(genealogy);
			Element staff = new Element("person");
			staff.setAttribute(new Attribute("id", "1"));
			staff.setAttribute(new Attribute("x", "1"));
			staff.setAttribute(new Attribute("y", "1"));
			staff.setAttribute(new Attribute("name", "victor"));
			doc.getRootElement().addContent(staff);
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("c:\\ontologies\\file.xml"));
			System.out.println("File Saved!");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}
	
	
	public void searchPreviewOntology(String topic) {
		loadOntology();
		topic = topic.replaceAll(Messages.getString("runOntology.2"), //$NON-NLS-1$
				Messages.getString("runOntology.3"));  //$NON-NLS-1$ 
		for(Iterator<?> it = jenaModel.getUserDefinedOWLNamedClasses().
				iterator();it.hasNext();) {
			OWLNamedClass searchClass = (OWLNamedClass)it.next();
			if(searchClass instanceof OWLNamedClass) {
				if(!(searchClass.isMetaclass()) || (searchClass.isVisible())){
					if(searchClass.getLocalName().toLowerCase().contains(topic.toLowerCase())) {
						 setResultSearch(searchClass.getLocalName());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void searchDefinitiveOntology(String topic) {
		OWLNamedClass searchedTopic = jenaModel.getOWLNamedClass(topic);
		if (searchedTopic == null)  {
			for (Iterator<?> it = jenaModel.getUserDefinedOWLNamedClasses().
					iterator();it.hasNext();) {
				OWLNamedClass searchClass = (OWLNamedClass)it.next();
				if(searchClass instanceof OWLNamedClass) {
					if(!(searchClass.isMetaclass()) || (searchClass.isVisible())){
						if(searchClass.getLocalName().equals(topic)) {
							searchedTopic = searchClass;
						}
					}
				}
			}
		}
        try {
			eccModel = ProtegeOWL.createJenaOWLModel();
			eccModel.createOWLNamedClass(topic);
			hasMapArray.clear();
			for(Iterator<?> it = searchedTopic.getSubclasses(true).iterator();it.hasNext();) {
				RDFSClass searchSubClass = (RDFSClass)it.next();
				if((searchSubClass instanceof OWLNamedClass)) {
					if(!(searchSubClass.isMetaclass()) || (searchSubClass.isVisible())) {
						List<OWLNamedClass> arrayAddSuperclass = new ArrayList<OWLNamedClass>();
						for(Iterator<?> jt =  searchSubClass.
								getNamedSuperclasses(false).iterator(); jt.hasNext();) {
							OWLNamedClass searchSuperClass = (OWLNamedClass)jt.next();
							if(eccModel.getOWLNamedClass(searchSuperClass.getLocalName()) != null) {
								OWLNamedClass var1 = eccModel.getOWLNamedClass(searchSuperClass.getLocalName());
								OWLNamedClass var2 = eccModel.createOWLNamedClass(searchSubClass.getLocalName());
								var2.addSuperclass(var1);
								var2.removeSuperclass(eccModel.getOWLThingClass());
							}else {
								arrayAddSuperclass.add(searchSuperClass);
								hasMapArray.put(searchSubClass, arrayAddSuperclass);
							}
						}
					}
				}
			}
			if(hasMapArray.isEmpty() != true) {
				for(Iterator<?> it = hasMapArray.entrySet().iterator();it.hasNext();) {
					Map.Entry<?, ?> e = (Map.Entry<?, ?>)it.next();
					RDFSClass  consultSuperclass = eccModel.getOWLNamedClass(((RDFSClass) e.getKey()).
							getLocalName());
					for(Iterator<?> jt = ((List<OWLNamedClass>) e.getValue()).iterator();jt.hasNext();) {
						OWLNamedClass superClass = (OWLNamedClass)jt.next();
						RDFSClass consultSubclass = eccModel.getOWLNamedClass(consultSuperclass.getLocalName());
						RDFSClass ind = eccModel.getOWLNamedClass(superClass.getLocalName());
						if(ind != null) {
							consultSubclass.addSuperclass(ind);
						}
					}
				}
			}
			OWLObjectProperty createProperty = eccModel.createOWLObjectProperty("partof");
			for(Iterator<?> it = searchedTopic.getSubclasses(true).iterator();
					it.hasNext();) {
				RDFSClass searchSubClass = (RDFSClass)it.next();
				if( searchSubClass instanceof OWLNamedClass){
					for(Iterator<?> jt = ((OWLNamedClass)searchSubClass).getRestrictions(false).
							iterator();jt.hasNext();) {
						OWLRestriction searchProperty = (OWLRestriction)jt.next();
						RDFProperty property = searchProperty.getOnProperty();
						if(property.getLocalName().toLowerCase().replaceAll("[^a-zA-Z]", "").
								equals(createProperty.getLocalName().toLowerCase())) {
							OWLNamedClass  subClass = eccModel.getOWLNamedClass(
									searchSubClass.getBrowserText());
							createProperty = eccModel.createOWLObjectProperty("part_of");
							if(searchProperty instanceof OWLSomeValuesFrom) {
								RDFResource someValuesFrom = (RDFResource) ((OWLSomeValuesFrom) searchProperty).
										getSomeValuesFrom();
								if(eccModel.getOWLNamedClass(someValuesFrom.getLocalName()) !=null) {
									RDFSClass superClass = eccModel.getOWLNamedClass(
											someValuesFrom.getLocalName());
									OWLSomeValuesFrom superRestriction = eccModel.createOWLSomeValuesFrom(
									createProperty, superClass);
									subClass.addSuperclass(superRestriction);
								}
							}
							if(searchProperty instanceof OWLAllValuesFrom) {
								RDFResource AllValuesFrom = (RDFResource) ((OWLAllValuesFrom) searchProperty).
										getAllValuesFrom();
								if(eccModel.getOWLNamedClass(AllValuesFrom.getLocalName()) !=null) {
									RDFSClass superClass = eccModel.getOWLNamedClass(
											AllValuesFrom.getLocalName());
									OWLAllValuesFrom superRestriction = eccModel.createOWLAllValuesFrom(
									createProperty, superClass);
									subClass.addSuperclass(superRestriction);
								}
							}
							if(searchProperty instanceof OWLHasValue) {
								RDFResource hasValue = (RDFResource) ((OWLHasValue) searchProperty).
										getHasValue();
								if(eccModel.getOWLNamedClass(hasValue.getLocalName()) !=null) {
									RDFSClass superClass = eccModel.getOWLNamedClass(
											hasValue.getLocalName());
									OWLHasValue superRestriction = eccModel.createOWLHasValue(
									createProperty, superClass);
									subClass.addSuperclass(superRestriction);
								}
							}
						}
					}
				}
			}
			incorporateTerpsicore(searchedTopic.getLocalName());
		} catch(OntologyLoadException oe) {
			oe.printStackTrace();
		}
		try {
			FileWriter writer = new FileWriter("C:/ontologies/retOnto/prueba.owl");
			OWLModelWriter omw = new OWLModelWriter(eccModel,eccModel.
					getTripleStoreModel().getActiveTripleStore(), writer);
			 omw.write();
		     writer.close();
		} catch(Exception e) {
			 System.out.println("Exception: " + e);
		}
		try {
			new GenerateXML(topic).ontoZestGraph();
			//generateXML();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setResultSearch(String topic) {
		resultSearch.add(topic);
	}
	
	public List<String> getResultSearch() {
		return resultSearch;
	}
	
	public List<String> getErrors() {
		return errors;
	}
}	

	
