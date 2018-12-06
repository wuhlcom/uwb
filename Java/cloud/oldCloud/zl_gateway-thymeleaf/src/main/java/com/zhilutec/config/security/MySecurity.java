// package com.zhilutec.config.security;
//
//
// import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
// @EnableWebSecurity
// public class MySecurity extends WebSecurityConfigurerAdapter {
//
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         super.configure(http);
//         // http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
//         //         .anyRequest().hasRole("ENDPOINT_ADMIN")
//         //         .anyRequest().permitAll()
//         //         .and()
//         //         .httpBasic();
//
//         // http.csrf().disable();
//         // http.authorizeRequests()
//         //         .antMatchers("/api/", "/login/**", "/login/auth").permitAll()
//         //         .antMatchers("/admin/**").authenticated()
//         //         .and().rememberMe().tokenValiditySeconds(3600)
//         //         .and().formLogin().loginPage("/login")
//         //         .defaultSuccessUrl("/admin/miaroutes").permitAll()
//         //         .and()
//         //         .logout().logoutUrl("/admin/loginOut").permitAll();
//         // http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
//         //         .anyRequest().permitAll();
//     }
//
//     @Override
//     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//         super.configure(auth);
//         // auth.inMemoryAuthentication().withUser("zhilu").password("123456").roles("admin")
//         //         .and().withUser("test").password("123456").roles("admin");
//     }
//
//     @Override
//     public void configure(WebSecurity web) throws Exception {
//         super.configure(web);
//     }
//
//
// }