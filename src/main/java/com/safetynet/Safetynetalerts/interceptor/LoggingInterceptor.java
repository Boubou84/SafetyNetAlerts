package com.safetynet.safetynetalerts.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log avant le traitement de la requête
        logger.info("Avant la requête: " + request.getMethod() + " " + request.getRequestURI()+ " " + request.getQueryString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // Log après le traitement de la requête mais avant l'envoi de la réponse
        logger.info("Après la requête: " + request.getMethod() + " " + request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Log après la complétion de la requête
        logger.info("Après la complétion de la requête: " + request.getMethod() + " " + request.getRequestURI());
    }
}

