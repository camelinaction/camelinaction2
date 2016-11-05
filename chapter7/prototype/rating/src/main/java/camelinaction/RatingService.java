package camelinaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component("ratingService")
public class RatingService {

    /**
     * Generate ratings for the items
     * <p/>
     * Notice how the items is mapped to @Header with the name ids, which
     * refers to the context-path {ids} in the rest-dsl service
     */
    public List<RatingDto> ratings(@Header("ids") String items) {
        System.out.println("Rating items " + items);

        List<RatingDto> answer = new ArrayList<>();

        for (String id : items.split(",")) {
            RatingDto dto = new RatingDto();
            answer.add(dto);

            dto.setItemNo(Integer.valueOf(id));
            // generate some random ratings
            dto.setRating(new Random().nextInt(100));
        }

        return answer;
    }
}
