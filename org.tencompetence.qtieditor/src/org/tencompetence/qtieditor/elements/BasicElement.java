package org.tencompetence.qtieditor.elements;

import org.jdom.Namespace;
import org.tencompetence.qtieditor.serialization.INamespaces;

public abstract class BasicElement {


	protected abstract String getTagName();
	
	protected Namespace getEmbeddedNamespace() {
		return INamespaces.IMSQTI_NAMESPACE_2_0_EMBEDDED;
	}
		
	protected Namespace getNamespace() {
		return INamespaces.IMSQTI_NAMESPACE_2_0;
	}
	
	protected Namespace getEmbeddedTestNamespace() {
		return INamespaces.IMSQTI_NAMESPACE_2_1_EMBEDDED;
	}
		
	protected Namespace getTestNamespace() {
		return INamespaces.IMSQTI_NAMESPACE_2_1;
	}
	
}
