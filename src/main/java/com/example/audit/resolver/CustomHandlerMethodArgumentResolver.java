package com.example.audit.resolver;

import com.example.audit.user.Actor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int UNAUTHZ_USER_ID = -1;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Actor.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        long userId = NumberUtils.toLong(webRequest.getHeader("x-user-id"), UNAUTHZ_USER_ID);

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String ip = getClientIp(request);

        return userId <= 0 ? null : new Actor(userId, ip);
    }

    private String getClientIp(HttpServletRequest request) {
        final String forwardedAddresses = request.getHeader("X-FORWARDED-FOR");

        if (forwardedAddresses != null
                && !"unknown".equalsIgnoreCase(forwardedAddresses)) {
            int pos = forwardedAddresses.indexOf(",");
            if (pos > 0) {
                return forwardedAddresses.substring(0, pos);
            }
            return forwardedAddresses;
        }
        return request.getRemoteAddr();
    }

}