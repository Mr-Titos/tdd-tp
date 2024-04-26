package tdd.tp.service.subscriber;

import lombok.AllArgsConstructor;
import tdd.tp.entity.Subscriber;
import tdd.tp.exception.ObjectNotFoundException;

@AllArgsConstructor
public class SubscriberService {
    private SubscriberDataDBService subscriberDataDBService;

    public Subscriber getSubscriberById(String id) throws ObjectNotFoundException {
        return null;
    }

    public Subscriber updateSubscriber(Subscriber s) {
        throw new RuntimeException("Not Implemented yet");
    }

    public Subscriber createSubscriber(Subscriber s) {
        throw new RuntimeException("Not Implemented yet");
    }

    public void deleteSubscriber(String id) {
        throw new RuntimeException("Not Implemented yet");
    }
}
