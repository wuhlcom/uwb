// package com.zhilutec.config.security;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
// import java.util.ArrayList;
// import java.util.List;
//
// @Configuration
// @EnableWebSecurity
// public class CsrfConfig {
//
//     @Bean
//     public CsrfSecurityRequestMatcher csrfSecurityRequestMatcher() {
//         CsrfSecurityRequestMatcher csrfSecurityRequestMatcher = new CsrfSecurityRequestMatcher();
//         List<String> urls = new ArrayList<>();
//         urls.add("/gateway/");
//         csrfSecurityRequestMatcher.setExecludeUrls(urls);
//         return csrfSecurityRequestMatcher;
//     }
// }
