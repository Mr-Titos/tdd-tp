package tdd.exo1.service;

import tdd.exo1.exception.ObjectNotFoundException;

public interface DataService<T> {

    T fetch(String id) throws ObjectNotFoundException;
}
