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
package org.tencompetence.widgetadvert;

/**
 * Some constants used for parsing the xml structure
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetConstants.java,v 1.2 2008/04/24 10:25:14 phillipus Exp $
 *
 */
public interface IWidgetConstants {
	String WIDGETS_ELEMENT = "widgets"; //$NON-NLS-1$
	String WIDGET_ELEMENT = "widget"; //$NON-NLS-1$
	String IDENTIFIER_ATTRIBUTE = "identifier"; //$NON-NLS-1$
	String TITLE_ELEMENT = "title"; //$NON-NLS-1$
	String DESCRIPTION_ELEMENT = "description"; //$NON-NLS-1$
	String ICON_ELEMENT = "icon"; //$NON-NLS-1$
	String PARAMETER_ELEMENT = "parameter"; //$NON-NLS-1$
	String UNTITLED_WIDGET = "untitled widget"; //$NON-NLS-1$
	String NO_ICON_DEFINED = Messages.IWidgetConstants_0;
	String NO_PARAM_DEFINED = Messages.IWidgetConstants_1;		
}
