package com.classpath.streams;

import java.util.Scanner;

public class Solution {
    static Fabric[] allFabrics = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        allFabrics = new Fabric[n];

        for(int i=0; i<n; i++) {
            int id = sc.nextInt();
            sc.nextLine();
            String name = sc.nextLine();
            int stock = sc.nextInt();
            double price = sc.nextDouble();

            Fabric fabric = new Fabric(id, name, stock, price);
            allFabrics[i] = fabric;
        }

        sc.nextLine();
        String name = sc.nextLine();

        Fabric maxFabric = findFabricWithMaximumPrice(allFabrics);
        if(maxFabric != null) {
            System.out.println("id-"+maxFabric.getId());
            System.out.println("name-"+maxFabric.getName());
            System.out.println("availableStock-"+maxFabric.getAvailableStock());
            System.out.println("price-"+maxFabric.getPrice());
        } else {
            System.out.println("No Fabric found with mentioned attribute ");
        }

        Fabric fabric = searchFabricByName(allFabrics, name);
        if(fabric != null) {
            System.out.println("Id-"+fabric.getId());
            System.out.println("name-"+fabric.getName());
            System.out.println("availableStock-"+fabric.getAvailableStock());
            System.out.println("price-"+fabric.getPrice());
        } else {
            System.out.println("No Fabric found with mentioned attribute ");
        }
    }

    private static Fabric findFabricWithMaximumPrice(Fabric[] fabrics) {
        Fabric maxPriceFabric = null;

        double maxPrice = fabrics[0].getPrice();
        for(int i=0; i<fabrics.length; i++) {
            if(fabrics[i].getPrice() > maxPrice) {
                maxPrice = fabrics[i].getPrice();
                maxPriceFabric = fabrics[i];
            }
        }

        return maxPriceFabric;
    }

    private static Fabric searchFabricByName(Fabric[] fabrics, String name) {
        Fabric fabric = null;

        for(int i=0; i<fabrics.length; i++) {
            Fabric fab = fabrics[i];
            if(fab.getName().equalsIgnoreCase(name)) {
                fabric = fab;
            }
        }

        return fabric;
    }
}

class Fabric {
    private int id;
    private String name;
    private int availableStock;
    private double price;

    public Fabric(int id, String name, int availableStock, double price) {
        this.id = id;
        this.name = name;
        this.availableStock = availableStock;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


class User {
    private int id;
    private String name;
    private String email;
    private int age;

    public User(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

