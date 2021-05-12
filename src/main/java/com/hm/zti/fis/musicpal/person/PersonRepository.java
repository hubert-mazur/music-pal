package com.hm.zti.fis.musicpal.person;


import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {
//    Optional<Person> getPersonByEmail(String email);
    Optional<Person> getFirstByEmail(String email);
}
