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
package org.tencompetence.imsldmodel.types.impl;

import org.tencompetence.imsldmodel.types.IDurationType;


/**
 * DurationType
 * 
 * @author Phillip Beauvoir
 * @version $Id: DurationType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class DurationType implements IDurationType {
    
    private int fYears, fMonths, fDays, fHours, fMins, fSecs;
    
    public DurationType() {
    }

    public DurationType(String durationString) {
        setDurationString(durationString);
    }

    /**
     * @return The duration String or "P0Y" if all values are zero.
     * We have to return at least something to keep the validator happy
     */
    public String getDurationString() {
        String s = ""; //$NON-NLS-1$
        
        if(getYears() != 0) {
            s = getYears() + "Y"; //$NON-NLS-1$
        }
        if(getMonths() != 0) {
            s += getMonths() + "M"; //$NON-NLS-1$
        }
        if(getDays() != 0) {
            s += getDays() + "D"; //$NON-NLS-1$
        }
        
        if(getHours() != 0 || getMinutes() != 0 || getSeconds() != 0) {
            s += "T"; //$NON-NLS-1$
        }
        
        if(getHours() != 0) {
            s += getHours() + "H"; //$NON-NLS-1$
        }
        
        if(getMinutes() != 0) {
            s += getMinutes() + "M"; //$NON-NLS-1$
        }

        if(getSeconds() != 0) {
            s += getSeconds() + "S"; //$NON-NLS-1$
        }

        return s == "" ? "P0Y" : "P" + s; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void setDurationString(String s) {
        fYears = parseValue(s, "y"); //$NON-NLS-1$
        fMonths = parseValue(s, "m"); //$NON-NLS-1$
        fDays = parseValue(s, "d"); //$NON-NLS-1$
        fHours = parseValue(s, "h"); //$NON-NLS-1$
        fMins = parseValue(s, "mm"); //$NON-NLS-1$
        fSecs = parseValue(s, "s"); //$NON-NLS-1$
    }

    public int getDays() {
        return fDays;
    }

    public void setDays(int days) {
        fDays = days;
    }

    public int getHours() {
        return fHours;
    }

    public void setHours(int hours) {
        fHours = hours;
    }

    public int getMinutes() {
        return fMins;
    }

    public void setMinutes(int mins) {
        fMins = mins;
    }

    public void setMonths(int months) {
        fMonths = months;
    }

    public int getMonths() {
        return fMonths;
    }

    public int getSeconds() {
        return fSecs;
    }

    public void setSeconds(int secs) {
        fSecs = secs;
    }

    public int getYears() {
        return fYears;
    }

    public void setYears(int years) {
        fYears = years;
    }

    /**
     * Return the value of the numbers just before the delimiter
     * @param delimiter Value of 'y', 'm', 'd', 'h', 'mm', or 's'
     * @return
     */
    private int parseValue(String durationString, String delimiter) {
        // Empty string
        if(isEmptyOrNull(durationString)) {
            return 0;
        }
        
        // No "P" start
        if(!hasStartDesignator(durationString)) {
            return 0;
        }
        
        // Lower case copy
        String d = durationString.toLowerCase();
        
        // If months, then if there's a Time delimiter chop it off before that so we don't confuse 
        // with the "M" for minutes later in the string
        if("m".equals(delimiter)) { //$NON-NLS-1$
            int t = d.indexOf("t"); //$NON-NLS-1$
            if(t != -1) {
                d = d.substring(0, d.indexOf("t")); //$NON-NLS-1$
            }
        }
        
        // If hours, mins or secs look for the T delimiter
        else if("h".equals(delimiter) || "mm".equals(delimiter) || "s".equals(delimiter)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            // No Time delimiter
            if(!hasTimeDelimiter(durationString)) {
                return 0;
            }
            
            // Create new String at T delimiter
            try {
                d = d.substring(d.indexOf("t")); //$NON-NLS-1$
            }
            catch(StringIndexOutOfBoundsException ex) {
                return 0;
            }
            
            // If "mm" (minutes) then change to "m"
            if("mm".equals(delimiter)) { //$NON-NLS-1$
                delimiter = "m"; //$NON-NLS-1$
            }
        }
        
        // Get end position of delimiter
        int endpos = d.indexOf(delimiter);
        if(endpos <= 0) {
            return 0;
        }
        
        // Get start position of number by working backwards from end position until we come
        // to a non-number character or the start of the string
        int startpos;
        for(startpos = endpos - 1; startpos >= 0; startpos--) {
            char c = d.charAt(startpos);
            if(!Character.isDigit(c)) {
                break;
            }
        }
        
        if(startpos < 0) {
            return 0;
        }
        
        String num = d.substring(startpos + 1, endpos);
        
        try {
            return Integer.parseInt(num);
        }
        catch(NumberFormatException ex) {
            return 0;
        }
    }

    
    /**
     * @return True if the first character of the string starts with "P"
     */
    private boolean hasStartDesignator(String durationString) {
        if(isEmptyOrNull(durationString)) {
            return false;
        }

        String d = durationString.toLowerCase();
        return d.indexOf("p") == 0; //$NON-NLS-1$
    }
    
    /**
     * @return True if there is a Time "T" delimiter
     */
    private boolean hasTimeDelimiter(String durationString) {
        if(isEmptyOrNull(durationString)) {
            return false;
        }

        String d = durationString.toLowerCase();
        return d.indexOf("t") != -1; //$NON-NLS-1$
    }

    /**
     * @return True if the string is null or the empty tring
     */
    private boolean isEmptyOrNull(String s) {
        return s == null || "".equals(s); //$NON-NLS-1$
    }
}
