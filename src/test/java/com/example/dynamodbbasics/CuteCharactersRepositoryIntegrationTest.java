package com.example.dynamodbbasics;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.*;
import com.example.dynamodbbasics.model.CuteCharacters;
import com.example.dynamodbbasics.model.CuteCharactersId;
import com.example.dynamodbbasics.repository.CuteCharactersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DynamoDbBasicsApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" })
public class CuteCharactersRepositoryIntegrationTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    CuteCharactersRepository repository;

    @Before
    public void setup() {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest;
        ListTablesResult listTablesResult = amazonDynamoDB.listTables();
        if (!listTablesResult.getTableNames().contains("CuteCharacters")) {
            tableRequest = dynamoDBMapper.generateCreateTableRequest(CuteCharacters.class);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);
        }
//        dynamoDBMapper.batchDelete(repository.findAll());
    }

    @Test
    public void queryWithPrimaryKey1(){
        CuteCharacters cuteCharacters = new CuteCharacters();
        cuteCharacters.setBrand("kakao");

        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS("chunsik"));

        DynamoDBQueryExpression<CuteCharacters> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withHashKeyValues(cuteCharacters)
                .withRangeKeyCondition("name", rangeKeyCondition)
                .withFilterExpression("");

        PaginatedQueryList<CuteCharacters> result = dynamoDBMapper.query(CuteCharacters.class, queryExpression);
        for (CuteCharacters characters : result) {
            System.out.println("characters = " + characters);
        }
    }

    @Test
    public void queryWithPrimaryKey2() {
        HashMap<String, String> namesMap = new HashMap<>();
        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        DynamoDBQueryExpression<CuteCharacters> queryExpression = new DynamoDBQueryExpression<>();

        namesMap.put("#brand", "brand");
        namesMap.put("#name", "name");
        valueMap.put(":brand", new AttributeValue().withS("kakao"));
        valueMap.put(":name", new AttributeValue().withS("chunsik"));

        queryExpression.withKeyConditionExpression("#brand = :brand and #name = :name")
                .withExpressionAttributeNames(namesMap)
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<CuteCharacters> result = dynamoDBMapper.query(CuteCharacters.class, queryExpression);
        for (CuteCharacters characters : result) {
            System.out.println("characters = " + characters);
        }

    }



    @Test
    public void givenItemWithExpected_whenRunFindAll_thenItemIsFound() {
        List<CuteCharacters> characters = new ArrayList<>();
        CuteCharacters chunsik = new CuteCharacters(new CuteCharactersId(), "kakao", "chunsik", "cat", "Inje", 3);
        CuteCharacters ryan = new CuteCharacters(new CuteCharactersId(), "kakao", "ryan", "tiger", "Seoul", 5);
        CuteCharacters apeach = new CuteCharacters(new CuteCharactersId(), "kakao", "apeach", "plum", "Cheongju", 4);
        CuteCharacters minini = new CuteCharacters(new CuteCharactersId(), "ig", "minini", "chick", "Jeju", 1);
        CuteCharacters pikachu = new CuteCharacters(new CuteCharactersId(), "pokemon", "pikachu", "rat", "Paju", 25);
        CuteCharacters ggobuki = new CuteCharacters(new CuteCharactersId(), "pokemon", "ggobuki", "turtle", "Sokcho", 18);
        CuteCharacters mangnanyong = new CuteCharacters(new CuteCharactersId(), "pokemon", "mangnanyong", "dragon", "Daejeon", 15);
        characters.add(chunsik);
        characters.add(ryan);
        characters.add(apeach);
        characters.add(minini);
        characters.add(pikachu);
        characters.add(ggobuki);
        characters.add(mangnanyong);

        repository.saveAll(characters);

        List<CuteCharacters> all = (List<CuteCharacters>) repository.findAll();

        assertThat(all.size(), is(greaterThan(0)));
        assertThat(all.size(), is(equalTo(7)));
    }
}
