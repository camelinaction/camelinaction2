package camelinaction.goal;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import camelinaction.util.IOHelper;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.ResourceEndpoint;

public class GoalEndpoint extends ResourceEndpoint {

    public GoalEndpoint(String endpointUri, Component component, String resourceUri) {
        super(endpointUri, component, resourceUri);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Producer createProducer() throws Exception {
        // load games from resource
        InputStream is = getResourceAsInputStream();
        String text = IOHelper.loadText(is);

        // store games in a list
        Stream<String> stream = Arrays.stream(text.split("\n"));
        List<String> games = stream.collect(Collectors.toList());

        // create producer with the games
        return new GoalProducer(this, games);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        // load goals from resource
        InputStream is = getResourceAsInputStream();
        String text = IOHelper.loadText(is);

        // split each line
        Stream<String> stream = Arrays.stream(text.split("\n"));

        // sort goals scored on minutes
        stream = stream.sorted((a, b) -> goalTime(a).compareTo(goalTime(b)));

        // store goals in a list
        List<String> goals = stream.collect(Collectors.toList());

        // create consumer with the goals
        return new GoalConsumer(this, processor, goals);
    }

    private static Integer goalTime(String line) {
        return Integer.valueOf(line.split(",")[1]);
    }

}
