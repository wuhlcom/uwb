package uwb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uwb.common.validator.FdfsValidator;
import uwb.service.IFdfsClientService;
import uwb.service.IFdfsFileService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/*** fdfs clien接口 **/
@Component
@Service
public class FdfsClientServiceImpl implements IFdfsClientService {

	@Autowired
	private FastFileStorageClient storageClient;

	@Resource(name = "defaultGenerateStorageClient")
	private GenerateStorageClient defaultGenStorageClient;

	@Autowired
	private FdfsWebServer fdfsWebServer;

	@Autowired
	IFdfsFileService fdfsFileService;

	/**
	 * 上传单个文件到fdfs，并给文件添加一些额外属性，上传成功后添加数据记录
	 */
	@Override
	public Map<String, Object> uploadFdfsFileOne(MultipartHttpServletRequest multipartRequest) {
		MultipartFile multipartFile = multipartRequest.getFile("file");
		String alias = multipartRequest.getParameter("name");
		String type = multipartRequest.getParameter("type");
		JSONObject jsonObj = new JSONObject();

		try {

			String originalName = multipartFile.getOriginalFilename();
			byte[] file_buff = null;
			InputStream inputStream = multipartFile.getInputStream();
			if (inputStream != null) {
				int len1 = inputStream.available();
				file_buff = new byte[len1];
				inputStream.read(file_buff);
			}
			inputStream.close();

			/** 上传文件到文件服务器 */
			StorePath storePath = this.uploadFileMF(multipartFile);
			if (storePath == null) {
				return null;
			}

			/** 将文件名映射到数据库 */
			String fdfsGroup = storePath.getGroup();
			String fdfsPath = storePath.getPath();

			String remotePath = fdfsGroup + "/" + fdfsPath;

			FileInfo fileInfo = this.getFileInfo(fdfsGroup, fdfsPath);
			Integer createTime = fileInfo.getCreateTime() / 1000;

			// 别名为空时使用原始文件名
			if (alias == null || alias == "")
				alias = originalName;

			if (type != null) {
				Integer typeInt = Integer.parseInt(type);
				if (typeInt == 0) {
					String lengthStr = multipartRequest.getParameter("length");
					Double length = null;
					if (lengthStr != null) {
						length = Double.parseDouble(Double.valueOf(lengthStr).toString());
					}

					String widthStr = multipartRequest.getParameter("width");
					Double width = null;
					if (widthStr != null) {
						width = Double.parseDouble(Double.valueOf(widthStr).toString());
					}

					String picLengthStr = multipartRequest.getParameter("picLength");
					Integer picLength = null;
					if (picLengthStr != null) {
						picLength = Integer.parseInt(Integer.valueOf(picLengthStr).toString());
					}

					String picWidthStr = multipartRequest.getParameter("picWidth");
					Integer picWidth = null;
					if (picWidthStr != null) {
						picWidth = Integer.parseInt(Integer.valueOf(picWidthStr).toString());
					}
					jsonObj.put("length", length);
					jsonObj.put("width", width);
					jsonObj.put("picLength", picLength);
					jsonObj.put("picWidth", picWidth);
				}
			} else {
				type = "1";
			}

			jsonObj.put("name", alias);
			jsonObj.put("fileName", originalName);
			jsonObj.put("groupName", fdfsGroup);
			jsonObj.put("remoteName", fdfsPath);
			jsonObj.put("remotePath", remotePath);
			jsonObj.put("createdAt", createTime);
			jsonObj.put("size",  fileInfo.getFileSize());
			jsonObj.put("type", type);
			fdfsFileService.addDb(jsonObj.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	@Override
	public String uploadFdfsFileRs(MultipartHttpServletRequest multipartRequest){
		//参数检查
		Result validator = FdfsValidator.uploadVal(multipartRequest);
		if ((Integer) validator.get("errcode") != ResultCode.SUCCESS.getCode())
			return validator.toJSONString();

		Map<String,Object> uploadRs = this.uploadFdfsFileOne(multipartRequest);
		if (uploadRs == null) {
			return Result.error("文件上传失败").toJSONString();
		} else {
			return Result.ok(ResultCode.SUCCESS.getCode(), "文件上传成功").toJSONString();
		}
	}

	/** 上传文件,返回上传文件地址 */
	@Override
	public String uploadFile(MultipartFile file) throws IOException {
		StorePath storePath = uploadFileMF(file);
		return this.getFileFullUrl(storePath);
	}

	/** 上传文件，返回上传后文件信息 */
	@Override
	public StorePath uploadFileMF(MultipartFile file) throws IOException {
		return storageClient.uploadFile(file.getInputStream(), file.getSize(),
				FilenameUtils.getExtension(file.getOriginalFilename()), null);
	}

	/**
	 * 下载文件
	 *
	 * @param fileUrl
	 *            文件URL
	 * @return 文件字节
	 * @throws IOException
	 */
	@Override
	public byte[] downloadFile(String fileUrl) throws IOException {
		String group = fileUrl.substring(0, fileUrl.indexOf("/"));
		String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
		DownloadByteArray downloadByteArray = new DownloadByteArray();
		byte[] bytes = storageClient.downloadFile(group, path, downloadByteArray);
		return bytes;
	}

	// 封装文件完整URL地址
	private String getFileFullUrl(StorePath storePath) {
		String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
		return fileUrl;
	}

	/** 删除文件 */
	@Override
	public void deleteFile(String filePath) {
		storageClient.deleteFile(filePath);
	}

	// 注意group和path开头没有 "/"
	// {"groupName":"group1","path":"M00/00/00/Cmgw3FvS6GqAO7GsAAhuYZ4N61o025.png"}
	@Override
	public FileInfo getFileInfo(String groupName, String path) {
		return defaultGenStorageClient.queryFileInfo(groupName, path);
	}
}
