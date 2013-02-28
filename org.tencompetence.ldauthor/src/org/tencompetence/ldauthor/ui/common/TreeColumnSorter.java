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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * The abstract TreeColumnSorter extends the ViewerSorter. It allows the
 * sorting of objects within all columns of a TreeView. These objects should
 * implement the Comparable interface and all objects should be of the same
 * class.
 * 
 * @author Hubert Vogten
 * @author Phillip Beauvoir
 * @version $Id: TreeColumnSorter.java,v 1.3 2010/02/01 10:45:09 phillipus Exp $
 */
public abstract class TreeColumnSorter extends ViewerSorter {

	@Override
	public boolean isSorterProperty(Object element, String property) {
		return true;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1.getClass() == e2.getClass()) {
			TreeColumn sortColumn = ((TreeViewer) viewer).getTree().getSortColumn();
			if (sortColumn == null)
				return 0;
			int index = ((Integer) sortColumn.getData("index")).intValue(); //$NON-NLS-1$

			Object o1 = getValue(e1, index);
			Object o2 = getValue(e2, index);
			return _compare(o1, o2) * (((TreeViewer)viewer).getTree().getSortDirection() == SWT.UP ? 1 : -1);
		}
		return 0;
	}

	protected Object getValue(Object o, int index) {
		return o;
	}

	@SuppressWarnings("unchecked")
    protected int _compare(Object o1, Object o2) {
		if (o1 instanceof Comparable) {
			return ((Comparable) o1).compareTo(o2); // unchecked cast
		}
		return 0;
	}
}