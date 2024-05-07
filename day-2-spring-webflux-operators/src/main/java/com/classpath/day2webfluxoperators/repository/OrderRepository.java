package com.classpath.day2webfluxoperators.repository;

import com.classpath.day2webfluxoperators.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> findByNameLike(String name);
}
