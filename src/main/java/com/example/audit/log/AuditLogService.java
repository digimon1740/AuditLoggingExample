package com.example.audit.log;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void recordLog(AuditLog auditLog) {
        auditLog.setCreatedAt(LocalDateTime.now());
        repository.save(auditLog);
    }
}
