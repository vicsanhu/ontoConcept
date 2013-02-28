/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldvisualiser.ui.view;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.tencompetence.imsldmodel.ILDModel;


/**
 * Stores states of LDs
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDState.java,v 1.7 2009/06/21 11:28:07 phillipus Exp $
 */
public class LDState {
    
    private Stack<Object> fBackStack = new Stack<Object>();
    private Stack<Object> fForwardStack = new Stack<Object>();
    
    private Object fCurrentObject;
    
    private ILDModel fLDModel;
    
    private LDGraphViewer fGraphViewer;
    
    private HashMap<Object, ObjectPositions> fPositions = new HashMap<Object, ObjectPositions>();
    
    public LDState(ILDModel ldModel, LDGraphViewer graphViewer) {
        fLDModel = ldModel;
        fCurrentObject = ldModel;
        fGraphViewer = graphViewer;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public Object getCurrentObject() {
        return fCurrentObject;
    }
    
    public void setCurrentObject(Object object) {
        fCurrentObject = object;
    }
    
    public Stack<Object> getBackStack() {
        return fBackStack;
    }
    
    public Stack<Object> getForwardStack() {
        return fForwardStack;
    }
    
    public void storeCurrentState() {
        ObjectPositions pos = fPositions.get(fCurrentObject);
        if(pos == null) {
            pos = new ObjectPositions();
            fPositions.put(fCurrentObject, pos);
        }
        pos.storeNodePositions(fGraphViewer.getNodesArray());
    }
    
    public void restoreLastState() {
        ObjectPositions pos = fPositions.get(fCurrentObject);
        if(pos != null) {
            // Restore positions
            pos.restoreNodePositions(fGraphViewer.getNodesArray());
        }
        else {
            // Clear pinned node
            ((LDGraphLabelProvider)fGraphViewer.getLabelProvider()).setPinnedNode(null, null);
            
            // De-highlight the selection's dependents
            ((LDGraphLabelProvider)fGraphViewer.getLabelProvider()).setCurrentSelection(fCurrentObject, null);
            
            // Use default layout
            fGraphViewer.doApplyLayout();
        }
    }
    
    public void dispose() {
        fBackStack.clear();
        fForwardStack.clear();
        fPositions.clear();
    }

    
    
    /*
     * Stores an Object's child positions and the currently selected child object
     */
    private class ObjectPositions {
        Object selectedChild;
        Object pinnedChild;
        
        private HashMap<Object, Point> childPositions = new HashMap<Object, Point>();
        
        void storeNodePositions(GraphNode[] nodes) {
            for(GraphNode node : nodes) {
                Object child = node.getData();
                childPositions.put(child, node.getLocation());
            }
            
            // Store selected child
            selectedChild = ((IStructuredSelection)fGraphViewer.getSelection()).getFirstElement();
            
            // And pinned child
            pinnedChild = ((LDGraphLabelProvider)fGraphViewer.getLabelProvider()).getPinnedNode();
        }
        
        void restoreNodePositions(GraphNode[] nodes) {
            for(GraphNode node : nodes) {
                Object child = node.getData();
                Point pt = childPositions.get(child);
                if(pt != null) {
                    node.setLocation(pt.x, pt.y);
                }
                else {
                    childPositions.put(child, node.getLocation());
                }
            }
            
            // Restore pinned child FIRST!
            ((LDGraphLabelProvider)fGraphViewer.getLabelProvider()).setPinnedNode(fCurrentObject, pinnedChild);

            // Then restore selected child
            if(selectedChild != null) {
                fGraphViewer.setSelection(new StructuredSelection(selectedChild));
            }
            
            // Highlight the selection's dependents
            ((LDGraphLabelProvider)fGraphViewer.getLabelProvider()).setCurrentSelection(fCurrentObject, selectedChild);
            
        }
    }
}
