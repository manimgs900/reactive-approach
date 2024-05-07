package com.classpath.day2webfluxoperators.service;

import com.classpath.day2webfluxoperators.model.Order;
import com.classpath.day2webfluxoperators.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /*public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }*/

    public Mono<Order> save(Order order){
        return this.orderRepository.save(order);
    }

    public Flux<Order> fetchAllOrders(){
        return this.orderRepository.findAll();
    }

    public Mono<Order> fetchOrderById( long orderId){
        return this.orderRepository
                        .findById(orderId)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException(" invalid order id")));
    }

    public Mono<Order> deleteOrderById(long orderId){
        Mono<Order> monoOrder = this.orderRepository.findById(orderId);
        Mono<Order> deletedOrder = monoOrder.flatMap(order -> this.orderRepository.delete(order).thenReturn(order));
        return deletedOrder;
    }

    public Mono<Order> updateOrder(long orderId, Order updatedOrder){
        return this.orderRepository
                .findById(orderId)
                .map(dbRecord -> updatedOrder)
                .flatMap(order -> this.orderRepository.save(order));
    }

}
