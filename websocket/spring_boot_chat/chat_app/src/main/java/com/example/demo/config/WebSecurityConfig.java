package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .cors().and()
//                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/", "/stomp/**").permitAll()
//                .antMatchers("/secured/**", "/private/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
//                .and()
//                .formLogin().and()
//                .logout()
////                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/")
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID");
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .usernameParameter("username")
//                .passwordParameter("password");
//                .loginProcessingUrl("/authenticate");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8081")
//                .allowedMethods("*");
//    }

//        @Override
//    public void configure(WebSecurity web) throws Exception {
//
//        // Tell Spring to ignore securing the handshake endpoint. This allows the handshake to take place unauthenticated
//        web.ignoring().antMatchers("/stomp/**");
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    // Create an AuthenticationManager bean to Authenticate users in the ChannelInterceptor
//    @Bean
//    public AuthenticationManager authManager() throws Exception {
//        return this.authenticationManager();
//    }
}