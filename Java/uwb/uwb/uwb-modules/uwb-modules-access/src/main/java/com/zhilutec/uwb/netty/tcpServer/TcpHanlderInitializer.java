package com.zhilutec.uwb.netty.tcpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TcpHanlderInitializer extends ChannelInitializer<Channel> {
    public static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));
    private static final int READ_IDEL_TIME_OUT = 60; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 60;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 60; // 所有超时
    // 自定义报文分隔符
    // 包分隔符回车 换行
    // LineBasedFrameDecoder lineDecoder = new LineBasedFrameDecoder(2048);
    // 包分隔符回车 换行
    // ByteBuf[] delimiters = Delimiters.lineDelimiter();
    ByteBuf delimiter = Unpooled.copiedBuffer("*".getBytes());

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(2048, delimiter));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        /**心跳配置**/
        // pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
        //         WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
        /*心跳事件处理**/
        pipeline.addLast(new TcpHandler());
    }

}
