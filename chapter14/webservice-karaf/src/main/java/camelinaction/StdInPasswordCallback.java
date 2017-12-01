package camelinaction;

import java.io.Console;
import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class StdInPasswordCallback implements CallbackHandler {
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        Console console = System.console();
        console.printf("Please enter your Rider Auto Parts password: ");
        char[] passwordChars = console.readPassword();
        String passwordString = new String(passwordChars);
        pc.setPassword(passwordString);        
    }
}
