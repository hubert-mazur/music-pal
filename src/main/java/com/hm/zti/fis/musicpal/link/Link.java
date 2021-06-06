package com.hm.zti.fis.musicpal.link;


import com.hm.zti.fis.musicpal.person.Person;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;


@Node("link")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Link {
    @Id
    @GeneratedValue
    private Long id;
    private String link;

    @Relationship(type = "DOWNVOTED", direction = Relationship.Direction.INCOMING)
    List<Person> downvoted;
    @Relationship(type = "UPVOTED", direction = Relationship.Direction.INCOMING)
    List<Person> upvoted;

    public Link(String link) {
        this.link = link;
    }
}
