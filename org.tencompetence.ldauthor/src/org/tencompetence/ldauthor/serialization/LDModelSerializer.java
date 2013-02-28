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
package org.tencompetence.ldauthor.serialization;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseInfoModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.ZipUtils;


/**
 * LD Model Serializer
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelSerializer.java,v 1.33 2009/06/18 18:02:29 phillipus Exp $
 */
public class LDModelSerializer implements ILDAuthorNamespaces, IMSNamespaces {
    
    private IReCourseLDModel fLDModel;
    
    /*
     * Preserve the original root element for later functionality:
     *          Preserve xml comments
     *          Preserve Metadata tags
     *          Preserve other original information
     */
    private Element fRootElement;
    
    public LDModelSerializer(IReCourseLDModel ldModel) {
        fLDModel = ldModel;
    }
    
    /**
     * Load the model from file into an ILDModel
     * 
     * @throws IOException
     * @throws JDOMException
     */
    public void loadModel() throws IOException, JDOMException {
        Document doc = JDOMXMLUtils.readXMLFile(fLDModel.getManifestFile());
        fRootElement = doc.getRootElement();
        
        // Manifest ID
        fLDModel.setManifestIdentifier(fRootElement.getAttributeValue(LDModelFactory.IDENTIFIER));
        
        Element ldRoot = null;
        
        /*
         * Get the organizations element
         */
        Element orgsElement = fRootElement.getChild("organizations", fRootElement.getNamespace()); //$NON-NLS-1$
        if(orgsElement != null) {
            /*
             * Get the LD element
             */
            ldRoot = orgsElement.getChild(LDModelFactory.LEARNING_DESIGN, IMSLD_NAMESPACE_100);
        }

        /*
         * Load Resources elements **first** because we need to reference them
         */
        Element resources = fRootElement.getChild(LDModelFactory.RESOURCES, fRootElement.getNamespace());
        if(resources != null) {
            fLDModel.getResourcesModel().fromJDOM(resources);
        }

        // Load LD Model
        if(ldRoot != null) {
            fLDModel.fromJDOM(ldRoot);
        }
    }

    /**
     * Save the LD Model to the file specified in ILDModel
     * @throws IOException
     */
    public void saveModel() throws IOException {
        Document doc = new Document();
        
        /*
         * Add Comments
         */
        Comment comment = new Comment(Messages.LDModelSerializer_0);
        doc.addContent(comment);
        comment = new Comment(Messages.LDModelSerializer_1 + fLDModel.getReCourseInfoModel().getDateCreated());
        doc.addContent(comment);
        comment = new Comment(Messages.LDModelSerializer_2 + new Date().toString());
        doc.addContent(comment);
        
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
        root.addNamespaceDeclaration(LDAUTHOR_NAMESPACE_EMBEDDED);

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
        
        // Ensure correct level first
        fLDModel.ensureIsCorrectLevel();
        
        // Schema location depends on LD level
        String level = fLDModel.getLevel();
        
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
        
        // ReCourse Schema Location
        schemaLocationURI.append(" "); //$NON-NLS-1$
        schemaLocationURI.append(LDAUTHOR_NAMESPACE.getURI());
        schemaLocationURI.append(" "); //$NON-NLS-1$
        schemaLocationURI.append(LDAUTHOR_SCHEMALOCATION);

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

        JDOMXMLUtils.write2XMLFile(doc, fLDModel.getManifestFile());
        
        /*
         * Take this opportunity to ensure that the latest ld-author Schema file is present in the target folder...
         */
        File schemaFileSource = new File(LDAuthorPlugin.getDefault().getSchemaFolder(), LDAUTHOR_SCHEMALOCATION);
        File schemaFileTarget = new File(fLDModel.getRootFolder(), LDAUTHOR_SCHEMALOCATION);
        FileUtils.copyFile(schemaFileSource, schemaFileTarget, false);
    }
    
    /**
     * Create the Zip file by including every file in the LD root folder
     * @param zipFile
     * @param includeReCourseInfo
     * @throws IOException
     * @throws JDOMException
     */
    public void createZipFile(File zipFile, boolean includeReCourseInfo) throws IOException, JDOMException {
        ZipOutputStream zOut = null;
        
        try {
            // Delete any existing zip first
            zipFile.delete();
            
            // Start a zip stream
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            zOut = new ZipOutputStream(out);

            // Root folder
            File rootFolder = fLDModel.getRootFolder();
            
            // If we need to strip out the ReCourse info...
            if(!includeReCourseInfo) {
                File originalManifest = fLDModel.getManifestFile();
                
                // Create new temp Manifest file
                File newManifest = stripReCourseInfo(fLDModel.getManifestFile());
                
                // XSD File
                File xsdFile = new File(rootFolder, "ld-author.xsd"); //$NON-NLS-1$

                // Add all files in root folder except for original manifest in root folder, this zip file and xsd file
                ZipUtils.addFolderToZip(rootFolder, zOut, new File[] {originalManifest, zipFile, xsdFile}, null);
                
                // Now add new temp manifest file
                ZipUtils.addFileToZip(newManifest, "imsmanifest.xml", zOut); //$NON-NLS-1$
            }
            else {
                // Add all files in root folder except for this zip file
                ZipUtils.addFolderToZip(rootFolder, zOut, new File[] {zipFile}, null);
            }
            
        }
        finally {
            if(zOut != null) {
                try {
                    zOut.flush();
                    zOut.close();
                }
                catch(IOException ex) {
                }
            }
        }
    }

    /**
     * Create the Zip file by only including files declared in the resources section of the manifest
     * @param zipFile target zip file
     * @param includeReCourseInfo Keep the ReCourse info in the manifest
     * @throws IOException
     * @throws JDOMException
     */
    public void createZipFileFromResources(File zipFile, boolean includeReCourseInfo) throws IOException, JDOMException {
        ZipOutputStream zOut = null;
        
        try {
            // Delete any existing zip first
            zipFile.delete();
            
            // Start a zip stream
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            zOut = new ZipOutputStream(out);
            
            // Manifest file
            File manifestFile = fLDModel.getManifestFile();
            
            // Root folder
            File rootFolder = fLDModel.getRootFolder();
            
            List<File> doneList = new ArrayList<File>();
            
            // If we need to strip out the ReCourse info
            if(!includeReCourseInfo) {
                manifestFile = stripReCourseInfo(manifestFile);
            }

            // Manifest
            ZipUtils.addFileToZip(manifestFile, "imsmanifest.xml", zOut); //$NON-NLS-1$
            doneList.add(manifestFile);
            
            // XSD files in root folder
            for(File file : rootFolder.listFiles()) {
                String name = file.getName();
                if(name.toLowerCase().endsWith(".xsd")) { //$NON-NLS-1$
                    if(!includeReCourseInfo && name.equalsIgnoreCase("ld-author.xsd")) { //$NON-NLS-1$
                        continue;  // If we strip the ReCourse tag then we don't need the XSD
                    }
                    ZipUtils.addFileToZip(file, name, zOut);
                    doneList.add(file);
                }
            }
            
            // Resource Files
            for(IResourceModel resource : fLDModel.getResourcesModel().getResources()) {
                // Resource HREF
                String href = resource.getHref();
                if(href != null) {
                    File resourceFile = new File(rootFolder, href);
                    if(resourceFile.exists() && !doneList.contains(resourceFile)) {
                        ZipUtils.addFileToZip(resourceFile, href, zOut);
                        doneList.add(resourceFile);
                    }
                }
                
                // Files HREFs
                for(IResourceFileModel fileModel : resource.getFiles()) {
                    String href2 = fileModel.getHref();
                    if(href2 != null) {
                        File file2 = new File(rootFolder, href2);
                        if(file2.exists() && !doneList.contains(file2)) {
                            ZipUtils.addFileToZip(file2, href2, zOut);
                            doneList.add(file2);
                        }
                    }
                }
            }
        }
        finally {
            if(zOut != null) {
                try {
                    zOut.flush();
                    zOut.close();
                }
                catch(IOException ex) {
                }
            }
        }
    }

    /**
     * Strip out the ReCourse tag and return the new manifest file, a temporary file
     * @return
     * @throws JDOMException 
     * @throws IOException 
     */
    public File stripReCourseInfo(File manifestFile) throws IOException, JDOMException {
        File tmpFile = new File(System.getProperty("java.io.tmpdir"), "~imsmanifest.xml"); //$NON-NLS-1$ //$NON-NLS-2$
        tmpFile.deleteOnExit();
        
        Document doc = JDOMXMLUtils.readXMLFile(manifestFile);
        Element rootElement = doc.getRootElement();
        
        /*
         * Get the organizations element
         */
        Element orgsElement = rootElement.getChild("organizations", rootElement.getNamespace()); //$NON-NLS-1$
        if(orgsElement == null) {
            return manifestFile;
        }

        /*
         * Get the LD element
         */
        Element ldRoot = orgsElement.getChild(LDModelFactory.LEARNING_DESIGN, IMSLD_NAMESPACE_100);
        if(ldRoot == null) {
            return manifestFile;
        }
        
        /*
         * Get the LD's metadata element
         */
        Element metadata = ldRoot.getChild(LDModelFactory.METADATA, IMSLD_NAMESPACE_100);
        if(metadata == null) {
            return manifestFile;
        }
        
        /*
         * ReCourse root
         */
        Element recourseElement = metadata.getChild(IReCourseInfoModel.ROOT_ELEMENT, LDAUTHOR_NAMESPACE);
        if(recourseElement == null) {
            return manifestFile;
        }
        
        // Remove it
        recourseElement.detach();
        
        // If metadata tag is now empty, remove it
        if(metadata.getChildren().isEmpty()) {
            metadata.detach();
        }
        
        JDOMXMLUtils.write2XMLFile(doc, tmpFile);
        
        return tmpFile;
    }
}
