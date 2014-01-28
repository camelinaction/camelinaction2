/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import java.net.URL;

import org.apache.camel.util.ObjectHelper;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.PropertiesUserManager;

/**
 * @version $Revision: 24 $
 */
public class FtpServerBean {

    protected static FtpServer ftpServer;
    private static int port = 21000;

    public static void startServer() throws Exception {
        System.out.println("Starting FTP Server...");
        initFtpServer();
        ftpServer.start();
        System.out.println("Starting FTP Server done.");
    }

    public static void shutdownServer() throws Exception {
        System.out.println("Stopping FTP Server...");
        try {
            ftpServer.stop();
            ftpServer = null;
        } catch (Exception e) {
            // ignore while shutting down as we could be polling during shutdown
            // and get errors when the ftp server is stopping. This is only an issue
            // since we host the ftp server embedded in the same jvm for unit testing
        }
        System.out.println("Stopping FTP Server done.");
    }

    public static void initFtpServer() throws Exception {
        FtpServerFactory serverFactory = new FtpServerFactory();

        // setup user management to read our users.properties and use clear text passwords
        URL url = ObjectHelper.loadResourceAsURL("users.properties");
        UserManager uman = new PropertiesUserManager(new ClearTextPasswordEncryptor(), url, "admin");

        serverFactory.setUserManager(uman);

        NativeFileSystemFactory fsf = new NativeFileSystemFactory();
        fsf.setCreateHome(true);
        serverFactory.setFileSystem(fsf);

        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);
        serverFactory.addListener("default", factory.createListener());

        ftpServer = serverFactory.createServer();
    }

}
