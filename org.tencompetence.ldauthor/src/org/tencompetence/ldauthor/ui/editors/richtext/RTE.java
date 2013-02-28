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
package org.tencompetence.ldauthor.ui.editors.richtext;

import org.eclipse.epf.richtext.IRichText;
import org.eclipse.epf.richtext.IRichTextToolBar;
import org.eclipse.epf.richtext.RichTextEditor;
import org.eclipse.epf.richtext.actions.AddImageAction;
import org.eclipse.epf.richtext.actions.AddLinkAction;
import org.eclipse.epf.richtext.actions.AddOrderedListAction;
import org.eclipse.epf.richtext.actions.AddTableAction;
import org.eclipse.epf.richtext.actions.AddUnorderedListAction;
import org.eclipse.epf.richtext.actions.BoldAction;
import org.eclipse.epf.richtext.actions.ClearContentAction;
import org.eclipse.epf.richtext.actions.CopyAction;
import org.eclipse.epf.richtext.actions.CutAction;
import org.eclipse.epf.richtext.actions.FindReplaceAction;
import org.eclipse.epf.richtext.actions.FontNameAction;
import org.eclipse.epf.richtext.actions.FontSizeAction;
import org.eclipse.epf.richtext.actions.FontStyleAction;
import org.eclipse.epf.richtext.actions.IndentAction;
import org.eclipse.epf.richtext.actions.ItalicAction;
import org.eclipse.epf.richtext.actions.OutdentAction;
import org.eclipse.epf.richtext.actions.PasteAction;
import org.eclipse.epf.richtext.actions.SubscriptAction;
import org.eclipse.epf.richtext.actions.SuperscriptAction;
import org.eclipse.epf.richtext.actions.TidyActionGroup;
import org.eclipse.epf.richtext.actions.UnderlineAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorSite;


/**
 * RichTextEditor
 * 
 * @author Phillip Beauvoir
 * @version $Id: RTE.java,v 1.1 2007/09/12 16:44:32 phillipus Exp $
 */
class RTE
extends RichTextEditor
{

    public RTE(Composite parent, int style, IEditorSite editorSite, String basePath) {
        super(parent, style, editorSite, basePath);
    }
    
    public RTE(Composite parent, int style, IEditorSite editorSite) {
        super(parent, style, editorSite);
    }

    @Override
    public void fillToolBar(IRichTextToolBar toolBar) {
        toolBar.addAction(new FontStyleAction(this));
        toolBar.addAction(new FontNameAction(this));
        toolBar.addAction(new FontSizeAction(this));
        toolBar.addSeparator();     
        toolBar.addAction(new CutAction(this));
        toolBar.addAction(new CopyAction(this));
        toolBar.addAction(new PasteAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new ClearContentAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new BoldAction(this));
        toolBar.addAction(new ItalicAction(this));
        toolBar.addAction(new UnderlineAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new SubscriptAction(this));
        toolBar.addAction(new SuperscriptAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new TidyActionGroup(this));
        toolBar.addSeparator();
        toolBar.addAction(new AddOrderedListAction(this));
        toolBar.addAction(new AddUnorderedListAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new OutdentAction(this));
        toolBar.addAction(new IndentAction(this));
        toolBar.addSeparator();
        toolBar.addAction(new FindReplaceAction(this) {
            @Override
            public void execute(IRichText richText) {
                richText.getFindReplaceAction().execute(richText);
            }
        });
        toolBar.addSeparator();
        toolBar.addAction(new AddLinkAction(this));
        toolBar.addAction(new AddImageAction(this));
        toolBar.addAction(new AddTableAction(this));
    }

}
