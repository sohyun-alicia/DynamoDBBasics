package com.example.dynamodbbasics.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;

public class CuteCharactersId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String brand;
    private String name;

    @DynamoDBHashKey
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @DynamoDBRangeKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CuteCharactersId(String brand, String name) {
        this.brand = brand;
        this.name = name;
    }

    public CuteCharactersId() {}
}
