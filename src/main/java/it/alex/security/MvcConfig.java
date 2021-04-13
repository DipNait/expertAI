package it.alex.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

//@Configuration
public class MvcConfig{ // implements WebMvcConfigurer {

   // @Override
   public void addViewControllers(ViewControllerRegistry registry) {
       registry.addViewController("/home").setViewName("home");
       registry.addViewController("/").setViewName("home");
       registry.addViewController("/login").setViewName("login");
        registry.addViewController("/chart").setViewName("chart");

    }

    //@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //specifying static resource location for themes related files(css etc)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

}
