package com.example.audit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Actor {

    @Getter
    private long id;

    @Getter
    private String ip;
}
