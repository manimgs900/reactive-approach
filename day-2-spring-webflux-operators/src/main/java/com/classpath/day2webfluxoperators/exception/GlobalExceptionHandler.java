package com.classpath.day2webfluxoperators.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
@RequiredArgsConstructor
@Order(-2)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        log.error(" Exception :: "+ exception.getMessage());
        System.out.println(" Inside the exception handler :: "+ exception.getMessage());
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = null;

            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            try {
                dataBuffer = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(new Error (100, exception.getMessage())));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}

@RequiredArgsConstructor
@Getter
class Error {
    private final int code;
    private final String message;
}
