package camelinaction;

public class MyAggregationStrategyPojo {

    /**
     * Concats the messages.
     *
     * @param oldBody  the existing aggregated message body. Is <tt>null</tt> the
     *                     very first time as there are no existing message.
     * @param newBody  the incoming message body. This is never <tt>null</tt>.
     * @return the aggregated message.
     */
    public String concat(String oldBody, String newBody) {
        if (newBody != null) {
            return oldBody + newBody;
        } else {
            return oldBody;
        }
    }
    
}
