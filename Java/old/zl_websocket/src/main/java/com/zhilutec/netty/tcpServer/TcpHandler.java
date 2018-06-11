package com.zhilutec.netty.tcpServer;

import java.util.HashMap;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;

import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ChangeCharsetUtil;

import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.db.entities.RedisStatusEnitity;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.zhilutec.services.ITcpHandlerService;
import com.zhilutec.services.UwbWebSocket;

import static com.zhilutec.netty.tcpServer.HeartbeatHandlerInitializer.HEARTBEAT_SEQUENCE;

@Component
public class TcpHandler extends ChannelInboundHandlerAdapter {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 记录报警和在离线 同一对象读写会不会存在读写不一致问题
    protected static HashMap<Long, WarningStatusEntity> warningMap = new HashMap<>();
    // 记录最后一次的坐标
    protected static HashMap<Long, WarningStatusEntity> coordinateMap = new HashMap<>();
    // 状态信息
    protected static Map<Long, RedisStatusEnitity> statusMap = new HashMap<>();
    //报警数量统计
    protected static Integer WarningCount = 0;

    // Return a unreleasable view on the given ByteBuf
    // which will just ignore release and retain calls.
    // private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
    // .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
    // CharsetUtil.UTF_8));

    @Resource
    private ITcpHandlerService tcpHandlerService;

    public static TcpHandler tcpHandler;

    @PostConstruct
    public void init() {
        tcpHandler = this;
    }

    // private UwbWebSocket myWebsocket;
    public String message;

    // 连接建立时调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // String message = "Tcp server channel actived... ";
        // logger.info(message);
    }


    // 读取tcp client消息并返回响应
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChangeCharsetUtil strChange = new ChangeCharsetUtil();
        String receiveMsg = strChange.toUTF_8(msg.toString());
        String response = "Tcp server reply: " + receiveMsg;

        try {
            // tcp client将消息转为json对象
            JSONObject tcpMsgObj = JSON.parseObject(receiveMsg);

            /*** tcp应答 **/
            if (tcpMsgObj.getInteger("type").intValue() == 4) {
                ctx.writeAndFlush("{\"type\":4,\"ping\":\"pong\"}");
            } else {
                ctx.writeAndFlush(response);
                // 坐标换算
                String receiveMsgNew = tcpHandler.tcpHandlerService.convert(receiveMsg);
                // 在redis中保存状态
                tcpHandler.tcpHandlerService.saveStatus(receiveMsgNew);

                // 将处理过后的消息保存到map中
                tcpHandler.tcpHandlerService.saveMessage(receiveMsgNew);
                logger.info("------------------tcpMsgObj-----------------------");
                logger.info(receiveMsg);
                Long tcpTagId = tcpMsgObj.getLong("tag_id");
                if (tcpTagId == null) {
                    logger.info("-------------------tcp消息中不找不到tagI--------------------");
                }

                // 保存报警和离线消息
                tcpHandler.tcpHandlerService.saveMessage(tcpTagId);

                Integer op = tcpMsgObj.getInteger("op");
                if (op != null && op == 0) {
                    logger.info("------------------收到取消报警消息不推送数据-----------------------");
                } else {
                    WarningStatusEntity tcpData = TcpHandler.coordinateMap.get(tcpTagId);
                    Map<Session, String> sessionMap = UwbWebSocket.getSessionMap();
                    for (Map.Entry<Session, String> entry : sessionMap.entrySet()) {
                        // 获取当前websocket session对象
                        Session wsSession = entry.getKey();

                        if (wsSession.isOpen()) {
                            String wsMsg = entry.getValue().trim();
                            JSONObject wsMsgObj = JSON.parseObject(wsMsg);

                            // 请求类型
                            Integer wsType = wsMsgObj.getInteger("type");
                            if (wsType == 1) {
                                tcpHandler.tcpHandlerService.sendMultiAreaMsgNew(wsSession, wsMsgObj, tcpData);
                            } else if (wsType == 2) {
                                tcpHandler.tcpHandlerService.sendAreaMsg(tcpTagId, wsSession, wsMsgObj);
                            } else if (wsType == 3) {
                                tcpHandler.tcpHandlerService.sendPrisonerMsg(tcpTagId, wsSession, wsMsgObj, tcpData, tcpMsgObj);
                            }
                        } else {
                            sessionMap.remove(wsSession);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }

    // 获取客户端消息结束后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // logger.info("tcp server read message finished");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
        t.printStackTrace();
        ctx.close();
    }

    /**
     * 超时事件处理
     **/
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
             String type = "";
             if (event.state() == IdleState.READER_IDLE) {
             type = "read idle";
             } else if (event.state() == IdleState.WRITER_IDLE) {
             type = "write idle";
             } else if (event.state() == IdleState.ALL_IDLE) {
             type = "all idle";
             }

             // ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
             // logger.warn(ctx.channel().remoteAddress() + "超时类型：" + type);

            if (event.state() == IdleState.READER_IDLE) {
                ctx.channel().close();
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.channel().close();
                ctx.close();
            } else if (event.state() == IdleState.ALL_IDLE) {
                ctx.channel().close();
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}