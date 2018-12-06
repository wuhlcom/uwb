package com.zhilutec.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.fastdfs.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface IFastDFSFileService {

    /**
     * 上传文件地址
     *
     * @param local_file
     * @return
     */
    public abstract String uploadFile(String local_file);

    JSONArray uploadFileByPath(String localFile);

    /**
     * @param file_buff     文件字节流
     * @param *offset       偏移量
     * @param *length       字节长度
     * @param file_ext_name 文件后缀名
     * @return
     */
    public abstract String uploadFile(byte[] file_buff, String file_ext_name);


    public abstract String uploadFileByStream(InputStream inStream, String file_ext_name, long fileLength);

    String uploadFdfsFileRs(MultipartHttpServletRequest multipartRequest);

    String[] uploadFdfsFile(FastDFSFile file);

    /**
     * 删除
     *
     * @param identifier 文件标识
     * @param expand_str 扩展字段
     * @return
     */
    public abstract Boolean deleteFile(String identifier, String expand_str);

    Boolean deleteFileByName(String groupName, String remoteFilename);

    byte[] downloadFile(String groupName, String remoteFileName) throws IOException, MyException;

    /**
     * 下载
     *
     * @param identifier     文件标识
     * @param *target_url    下载模板路径
     * @param expand_str     扩展字段
     * @param file_offset
     * @param download_bytes file content/buff, return null if fail
     */
    public abstract byte[] downloadFile(String identifier, long file_offset, long download_bytes, String expand_str);


    /**
     * 下载
     *
     * @param identifier 文件标识
     * @param target_url 下载模板路径
     * @param expand_str 扩展str
     *                   file content/buff, return null if fail
     */
    public abstract Boolean downloadFile(String identifier, String target_url, String expand_str);


    Boolean downloadFileByName(String groupName, String remoteFileName,
                               String localFileName);


    String downloadFileByNameRs(JSONObject jsonObject);


    String getTrackerAddrUrl();


    String getTrackerAddr();

    FileInfo getFileInfo(String groupName, String remoteFileName);

    FileInfo queryFileInfo(String groupName, String remoteFileName);

    String getServer();

}
