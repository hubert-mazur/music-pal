package com.hm.zti.fis.musicpal.person;


import org.neo4j.driver.internal.shaded.reactor.core.publisher.Mono;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> getFirstByEmail(String email);
    Boolean existsByEmail(String email);
    Object deleteAllByEmail(String email);
}
