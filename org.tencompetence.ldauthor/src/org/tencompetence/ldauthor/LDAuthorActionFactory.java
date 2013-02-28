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
package org.tencompetence.ldauthor;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.ui.ImageFactory;



/**
 * Action Factory - thes are for *local* Actions that can be instantiated.
 * Surely there must be a better way to do this in Eclipse than
 * repeating Actions from org.eclipse.ui.actions.ActionFactory?
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorActionFactory.java,v 1.7 2008/10/16 06:24:11 phillipus Exp $
 */
public final class LDAuthorActionFactory {
    /**
     * DELETE
     */
    public static class DeleteAction extends Action {
        public DeleteAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_0);
            setActionDefinitionId("org.eclipse.ui.edit.delete"); // Ensures key binding is displayed //$NON-NLS-1$
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        }
    }

    /**
     * RENAME
     */
    public static class RenameAction extends Action {
        public RenameAction() {
            setText(Messages.LDAuthorActionFactory_1);
            setActionDefinitionId("org.eclipse.ui.edit.rename"); // Ensures key binding is displayed //$NON-NLS-1$
        }
    }

    /**
     * CUT
     */
    public static class CutAction extends Action {
        public CutAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_2);
            setActionDefinitionId("org.eclipse.ui.edit.cut"); // Ensures key binding is displayed //$NON-NLS-1$
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
        }
    }

    /**
     * COPY
     */
    public static class CopyAction extends Action {
        public CopyAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_3);
            setActionDefinitionId("org.eclipse.ui.edit.copy"); // Ensures key binding is displayed //$NON-NLS-1$
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        }
    }
    
    /**
     * PASTE
     */
    public static class PasteAction extends Action {
        public PasteAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_4);
            setActionDefinitionId("org.eclipse.ui.edit.paste"); // Ensures key binding is displayed //$NON-NLS-1$
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        }
    }

    /**
     * SELECT ALL
     */
    public static class SelectAllAction extends Action {
        public SelectAllAction() {
            setText(Messages.LDAuthorActionFactory_5);
            setActionDefinitionId("org.eclipse.ui.edit.selectAll"); // Ensures key binding is displayed //$NON-NLS-1$
        }
    }

    /**
     * BACK
     */
    public static class BackAction extends Action {
        public BackAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_6);
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));
            setToolTipText(Messages.LDAuthorActionFactory_6);
            setActionDefinitionId("org.eclipse.ui.navigate.back"); // Ensures key binding is displayed //$NON-NLS-1$
        }
    }
    
    /**
     * FORWARD
     */
    public static class ForwardAction extends Action {
        public ForwardAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setText(Messages.LDAuthorActionFactory_7);
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
            setDisabledImageDescriptor(
                    sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
            setToolTipText(Messages.LDAuthorActionFactory_7);
            setActionDefinitionId("org.eclipse.ui.navigate.forward"); // Ensures key binding is displayed //$NON-NLS-1$
        }
    }

    /**
     * STOP
     */
    public static class StopAction extends Action {
        public StopAction() {
            setText(Messages.LDAuthorActionFactory_8);
            setToolTipText(Messages.LDAuthorActionFactory_8);
            setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_STOP));
        }
    }

    /**
     * REFRESH
     */
    public static class RefreshAction extends Action {
        public RefreshAction() {
            setText(Messages.LDAuthorActionFactory_9);
            setToolTipText(Messages.LDAuthorActionFactory_9);
            setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_REFRESH));
            setActionDefinitionId("org.eclipse.ui.file.refresh"); //$NON-NLS-1$
        }
    }
    
    /**
     * MOVE UP
     */
    public static class MoveUpAction extends Action {
        public MoveUpAction() {
            setText(Messages.LDAuthorActionFactory_10);
            setToolTipText(Messages.LDAuthorActionFactory_10);
            setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_UP));
        }
    }

    /**
     * MOVE DOWN
     */
    public static class MoveDownAction extends Action {
        public MoveDownAction() {
            setText(Messages.LDAuthorActionFactory_11);
            setToolTipText(Messages.LDAuthorActionFactory_11);
            setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_DOWN));
        }
    }
    
    /**
     * NEW FILE
     */
    public static class NewFileAction extends Action {
        public NewFileAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
            setText(Messages.LDAuthorActionFactory_12);
            setToolTipText(Messages.LDAuthorActionFactory_13);
        }
    }

    /**
     * NEW FOLDER
     */
    public static class NewFolderAction extends Action {
        public NewFolderAction() {
            ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
            setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
            setText(Messages.LDAuthorActionFactory_14);
            setToolTipText(Messages.LDAuthorActionFactory_15);
        }
    }

    /**
     * EDIT
     */
    public static class EditAction extends Action {
        public EditAction() {
            setText(Messages.LDAuthorActionFactory_16);
            setToolTipText(Messages.LDAuthorActionFactory_16);
            setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_EDIT));
        }
    }

    /**
     * OPEN
     */
    public static class OpenAction extends Action {
        public OpenAction() {
            setText(Messages.LDAuthorActionFactory_17);
            setToolTipText(Messages.LDAuthorActionFactory_17);
            setActionDefinitionId("org.tencompetence.ui.open"); // Ensures key binding is displayed //$NON-NLS-1$
        }
    }
}
