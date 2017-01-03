package camelinaction;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {

    private static final Logger LOG = LoggerFactory.getLogger(RatingService.class);

    // use rest template to call external REST service
    private final RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "fallbackRating")
    public RatingDto[] rating(String ratingsUrl, String itemIds) {
        LOG.info("Calling rating service {}", ratingsUrl);
        return restTemplate.getForObject(ratingsUrl, RatingDto[].class, itemIds);
    }

    public RatingDto[] fallbackRating(String ratingsUrl, String itemIds) {
        String[] ids = itemIds.split(",");

        RatingDto[] dtos = new RatingDto[ids.length];
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            RatingDto dto = new RatingDto();
            dto.setItemNo(Integer.valueOf(id));
            dto.setRating(6);
            dtos[i] = dto;
        }

        return dtos;
    }

}
