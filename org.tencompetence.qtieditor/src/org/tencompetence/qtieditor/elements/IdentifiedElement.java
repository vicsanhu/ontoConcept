package org.tencompetence.qtieditor.elements;

import java.util.UUID;

public abstract class IdentifiedElement extends BasicElement{

	protected String fID = ""; // optional

	protected String createId() {

		fID = "" + UUID.randomUUID().toString() + "-" + IDGenerator.getID();
		return fID;
	}

	protected String createId(String elementName) {

		fID = elementName + "-" + UUID.randomUUID().toString() + "-" + IDGenerator.getID();
		return fID;
	}

	public void setId(String id) {
		this.fID = id;
	}

	public String getId() {
		return fID;
	}
}
