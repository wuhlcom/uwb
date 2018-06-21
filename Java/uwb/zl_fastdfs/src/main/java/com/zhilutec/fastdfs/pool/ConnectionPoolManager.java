package com.zhilutec.fastdfs.pool;


import org.springframework.context.annotation.Configuration;

/**
 *
 * @author sungao
 */
@Configuration
// 不被spring管理也可以使用
public class ConnectionPoolManager {

    private static FastDFSConnectionPool fastDFSConnectionPool;

    //private static FTPConnetctionPool pool;
    //private static LocalConnetctionPool pool;

    public static FastDFSConnectionPool initFastDFSPool() {
        fastDFSConnectionPool = new FastDFSConnectionPool();
        return fastDFSConnectionPool;
    }
}
