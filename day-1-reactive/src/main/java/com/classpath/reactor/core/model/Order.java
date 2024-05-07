package com.classpath.reactor.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private String email;
    private Set<LineItem> lineItems;

    public void addLineItem(LineItem lineItem){
        if (this.lineItems == null){
            this.lineItems = new HashSet<>();
        }
        this.lineItems.add(lineItem);
    }
}
