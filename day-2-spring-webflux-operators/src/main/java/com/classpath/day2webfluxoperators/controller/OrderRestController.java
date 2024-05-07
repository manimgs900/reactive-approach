package com.classpath.day2webfluxoperators.controller;

import com.classpath.day2webfluxoperators.model.Order;
import com.classpath.day2webfluxoperators.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Flux<Order> fetchOrders(){
        log.info("Fetching all the orders::");
        return this.orderService.fetchAllOrders().delayElements(Duration.ofMillis(20));
    }

    @GetMapping("/{id}")
    public Mono<Order> fetchById(@PathVariable("id") long orderId){
        return this.orderService.fetchOrderById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> saveOrder(@RequestBody @Valid Order order){
        return this.orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public Mono<Order> deleteOrderById(@PathVariable long id){
        return this.orderService.deleteOrderById(id);
    }
}
