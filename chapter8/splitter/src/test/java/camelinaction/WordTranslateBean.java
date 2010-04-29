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

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision$
 */
public class WordTranslateBean {

    private Map<String, String> words = new HashMap<String, String>();

    public WordTranslateBean() {
        words.put("A", "Camel rocks");
        words.put("B", "Hi mom");
        words.put("C", "Yes it works");
    }

    public String translate(String key) {
        return words.get(key);
    }

}
