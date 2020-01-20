package com.example.redis.token.api.interpretor;

import com.example.redis.token.api.anno.ApiIdempotent;
import com.example.redis.token.api.common.rest.ResponseCode;
import com.example.redis.token.api.common.rest.RestResult;
import com.example.redis.token.api.common.utils.ConstantUtil;
import com.example.redis.token.api.config.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 接口幂等性拦截器
 *
 * @author
 */
@Slf4j
public class ApiIdempotentInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotent apiIdempotent = method.getAnnotation(ApiIdempotent.class);
        //判断拦截的该方法上是否使用了该注解，如果没有直接返回true
        if (apiIdempotent == null) {
            return true;
        }
        //校验token
        String token = request.getHeader(ConstantUtil.RequestHeader.TOKEN);
        if (StringUtils.isEmpty(token)) {
            log.info("token参数为空");
            response.getWriter().print(RestResult.error().setCode(ResponseCode.PARAM_IS_NULL.getCode()).setMsg("token参数为空").toJson());
            return false;
        }
//        token = request.getParameter(ConstantUtil.RequestHeader.TOKEN);
//        if (StringUtils.isEmpty(token)) {
//            log.info("token参数为空");
//            response.getWriter().print(RestResult.error().setCode(ResponseCode.PARAM_IS_NULL.getCode()).setMsg("token参数为空").toJson());
//            return false;
//        }
        //拼接token关键字
        String tokenKey = ConstantUtil.Redis.TOKEN_PREFIX.concat(token);
        //判断token:value的key值是否存在
        if (!redisCache.hasKey(tokenKey)) {
            //不存在，返回请勿重复操作异常
            log.info("redis中key不存在,请勿重复操作-" + tokenKey);
            response.getWriter().print(RestResult.error().setCode(ResponseCode.OPERATION_NON_REPEAT.getCode()).setMsg("请勿重复操作").toJson());
            return false;
        }
        //如果存在删除 key,并判断是否删除成功
        Long deleteCount = redisCache.delete(tokenKey);
        if (deleteCount <= 0) {
            log.info("redis中删除key失败,请勿重复操作-" + tokenKey);
            response.getWriter().print(RestResult.error().setCode(ResponseCode.OPERATION_NON_REPEAT.getCode()).setMsg("请勿重复操作").toJson());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
