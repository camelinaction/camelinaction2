package org.apache.camel.component.mail.mock;

import java.util.HashMap;
import java.util.Map;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import org.jvnet.mock_javamail.Aliases;
import org.jvnet.mock_javamail.Mailbox;
import org.jvnet.mock_javamail.MockStore;

public class MyMockStore extends MockStore {

    private Map<String, Folder> folders = new HashMap<String, Folder>();
    private String address;

    public MyMockStore(Session session, URLName urlname) {
        super(session, urlname);
    }

    @Override
    protected boolean protocolConnect(String host, int port, String user, String password) throws MessagingException {
        address = user + '@' + host;
        return super.protocolConnect(host, port, user, password);
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        if ("INBOX".equals(name)) {
            return super.getFolder(name);
        }

        Folder folder = folders.get(name);
        if (folder == null) {
            // need a new mailbox as its a sub folder
            String adr = name + "-" + address;
            Mailbox mailbox = Mailbox.get(Aliases.getInstance().resolve(adr));
            folder = new MyMockFolder(this, mailbox, name);
            folders.put(name, folder);
        }
        return folder;
    }
}
