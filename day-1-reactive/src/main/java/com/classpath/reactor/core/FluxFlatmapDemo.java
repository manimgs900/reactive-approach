package com.classpath.reactor.core;

import com.classpath.reactor.core.model.LineItem;
import com.classpath.reactor.core.model.Order;
import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class FluxFlatmapDemo {

    private static final Faker faker = new Faker();
    public static void main(String[] args) {

        /*
           Flux<Integer[]>  => flatmap => Flux<Integer>
           Flux<List<T>>  => flatmap => Flux<T>
           Flux<Set<T>>  => flatmap => Flux<T>
           Flux<Flux<T>>  => flatmap => Flux<T>
           Flux<Stream<T>>  => flatmap => Flux<T>
           Flux<T>  => flatmap => Flux<T>
         */

        Flux<Order> orderFlux = generateTestData();
        //orderFlux.subscribe(order -> System.out.println(order));
        Flux<Set<LineItem>> setFlux = orderFlux.map(order -> order.getLineItems());

        Flux<Set<LineItem>> lineItemsFlux = orderFlux.map(order -> order.getLineItems());

        Flux<LineItem> lineItemFlux = lineItemsFlux.flatMap(Flux::fromIterable);
        lineItemFlux.subscribe(lineItem -> System.out.println(lineItem.getName()));


    }

    private static Flux<Order> generateTestData() {
        List<Order> orders = new ArrayList<>();

        IntStream.range(0, 100).forEach(index -> {
            Order order = Order.builder().email(faker.internet().emailAddress()).id(index).build();

            IntStream.range(0, faker.number().numberBetween(3, 4)).forEach(value -> {
                LineItem item = LineItem
                                    .builder()
                                    .id(value)
                                    .name(faker.commerce().productName())
                                    .price(faker.number().randomDouble(2, 25000, 30000))
                                    .qty(faker.number().numberBetween(2,5))
                                    .build();
                order.addLineItem(item);
            });
            orders.add(order);
        });
        return Flux.fromIterable(orders);
    }
}
