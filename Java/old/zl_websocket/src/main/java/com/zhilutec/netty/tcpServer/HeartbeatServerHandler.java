package com.zhilutec.netty.tcpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * 说明：心跳服务器处理器
 *
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// Return a unreleasable view on the given ByteBuf
	// which will just ignore release and retain calls.
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
					CharsetUtil.UTF_8));  

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {

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

			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
					ChannelFutureListener.CLOSE_ON_FAILURE); 
			logger.info(ctx.channel().remoteAddress()+"超时类型：" + type);
			
			if (event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			} else if (event.state() == IdleState.WRITER_IDLE) {
				ctx.channel().close();
			} else if (event.state() == IdleState.ALL_IDLE) {
			    ctx.channel().close();
			}
			
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
