package com.classpath.streams;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FlatmapOpertor {

    public static void main(String[] args) {
        /*int[][] arrays = {{1,2,}, {3,4}, {1,3}, {4, 5}, {5,6}};
        Stream<int[]> streamOfIntegers = Arrays.stream(arrays);
        //interested in Stream<int>
        *//*
          Stream<Stream<T>>  Stream<T>
          Stream<List<T>>   Stream<T>
        *//*
        IntStream intStream = streamOfIntegers.flatMapToInt(Arrays::stream);
        intStream.distinct().forEach(System.out::println);*/

        char[] c = {'a', 'b', 'c'};
        String s = null;
        System.out.println(s.copyValueOf(c));
    }
}
