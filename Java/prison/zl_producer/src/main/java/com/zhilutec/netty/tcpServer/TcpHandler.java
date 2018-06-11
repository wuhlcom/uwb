package com.zhilutec.netty.tcpServer;

import javax.annotation.PostConstruct;

import com.zhilutec.kafka.KafkaConfig;
import com.zhilutec.kafka.KafkaProducerConfig;
import com.zhilutec.kafka.KafkaProducerListener;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ChangeCharsetUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
public class TcpHandler extends ChannelInboundHandlerAdapter {

    public static TcpHandler tcpHandler;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;

    @PostConstruct
    public void init() {
        tcpHandler = this;
    }

    // 连接建立时调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // String message = "Tcp server channel actived... ";
        // logger.info(message);
    }

    // 读取tcp client消息并返回响应
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Long timeMillis = System.currentTimeMillis();
        ChangeCharsetUtil strChange = new ChangeCharsetUtil();
        String receiveMsg = strChange.toUTF_8(msg.toString());
        try {
            // tcp client将消息转为json对象
            JSONObject tcpMsgObj = JSON.parseObject(receiveMsg);

            /*** tcp应答 **/
            if (tcpMsgObj.getInteger("type").intValue() == 4) {
                ctx.writeAndFlush("{\"type\":4,\"ping\":\"pong\"}");
            } else {
                tcpHandler.kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC, msg.toString());
                tcpHandler.kafkaTemplate.setProducerListener(new KafkaProducerListener());
            }

            //丢弃数据
            // ((ByteBuf) msg).release();
            ReferenceCountUtil.release(msg);//ByteBuf.release()释放引用计数
            // Long timeMillis2 = System.currentTimeMillis();
            // Long value = (timeMillis2 - timeMillis);
            // logger.warn("kakfa producer 收到消息时间:" + timeMillis.toString());
            // logger.warn("kakfa producer 转发消息时间:" + timeMillis2.toString());
            // logger.warn("kakfa producer 转发消息用时:" + value.toString()+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            ReferenceCountUtil.release(msg);//ByteBuf.release()释放引用计数
            logger.error("Kafka producer 发送消息错误:" + e.getMessage());
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