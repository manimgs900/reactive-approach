package com.classpath.reactor.core;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class SubscriberDemo {

    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(12,33,12,45,100,34,67,33,132);//.concatWith(Flux.error(new IllegalArgumentException("invalid data")));

        /*numbers.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe method is invoked :::: ");
                subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer value) {
                System.out.println("On next event called :: "+ value);
            }

            @Override
            public void onError(Throwable exception) {
                System.out.println("On Error method called :: " + exception.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("On Complete method called");
            }
        });*/
/*
        numbers.subscribe(new Subscriber<>() {
            private Subscription subscription;
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe method is invoked :::: ");
                this.subscription = subscription;
                subscription.request(2);
            }

            @Override
            public void onNext(Integer value) {
                if ( value % 2 == 0){
                    System.out.println("On next event called :: "+ value);
                    System.out.println("Processing the even numbers :: ");
                    subscription.request(2);
                }
            }

            @Override
            public void onError(Throwable exception) {
                System.out.println("On Error method called :: " + exception.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("On Complete method called");
            }
        });*/

        numbers.subscribe(new Subscriber<Integer>() {
            private Subscription subscription;
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe method is invoked :::: ");
                this.subscription = subscription;
                subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer value) {
                System.out.println("Value is :: "+ value);
                if(value == 12) {
                    subscription.request(1);
                }
            }

            @Override
            public void onError(Throwable exception) {
                System.out.println("On Error method called :: " + exception.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("On Complete method called");
            }
        });
        numbers.concatWithValues(20, 40, 60, 80);
    }
}
