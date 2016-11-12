package camelinaction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shopping cart bean.
 * <p/>
 * This implementation is just an in-memory dummy service
 */
@ApplicationScoped
@Named("cart")
public class CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartService.class);

    private final Map<String, Set<CartDto>> content = new LinkedHashMap<>();

    public void addItem(@Header("sessionId") String sessionId, @Body CartDto dto) {
        LOG.info("addItem {} {}", sessionId, dto);

        Set<CartDto> dtos = content.get(sessionId);
        if (dtos == null) {
            dtos = new LinkedHashSet<>();
            content.put(sessionId, dtos);
        }
        dtos.add(dto);
    }

    public void removeItem(@Header("sessionId") String sessionId, @Header("itemId") String itemId) {
        LOG.info("removeItem {} {}", sessionId, itemId);

        Set<CartDto> dtos = content.get(sessionId);
        if (dtos != null) {
            dtos.remove(itemId);
        }
    }

    public Set<CartDto> getItems(@Header("sessionId") String sessionId) {
        LOG.info("getItems {}", sessionId);
        Set<CartDto> answer = content.get(sessionId);
        if (answer == null) {
            answer = Collections.EMPTY_SET;
        }
        return answer;
    }
}
