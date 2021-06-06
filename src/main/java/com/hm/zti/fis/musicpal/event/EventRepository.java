package com.hm.zti.fis.musicpal.event;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends Neo4jRepository<Event, Long> {
    @Query("MATCH (e:event) where id(e) = $eventId\n" +
            "MATCH (p:Person) where id(p) = $personId\n" +
            "with e,p \n" +
            "MERGE (p)-[:TAKES_PART]->(e) \n" +
            "Return e;")
    Event setParticipant(@Param("eventId") Long eventId, @Param("personId") Long personId);

    @Query("MATCH (e:event) where id(e) = $eventId\n" +
            "MATCH (p:Person) where id(p) = $ownerId\n" +
            "with e,p \n" +
            "MERGE (p)-[:CREATED]->(e) \n" +
            "Return e;")
    Event setOwnership(@Param("eventId") Long eventId, @Param("ownerId") Long ownerId);

    @Query("MATCH (e:event) where id(e) = $eventId SET e.closed = true return e")
    Event setClosed(@Param("eventId") Long eventId);

}
