package camelinaction.client2;

public class JmxClient2Main {

    private JmxCamelClient2 client;
    private String serviceUrl = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi/camel";

    public static void main(String[] args) throws Exception {
        JmxClient2Main main = new JmxClient2Main();
        main.run();
    }

    public void run() throws Exception {
        client = new JmxCamelClient2();
        client.connect(serviceUrl);

        System.out.println("Client 2 : Connected to remote JMX: " + serviceUrl);

        System.out.println("Client 2 : Camel Version: " + client.getCamelVersion());
        System.out.println("Client 2 : Camel Uptime: " + client.getCamelUptime());

        client.disconnect();
        System.out.println("Client 2 : Disconnected");
    }

}
