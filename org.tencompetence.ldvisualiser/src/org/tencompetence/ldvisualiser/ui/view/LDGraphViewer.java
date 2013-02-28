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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;


/**
 * LDGraphViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphViewer.java,v 1.4 2009/06/22 11:16:49 phillipus Exp $
 */
public class LDGraphViewer extends GraphViewer {
    
    private boolean doLayout;
    
    public LDGraphViewer(Composite composite, int style) {
        // Stupidly, super creates a Graph control that we can't get rid of or dispose
        // As there are some side-effects
        super(composite, style);
        
        /*
         * Over-ride this to block laying out
         */
        Graph mygraph = new Graph(composite, SWT.NONE) {
            @Override
            public void applyLayout() {
                if(doLayout) {
                    super.applyLayout();
                }
            }
        };
        
        setControl(mygraph);
    }
    
      
    public void doApplyLayout() {
        doLayout = true;
        super.applyLayout();
        doLayout = false;
    }

    @SuppressWarnings("unchecked")
    public GraphNode[] getNodesArray() {
        GraphNode[] nodesArray = new GraphNode[graph.getNodes().size()];
        nodesArray = (GraphNode[]) graph.getNodes().toArray(nodesArray);
        return nodesArray;
    }
}
