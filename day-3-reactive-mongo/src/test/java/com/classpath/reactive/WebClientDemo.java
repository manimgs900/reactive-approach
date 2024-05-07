package com.classpath.reactive;

import com.classpath.reactive.dto.ProductDto;
import io.netty.channel.ChannelOption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.function.Predicate;


public class WebClientDemo {

    public static void main(String[] args) throws InterruptedException {

        HttpClient httpClient = HttpClient.create().tcpConfiguration(
                tcpClient -> tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
        );
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        Predicate<HttpStatus> httpPredicate = httpStatus -> httpStatus.is2xxSuccessful();

        WebClient webClient = WebClient
                                    .builder()
                                    .clientConnector(connector)
                                    .baseUrl("http://localhost:9292")
                                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .build();

        Flux<ProductDto> getProductsFlux = webClient
                                            .get()
                                            .uri("/api/products")
                                            .header("Authorization", "Bearer token")
                                            .retrieve()
                                            .onStatus(httpPredicate, httpStatus -> {
                                                if(httpStatus.statusCode() == HttpStatus.BAD_REQUEST){
                                                    return Mono.just(new IllegalArgumentException(""));
                                                }
                                                return Mono.just(new RuntimeException(""));
                                            })
                                            .bodyToFlux(ProductDto.class);



        //getProductsFlux.subscribe(res -> System.out.println(res));

        Mono<ProductDto> saveProduct = webClient
                                            .post()
                                            .uri("/api/products")
                                            .body(Mono.just(ProductDto.builder().name("MacbookPro").price(250000).qty(1).build()), ProductDto.class)
                                            .header("Authorization", "Bearer token")
                                            .retrieve()
                                            .bodyToMono(ProductDto.class);

        ProductDto productDto = saveProduct.block();
        System.out.println(productDto);
        saveProduct.subscribe(res -> System.out.println(res));




    }
}
