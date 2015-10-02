package org.apache.camel.component.mail.mock;

import org.jvnet.mock_javamail.Mailbox;
import org.jvnet.mock_javamail.MockFolder;
import org.jvnet.mock_javamail.MockStore;

public class MyMockFolder extends MockFolder {

    private String name;

    public MyMockFolder(MockStore store, Mailbox mailbox, String name) {
        super(store, mailbox);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return name;
    }

}
