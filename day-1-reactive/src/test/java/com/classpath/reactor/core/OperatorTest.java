package com.classpath.reactor.core;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class OperatorTest {

    @Test
    public void testMergeOperator() throws InterruptedException {
        Flux<Integer> evenFlux = Flux.just(2,4,6,8).delayElements(Duration.ofSeconds(2));
        Flux<Integer> oddFlux = Flux.just(1,3,5,7).delayElements(Duration.ofSeconds(1));

        evenFlux.mergeWith(oddFlux).subscribe(value -> System.out.println("Value:: "+value));

        Thread.sleep(50000);
    }

    @Test
    public void testZipOperator() throws InterruptedException {
        Flux<Integer> numberFlux = Flux.just(1, 2, 3, 4,5,6);
        Flux<String> letterFlux = Flux.just("a", "b", "c", "d", "e");
        letterFlux.zipWith(numberFlux , (val1, val2) -> val1 + val2).timeout(Duration.ofSeconds(6)).subscribe(data -> System.out.println(data), (e) -> System.out.println(e.getMessage()), ()-> System.out.println("Completed"));
        Thread.sleep(50000);
    }
}
