package com.hm.zti.fis.musicpal.person;

import com.hm.zti.fis.musicpal.event.Event;
import com.hm.zti.fis.musicpal.link.Link;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Node("Person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private PersonRole personRole;

    @Relationship(type = "CREATED", direction = Relationship.Direction.OUTGOING)
    private List<Event> ownedEvents;

    @Relationship(type = "TAKES_PART", direction = Relationship.Direction.OUTGOING)
    private Set<Event> participatedEvents;

    @Relationship(type = "UPVOTED", direction = Relationship.Direction.OUTGOING)
    private Set<Link> upVotes;

    @Relationship(type = "DOWNVOTED", direction = Relationship.Direction.OUTGOING)
    private Set<Link> downVotes;


    public Person(String firstName, String lastName, String email, String password, PersonRole personRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.personRole = personRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(personRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}