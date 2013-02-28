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

import java.util.Comparator;

import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutEntity;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.ldvisualiser.ui.view.ObjectAnalysis.RolePartMapping;


/**
 * Experimental Comparator
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDComparator.java,v 1.1 2009/06/25 15:02:55 phillipus Exp $
 */
public class LDComparator implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        LayoutEntity l1 = (LayoutEntity)o1;
        LayoutEntity l2 = (LayoutEntity)o2;
        
        GraphNode n1 = (GraphNode)l1.getGraphData();
        GraphNode n2 = (GraphNode)l2.getGraphData();
        
        Object modelObject1 = n1.getData();
        Object modelObject2 = n2.getData();
        
        System.out.println(modelObject1.getClass() + "  --->  " + modelObject2.getClass()); //$NON-NLS-1$
        
        if(modelObject1 instanceof RolePartMapping && modelObject2 instanceof IActModel) {
            return 1;
        }
        
        if(modelObject2 instanceof IActivityType) {
            return 1;
        }
        
        if(modelObject2 instanceof IEnvironmentModel) {
            return -2;
        }
        
        return 0;
    }

}
