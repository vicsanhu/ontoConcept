/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.tencompetence.jdom.JDOMXMLUtils;


/**
 * Example code to show how to create new LD Model and open and save it
 * 
 * @author Phillip Beauvoir
 * @version $Id: Example.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class Example implements IMSNamespaces {

    public static void main(String[] args) {
        // Create a new, blank LDModel..
        ILDModel ldModel = new LDModel();
        
        // But you must give it a file name to save to!
        File manifestFile = new File("imsmanifest.xml"); //$NON-NLS-1$
        ldModel.setManifestFile(manifestFile);
        
        // If you want to populate the model from an existing file then load it...
        try {
            loadModel(ldModel);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Now that we have the model we can manipulate it...See the ReCourse LD editor for examples...
        
        // And we can save the LD Model back to the manifest file
        try {
            saveModel(ldModel);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        // If you want to zip the contents into a zip file see the ReCourse LD editor for examples...
    }
    
    /**
     * Load the LD model from manifest file
     * 
     * @throws IOException
     * @throws JDOMException
     */
    private static void loadModel(ILDModel ldModel) throws IOException, JDOMException {
        Document doc = JDOMXMLUtils.readXMLFile(ldModel.getManifestFile());
        Element rootElement = doc.getRootElement();
        
        // Manifest ID
        ldModel.setManifestIdentifier(rootElement.getAttributeValue(LDModelFactory.IDENTIFIER));
        
        Element ldRoot = null;
        
        /*
         * Get the organizations element
         */
        Element orgsElement = rootElement.getChild("organizations", rootElement.getNamespace()); //$NON-NLS-1$
        if(orgsElement != null) {
            /*
             * Get the LD element
             */
            ldRoot = orgsElement.getChild(LDModelFactory.LEARNING_DESIGN, IMSLD_NAMESPACE_100);
        }

        /*
         * Load Resources elements **first** because we need to reference them
         */
        Element resources = rootElement.getChild(LDModelFactory.RESOURCES, rootElement.getNamespace());
        if(resources != null) {
            ldModel.getResourcesModel().fromJDOM(resources);
        }

        // Then load LD Model
        if(ldRoot != null) {
            ldModel.fromJDOM(ldRoot);
        }
    }

    /**
     * Save the LD Model to the file specified in ILDModel
     * @throws IOException
     */
    private static void saveModel(ILDModel ldModel) throws IOException {
        Document doc = new Document();
        
        /*
         * Root is CP manifest
         */ 
        Element root = new Element("manifest", IMSCP_NAMESPACE_114); //$NON-NLS-1$
        doc.setRootElement(root);
        
        /*
         * Additional namespaces
         */
        root.addNamespaceDeclaration(IMSLD_NAMESPACE_100_EMBEDDED);
        root.addNamespaceDeclaration(XSI_NAMESPACE);

        /* 
         * Add Schema Location Attribute which is constructed from Target Namespaces
         * and file names of Schemas
         */
        StringBuffer schemaLocationURI = new StringBuffer();
        
        // CP
        schemaLocationURI.append(root.getNamespace().getURI());
        schemaLocationURI.append(" ");  //$NON-NLS-1$
        schemaLocationURI.append(IMSCP_SCHEMALOCATION_114);
        schemaLocationURI.append(" ");  //$NON-NLS-1$
        
        // Schema location depends on LD level
        String level = ldModel.getLevel();
        
        schemaLocationURI.append(IMSLD_NAMESPACE_100.getURI());
        schemaLocationURI.append(" ");  //$NON-NLS-1$
        if("B".equalsIgnoreCase(level)) { //$NON-NLS-1$
            schemaLocationURI.append(IMSLD_SCHEMALOCATION_100B);
        }
        else if("C".equalsIgnoreCase(level)) { //$NON-NLS-1$
            schemaLocationURI.append(IMSLD_SCHEMALOCATION_100C);
        }
        else {
            schemaLocationURI.append(IMSLD_SCHEMALOCATION_100A);
        }
        
        root.setAttribute(XSI_SCHEMALOCATION, schemaLocationURI.toString(), XSI_NAMESPACE);

        /*
         * Manifest Identifier
         */
        root.setAttribute(LDModelFactory.IDENTIFIER, ldModel.getManifestIdentifier());
        
        /*
         * Add Learning Design to "organizations" element with LD Namespace
         */
        Element orgs = new Element("organizations", root.getNamespace()); //$NON-NLS-1$
        root.addContent(orgs);

        Element ldRoot = ldModel.toJDOM();
        orgs.addContent(ldRoot);
        
        /*
         * Resources element
         */
        Element resources = ldModel.getResourcesModel().toJDOM();
        root.addContent(resources);

        // And save the model
        JDOMXMLUtils.write2XMLFile(doc, ldModel.getManifestFile());
    }

}
