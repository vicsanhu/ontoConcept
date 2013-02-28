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
package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.Activator;


/**
 * Image Factory - copied from ld author and modified
 * 
 * @author Phillip Beauvoir
 * 
 */
public class ImageFactory {
    
    public static final String ICONPATH = "icons/"; //$NON-NLS-1$
    
    public static String ICON_ITEM = ICONPATH + "item.gif"; //$NON-NLS-1$
    //public static String ICON_INFO = ICONPATH + "info-icon-small.gif"; //$NON-NLS-1$
    public static String ICON_INFO = ICONPATH + "info-icon-med.gif"; //$NON-NLS-1$
    public static String ICON_DELETE = ICONPATH + "icon-delete.gif"; //$NON-NLS-1$
    
    public static String ICON_NEW_FOLDER = ICONPATH + "newfolder.gif"; //$NON-NLS-1$
    public static String ICON_NEW_TEST_FOLDER = ICONPATH + "folder_66.png"; //$NON-NLS-1$
    public static String ICON_NEW_TEST = ICONPATH + "newfile.gif"; //$NON-NLS-1$
    
    public static final String IMAGE_PATH = "images/"; //$NON-NLS-1$
    public static String EXAMPLE_MULTIPLE_CHOICE = IMAGE_PATH + "MultipleChoice_item.jpg"; //$NON-NLS-1$
    public static String EXAMPLE_MULTIPLE_RESPONSE = IMAGE_PATH + "MultipleResponse_item.jpg"; //$NON-NLS-1$
    public static String EXAMPLE_YES_NO = IMAGE_PATH + "Yes_No_item.jpg"; //$NON-NLS-1$
    public static String EXAMPLE_LIKERT_SCALE = IMAGE_PATH + "Likert_item.JPG"; //$NON-NLS-1$
    public static String EXAMPLE_OPEN_QUESTION = IMAGE_PATH + "Open_question_item.jpg"; //$NON-NLS-1$
    public static String EXAMPLE_FILL_IN_THE_BLANK = IMAGE_PATH + "Fill_in_the_blank_item.jpg"; //$NON-NLS-1$
    public static String EXAMPLE_INLINE_CHOICE = IMAGE_PATH + "inline-choice.png"; //$NON-NLS-1$
    public static String EXAMPLE_ASSOCIATE = IMAGE_PATH + "Associate_item.JPG"; //$NON-NLS-1$
    public static String EXAMPLE_MATCH = IMAGE_PATH + "Match_item.JPG"; //$NON-NLS-1$
    public static String EXAMPLE_ORDER = IMAGE_PATH + "Order_item.JPG"; //$NON-NLS-1$
    
    /**
     * Returns the shared image represented by the given key.
     * 
     * @param imageName
     *          the logical name of the image to retrieve
     * @return the shared image represented by the given key
     */
    public static Image getImage(String imageName) {

        ImageRegistry registry = Activator.getDefault().getImageRegistry();

        Image image = registry.get(imageName);
        
        // Make it and cache it
        																																																																																																																												if(image == null) {
            ImageDescriptor descriptor = getImageDescriptor(imageName);
            if(descriptor != null) {
                image = descriptor.createImage();
                if(image != null) {
                    registry.put(imageName, image);
                }
                else {
                    Logger.logError("ImageFactory: Error creating image for " + imageName); //$NON-NLS-1$
                }
            }
        }        
        return image;
    }
    
    /**
     * Returns the shared image description represented by the given key.
     * 
     * @param imageName
     *          the logical name of the image description to retrieve
     * @return the shared image description represented by the given name
     */
    public static ImageDescriptor getImageDescriptor(String imageName) {
       
        return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imageName);
    }

    /**
     * @param name
     * @return A shared Image from the system
     */
    public static Image getSharedImage(String name) {
        return PlatformUI.getWorkbench().getSharedImages().getImage(name);
    }
    
    /**
     * @param name
     * @return A shared ImageDescriptor from the system
     */
    public static ImageDescriptor getSharedImageDescriptor(String name) {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(name);
    }
}
