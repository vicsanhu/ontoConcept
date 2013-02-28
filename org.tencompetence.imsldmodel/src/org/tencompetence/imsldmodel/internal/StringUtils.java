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
package org.tencompetence.imsldmodel.internal;





/**
 * Some useful String Utilities
 *
 * @author Phillip Beauvoir
 * @version $Id: StringUtils.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public final class StringUtils {
    
    /**
     * Empty String
     */
    final static String ZERO_LENGTH_STRING = ""; //$NON-NLS-1$


    /**
     * Ensures that a string is not null. Converts null strings into empty
     * strings, and leaves any other string unmodified. Use this to help
     * wrap calls to methods that return null instead of the empty string.
     * Can also help protect against implementation errors in methods that
     * are not supposed to return null. 
     * 
     * @param input input string (may be null)
     * @return input if not null, or the empty string if input is null
     */
    public static String safeString(String input) {
        if(input != null) {
            return input;
        }

        return ZERO_LENGTH_STRING;
    }
    
    /**
     * Checks that a String contains some content
     * 
     * @param input
     * @return
     */
    public static boolean isSet(String input) {
        return input != null && !ZERO_LENGTH_STRING.equals(input);
    }
    
    /**
     * Checks that a String contains some content after calling trim()
     * 
     * @param input
     * @return
     */
    public static boolean isSetAfterTrim(String input) {
        return isSet(input) && input.trim().length() > 0;
    }
    
    /**
     * @param id
     * @return If ID is a valid XML ID or IDREF
     */
    public static boolean isValidID(String id) {
        if(!isSet(id)) {
            return false;
        }
        
        return id.matches("[a-zA-Z_][\\w-_.]*"); //$NON-NLS-1$
    }
}