package com.hm.zti.fis.musicpal.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventRequest {
    private String title;
    private String description;
    private Set<Long> participants;
}
