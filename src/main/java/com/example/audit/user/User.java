package com.example.audit.user;

import com.example.audit.aspect.AuditExclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String email;

    @AuditExclude
    private int age;

    @AuditExclude
    private String phoneNumber;
}
