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

package com.huawei.http;

import com.huawei.esdk.byod.anyoffice.DemoConfiguration;
import com.huawei.svn.sdk.thirdpart.httpclient.SvnHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class HttpClientHelp
{
    static
    {
        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
    }

    private static DefaultHttpClient httpClient = null;

    private HttpClientHelp()
    {

    }

    public synchronized static HttpClient getInstance()
    {
        if (null == httpClient)
        {
            if (!DemoConfiguration.useAppVPN) {
                SvnHttpClient svnHttpClient = new SvnHttpClient();
                svnHttpClient.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                httpClient = svnHttpClient;


                httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 2000); // 超时设置
                httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时
            }else{
                DefaultHttpClient client = new DefaultHttpClient();

                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 2000); // 超时设置
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时

                httpClient = client;
            }
        }

        // if (cookie != null)
        // {
        // ((AbstractHttpClient)httpClient)
        // .setCookieStore(cookie);
        // Log.i("eSDK", "Cookie = " + cookie.toString());
        // }

        return httpClient;
    }
    // public static CookieStore getCookie()
    // {
    // return cookie;
    // }
    //
    // public static void setCookie(CookieStore cookie)
    // {
    // HttpClientHelp.cookie = cookie;
    // }
}
