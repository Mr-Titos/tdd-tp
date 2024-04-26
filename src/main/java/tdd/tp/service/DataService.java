package tdd.tp.service;

import tdd.tp.exception.ObjectNotFoundException;

public interface DataService<T> {
    T findByID(String id) throws ObjectNotFoundException;
}
