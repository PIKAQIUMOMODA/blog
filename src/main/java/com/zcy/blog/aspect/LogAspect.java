package com.zcy.blog.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect  {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zcy.blog.controller.*.*(..))")
    public void log() {}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint)
    {
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url=request.getRequestURL().toString();
        String ip=request.getRemoteAddr();
        String className=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args=joinPoint.getArgs();
        RequestLog requestLog=new RequestLog(ip,url,className,args);
        logger.info("Request:{}",requestLog.toString());
    }

    @After("log()")
    public void doAfter()
    {
     //   logger.info("----doAfter------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterRuturn(Object result)
    {
       logger.info("result {}",result);

    }

     private class RequestLog{
        private String ip;
        private String url;
        private String className;
        private Object[] args;

         public RequestLog(String ip, String url, String className, Object[] args) {
             this.ip = ip;
             this.url = url;
             this.className = className;
             this.args = args;
         }

         public String getIp() {
             return ip;
         }

         public void setIp(String ip) {
             this.ip = ip;
         }

         public String getUrl() {
             return url;
         }

         public void setUrl(String url) {
             this.url = url;
         }

         public String getClassName() {
             return className;
         }

         public void setClassName(String className) {
             this.className = className;
         }

         public Object[] getArgs() {
             return args;
         }

         public void setArgs(Object[] args) {
             this.args = args;
         }

         @Override
         public String toString() {
             return "RequestLog{" +
                     "ip='" + ip + '\'' +
                     ", url='" + url + '\'' +
                     ", className='" + className + '\'' +
                     ", args=" + Arrays.toString(args) +
                     '}';
         }
     }

}
