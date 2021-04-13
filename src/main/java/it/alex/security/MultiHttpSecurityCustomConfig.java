package it.alex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class MultiHttpSecurityCustomConfig {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN");
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/files/upload")
                    .authorizeRequests().anyRequest().hasRole("ADMIN")
                    .and()
                    .httpBasic().and().csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class ApiWebSecurityConfigurationAdapter2 extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/files/list")
                    .authorizeRequests().anyRequest().hasRole("ADMIN")
                    .and()
                    .httpBasic().and().csrf().disable();
        }
    }

    @Configuration
    @Order(3)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/files/listUser").hasRole("USER")
                    .antMatchers("/chart/").hasRole("USER")
                    .antMatchers("/home/").hasRole("USER")
                    .antMatchers("/login*").permitAll()
                    .anyRequest().authenticated().and().formLogin();
        }
    }
}
