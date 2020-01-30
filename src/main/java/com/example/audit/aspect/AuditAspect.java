package com.example.audit.aspect;

import com.example.audit.log.AuditAction;
import com.example.audit.log.AuditLog;
import com.example.audit.log.AuditLogService;
import com.example.audit.user.Actor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Component
public class AuditAspect {

    private AuditLogService auditLogService;

    private ObjectMapper mapper;

    public AuditAspect(AuditLogService auditLogService, ObjectMapper mapper) {
        this.auditLogService = auditLogService;
        this.mapper = mapper;
    }

    @AfterReturning("@annotation(auditAnnotation)")
    public void audit(JoinPoint joinPoint, Audit auditAnnotation) {
        String target = auditAnnotation.target();
        String fullTargetClassName = auditAnnotation.targetClass().toString();
        String targetClassName = fullTargetClassName.substring(fullTargetClassName.lastIndexOf(".") + 1);

        if (StringUtils.isEmpty(target) && "Object".equals(targetClassName)) {
            return;
        }
        if (!"Object".equals(targetClassName)) {
            target = targetClassName;
        }

        AuditAction action = auditAnnotation.action();
        Actor actor = extractActor(joinPoint);
        if (action == AuditAction.NONE || actor == null) {
            return;
        }

        // TODO event bus로 뺄까?
        AuditLog auditLog = AuditLog.builder()
                .target(target)
                .action(action)
                .actor(actor.getId())
                .signature(getSignature(joinPoint))
                .parameters(extractParameters(joinPoint))
                .ip(actor.getIp())
                .build();
        try {
            auditLogService.recordLog(auditLog);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Actor extractActor(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            for (Object arg : args) {
                if (arg instanceof Actor) {
                    return (Actor) arg;
                }
            }
            return null;
        }
        return null;
    }

    private String extractParameters(JoinPoint joinPoint) {
        StringBuilder parameters = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            int idx = 0;
            for (Object arg : args) {
                if (arg instanceof Actor)
                    continue;
                try {
                    Class<?> clazz = arg.getClass();
                    if (clazz.isPrimitive()) {
                        parameters.append(arg.toString());
                    } else {
                        Object clone = clazz.newInstance();
                        List<String> excludeFields = getExcludeFields(clazz);
                        if (!CollectionUtils.isEmpty(excludeFields)) {
                            // TODO setter가 없는 경우에 대한 deep copy 지원하는 라이브러리 찾아보기..
                            BeanUtils.copyProperties(arg, clone, excludeFields.toArray(new String[0]));
                        }
                        parameters.append(mapper.writeValueAsString(clone));
                    }
                } catch (Exception e) {
                    parameters.append(arg.toString());
                }
                if (idx != args.length - 2)
                    parameters.append(",");
                idx++;
            }
        }
        return parameters.toString();
    }

    private List<String> getExcludeFields(Class<?> clazz) {
        List<String> excludeFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            AuditExclude auditExclude = field.getAnnotation(AuditExclude.class);
            if (auditExclude != null) {
                excludeFields.add(field.getName());
            }
        }
        return excludeFields;
    }

    private String getSignature(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.toString();
        return signature.toShortString().replace("..", className.substring(className.indexOf("(") + 1,
                                                                           className.indexOf(")")));
    }
}