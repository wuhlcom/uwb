package com.zhilutec.fastdfs.pool;


import org.springframework.context.annotation.Configuration;

/**
 *
 * @author sungao
 */
@Configuration
public class ConnectionPoolManager {

    private static FastDFSConnectionPool fastDFSConnectionPool;


    //private static TFPConnetctionPool pool;

    //private static LocalConnetctionPool pool;

    public static FastDFSConnectionPool initFastDFSPool() {
        fastDFSConnectionPool = new FastDFSConnectionPool();
        return fastDFSConnectionPool;
    }
}
