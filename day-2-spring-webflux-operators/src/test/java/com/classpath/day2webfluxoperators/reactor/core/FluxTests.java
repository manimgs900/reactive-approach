package com.classpath.day2webfluxoperators.reactor.core;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.relational.core.sql.In;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FluxTests {

    @Test
    public void testFluxFilter(){
        Flux<Integer> integerFlux = Flux.just(11, 22, 33, 44);
        integerFlux.subscribe(value -> System.out.println(value));
    }

    @Test
    public void testZip(){
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh");
        Flux<String> lastNames = Flux.just("Kumar", "Mehta", "Rao");
        Flux<String> cities = Flux.just("Kanpur", "Mumbai", "Pune");

      //  Flux.zip(firstNames, lastNames, (firstName, lastName) -> firstName + ", "+ lastName).subscribe(fullName -> System.out.println(fullName));
        Flux.zip(firstNames, lastNames, cities).map(result -> result.getT1() + ","+ result.getT2() + " lives in "+ result.getT3())
                .subscribe(response -> System.out.println(response));

    }

    @Test
    public void testZipWithMono(){
        Mono<String> currency = Mono.just("INR");
        Mono<Double> money = Mono.just(35_000d);

        currency.zipWith(money, (c, amount) ->  amount + " in "+c)
                .subscribe(response -> System.out.println(response));
    }

    @Test
    public void testFlatMap(){
        Flux<String> stringFlux = Flux.fromIterable(List.of("Mumbai", "Pune", "Bangalore"));
        Flux<String[]> mapResult = stringFlux.map(v -> v.split(""));
        Flux<String> flatMapOutput = mapResult.flatMap(out -> Flux.just(out));
        flatMapOutput.distinct().map(String::toUpperCase).subscribe(val -> System.out.println(val));
    }

    @Test
    public void testReduce(){
        Flux<Integer> ages = Flux.just(11, 22, 55, 66, 33, 99);
        //reduction opertators
        /*
             sum, max, avg, min, product
         */
        BiFunction<Integer, Integer, Integer> max = (input1, input2) -> Integer.max(input1, input2);
        BiFunction<Integer, Integer, Integer> sum = (input1, input2) -> input1 + input2;
        //ages.reduce(max).subscribe(maxAge -> System.out.println(" Max age:: "+ maxAge));
        ages.reduce(100, sum).subscribe(total -> System.out.println(" Total age:: "+ total));
    }

    @Test
    public void concatMap() throws InterruptedException {
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh");
        //Flux<String> citiesFlux = Flux.fromIterable(List.of("Mumbai", "Pune", "Bangalore"));

        firstNames.concatMap(name -> Flux.just(name.split(""))).delayElements(Duration.ofMillis(200)).subscribe(res -> System.out.println(res));
        Thread.sleep(50_000);
    }

    @Test
    public void testSwitchMap() throws InterruptedException {
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh");
        Flux<Integer> empIds = Flux.just(1, 2 ,3 ,4, 5, 6, 7, 8 );
        empIds.switchMap(id -> getEmployeeDetails(id)).subscribe(res -> System.out.println(res));
        //firstNames.switchMap(name -> Flux.just(name.toUpperCase())).subscribe(response -> System.out.println(response));
        Thread.sleep(20_0000);
    }

    @Test
    public void testTransform(){
        //Stream.of(1, 2, 3, 4).filter(num -> num % 2 == 0).max(Integer::compareTo).;
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh", "Kiran", "Manu");

        Function<Flux<String>, Flux<String>> filter = name -> name.filter( value -> value.length() < 7);

        Flux<String> responseFlux = firstNames.transform(filter);
        responseFlux.subscribe(data -> System.out.println(data));
    }

    @Test
    public void testDefaultIfEmpty(){
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh", "Kiran", "Manu");

        Function<Flux<String>, Flux<String>> filter = name -> name.filter( value -> value.length() < 3);

        Flux<String> responseFlux = firstNames
                                        .transform(filter)
                                        .defaultIfEmpty("Default");

        responseFlux.subscribe(data -> System.out.println(data));
    }

    @Test
    public void testSwitchIfEmpty(){
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh", "Kiran", "Manu");

        Function<Flux<String>, Flux<String>> filter = name -> name.filter( value -> value.length() < 3);

        Flux<String> responseFlux = firstNames
                                        .transform(filter)
                                        .switchIfEmpty(Flux.just("Shiva", "Ke", "Vinod"). transform(filter));

        responseFlux.subscribe(data -> System.out.println(data));
    }

    @Test
    public void testFlatMapManyOperator(){
        Flux<String> response = Mono.just("Hello-world").flatMapMany(value -> Flux.just(value.split("-")));
        response.subscribe(data -> System.out.println(data));
    }

    @Test
    public void testDoOn(){
        Flux<String> firstNames = Flux.just("Harish", "Vishnu", "Ramesh", "Kiran", "Manu");
        Flux<String> fluxWithDoOnMethods = firstNames
                .filter(name -> name.length() < 5)
                .doOnNext((next) -> System.out.println("onNext event : " + next))
                .doOnSubscribe(subscription -> System.out.println(subscription))
                .doOnComplete(() -> System.out.println(" On complete event triggered"));

        fluxWithDoOnMethods.subscribe(val -> System.out.println(val));
    }


    // error scenarios
    @Test
    public void testErrorMap(){
        Flux<Integer> invalidData = Flux.fromIterable(List.of(11, 22, 33, 44, 55, 66))
                .map(val -> {
                    if (val == 33) {
                        throw new IllegalArgumentException("invalid data");
                    }
                    return val;
                })
                .onErrorMap(throwable -> {
                    System.out.println(" Exception while processing the record" + throwable.getMessage());
                    return new IllegalStateException(throwable.getMessage());
                });

        invalidData.subscribe(val -> System.out.println(val));
    }

    @Test
    public void testErrorContinue(){
        Flux<Integer> invalidData = Flux.fromIterable(List.of(11, 22, 33, 44, 55, 66))
                .map(val -> {
                    if (val == 33) {
                        throw new IllegalArgumentException("invalid data");
                    }
                    return val;
                })
                .onErrorContinue((throwable, value) -> {
                    System.out.println(throwable.getMessage() + " message");
                    System.out.println(value);
                });

        invalidData.subscribe(val -> System.out.println(val), (error) -> System.out.println("error"+ error), ()-> System.out.println("on completion") );
    }

    @Test
    public void testErrorReturn(){
        Flux<Integer> invalidData = Flux.fromIterable(List.of(11, 22, 33, 44, 55, 66))
                .map(val -> {
                    if (val == 33) {
                        throw new IllegalArgumentException("invalid data");
                    }
                    return val;
                })
                .onErrorReturn(38);
        invalidData.subscribe(val -> System.out.println(val), (error) -> System.out.println("error"+ error), ()-> System.out.println("on completion") );
    }

    @Test
    public void testEmpty(){
        Mono.empty().subscribe((v) -> System.out.println(v), (e) -> System.out.println(e), () -> System.out.println("Completed"));
    }

    @Test
    public void testSubscription() throws InterruptedException {
        Flux<Integer> integerFlux = Flux.range(1, 100).delayElements(Duration.ofMillis(200));
        integerFlux.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
        //.subscribe(val -> System.out.println(val));
        Thread.sleep(20_000);
    }

    @Test
    public void testFluxWithCreate() throws InterruptedException {
        Flux<Integer> integerFlux = Flux.create((FluxSink<Integer> sink) -> {
            IntStream
                    .range(1, 10)
                    .forEach(index -> {
                        try {
                            System.out.println(" Sleep in "+ Thread.currentThread().getName());
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(" Generating the next element:: "+ index + " Thread :: "+ Thread.currentThread().getName());
                        sink.next(index * 4);
                    });
        });

        integerFlux
                .delayElements(Duration.ofMillis(100))
                .subscribe(val -> {
                        System.out.println( "Subscriber 1 " +val + "THread name :: "+ Thread.currentThread().getName()) ;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    System.out.println(" After waking up :: "+Thread.currentThread().getName());
        });
        integerFlux.delayElements(Duration.ofMillis(400)).subscribe(val -> System.out.println( "Subscriber 2 " +val));
        Thread.sleep(40_000);
    }

    @Test
    public void fluxGenerate() throws InterruptedException {

        Flux<String> stringFlux = Flux.generate( () -> 1, (state, sink) -> {
            System.out.println(" Emitting the event ::: " + state);
            sink.next(" state "+ state * 3 );
            if(state  == 1000000) {
                sink.complete();
            }
            return state + 1;
        });

        stringFlux.delayElements(Duration.ofMillis(1000))
                .subscribe(result -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Subscribing" + result);
                });

        Thread.sleep(200_000);
    }


    @Test
    public void coldPusblisherDemo() throws InterruptedException {
        Flux<Integer> coldFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(10));

        coldFlux.subscribe(value -> System.out.println("Subscriber 1 :: "+ value));
        coldFlux.subscribe(val -> System.out.println("Subscriber 2 :: "+ val));
        coldFlux.subscribe(val -> System.out.println("Subscriber 3 :: "+ val));

        Thread.sleep(20000);
    }

    @Test
    public void hotPusblisherDemo() throws InterruptedException {
        Flux<Integer> hotPublisher = Flux.range(1, 1000).delayElements(Duration.ofSeconds(2)).share();
        hotPublisher.subscribe(value -> System.out.println("Subscriber 1 :: "+ value));
        Thread.sleep(6000);
        System.out.println("Subscriber 2 is about to subscribe :: ");
        hotPublisher.subscribe(val -> System.out.println("Subscriber 2 :: "+ val));
        Thread.sleep(6000);
        System.out.println("Subscriber 3 is about to subscribe :: ");
        hotPublisher.subscribe(val -> System.out.println("Subscriber 3 :: "+ val));
        Thread.sleep(20000);
    }

    @Test
    public void hotPusblisherDemoWithPublish() throws InterruptedException {
        ConnectableFlux<Integer> hotPublisher = Flux.range(1, 1000).delayElements(Duration.ofSeconds(2)).publish();
        //triggering the publish events when the min threshold is met.
        //Flux<Integer> connectFlux = hotPublisher.autoConnect(1);
        //explicitly triggering the hot publisher
        hotPublisher.connect();
        hotPublisher.subscribe(value -> System.out.println("Subscriber 1 :: "+ value));
        Thread.sleep(6000);
        System.out.println("Subscriber 2 is about to subscribe :: ");
        hotPublisher.subscribe(val -> System.out.println("Subscriber 2 :: "+ val));
        Thread.sleep(6000);
        System.out.println("Subscriber 3 is about to subscribe :: ");
        hotPublisher.subscribe(val -> System.out.println("Subscriber 3 :: "+ val));
        Thread.sleep(20000);
    }

    @Test
    public void testRetry(){

        Flux<Integer> fetchUserAges = Flux.just(22,45, 18,56, 17);

        Flux<String> retryFlux = fetchUserAges
                .map(age -> validateAgeForCastingVote(age))
                .doOnNext(err -> System.out.println(" Error :: " + err))
                .retry(3);


        retryFlux.log().subscribe(response -> System.out.println(response));
    }

    @Test
    public void testRetryWithException() throws InterruptedException {
        Flux<Integer> fetchUserAges = Flux.just(22,45, 17, 56, 112, 17);

        Flux<String> retryFlux = fetchUserAges
                .map(age -> validateAgeForCastingVote(age))
                .doOnNext(err -> System.out.println(" Error :: " + err))
                .log()
                .retryWhen(
                        Retry.backoff(3,Duration.ofSeconds(2))
                        .jitter(0.5)
                        .filter(throwable -> throwable instanceof IllegalArgumentException)
                )
                .onErrorContinue((error) -> error instanceof IllegalArgumentException, (throwable, value) -> {
                    System.out.println("******************");
                    System.out.println(throwable.getMessage());
                    System.out.println("******************");
                    System.out.println(" Value :::::: "+value);
                } );
        retryFlux.log().subscribe(res -> System.out.println(res));

       // Thread.sleep(20000);

    }

    @Test
    public void testRetryWithReturnException() throws InterruptedException {
        Flux<Integer> fetchUserAges = Flux.just(22,45, 17, 56, 112, 17);

        Flux<String> retryFlux = fetchUserAges
                .map(age -> validateAgeForCastingVote(age))
                .doOnNext(err -> System.out.println(" Error :: " + err))
                .log()
                .retryWhen(
                        Retry.backoff(3,Duration.ofSeconds(2))
                                .jitter(0.5)
                                .filter(throwable -> throwable instanceof IllegalArgumentException)
                )
                .onErrorReturn(" Error :: Fallback");
        retryFlux.log().subscribe(res -> System.out.println(res));

        Thread.sleep(20000);

    }

    private static String validateAgeForCastingVote(int age){
        if(age < 18){
            throw new IllegalArgumentException("Invalid age for casting vote");
        } else if (age > 110){
            throw new IllegalStateException("illegal state");
        }
        return "Valid :: "+ age;
    }




    private Flux<String> getEmployeeDetails(Integer id) {
        return Flux.just(id+ "-"+ UUID.randomUUID().toString()).delayElements(Duration.ofSeconds(2 * id));
    }
}
