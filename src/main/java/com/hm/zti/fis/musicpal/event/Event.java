package com.hm.zti.fis.musicpal.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hm.zti.fis.musicpal.link.Link;
import com.hm.zti.fis.musicpal.person.Person;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.Set;

@Node("event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@Id")
public class Event {
    @Id
    @GeneratedValue
    Long id;
    private String title;
    private String description;
    private Boolean closed = false;

    @Relationship(type = "TAKES_PART", direction = Relationship.Direction.INCOMING)
    private List<Person> participants;

    @Relationship(type = "HAS", direction = Relationship.Direction.OUTGOING)
    private Set<Link> links;
    
    public Event(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
