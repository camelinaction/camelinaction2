package camelinaction;

import org.apache.camel.Header;

public class CombineDataBean {

    public String combine(@Header("ERP") String erp, @Header("CRM") String crm, @Header("SHIPPING") String shipping) {
        StringBuilder sb = new StringBuilder("Customer overview");
        sb.append("\nERP: " + erp);
        sb.append("\nCRM: " + crm);
        sb.append("\nSHIPPING: " + shipping);
        return sb.toString();
    }
}
