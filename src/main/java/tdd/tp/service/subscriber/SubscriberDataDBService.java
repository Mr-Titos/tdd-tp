package tdd.tp.service.subscriber;

import tdd.tp.entity.Subscriber;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.service.DataService;

public class SubscriberDataDBService implements DataService<Subscriber> {

    @Override
    public Subscriber findByID(String id) throws ObjectNotFoundException {
        throw new RuntimeException("Not Implemented");
    }
}
