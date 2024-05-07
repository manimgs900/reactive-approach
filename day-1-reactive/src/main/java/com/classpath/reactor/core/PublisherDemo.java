package com.classpath.reactor.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PublisherDemo {
    public static void main(String[] args) throws InterruptedException {
        //Publishers
        Mono<Integer> element = Mono.just(22);
        element.subscribe(System.out::println);


        Flux<Integer> values = Flux.just(12,33,12,45,34, 67,33,132);
        Thread.sleep(2000);
        values.subscribe(System.out::println);
        Thread.sleep(2000);
        values.subscribe(System.out::println);

        List<Integer> nums = Arrays.asList(12,33,12,45,34, 67,33,132);
        Stream<Integer> stream = nums.stream().filter(n -> {
            System.out.println(n);
            return n%2 == 0;
        });

        /*
            Predicate
            Supplier
            Consumer
            Function
            BiFunction
            BiConsumer
         */
    }
}
