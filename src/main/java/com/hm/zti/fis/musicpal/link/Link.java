package com.hm.zti.fis.musicpal.link;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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

    public Link(String link) {
        this.link = link;
    }
}
