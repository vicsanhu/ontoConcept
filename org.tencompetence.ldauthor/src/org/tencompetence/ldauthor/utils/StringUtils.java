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

import java.util.regex.Pattern;




/**
 * Some useful String Utilities
 *
 * @author Phillip Beauvoir
 * @version $Id: StringUtils.java,v 1.9 2009/03/25 15:57:46 phillipus Exp $
 */
public final class StringUtils {
    
    /**
     * The reg expression for HTML tags
     */
    static final String HTML_TAG_REGEX = "<[^>]+>"; //$NON-NLS-1$

    /** 
     * The compiled pattern to match HTML tags
     */
    static final Pattern HTML_TAG_REGEX_PATTERN = Pattern.compile(HTML_TAG_REGEX);
    
    /**
     * Empty String
     */
    final static String ZERO_LENGTH_STRING = ""; //$NON-NLS-1$

    /**
     * Strip tags out of a String
     * @param str
     * @return
     */
    public static String stripTags(String str) {
        
        if (str == null || str.indexOf('<') == -1 || str.indexOf('>') == -1) {
            return str;
        }
        
        str = HTML_TAG_REGEX_PATTERN.matcher(str).replaceAll(""); //$NON-NLS-1$
        return str;
    }

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
     * @param s
     * @return A String consisting of x amount of the same character s
     *         If amount is zero or less, the empty String is returned.
     */
    public static String getStringCharacters(String s, int amount) {
        if(amount < 1) {
            return ""; //$NON-NLS-1$
        }
        
        String ret = s;
        
        for(int i = 1; i < amount; i++) {
            ret += s;
        }
        
        return ret;
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
    
    /**
     * Convert all illegal characters to "_"
     * @param id
     * @return
     */
    public static String getValidID(String id) {
        if(!StringUtils.isSet(id)) {
            return id;
        }
        return id.replaceAll("[^a-zA-Z0-9-]", "_"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}