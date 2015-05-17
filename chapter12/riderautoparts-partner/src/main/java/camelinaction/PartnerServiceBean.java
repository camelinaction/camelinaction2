package camelinaction;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.language.XPath;

public class PartnerServiceBean {

    public Map toMap(@XPath("partner/@id") int partnerId,
                        @XPath("partner/date/text()") String date,
                        @XPath("partner/code/text()") int statusCode,
                        @XPath("partner/time/text()") long responseTime) {

        Map map = new HashMap();
        map.put("id", partnerId);
        map.put("date", date);
        map.put("code", statusCode);
        map.put("time", responseTime);
        return map;
    }
}
