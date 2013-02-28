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
package org.tencompetence.ldauthor.organisermodel;

import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Description
 * 
 * @author Phillip Beauvoir
 * @version $Id: IOrganiserIndex.java,v 1.8 2008/06/02 22:15:30 phillipus Exp $
 */
public interface IOrganiserIndex
extends IOrganiserContainer
{

    String PROPERTY_ORGANISER_CHANGED = "IOrganiserIndex.changed"; //$NON-NLS-1$
    
    /**
     * Save the Index
     */
    void save();
    
    /**
     * Set the dirty flag so that the index will be saved on dispose
     * @param dirty
     */
    void setDirty(boolean dirty);
    
    /**
     * @return Whether the Index is dirty (and needs saving)
     */
    boolean isDirty();

    /**
     * Add a Property Change Listener
     * @param l
     */
    void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Remove a Property Change Listener
     * @param l
     */
    void removePropertyChangeListener(PropertyChangeListener l);

    /**
     * The location of the user "Organiser" folder
     */
    File getOrganiserFolder();
}