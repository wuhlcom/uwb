package uwb.service;

import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFdfsClientService {

	String uploadFdfsFileRs(MultipartHttpServletRequest multipartRequest);

	String uploadFile(MultipartFile file) throws IOException;

	byte[] downloadFile(String fileUrl) throws IOException;

	void deleteFile(String filePath);

	FileInfo getFileInfo(String groupName, String path);

	StorePath uploadFileMF(MultipartFile file) throws IOException;

	Map<String, Object> uploadFdfsFileOne(MultipartHttpServletRequest multipartRequest);
   
}
