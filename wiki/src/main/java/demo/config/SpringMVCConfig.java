package demo.config;

import demo.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.annotation.Resources;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;


    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**").
                excludePathPatterns("/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/listContent/**",
                        "/redis/**",
                        "/doc/vote/**",
                        "/snapshot/**");
    }
}
