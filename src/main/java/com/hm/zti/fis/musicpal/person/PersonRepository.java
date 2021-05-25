package com.hm.zti.fis.musicpal.person;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> getFirstByEmail(String email);
    Boolean existsByEmail(String email);
    Object deleteAllByEmail(String email);
    Collection<PersonBasicInfo> findAllBy();
    PersonBasicInfo findPersonById(Long id);
    void deleteByEmail(String email);
}
