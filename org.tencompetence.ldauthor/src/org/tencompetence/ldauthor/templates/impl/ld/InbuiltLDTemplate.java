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
package org.tencompetence.ldauthor.templates.impl.ld;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.jdom.Element;
import org.tencompetence.ldauthor.templates.ILDTemplateXMLTags;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Inbuilt LD Template
 * 
 * @author Phillip Beauvoir
 * @version $Id: InbuiltLDTemplate.java,v 1.2 2010/01/06 15:34:25 phillipus Exp $
 */
public class InbuiltLDTemplate extends AbstractCopiedLDTemplate
implements ILDTemplateXMLTags {

    public InbuiltLDTemplate() {
        super();
    }
    
    public Image getImage() {
        return ImageFactory.getImage(ImageFactory.IMAGE_APP_48);
    }
    
    @Override
    public File getSourceFolder() {
        return new File(LDTemplateManager.getInbuiltTemplatesFolder(), getLocation());
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        Element description_element = element.getChild(XMLTAG_DESCRIPTION);
        if(description_element != null) {
            setDescription(description_element.getText());
        }
        
        Element learning_objectives_element = element.getChild(XMLTAG_LEARNING_OBJECTIVES);
        if(learning_objectives_element != null) {
            setLearningObjectives(learning_objectives_element.getText());
        }
    }

    public String getTagName() {
        return XMLTAG_TEMPLATE;
    }

    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        
        Element description_element = new Element(XMLTAG_DESCRIPTION);
        description_element.setText(getDescription());
        element.addContent(description_element);
        
        Element learning_objectives_element = new Element(XMLTAG_LEARNING_OBJECTIVES);
        learning_objectives_element.setText(getLearningObjectives());
        element.addContent(learning_objectives_element);
        
        return element;
    }
}
