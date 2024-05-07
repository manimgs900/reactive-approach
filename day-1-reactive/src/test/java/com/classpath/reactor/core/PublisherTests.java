package com.classpath.reactor.core;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class PublisherTests {

    @Test
    public void testMonoPublisher(){
        //execution
        Mono<String> emptyMono = Mono.empty();
        StepVerifier.create(emptyMono).expectComplete().verify();
    }

    @Test
    public void testSingleMonoPublisher(){
        Mono<String> message = Mono.just("reactive-programming");
        StepVerifier.create(message).expectNext("reactive-programming").expectComplete().verify();
    }

    @Test
    public void testFluxPublisher(){
        Flux<String> message = Flux.just("one", "two", "three");
        StepVerifier.create(message).expectNext("one", "two", "three").expectComplete().verify();
    }

    @Test
    public void testFluxWithMapOperator(){
        Flux<Integer> numbersFlux = Flux.fromIterable(Arrays.asList(11,22,33,44));
        StepVerifier.create(numbersFlux).expectNext(11,22,33,44).expectComplete().verify();
    }

    @Test
    public void testFluxWithWithException(){
        Flux<Integer> numbersFlux = Flux.fromIterable(Arrays.asList(11, 22, 33, 44))
                                .concatWith(Flux.error(new IllegalArgumentException()));
                                //.concatWith(Flux.just(22, 33,44));
        StepVerifier.create(numbersFlux).expectNext(11,22,33,44).expectError(IllegalArgumentException.class).verify();
    }

    @Test
    public void delayedPublisher() throws InterruptedException {
        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("hari", "vinod", "ramesh")).delayElements(Duration.ofSeconds(2));

        namesFlux.subscribe((value) -> System.out.println(value), (error) -> System.out.println(error.getMessage()), () -> System.out.println("Processed all the messages"));
        Thread.sleep(1000);

    }

    @Test
    public void testFluxWithWithExceptionWithOnErrorResume(){
        Flux<Integer> numbersFlux = Flux.fromIterable(Arrays.asList(11))
                                        .concatWith(Flux.error(new IllegalArgumentException()))
                                        .concatWith(Flux.just(88))
                                        .concatWith(Flux.error(new NullPointerException()))
                                        .onErrorResume(e -> Flux.just(44));
        numbersFlux.log().subscribe(data -> System.out.println("Data :: "+data), (error) -> System.out.println("Error :: "+error), () -> System.out.println("====Completed ===="));
        StepVerifier.create(numbersFlux).expectNext(11, 44)
                                        .expectComplete()
                                        .verify();
    }

    @Test
    public void testWithLog(){
        Flux<String> message = Flux.just("one", "two", "three");
        message
                .log()
              .subscribe(value -> value.toUpperCase(), (error ) -> System.out.println(error), ()-> System.out.println("completed.."));
    }

    @Test
    public void testWithLogAndSubscription(){
        Flux<String> messageFlux = Flux.just("one", "two", "three").concatWith(Flux.error(new IllegalArgumentException()));//.onErrorReturn("fallback");

        StepVerifier.create(messageFlux.log()).expectSubscription().expectNext("one", "two", "three").expectError().verify();
    }

}