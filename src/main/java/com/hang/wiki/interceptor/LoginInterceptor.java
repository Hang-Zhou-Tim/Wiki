package com.hang.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("------------- LoginInterceptor Starts -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);


        // Industrial Standard Option Request in Front/Backend Project.
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }

        String path = request.getRequestURL().toString();
        LOG.info("Path URL :{}", path);

        //Get header's token parameter
        String token = request.getHeader("token");
        LOG.info("Login Validation starts，token:{}", token);
        if (token == null || token.isEmpty()) {
            LOG.info( "Token is Empty!" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object object = redisTemplate.opsForValue().get(token);
        if (object == null) {
            LOG.warn( "Invalid token!" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            LOG.info("Already login: {}", object);
            //LoginUserContext.setUser(JSON.parseObject((String) object, UserLoginResp.class));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("------------- LoginInterceptor End Timing: {} ms -------------", System.currentTimeMillis() - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        LOG.info("LogInterceptor ends");
    }
}
