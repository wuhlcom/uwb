package com.zhilutec.configs;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * 从websocket中获取用户session
 *
 *
 */
public class HttpSessionConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
          HttpSession httpSession = (HttpSession) request.getHttpSession();
          sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}