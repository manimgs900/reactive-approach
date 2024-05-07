package com.classpath.day2webfluxoperators.config;

import com.classpath.day2webfluxoperators.model.Order;
import com.classpath.day2webfluxoperators.repository.OrderRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class BootstrapAppData implements ApplicationListener<ApplicationReadyEvent> {

    private final OrderRepository orderRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Faker faker = new Faker();
        IntStream.range(1,100).forEach(index -> {
            Order order = Order.builder()
                                .orderDate(LocalDate.now()).email(faker.internet().emailAddress())
                                .name(faker.name().fullName())
                                .build();
            this.orderRepository.save(order).subscribe(savedOrder -> System.out.println(" Saved the order "));
        });
    }
}
