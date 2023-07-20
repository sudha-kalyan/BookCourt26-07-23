package com.CourtReserve.app.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class loginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/public") || requestURI.equals("/")){
            return true;
        }
        // Check if the session variable is present
        Object sessionVariable = request.getSession().getAttribute("user");
        if (sessionVariable == null || sessionVariable == "") {
            response.sendRedirect("/public/login"); // Redirect to the login page if the session variable is not present
            return false; // Stop further processing of the request
        }
        return true; // Continue processing the request
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // No action needed in this example
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // No action needed in this example
    }
}
