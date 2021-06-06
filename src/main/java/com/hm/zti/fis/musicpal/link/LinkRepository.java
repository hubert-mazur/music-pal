package com.hm.zti.fis.musicpal.link;


import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface LinkRepository extends Neo4jRepository<Link, Long> {
//    @Query("MATCH (p:Person)-[c:UPVOTED]-(l:link) where id(l) = $linkId RETURN p;")
//    List<Person> countUpVotes(@Param("linkId") Long linkId);
//
//    @Query("MATCH (p:Person)-[c:DOWNVOTED]-(l:link) where id(l) = $linkId RETURN p;")
//    List<Person> countDownVotes(@Param("linkId") Long linkId);
}
