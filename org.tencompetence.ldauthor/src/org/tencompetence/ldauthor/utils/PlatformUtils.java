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
package org.tencompetence.ldauthor.utils;

import java.io.File;

import org.eclipse.core.runtime.Platform;


/**
 * PlatformUtils
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlatformUtils.java,v 1.2 2008/04/24 10:15:10 phillipus Exp $
 */
public class PlatformUtils {

    /**
     * @return The App Data folder for each platform
     */
    public static File getApplicationDataFolder() {
        // Windows
        if(Platform.getOS().equals(Platform.OS_WIN32)) {
            return new File(System.getenv("APPDATA")); //$NON-NLS-1$
        }
        
        // Linux
        if(Platform.getOS().equals(Platform.OS_LINUX)) {
            return new File(System.getProperty("user.home")); //$NON-NLS-1$
        }
        
        // Mac
        if(Platform.getOS().equals(Platform.OS_MACOSX)) {
            return new File(System.getProperty("user.home")); //$NON-NLS-1$
        }

        // Default
        return new File(System.getProperty("user.home")); //$NON-NLS-1$
    }
}
