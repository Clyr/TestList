/*
 * Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
 * eSDK is licensed under the Apache License, Version 2.0 ^(the "License"^);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package com.huawei.common;

import com.huawei.FileUtil;

/**
 * @author cWX223941
 */
public class Constants
{
    
    /* ############################################# Login Information ##################################################################### */
    public static final String AUTHENTICATE_USERNAME = "1";
    
    public static final String AUTHENTICATE_PASSWORD = "1";
    
    public static final String AUTHENTICATE_IP = "192.168.11.40";
    
    //    public static final String AUTHENTICATE_USERNAME = "showtest";
    //    public static final String AUTHENTICATE_PASSWORD = "Admin@123";
    //    public static final String AUTHENTICATE_IP = "58.60.106.145";
    
    /* ############################################# HTTP Test ##################################################### */
    
    public static final String HTTP_TEST_SERVER = "192.120.1.155";
    
    //public static final String HTTP_TEST_SERVER = "172.22.8.15";
    //    public static final String HTTP_TEST_SERVER = "172.19.110.5";
    public static final String HTTP_TEST_PORT = "8080";
    
    public static final String HTTP_TEST_URL_PREFIX = "/HttpServerDemo/e";
    
    public static final String HTTP_TEST_BASE_URL = "http://" + HTTP_TEST_SERVER + ":" + HTTP_TEST_PORT
        + HTTP_TEST_URL_PREFIX;
    
    public static final String HTTP_TEST_LOGIN_URL = "/Login.do";
    
    public static final String HTTP_TEST_USERINFO_URL = "/Userinfo.do";
    
    public static final String HTTP_TEST_DOWNLOAD_URL = "/Download.do";
    
    public static final String HTTP_TEST_UPLOAD_URL = "/execute_upload.do";
    
    public static final String HTTP_TEST_WEBVIEW_URL = "http://" + HTTP_TEST_SERVER + ":" + HTTP_TEST_PORT;
    
    public static final String HTTP_TEST_ORIG_WEBVIEW_URL = "http://www.baidu.com";
    
    /* #############################################Menu Item ####################################################################### */
    
    public static final int MENU_HTTP_HTTPCLIENT = 1;
    
    public static final int MENU_HTTP_URLCONNECTION = 2;
    
    public static final int MENU_HTTP_WEBVIEW = 3;
    
    public static final int MENU_SPEED_COMPARE = 4;
    
    public static final int MENU_SOCKET_TCP = 5;
    
    public static final int MENU_ENCRYPT_FILE = 6;
    
    public static final int MENU_ENCRYPT_DATA = 7;
    
    public static final int MENU_RANDOM_ACCESS_FILE = 8;
    
    public static final int MENU_ENCRYPT_SQLITE = 9;
    
    public static final int MENU_ENCRYPT_CLIPBOARD = 10;
    
    public static final int MENU_MDM_CHECK = 11;
    
    public static final int MENU_LOG_UPLOAD = 12;
    
    public static final int MENU_WIFI_UPLOAD = 13;
    
    public static final int MENU_CHECKBIND = 14;
    
    public static final int MENU_CHECKNETTYPE = 15;
    
    /* #############################################Encrypt /Decrypt ##################################################### */
    
    public static final String FOLDER_ROOT = "SvnSdkDemo";
    
    public static final String FOLDER_PATH_DOWNLOAD = FileUtil.getSDPath() + "/" + FOLDER_ROOT + "/Download";
    
    public static final String FILE_PATH_ORIGINAL = FileUtil.getSDPath() + "/" + FOLDER_ROOT + "/Original";
    
    public static final String FILE_PATH_ENCRYPT = FileUtil.getSDPath() + "/" + FOLDER_ROOT + "/Encrypt";
    
    public static final String FILE_BROWSE_RESULT = "FileBrowseResult";
    
    /* #############################################Login or Logout or Request Status ##################################################### */
    public static final int LOGIN_STATUS_SUCCESS = 0;
    
    public static final int LOGIN_STATUS_FALSE = 1;
    
    public static final int LOGOUT_STATUS_SUCCESS = 0;
    
    public static final int LOGOUT_STATUS_FALSE = 1;
    
    public static final int STATUS_REQUEST_SUCCESS = 0;
    
    public static final int STATUS_REQUEST_FALSE = 1;
    
    /* #############################################Guide category ######################################################################## */
    public static final String GUIDE_CATEGORY = "GuideCategory";
    
    public static final String GUIDE_CATEGORY_URLCONNECTION = "UrlConnection";
    
    public static final String GUIDE_CATEGORY_HTTPCLIENT = "HttpClient";
    
    public static final String GUIDE_CATEGORY_FILE_ENDECRYPT = "FileEnDecrypt";
    
    public static final String GUIDE_CATEGORY_DATA_ENDECRYPT = "DataEnDecrypt";
    
    /* #############################################Activity Send ######################################################################### */
    public static final String ACTIVITY_SEND_HTTPTYPE = "HttpType";
    
    public static final String ACTIVITY_SEND_HTTPTYPE_URLCONNECTION = "UrlConnection";
    
    public static final String ACTIVITY_SEND_HTTPTYPE_HTTPCLIENT = "HttpClient";
    
    public static final String ACTIVITY_SEND_ENDECRYPT_TYPE = "EnDecryptType";
    
    public static final String ACTIVITY_SEND_ENDECRYPT_TYPE_LOGIN = "Login";
    
    public static final String ACTIVITY_SEND_ENDECRYPT_TYPE_NO_LOGIN = "NoLogin";
    
    public static final String ACTIVITY_SEND_FAQ = "Faq";
    
    /* #############################################Click Interface Type################################################################### */
    public static final int CLICK_IFACE_TYPE_FILE_SYSTEM_FILE = 1001;
    
    /* #############################################Assets Folder or File ################################################################# */
    public static final String ASSETS_FAQ = "faq";
    
    public static final String ASSETS_GUIDE = "guide";
    
    public static final String ASSETS_GUIDE_HTTPCLIENT_FILE = "guide/guide_httpclient.txt";
    
    public static final String ASSETS_GUIDE_URLCONNECTION_FILE = "guide/guide_urlconnection.txt";
    
    public static final String ASSETS_GUIDE_FILE_ENDECRYPT_FILE = "guide/guide_file_endecrypt.txt";
    
    public static final String ASSETS_GUIDE_DATA_ENDECRYPT_FILE = "guide/guide_data_endecrypt.txt";
    
    /* ################################################################################################################## */
    public static final int LOGIN_RESULT_SUCCESS = 0;
    
    /* login action中产生的问题分类*/
    public static final String LOGIN_ERROR_FILE = "FileError";
    
    public static final String LOGIN_SUCCESS = "Success";
    
    public static final boolean LOGIN_AUTO = true;
    
    /* ###########################################FAQ Format ########################################################################## */
    public static final String FAQ_QUESTION = "===Q===";
    
    public static final String FAQ_ANSWER = "===A===";
    /* ################################################################################################################## */
}
