package com.zhilutec.netty.tcpServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.pojos.UpgradeInfo;
import com.zhilutec.common.pojos.VersionInfo;
import com.zhilutec.common.utils.ChangeCharsetUtil;
import com.zhilutec.kafka.producer.KafkaProducerListener;
import com.zhilutec.services.IUpgradeService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class TcpHandler extends ChannelInboundHandlerAdapter {
  
    public static TcpHandler tcpHandler;
    //保存tcp session
    private static Map<String, Channel> SESSION_CH_MAP = new ConcurrentHashMap<String, Channel>();
    private static VersionInfo VERSION_INFO = new VersionInfo();
    private static UpgradeInfo UPGRADE_INFO = new UpgradeInfo();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IUpgradeService upgradeService;

    @Value("${spring.kafka.producer.topic}")
    private String kafkaTopic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public static UpgradeInfo getUpgradeInfo() {
        return UPGRADE_INFO;
    }

    public static void setUpgradeInfo(UpgradeInfo upgradeInfo) {
        UPGRADE_INFO = upgradeInfo;
    }

    public static Map<String, Channel> getSessionChMap() {
        return SESSION_CH_MAP;
    }

    public static void setSessionChMap(Map<String, Channel> SESSION_CH_MAP) {
        TcpHandler.SESSION_CH_MAP = SESSION_CH_MAP;
    }

    public static VersionInfo getVersionInfo() {
        return VERSION_INFO;
    }

    public static void setVersionInfo(VersionInfo versionInfo) {
        VERSION_INFO = versionInfo;
    }

    public static TcpHandler getTcpHandler() {
        return tcpHandler;
    }

    @PostConstruct
    public void init() {
        tcpHandler = this;
    }

    // 连接建立时调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String message = "Tcp server channel actived... ";
        Channel channel = ctx.channel();
        ChannelId channelId = channel.id();
        SESSION_CH_MAP.put(channelId.asShortText(), channel);
    }

    // 读取tcp client消息并返回响应
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Long timeMillis = System.currentTimeMillis() / 1000;
       ChangeCharsetUtil strChange = new ChangeCharsetUtil();
        String receiveMsg = strChange.toUTF_8(msg.toString());
        try {
            // tcp client将消息转为json对象
            JSONObject tcpMsgObj = JSON.parseObject(receiveMsg);
            //添加消息产生时间
            // tcpMsgObj.put("timestamp", timeMillis);
            /*** tcp应答 **/
            if (tcpMsgObj.getInteger("type").intValue() == 4) {
                ctx.writeAndFlush("{\"type\":4,\"ping\":\"pong\"}");
            } else if (tcpMsgObj.getInteger("type").intValue() == 5) {
                VERSION_INFO.setCmdType(tcpMsgObj.getString("cmd_type"));
                VERSION_INFO.setCmdDir(tcpMsgObj.getString("cmd_dir"));
                VERSION_INFO.setAncVer(tcpMsgObj.getString("anc_ver"));
                VERSION_INFO.setTagVer(tcpMsgObj.getString("tag_ver"));
                VERSION_INFO.setAncUpTime(tcpMsgObj.getLong("anc_up_time"));
                VERSION_INFO.setTagUpTime(tcpMsgObj.getLong("tag_up_time"));
                VERSION_INFO.setType(tcpMsgObj.getInteger("type"));
            } else if (tcpMsgObj.getInteger("type").intValue() == 6) {
                UPGRADE_INFO.setCmdType(tcpMsgObj.getString("cmd_type"));
                UPGRADE_INFO.setCmdDir(tcpMsgObj.getString("cmd_dir"));
                UPGRADE_INFO.setUpStatus(tcpMsgObj.getInteger("up_status"));
                UPGRADE_INFO.setType(tcpMsgObj.getInteger("type"));
                upgradeService.updateUpgradeStatus(UPGRADE_INFO);
            } else {
                tcpHandler.kafkaTemplate.send(tcpHandler.kafkaTopic, tcpMsgObj.toJSONString());
                //debug
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