package com.classpath.day2webfluxoperators.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@Table("orders")
@NoArgsConstructor
@Valid
public class Order {

    @Id
    private long orderId;
    @Email(message = " INvalid email format")
    private String email;
    @NotEmpty(message = "Name is mandatory field")
    private String name;
    @PastOrPresent(message = " order date cannot be in the future")
    private LocalDate orderDate;
}
