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
package org.tencompetence.ldauthor.ui.editors.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import org.tencompetence.ldauthor.LDAuthorPlugin;

/**
 * Simple Document Provider
 * 
 * @author Phillip Beauvoir
 * @version $Id: SimpleDocumentProvider.java,v 1.2 2008/04/24 10:15:27 phillipus Exp $
 */
public class SimpleDocumentProvider
extends AbstractDocumentProvider {

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(java.lang.Object)
     */
    @Override
    protected IDocument createDocument(Object element) throws CoreException {
        if(element instanceof IEditorInput) {
            IDocument document = new Document();
            if(setDocumentContent(document, (IEditorInput)element)) {
                setupDocument(document);
            }
            return document;
        }

        return null;
    }

    private boolean setDocumentContent(IDocument document, IEditorInput input)
            throws CoreException {
        // Should handle encoding
        Reader reader;
        try {
            if(input instanceof IPathEditorInput) reader = new FileReader(
                    ((IPathEditorInput)input).getPath().toFile());
            else reader = new FileReader(input.getName());
        }
        catch(FileNotFoundException e) {
            // return empty document and save later
            return true;
        }

        try {
            setDocumentContent(document, reader);
            return true;
        }
        catch(IOException e) {
            throw new CoreException(
                    new Status(
                            IStatus.ERROR,
                            "Straker Text Editor", IStatus.OK, "error reading file", e)); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private void setDocumentContent(IDocument document, Reader reader)
            throws IOException {
        Reader in = new BufferedReader(reader);
        try {

            StringBuffer buffer = new StringBuffer(512);
            char[] readBuffer = new char[512];
            int n = in.read(readBuffer);
            while(n > 0) {
                buffer.append(readBuffer, 0, n);
                n = in.read(readBuffer);
            }

            document.set(buffer.toString());

        }
        finally {
            in.close();
        }
    }

    /**
     * Set up the document - default implementation does nothing.
     * 
     * @param document the new document
     */
    protected void setupDocument(IDocument document) {
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createAnnotationModel(java.lang.Object)
     */
    @Override
    protected IAnnotationModel createAnnotationModel(Object element) {
        return null;
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(org.eclipse.core.runtime.IProgressMonitor,
     *      java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
     */
    @Override
    protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
        if(element instanceof IPathEditorInput) {
            IPathEditorInput pei = (IPathEditorInput)element;
            IPath path = pei.getPath();
            File file = path.toFile();

            try {
                file.createNewFile();

                if(file.exists()) {
                    if(file.canWrite()) {
                        Writer writer = new FileWriter(file);
                        writeDocumentContent(document, writer, monitor);
                    }
                    else {
                        // Should prompt to SaveAs
                        throw new CoreException(new Status(
                                        IStatus.ERROR,
                                        LDAuthorPlugin.PLUGIN_ID, IStatus.OK, "File is read-only", null));  //$NON-NLS-1$
                    }
                }
                else {
                    throw new CoreException(new Status(
                                    IStatus.ERROR,
                                    LDAuthorPlugin.PLUGIN_ID, IStatus.OK, "Error creating file", null)); //$NON-NLS-1$
                }
            }
            catch(IOException e) {
                throw new CoreException(new Status(IStatus.ERROR,
                        LDAuthorPlugin.PLUGIN_ID, IStatus.OK, "Error when saving file", e));  //$NON-NLS-1$
            }

        }
    }

    private void writeDocumentContent(IDocument document, Writer writer,
            IProgressMonitor monitor) throws IOException {
        Writer out = new BufferedWriter(writer);
        try {
            out.write(document.get());
        }
        finally {
            out.close();
        }
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
        return null;
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isModifiable(java.lang.Object)
     */
    @Override
    public boolean isModifiable(Object element) {
        if(element instanceof IPathEditorInput) {
            IPathEditorInput pei = (IPathEditorInput)element;
            File file = pei.getPath().toFile();
            return file.canWrite() || !file.exists(); // Allow to edit new files
        }
        return false;
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isReadOnly(java.lang.Object)
     */
    @Override
    public boolean isReadOnly(Object element) {
        return !isModifiable(element);
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isStateValidated(java.lang.Object)
     */
    @Override
    public boolean isStateValidated(Object element) {
        return true;
    }
}