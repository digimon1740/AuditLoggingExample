## AOP + Annotation 을 사용한 Audit Logging 예제 
- Spring Boot 2.0.9.RELEASE
- Spring-boot-starter-aop
- Spring-boot-starter-data-jpa
- Spring-boot-starter-jdbc


## Audit Log?
- Martin Fowler : [Audit Log](https://martinfowler.com/eaaDev/AuditLog.html)
- [Audit Logs | Online Help Site24x7](https://www.site24x7.com/help/admin/operations/audit-logs.html)
- https://www.visualclick.com/content/windows_group_policy_gpo_audit_change_tracking.htm#gpo
## 제약사항
- Spring 3.* 대의 레거시 코드에서도 동작가능하여야 한다.
- 여러 종류의 서비스에 붙여야 하므로 범용성이 있어야한다.
- Spring Security를 사용하지 않는 환경도 있어서 Principal 객체로 유저 아이디를 가져오지 못한다.
    - RequestContextHolder 도 있으나 이역시 CommonContext와 같이 ThreadLocal을 사용하고 바인딩 유틸을 컨트롤러에 추가해야함.
    - HandlerMethodArgumentResolver로 컨트롤러 인자에 들어온 경우에만 값을 바인딩 할수 있고, 공통처리가 쉬움 - 개인의견
- 웹에 의존적인 AbstractRequestLogginFilter 등을 사용하지 않는다.
    - before, after가 아닌 after 만 기록함
- audit log에 제외될 필드에 대해 @JsonIgnore, @JsonView, @JsonFilter 등을 사용하지 않음.  필드값이 누락될 수 있기 때문에..