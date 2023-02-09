package com.example.dynamodbbasics.repository;

import com.example.dynamodbbasics.model.CuteCharacters;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface CuteCharactersRepository extends CrudRepository<CuteCharacters, String> {
    Optional<CuteCharacters> findByBrandAndName(String brand, String name);
}
