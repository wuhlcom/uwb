package com.zhilutec.fastdfs.pool;


import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// @SuppressWarnings("SpringJavaAutowiringInspection")
// @DependsOn("fastDfsProp")
@Component
public class FastDFSConnectionPool {

    @Resource
    FastDFSProp fastDFSProp;

    @Resource
    HeartBeat heartBeat;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FastDFSConnectionPool.class);
    private static String defaultDevice = "FDFS";
    /**
     * 空闲的连接池
     */
    private static LinkedBlockingQueue<TrackerServer> IDLE_CONNECTION_POOL = new LinkedBlockingQueue<TrackerServer>();


    //在构建函数中初化会使依赖注入会在类初化之前
    // public FastDFSConnectionPool() {
    //     poolInit();
    //     HeartBeat beat = new HeartBeat(this);
    //     beat.beat();
    // }
    //

    //构造函数初始化,先于需要注入的bean,所以这里要把原来的在构造函数中的初始化注释掉
    //PostConstruct 注释用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化。
    @PostConstruct
    public void init() {
        poolInit();
        heartBeat.beat();
    }


    private void poolInit() {
        try {
            this.initClientGlobal();
            for (int i = 0; i < fastDFSProp.getMinPoolSize(); i++) {
                this.createTrackerServer();
            }
            LOGGER.info("[FASTDFS CONNECTION POOL初始化]--连接池大小为:{}",IDLE_CONNECTION_POOL.size());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[FASTDFS CONNECTION POOL初始化]--[异常:{}]", e);
        }
    }

    /**
     * 初始化配置
     */
    private void initClientGlobalStr() throws Exception {
        String propFile = "application.properties";
        //两种获取项目下配置文件路径的方法
        // String configFile  = ClassLoader.getSystemResource("fdfs_client.conf").getFile();
        // URL url = FastDFSConnectionPool.class.getResource("/fdfs_client.conf");
        // Resource resource = new ClassPathResource("fdfs_client.conf");
        // File file = resource.getFile();
        // String configFile = file.getAbsolutePath();
        try {
            ClientGlobal.initByProperties(propFile);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    //初始化fastdfs配置
    private void initClientGlobal() throws Exception {
        Properties props = new Properties();
        //注意这里要使用ClientGlobal.PROP_KEY_而不能使用ClientGlobal.CONF_KEY_
        //要与application中的一致
        props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, fastDFSProp.getTrackerServers());
        props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, fastDFSProp.getConnTimeout());
        props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, fastDFSProp.getNetTimeout());
        props.put(ClientGlobal.PROP_KEY_CHARSET, fastDFSProp.getCharset());
        props.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, fastDFSProp.getStealToken());
        props.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, fastDFSProp.getSecretKey());
        props.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, fastDFSProp.getTrackerHttpPort());
        try {
            ClientGlobal.initByProperties(props);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建TrackerServer,并放入空闲连接池
     */
    public void createTrackerServer() {
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            org.csource.fastdfs.ProtoCommon.activeTest(trackerServer
                    .getSocket());
            IDLE_CONNECTION_POOL.add(trackerServer);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("[创建TrackerServer(createTrackerServer)]--[异常：{}]", e);
        }
    }

    /**
     * 获取空闲连接
     *
     * @throws InterruptedException
     */
    public TrackerServer achieve() throws InterruptedException {

        TrackerServer trackerServer = IDLE_CONNECTION_POOL.poll(fastDFSProp.getWaitTimes(),
                TimeUnit.SECONDS);
        if (trackerServer == null) {
            LOGGER.info("[trackerServer is NULL] ");
            this.createTrackerServer();
            trackerServer = IDLE_CONNECTION_POOL.poll(fastDFSProp.getWaitTimes(), TimeUnit.SECONDS);
        }
        LOGGER.info("[获取空闲连接成功]");
        return trackerServer;

    }

    /**
     * 释放繁忙连接
     *
     * @param trackerServer
     */

    public void release(TrackerServer trackerServer) {
        LOGGER.info("[释放当前连接[prams:" + trackerServer.getInetSocketAddress().getAddress().getHostName() + "] ");
        if (trackerServer != null) {
            IDLE_CONNECTION_POOL.add(trackerServer);
        }
    }

    /**
     * 删除不可用的连接
     *
     * @param trackerServer
     */
    public synchronized void drop(TrackerServer trackerServer) {
        LOGGER.info("[删除不可用连接方法(drop)][parms:" + trackerServer.getInetSocketAddress().getAddress().getHostName() + "] ");
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (IOException e) {
                LOGGER.info("[删除不可用连接方法(drop)--关闭trackerServer异常][异常：{}]", e);
                e.printStackTrace();
            }
        }
    }

    public LinkedBlockingQueue<TrackerServer> getIdleConnectionPool() {
        return IDLE_CONNECTION_POOL;
    }



    public void addTrackerServer(TrackerServer trackerServer) {
        float poolPercentage = (float) (IDLE_CONNECTION_POOL.size()) / fastDFSProp.getMinPoolSize() * 100;
        //获取格式化对象
        // NumberFormat nt = NumberFormat.getPercentInstance();
        // //设置百分数精确度2即保留两位小数
        // nt.setMinimumFractionDigits(2);
        // if (poolPercentage < 0.7) {
        //     this.createTrackerServer();
        // }
        IDLE_CONNECTION_POOL.add(trackerServer);
    }

}

