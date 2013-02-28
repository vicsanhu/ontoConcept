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
package org.tencompetence.ldauthor.opendock;


/**
 * IOpenDockConstants
 * 
 * @author Phillip Beauvoir
 * @version $Id: IOpenDockConstants.java,v 1.6 2008/04/25 11:46:13 phillipus Exp $
 */
public interface IOpenDockConstants {
    
    String[] LICENCE_ITEMS = {"none", "by-sa-na", "by-sa", "by-nd-nc", "by-nd", "by-nc", "by-nc-sa", "by"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    
    
    // Query Contstants
    
    String QUERY_CC_LICENSE = "cc_license"; //$NON-NLS-1$
    String QUERY_CC_LANGUAGE = "cc_language"; //$NON-NLS-1$
    String QUERY_COMPRESSED_SIZE = "compressed_size"; //$NON-NLS-1$
    String QUERY_ELEMENT_LASTUPDATE = "element_lastupdate"; //$NON-NLS-1$
    String QUERY_ELEMENT_OWNER_NAME = "element_owner_name"; //$NON-NLS-1$
    String QUERY_ELEMENT_RELEASEDATE = "element_releasedate"; //$NON-NLS-1$
    String QUERY_META_TITLE = "meta_title"; //$NON-NLS-1$
    String QUERY_META_SHORTDESC = "meta_shortdesc"; //$NON-NLS-1$
    String QUERY_META_DESCRIPTION = "meta_description"; //$NON-NLS-1$
    
    String QUERY_LD_NR_ACTIVITIES = "ld_nr_activities"; //$NON-NLS-1$
    String QUERY_LD_STRUCTURE_TYPE = "ld_structure_type"; //$NON-NLS-1$
    String QUERY_LD_LEVEL = "ld_model_level"; //$NON-NLS-1$
    String QUERY_LD_AGE = "ld_age"; //$NON-NLS-1$

    String QUERY_SUBJECT = "subject"; //$NON-NLS-1$
    String QUERY_TOPIC = "topic"; //$NON-NLS-1$
    String QUERY_PEDAGOGY = "pedagogy"; //$NON-NLS-1$

    String QUERY_CONTAINER = "container"; //$NON-NLS-1$
    String QUERY_CONTAINERS = "containers"; //$NON-NLS-1$
    String QUERY_CONTAINER_TYPE = "container_type"; //$NON-NLS-1$
    String QUERY_CONTAINER_ITEM_COUNT = "container_item_count"; //$NON-NLS-1$
    String QUERY_CONTAINER_ID = "container_id"; //$NON-NLS-1$
    String QUERY_NETWORK_ID = "network_id"; //$NON-NLS-1$
    String QUERY_REPOSITORY_ID = "repository_id"; //$NON-NLS-1$
    String QUERY_ITEM_ID = "item_id"; //$NON-NLS-1$

    String UOL = "uol"; //$NON-NLS-1$

    String IDENTITY_TIME = "time"; //$NON-NLS-1$
    String IDENTITY_APPLICATION = "application"; //$NON-NLS-1$
    String IDENTITY_USER = "user"; //$NON-NLS-1$
    
    // XML_RPC
    String METHOD_GET_LIST_NETWORKS = "getListNetworks"; //$NON-NLS-1$
    String METHOD_GET_LIST_REPOSITORIES = "getListRepositories"; //$NON-NLS-1$
    String METHOD_GET_LIST_CONTAINERS = "getListContainers"; //$NON-NLS-1$
    String METHOD_SEARCH_REPOSITORY = "searchRepository"; //$NON-NLS-1$
    String METHOD_GET_PLUGIN_DETAILS = "getPluginDetails"; //$NON-NLS-1$
    String METHOD_DOWNLOAD_ELEMENT = "downloadElement"; //$NON-NLS-1$
    String METHOD_UPLOAD_ELEMENT = "uploadElement"; //$NON-NLS-1$
    
    String PARAM_CONTENT = "content"; //$NON-NLS-1$
    String PARAM_AUTHOR = "author"; //$NON-NLS-1$
    String PARAM_LICENSE = "license"; //$NON-NLS-1$
}
