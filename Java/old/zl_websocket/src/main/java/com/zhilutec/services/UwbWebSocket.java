/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年11月7日 下午4:16:33 *
 */
package com.zhilutec.services;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.validators.WebsocketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by whl
 */

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/{wsRequest}")
@Component
public class UwbWebSocket {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    // 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<UwbWebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, Session> webSocketMap = new ConcurrentHashMap<String, Session>();
    private static Map<Session, String> sessionMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("wsRequest") String wsRequest, Session session, EndpointConfig config) throws IOException {
        //获取httpSession
//		HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//		httpSession.getAttribute(SessionName.USER);
        this.session = session;
        webSocketSet.add(this);
        webSocketMap.put(wsRequest, session);
        incrOnlineCount();

        // 向所有成员通知有客户端连接上来
        // for(UwbWebSocket item : webSocketSet){
        // if(!item.equals(this)) { //send to others only.
        // item.sendMessage("someone just joined in.");
        // }
        // }

        logger.info(wsRequest + " new connection...current online count: {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     *                {"tag_id":"00154","type":1}
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(@PathParam("wsRequest") String wsRequest, String message, Session session)
            throws IOException {
        logger.info("来自客户端的消息:" + message);
        Result rs = WebsocketValidator.wsTypeVal(message);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            this.sendMessage(rs.toJSONString());
            logger.info("请求参数错误修改后重发");
            //关闭连接
            //session.close();
        }
        // 保存session和客户端消息
        sessionMap.put(session, message);
        // 群发消息
        // for(UwbWebSocket item : webSocketSet){
        // item.sendMessage(message);
        // }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        // 同步发送
        // this.session.getBasicRemote().sendText(message);
        // 异步发送
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("wsRequest") String wsRequest) throws IOException {
        webSocketSet.remove(this);
        webSocketMap.remove(wsRequest);
        sessionMap.remove(session);
        decOnlineCount();

        // for (UwbWebSocket item : webSocketSet) {
        // item.sendMessage("someone just closed a connection.");
        // }
        logger.warn("one connection closed...current online count: {}", getOnlineCount());
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     * @throws IOException
     */
    @OnError
    public void onError(@PathParam("wsRequest") String wsRequest, Session session, Throwable error) throws IOException {
        webSocketSet.remove(this);
        webSocketMap.remove(wsRequest);
        sessionMap.remove(session);
        logger.info("Websocket连接发生错误");
        session.close();
        error.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return UwbWebSocket.onlineCount;
    }

    public static synchronized void incrOnlineCount() {
        UwbWebSocket.onlineCount++;
    }

    public static synchronized void decOnlineCount() {
        UwbWebSocket.onlineCount--;
    }


    public static synchronized CopyOnWriteArraySet<UwbWebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static synchronized void setWebSocketSet(CopyOnWriteArraySet<UwbWebSocket> webSocketSet) {
        UwbWebSocket.webSocketSet = webSocketSet;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static Map<Session, String> getSessionMap() {
        return sessionMap;
    }

    public static void setSessionMap(Map<Session, String> sessionMap) {
        UwbWebSocket.sessionMap = sessionMap;
    }

    public static Map<String, Session> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(Map<String, Session> webSocketMap) {
        UwbWebSocket.webSocketMap = webSocketMap;
    }

}
