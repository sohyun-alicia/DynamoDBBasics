package com.example.dynamodbbasics.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@DynamoDBTable(tableName = "CuteCharacters")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class CuteCharacters {

    @Id
    @DynamoDBIgnore
    private CuteCharactersId id;
    @DynamoDBHashKey
    public String brand;
    @DynamoDBRangeKey
    private String name;
    @DynamoDBAttribute
    private String animal;
    @DynamoDBAttribute
    private String hometown;
    @DynamoDBAttribute
    private int age;


}
