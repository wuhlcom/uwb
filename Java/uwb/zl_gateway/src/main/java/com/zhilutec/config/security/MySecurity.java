// package com.zhilutec.config.security;
//
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.ApplicationContextAware;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.web.util.matcher.RegexRequestMatcher;
// import org.springframework.security.web.util.matcher.RequestMatcher;
//
// import javax.servlet.http.HttpServletRequest;
// import java.util.regex.Pattern;
//
// @Configuration
// @EnableWebSecurity
// public class MySecurity extends WebSecurityConfigurerAdapter {
//     protected final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         logger.info("-----------------http config-------------------");
//         logger.info(http.toString());
//         http
//                 .authorizeRequests()
//                 .antMatchers("/gateway/api/**").permitAll()
//                 .anyRequest().permitAll()
//                 .and()
//                 .formLogin()
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/index.html")
//                 // .permitAll().and().csrf().disable();
//                 .permitAll().and().csrf().ignoringAntMatchers("/gateway/**");
//
//         // http.csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
//         //     private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
//         //     private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("gateway*", null);
//         //
//         //     @Override
//         //     public boolean matches(HttpServletRequest request) {
//         //         // No CSRF due to allowedMethod
//         //         if(allowedMethods.matcher(request.getMethod()).matches())
//         //             return false;
//         //
//         //         // No CSRF due to api call
//         //         if(apiMatcher.matches(request))
//         //             return false;
//         //
//         //         // CSRF for everything else that is not an API call or an allowedMethod
//         //         return true;
//         //     }
//         // });
//
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
//         // super.configure(web);
//         web.ignoring().antMatchers("/gateway/api/**");
//         // web.ignoring().antMatchers(".*");
//     }
//
// }