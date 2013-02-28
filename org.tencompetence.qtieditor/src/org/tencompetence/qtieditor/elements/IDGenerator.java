package org.tencompetence.qtieditor.elements;

public class IDGenerator {

	private static int count = 1;

	/** Creates a new instance of IDGenerator */
	public IDGenerator() {
	}

	public static int getID() {
		return count++;
	}
}
