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
package org.tencompetence.ldauthor.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ui.views.checker.XMLException;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Validate the LD Model in memory
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelValidator.java,v 1.2 2009/07/02 12:51:01 phillipus Exp $
 */
public class LDModelValidator implements ILDAuthorNamespaces, IMSNamespaces {

    private IReCourseLDModel fLDModel;
    
    private File fFileSchemaCP114Target;
    private File fFileSchemaLD100Target;
    private File fFileSchemaReCourseTarget;
    
    private List<XMLException> fErrorList = new ArrayList<XMLException>();
    
    private ErrorHandler fErrorHandler = new ErrorHandler() {
        public void error(SAXParseException exception) throws SAXException {
            fErrorList.add(new XMLException(XMLException.ERROR, exception));
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            fErrorList.add(new XMLException(XMLException.FATAL_ERROR, exception));
        }

        public void warning(SAXParseException exception) throws SAXException {
            //fErrorList(new XMLException(XMLException.WARNING, exception));
        }
    };
    
    public LDModelValidator(IReCourseLDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public List<XMLException> validate() throws IOException, JDOMException {
        fErrorList.clear();
        
        // Ensure correct level
        fLDModel.ensureIsCorrectLevel();
        
        // Temp folder
        File tmpFolder = new File(System.getProperty("java.io.tmpdir"), "recourse.tmp");  //$NON-NLS-1$//$NON-NLS-2$
        tmpFolder.mkdirs();
        
        // Copy Schema files...
        
        File schemaCP114 = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), "/imscp/imscp_v1p2.xsd"); //$NON-NLS-1$
        File schemaLD100 = null;
        
        // Schema depends on LD level
        String level = fLDModel.getLevel();
        if("A".equals(level)) { //$NON-NLS-1$
            schemaLD100 = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), "/imsld/IMS_LD_Level_A.xsd"); //$NON-NLS-1$
        }
        if("B".equals(level)) { //$NON-NLS-1$
            schemaLD100 = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), "/imsld/IMS_LD_Level_B.xsd"); //$NON-NLS-1$
        }
        else {
            schemaLD100 = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), "/imsld/IMS_LD_Level_C.xsd"); //$NON-NLS-1$
        }
        
        //File schemaMD114 = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), "/imsmd/imsmd_v1p2p4.xsd");
        File schemaReCourse = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), LDAUTHOR_SCHEMALOCATION);
        
        fFileSchemaCP114Target = new File(tmpFolder, schemaCP114.getName());
        fFileSchemaLD100Target = new File(tmpFolder, schemaLD100.getName());
        fFileSchemaReCourseTarget = new File(tmpFolder, schemaReCourse.getName());
        
        FileUtils.copyFile(schemaCP114, fFileSchemaCP114Target, false);
        FileUtils.copyFile(schemaLD100, fFileSchemaLD100Target, false);
        FileUtils.copyFile(schemaReCourse, fFileSchemaReCourseTarget, false);
        
        // Save LD to temp folder
        File manifestFile = saveModel(tmpFolder);
        
        // And now parse it
        parse(manifestFile);
        
        // Delete temp folder
        FileUtils.deleteFolder(tmpFolder);
        
        return fErrorList;
    }
    
    /**
     * Utility method to do a quick check to see if manifestFile is a LD xml manifest
     * It does not do any XML validation
     * @param manifestFile
     * @throws JDOMException
     * @throws IOException
     */
    public static void checkIsLDManifest(File manifestFile) throws JDOMException, IOException {
        if(manifestFile == null || !manifestFile.exists()) {
            throw new FileNotFoundException("Cannot find file"); //$NON-NLS-1$
        }
        
        // Try to open it without validation
        SAXBuilder builder = new SAXBuilder(false);
        
        // This way allows UNC mapped locations to load
        Document doc = builder.build(new FileInputStream(manifestFile));
        
        // See if we have the right elements
        if(doc.hasRootElement()) {
            Element orgsElement = doc.getRootElement().getChild("organizations", doc.getRootElement().getNamespace()); //$NON-NLS-1$
            if(orgsElement != null) {
                Element ldRoot = orgsElement.getChild(LDModelFactory.LEARNING_DESIGN, IMSLD_NAMESPACE_100);
                if(ldRoot == null) {
                    throw new JDOMException("Incorrect XML file"); //$NON-NLS-1$
                }
            }
            else {
                throw new JDOMException("Incorrect XML file"); //$NON-NLS-1$
            }
        }
        else {
            throw new JDOMException("Incorrect XML file"); //$NON-NLS-1$
        }
    }
    
    private void parse(File manifestFile) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(true);
        
        builder.setErrorHandler(fErrorHandler);
        
        builder.setFeature("http://apache.org/xml/features/validation/schema", true); //$NON-NLS-1$
        
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", //$NON-NLS-1$
                IMSCP_NAMESPACE_114 + " " + fFileSchemaCP114Target.getAbsolutePath() + " " +  //$NON-NLS-1$//$NON-NLS-2$
                LDAUTHOR_NAMESPACE + " " + fFileSchemaReCourseTarget.getAbsolutePath() + " " + //$NON-NLS-1$ //$NON-NLS-2$
                IMSLD_NAMESPACE_100 + " " + fFileSchemaLD100Target.getAbsolutePath()); //$NON-NLS-1$
        
        // This way allows UNC mapped locations to load
        builder.build(new FileInputStream(manifestFile));
    }
    
    private File saveModel(File tmpFolder) throws IOException {
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
        schemaLocationURI.append(fFileSchemaCP114Target.getAbsolutePath());
        schemaLocationURI.append(" "); //$NON-NLS-1$

        // LD
        schemaLocationURI.append(IMSLD_NAMESPACE_100.getURI());
        schemaLocationURI.append(" ");  //$NON-NLS-1$
        schemaLocationURI.append(fFileSchemaLD100Target.getAbsolutePath());
        
        // ReCourse Schema Location
        schemaLocationURI.append(" "); //$NON-NLS-1$
        schemaLocationURI.append(LDAUTHOR_NAMESPACE.getURI());
        schemaLocationURI.append(" "); //$NON-NLS-1$
        schemaLocationURI.append(fFileSchemaReCourseTarget.getAbsolutePath());
        
        root.setAttribute(XSI_SCHEMALOCATION, schemaLocationURI.toString(), XSI_NAMESPACE);
        
        /*
         * Manifest Identifier
         */
        root.setAttribute(LDModelFactory.IDENTIFIER, fLDModel.getManifestIdentifier());
        
        /*
         * Add Learning Design to "organizations" element with LD Namespace
         */
        Element orgs = new Element("organizations", root.getNamespace()); //$NON-NLS-1$
        root.addContent(orgs);

        Element ldRoot = fLDModel.toJDOM();
        orgs.addContent(ldRoot);
        
        /*
         * Resources element
         */
        Element resources = fLDModel.getResourcesModel().toJDOM();
        root.addContent(resources);

        /*
         * Save it
         */
        File file = new File(tmpFolder, "imsmanifest.xml"); //$NON-NLS-1$
        JDOMXMLUtils.write2XMLFile(doc, file);
        
        return file;
    }
}
