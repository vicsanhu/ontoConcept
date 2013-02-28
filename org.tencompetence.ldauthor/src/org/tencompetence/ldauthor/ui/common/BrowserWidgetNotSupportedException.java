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
package org.tencompetence.ldauthor.ui.common;

import org.tencompetence.ldauthor.ui.views.browser.Messages;


/**
 * Exception for Browser Not supprted
 * 
 * @author Phillip Beauvoir
 * @version $Id: BrowserWidgetNotSupportedException.java,v 1.1 2007/09/03 12:07:19 phillipus Exp $
 */
public class BrowserWidgetNotSupportedException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		/**
     * Constructs a <code>SchemaException</code> with <code>null</code>
     * as its error detail message.
     */
    public BrowserWidgetNotSupportedException() {
        super(Messages.BrowserWidgetNotSupportedException_0, null);
    }
    
    /**
     * Constructs a <code>SchemaException</code> with <code>null</code>
     * as its error detail message.
     */
    public BrowserWidgetNotSupportedException(Throwable cause) {
        super(Messages.BrowserWidgetNotSupportedException_0, cause);
    }
    
    /**
     * Constructs a <code>SchemaException</code> with the specified detail
     * message. The error message string <code>s</code> can later be
     * retrieved by the <code>{@link java.lang.Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param   s   the detail message.
     */
    public BrowserWidgetNotSupportedException(String s) {
        super(s);
    }
}
