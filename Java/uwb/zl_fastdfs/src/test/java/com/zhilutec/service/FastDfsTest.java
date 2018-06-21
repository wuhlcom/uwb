package com.zhilutec.service;

import com.alibaba.fastjson.JSONArray;
import com.zhilutec.services.FileManage;
import com.zhilutec.services.IFastDFSFileService;
import org.csource.fastdfs.FileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FastDfsTest {


    public FastDfsTest() {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
    }

    @BeforeClass
    public static void setup() {

    }

    @Before
    public void begin() {

    }

    @Test
    public void uploadByPath() throws Exception {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        JSONArray rs = ifO.uploadFileByPath("e:/tmp/wangyi.png");
        System.out.println(rs);//        "group1\M00/00/00/wKgKplqWXXGASUr4AAAQIUZb104606.png"
    }

    @Test
    public void upload() throws Exception {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        String rs = ifO.uploadFile("e:/tmp/wangyi.png");
        System.out.println(rs);//        "group1\M00/00/00/wKgKplqWXXGASUr4AAAQIUZb104606.png"
    }

    @Test
    public void download() throws Exception {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        byte[] dfile = ifO.downloadFile("group1", "M00/00/00/wKgKplqWXXGASUr4AAAQIUZb104606.png");
        FileOutputStream fos = new FileOutputStream(new File("E:/tmp/downloads/wangyi.png"));
        fos.write(dfile);
        fos.flush();
        fos.close();
    }


    @Test
    public void getTracker() throws Exception {
        // FileManage.initFastDFSPool();
        // IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        // String rs = ifO.getTrackerUrl();
        // System.out.println(rs);
    }


    @Test
    public void downloadAndSave() {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        boolean rs = ifO.downloadFileByName("group1", "M00/00/00/wKgKplqWlfKABKEZAAAQIUZb104003.png", "E:/tmp/downloads/wangyi2.png");
        System.out.println(rs);
    }

    @Test
    public void deleteFile() {
        // 静态调用过去 最后调用spring容器bean 但是没有容器初始化 所以NPE
        // 如果想要在spring之外使用spring容器 可以继承ApplicationContextAware拿到ApplicationContext 然后getBean
        // 或者将调用方法链上的对象都交给spring管理
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        String groupName = "group1";
        String remoteFileName = "M00/00/00/wKgKplqWlfKABKEZAAAQIUZb104003.png";
        boolean rs = ifO.deleteFileByName(groupName, remoteFileName);
        System.out.println(rs);
    }

    @Test
    public void getFileInfo() {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        String groupName = "group1";
        String remoteFileName = "M00/00/00/wKgKplqWlfKABKEZAAAQIUZb104003.png";
        FileInfo info = ifO.getFileInfo(groupName, remoteFileName);
        System.out.println(info.toString());
        System.out.println(info.getSourceIpAddr());
        System.out.println(info.getFileSize());
        System.out.println(info.getCrc32());
        System.out.println(info.getCreateTimestamp());
    }


    @Test
    public void queryFileInfo() {
        FileManage.initFastDFSPool();
        IFastDFSFileService ifO = FileManage.createtDefaultDevice();
        String groupName = "group1";
        String remoteFileName = "M00/00/00/wKgKplqWlfKABKEZAAAQIUZb104003.png";
        FileInfo info = ifO.queryFileInfo(groupName, remoteFileName);

        if (info != null) {
            System.out.println(info.toString());
            System.out.println(info.getSourceIpAddr());
            System.out.println(info.getFileSize());
            System.out.println(info.getCrc32());
            System.out.println(info.getCreateTimestamp());
        }
    }
}