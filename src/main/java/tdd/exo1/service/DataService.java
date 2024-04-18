package tdd.exo1.service;

import tdd.exo1.entity.Book;
import tdd.exo1.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

public interface DataService<T> {

    public T fetch(String id) throws ObjectNotFoundException;
}
