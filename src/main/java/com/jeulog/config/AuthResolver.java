package com.jeulog.config;

import com.jeulog.config.data.UserSession;
import com.jeulog.domain.Session;
import com.jeulog.exception.Unauthorized;
import com.jeulog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");

        if(!StringUtils.hasText(accessToken)) throw new Unauthorized();

        // 데이터베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);

        return new UserSession(session.getUser().getId());
    }
}
