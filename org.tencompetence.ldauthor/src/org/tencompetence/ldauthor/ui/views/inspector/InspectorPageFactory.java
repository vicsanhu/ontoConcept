/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.graphicsmodel.other.IGraphicalNoteModel;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.ui.views.inspector.activity.activitystructure.ActivityStructurePage;
import org.tencompetence.ldauthor.ui.views.inspector.activity.learningactivity.LearningActivityPage;
import org.tencompetence.ldauthor.ui.views.inspector.activity.supportactivity.SupportActivityPage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.EnvironmentPage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.conference.ConferencePage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.indexsearch.IndexSearchPage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.learningobject.LearningObjectPage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.monitor.MonitorPage;
import org.tencompetence.ldauthor.ui.views.inspector.environment.sendmail.SendMailPage;
import org.tencompetence.ldauthor.ui.views.inspector.graphics.GEFConnectionPage;
import org.tencompetence.ldauthor.ui.views.inspector.graphics.GEFNotePage;
import org.tencompetence.ldauthor.ui.views.inspector.item.ItemModelTypePage;
import org.tencompetence.ldauthor.ui.views.inspector.method.CompleteUOLPage;
import org.tencompetence.ldauthor.ui.views.inspector.method.act.ActPage;
import org.tencompetence.ldauthor.ui.views.inspector.method.play.PlayPage;
import org.tencompetence.ldauthor.ui.views.inspector.organiser.OrganiserFolderPage;
import org.tencompetence.ldauthor.ui.views.inspector.organiser.OrganiserLDPage;
import org.tencompetence.ldauthor.ui.views.inspector.organiser.OrganiserResourcePage;
import org.tencompetence.ldauthor.ui.views.inspector.role.RolePage;



/**
 * InspectorPageFactory
 * 
 * @author Phillip Beauvoir
 * @version $Id: InspectorPageFactory.java,v 1.13 2009/06/10 10:16:48 phillipus Exp $
 */
public class InspectorPageFactory {
    
    private HashMap<Class<?>, AbstractInspectorPage> fListPages = new HashMap<Class<?>, AbstractInspectorPage>();
    

    public AbstractInspectorPage getPage(Composite parent, Object element) {
        AbstractInspectorPage page = null;
        
        // Complete Uol
        if(element instanceof ICompleteUOLType) {
            page = fListPages.get(ICompleteUOLType.class);
            if(page == null) {
                page = new CompleteUOLPage(parent);
                fListPages.put(ICompleteUOLType.class, page);
            }
        }

        // Item Model Type
        if(element instanceof IItemModelType) {
            page = fListPages.get(IItemModelType.class);
            if(page == null) {
                page = new ItemModelTypePage(parent);
                fListPages.put(IItemModelType.class, page);
            }
        }
        
        // Play
        if(element instanceof IPlayModel) {
            page = fListPages.get(IPlayModel.class);
            if(page == null) {
                page = new PlayPage(parent);
                fListPages.put(IPlayModel.class, page);
            }
        }
        
        // Act
        if(element instanceof IActModel) {
            page = fListPages.get(IActModel.class);
            if(page == null) {
                page = new ActPage(parent);
                fListPages.put(IActModel.class, page);
            }
        }

        // Role
        if(element instanceof IRoleModel) {
            page = fListPages.get(IRoleModel.class);
            if(page == null) {
                page = new RolePage(parent);
                fListPages.put(IRoleModel.class, page);
            }
        }
        
        // Learning Activity
        if(element instanceof ILearningActivityModel) {
            page = fListPages.get(ILearningActivityModel.class);
            if(page == null) {
                page = new LearningActivityPage(parent);
                fListPages.put(ILearningActivityModel.class, page);
            }
        }
        
        // Support Activity
        if(element instanceof ISupportActivityModel) {
            page = fListPages.get(ISupportActivityModel.class);
            if(page == null) {
                page = new SupportActivityPage(parent);
                fListPages.put(ISupportActivityModel.class, page);
            }
        }

        // Activity Structure
        if(element instanceof IActivityStructureModel) {
            page = fListPages.get(IActivityStructureModel.class);
            if(page == null) {
                page = new ActivityStructurePage(parent);
                fListPages.put(IActivityStructureModel.class, page);
            }
        }

        // Environment
        if(element instanceof IEnvironmentModel) {
            page = fListPages.get(IEnvironmentModel.class);
            if(page == null) {
                page = new EnvironmentPage(parent);
                fListPages.put(IEnvironmentModel.class, page);
            }
        }

        // Learning Object
        if(element instanceof ILearningObjectModel) {
            page = fListPages.get(ILearningObjectModel.class);
            if(page == null) {
                page = new LearningObjectPage(parent);
                fListPages.put(ILearningObjectModel.class, page);
            }
        }

        // Send Mail
        if(element instanceof ISendMailModel) {
            page = fListPages.get(ISendMailModel.class);
            if(page == null) {
                page = new SendMailPage(parent);
                fListPages.put(ISendMailModel.class, page);
            }
        }

        // Conference
        if(element instanceof IConferenceModel) {
            page = fListPages.get(IConferenceModel.class);
            if(page == null) {
                page = new ConferencePage(parent);
                fListPages.put(IConferenceModel.class, page);
            }
        }

        // Index Search
        if(element instanceof IIndexSearchModel) {
            page = fListPages.get(IIndexSearchModel.class);
            if(page == null) {
                page = new IndexSearchPage(parent);
                fListPages.put(IIndexSearchModel.class, page);
            }
        }
        
        // Monitor
        if(element instanceof IMonitorModel) {
            page = fListPages.get(IMonitorModel.class);
            if(page == null) {
                page = new MonitorPage(parent);
                fListPages.put(IMonitorModel.class, page);
            }
        }

        // Organiser Folder
        if(element instanceof IOrganiserFolder) {
            page = fListPages.get(IOrganiserFolder.class);
            if(page == null) {
                page = new OrganiserFolderPage(parent);
                fListPages.put(IOrganiserFolder.class, page);
            }
        }
        
        // Organiser Resource
        if(element instanceof IOrganiserResource) {
            page = fListPages.get(IOrganiserResource.class);
            if(page == null) {
                page = new OrganiserResourcePage(parent);
                fListPages.put(IOrganiserResource.class, page);
            }
        }
        
        // Organiser LD
        if(element instanceof IOrganiserLD) {
            page = fListPages.get(IOrganiserLD.class);
            if(page == null) {
                page = new OrganiserLDPage(parent);
                fListPages.put(IOrganiserLD.class, page);
            }
        }
        
        // GEF Note
        if(element instanceof IGraphicalNoteModel) {
            page = fListPages.get(IGraphicalNoteModel.class);
            if(page == null) {
                page = new GEFNotePage(parent);
                fListPages.put(IGraphicalNoteModel.class, page);
            }
        }
        
        // GEF Connection
        if(element instanceof IGraphicalModelConnection) {
            page = fListPages.get(IGraphicalModelConnection.class);
            if(page == null) {
                page = new GEFConnectionPage(parent);
                fListPages.put(IGraphicalModelConnection.class, page);
            }
        }

        // Blank Page
        if(page == null) {
            page = new AbstractInspectorPage(parent) {
            };
        }
        
        return page;
    }

    public void dispose() {
        fListPages.clear();
        fListPages = null;
    }
    
}
