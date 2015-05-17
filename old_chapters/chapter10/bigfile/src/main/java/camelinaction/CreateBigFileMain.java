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

import java.io.File;
import java.io.FileOutputStream;

/**
 * A main class to create a sample bigfile.csv to be used for testing.
 *
 * @version $Revision$
 */
public class CreateBigFileMain {

    public static void main(String[] args) throws Exception {
        new File("target/inventory").mkdirs();
        File file = new File("target/inventory/bigfile.csv");
        if (file.exists()) {
            file.delete();
        }

        int lines = 100;
        if (args.length == 1) {
            lines = Integer.parseInt(args[0]);
        }

        System.out.println("Creating target/inventory/bigfile.csv with " + lines + " lines");
        createBigFile(file, lines);
        System.out.println("Creating target/inventory/bigfile.csv done");
    }

    private static void createBigFile(File file, int lines) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        for (int i = 0; i < lines; i++) {
            String line = "123," + i + ",bumper,4\n";
            fos.write(line.getBytes());
        }
        fos.close();
    }

}
