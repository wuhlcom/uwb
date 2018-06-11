package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.validators.FastDFSValidator;
import com.zhilutec.fastdfs.FastDFSFile;
import com.zhilutec.fastdfs.pool.FastDFSConnectionPool;
import com.zhilutec.services.IFastDFSFileService;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FastDFSFileServiceImpl implements IFastDFSFileService {

    private static Logger LOGGER = LoggerFactory.getLogger(FastDFSFileServiceImpl.class);

    @Autowired
    FileServiceImpl fileService;
    private FastDFSConnectionPool fastDFSConnectionPool;


    public FastDFSFileServiceImpl(FastDFSConnectionPool fastDFSConnectionPool) {
        this.fastDFSConnectionPool = fastDFSConnectionPool;
    }

    @Override
    public String uploadFile(String localFile) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String fso = null;
        try {
            String[] str = storageClient.upload_file(localFile, null,
                    new NameValuePair[]{});
            fso = str[0] + File.separator + str[1];
            this.release(trackerServer);
        } catch (Exception e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }

        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fso;
    }


    @Override
    public JSONArray uploadFileByPath(String localFile) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        JSONArray fsoArr = null;
        try {
            String[] uploadResult = storageClient.upload_file(localFile, null,
                    new NameValuePair[]{});
            fsoArr = (JSONArray) JSONArray.toJSON(uploadResult);
            this.release(trackerServer);
        } catch (Exception e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fsoArr;
    }

    @Override
    public String uploadFile(byte[] file_buff, String file_ext_name) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String fso = null;
        try {
            String[] str = storageClient.upload_file(file_buff, 0,
                    file_buff.length, file_ext_name, new NameValuePair[]{});
            fso = str[0] + File.separator + str[1];
            this.release(trackerServer);
        } catch (Exception e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fso;
    }

    @Override
    public String uploadFileByStream(InputStream inStream,
                                     String file_ext_name, long fileLength) {
        String fso = null;
        String[] results = null;
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            results = storageClient.upload_file(null, fileLength,
                    new UploadStream(inStream, fileLength), file_ext_name,
                    new NameValuePair[]{});
            fso = results[0] + File.separator + results[1];
            this.release(trackerServer);
        } catch (Exception e) {
            fso = null;
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fso;
    }

    /**
     * 图片上传接口
     **/
    private JSONObject uploadFdfsFile(MultipartHttpServletRequest multipartRequest) {
        MultipartFile multipartFile = multipartRequest.getFile("file");
        String name = multipartRequest.getParameter("name");
        String type = multipartRequest.getParameter("type");

        String[] fileAbsolutePath = null;
        JSONObject jsonObj = new JSONObject();
        try {
            String fileName = multipartFile.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            byte[] file_buff = null;
            InputStream inputStream = multipartFile.getInputStream();
            if (inputStream != null) {
                int len1 = inputStream.available();
                file_buff = new byte[len1];
                inputStream.read(file_buff);
            }
            inputStream.close();

            /**上传文件到文件服务器*/
            FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
            String[] uploadRs = this.uploadFdfsFile(file);
            if (uploadRs != null) {
                fileAbsolutePath = this.uploadFdfsFile(file);
            } else {
                return null;
            }

            /**将文件名映射到数据库*/
            if (fileAbsolutePath != null && fileAbsolutePath.length != 0) {
                String groupName = fileAbsolutePath[0];
                String remoteName = fileAbsolutePath[1];
                String remotePath = groupName + "/" + remoteName;
                FileInfo fileInfo = this.getFileInfo(groupName, remoteName);

                Long fileSize = fileInfo.getFileSize();
                Long createTime = fileInfo.getCreateTimestamp().getTime() / 1000;

                if (name == null || name == "")
                    name = fileName;

                Integer typeInt = Integer.parseInt(type);
                if (typeInt == 0) {
                    String lengthStr = multipartRequest.getParameter("length");
                    Double length = Double.parseDouble(Double.valueOf(lengthStr).toString());

                    String widthStr = multipartRequest.getParameter("width");
                    Double width = Double.parseDouble(Double.valueOf(widthStr).toString());

                    String picLengthStr = multipartRequest.getParameter("picLength");
                    Integer picLength = Integer.parseInt(Integer.valueOf(picLengthStr).toString());

                    String picWidthStr = multipartRequest.getParameter("picWidth");
                    Integer picWidth = Integer.parseInt(Integer.valueOf(picWidthStr).toString());
                    jsonObj.put("length", length);
                    jsonObj.put("width", width);
                    jsonObj.put("picLength", picLength);
                    jsonObj.put("picWidth", picWidth);
                }
                jsonObj.put("name", name);
                jsonObj.put("fileName", fileName);
                jsonObj.put("groupName", groupName);
                jsonObj.put("remoteName", remoteName);
                jsonObj.put("remotePath", remotePath);
                jsonObj.put("createdAt", createTime);
                jsonObj.put("size", fileSize);
                jsonObj.put("type", type);
                fileService.add(jsonObj.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    @Override
    public String uploadFdfsFileRs(MultipartHttpServletRequest multipartRequest) {
        //参数检查
        Result validator = FastDFSValidator.uploadVal(multipartRequest);
        if ((Integer) validator.get("errcode") != ResultCode.SUCCESS.getCode())
            return validator.toJSONString();

        JSONObject uploadRs = this.uploadFdfsFile(multipartRequest);
        if (uploadRs == null) {
            return Result.error("文件上传失败").toJSONString();
        } else {
            return Result.ok(ResultCode.SUCCESS.getCode(), "文件上传成功").toJSONString();
        }
    }


    @Override
    public String[] uploadFdfsFile(FastDFSFile file) {
        LOGGER.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());

        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            LOGGER.error("IO Exception when uploadind the file:" + file.getName(), e);
            return uploadResults;
        } catch (Exception e) {
            LOGGER.error("Non IO Exception when uploadind the file:" + file.getName(), e);
            return uploadResults;
        }
        LOGGER.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            LOGGER.error("upload file fail, error code:" + storageClient.getErrorCode());
            return uploadResults;
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];

        LOGGER.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return uploadResults;
    }

    @Override
    public Boolean deleteFile(String identifier, String expand_str) {
        String groupName = identifier.substring(0, identifier.indexOf("/"));
        String remoteFilename = identifier
                .substring(identifier.indexOf("/") + 1);
        return deleteFileByName(groupName, remoteFilename);
    }

    /**
     * 删除文件
     *
     * @param groupName      "group1"
     * @param remoteFilename ""
     */
    @Override
    public Boolean deleteFileByName(String groupName, String remoteFilename) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        int i = 1;
        try {
            i = storageClient.delete_file(groupName, remoteFilename);
            this.release(trackerServer);
        } catch (IOException | MyException e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return i == 0 ? true : false;
    }

    /**
     * 文件下载
     *
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws IOException
     * @throws MyException
     */
    @Override
    public byte[] downloadFile(String groupName, String remoteFileName) throws IOException, MyException {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return storageClient.download_file(groupName, remoteFileName);
    }

    /**
     * 文件下载
     *
     * @param identifier
     * @return
     */
    @Override
    public byte[] downloadFile(String identifier, long file_offset,
                               long download_bytes, String expand_str) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String group_name = identifier.substring(0, identifier.indexOf("/"));
        String remote_filename = identifier
                .substring(identifier.indexOf("/") + 1);
        byte[] b = null;
        try {
            b = storageClient.download_file(group_name, remote_filename,
                    file_offset, download_bytes);
            this.release(trackerServer);
        } catch (IOException | MyException e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return b;
    }

    @Override
    public Boolean downloadFile(String identifier, String target,
                                String expand_str) {
        String groupName = identifier.substring(0, identifier.indexOf("/"));
        String remoteFileName = identifier
                .substring(identifier.indexOf("/") + 1);
        return this.downloadFileByName(groupName, remoteFileName, target);
    }

    @Override
    public Boolean downloadFileByName(String groupName, String remoteFileName,
                                      String localFileName) {
        TrackerServer trackerServer = this.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        int i = 1;
        try {
            i = storageClient.download_file(groupName, remoteFileName,
                    localFileName);
            this.release(trackerServer);
        } catch (IOException | MyException e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }
        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return i == 0 ? true : false;
    }

    @Override
    public String downloadFileByNameRs(JSONObject jsonObject) {
        String groupName = jsonObject.getString("groupName");
        String remoteFileName = jsonObject.getString("fileName");
        String localFileName = jsonObject.getString("loalPath");
        Boolean rs = downloadFileByName(groupName, remoteFileName, localFileName);
        if (rs) {
            return Result.ok("文件下载成功").toJSONString();
        } else {
            return Result.error("文件下载失败").toJSONString();
        }
    }


    @Override
    public String getTrackerUrl() {
        String hostPort = this.getTrackerHost();
        String trackerUrl = null;
        if (hostPort != null) {
            trackerUrl = "http://" + hostPort;
        }

        return trackerUrl;
    }

    @Override
    public String getTrackerAddrUrl() {
        String addrPort = this.getTrackerAddr();
        String trackerUrl = null;
        if (addrPort != null) {
            trackerUrl = "http://" + addrPort;
        }

        return trackerUrl;
    }

    @Override
    public String getTrackerHost() {
        TrackerServer trackerServer = this.getTrackerServer();
        String trackerHost = null;
        try {
            trackerHost = trackerServer.getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
            this.release(trackerServer);
        } catch (Exception e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }

        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }

        return trackerHost;
    }

    @Override
    public String getTrackerAddr() {
        TrackerServer trackerServer = this.getTrackerServer();
        String trackerAdd = null;

        try {
            trackerAdd = trackerServer.getInetSocketAddress().getAddress() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
            this.release(trackerServer);
        } catch (Exception e) {
            this.drop(trackerServer);
            e.printStackTrace();
        }

        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return trackerAdd;
    }

    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) {
        TrackerServer trackerServer = null;
        FileInfo fileInfo = null;
        try {
            trackerServer = this.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            this.release(trackerServer);
            return fileInfo;
        } catch (IOException e) {
            LOGGER.error("IO Exception: Get File from Fast DFS failed", e);
            this.drop(trackerServer);
        } catch (Exception e) {
            LOGGER.error("Non IO Exception: Get File from Fast DFS failed", e);
            this.drop(trackerServer);
        }

        /*回收连接*/
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fileInfo;
    }

    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) {
        TrackerServer trackerServer = null;
        FileInfo fileInfo = null;
        try {
            trackerServer = this.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            fileInfo = storageClient.query_file_info(groupName, remoteFileName);
            this.release(trackerServer);
            return fileInfo;

        } catch (IOException e) {
            LOGGER.error("IO Exception: Get File from Fast DFS failed", e);
            this.drop(trackerServer);
        } catch (Exception e) {
            LOGGER.error("Non IO Exception: Get File from Fast DFS failed", e);
            this.drop(trackerServer);
        }
        if (trackerServer != null) {
            this.addTrackerServer(trackerServer);
        }
        return fileInfo;
    }


    private TrackerServer getTrackerServer() {
        try {
            return fastDFSConnectionPool.achieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getServer() {
        Map rsMap = new HashMap<>();
        TrackerServer trackerServer = this.getTrackerServer();
        if (trackerServer != null) {
            rsMap.put("serverIP", trackerServer.getInetSocketAddress().getHostString());
            rsMap.put("serverPort", ClientGlobal.getG_tracker_http_port());
            return Result.ok(rsMap).toJSONString();
        }
        return null;
    }


    private void release(TrackerServer trackerServer) {
        try {
            fastDFSConnectionPool.release(trackerServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drop(TrackerServer trackerServer) {
        try {
            fastDFSConnectionPool.drop(trackerServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTrackerServer(TrackerServer trackerServer) {
        fastDFSConnectionPool.addTrackerServer(trackerServer);
    }


}