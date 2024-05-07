package com.classpath.reactor.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FluxGeneratorDemo {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1,2,3,2,1,4,5,6);
        Flux<Integer> interableFlux = Flux.fromIterable(numbers);

        Integer[] array = {11,22,33,44,55,66};
        Flux<Integer> arrayFlux = Flux.fromArray(array);

        Stream<Integer> integerStream = Stream.of( 22,33,44,55,66,77,88,99);
        Flux<Integer> fluxFromStream = Flux.fromStream(integerStream);

        Flux<Integer> fluxFromRange = Flux.range(3, 8);

        Flux.just(99,88,77,66,55,44);

        Flux<Object> empty = Flux.empty();

        Flux.just(99, 88, 77, 66, 55, 44);

        Flux<Sinks.Empty> emptyFlux = Flux.empty();

        fluxFromRange.subscribe(System.out::println);
    }
}
