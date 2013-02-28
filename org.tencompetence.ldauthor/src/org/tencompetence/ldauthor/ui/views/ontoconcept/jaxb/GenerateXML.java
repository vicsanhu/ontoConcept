package org.tencompetence.ldauthor.ui.views.ontoconcept.jaxb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

@SuppressWarnings("nls")
public class GenerateXML {
	private String topic;
	private JenaOWLModel eccModel;

	public GenerateXML(String topic) {
		this.topic = topic;
		try {
			eccModel = ProtegeOWL.createJenaOWLModelFromURI(
					"file:/c:/ontologies/retOnto/prueba.owl");
			eccModel.createOWLAllDifferent();
		} catch (OntologyLoadException e) {
			 e.printStackTrace();
		}
	}
	
	public void ontoZestGraph() throws JAXBException, FileNotFoundException {
		OWLNamedClass root =  eccModel.
				 getOWLNamedClass(topic);
		int countSuperClass = 0;
		ArrayList<Person> person = new ArrayList<Person>();
		Person person_root = new Person();
		person_root.setId(Integer.toString(countSuperClass));
		person_root.setName(root.getLocalName());
		person_root.setGender("MALE");
		person_root.setNote(Integer.toString(root.getSubclassCount()));
		person.add(person_root);
		countSuperClass++;
		Genealogy genealogy = new Genealogy();
		genealogy.setPerson(person);
		JAXBContext context = JAXBContext.newInstance(Genealogy.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(genealogy, System.out);
		OutputStream outputStream = new FileOutputStream("./genealogy.xml");
		m.marshal(genealogy, outputStream);
		

		Unmarshaller um = context.createUnmarshaller();
		InputStream inputStream = new FileInputStream("./genealogy.xml");
		Genealogy genealogy1 = (Genealogy) um.unmarshal(inputStream);
		ArrayList<Person> list = genealogy1.getPerson();
		ArrayList<Marriage> marriageList = new ArrayList<Marriage>();
		 if (root.getSubclassCount() > 0) {
			 ArrayList<Offspring> offspringList = new ArrayList<Offspring>();
			 for(Iterator<?> it = root.
					 getSubclasses(false).iterator();it.hasNext();) {
				 OWLNamedClass searchSubClass = (OWLNamedClass)it.next();
				 Person person_sub = new Person();
				 person_sub.setId(Integer.toString(countSuperClass));
				 person_sub.setName(searchSubClass.getLocalName());
				 person_sub.setGender("FEMALE");
				 person_sub.setNote(Integer.toString(searchSubClass.
						 getSubclassCount()));
				 list.add(person_sub);
				 Offspring offspring = new Offspring();
				 offspring.setChildId(Integer.toString(countSuperClass));
				 offspringList.add(offspring);
				 countSuperClass++;
			 } 
			 Marriage marriage = new Marriage();
			 marriage.setTypeRelation("is_a");
			 marriage.setSuperclassId(person_root.getId());
			 marriage.setoffspring(offspringList);
			 marriageList.add(marriage);
			 genealogy.setPerson(list);
			 genealogy.setMarriage(marriageList);
			 JAXBContext context1 = JAXBContext.newInstance(Genealogy.class);
			 Marshaller m1 = context1.createMarshaller();
			 m1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			 m1.marshal(genealogy, System.out);
			 OutputStream outputStreamF = new FileOutputStream("./genealogy.xml");
			 System.out.println(outputStreamF);
			 m1.marshal(genealogy,outputStreamF );
		 }
		 
	}
}
