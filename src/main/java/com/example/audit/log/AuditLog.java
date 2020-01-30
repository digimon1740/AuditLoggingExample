package com.example.audit.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@Entity
@Table(name="audit_logs")
public class AuditLog {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "target")
    private String target;

    @Column(name = "actor")
    private long actor;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private AuditAction action;

    @Column(name = "signature")
    private String signature;

    @Column(name = "parameters")
    private String parameters;

    @Column(name="ip")
    private String ip;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
