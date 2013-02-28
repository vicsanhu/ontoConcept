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
package org.tencompetence.ldauthor.opendock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;


/**
 * Info structure for uploading a UoL
 * 
 * @author Phillip Beauvoir
 * @version $Id: UOL_UploadInfo.java,v 1.4 2008/04/25 11:46:13 phillipus Exp $
 */
public class UOL_UploadInfo implements IOpenDockConstants {
    
    int netID = 1;  // Default network
    
    Hashtable<String, String> identity;
    String repositoryID;
    
    public File file;
    public String metaTitle = ""; //$NON-NLS-1$
    public String metaShortDesc = ""; //$NON-NLS-1$
    public String metaLongDesc = ""; //$NON-NLS-1$
    public String cc_CommercialUse = "n"; //$NON-NLS-1$
    public String cc_DerivativeUse = "sa"; //$NON-NLS-1$
    public String cc_Jurisdiction = "generic"; //$NON-NLS-1$
    public String cc_Language = "en_GB"; //$NON-NLS-1$
    public String cc_CopyRightHolder = ""; //$NON-NLS-1$
    public String cc_CopyrightYear = "2008"; //$NON-NLS-1$
    
    public UOL_UploadInfo() {
    }
    
    public Object[] getParams() throws IOException {
        if(identity == null) {
            throw new RuntimeException("Identity cannot be null, must be set."); //$NON-NLS-1$
        }
        
        if(repositoryID == null) {
            throw new RuntimeException("Repository ID cannot be null, must be set."); //$NON-NLS-1$
        }
        
        // File bytes
        byte[] data = readFileBytes(file);

        Hashtable<String, Object> paramTable1 = new Hashtable<String, Object>();
        paramTable1.put(QUERY_META_TITLE, metaTitle);
        paramTable1.put(QUERY_META_SHORTDESC, metaShortDesc);
        paramTable1.put(QUERY_META_DESCRIPTION, metaLongDesc);
        paramTable1.put("cc_commercialUse", cc_CommercialUse); //$NON-NLS-1$
        paramTable1.put("cc_derivativeWorks", cc_DerivativeUse); //$NON-NLS-1$
        paramTable1.put("cc_jurisdiction", cc_Jurisdiction); //$NON-NLS-1$
        paramTable1.put("cc_language", cc_Language); //$NON-NLS-1$
        paramTable1.put("cc_copyrightholder", cc_CopyRightHolder); //$NON-NLS-1$
        paramTable1.put("cc_copyrightyear", cc_CopyrightYear); //$NON-NLS-1$
        paramTable1.put(QUERY_COMPRESSED_SIZE, data.length);  // Cannot be long type
        paramTable1.put("container_zipfile", file.getName()); //$NON-NLS-1$
        paramTable1.put(QUERY_CONTAINER_TYPE, UOL);
        
        Hashtable<String, Object> paramTable2 = new Hashtable<String, Object>();
        paramTable2.put("name", file.getName()); //$NON-NLS-1$
        paramTable2.put("type", "application/zip"); //$NON-NLS-1$ //$NON-NLS-2$
        paramTable2.put("tmp_name", file.getName()); //$NON-NLS-1$
        paramTable2.put("error", 0); //$NON-NLS-1$
        paramTable2.put("size", data.length);  // Cannot be long type //$NON-NLS-1$

        return new Object[] { identity, netID, repositoryID, "", paramTable1, data, paramTable2 }; //$NON-NLS-1$
    }
    
    private byte[] readFileBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        byte[] data = new byte[is.available()];
        is.read(data);
        return data;
    }

}
