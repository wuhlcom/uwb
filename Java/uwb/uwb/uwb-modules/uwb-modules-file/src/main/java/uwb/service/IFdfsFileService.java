package uwb.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

public interface IFdfsFileService {


	Integer addDb(String jsonStr);

	String deleteDb(JSONObject jsonObject);

	@Transactional
	String updateDb(JSONObject jsonObject);

	String getFilesRs(JSONObject jsonObject);

	@Transactional
	String mapSwitch(JSONObject jsonObject);
}
