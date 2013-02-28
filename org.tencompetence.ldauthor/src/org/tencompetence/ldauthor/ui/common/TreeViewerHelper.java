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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;



/**
 * A Helper class with useful functionality for a TreeViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: TreeViewerHelper.java,v 1.2 2008/04/24 10:15:29 phillipus Exp $
 */
public abstract class TreeViewerHelper {
    
    protected TreeViewer fTreeViewer;
    
    protected TreeViewerHelper(TreeViewer treeViewer) {
        fTreeViewer = treeViewer;
    }
    
    /**
     * Find the TreeItem that is placed before the given tree item
     * 
     * @param item
     * @return The previous tree item or null
     */
    public TreeItem findItemBefore(TreeItem item) {
        if(item == null) {
            return null;
        }
        
        TreeItem parentTreeItem = item.getParentItem();
        if(parentTreeItem == null) {  // At top level
            Tree tree = item.getParent();
            int index = tree.indexOf(item);
            if(index <= 0) {
                return null;
            }
            return tree.getItem(index - 1);
        }
        
        // Item
        int index = parentTreeItem.indexOf(item);
        if(index <= 0) {
            return null;
        }
        
        return parentTreeItem.getItem(index - 1);
    }
    
    /**
     * Figure out which TreeItem to select after some tree items have been deleted
     * @return The Tree Item to select or null if there are none left, or none selected
     */
    public TreeItem getItemToSelectAfterDeletion() {
        /*
         * The algorithm is this:
         * 1. Find the first/topmost selected treenode
         * 2. If this node has a previous sibling, return that
         * 3. Else return this node's parent node
         * 
         * This is OK on Windows, where the selection is in the correct order,
         * but not on a Mac where the firstSelected is actually the _last_ node.
         */
        
        TreeItem[] items = fTreeViewer.getTree().getSelection();
        
        // Nothing selected
        if(items.length == 0) {
            return null;
        }
        
        TreeItem item = null;
        
        if(Platform.getOS().equals(Constants.OS_MACOSX)) {
            item = items[items.length - 1];
        }
        else {
            item = items[0];
        }
        
        TreeItem prev = findItemBefore(item);
        
        if(prev != null) {
            item = prev;
        }
        else {
            item = item.getParentItem();
        }
        
        return item;
    }
    
    /**
     * Delete any selected objects on a TreeViewer from the Tree and underlying DataModel
     * Note - this method does not actually do the deletion.  deleteObjects() does.
     * 
     * @returns False if the operation was unsuccessful or the user cancelled
     */
    public boolean deleteSelectedObjects() {
        IStructuredSelection selection = (IStructuredSelection)fTreeViewer.getSelection();
        if(selection == null || selection.isEmpty()) {
            return false;
        }

        /*
         * Store previous TreeItem to be selected.
         * We select the previous TreeItem rather than the previous data object
         * because the data objects are stored in an arbitrary order.
         */
        TreeItem prevItem = getItemToSelectAfterDeletion();
        
        // Ask the user
        if(!askUserDeleteObjects(selection.size() > 1)) {
            return false;
        }
        
        // OK, so delete them
        deleteObjects(selection.toArray());
        
        /*
         * Reselect previous TreeItem ensuring that there is at least one TreeItem
         */ 
        if(prevItem == null && fTreeViewer.getTree().getItemCount() > 0) {  
            prevItem = fTreeViewer.getTree().getItem(0);
        }
        if(prevItem != null && !prevItem.isDisposed()) {
            fTreeViewer.setSelection(new StructuredSelection(prevItem.getData()));
        }
        
        return true;
    }
    
    /**
     * Implementers must over-ride this to actually do the business of deleting
     * the given objects
     * @param objects The objects to delete
     */
    protected abstract void deleteObjects(Object[] objects);

    /**
     * Ask the user whether they wish to delete the given objects
     * 
     * @param objects The objects to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    boolean askUserDeleteObjects(boolean plural) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.TreeViewerHelper_0,
                plural ?
                        Messages.TreeViewerHelper_1 
                        : 
                        Messages.TreeViewerHelper_2);
    }
}
