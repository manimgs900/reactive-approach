package com.classpath.reactor.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {
    private int id;
    private double price;
    private int qty;
    private String name;
}
