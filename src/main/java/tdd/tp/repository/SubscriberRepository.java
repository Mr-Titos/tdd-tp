package tdd.tp.repository;

import tdd.tp.entity.Subscriber;


public interface SubscriberRepository /*extends JpaRepository<Subscriber, String>*/ {
    Subscriber findByCode(String code);

}