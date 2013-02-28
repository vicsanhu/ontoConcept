package org.tencompetence.ldauthor.ui.views.ontoconcept.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.GenealogyGraph;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Note;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Relation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Loads information from an XML stream into a {@link GenealogyGraph}
 */
@SuppressWarnings("nls")
public class GenealogyGraphReader extends DefaultHandler
{
	private final GenealogyGraph graph;
	private Map<Integer, Class> idToPerson;
	private Class currentPerson;
	private Note currentNote;
	private Relation currentMarriage;

	public GenealogyGraphReader(GenealogyGraph graph) {
		this.graph = graph;
	}

	public void read(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		idToPerson = new HashMap<Integer, Class>();
		parser.parse(stream, this);
		resolveRelationships();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("person")) {
			Class p = new Class(Class.Gender.valueOf(attributes.getValue("gender")));
			idToPerson.put(readInt(attributes, "id"), p);
			p.setName(attributes.getValue("name"));
			graph.addPerson(p);
			currentPerson = p;
		}
		else if (qName.equals("marriage")) {
			Relation m = new Relation(readString(attributes, "typeRelation"));
			// ASSUME that all person objects have been created
			m.setSuperClass(idToPerson.get(readInt(attributes, "superclassId")));
			m.setSubClass(idToPerson.get(readInt(attributes, "subclassId")));
			graph.addMarriage(m);
			currentMarriage = m;
		}
		else if (qName.equals("offspring")) {
			// ASSUME that all person objects have been created
			currentMarriage.addOffspring(idToPerson.get(readInt(attributes, "childId")));
		}
		else if (qName.equals("note")) {
			Note n = new Note(attributes.getValue("text"));
			if (currentPerson != null)
				currentPerson.addNote(n);
			else
				graph.addNote(n);
			currentNote = n;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentNote != null)
			currentNote.setText(new String(ch, start, length));
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("person"))
			currentPerson = null;
		else if (qName.equals("marriage"))
			currentMarriage = null;
		else if (qName.equals("note"))
			currentNote = null;
	}
	private int readInt(Attributes attributes, String key) {
		String value = attributes.getValue(key);
		if (value == null)
			return -1;
		return Integer.parseInt(value);
	}
	
	private String readString(Attributes attributes, String key) {
		String value = attributes.getValue(key);
		return value;
	}

	private void resolveRelationships() {
		// Ignore
	}
}
