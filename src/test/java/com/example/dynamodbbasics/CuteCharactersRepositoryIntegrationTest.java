package com.example.dynamodbbasics;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.example.dynamodbbasics.model.CuteCharacters;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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

        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(CuteCharacters.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

        dynamoDBMapper.batchDelete(repository.findAll());
    }

    @Test
    public void givenItemWithExpected_whenRunFindAll_thenItemIsFound() {
        CuteCharacters chunsik = new CuteCharacters("kakao", "chunsik", "cat", "Inje", 3);
        repository.save(chunsik);

        List<CuteCharacters> all = (List<CuteCharacters>) repository.findAll();
        assertThat(all.size());
    }
}
