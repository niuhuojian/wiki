package demo.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger Log= LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Log.info("--------LogInterceptor开始--------");
        Log.info("请求地址：{} {}",request.getRequestURL().toString(),request.getMethod());
        Log.info("远程地址：{}",request.getRemoteAddr());

        long startTime=System.currentTimeMillis();
        request.setAttribute("requestStartTime",startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime=(Long) request.getAttribute("requestStartTime");
        Log.info("----------LogInterceptor结束 耗时：{}ms-------",System.currentTimeMillis()-startTime);
    }
}
