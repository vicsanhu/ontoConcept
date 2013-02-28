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
package org.tencompetence.ldauthor.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Image Factory
 * 
 * @author Phillip Beauvoir
 * @version $Id: ImageFactory.java,v 1.66 2009/07/03 11:13:16 phillipus Exp $
 */
public class ImageFactory {
    
    public static final String ICONPATH = "icons/"; //$NON-NLS-1$
    
    public static String IMAGEPATH = "images/"; //$NON-NLS-1$
    
    public static String IMAGE_APP_16 = IMAGEPATH + "Library-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_APP_32 = IMAGEPATH + "Library-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_APP_48 = IMAGEPATH + "Library-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_ACT_16 = IMAGEPATH + "Library-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_ACT_24 = IMAGEPATH + "Library-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_ACT_32 = IMAGEPATH + "Library-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_ACT_48 = IMAGEPATH + "Library-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_SEQUENCE_16 = IMAGEPATH + "Gear-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_SEQUENCE_24 = IMAGEPATH + "Gear-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_SEQUENCE_32 = IMAGEPATH + "Gear-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_SEQUENCE_48 = IMAGEPATH + "Gear-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_ACTIVITY_STRUCTURE_16 = IMAGEPATH + "AS_16x16.png"; //$NON-NLS-1$
    public static String IMAGE_ACTIVITY_STRUCTURE_24 = IMAGEPATH + "AS_24x24.png"; //$NON-NLS-1$
    public static String IMAGE_ACTIVITY_STRUCTURE_32 = IMAGEPATH + "AS_32x32.png"; //$NON-NLS-1$
    public static String IMAGE_ACTIVITY_STRUCTURE_48 = IMAGEPATH + "AS_48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_ARROW = IMAGEPATH + "arrow.png"; //$NON-NLS-1$
    public static String IMAGE_ARROW_RIGHT_SMALL = IMAGEPATH + "arrow_right_small.png"; //$NON-NLS-1$
    public static String IMAGE_ARROW_RIGHT_LARGE = IMAGEPATH + "arrow_right_large.png"; //$NON-NLS-1$
    public static String IMAGE_ARROW_DOWN_SMALL = IMAGEPATH + "arrow_down_small.png"; //$NON-NLS-1$
    public static String IMAGE_ARROW_DOWN_LARGE = IMAGEPATH + "arrow_down_large.png"; //$NON-NLS-1$
    public static String IMAGE_ARROW_LEFT_LARGE = IMAGEPATH + "arrow_left_large.png"; //$NON-NLS-1$
    public static String IMAGE_CLIENT_48 = IMAGEPATH + "Client-3-48x48.png"; //$NON-NLS-1$
    public static String IMAGE_CLOCK_16 = IMAGEPATH + "Clock-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_CLOCK_32 = IMAGEPATH + "Clock-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_CLOCK_48 = IMAGEPATH + "Clock-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_CONFERENCE_16 = IMAGEPATH + "user-group-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_CONFERENCE_24 = IMAGEPATH + "user-group-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_CONFERENCE_32 = IMAGEPATH + "user-group-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_CONFERENCE_48 = IMAGEPATH + "user-group-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_DOCUMENT_48 = IMAGEPATH + "Document-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_ENVIRONMENT_16 = IMAGEPATH + "Briefcase-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_ENVIRONMENT_24 = IMAGEPATH + "Briefcase-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_ENVIRONMENT_32 = IMAGEPATH + "Briefcase-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_ENVIRONMENT_48 = IMAGEPATH + "Briefcase-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_FACE = IMAGEPATH + "face.gif"; //$NON-NLS-1$
    public static String IMAGE_FOLDER = IMAGEPATH + "folder_66.png"; //$NON-NLS-1$
    
    public static String IMAGE_INDEXSEARCH_16 = IMAGEPATH + "index_search_16x16.gif"; //$NON-NLS-1$
    public static String IMAGE_INDEXSEARCH_24 = IMAGEPATH + "index_search_24x24.gif"; //$NON-NLS-1$
    
    public static String IMAGE_LEARNER_16 = IMAGEPATH + "User-3-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNER_24 = IMAGEPATH + "User-3-24x24.png"; //$NON-NLS-1$
    
    public static String IMAGE_LEARNING_ACTIVITY_16 = IMAGEPATH + "Document-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_ACTIVITY_24 = IMAGEPATH + "Document-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_ACTIVITY_32 = IMAGEPATH + "Document-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_ACTIVITY_48 = IMAGEPATH + "Document-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_LEARNING_OBJECT_16 = IMAGEPATH + "Dictionary-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_24 = IMAGEPATH + "Dictionary-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_32 = IMAGEPATH + "Dictionary-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_48 = IMAGEPATH + "Dictionary-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_LEARNING_OBJECT_TOOL_OBJECT_16 = IMAGEPATH + "development-tools-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TOOL_OBJECT_24 = IMAGEPATH + "development-tools-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TOOL_OBJECT_32 = IMAGEPATH + "development-tools-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TOOL_OBJECT_48 = IMAGEPATH + "development-tools-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_LEARNING_OBJECT_TEST_OBJECT_16 = IMAGEPATH + "System-Security-Question-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TEST_OBJECT_24 = IMAGEPATH + "System-Security-Question-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TEST_OBJECT_32 = IMAGEPATH + "System-Security-Question-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_LEARNING_OBJECT_TEST_OBJECT_48 = IMAGEPATH + "System-Security-Question-48x48.png"; //$NON-NLS-1$

    public static String IMAGE_MONITOR_16 = IMAGEPATH + "check-user-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_MONITOR_24 = IMAGEPATH + "check-user-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_MONITOR_32 = IMAGEPATH + "check-user-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_MONITOR_48 = IMAGEPATH + "check-user-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_MYSTERYMAN = IMAGEPATH + "mysteryman.gif"; //$NON-NLS-1$
    public static String IMAGE_NEW_LEARNER_WIZBAN = IMAGEPATH + "new_learner_wizban.png"; //$NON-NLS-1$
    public static String IMAGE_NEW_STAFF_WIZBAN = IMAGEPATH + "new_staff_wizban.png"; //$NON-NLS-1$
    
    public static String IMAGE_PACKAGE_16 = IMAGEPATH + "System-Package-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_PACKAGE_24 = IMAGEPATH + "System-Package-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_PACKAGE_32 = IMAGEPATH + "System-Package-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_PACKAGE_48 = IMAGEPATH + "System-Package-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_PLAY_16 = IMAGEPATH + "Movies-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_PLAY_32 = IMAGEPATH + "Movies-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_PLAY_48 = IMAGEPATH + "Movies-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_SENDMAIL_16 = IMAGEPATH + "Mail-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_SENDMAIL_24 = IMAGEPATH + "Mail-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_SENDMAIL_32 = IMAGEPATH + "Mail-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_SENDMAIL_48 = IMAGEPATH + "Mail-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_STAFF_16 = IMAGEPATH + "Client-3-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_STAFF_24 = IMAGEPATH + "Client-3-24x24.png"; //$NON-NLS-1$
    
    public static String IMAGE_SUPPORT_ACTIVITY_16 = IMAGEPATH + "file-share-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_SUPPORT_ACTIVITY_24 = IMAGEPATH + "file-share-24x24.png"; //$NON-NLS-1$
    public static String IMAGE_SUPPORT_ACTIVITY_32 = IMAGEPATH + "file-share-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_SUPPORT_ACTIVITY_48 = IMAGEPATH + "file-share-48x48.png"; //$NON-NLS-1$
    
    public static String IMAGE_TASK_16 = IMAGEPATH + "Task-List-16x16.png"; //$NON-NLS-1$
    public static String IMAGE_TASK_32 = IMAGEPATH + "Task-List-32x32.png"; //$NON-NLS-1$
    public static String IMAGE_TASK_48 = IMAGEPATH + "Task-List-48x48.png"; //$NON-NLS-1$
    
    public static String ICON_ACT = ICONPATH + "act.gif"; //$NON-NLS-1$
    public static String ICON_ARROW_SOLID = ICONPATH + "arrow_unbroken.gif"; //$NON-NLS-1$
    public static String ICON_BROWSER = ICONPATH + "navigate_16.png"; //$NON-NLS-1$
    public static String ICON_ERROR = ICONPATH + "error.gif"; //$NON-NLS-1$
    public static String ICON_CHILD_ITEM = ICONPATH + "child_item.gif"; //$NON-NLS-1$
    public static String ICON_CONDITION = ICONPATH + "condition.gif"; //$NON-NLS-1$
    public static String ICON_CONNECT = ICONPATH + "connect.gif"; //$NON-NLS-1$
    public static String ICON_DATE = ICONPATH + "date.gif"; //$NON-NLS-1$
    public static String ICON_DEFAULT_SIZE = ICONPATH + "default_size.gif"; //$NON-NLS-1$
    public static String ICON_DOWN = ICONPATH + "down.gif"; //$NON-NLS-1$
    public static String ICON_EDIT = ICONPATH + "edit.gif"; //$NON-NLS-1$
    public static String ICON_EDITOR = ICONPATH + "editor.gif"; //$NON-NLS-1$
    public static String ICON_GREENTICK = ICONPATH + "greentick.gif"; //$NON-NLS-1$
    public static String ICON_GO = ICONPATH + "go.gif"; //$NON-NLS-1$
    public static String ICON_INSPECTOR = ICONPATH + "inspector.png"; //$NON-NLS-1$
    public static String ICON_ITEM = ICONPATH + "item.gif"; //$NON-NLS-1$
    public static String ICON_ITEMS = ICONPATH + "items.gif"; //$NON-NLS-1$
    public static String ICON_LD = ICONPATH + "ld.gif"; //$NON-NLS-1$
    //public static String ICON_LEARNER_ROLE = ICONPATH + "learner.gif";
    public static String ICON_MINUS = ICONPATH + "minus.gif"; //$NON-NLS-1$
    public static String ICON_NEWFILE = ICONPATH + "newfile.gif"; //$NON-NLS-1$
    public static String ICON_NEWFOLDER = ICONPATH + "newfolder.gif"; //$NON-NLS-1$
    public static String ICON_NEWLD = ICONPATH + "newld.gif"; //$NON-NLS-1$
    public static String ICON_NEW_OVERLAY = ICONPATH + "new_overlay.gif"; //$NON-NLS-1$
    public static String ICON_NEWRESOURCE = ICONPATH + "newresource.gif"; //$NON-NLS-1$
    public static String ICON_NEWRUN = ICONPATH + "newrun.gif"; //$NON-NLS-1$
    public static String ICON_NEWUSER = ICONPATH + "newuser.gif"; //$NON-NLS-1$
    public static String ICON_NODE = ICONPATH + "node.gif"; //$NON-NLS-1$
    public static String ICON_NOTE_16 = ICONPATH + "note_16.gif"; //$NON-NLS-1$
    public static String ICON_NOTE_24 = ICONPATH + "note_24.gif"; //$NON-NLS-1$
    public static String ICON_OPEN = ICONPATH + "open.gif"; //$NON-NLS-1$
    public static String ICON_ORANGE_EXCLAMATION = ICONPATH + "orange_exclamation.gif"; //$NON-NLS-1$
    public static String ICON_ORGANISER = ICONPATH + "organiser.gif"; //$NON-NLS-1$
    public static String ICON_PLAY = ICONPATH + "play.gif"; //$NON-NLS-1$
    public static String ICON_PLUS = ICONPATH + "plus.gif"; //$NON-NLS-1$
    public static String ICON_PLUS_RED = ICONPATH + "plus-red.gif"; //$NON-NLS-1$
    public static String ICON_PROPERTY = ICONPATH + "property.gif"; //$NON-NLS-1$
    public static String ICON_PUBLISHER = ICONPATH + "publisher.gif"; //$NON-NLS-1$
    public static String ICON_REDCROSS = ICONPATH + "redcross.gif"; //$NON-NLS-1$
    public static String ICON_REFRESH = ICONPATH + "refresh.gif"; //$NON-NLS-1$
    public static String ICON_RESOURCE = ICONPATH + "resource.gif"; //$NON-NLS-1$
    public static String ICON_ROLEPART = ICONPATH + "role_part.gif"; //$NON-NLS-1$
    public static String ICON_ROUTERDIRECT = ICONPATH + "router_direct.gif"; //$NON-NLS-1$
    public static String ICON_RUN = ICONPATH + "run.gif"; //$NON-NLS-1$
    //public static String ICON_SENDMAIL = ICONPATH + "sendmail.gif";
    public static String ICON_SERVICE = ICONPATH + "service.gif"; //$NON-NLS-1$
    public static String ICON_SHORTCUT_OVERLAY = ICONPATH + "shortcut_overlay.gif"; //$NON-NLS-1$
    public static String ICON_SLASH = ICONPATH + "slash.gif"; //$NON-NLS-1$
    //public static String ICON_STAFF_ROLE = ICONPATH + "staff.gif";
    public static String ICON_STOP = ICONPATH + "stop.gif"; //$NON-NLS-1$
    public static String ICON_TICK = ICONPATH + "tick.png"; //$NON-NLS-1$
    public static String ICON_TICK_FAINT = ICONPATH + "tick_faint.png"; //$NON-NLS-1$
    public static String ICON_UP = ICONPATH + "up.gif"; //$NON-NLS-1$
    public static String ICON_UPLOAD_ZIP = ICONPATH + "uploadzip.gif"; //$NON-NLS-1$
    public static String ICON_USER = ICONPATH + "user.gif"; //$NON-NLS-1$
    public static String ICON_VIEW = ICONPATH + "view.gif"; //$NON-NLS-1$

    public static String ECLIPSE_IMAGE_PROPERTIES_ICON = "properties_view"; //$NON-NLS-1$
    public static String ECLIPSE_IMAGE_NEW_WIZARD = "new_wizard"; //$NON-NLS-1$
    
    public static String ECLIPSE_IMAGE_FILE = "file"; //$NON-NLS-1$
    public static String ECLIPSE_IMAGE_FOLDER = "folder"; //$NON-NLS-1$
    
    // icon IDmethod
    public static String ICON_IDMethod = ICONPATH + "flagIcon_chile.png"; //$NON-NLS-1$
    
    //Icono ontoConcept
    public static String ICON_ONTOCONCEPT = ICONPATH + "pato.png"; //$NON-NLS-1$
    
    //Icono ontoZest
    public static String ICON_ONTOZEST = ICONPATH + "go.gif"; //$NON-NLS-1$
    
    
    /**
     * @param object
     * @return an icon for an LD type
     */
    public static Image getIconLDType(ILDModelObject object) {
        if(object == null) {
            return null;
        }
        
        // Special cases
        if(object instanceof ILearningObjectModel) {
            switch(((ILearningObjectModel)object).getType()) {
                case ILearningObjectModel.TYPE_TEST_OBJECT:
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16);
                case ILearningObjectModel.TYPE_TOOL_OBJECT:
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TOOL_OBJECT_16);
                default:
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_16);
            }
        }
        
        return getIconLDType(object.getTagName());
    }
    
    /**
     * @param elementName
     * @return an icon for an LD type with its tag name
     */
    public static Image getIconLDType(String elementName) {
        if(elementName == null) {
            return null;
        }
        
        if(elementName.equals(LDModelFactory.LEARNER)) {
            return getImage(IMAGE_LEARNER_16);
        }
        if(elementName.equals(LDModelFactory.STAFF)) {
            return getImage(IMAGE_STAFF_16);
        }
        if(elementName.equals(LDModelFactory.LEARNING_ACTIVITY)) {
            return getImage(IMAGE_LEARNING_ACTIVITY_16);
        }
        if(elementName.equals(LDModelFactory.SUPPORT_ACTIVITY)) {
            return getImage(IMAGE_SUPPORT_ACTIVITY_16);
        }
        if(elementName.equals(LDModelFactory.ACTIVITY_STRUCTURE)) {
            return getImage(IMAGE_ACTIVITY_STRUCTURE_16);
        }
        if(elementName.equals(LDModelFactory.ENVIRONMENT)) {
            return getImage(IMAGE_ENVIRONMENT_16);
        }
        if(elementName.equals(LDModelFactory.LEARNING_OBJECT)) {
            return getImage(IMAGE_LEARNING_OBJECT_16);
        }
        if(elementName.equals(LDModelFactory.SEND_MAIL)) {
            return getImage(IMAGE_SENDMAIL_16);
        }
        if(elementName.equals(LDModelFactory.CONFERENCE)) {
            return getImage(IMAGE_CONFERENCE_16);
        }
        if(elementName.equals(LDModelFactory.INDEX_SEARCH)) {
            return getImage(IMAGE_INDEXSEARCH_16);
        }
        if(elementName.equals(LDModelFactory.MONITOR)) {
            return getImage(IMAGE_MONITOR_16);
        }
        if(elementName.equals(LDModelFactory.PLAY)) {
            return getImage(IMAGE_PLAY_16);
        }
        if(elementName.equals(LDModelFactory.ACT)) {
            return getImage(IMAGE_ACT_16);
        }
        if(elementName.equals(LDModelFactory.ROLE_PART)) {
            return getImage(ICON_ROLEPART);
        }
        if(elementName.equals(LDModelFactory.LOCAL_PERSONAL_PROPERTY)) {
            return getImage(ICON_PROPERTY);
        }
        if(elementName.equals(LDModelFactory.LOCAL_PROPERTY)) {
            return getImage(ICON_PROPERTY);
        }
        if(elementName.equals(LDModelFactory.LOCAL_ROLE_PROPERTY)) {
            return getImage(ICON_PROPERTY);
        }
        if(elementName.equals(LDModelFactory.GLOBAL_PERSONAL_PROPERTY)) {
            return getImage(ICON_PROPERTY);
        }
        if(elementName.equals(LDModelFactory.GLOBAL_PROPERTY)) {
            return getImage(ICON_PROPERTY);
        }
        if(elementName.equals(LDModelFactory.PROPERTY_GROUP)) {
            return getImage(ECLIPSE_IMAGE_FOLDER);
        }
        if(elementName.equals(LDModelFactory.ITEM)) {
            return getImage(ICON_ITEM);
        }
        if(elementName.equals(LDModelFactory.RESOURCES)) {
            return getImage(IMAGE_PACKAGE_16);
        }
        if(elementName.equals(LDModelFactory.ROLES)) {
            return getImage(IMAGE_LEARNER_16);
        }
        if(elementName.equals(LDModelFactory.ENVIRONMENTS)) {
            return getImage(IMAGE_ENVIRONMENT_16);
        }
        if(elementName.equals(LDModelFactory.ACTIVITIES)) {
            return getImage(IMAGE_ACTIVITY_STRUCTURE_16);
        }
        if(elementName.equals(LDModelFactory.METHOD)) {
            return getImage(IMAGE_SEQUENCE_16);
        }
        
        return null;
    }
    
    /**
     * Returns the shared image represented by the given key.
     * 
     * @param imageName
     *          the logical name of the image to retrieve
     * @return the shared image represented by the given key
     */
    public static Image getImage(String imageName) {
        if(imageName == null || ECLIPSE_IMAGE_FILE.equals(imageName)) {
            return ImageFactory.getSharedImage(ISharedImages.IMG_OBJ_FILE);
        }

        if(ECLIPSE_IMAGE_FOLDER.equals(imageName)) {
            return ImageFactory.getSharedImage(ISharedImages.IMG_OBJ_FOLDER);
        }

        ImageRegistry registry = LDAuthorPlugin.getDefault().getImageRegistry();

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
     * Return a composite overlay image
     * 
     * @param imageName
     * @param overlayName
     * @param quadrant the quadrant (one of {@link IDecoration} 
     * ({@link IDecoration#TOP_LEFT}, {@link IDecoration#TOP_RIGHT},
     * {@link IDecoration#BOTTOM_LEFT}, {@link IDecoration#BOTTOM_RIGHT} 
     * or {@link IDecoration#UNDERLAY})
     * @return
     */
    public static Image getOverlayImage(String imageName, String overlayName, int quadrant) {
        // Make a registry name, cached
        String key_name = imageName + overlayName + quadrant;
        
        Image image = getImage(key_name);
        
        // Make it and cache it
        if(image == null) {
            Image underlay = getImage(imageName);
            ImageDescriptor overlay = getImageDescriptor(overlayName);
            if(underlay != null && overlay != null) {
                image = new DecorationOverlayIcon(underlay, overlay, quadrant).createImage();
                if(image != null) {
                    ImageRegistry registry = LDAuthorPlugin.getDefault().getImageRegistry();
                    registry.put(key_name, image);
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
        if (imageName == null || ECLIPSE_IMAGE_FILE.equals(imageName)) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
        }

        if (ECLIPSE_IMAGE_FOLDER.equals(imageName)) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
        }

        return AbstractUIPlugin.imageDescriptorFromPlugin(LDAuthorPlugin.PLUGIN_ID, imageName);
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
    
    
    /**
     * @return An ImageDescriptor from Eclipse
     * Possible names - "properties_view", "new_wizard"
     */
    public static ImageDescriptor getEclipseImageDescriptor(String name) {
        if(ECLIPSE_IMAGE_PROPERTIES_ICON.equals(name)) {
            return LDAuthorPlugin.imageDescriptorFromPlugin("org.eclipse.ui.views", "$nl$/icons/full/eview16/prop_ps.gif"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        if(ECLIPSE_IMAGE_NEW_WIZARD.equals(name)) {
            return LDAuthorPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/wizban/new_wiz.png"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return null;
    }
    
    /**
     * Get a scaled image 
     * @param uri The URI to access the image from.  If local, prefix with "file:///"
     * @param maxHeight The maximum height to resize the image to.  If -1, no resizing takes place
     * @return The Image or null
     */
    public static Image getScaledImage(URL url, int maxHeight) {
        if(url == null) {
            return null;
        }
        
        Image image = null;
        
        // This needs caching!!!!!
        
        try {
            ImageDescriptor id = ImageDescriptor.createFromURL(url);
            ImageData imageData = id.getImageData();
            if(imageData == null) {
                return null;
            }
            
            // Make smaller (within reason)
            if(maxHeight > 4) {
                int height = imageData.height;
                int width = imageData.width;
                if(height > maxHeight) {
                    height = maxHeight;
                    width *= ((float)maxHeight / imageData.height);
                    if(width > 10){ // Ensure a sensible size
                        imageData = imageData.scaledTo(width, height);
                    }
                }
            }
            
            image = new Image(Display.getCurrent(), imageData);
    
        }
        catch(Exception ex) {
            //ex.printStackTrace();
        }
        
        return image;
    }
    
    /**
     * @param image
     * @param width
     * @param height
     * @return A scaled image to width and height
     */
    public static Image getScaledImage(Image image, int width, int height) {
        if(image == null) {
            return null;
        }

        // This needs caching!!!!!
        
        ImageData imageData = image.getImageData();
        imageData = imageData.scaledTo(width, height);

        return new Image(Display.getCurrent(), imageData);
    }
    
    /**
     * Get an icon image associated with a file's extension or the default file icon if not found.
     * @param file The file to find the icon for
     * @return The icon image associated with a file's extension or the default file icon if not found.
     */
    public static Image getProgramFileIcon(File file) {
        ImageRegistry image_registry = LDAuthorPlugin.getDefault().getImageRegistry();
        Image image = null;
        
        /*
         * Manifest
         */
        if("imsmanifest.xml".equals(file.getName())) { //$NON-NLS-1$
            return getImage(IMAGE_APP_16);
        }
        
        /*
         * From File extension
         */
        String ext = FileUtils.getFileExtension(file);
        
        // XML Types
        if(".xml".equalsIgnoreCase(ext)) { //$NON-NLS-1$
            // Find Editor in registry
            try {
                IEditorDescriptor editor_descriptor = EditorManager.getDefaultEditor(file);
                if(editor_descriptor != null) {
                    image = image_registry.get(editor_descriptor.getId());  // cached?
                    if(image == null) {
                        ImageDescriptor imgDesc = editor_descriptor.getImageDescriptor();
                        if(imgDesc != null) {
                            image = imgDesc.createImage();
                            if(image != null) {
                                image_registry.put(editor_descriptor.getId(), image);  // cache it
                            }
                        }
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        /*
         * Other extensions
         */
        if(image == null && ext != null) {  
            image = image_registry.get(ext);  // cached?
            if(image == null) {
                Program p = Program.findProgram(ext);
                if(p != null) {
                    ImageData imageData = p.getImageData();
                    if(imageData != null) {
                        // On Ubuntu the image can be too big for jpeg files!
                        if(imageData.height > 16) {
                            imageData = imageData.scaledTo(16, 16);
                        }
                        image = new Image(Display.getDefault(), imageData);
                        if(image != null) {
                            image_registry.put(ext, image);  // cache it
                        }
                    }
                }
            }
        }
        
        if(image == null) {
            image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        }
        
        return image;
    }
}
