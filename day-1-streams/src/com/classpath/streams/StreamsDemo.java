package com.classpath.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamsDemo {

    public static void main(String[] args) {
        //imperative style of coding
        List<String> products = new ArrayList<>();
        products.add("IPhone");
        products.add("Galaxy");
        products.add("RealMe");
        products.add("Nord");
        products.add("Oppo-F17");
        products.add("Oppo-F19");

        //Declarative
                                   // companion classes
        List<String> newProducts = Arrays.asList("IPhone", "Galaxy", "RealMe", "Nord", "Oppo-F17", "Oppo-F19", "MacBookPro", "IPad");
        //List<String> newProducts = List.of("IPhone", "Galaxy", "RealMe", "Nord", "Oppo-F17", "Oppo-F19", "MacBookPro", "IPad");

        //Functional style of programming
        //assigned a function to a variable
        //Functional descriptor - Predicate => accepts input and returns boolean
        Predicate<String> isIPhone = (product) -> product.equalsIgnoreCase("IPhone");
        Predicate<String> isMackBookPro = (product) -> product.equalsIgnoreCase("MacBookPro");
        Predicate<String> isIpad = (product) -> product.equalsIgnoreCase("IPad");

        Predicate<String> isNoIphone = isIPhone.negate();

        Predicate<String> isApple = isIPhone.or(isMackBookPro).or(isIpad);

        Predicate<String> isNotApple = isApple.negate();

        //Function , Functional descriptor of Function => input value, outputs another value
        Function<String, String> upperCase = (product) -> product.toUpperCase();

        //Consumer, Functional descriptor of Consumer -> accepts input, returns void
        Consumer<String> printProduct = (product) -> System.out.println(product);

        newProducts.stream()
                .filter(isApple)
                .map(upperCase)
                .forEach(printProduct);
    }
}
