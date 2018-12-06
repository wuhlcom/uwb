package com.zhilutec.services;

import com.zhilutec.fastdfs.pool.ConnectionPoolManager;
import com.zhilutec.fastdfs.pool.FastDFSConnectionPool;
import com.zhilutec.services.impl.FastDFSFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

//不被spring管理也可以使用
//此类用于测试
@Configuration
public class FileManage {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileManage.class);

    private static String defaultDevice = "FDFS";

    // 如果后续有其他类型文件系统，可把使用接口
    private static FastDFSConnectionPool fastDFSConnectionPool;

    private static IFastDFSFileService fastDFSFileOperation;

    public static void initFastDFSPool() {
        fastDFSConnectionPool = ConnectionPoolManager.initFastDFSPool();
    }

    /**
     * 获取文件上传类
     *
     * @return FileOperation | null
     */
    public static IFastDFSFileService createtDefaultDevice() {
        if (null == fastDFSConnectionPool) {
            fastDFSConnectionPool = ConnectionPoolManager.initFastDFSPool();
            LOGGER.info("加载fastDFSConnectionPool：" + fastDFSConnectionPool);
        }
        switch (defaultDevice) {
            case "FDFS":
                if (null == fastDFSFileOperation) {
                    fastDFSFileOperation = new FastDFSFileServiceImpl(
                            fastDFSConnectionPool);
                    LOGGER.info("加载fastDFSFileOperation：" + fastDFSFileOperation);
                }
                break;
            case "FTP":

                // 未完待续
                break;
            default:
                break;
        }
        return fastDFSFileOperation;
    }

    public static String getDefaultDevice() {
        return defaultDevice;
    }

    public static void setDefaultDevice(String defaultDevice) {
        FileManage.defaultDevice = defaultDevice;
    }

}
