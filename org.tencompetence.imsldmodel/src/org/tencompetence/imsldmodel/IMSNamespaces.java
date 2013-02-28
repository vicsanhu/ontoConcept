/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.imsldmodel;

import org.jdom.Namespace;


/**
 * IMS Namespaces
 * 
 * @author Phillip Beauvoir
 * @version $Id: IMSNamespaces.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public interface IMSNamespaces {
    
    Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"); //$NON-NLS-1$ //$NON-NLS-2$
    String XSI_SCHEMALOCATION = "schemaLocation"; //$NON-NLS-1$
    
    /*
     * Metadata
     */ 
    String IMSMD_NAMESPACE_PREFIX = "imsmd"; //$NON-NLS-1$
    // MD Namespace Version 1.2.4
    Namespace IMSMD_NAMESPACE_124 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsmd_v1p2"); //$NON-NLS-1$
    Namespace IMSMD_NAMESPACE_124_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, IMSMD_NAMESPACE_124.getURI());
    String    IMSMD_SCHEMALOCATION_124 = "http://www.imsglobal.org/xsd/imsmd_v1p2p4.xsd"; //$NON-NLS-1$
    // MD Namespace Version 1.2.2
    Namespace IMSMD_NAMESPACE_122 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsmd_v1p2"); //$NON-NLS-1$
    Namespace IMSMD_NAMESPACE_122_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, IMSMD_NAMESPACE_122.getURI());
    String    IMSMD_SCHEMALOCATION_122 = "http://www.imsglobal.org/xsd/imsmd_v1p2.xsd"; //$NON-NLS-1$
    // MD Namespace Version 1.2.1
    Namespace IMSMD_NAMESPACE_121 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsmd_rootv1p2p1"); //$NON-NLS-1$
    Namespace IMSMD_NAMESPACE_121_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, IMSMD_NAMESPACE_121.getURI());
    String    IMSMD_SCHEMALOCATION_121 = "http://www.imsglobal.org/xsd/imsmd_v1p2.xsd"; //$NON-NLS-1$
    // MD Namespace Version 1.2 - Not supported, has out of date Schema
    Namespace IMSMD_NAMESPACE_12 = Namespace.getNamespace("http://www.imsproject.org/xsd/imsmd_rootv1p2"); //$NON-NLS-1$
    Namespace IMSMD_NAMESPACE_12_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, IMSMD_NAMESPACE_12.getURI());
    // MD Namespace Version 1.1 
    Namespace IMSMD_NAMESPACE_11 = Namespace.getNamespace("http://www.imsproject.org/xsd/ims_md_rootv1p1"); //$NON-NLS-1$
    Namespace IMSMD_NAMESPACE_11_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, IMSMD_NAMESPACE_11.getURI());
    // MD Bogus Namespace as in WebCT
    Namespace MD_BOGUS_NAMESPACE1 = Namespace.getNamespace("http://www.imsproject.org/metadata"); //$NON-NLS-1$
    Namespace MD_BOGUS_NAMESPACE1_EMBEDDED = Namespace.getNamespace(IMSMD_NAMESPACE_PREFIX, MD_BOGUS_NAMESPACE1.getURI());
    // IEEE Lom
    String IEEE_NAMESPACE_PREFIX = "lom"; //$NON-NLS-1$
    Namespace IEEE_NAMESPACE_100 = Namespace.getNamespace("http://ltsc.ieee.org/xsd/LOMv1p0"); //$NON-NLS-1$
    Namespace IEEE_NAMESPACE_100_EMBEDDED = Namespace.getNamespace(IEEE_NAMESPACE_PREFIX, IEEE_NAMESPACE_100.getURI());
    
    /*
     * IMS Content Packaging
     */
    String IMSCP_NAMESPACE_PREFIX = "imscp"; //$NON-NLS-1$
    // CP Version 1.1.4
    Namespace IMSCP_NAMESPACE_114 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imscp_v1p1"); //$NON-NLS-1$
    Namespace IMSCP_NAMESPACE_114_EMBEDDED = Namespace.getNamespace(IMSCP_NAMESPACE_PREFIX, IMSCP_NAMESPACE_114.getURI());
    String    IMSCP_SCHEMALOCATION_114 = "http://www.imsglobal.org/xsd/imscp_v1p1.xsd"; //$NON-NLS-1$
    // CP Version 1.1.3
    Namespace IMSCP_NAMESPACE_113 =  Namespace.getNamespace("http://www.imsglobal.org/xsd/imscp_v1p1"); //$NON-NLS-1$
    Namespace IMSCP_NAMESPACE_113_EMBEDDED =  Namespace.getNamespace(IMSCP_NAMESPACE_PREFIX, IMSCP_NAMESPACE_113.getURI());
    String    IMSCP_SCHEMALOCATION_113 = "http://www.imsglobal.org/xsd/imscp_v1p1.xsd"; //$NON-NLS-1$
    // CP Version 1.1.2
    Namespace IMSCP_NAMESPACE_112 = Namespace.getNamespace("http://www.imsproject.org/xsd/imscp_rootv1p1p2"); //$NON-NLS-1$
    Namespace IMSCP_NAMESPACE_112_EMBEDDED = Namespace.getNamespace(IMSCP_NAMESPACE_PREFIX, IMSCP_NAMESPACE_112.getURI());
    // CP Version 1.1.1 
    Namespace IMSCP_NAMESPACE_111 = Namespace.getNamespace("http://www.imsproject.org/xsd/ims_cp_rootv1p1"); //$NON-NLS-1$
    Namespace IMSCP_NAMESPACE_111_EMBEDDED = Namespace.getNamespace(IMSCP_NAMESPACE_PREFIX, IMSCP_NAMESPACE_111.getURI());
    // CP Bogus Namespace as in WebCT
    Namespace CP_BOGUS_NAMESPACE1 = Namespace.getNamespace("http://www.imsproject.org/content"); //$NON-NLS-1$
    Namespace CP_BOGUS_NAMESPACE1_EMBEDDED = Namespace.getNamespace(IMSCP_NAMESPACE_PREFIX, CP_BOGUS_NAMESPACE1.getURI());
    
    /*
     * ADL SCORM
     */ 
    String ADLCP_NAMESPACE_PREFIX = "adlcp"; //$NON-NLS-1$
    // ADL SCORM Version 1.2
    Namespace ADLCP_NAMESPACE_12 = Namespace.getNamespace("http://www.adlnet.org/xsd/adlcp_rootv1p2"); //$NON-NLS-1$
    Namespace ADLCP_NAMESPACE_12_EMBEDDED = Namespace.getNamespace(ADLCP_NAMESPACE_PREFIX, ADLCP_NAMESPACE_12.getURI());
    // ADL SCORM Version 1.3
    Namespace ADLCP_NAMESPACE_13 = Namespace.getNamespace("http://www.adlnet.org/xsd/adlcp_v1p3"); //$NON-NLS-1$
    Namespace ADLCP_NAMESPACE_13_EMBEDDED = Namespace.getNamespace(ADLCP_NAMESPACE_PREFIX, ADLCP_NAMESPACE_13.getURI());

    /**
     * IMS Learning Design
     */ 
    String IMSLD_NAMESPACE_PREFIX = "imsld"; //$NON-NLS-1$
    // LD Version 1.0
    Namespace IMSLD_NAMESPACE_100 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsld_v1p0"); //$NON-NLS-1$
    Namespace IMSLD_NAMESPACE_100_EMBEDDED = Namespace.getNamespace(IMSLD_NAMESPACE_PREFIX, IMSLD_NAMESPACE_100.getURI());
    String    IMSLD_SCHEMALOCATION_100A = "http://www.imsglobal.org/xsd/IMS_LD_Level_A.xsd"; //$NON-NLS-1$
    String    IMSLD_SCHEMALOCATION_100B = "http://www.imsglobal.org/xsd/IMS_LD_Level_B.xsd"; //$NON-NLS-1$
    String    IMSLD_SCHEMALOCATION_100C = "http://www.imsglobal.org/xsd/IMS_LD_Level_C.xsd"; //$NON-NLS-1$
    
}
