//------------------------------------------------------------------------------
// Copyright (c) 2005, 2007 IBM Corporation and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.eclipse.epf.common.html;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.regex.Pattern;

import org.eclipse.epf.common.utils.FileUtil;
import org.eclipse.epf.common.utils.StrUtil;
import org.w3c.tidy.Tidy;

/**
 * Pretty-formats HTML source and makes it XHTML compliant.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
public class HTMLFormatter {

	private static final String HTML_BODY_START_TAG = "<body"; //$NON-NLS-1$

	private static final String HTML_BODY_END_TAG = "</body>"; //$NON-NLS-1$

	private static final int HTML_BODY_START_TAG_LENGTH = HTML_BODY_START_TAG
			.length();

	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.epf.common.HTMLFormatter"; //$NON-NLS-1$

	private int lineWidth;

	private boolean indent;

	private int indentSize;

	private String lastErrorStr;

	/*
	 * String location = m.group(1);
	 * String lineStr = m.group(2);
	 * String columnStr = m.group(3);
	 * String errorMsg = m.group(4);
	 */
	public static final Pattern jTidyErrorParser = Pattern
			.compile(
					"(line\\s+(\\d+)\\s+column\\s+(\\d+))\\s+-\\s+(.+)", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	/**
	 * Creates a new instance.
	 */
	public HTMLFormatter() {
		this(132, true, 4);
	}

	/**
	 * Creates a new instance.
	 */
	public HTMLFormatter(int lineWidth, boolean indent, int indentSize) {
		this.lineWidth = lineWidth;
		this.indent = indent;
		this.indentSize = indentSize;
	}

	/**
	 * Sets the maximum character width of a line.
	 * 
	 * @param lineWidth
	 *            The line width (in number of characters).
	 */
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Enables or disables tags indent.
	 * 
	 * @param indent
	 *            If true, ident the tags.
	 */
	public void setIndent(boolean indent) {
		this.indent = indent;
	}

	/**
	 * Sets the indent size.
	 * 
	 * @param indentSize
	 *            The indent size (in number of characters).
	 */
	public void setIndentSize(int indentSize) {
		this.indentSize = indentSize;
	}

	/**
	 * Formats the given HTML source.
	 * 
	 * @param html
	 *            The HTML source.
	 * @return The pretty-formatted HTML source.
	 */
	public String formatHTML(String html) throws UnsupportedEncodingException {
		return formatHTML(html, false, false, false, false);
	}

	/**
	 * Formats the given HTML source.
	 * 
	 * @param html The HTML source.
	 * @param returnBodyOnly if false, return full HTML document or body content based on what is passed in.  if true, always return body content only
	 * @param forceOutput if true, return cleaned HTML even if errors. if false, will clean minor problems and return clean HTML, but on a major error, will set getLastErrorStr() and return passed-in html
	 * @param makeBare set to true for cleaning MS HTML
	 * @param word2000 set to true for cleaning MS Word 2000 HTML 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String formatHTML(String html, boolean returnBodyOnly, boolean forceOutput, boolean makeBare, boolean word2000) throws UnsupportedEncodingException {
		lastErrorStr = null;
		if (html == null || html.length() == 0) {
			return html;
		}

		Tidy tidy = new Tidy();
        // !!!PB HACK!!! Turning this off stops the addition of the line <?xml version="1.0"?>
		//tidy.setXHTML(true);
		tidy.setDropEmptyParas(false);
		tidy.setDropFontTags(false);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setSmartIndent(false);
		tidy.setTidyMark(false);
		tidy.setWraplen(lineWidth);
		tidy.setIndentAttributes(false);
		tidy.setIndentContent(indent);
		tidy.setSpaces(indentSize);
		tidy.setInputEncoding("UTF-16"); //$NON-NLS-1$
		tidy.setOutputEncoding("UTF-16"); //$NON-NLS-1$
		// this will add <p> around each text block (?that isn't in a block already?)
//		tidy.setEncloseBlockText(true);
		// setting this seemed to prevent JTidy from indenting the source
//		tidy.setPrintBodyOnly(true);
		
		if (forceOutput) {
			// output document even if errors are present
			tidy.setForceOutput(true);
		}
		if (makeBare) {
			// remove MS clutter
			tidy.setMakeBare(true);
		}
		if (word2000) {
			// draconian Word2000 cleaning
			tidy.setWord2000(true);
		}

		Reader input = new StringReader(html);
		Writer output = new StringWriter();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tidy.setErrout(pw);
		tidy.parse(input, output);
		String error = sw.getBuffer().toString();
		if (error != null && error.length() > 0
				&& error.startsWith("line") && error.indexOf("column") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			lastErrorStr = error;
			if (!forceOutput) {
				// if forceOutput is true, JTidy will return clean HTML so don't return here
				return html;
			}
		}

		String formattedHTML = output.toString();
		formattedHTML = StrUtil.getEscapedHTML(formattedHTML);
		
		String htmlStartUpper = html.substring(0, Math.min(10, html.length())).toUpperCase();

		if (returnBodyOnly) {
		// !!!PB HACK!!! We want to preserve all of the html
		//if (returnBodyOnly || (!htmlStartUpper.startsWith("<!DOCTYPE") && !htmlStartUpper.startsWith("<HTML"))) { //$NON-NLS-1$ //$NON-NLS-2$
			int startBodyTag = formattedHTML.indexOf(HTML_BODY_START_TAG);
			int start = -1;
			if (startBodyTag != -1) {
				start = formattedHTML.indexOf(">",startBodyTag); //$NON-NLS-1$
			}
			int end = formattedHTML.indexOf(HTML_BODY_END_TAG);
			if (start == -1 || end == -1) {
				return ""; //$NON-NLS-1$
			}
			start += 1;
			if (start >= end) {
				return ""; //$NON-NLS-1$
			}
			start += FileUtil.LINE_SEP_LENGTH;
			end -= FileUtil.LINE_SEP_LENGTH;
			if (indent && indentSize > 0) {
				end -= indentSize;
			}
			if (start >= end) {
				return ""; //$NON-NLS-1$
			}
			String result = formattedHTML.substring(start, end);
			if (indent && indentSize > 0) {
				String indentStr = getIndentStr(indentSize * 2);
				result = fixIndentation(result, indentStr);
				return result;
			}
		}
		return formattedHTML;
	}
		
	/**
	 * Returns the indent string.
	 */
	private static String getIndentStr(int indentLength) {
		if (indentLength == 0) {
			return ""; //$NON-NLS-1$
		}
		StringBuffer indentStr = new StringBuffer();
		for (int i = 0; i < indentLength; i++) {
			indentStr.append(' ');
		}
		return indentStr.toString();
	}

	public static final String PRE_TAG_START = "<pre>"; //$NON-NLS-1$

	public static final String PRE_TAG_END = "</pre>"; //$NON-NLS-1$

	public static final int PRE_TAG_END_LENGTH = PRE_TAG_END.length();

	/**
	 * Undo the JTidy indent, but ignore &lt;pre&gt; tags
	 * 
	 * @param html
	 * @param indentStr
	 * @return
	 */
	private static String fixIndentation(String html, String indentStr) {
		if (html.startsWith(indentStr)) {
			html = html.substring(indentStr.length());
		}
		StringBuffer strBuf = new StringBuffer();
		int pre_index = -1;
		int last_pre_end_index = -1;
		while ((pre_index = html.indexOf(PRE_TAG_START, last_pre_end_index)) != -1) {
			strBuf.append(html.substring(
					last_pre_end_index < 0 ? 0 : last_pre_end_index
							+ PRE_TAG_END_LENGTH, pre_index).replaceAll(
					"\r\n" + indentStr, "\r\n")); //$NON-NLS-1$ //$NON-NLS-2$
			last_pre_end_index = html.indexOf(PRE_TAG_END, pre_index);
			if (last_pre_end_index != -1) {
				strBuf.append(html.substring(pre_index, last_pre_end_index
						+ PRE_TAG_END_LENGTH));
			} else {
				// found <pre>, but no ending </pre> - shouldn't ever get here
				// append rest of string and return it
				strBuf.append(html.substring(pre_index));
				return strBuf.toString();
			}
		}
		strBuf.append(html.substring(
				last_pre_end_index < 0 ? 0 : last_pre_end_index
						+ PRE_TAG_END_LENGTH).replaceAll("\r\n" + indentStr, //$NON-NLS-1$
				"\r\n")); //$NON-NLS-1$
		return strBuf.toString();
	}

	public String getLastErrorStr() {
		return lastErrorStr;
	}
}
