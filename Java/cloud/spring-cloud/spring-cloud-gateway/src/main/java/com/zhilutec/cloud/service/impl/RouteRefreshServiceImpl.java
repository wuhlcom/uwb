package com.zhilutec.cloud.service.impl;

import com.zhilutec.cloud.service.IRouteRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RouteRefreshServiceImpl implements IRouteRefreshService {

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    RouteLocator routeLocator;

    //刷新路由
    @Override
    public void refreshRoute() {
        RefreshRoutesEvent routesRefreshedEvent = new RefreshRoutesEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}
