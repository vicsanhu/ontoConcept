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
package org.tencompetence.ldauthor.ui.editors.environment;


import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.requests.CreationFactory;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.EnvironmentEditorGraphicalModelFactory;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.common.AbstractLDEditorPalette;


/**
 * Palette for Environment Parts 
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentEditorPalette.java,v 1.35 2009/05/22 16:35:05 phillipus Exp $
 */
public class EnvironmentEditorPalette
extends AbstractLDEditorPalette {
    
    public EnvironmentEditorPalette(ILDModel ldModel) {
        super(ldModel);
        setup();
    }
    
    protected void setup() {
        PaletteGroup mainGroup = new PaletteGroup(LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT));
        add(mainGroup);
        
        PaletteEntry entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT), //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_5 + LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT),
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.ENVIRONMENT, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_ENVIRONMENT_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_ENVIRONMENT_24));
        mainGroup.add(entry);
        
        entry = new StickyConnectionCreationToolEntry(
                Messages.EnvironmentEditorPalette_0,
                Messages.EnvironmentEditorPalette_1,
                new CreationFactory() {
                    public Object getNewObject() {
                        return null;
                    }

                    // see ShapeEditPart#createEditPolicies()
                    // this is abused to transmit the desired line style
                    public Object getObjectType() {
                        return new Integer(Graphics.LINE_SOLID);
                    }
                },
                ImageFactory.getImageDescriptor(ImageFactory.ICON_ARROW_SOLID),
                ImageFactory.getImageDescriptor(ImageFactory.ICON_ARROW_SOLID)); 
        mainGroup.add(entry); 


        PaletteDrawer drawerComponents = new PaletteDrawer(Messages.EnvironmentEditorPalette_4);
        add(drawerComponents);

        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + Messages.EnvironmentEditorPalette_12, //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_6,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.LEARNING_OBJECT_KNOWLEDGE_OBJECT, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_24));
        drawerComponents.add(entry);
        
        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + Messages.EnvironmentEditorPalette_15, //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_16,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.LEARNING_OBJECT_TOOL_OBJECT, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_TOOL_OBJECT_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_TOOL_OBJECT_24));
        drawerComponents.add(entry);

        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + Messages.EnvironmentEditorPalette_13, //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_14,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.LEARNING_OBJECT_TEST_OBJECT, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_24));
        drawerComponents.add(entry);
        
        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + LDModelUtils.getUserObjectName(LDModelFactory.SEND_MAIL), //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_7,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.SEND_MAIL, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_SENDMAIL_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_SENDMAIL_24));
        drawerComponents.add(entry);

        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + LDModelUtils.getUserObjectName(LDModelFactory.CONFERENCE), //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_8,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.CONFERENCE, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_CONFERENCE_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_CONFERENCE_24));
        drawerComponents.add(entry);

        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + LDModelUtils.getUserObjectName(LDModelFactory.INDEX_SEARCH),  //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_9,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.INDEX_SEARCH, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_INDEXSEARCH_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_INDEXSEARCH_24));
        drawerComponents.add(entry);

        entry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + LDModelUtils.getUserObjectName(LDModelFactory.MONITOR),  //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_10,
                new EnvironmentEditorGraphicalModelFactory(LDModelFactory.MONITOR, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_MONITOR_16),
                ImageFactory.getImageDescriptor(ImageFactory.IMAGE_MONITOR_24));
        drawerComponents.add(entry);

        PaletteEntry noteEntry = new CombinedTemplateCreationEntry(Messages.EnvironmentEditorPalette_11 + " " + Messages.EnvironmentEditorPalette_2,  //$NON-NLS-1$
                Messages.EnvironmentEditorPalette_3,
                new EnvironmentEditorGraphicalModelFactory(EnvironmentEditorGraphicalModelFactory.NOTE, getLDModel()),
                ImageFactory.getImageDescriptor(ImageFactory.ICON_NOTE_16),
                ImageFactory.getImageDescriptor(ImageFactory.ICON_NOTE_24));
        drawerComponents.add(noteEntry);

        
        createControlDrawer();
    }
}
