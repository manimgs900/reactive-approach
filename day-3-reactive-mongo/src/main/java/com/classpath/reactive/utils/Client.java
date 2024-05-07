package com.classpath.reactive.utils;

import com.classpath.reactive.dto.ProductDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(1));

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();

        WebClient webClient2 = WebClient.builder()
                .baseUrl("http://localhost:9292")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

                .build();

        Flux<ProductDto> productDtoFlux = WebClient.create("http://localhost:9292/products")
                .post()
                .body(Mono.just(ProductDto.builder().name("can").price(25).qty(2).build()), ProductDto.class)
                .retrieve()
                .bodyToFlux(ProductDto.class);
        productDtoFlux.subscribe(v -> System.out.println(v));

/*
        Flux<ProductDto> productDtoFlux = WebClient.create("http://localhost:9292/products")
                .get()
                .retrieve()
                .bodyToFlux(ProductDto.class);

        productDtoFlux.subscribe(val -> System.out.println(val));

        */Thread.sleep(30000);
        //
    }
}
