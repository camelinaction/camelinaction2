package camelinaction;

import org.apache.camel.Header;

public class OrderStatusService {
    public String checkStatus(@Header("id") String id) throws Exception {
        if ("123".equals(id)) {
            return "Processing";
        }
        return "Complete";
    }
}
