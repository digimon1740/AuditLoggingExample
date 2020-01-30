package com.example.audit.user;

import com.example.audit.aspect.Audit;
import com.example.audit.log.AuditAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Audit(target = "User", action = AuditAction.CREATED)
    @Transactional
    public void create(User user, Actor actor) {
        // do nothing

    }

    @Audit(targetClass = User.class, action = AuditAction.UPDATED)
    @Transactional
    public void update(Long id, User user, Actor actor) {
        // do nothing
    }

    @Audit(targetClass = User.class, action = AuditAction.DELETED)
    @Transactional
    public void delete(Long id, Actor actor) {
        // do nothing
    }
}
