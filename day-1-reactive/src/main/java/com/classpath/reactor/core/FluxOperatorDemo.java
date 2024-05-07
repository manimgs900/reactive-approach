package com.classpath.reactor.core;

import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public class FluxOperatorDemo {

    public static void main(String[] args) {
        Stream<Integer> integerStream = Stream.of( 22,33, 22,44,55, 44, 88, 66,77,88,99, 33);
        Flux<Integer> fluxFromStream = Flux.fromStream(integerStream);
        fluxFromStream
                .distinct()
                .filter(number -> number % 2 == 0)
                .map(even -> {
                    System.out.println("Inside the map method:: ");
                    return " Even number :: "+ even;
                }).subscribe(System.out::println);
    }
}
