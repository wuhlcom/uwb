package com.zhilutec.netty.tcpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明：心跳服务器初始化
 *
 */
public class HeartbeatHandlerInitializer extends ChannelInitializer<Channel> {

	private static final int READ_IDEL_TIME_OUT = 4; // 读超时
	private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
	private static final int ALL_IDEL_TIME_OUT = 7; // 所有超时

	public static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
					CharsetUtil.UTF_8));

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
				WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
		pipeline.addLast(new HeartbeatServerHandler());
	}
}