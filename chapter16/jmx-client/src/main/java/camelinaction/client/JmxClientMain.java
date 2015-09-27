package camelinaction.client;

public class JmxClientMain {

    private JmxCamelClient client;
    private String serviceUrl = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi/camel";

    public static void main(String[] args) throws Exception {
        JmxClientMain main = new JmxClientMain();
        main.run();
    }

    public void run() throws Exception {
        client = new JmxCamelClient();
        client.connect(serviceUrl);

        System.out.println("Client 1 : Connected to remote JMX: " + serviceUrl);

        System.out.println("Client 1 : Camel Version: " + client.getCamelVersion());
        System.out.println("Client 1 : Camel Uptime: " + client.getCamelUptime());

        client.disconnect();
        System.out.println("Client 1 : Disconnected");
    }

}
