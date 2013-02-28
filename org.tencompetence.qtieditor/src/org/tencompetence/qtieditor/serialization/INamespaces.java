package org.tencompetence.qtieditor.serialization;

import org.jdom.Namespace;

public interface INamespaces {
	
    Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    String XSI_SCHEMALOCATION = "schemaLocation";

	String IMSQTI_NAMESPACE_PREFIX = "imsqti";
    // QTI Version 2.0
    Namespace IMSQTI_NAMESPACE_2_0 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsqti_v2p0");
    Namespace IMSQTI_NAMESPACE_2_0_EMBEDDED = Namespace.getNamespace(IMSQTI_NAMESPACE_PREFIX, IMSQTI_NAMESPACE_2_0.getURI());
    String    IMSQTI_SCHEMALOCATION_2_0 = "http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd";
    // QTI Version 2.1
    Namespace IMSQTI_NAMESPACE_2_1 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsqti_v2p1");
    Namespace IMSQTI_NAMESPACE_2_1_EMBEDDED = Namespace.getNamespace(IMSQTI_NAMESPACE_PREFIX, IMSQTI_NAMESPACE_2_1.getURI());
    String    IMSQTI_SCHEMALOCATION_2_1 = "http://www.imsglobal.org/xsd/imsqti_v2p1 imsqti_v2p1.xsd";
}
