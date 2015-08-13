package camelinaction;

import java.util.Date;

/**
 * To publish failures
 */
public interface RiderFailurePublisher {

    public void publish(String appId, String eventId, Date timestamp, String message);
}
