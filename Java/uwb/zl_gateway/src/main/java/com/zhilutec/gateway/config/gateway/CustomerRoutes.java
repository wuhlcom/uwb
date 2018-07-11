package com.zhilutec.gateway.config.gateway;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.CompositeRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * 参考配置
 * http://www.baeldung.com/spring-cloud-gateway
 * <p>
 * - id: restfull_route
 * uri: http://192.168.10.232
 * predicates:
 * - Path=/uwb/resources/**
 * - id: websocket_route
 * uri: ws://192.168.10.232:80
 * predicates:
 * - Path=/uwb/websocket/**
 */

/**自定义路由**/
@Configuration
public class CustomerRoutes {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        RouteLocator routeLocator = builder.routes()
                .route("restfull_route2", r -> r.path("/uwb/resources/**")
                        .uri("http://192.168.10.232:80"))
                .route("websocket_route2", r -> r.path("/uwb/websocket/**")
                        .uri("http://192.168.10.232:80"))
                // .route("path_route", r -> r.path("/get")
                //         .uri("http://httpbin.org"))
                // .route("host_route", r -> r.host("*.myhost.org")
                //         .uri("http://httpbin.org"))
                // .route("rewrite_route", r -> r.host("*.rewrite.org")
                //         .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
                //                 "/${segment}"))
                //         .uri("http://httpbin.org"))
                // .route("hystrix_route", r -> r.host("*.hystrix.org")
                //         .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
                //         .uri("http://httpbin.org"))
                // .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                //         .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
                //         .uri("http://httpbin.org"))
                // .route("limit_route", r -> r
                //         .host("*.limited.org").and().path("/anything/**")
                //         .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                //         .uri("http://httpbin.org"))
                .build();
        return routeLocator;
    }

    // @Bean
    // RedisRateLimiter redisRateLimiter() {
    //     return new RedisRateLimiter(1, 2);
    // }

    //spring security路由安全相关的配置
    // @Bean
    // SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
    //     return http.httpBasic().and()
    //             .csrf().disable()
    //             .authorizeExchange()
    //             .pathMatchers("/anything/**").authenticated()
    //             .anyExchange().permitAll()
    //             .and()
    //             .build();
    // }
    //
    // @Bean
    // public MapReactiveUserDetailsService reactiveUserDetailsService() {
    //     UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
    //     return new MapReactiveUserDetailsService(user);
    // }

    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient,properties);
    }
}


