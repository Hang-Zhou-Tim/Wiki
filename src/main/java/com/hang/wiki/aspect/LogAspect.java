package com.hang.wiki.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.hang.wiki.util.RequestContext;
import com.hang.wiki.util.SnowFlake;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /** Define An Aspect */
    @Pointcut("execution(public * com.hang.wiki.controller..*Controller.*(..))")
    public void controllerPointcut() {}

    @Resource
    private SnowFlake snowFlake;

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        // Generate Log Stream ID
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));

        // Begin printing log
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // Printing Request Connection Details
        LOG.info("------------- Begin -------------");
        LOG.info("Requested Address: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("Signature: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("Remote Address: {}", request.getRemoteAddr());

        RequestContext.setRemoteAddr(getRemoteIp(request));

        // Printing Request Parameter
        Object[] args = joinPoint.getArgs();
		// LOG.info("Requested Parameter: {}", JSONObject.toJSONString(args));

		Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // Exclude Sensitive Field/Keywords
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("Requested Parameter: {}", JSONObject.toJSONString(arguments, excludefilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();

        //Exclude sensitive fields
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("Result: {}", JSONObject.toJSONString(result, excludefilter));
        LOG.info("------------- Ends. Timing: {} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Find real address after proxy like Nginx
     * @param request
     * @return
     */
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
