package com.zhilutec;

import com.zhilutec.configs.TcpConfig;
import com.zhilutec.netty.tcpServer.TcpHanlderInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * server服务器 Created by wj on 2017/8/30.
 * SO_RCVBUF
 * Socket参数，TCP数据接收缓冲区大小。该缓冲区即TCP接收滑动窗口，linux操作系统可使用命令：cat
 * /proc/sys/net/ipv4/tcp_rmem查询其大小。一般情况下，该值可由用户在任意时刻设置，但当设置值超过64KB时，需要在连接到远端之前设置。
 * SO_SNDBUF
 * Socket参数，TCP数据发送缓冲区大小。该缓冲区即TCP发送滑动窗口，linux操作系统可使用命令：cat
 * /proc/sys/net/ipv4/tcp_smem查询其大小。
 * TCP_NODELAY
 * TCP参数，立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。
 * 该值设置Nagle算法的启用，改算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，如果需要发送一些较小的报文，则需要禁用该算法。Netty默认禁用该算法，从而最小化报文传输延时。
 * SO_KEEPALIVE
 * Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
 * SO_REUSEADDR
 * Socket参数，地址复用，默认值False。有四种情况可以使用：
 * (1).当有一个有相同本地地址和端口的socket1处于TIME_WAIT状态时，而你希望启动的程序的socket2要占用该地址和端口，比如重启服务且保持先前端口。
 * (2).有多块网卡或用IP Alias技术的机器在同一端口启动多个进程，但每个进程绑定的本地IP地址不能相同。
 * (3).单个进程绑定相同的端口到多个socket上，但每个socket绑定的ip地址不同。
 * (4).完全相同的地址和端口的重复绑定。但这只用于UDP的多播，不用于TCP。
 * SO_LINGER
 * Socket参数，关闭Socket的延迟时间，默认值为-1，表示禁用该功能。
 * -1表示socket.close()方法立即返回，但OS底层会将发送缓冲区全部发送到对端。
 * 0表示socket.close()方法立即返回，OS放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误。
 * 非0整数值表示调用socket.close()方法的线程被阻塞直到延迟时间到或发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错误。
 * IP_TOS
 * IP参数，设置IP头部的Type-of-Service字段，用于描述IP包的优先级和QoS选项。
 * ALLOW_HALF_CLOSURE
 * Netty参数，一个连接的远端关闭时本地端是否关闭，默认值为False。值为False时，连接自动关闭；
 * 为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
 */

@SpringBootApplication
@EnableEurekaClient
@EnableAsync
@ComponentScan("com.zhilutec")
public class Application implements CommandLineRunner {
    private static final int DEFAULT_MIN_SIZE = 64;
    private static final int DEFAULT_INITIAL_SIZE = 2048;
    private static final int DEFAULT_MAX_SIZE = 65536;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // 在main中添加多线程任务
        // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        // AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);
        //
        // for(int i = 0; i < 10; i++){
        //     asyncTaskService.executeAsyncTask(i);
        //     asyncTaskService.executeAsyncTaskPlus(i);
        // }

    }

    @Autowired
    private TcpConfig tcpConfig;

    @Override
    public void run(String... strings) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(tcpConfig.getBossThreadCount()); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup(tcpConfig.getWorkerThreadCount());
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new TcpHanlderInitializer())
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    // .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,
                            new AdaptiveRecvByteBufAllocator(DEFAULT_MIN_SIZE, DEFAULT_INITIAL_SIZE, DEFAULT_MAX_SIZE))
                    .option(ChannelOption.SO_BROADCAST, tcpConfig.isSoBroadcast()) // (4)
                    .option(ChannelOption.SO_REUSEADDR, tcpConfig.isSoReuseaddr())
                    .option(ChannelOption.SO_TIMEOUT, tcpConfig.getSoTimeout())
                    .option(ChannelOption.SO_RCVBUF, tcpConfig.getSoRcvbuf())
                    .option(ChannelOption.TCP_NODELAY, tcpConfig.isNodelay())
                    .option(ChannelOption.SO_SNDBUF, tcpConfig.getSoSndbuf())
                    .option(ChannelOption.SO_TIMEOUT, tcpConfig.getSoTimeout())
                    // 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .option(ChannelOption.SO_BACKLOG, tcpConfig.getSoBacklog()) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, tcpConfig.isSoKeepAlive()); // (6)

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(tcpConfig.getPort()).sync(); // (7)
            logger.info("Tcp server start listen at " + tcpConfig.getPort());
            // 等待服务器 socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
